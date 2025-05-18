package ricerca;

import entities.Libro;

public class FiltroISBN extends FiltroAbstract{

    private final String ISBN;
    public FiltroISBN(Filtro filtro, String ISBN) {
        super(filtro);
        this.ISBN = ISBN.toUpperCase();
    }
    @Override
    public boolean filtro(Libro libro) {
        return filtro.filtro(libro) &&
                libro.getISBN().toUpperCase().contains(ISBN);
    }
}
