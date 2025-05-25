package libreria.persistente.chunk;

import entities.Libro;
import entities.Pagina;
import libreria.persistente.LibreriaPersistenteAbstract;

import java.util.List;

public abstract class ChunkAbstract implements Chunk{
    protected int pagina = 0;
    public final static int CHUNK_SIZE = 24;
    protected int size = -1;
    protected LibreriaPersistenteAbstract lib;
    public ChunkAbstract(LibreriaPersistenteAbstract lib) {
        this.lib = lib;
    }
    @Override
    public List<Libro> leggi(Pagina richiesta) {
        size = lib.getSize();
        if(richiesta.equals(Pagina.ULTIMA)) {
           int paginaFinale = Math.floorDiv(size, CHUNK_SIZE);
            return recuperaChunk(paginaFinale);
        }
        if(richiesta.equals(Pagina.PRIMA))
            return recuperaChunk(0);

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

    protected abstract List<Libro> recuperaChunk(int pagina);

}
