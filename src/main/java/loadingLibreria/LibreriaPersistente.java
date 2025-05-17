package loadingLibreria;

import entities.Libro;

import java.io.IOException;
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
     * pre: size >= 0 && < numero di elementi (libri) presenti in memoria secondaria
     */
    List<Libro> leggiLibro(int size) throws IOException;
    /**
     * Modifica le informazioni di un libro esistente. 
     *
     * @param libro il libro modificato
     * @param ISBN L'ISBN del libro da modificare.
     * @return true se la modifica è avvenuta con successo, false altrimenti.
     * pre: ISBN != null && Libro != null
     *
     */
    boolean modificaLibro(Libro libro, String ISBN);
    /**
     * Elimina un libro dalla libreria.
     * 
     * @param libro Il libro da eliminare.
     * @return true se l'eliminazione è avvenuta con successo, false altrimenti.
     * pre: libro != null
     */
    boolean eliminaLibro(Libro libro);

    /**
     * Salva un libro nella libreria persistente. Se l'operazione va a buon fine,
     * restituisce una stringa che rappresenta il libro passato in input e formattato
     * secondo la specifica.
     *
     * @param libro Il libro da salvare nella libreria persistente.
     *              Deve contenere informazioni valide e complete.
     * @return Una stringa che rappresenta l'oggetto sotto forma di stringa formattata secondo
     *         la specifica (CSV, JSON, ecc...)
     */
    String salvaLibro(Libro libro);

    /**
     * Restituisce il numero di libri totali presenti nella sorgente persistente.
     * @return il numero di libri presenti nella sorgente persistente
     */
    int getSize();
}
