package loadingLibreria;

import entities.Libro;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * LibreriaPersistenteAbstract
 */
public abstract class LibreriaPersistenteAbstract implements LibreriaPersistente{
    protected List<Libro> libri = null;
    protected int aggiunte = 0;
    protected int size = -1;
    protected boolean hasBeenModified = false;
    public LibreriaPersistenteAbstract() {
    }
    /**
     * Metodo per la gestione dell'inizializzazione della struttura persistente.
     * @return true se l'inizializzazione si è conclusa con successo, false altrimenti
     */
    protected abstract boolean onInit();
    /**
     * Metodo per la gestione della chiusura della struttura persistente.
     * Esempio: si può gestire la chiusura della connessione del database o di possibili file aperti.
     *
     */
    protected abstract void close();

    protected abstract void persist();

    @Override
    public String salvaLibro(Libro l) {
        // per semplicità assumiamo che gli isbn siano diversi
        if(l != null) {
            libri.add(l);
            aggiunte++;
            return l.toString();
        }
        return null;
    }
    @Override
    public boolean modificaLibro(Libro libro, String ISBN) {
        if (libro == null || ISBN == null) return false;
        int found = cercaLibroPerISBN(ISBN);
        if(found == -1) return false;
        libri.set(found, libro);
        hasBeenModified = true;
        persist();
        return true;
    }
    @Override
    public boolean eliminaLibro(Libro libro) {
        if(libro == null) return false;
        int found = cercaLibroPerISBN(libro.isbn());
        if(found == -1) return false;
        libri.remove(found);
        hasBeenModified = true;
        persist();
        return true;
    }
    /**
     * Restituisce l'indice in cui si trova, se presente, il libro identificato da ISBN passato in ingresso.
     * @param ISBN, il libro da cercare
     * @return la posizione nella lista in cui si trova il libro se esiste, altrimenti -1
     */
    protected int cercaLibroPerISBN(String ISBN) {
        int found = -1;
        int cnt = 0;
        for (Libro l : libri) {
            if(l.isbn().equals(ISBN)) {
                found = cnt;
                break;
            }
            cnt++;
        }
        return found;
    }
    protected List<Libro> ordinaLibreria(List<Libro> libri, Comparator<Libro> comparator) {
        return libri.stream().sorted(comparator).collect(Collectors.toList());
    }
}
