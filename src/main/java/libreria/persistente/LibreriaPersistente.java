package libreria.persistente;

import entities.Libro;
import entities.Pagina;

import java.io.IOException;
import java.util.List;

/**
 * LibreriaPersistente
 * Interfaccia per la gestione della libreria persistente.
 * Fornisce metodi per leggere i libri e gestire le operazioni di caricamento.
 */
public interface LibreriaPersistente {

    /**
     * Legge una lista di libri dalla sorgente persistente in base alla pagina richiesta.
     *
     * @param richiesta la pagina specificata da leggere, uno dei valori definiti nell'enum Pagina
     *                  (e.g., PROSSIMA, PRECEDENTE, CORRENTE, ULTIMA, PRIMA).
     * @return una lista di oggetti Libro che rappresentano i libri letti dalla sorgente persistente.
     * @throws IOException se si verifica un errore durante l'accesso o la lettura dalla sorgente persistente.
     */
    List<Libro> leggiLibro(Pagina richiesta) throws IOException;
    /**
     * Modifica le informazioni di un libro esistente. 
     *
     * @param libro il libro modificato
     * @param ISBN L'ISBN del libro da modificare.
     * @return true se la modifica è avvenuta con successo, false altrimenti.
     *
     */
    boolean modificaLibro(Libro libro, String ISBN) throws IOException;
    /**
     * Elimina un libro dalla libreria.
     * 
     * @param libro Il libro da eliminare.
     * @return true se l'eliminazione è avvenuta con successo, false altrimenti.
     */
    boolean eliminaLibro(Libro libro) throws IOException;

    /**
     * Aggiunge un libro alla libreria persistente.
     * Questo metodo salva il libro nella libreria e aggiorna il file persistente per mantenerne la coerenza.
     *
     * @param libro il libro da aggiungere. Non deve essere null.
     * @return una stringa contenente il risultato dell'operazione,
     *         oppure null se l'operazione non ha prodotto messaggi significativi.
     * @throws IOException se si verifica un errore durante l'accesso o la scrittura dei dati nel file persistente.
     */
    String aggiungiLibro(Libro libro) throws IOException;

    /**
     * Restituisce il numero di libri totali presenti nella sorgente persistente.
     * @return il numero di libri presenti nella sorgente persistente
     */
    int getSize();
}
