package loadingLibreria;

import entities.Libro;

import java.io.IOException;
import java.util.LinkedList;

/**
 * LibreriaPersistenteAbstract
 */
public abstract class LibreriaPersistenteAbstract implements LibreriaPersistente{

    protected final String[] template = {"titolo", "autore", "isbn", "genere", "valutazione", "stato"};
    protected LinkedList<String> libri = new LinkedList<>();
    protected final String fileName = "libri.csv";
    /**
     * Legge un singolo libro
     *
     */
    protected void leggiLibro() throws IOException {
        leggiLibro(1);
    }
}
