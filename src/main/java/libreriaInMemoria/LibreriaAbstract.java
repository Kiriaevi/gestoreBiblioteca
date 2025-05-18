package libreriaInMemoria;

import java.util.LinkedList;
import java.util.List;

import entities.Libro;
import loadingLibreria.LibreriaPersistente;
import loadingLibreria.LibreriaPersistenteAbstract;

public abstract class LibreriaAbstract implements Libreria {

    protected LibreriaPersistente lib = null;
    protected List<Libro> libri = new LinkedList<>();
    public LibreriaAbstract() {
    }
    protected abstract void onInit(String type);
    protected abstract void onClose();

    //FIXME: come sincronizzo le due liste? Ogni modifica fatta ad una va fatta all'altra
    // oppure cercare di fare aliasing.
    @Override
    public boolean modificaLibro(Libro l, String ISBN) {
        return lib.modificaLibro(l, ISBN);
    }
    @Override
    public boolean eliminaLibro(Libro l) {
        return lib.eliminaLibro(l);
    }
    @Override
    public void aggiungiLibro(Libro l) {
        lib.salvaLibro(l);
        System.out.println("IN MEMORIA: "+this.libri);
    }
    public List<Libro> getLibreria() { return this.libri; }
}
