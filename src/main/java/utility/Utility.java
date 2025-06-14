package utility;

import entities.Libro;
import entities.Stato;
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
            // Decido di ignorare le righe malformate, evitando così un'eccezione
            if(splitLibro.length < 6)
                continue;
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
     * Converte l'oggetto Libro fornito in una stringa formattata in CSV, dove ogni campo del Libro
     * è separato da virgole.
     *
     * @param libro l'oggetto Libro da convertire in una stringa formattata CSV
     * @return una stringa formattata come CSV che rappresenta i dettagli dell'oggetto Libro fornito
     */
    public static String convertiInCSV(Libro libro) {
        String sb = libro.titolo() +
                "," +
                libro.autore() +
                "," +
                libro.isbn() +
                "," +
                libro.genere() +
                "," +
                libro.valutazione() +
                "," +
                Utility.fromStatoToString(libro.stato());
        return sb;
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
