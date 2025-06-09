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
            throw new IllegalArgumentException("Non hai passato un nome libreria non valida");
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
        if(libroDaAggiungere == null || libroDaEliminare == null) return false;
        // Se abbiamo modificato l'ISBN allora controlla che quello nuovo non sia già presente in libreria
        if(!libroDaAggiungere.isbn().equals(libroDaEliminare.isbn())
                && bookExists(libroDaAggiungere.isbn())) return false;

        lib.setLibroDaEliminare(libroDaEliminare);
        return lib.modificaLibro(libroDaAggiungere, libroDaEliminare.isbn());
    }
    @Override
    public boolean eliminaLibro(Libro l) throws IOException {
        if(l == null) return false;
        // il libro da eliminare deve esistere
        if(!bookExists(l.isbn())) return false;
        lib.setLibroDaEliminare(l);
        return lib.eliminaLibro(l);
    }
    @Override
    public boolean aggiungiLibro(Libro l) throws IOException {
        if(l != null && bookExists(l.isbn())) return false;
        if(l == null) return false;
        lib.aggiungiLibro(l);
        return true;
    }
    @Override
    public boolean bookExists(String ISBN) {
        if(ISBN == null) return false;
        return lib.cercaLibroPerISBN(ISBN) != -1;
    }
    public Collection<Libro> cerca(Filtro f) {
        if(f == null) throw new IllegalArgumentException("Il filtro non è stato inizializzato");
        return lib.cerca(f);
    }
}
