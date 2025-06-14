package libreria.persistente.chunk;

import entities.Libro;
import entities.Pagina;
import ricerca.Filtro;

import java.util.Collection;
import java.util.List;

/**
 * Interfaccia che definisce le operazioni per la paginazione e la gestione di una collezione di libri.
 * Permette la navigazione tra i chunk di dati da una libreria ed il filtraggio dei libri secondo un filtro.
 */
public interface Chunk {
    List<Libro> leggi(Pagina richiesta);
    Collection<Libro> cerca(Filtro f);
}
