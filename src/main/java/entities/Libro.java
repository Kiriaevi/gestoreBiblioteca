package entities;

public class Libro {
    private final String titolo, autore, ISBN, genere;
    private final int valutazione;
    private final Stato stato;

    public Libro(String titolo, String autore, String ISBN, String genere, int valutazione, Stato stato) {
        this.titolo = titolo.isEmpty() ? "Titolo sconosciuto" : titolo;
        this.autore = autore.isEmpty() ? "Autore sconosciuto" : autore;
        this.ISBN = ISBN.isEmpty() ? "ISBN non disponibile" : ISBN;
        this.genere = genere.isEmpty() ? "Genere non impostato" : genere;
        this.valutazione = Math.max(valutazione, 0);
        this.stato = stato == null ? Stato.DA_LEGGERE : stato;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getAutore() {
        return autore;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getGenere() {
        return genere;
    }

    public int getValutazione() {
        return valutazione;
    }

    public Stato getStato() {
        return stato;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "titolo='" + titolo + '\'' +
                ", autore='" + autore + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", genere='" + genere + '\'' +
                ", valutazione=" + valutazione +
                ", stato=" + stato +
                '}';
    }
}
