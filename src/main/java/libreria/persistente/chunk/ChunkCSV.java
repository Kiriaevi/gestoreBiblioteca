package libreria.persistente.chunk;

import utility.Utility;
import entities.Libro;
import entities.Pagina;
import libreria.persistente.LibreriaPersistenteAbstract;
import ricerca.Filtro;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ChunkCSV extends ChunkAbstract{
    private final String fileName;
    public ChunkCSV(String filename, LibreriaPersistenteAbstract lib) {
        super(lib);
        this.fileName = filename;
        this.lib = lib;
    }
    @Override
    protected List<Libro> recuperaChunk(int pagina) {
        LinkedList<String> ret = new LinkedList<>();
        int inizio = pagina * CHUNK_SIZE;
        int fine = Math.min(inizio + CHUNK_SIZE, size);
        leggiChunk(inizio, fine, ret);
        return Utility.convertiLibroDaCSV(ret);
    }
    @Override
    public Collection<Libro> cerca(Filtro f) {
        List<Libro> ret = new LinkedList<>();
        Chunk chunk = new ChunkCSV(fileName, lib);
        if(super.size != -1) {
            ret.addAll(chunk.leggi(Pagina.CORRENTE).stream().filter(f::filtro).toList());
            List<Libro> next = chunk.leggi(Pagina.PROSSIMA);
            while(!next.isEmpty()) {
                ret.addAll(next.stream().filter(f::filtro).toList());
                next = chunk.leggi(Pagina.PROSSIMA);
            }
        }
        return ret;
    }
    public boolean leggiChunk(int inizio, int fine, LinkedList<String> ret) {
        int cnt = 0;
        try(BufferedReader bfr = new BufferedReader(new FileReader(fileName))){
            //TODO: controllare se l'ultimo libro viene letto (fine)
            for (int i = 0; i < fine; i++) {
                if(cnt < inizio) {
                    cnt++;
                    bfr.readLine();
                    continue;
                }
                ret.add(bfr.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}
