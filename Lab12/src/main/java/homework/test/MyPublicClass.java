public class MyPublicClass {

    public MyPublicClass() {}
    
    @MyTestAnnotation
    public void metodaFaraArgumente() {
        System.out.println("   [TEST-RUN] metodaFaraArgumente() a fost executata cu succes!");
    }
    
    @MyTestAnnotation
    public void metodaCuUnInt(int numar) {
        System.out.println("   [TEST-RUN] metodaCuUnInt() a fost executata. Valoare primita: " + numar);
    }

    
    public void metodaNeadnotata() {
        System.out.println("   [EROARE] Aceasta metoda nu ar fi trebuit sa fie apelata!");
    }

    
    @MyTestAnnotation
    public void metodaCuString(String text) {
        System.out.println("   [EROARE] Aceasta metoda are semnatura gresita si nu trebuie apelata!");
    }
}
