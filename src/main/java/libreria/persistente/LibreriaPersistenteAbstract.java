package libreria.persistente;

import entities.Libro;
import ricerca.Filtro;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * LibreriaPersistenteAbstract
 */
public abstract class LibreriaPersistenteAbstract implements LibreriaPersistente{
    protected List<Libro> libri = null;
    protected boolean isBookAdded = false;
    protected int size = -1;
    protected boolean hasBeenModified = false;
    protected Libro libroDaEliminare = null;
    public LibreriaPersistenteAbstract(String pathFile) {
        if(pathFile == null || pathFile.isEmpty())
            throw new IllegalArgumentException("Devi impostare il filePath in ingresso");
    }
    /**
     * Metodo per la gestione dell'inizializzazione della struttura persistente.
     * @return true se l'inizializzazione si è conclusa con successo, false altrimenti
     */
    protected abstract boolean onInit();
    /**
     * Metodo per la gestione della chiusura della struttura persistente.
     * Esempio: si può gestire la chiusura della connessione del database.
     *
     */
    protected abstract void close();

    protected abstract void persist() throws IOException;

    @Override
    public String aggiungiLibro(Libro l) throws IOException {
        if(l != null) {
            libri.add(l);
            isBookAdded = true;
            persist();
            isBookAdded = false;
            return l.toString();
        }
        return null;
    }
    @Override
    public boolean modificaLibro(Libro libro, String ISBN) throws IOException {
        if (libro == null || ISBN == null) return false;
        int found = cercaLibroPerISBN(ISBN);
        if(found == -1) return false;
        libri.set(found, libro);
        hasBeenModified = true;
        persist();
        return true;
    }
    @Override
    public boolean eliminaLibro(Libro libro) throws IOException {
        if(libro == null) return false;
        int found = cercaLibroPerISBN(libro.isbn());
        if(found == -1) return false;
        libri.remove(found);
        hasBeenModified = true;
        persist();
        return true;
    }

    /**
     * Cerca un libro nella libreria utilizzando il suo ISBN.
     *
     * @param ISBN l'ISBN del libro da cercare. Non deve essere null.
     * @return l'indice del libro trovato nella lista dei libri se esiste,
     *         altrimenti restituisce -1 se il libro non è presente.
     */
    public int cercaLibroPerISBN(String ISBN) {
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
    public Collection<Libro> cerca(Filtro f) {
        return libri.stream().filter(f::filtro).toList();
    }
    public void setLibroDaEliminare(Libro l) {
        this.libroDaEliminare = l;
    }
}
