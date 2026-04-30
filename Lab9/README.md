# Laboratorul 9 - Bunny Maze Escape

### Notă: Deoarece am lucrat în cap cu gândul de a face aplicatia cu GUI de la început toate funcționalitățile de mai jos sunt implentate în pachetul „compulsory” .

## Compulsory

### 1. Modelul Orientat pe Obiecte (OOP)
Aplicația se bazează pe o arhitectură orientată pe obiecte clară pentru a simula un labirint interactiv în care entitățile ("Bunnies" și "Robots") se mișcă.
* Modelele de bază sunt descrise în pachetul `compulsory.maze`, unde clasa `Maze` reține toată starea logică a jocului.
* `Maze` stochează coordonatele țintei (`finish`), dicționarul de iepuri (`bunnies`), un vector de roboți (`robots`) și matricea `boolean[][] closedDoors` pentru a cunoaște structura pereților. De asemenea, `Coordonates` este un model de date simplu care înglobează rândul și coloana.
* Reprezentarea grafică separată pe panouri (`MazePane`, `BunniesPanel`, `RobotsPanel`) și clasele interne de logică respectă decuplarea stării de UI.

### 2. Explorarea Concurentă a Labirintului (Threads)
Am folosit pachetul `java.lang.Thread` pentru a permite iepurilor și roboților să acționeze concomitent.
* Entitățile au fost abstractizate în task-uri (clase care implementează `Runnable`): `BunnyTask` și `RobotTask`.
* În clasa principală `Main`, pentru fiecare panou de iepure (`BunnyPanel`) și de robot (`RobotPanel`), am instanțiat un obiect de tip `Thread`, transmițându-i task-ul aferent și adăugându-l în clasa mea de gestiune, `ExecutingController`. Metoda `startThreads()` lansează toate aceste fire de execuție simultan.
* Mișcarea propriu-zisă folosește metoda `makeStep(Coordonates whereTo)` în interiorul căreia entitatea ia decizia de a face un pas sau de a aplica un anumit verdict pe baza pozițiilor relative.

### 3. Sincronizarea Firelor de Execuție (Thread Synchronization)
Pentru a asigura accesul corect la memorie, controlul pauzei și actualizarea interfeței JavaFX (care nu este *thread-safe*), am folosit tehnici de sincronizare.
* **Controlul stării (Pause/Resume):** Am utilizat un bloc `synchronized` pe un obiect de lacăt dedicat: `executingController.getPauseLock()`. Dacă utilizatorul apasă "Stop" pe un robot/iepure, sau din panoul central, firul intră în repaus executând `executingController.getPauseLock().wait()`. Atunci când este pornit din nou, se trimite `notifyAll()` pentru a trezi thread-urile.
* **Sincronizarea Interfeței Grafice:** Toate modificările vizuale asupra instanțelor de labirint sau la elementele din UI se fac doar cu `Platform.runLater()`, pentru a trimite cererile pe thread-ul principal (JavaFX Application Thread) evitând excepțiile concurențiale.

### 4. Text-Based Display
Deși avem un GUI complet funcțional, pentru o vizualizare auxiliară și depanare, modelul `Maze` implementează o reprezentare text `drawMaze()`.
Aici, matricea este convertită într-un `StringBuilder` pe baza stării `closedDoors`, plasând caractere textuale ("+", "|", "--") pentru ziduri și ușile blocate, iar entitățile sunt reprezentate sub forma literelor/numărului lor ("B1", "R1", "✅") și publicate ulterior folosind `System.out.println`.

## Homework

### 1. Algoritm Sistematic de Explorare
Atât iepurii, cât și roboții se folosesc de o tehnică de vizibilitate în linie ("Hall Scanning") pentru luarea deciziilor, simulând vederea în lungul unui coridor (metoda `hallScanning`).
1. Preluăm coordonatele adiacente (sus, jos, stânga, dreapta) și validăm că o ușă este deschisă (`!closedDoors[...]`).
2. Se execută funcția recursivă `hallScanning` ce returnează un "scor" de atracție pentru direcția respectivă.
3. Iepurii sunt puternic atrași de punctul de final ("✅", scor +10) și puternic respinși de vederea unui robot (scor -5 sau -6). Ei încearcă să evite locațiile din memoria pe termen scurt (`bunniesMemory`, scor -0.2).
4. Roboții sunt atrași de locațiile iepurilor (+20).
   Dintre toate direcțiile validate, entitatea alege să se deplaseze în direcția cu scorul maxim (dacă sunt mai multe egale, face un salt folosind clasa `Random`).

### 2. Interacțiunea dinamică a vitezei și pauzei (Speed / Slow Down / Stop)
Prin interfața grafică am introdus posibilitatea de a manipula independent viteza de explorare a subiecților.
* **Panoul individual:** Fiecare `BunnyPanel` sau `RobotPanel` dispune de componenta `Spinner<Integer>` prin care se alege o viteză de la 1 la 5, precum și butoane locale de "Start" / "Stop".
* **Cum este controlat Time-Delay-ul:** În bucla `while` a fiecărui task, am implementat `Thread.sleep(delayMs)` unde valoarea depinde invers-proporțional de setarea din UI: `6000 - (1000 * speed)`. Astfel, un multiplicator mai mare micșorează perioada de staționare a thread-ului.
* **Control Global:** `ControlPanel`-ul central suprascrie controlul individual prin invocarea rutinelor de `disableMovement()` sau `enableMovement()` pe toată lista de instanțe ale roboților sau iepurilor.

### 3. Daemon Manager Thread (Contorul și Arbitrul Jocului)
Am implementat un "arbitru" prin `CowntDownTask` (instanțiat ca fir separat, pe un timp fix de x secunde, care gestionează oprirea jocului).
* Acest thread se execută în paralel și își desfășoară ciclul la fiecare secundă (`Thread.sleep(1000)`).
* El decrementează numărătoarea inversă vizibilă din interfață (prin re-desenarea modelului `CountDown`).
* Când expiră timpul sau când pe tablă nu a mai rămas niciun iepure liber, "Arbitrul" oprește forțat execuția tuturor celorlalte fire (`executingController.killThreadsExcluding(Thread.currentThread())`) și afișează, calculând `score`-ul general un mesaj JavaFX Alert cu câștigătorul ("Bunnies WON!" sau "Robots WON!") oprind apoi procesul.

## Advanced

### 1. Memoria Partajată (Shared Memory) și Evitarea Duplicării Muncii
Roboții colaborează menținând un *shared pool* al locațiilor anterioare vizitate (`Maze.robotsMemory`), folosită ca un tip special de date concurent (`Queue`) cu dimensiune limitată de `ROBOTS_MEMORY` (15 locații).
Când un robot scanează harta (prin metoda `hallScanning`), el depunctează (scor negativ de -0.2) drumurile care deja au fost vizitate recent de el sau de asociații săi, încurajându-se astfel o acoperire a hărții independentă. Similar, iepurii folosesc același tip de hartă a urmelor (`Maze.bunniesMemory`).

### 2. Mai mulți Iepuri și Sistemul de Eliminare Concurentă
Designul a fost construit capabil de a prelua, controla și reprezenta un număr extins de resurse de ambele tabere (ex. `numberBunnies = 2`, `numberRobots = 2` în `Main`).
Când două entități inamice ocupă aceeași coordonată:
* Jocul nu îngheață: entitatea care a provocat impactul blochează starea victimei modificând UI-ul (aplică `Status: Caught` sau `Status: Escaped`), apoi elimină cu succes victima din joc (apelând `Maze.eliminateBunny(bunnyName)`). Acest fapt validează ideea că firele continuă pentru ceilalți iepuri sau roboți. Punctajul de echipă de pe panouri va crește.