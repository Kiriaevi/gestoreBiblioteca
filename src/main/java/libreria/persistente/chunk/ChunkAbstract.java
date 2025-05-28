package libreria.persistente.chunk;

import entities.Libro;
import entities.Pagina;
import libreria.persistente.LibreriaPersistenteAbstract;

import java.util.List;

public abstract class ChunkAbstract implements Chunk{
    protected int pagina = 0;
    public final static int CHUNK_SIZE = 24;
    protected int size = -1;
    protected final LibreriaPersistenteAbstract lib;
    protected final String fileName;
    public ChunkAbstract(LibreriaPersistenteAbstract lib, String filename) {
        this.lib = lib;
        this.fileName = filename;
    }
    @Override
    public List<Libro> leggi(Pagina richiesta) {
        size = lib.getSize();
        if(richiesta.equals(Pagina.ULTIMA)) {
           int paginaFinale = Math.floorDiv(size, CHUNK_SIZE);
           pagina = paginaFinale;
            return recuperaChunk(paginaFinale);
        }
        if(richiesta.equals(Pagina.PRIMA)) {
            pagina = 0;
            return recuperaChunk(0);
        }

        if(richiesta.equals(Pagina.PRECEDENTE) && pagina == 0)
            return recuperaChunk(pagina);
        int inizio = pagina * CHUNK_SIZE;
        if(inizio > size && richiesta.equals(Pagina.PROSSIMA)) {
            return List.of();
        }
        switch (richiesta) {
            case CORRENTE: break;
            case PRECEDENTE: pagina--; break;
            case PROSSIMA: pagina++; break;
        }
        return recuperaChunk(pagina);
    }

    public int cercaLibroPerISBN(String ISBN) {
        ChunkAbstract c = new ChunkCSV(fileName,lib);
        List<Libro> libri = leggiSequenzialmente(c);
        while(!libri.isEmpty()) {
            int cnt = 0;
            for(Libro l : libri) {
                if(l.isbn().equals(ISBN))
                    return cnt;
                cnt++;
            }
            libri = leggiSequenzialmente(c);
        }
        return -1;
    }
    protected abstract List<Libro> recuperaChunk(int pagina);
    protected abstract List<Libro> leggiSequenzialmente(ChunkAbstract c);

}
