import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import entities.Libro;

public abstract class LibreriaAbstract implements Libreria {
    private final List<Libro> libriTmp = new LinkedList<>();

    protected abstract void onInit();
    protected abstract void onClose();
    protected abstract void onChange();

}
