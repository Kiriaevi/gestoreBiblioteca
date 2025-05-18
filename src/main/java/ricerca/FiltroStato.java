package ricerca;

import entities.Libro;
import entities.Stato;

public class FiltroStato extends FiltroAbstract{
    private final Stato stato;
    public FiltroStato(Filtro filtro, Stato stato) {
        super(filtro);
        this.stato = stato;
    }
    @Override
    public boolean filtro(Libro libro) {
        return filtro.filtro(libro) &&
                libro.getStato().equals(stato);
    }
}
