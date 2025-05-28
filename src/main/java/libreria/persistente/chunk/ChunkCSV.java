package libreria.persistente.chunk;

import utility.Utility;
import entities.Libro;
import entities.Pagina;
import libreria.persistente.LibreriaPersistenteAbstract;
import ricerca.Filtro;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ChunkCSV extends ChunkAbstract{
    public ChunkCSV(String filename, LibreriaPersistenteAbstract lib) {
        super(lib, filename);
    }
    @Override
    protected List<Libro> recuperaChunk(int pagina) {
        int inizio = pagina * CHUNK_SIZE;
        int fine = Math.min(inizio + CHUNK_SIZE, size);
        return Utility.convertiLibroDaCSV(leggiChunk(inizio,fine));
    }
    private List<String> leggiChunk(int inizio, int fine) {
        LinkedList<String> ret = new LinkedList<>();
        int cnt = 0;
        try(BufferedReader bfr = new BufferedReader(new FileReader(fileName))){
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
        return ret;
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
    public void riscritturaCompletaDelFile(Libro libroDaEliminare) throws IOException {

        ChunkAbstract c = new ChunkCSV(fileName, lib);
        List<Libro> libri = leggiSequenzialmente(c);
        File tmpFile = new File("tmp_"+fileName);
        try(PrintWriter writer = new PrintWriter(new FileWriter(tmpFile))) {
            while(!libri.isEmpty()) {
                for(Libro libro : libri)
                    if(libroDaEliminare != null && !libro.equals(libroDaEliminare))
                        writer.println(Utility.convertiInCSV(libro));
                libri = leggiSequenzialmente(c);
            }
        }
        Files.move(tmpFile.toPath(), Path.of(fileName), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Legge la pagina corrente e manda avanti il cursore.
     * @param c, l'oggetto chunk che possiede le informazioni sul file in lettura
     * @return Lista di libri letti dal chunk alla pagina corrente
     */
    @Override
    protected List<Libro> leggiSequenzialmente(ChunkAbstract c) {
        List<Libro> ret = c.leggi(Pagina.CORRENTE);
        c.pagina++;
        return ret;
    }
}
