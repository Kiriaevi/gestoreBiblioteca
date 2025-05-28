package entities;

import utility.Utility;

import java.io.Serializable;
import java.util.Objects;

public record Libro(String titolo, String autore, String isbn, String genere, int valutazione, Stato stato) implements Serializable {
    public Libro(String titolo, String autore, String isbn, String genere, int valutazione, Stato stato) {
        this.titolo = titolo.isEmpty() ? "Titolo sconosciuto" : titolo;
        this.autore = autore.isEmpty() ? "Autore sconosciuto" : autore;
        this.isbn = isbn.isEmpty() ? Utility.generaISBNRandom() : isbn;
        this.genere = genere.isEmpty() ? "Genere non impostato" : genere;
        this.valutazione = valutazione > 5 ? 5 : Math.max(valutazione, 0);
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return Objects.equals(isbn, libro.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isbn);
    }
}
