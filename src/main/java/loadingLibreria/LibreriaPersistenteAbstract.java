package loadingLibreria;

import entities.Libro;
import libreriaInMemoria.LibreriaAbstract;
import libreriaInMemoria.LibreriaImpl;

import java.util.LinkedList;
import java.util.List;

/**
 * LibreriaPersistenteAbstract
 */
public abstract class LibreriaPersistenteAbstract implements LibreriaPersistente{
    protected List<Libro> libri = null;
    protected List<Libro> nuoveAggiunte = new LinkedList<>();
    protected LibreriaAbstract libInMemoria = null;
    protected boolean hasBeenModified = false;
    protected int agggiunte = 0;
    protected int size = -1;
    public LibreriaPersistenteAbstract(LibreriaAbstract lib) {
        this.libInMemoria = lib;
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
        return this.libri;
    }

}
