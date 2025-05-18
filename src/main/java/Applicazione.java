import entities.Libro;
import entities.Stato;
import libreriaInMemoria.Libreria;
import libreriaInMemoria.LibreriaImpl;
import org.w3c.dom.ls.LSOutput;
import ricerca.Filtro;
import ricerca.FiltroAutore;
import ricerca.FiltroBase;
import ricerca.FiltroTitolo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Applicazione {
    public static void main(String[] args) {
        LibreriaImpl libreria = new LibreriaImpl("csv");
        libreria.loadAll();
        System.out.println(libreria.getLibri(Integer.MAX_VALUE));
      //  System.out.println(libreria.getLibri(5));
        //Libro l = new Libro("学生 の学園", "sus", "12231-2112-12", "brainrot",5, Stato.LETTO);
        //libreria.aggiungiLibro(l); // <-- non posso richiamare modifica libro

       // Collection<Libro> libreriaData = libreria.getLibri(Integer.MAX_VALUE);
       // Filtro filtro = new FiltroAutore(new FiltroTitolo(new FiltroBase(), "学生"), "sus");
        //System.out.println(libreriaData.stream().filter(filtro::filtro).toList());
    }
}
