package libreria.persistente.chunk;

import entities.Libro;
import entities.Pagina;
import ricerca.Filtro;

import java.util.Collection;
import java.util.List;

public interface Chunk {
    List<Libro> leggi(Pagina richiesta);
    Collection<Libro> cerca(Filtro f);
}
