package entities;

import java.io.Serializable;

public record Libro(String titolo, String autore, String isbn, String genere, int valutazione, Stato stato) implements Serializable {
    public Libro(String titolo, String autore, String isbn, String genere, int valutazione, Stato stato) {
        this.titolo = titolo.isEmpty() ? "Titolo sconosciuto" : titolo;
        this.autore = autore.isEmpty() ? "Autore sconosciuto" : autore;
        this.isbn = isbn.isEmpty() ? "ISBN non disponibile" : isbn;
        this.genere = genere.isEmpty() ? "Genere non impostato" : genere;
        this.valutazione = Math.max(valutazione, 0);
        this.stato = stato == null ? Stato.DA_LEGGERE : stato;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "titolo='" + titolo + '\'' +
                ", autore='" + autore + '\'' +
                ", ISBN='" + isbn + '\'' +
                ", genere='" + genere + '\'' +
                ", valutazione=" + valutazione +
                ", stato=" + stato +
                '}';
    }
}
