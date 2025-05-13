package entities;

public class Libro {
    private final String titolo, autore, ISBN, genere;
    private final int valutazione;
    private final Stato stato;

    public static LibroBuilder builder() {
        return new LibroBuilder();
    }
    private Libro(String titolo, String autore, String ISBN, String genere, int valutazione, Stato stato) {
        this.titolo = titolo;
        this.autore = autore;
        this.ISBN = ISBN;
        this.genere = genere;
        this.valutazione = valutazione;
        this.stato = stato;
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
    public static class LibroBuilder {
        private String titolo = "";
        private String autore = "";
        private String ISBN = "";
        private String genere = "";
        private int valutazione = 0;
        private Stato stato = Stato.DA_LEGGERE;

        public LibroBuilder setTitolo(String titolo) {
            this.titolo = titolo;
            return this;
        }
        public LibroBuilder setAutore(String autore) {
            this.autore = autore;
            return this;
        }
        public LibroBuilder setISBN(String ISBN) {
            this.ISBN = ISBN;
            return this;
        }
        public LibroBuilder setGenere(String genere) {
            this.genere = genere;
            return this;
        }
        public LibroBuilder setValutazione(int valutazione) {
            this.valutazione = valutazione;
            return this;
        }
        public LibroBuilder setStato(Stato stato) {
            this.stato = stato;
            return this;
        }
        public Libro build() {
            return new Libro(titolo, autore, ISBN, genere, valutazione, stato);
        }
    }

}
