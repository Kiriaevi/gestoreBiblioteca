package libreria.memoria;


import entities.Libro;

import java.io.IOException;
import java.util.Collection;

public interface Libreria {
    /**
     * Carica tutti i libri dalla libreria persistente e li memorizza nella lista interna.
     * Questo metodo legge i dati della libreria da una fonte persistente configurata
     * (es. file CSV o JSON) e li memorizza in memoria per ulteriori operazioni.
     *
     * @throws RuntimeException se si verifica un errore durante la lettura dei dati
     */
    void loadAll();

    /**
     * Recupera una collezione di libri dalla libreria con una dimensione massima specificata.
     * Se la dimensione supera il numero di libri disponibili, verranno restituiti tutti i libri.
     * I libri restituiti provengono dalla RAM, se il file viene modificato e non viene richiamata la loadAll
     * non si vedranno i libri aggiunti
     *
     * @param size il numero massimo di libri da recuperare; deve essere non negativo
     * @return una collezione di libri, limitata alla dimensione specificata
     * @throws IllegalArgumentException se il parametro size è negativo
     */
    Collection<Libro> getLibri(int size);

    /**
     * Modifica le informazioni di un libro esistente nella libreria, identificandolo tramite l'ISBN.
     *
     * @param l    il libro con le nuove informazioni da aggiornare
     * @param ISBN l'ISBN del libro esistente che deve essere modificato
     * @return true se la modifica ha avuto successo, false altrimenti
     */
    boolean modificaLibro(Libro l, String ISBN) throws IOException;

    /**
     * Rimuove un libro specifico dalla libreria interna, se presente.
     * Il libro da eliminare viene identificato dall'oggetto passato come parametro.
     * Se il libro non è presente nella libreria, non verrà effettuata alcuna modifica.
     *
     * @param l il libro da rimuovere dalla libreria; non può essere null
     * @return true se il libro è stato rimosso con successo, false se il libro non era presente
     *         nella libreria o se l'operazione di rimozione non è stata possibile
     */
    boolean eliminaLibro(Libro l) throws IOException;

    /**
     * Aggiunge un nuovo libro alla libreria. Se il libro è già presente
     * in base all'ISBN, l'operazione potrebbe non avere effetto,
     * in base all'implementazione concreta.
     *
     * @param l il libro da aggiungere alla libreria; non può essere null
     * pre: il libro non deve già esistere nell'elemento persistente TODO
     */
    void aggiungiLibro(Libro l) throws IOException;

}