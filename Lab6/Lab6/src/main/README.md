# PA laboratorul numărul 6

## Compulsory

### 1. Baza de date relațională și crearea tabelelor
Am ales **PostgreSQL** drept sistem de gestiune a bazelor de date (RDBMS). În loc să cer executarea manuală a unui script SQL, am automatizat procesul: schema bazei de date este generată automat la rulare prin metoda `Database.initaliseDatabase()`.
Scriptul SQL din cod creează următoarele tabele:
* `movies`: Conține `id`, `title`, `release_date`, `duration` și `score`.
* `genres`: Stochează categoriile de genuri (`id`, `name`).
* `actors`: Stochează detaliile actorilor (`id`, `name`).
* `movies_actors`: Un tabel asociativ (junction table) care gestionează relația Many-to-Many dintre filme și actori.
* `movies_genres`: Un tabel asociativ care leagă filmele de genuri. **Notă:** Pentru a îndeplini cerința specifică conform căreia *"un film are un singur gen"*, am marcat coloana `movie_id` din acest tabel cu `UNIQUE`. Astfel, am impus constrângerea de relație One-to-Many / One-to-One direct la nivelul bazei de date.

### 2. Configurarea Maven (Actualizare `pom.xml`)
Pentru a permite aplicației Java să comunice cu PostgreSQL, am adăugat driverul de baze de date la bibliotecile proiectului. Pentru a rula proiectul, fișierul `pom.xml` trebuie să includă următoarea dependență:

```xml
<dependencies>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.7.2</version> </dependency>
</dependencies>
```

### 3. Gestionarea conexiunii la baza de date (Singleton Pattern)
Clasa Database a fost implementată strict conform șablonului Singleton. Acest lucru asigură că există o singură instanță a managerului bazei de date pe tot parcursul rulării aplicației, prevenind blocajele și irosirea resurselor (resource leaks).

* Metoda getInstance() oferă acces global la obiectul Singleton.

* Metoda getConnection() se asigură că un singur obiect de tip Connection este refolosit. Dacă acea conexiune este null sau a fost închisă, metoda va inițializa una nouă. Funcția autoCommit este setată pe false pentru a-mi permite gestionarea manuală a tranzacțiilor.

### 4. Șablonul Data Access Object (DAO)
Clasa GenreDAO separă logica interogărilor bazei de date de restul programului. Oferă următoarele metode, folosind PreparedStatement pentru a asigura securitatea împotriva atacurilor de tip SQL Injection:
* create(String name): Inserează un gen nou în tabel și execută commit() pentru a salva tranzacția.
* findByName(String name): Interoghează baza de date și returnează id-ul unic al unui gen, căutând după numele acestuia.
* findById(int id): Returnează numele (name) unui gen pe baza cheii sale primare.

### 5. Testarea aplicației (Main.java)
Clasa Main reprezintă punctul de intrare în aplicație și testează funcționalitățile implementate:

1) Inițializarea bazei de date: Șterge tabelele existente (util pentru teste repetate prin metoda dropDatabase()) și le creează de la zero.
2) Inserarea datelor: Apelează GenreDAO pentru a adăuga două genuri ("Action" și "Adventure").
3) Interogarea datelor: Extrage și afișează în consolă numele genului aflat la ID-ul 2, precum și ID-ul genului "Action", confirmând că metodele DAO funcționează conform așteptărilor.
4) Curățarea mediului: Execută un script de tip DROP pentru a lăsa baza de date curată la final și închide conexiunea corect.

---

## Homework

### 1. Connection Pool (HikariCP)
Pentru a gestiona eficient conexiunile către baza de date, am renunțat la deschiderea/închiderea repetată a conexiunilor simple și am integrat **HikariCP**, unul dintre cele mai rapide și fiabile framework-uri de connection pooling din ecosistemul Java.
* Implementarea se află în clasa Singleton `Database`.
* La inițializarea instanței `Database`, am configurat un `HikariDataSource` folosind `HikariConfig`, setând un pool maxim de 10 conexiuni (`setMaximumPoolSize(10)`) și un timeout de 30 de secunde.
* Metoda `getConnection()` returnează acum o conexiune din acest pool, reducând masiv overhead-ul (timpul și resursele consumate) pentru interogările frecvente.

### 2. Modelul Orientat pe Obiecte (OOP)
Am creat un model de date complet pentru aplicație în pachetul `homework.objects`:
* Clasele `Actor`, `Genre` și `Movie` reprezintă entitățile din baza de date.
* Am folosit biblioteca **Lombok** (`@Getter`, `@Setter`, `@AllArgsConstructor`, `@ToString`) pentru a reduce codul de tip boilerplate (constructori, getteri/setteri) și a păstra clasele curate.
* **Sincronizare Activă:** Clasa `Movie` a fost extinsă cu metode inteligente (`setDuration`, `setScore`, `addGenre`, `addActor`). Când aceste metode sunt apelate în Java, ele invocă automat metodele din `MovieDAO` pentru a actualiza instantaneu starea bazei de date.

### 3. Implementarea completă a claselor DAO
Am dezvoltat clasele DAO (Data Access Object) pentru toate entitățile, separând logica de afaceri de interogările SQL:
* **`GenreDAO` și `ActorDAO`:** Implementează metodele de tip CRUD (`create`, `findByName`, `findById`). Am folosit `Statement.RETURN_GENERATED_KEYS` la inserare pentru a putea asocia imediat ID-ul generat de baza de date (prin PostgreSQL `SERIAL`) obiectului Java abia creat.
* **`MovieDAO`:** Este cel mai complex DAO. Pe lângă metodele standard de căutare și creare, conține metode specifice pentru tabelele asociative (`addActor`, `addGenre`) și metode de actualizare (`updateTitle`, `updateReleaseDate`, `updateDuration`, `updateScore`).
  Toate DAO-urile utilizează `PreparedStatement` și tranzacții manuale (`connection.commit()`) pentru securitate (prevenirea SQL Injection) și integritatea datelor.

### 4. Generarea unui raport HTML bazat pe un View din Baza de Date
Pentru raportare, am implementat un flux complet date-to-HTML:
* **Database View:** În metoda `Database.initaliseDatabase()`, scriptul SQL creează un view numit `movie_report_view`. Acesta face *JOIN-uri* între tabelele `movies`, `movies_genres` și `genres` pentru a denormaliza datele și a le pregăti pentru raport.
* **DTO (Data Transfer Object):** Am creat clasa `MovieReportRow` care mapează exact coloanele returnate de view-ul din baza de date. Metoda `Database.getMoviesForReport()` citește datele din view și returnează o listă de obiecte `MovieReportRow`.
* **Sistemul de Template-uri (FreeMarker):** În clasa `ReportService` am integrat **Apache FreeMarker**. Clasa ia lista de filme și o randează într-un fișier `movie_report.html` pe baza unui șablon predefinit (`report.ftl`).
* **Experiența Utilizatorului:** După ce fișierul HTML este generat pe disc, folosesc clasa `java.awt.Desktop` pentru a deschide automat raportul în browserul implicit al sistemului de operare.

### 5. Dependențe adăugate în `pom.xml`
Pentru ca acest cod să funcționeze, proiectul depinde de următoarele librării externe:
* **PostgreSQL Driver** (comunicarea cu DB)
* **HikariCP** (connection pooling)
* **Lombok** (generare de cod OOP)
* **FreeMarker** (motor de template-uri pentru HTML)

---

##  Advanced

### 1. Evoluția Bazei de Date (DB Migration cu Flyway)
Pentru a gestiona schimbările structurii bazei de date în timp, am integrat **Flyway**.
* În clasa `Main`, înainte ca orice altă operațiune să aibă loc, rulează codul `Flyway.configure()...migrate()`.
* Astfel, tabelele și view-urile nu mai sunt create dintr-un string Java (am renunțat la `Database.initaliseDatabase()`), ci sunt generate curat din scripturile SQL aflate în directorul `src/main/resources/db/migration`. Flyway ține evidența versiunilor și aplică doar modificările noi.

### 2. Importul datelor reale dintr-un dataset (CSV Parser)
Am folosit librăria **OpenCSV** pentru a citi fișierele din *The Movies Dataset* și **Jackson** (`ObjectMapper`) pentru a decoda structurile de tip JSON aflate în interiorul anumitor coloane din CSV.
* **`MovieImporter`:** Citește fișierul `movies_metadata.csv`, extrage informațiile de bază (titlu, dată, scor, etc.) și parsează dicționarul de genuri. Deoarece fișierul este imens, am limitat importul la primele 2000 de înregistrări valide pentru a nu bloca execuția.
* **`CreditsImporter`:** Citește fișierul `credits.csv`. Acesta este mai complex, deoarece actorii sunt stocați sub forma unui JSON array deghizat în string. Am curățat string-ul (`replace("'", "\"")`) pentru a-l transforma în JSON valid, am extras numele actorilor și le-am asociat cu filmele existente în DB.

### 3. Extinderea modelului de date (Liste de filme)
Am adăugat clasa `MovieList` în modelul obiectual (pachetul `importer/objects`).
* Conform cerinței, clasa are un `name` (numele listei), un `createdAt` (implementat folosind `java.sql.Timestamp` setat la `System.currentTimeMillis()`) și o listă de filme (`List<Movie>`). Aceasta ne ajută să stocăm rezultatul partiționării în memorie.

### 4. Algoritmul de partiționare a filmelor (Teoria Grafurilor)
Cerința menționează că două filme sunt *înrudite* dacă împart cel puțin un actor. Am fost provocat să împart filmele în liste de filme *neînrudite*, listele să fie cât mai puține, iar diferența de mărime dintre oricare două liste să fie de cel mult 1.

Acesta este un **problemă clasică de Colorare a Grafurilor (Graph Coloring)** combinată cu o problemă de **Balansare (Load Balancing)**. Am rezolvat-o în `MoviePartitionService` astfel:

**Pasul A: Reprezentarea grafului**
* Nodurile sunt filmele. Muchiile reprezintă actorii comuni.
* În `MovieDAO.getMovieAdjacencyList()`, am creat o interogare SQL care folosește un *Self-Join* (`ma1 JOIN ma2`) pe tabelul `movies_actors` pentru a găsi direct din baza de date ce filme împart același `actor_id`. Rezultatul este returnat ca o listă de adiacență: `Map<Integer, Set<Integer>>`.

**Pasul B: Colorarea Greedy (Minimizarea numărului de liste)**
* Am sortat filmele descrescător în funcție de numărul de vecini (grade).
* Pentru fiecare film, verific culorile (ID-urile de partiții) folosite de vecinii săi. Îi atribui filmului curent cea mai mică culoare nefolosită de vecini (`while (neighborColors.contains(color)) color++;`).
* Astfel, garantez că niciun film înrudit nu ajunge în aceeași listă, folosind un număr minim de liste (culori).

**Pasul C: Balansarea Partițiilor (Echilibrarea mărimii)**
* Odată formate listele (culorile), am implementat metoda `balancePartitions()`.
* Cât timp diferența dintre cea mai mare listă și cea mai mică este strict mai mare de 1, iau un film din lista cea mai mare și încerc să îl mut în lista cea mai mică.
* Mutarea se face doar dacă filmul trece de condiția `canPlaceInList` (adică nu are niciun vecin deja în lista mică). Procesul se repetă până se atinge echilibrul perfect sau nu se mai pot face mutări legale.

### 5. Modificări în `MovieDAO`
Singura clasă DAO modificată în această etapă a fost `MovieDAO`:
* `create(int id,String title)`: Deoarece în baza de date pe care o importăm actorii sunt asociați anumitor id-uri de filme trebuie să salvăm acum filmele cu id-urile explicite din baza de date din care importăm.
* `findAll()`: Pentru a aduce toate filmele din DB în memorie (necesar pentru algoritm).
* `getMovieAdjacencyList()`: Construiește lista de adiacență a grafului direct din interogări SQL, delegând munca grea motorului de baze de date PostgreSQL.