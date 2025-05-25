package libreria.memoria;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import comparators.OrdinamentoValutazione;
import entities.Libro;
import entities.Pagina;
import libreria.persistente.LibreriaPersistente;
import libreria.persistente.LibreriaPersistenteAbstract;
import ricerca.Filtro;

public abstract class LibreriaAbstract implements Libreria {

    protected LibreriaPersistenteAbstract lib = null;
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
    public void loadAll(Pagina richiesta) {
        try {
            libri = lib.leggiLibro(richiesta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Collection<Libro> getLibri(Pagina richiesta) {
        loadAll(richiesta);
        return libri;
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
    public Collection<Libro> cerca(Filtro f) {
        return lib.cerca(f);
    }
}
