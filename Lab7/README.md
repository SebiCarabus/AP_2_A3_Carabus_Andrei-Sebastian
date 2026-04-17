# Laboratorul 7

## Compulsory

### 1. Crearea și Inițializarea Proiectului Spring Boot
Pentru această etapă, am migrat arhitectura aplicației de laboratorul 6 către ecosistemul **Spring Boot**, ceea ce simplifică masiv configurarea serverului web și injectarea dependențelor (IoC - Inversion of Control).
* Punctul de intrare în aplicație este clasa `Main.java`, adnotată cu `@SpringBootApplication`. Această adnotare activează auto-configurarea și scanarea automată a componentelor (găsește automat clasele de tip `@Repository`, `@Service`, `@RestController`).
* **Popularea automată a bazei de date:** Pentru a avea date de testat la pornirea serverului, am creat un `@Bean` de tip `CommandLineRunner` în clasa `Main`. Acesta primește prin injecție de dependențe serviciile `MoviesImporter` și `CreditsImporter` și execută importul datelor din fișierele `.csv` imediat ce contextul Spring este complet încărcat.

### 2. Arhitectura REST Controller-ului
Pentru a expune funcționalitățile aplicației către exterior (în format web), am creat clasa `MoviesController` în pachetul `compulsory.web`.
* Am marcat clasa cu adnotarea `@RestController`. Spre deosebire de un simplu `@Controller`, aceasta combină logica de rutare cu `@ResponseBody`, ceea ce înseamnă că toate datele returnate de metodele noastre vor fi automat serializate în format **JSON** și scrise direct în corpul răspunsului HTTP.
* Am folosit `@RequestMapping("/api/v1/movies")` la nivel de clasa pentru a defini calea de bază a endpoint-ului.

### 3. Implementarea cererii HTTP GET
Conform cerinței, am implementat o metodă care returnează lista de filme din sistem.
* Am creat metoda `getAll()`, pe care am adnotat-o cu `@GetMapping`. Aceasta rutează automat orice request de tip HTTP GET primit pe adresa `/api/v1/movies` către această metodă.
* În interior, metoda comunică cu nivelul de persistență prin intermediul `movieDAO.findAll()`. DAO-ul nostru folosește acum **`JdbcTemplate`** (injectat cu `@Autowired`), care gestionează automat conexiunile și maparea rândurilor din baza de date în obiecte de tip `Movie`.

### 4. Utilizarea Șablonului DTO (Data Transfer Object)
Un detaliu arhitectural esențial este utilizarea clasei **`MovieDTO`** pentru returnarea datelor către client.
* **Motivația:** O entitate `Movie` poate conține referințe complexe către `Actor` sau `Genre`. Returnarea directă a entității ar putea cauza probleme de performanță sau bucle infinite la serializarea JSON.
* **Implementarea:** Metoda `getAll()` din controller transformă fiecare obiect `Movie` într-un `MovieDTO`. Constructorul `MovieDTO` extrage doar informațiile esențiale (titlu, data lansării, durata, scorul, numele genului și lista numelor actorilor), oferind un răspuns JSON curat și structurat.

### 5. Testarea serviciului (cu Postman)
Deoarece aplicația folosește Spring Boot, serverul Tomcat încorporat pornește implicit pe portul `8081`.

**Pași pentru testare:**
1. Rulăm aplicația din clasa `Main.java`.
2. În **Postman**, creăm un request nou cu metoda **GET**.
3. Introducem URL-ul: `http://localhost:8081/v1/movies`.
4. Apăsăm **Send**.

**Rezultatul așteptat:**
Vom primi un răspuns cu statusul `200 OK` și un corp JSON de forma:
```json
[
  {
    "id": 862,
    "title": "Toy Story",
    "releaseDate": "1995-10-30",
    "duration": 81,
    "score": 8.3,
    "genre": "Animation",
    "actors": [
      "Tom Hanks",
      "Tim Allen",
      "Don Rickles"
    ]
  }
]
```

## Homework

### 1. Implementarea Operațiilor CRUD (REST Controller)
Pentru a oferi un API complet, am extins clasa `MoviesController` cu noi rute (endpoints) care mapează celelalte acțiuni HTTP standard, transformând aplicația într-un serviciu web pe deplin funcțional:

* **Crearea unui film (`@PostMapping`):** Metoda `addNewMovie` primește un `MovieDTO` prin `@RequestBody`. Mai întâi, validează dacă filmul există deja (returnând codul HTTP `400 Bad Request` în caz afirmativ). Dacă nu, creează filmul și îi setează atributele de bază, asigurându-se că adaugă / asociază automat și genul sau actorii în baza de date.
* **Actualizarea completă (`@PutMapping`):** Metoda `updateMovie` înlocuiește un film existent. Găsește filmul vechi după titlu, îl șterge păstrându-i `id`-ul, și recreează filmul cu noile atribute.
* **Actualizarea parțială (`@PatchMapping`):** Pentru a modifica doar scorul unui film, am creat un Java Record special (`MovieScoreUpdate`). Această abordare este mult mai curată și eficientă, deoarece transferă doar datele strict necesare (titlul și noul scor), limitând consumul de resurse rețea.
* **Ștergerea unui film (`@DeleteMapping`):** Ruta `/{title}` primește titlul prin URL (folosind `@PathVariable`). Dacă filmul nu există, se trimite statusul `404 Not Found`. În caz contrar, filmul este șters, iar serverul răspunde cu statusul `200 OK` și un mesaj de succes.

### 2. Tratarea Globală a Excepțiilor
O aplicație robustă nu trebuie să "crape" și să returneze utilizatorilor acel stack-trace specific Java atunci când apare o eroare internă.
Pentru a gestiona elegant erorile, am implementat un Global Exception Handler:
* Am folosit clasa `GlobalExceptionHandler` adnotată cu `@RestControllerAdvice`. Aceasta funcționează ca un interceptor global; orice excepție neprinsă din orice Controller ajunge aici.
* Metoda `handleGeneralException` (adnotată cu `@ExceptionHandler(Exception.class)`) prinde erorile generale.
* Am creat un Java Record `ErrorResponse` (care conține `status`, `message` și `timestamp`). În loc de o eroare neformatată HTML, aplicația mea va trimite înapoi către client un JSON frumos structurat care descrie exact problema și momentul producerii ei, plus statusul `500 Internal Server Error`.

### 3. Aplicația Client (Spring Boot Client)
Pentru a demonstra funcționalitatea API-ului fără a depinde doar de teste manuale în Postman, am creat o a doua mică aplicație Spring Boot (`Lab7ClientApplication`).
* Logica se află în componenta `MovieClientApp`, care implementează interfața `CommandLineRunner`.
* Am utilizat **`RestClient`** (inclus în Spring Framework) pentru a realiza apeluri HTTP programatice către serverul meu principal (`http://localhost:8081/api/v1/movies`).
* La rulare, clientul execută automat un flux complet:
    1. Cere lista curentă de filme (GET) și afișează numărul lor.
    2. Adaugă un film nou, "Interstellar" (POST).
    3. Modifică scorul filmului "Interstellar" folosind recordul de actualizare (PATCH).
    4. Șterge filmul creat folosind titlul în URL (DELETE).
       Aceasta dovedește clar că rutele definite comunică perfect cu clienții externi.

### 4. Documentarea API-ului (Swagger)
Pentru documentare, metoda standard în ecosistemul Spring Boot este integrarea **OpenAPI/Swagger**.
* În fișierul `pom.xml`, am inclus dependența `springdoc-openapi-starter-webmvc-ui`.
* La simpla pornire a aplicației, framework-ul scanează toate `@RestController`-ele mele, analizează DTO-urile folosite (ca `MovieDTO` sau record-ul `MovieScoreUpdate`) și generează automat o pagină vizuală interactivă.
* Aceasta poate fi accesată direct în browser la adresa `http://localhost:8081/swagger-ui.html`. Interfața oferă nu doar documentația rutelor, parametrilor și tipurilor de date, ci și opțiunea de "Try it out" pentru a efectua cereri direct din browser.

## Advanced

### 1. Determinarea filmelor neînrudite folosind Constraint Programming (Choco-Solver)

Pentru a rezolva problema fără a scrie un algoritm de backtracking manual, am folosit **Choco-Solver**, un motor de rezolvare a problemelor de satisfacere a constrângerilor (Constraint Satisfaction Problem - CSP) din Java.
* **Controller-ul:** Am creat un endpoint nou, definit prin `@GetMapping("/unrelated/{min}")`. Acest endpoint extrage lista de adiacență a grafurilor din `MovieDAO` și o trimite mai departe către serviciu.
* **Modelarea constrângerilor (`MovieContraintService`):**
    1. **Variabilele:** Am creat un tablou de variabile booleene (`BoolVar[] selection`). Fiecare index reprezintă un film, unde `1` înseamnă "selectat" și `0` înseamnă "neselectat".
    2. **Constrângerea de neînrudire (Independent Set):** Am iterat prin toate perechile de filme. Dacă filmul `i` și filmul `j` se află în lista de adiacență reciprocă (împart un actor), am impus o constrângere aritmetică: `selection[i] + selection[j] <= 1`. Aceasta obligă solver-ul să nu le poată selecta pe amândouă simultan.
    3. **Constrângerea de cardinalitate:** Am adăugat condiția ca suma tuturor elementelor din vectorul `selection` să fie mai mare sau egală cu parametrul minim cerut: `model.sum(selection, ">=", minSize).post()`.
* **Rezolvarea:** Apelează `model.getSolver().solve()`. Dacă returnează `true`, iterez prin vectorul soluție și extrag doar filmele care au valoarea `1`, returnând lista către client în format DTO.

### 2. Securizarea API-ului cu JSON Web Tokens (JWT)
Pentru a proteja serviciile (astfel încât doar persoanele autorizate să poată face operații CRUD sau calcule costisitoare pe graf), am integrat **Spring Security** și am implementat un mecanism de autentificare *stateless* bazat pe JWT.

Implementarea este împărțită în 4 componente cheie (situate în pachetul `security` și `web`):

* **Configurarea Securității (`SecurityConfig`):** * Am dezactivat CSRF (deoarece folosim token-uri, nu cookie-uri de sesiune) și am setat politica de creare a sesiunilor pe `STATELESS`.
    * Am configurat regulile de acces: orice request către `/api/v1/auth/**` este permis (public), în timp ce `anyRequest().authenticated()` forțează ca absolut toate celelalte endpoint-uri (inclusiv serviciile de filme) să necesite un token valid.
    * Am injectat filtrul nostru custom (`JwtAuthFilter`) înainte de filtrul standard de validare a userului/parolei.

* **Controller-ul de Autentificare (`AuthController`):**
    * Expune endpoint-ul de login (`POST /api/v1/auth/login`).
    * Primește un payload JSON cu un username și o parolă. Pentru simplitatea demonstrației, le-am hardcodat (username: `"admin"`, password: `"parola123"`).
    * Dacă datele sunt corecte, generează un token JWT valid și îl trimite clientului sub cheia `token`, alături de tipul `Bearer`.

* **Utilitarul JWT (`JwtUtils`):** * Conține logica de generare, parcurgere și validare a token-ului folosind biblioteca `io.jsonwebtoken`.
    * Token-ul este semnat (`SignatureAlgorithm.HS256`) cu o cheie secretă complexă pentru a preveni falsificarea lui de către clienți. Expirația token-ului este setată la 24 de ore (86400000 ms).

* **Filtrul de Interceptare (`JwtAuthFilter`):**
    * Extinde `OncePerRequestFilter`, garantând că se execută o singură dată per request.
    * Extrage header-ul HTTP `Authorization`. Dacă acesta începe cu `"Bearer "`, decupează string-ul pentru a obține strict token-ul.
    * Îl validează folosind `JwtUtils`, extrage username-ul ("admin") și creează un obiect `UsernamePasswordAuthenticationToken` pe care îl stochează temporar în `SecurityContextHolder`. Acest lucru îi confirmă lui Spring Security că utilizatorul este autentificat.

### 3. Cum se testează secțiunea Advanced din Postman
Deoarece API-ul este acum complet securizat, testarea trebuie făcută în doi pași:

**Pasul A: Obținerea Token-ului**
1. Creează un request `POST` către `http://localhost:8081/api/v1/auth/login`.
2. În corpul (Body - raw JSON) request-ului, trimite:
   ```json
   {
       "username": "admin",
       "password": "parola123"
   }
   ```
3. Copiază valoarea string-ului aferent cheii `"token"` din răspuns.

**Pasul B: Accesarea resurselor protejate (ex. Filme Neînrudite)**
1. Creează un request `GET` către `http://localhost:8081/api/v1/movies/unrelated/{min}`.
2. Mergi în tab-ul **Authorization** din Postman, alege tipul **Bearer Token** și lipește token-ul copiat la pasul anterior.
3. Dacă token-ul este valid, motorul Choco-Solver se va activa și vei primi înapoi array-ul JSON cu filmele independente. Dacă uiți să pui token-ul, vei primi o eroare de tip `401 Unauthorized` sau `403 Forbidden`.