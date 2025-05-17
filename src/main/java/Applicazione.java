import entities.Libro;
import entities.Stato;
import libreriaInMemoria.Libreria;
import libreriaInMemoria.LibreriaImpl;

public class Applicazione {
    public static void main(String[] args) {
        LibreriaImpl libreria = new LibreriaImpl("csv");
        libreria.loadAll();
        System.out.println(libreria.getLibri(5));
        Libro l = new Libro("AMOGUS3", "sus", "12231-2112-12", "brainrot",5, Stato.LETTO);
        libreria.aggiungiLibro(l); // <-- non posso richiamare modifica libro
    }
}
