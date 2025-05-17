package libreriaInMemoria;

import java.util.LinkedList;
import java.util.List;

import entities.Libro;
import loadingLibreria.LibreriaPersistente;

public abstract class LibreriaAbstract implements Libreria {

    protected LibreriaPersistente lib = null;
    protected List<Libro> libri = null;
    public LibreriaAbstract(String type) {
        onInit(type);
    }
    protected abstract void onInit(String type);
    protected abstract void onClose();
    protected abstract void onChange();

}
