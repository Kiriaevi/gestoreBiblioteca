package utility;

import entities.Libro;
import entities.Stato;
import exceptions.DocumentoMalFormatoException;
import libreria.persistente.chunk.ChunkAbstract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Utility {
    /**
     * Il metodo di utility converte un array di libri in formato stringa in CSV.
     */
    public static List<Libro> convertiLibroDaCSV(List<String> libri) {
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
    /**
     * convertiInCSV
     * @param libro, il libro da convertire in una Stringa conforme allo standard CSV
     * @return la Stringa formattata in CSV che rappresenta il libro
     */
    public static String convertiInCSV(Libro libro) {
        StringBuilder sb = new StringBuilder();
        sb.append(libro.titolo());
        sb.append(",");
        sb.append(libro.autore());
        sb.append(",");
        sb.append(libro.isbn());
        sb.append(",");
        sb.append(libro.genere());
        sb.append(",");
        sb.append(libro.valutazione());
        sb.append(",");
        sb.append(Utility.fromStatoToString(libro.stato()));
        return sb.toString();
    }

    public static String generaISBNRandom() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 13; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

}
