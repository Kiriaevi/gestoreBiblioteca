package ricerca;

import entities.Libro;

public class FiltroGenere extends FiltroAbstract{
    private final String genere;
    public FiltroGenere(Filtro filtro, String genere) {
        super(filtro);
        this.genere = genere;
    }

    @Override
    public boolean filtro(Libro libro) {
        return filtro.filtro(libro) &&
                libro.getGenere().contains(genere);
    }
}
