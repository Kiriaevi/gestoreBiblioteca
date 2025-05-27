package libreria.memoria;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import entities.Libro;
import entities.Pagina;
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
    public boolean modificaLibro(Libro libroDaAggiungere, Libro libroDaEliminare) throws IOException {
        // Se abbiamo modificato l'ISBN allora controlla che quello nuovo non sia già presente in libreria
        if(libroDaAggiungere != null && libroDaEliminare != null &&
                !libroDaAggiungere.isbn().equals(libroDaEliminare.isbn())
                && bookExists(libroDaAggiungere.isbn())) return false;

        lib.setLibroDaEliminare(libroDaEliminare);
        return lib.modificaLibro(libroDaAggiungere, libroDaEliminare.isbn());
    }
    @Override
    public boolean eliminaLibro(Libro l) throws IOException {
        // il libro da eliminare deve esistere
        if(l != null && !bookExists(l.isbn())) return false;
        lib.setLibroDaEliminare(l);
        return lib.eliminaLibro(l);
    }
    @Override
    public boolean aggiungiLibro(Libro l) throws IOException {
        if(l != null && bookExists(l.isbn())) return false;
        lib.aggiungiLibro(l);
        return true;
    }
    @Override
    public boolean bookExists(String ISBN) {
        return lib.cercaLibroPerISBN(ISBN) != -1;
    }
    public Collection<Libro> cerca(Filtro f) {
        return lib.cerca(f);
    }
}
