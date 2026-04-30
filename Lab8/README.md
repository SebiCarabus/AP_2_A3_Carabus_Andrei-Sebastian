# Laboratorul 8

## Compulsory

### 1. Arhitectura principală a aplicației (JavaFX)
Pentru a rezolva cerința, am creat o interfață grafică folosind ecosistemul **JavaFX**. 
* Punctul de intrare în aplicație este clasa `Main`, care extinde `Application`. Am folosit un `BorderPane` ca layout principal pentru a asigura o organizare clară a ferestrei. 
* În partea de sus am plasat panoul de configurare (`ConfigPanel`), în centru am plasat tabla de desenare a labirintului (`MazePane`), iar în partea de jos se află panoul de control (`ControlPanel`).

### 2. Panoul de Configurare a Parametrilor
Am implementat clasa `ConfigPanel` extinzând un `HBox`. 
* Pentru a prelua dimensiunea labirintului de la utilizator, am utilizat componenta `Spinner<Integer>`. Acest lucru validează automat input-ul (valori între 2 și 10).
* Butonul "Draw" preia valoarea din `Spinner` și apelează metoda `resizeMaze()` din panoul central pentru a redesena structura.

### 3. Reprezentarea și Desenarea Labirintului (Canvas-ul)
Clasa `MazePane` (care extinde `GridPane`) reprezintă centrul aplicației unde va fi desenat labirintul.
* Labirintul este desenat ca o matrice extinsă, unde dimensiunea reală a grilei este `size * 2 + 1`. Acest lucru îmi permite să intercalez spațiile propriu-zise cu elemente de tip ușă (DOOR) și perete (WALL).
* Am folosit clasa ajutătoare `Cell` (extinde `StackPane`) pentru a modela vizual fiecare componentă a labirintului. Culoarea fiecărei celule se schimbă automat în constructor folosind CSS, în funcție de enumerarea `CellType` (ex: alb pentru ROOM, negru pentru WALL, verde pentru START, roșu pentru STOP, maro pentru ușile închise).

### 4. Panoul de Control și Modificarea Stării
Panoul `ControlPanel` gestionează butoanele de acțiune: *Create, Reset, Validate, Load, Save, Exit*.
* Butonul **Exit** închide complet execuția aplicației folosind apelul `System.exit(0)`.
* Butonul **Reset** redesenează labirintul la dimensiunea curentă, restabilind toți pereții și pozițiile inițiale prin reapelarea metodei `resizeMaze()`.
* Butonul **Create** simulează generarea aleatoare a pereților detașabili. Parcurge toate celulele de tip `DOOR` din `MazePane` și folosește instanța unui obiect `Random` pentru a determina dacă ușa va fi deschisă, schimbându-i aspectul vizual cu ajutorul metodei `updateAppearance()`.

## Homework

### 1. Editarea manuală a labirintului (Evenimente de Mouse)
Pentru a permite interacțiunea utilizatorului cu elementele grilei, am mapat evenimente de tip click pe celulele individuale:
* **Ușile (DOOR):** Am setat un `setOnMouseClicked` pe obiectul `Cell`. Când se apasă pe un perete de tip ușă, variabila booleană `isClosed` este inversată, iar culoarea se actualizează corespunzător (din "brown" în "bisque"), permițând crearea unui drum manual.
* **Punctele START și STOP:** Utilizatorul poate muta dinamic punctul de plecare făcând Click Stânga (`MouseButton.PRIMARY`) și punctul de finalizare cu Click Dreapta (`MouseButton.SECONDARY`) direct pe celulele de tip "ROOM". Labirintul se va redesena instantaneu preluând noile coordonate.

### 2. Validarea traseului (Algoritm de căutare a rutei)
Am adăugat funcționalitatea de a verifica dacă se poate ajunge de la START la STOP prin metoda `validate()`.
* **Algoritmul:** Am implementat metoda recursivă `validRoute` care funcționează ca o parcurgere în adâncime (DFS). Algoritmul pornește de la celula de START și se deplasează pe verticală și orizontală spre camere adiacente, verificând dacă elementul de tip DOOR dintre ele nu este închis.
* Pentru a nu intra într-un ciclu infinit, am folosit o matrice `boolean[][] visited`.
* **Feedback vizual:** În funcție de rezultatul algoritmului, aplicația afișează un dialog informativ folosind clasa `Alert` (afișează mesaj de succes dacă ruta validă de la START la STOP există, sau eroare dacă drumul este blocat).

### 3. Exportul labirintului într-un fișier PNG
Am implementat preluarea stadiului vizual al tablei de joc și stocarea acesteia pe disc ca imagine.
* Metoda `saveMazeAsPng()` din `Main` apelează `snapshot(null, null)` pe componenta `mazePane` pentru a genera un obiect `WritableImage`.
* Folosind utilitarul `SwingFXUtils.fromFXImage()`, imaginea capturată este exportată rapid prin clasa `ImageIO` în format PNG sub numele de `maze_lab8.png`.

### 4. Salvarea și Restaurarea Stării (Object Serialization)
Aplicația folosește API-ul nativ de Serializare din Java pentru a reține progresul utilizatorului și starea labirintului.
* **Structura de date:** Am creat clasa **`MazeState`** (care implementează interfața `Serializable`). Ea reține dimensiunea, coordonatele pentru START și STOP și o matrice `boolean[][] doorClosed` care reprezintă strict configurația ușilor salvate.
* **Salvare (`Save`):** Prin intermediul unui `ObjectOutputStream` împachetat peste un `FileOutputStream`, scriem obiectul `MazeState` pe disc în fișierul binar `maze_state.ser`.
* **Restaurare (`Load`):** Procesul invers folosește un `ObjectInputStream`. Citim entitatea serializată, preluăm atributele, iar metoda `loadMaze()` din `MazePane` reconstruiește structura GridPane-ului (`prepareMaze()`), lăsând deschise doar ușile care figurau ca atare în fișierul stocat.