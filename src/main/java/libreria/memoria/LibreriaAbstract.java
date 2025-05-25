package libreria.memoria;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import comparators.OrdinamentoValutazione;
import entities.Libro;
import libreria.persistente.LibreriaPersistente;

public abstract class LibreriaAbstract implements Libreria {

    protected LibreriaPersistente lib = null;
    protected List<Libro> libri = new LinkedList<>();
    protected final String nomeStruturaPersistente;
    public LibreriaAbstract(String n) {
        if(n.isEmpty())
            throw new IllegalArgumentException("Non hai passato un libro valido");
        this.nomeStruturaPersistente = n;
    }
    protected abstract void onInit(String type);
    protected abstract void onClose();
    @Override
    public void loadAll() {
        try {
            libri = lib.leggiLibro(Integer.MAX_VALUE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Collection<Libro> getLibri(int size) {
        if (size < 0)
            throw new IllegalArgumentException("size non può essere un valore negativo");
        if(size > lib.getSize())
            return libri.stream().sorted(new OrdinamentoValutazione(true).ottieniComparatore()).toList();
        return libri.subList(0, size).stream().sorted(new OrdinamentoValutazione(true).ottieniComparatore()).toList();
    }
    @Override
    public boolean modificaLibro(Libro l, String ISBN) throws IOException {
        return lib.modificaLibro(l, ISBN);
    }
    @Override
    public boolean eliminaLibro(Libro l) throws IOException {
        return lib.eliminaLibro(l);
    }
    @Override
    public void aggiungiLibro(Libro l) throws IOException {
        lib.aggiungiLibro(l);
    }
}
