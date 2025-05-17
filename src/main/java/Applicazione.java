import libreriaInMemoria.Libreria;
import libreriaInMemoria.LibreriaImpl;

public class Applicazione {
    public static void main(String[] args) {
        LibreriaImpl libreria = new LibreriaImpl("csv");
        libreria.loadAll();
        System.out.println(libreria.getLibri(5));
    }
}
