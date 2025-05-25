package Utility;

import entities.Libro;
import entities.Stato;
import exceptions.DocumentoMalFormatoException;
import libreria.persistente.chunk.ChunkAbstract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utility {
    /**
     * Il metodo di utility converte un array di libri in formato stringa in CSV.
     * pre: libri è un array di dimensione al massimo pari a CHUNK_SIZE (default 20), ogni libro è non nullo
     * post: una lista immutabile contenente 20 libri.
     */
    public static List<Libro> convertiLibroDaCSV(List<String> libri) {
        if(libri.size() > ChunkAbstract.CHUNK_SIZE)
            throw new IllegalArgumentException("Ogni chunk può essere grande al MASSIMO 20");
        List<Libro> ret = new ArrayList<>();
        for(String l : libri) {
            if(l == null) continue;
            String[] splitLibro = l.split(",");
            if(splitLibro.length < 6)
                throw new DocumentoMalFormatoException("Il documento passato non è correttamente formattato, dovrebbero esserci 6 campi!");
            String titolo = splitLibro[0];
            String autor = splitLibro[1];
            String isbn = splitLibro[2];
            String genere = splitLibro[3];
            int valutazione = Integer.parseInt(splitLibro[4]);
            Stato stato = splitLibro[5].isEmpty() ? Stato.DA_LEGGERE : fromStringToStato(splitLibro[5]);
            ret.add(new Libro(titolo, autor, isbn, genere, valutazione, stato));
        }
        return Collections.unmodifiableList(ret);
    }
    public static Stato fromStringToStato(String input) {
        String s = input.toUpperCase();
        if(s.equals("DA LEGGERE"))
            return Stato.DA_LEGGERE;
        if (s.equals("IN LETTURA"))
            return Stato.IN_LETTURA;
        if(s.equals("LETTO"))
            return Stato.LETTO;
        return null;
    }
    public static String fromStatoToString(Stato input) {
        if(input.equals(Stato.DA_LEGGERE))
            return "DA LEGGERE";
        if(input.equals(Stato.IN_LETTURA))
            return "IN LETTURA";
        else return "LETTO";
    }
}
