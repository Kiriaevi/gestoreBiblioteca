package loadingLibreria;

import entities.Libro;

import java.util.LinkedList;
import java.util.List;

/**
 * LibreriaPersistenteAbstract
 */
public abstract class LibreriaPersistenteAbstract implements LibreriaPersistente{
    protected LinkedList<Libro> libri = new LinkedList<>();
    protected int size = -1;
    public LibreriaPersistenteAbstract() {
    }
    /**
     * Metodo per la gestione dell'inizializzazione della struttura persistente.
     * @return true se l'inizializzazione si è conclusa con successo, false altrimenti
     */
    protected abstract boolean onInit();
    /**
     * Metodo per la gestione della chiusura della struttura persistente.
     * @return true se la chiusura delle strutture persistenti viene effettuata con successo, false altrimenti
     */
    protected abstract void onClose();

    protected abstract void persist();

    public List<Libro> getLibri() {
        return libri;
    }

}
