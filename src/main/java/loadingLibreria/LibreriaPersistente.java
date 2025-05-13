package loadingLibreria;

import entities.Libro;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * LibreriaPersistente
 * Interfaccia per la gestione della libreria persistente.
 * Fornisce metodi per leggere i libri e gestire le operazioni di caricamento.
 */
public interface LibreriaPersistente {
    /**
     * Legge un numero specifico di libri da una sorgente persistente.
     * 
     * @param size Il numero di libri da leggere.
     */
    List<Libro> leggiLibro(int size) throws IOException;
    /**
     * Modifica le informazioni di un libro esistente. 
     *
     * @param libro il libro modificato
     * @param ISBN L'ISBN del libro da modificare.
     * @return true se la modifica è avvenuta con successo, false altrimenti.
     */
    boolean modificaLibro(Libro libro, String ISBN);
    /**
     * Elimina un libro dalla libreria.
     * 
     * @param libro Il libro da eliminare.
     * @return true se l'eliminazione è avvenuta con successo, false altrimenti.
     */
    boolean eliminaLibro(Libro libro);

}
