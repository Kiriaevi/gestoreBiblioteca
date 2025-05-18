package ricerca;

import entities.Libro;

public class FiltroAutore extends FiltroAbstract{

    private final String autore;
    public FiltroAutore(Filtro filtro, String autore) {
        super(filtro);
        this.autore = autore.toUpperCase();
    }

    @Override
    public boolean filtro(Libro libro) {
        return filtro.filtro(libro) &&
                libro.getAutore().toUpperCase().contains(autore);
    }
}
