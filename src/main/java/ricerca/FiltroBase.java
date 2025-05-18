package ricerca;

import entities.Libro;

public class FiltroBase implements Filtro{
    @Override
    public boolean filtro(Libro libro) {
        return true;
    }
}
