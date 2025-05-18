package ricerca;

import entities.Libro;

public class FiltroTitolo extends FiltroAbstract{
    private final String titolo;
    public FiltroTitolo(Filtro filtro, String titolo) {
       super(filtro);
       this.titolo = titolo.toUpperCase();
    }

    @Override
    public boolean filtro(Libro libro) {
        return  filtro.filtro(libro) &&
                libro.getTitolo().toUpperCase().contains(titolo);
    }
}
