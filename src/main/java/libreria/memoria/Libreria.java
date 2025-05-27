package libreria.memoria;


import entities.Libro;
import entities.Pagina;

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
    void loadAll(Pagina richiesta);

    /**
     * Recupera una collezione di libri dalla libreria in base alla pagina richiesta.
     *
     * @param richiesta l'oggetto Pagina che stabilisce quale chunk leggere, (CORRENTE, PROSSIMA, PRECEDENTE, ULTIMA, PRIMA).
     * @return una collezione di libri corrispondente alla pagina richiesta
     * @throws IllegalArgumentException se i parametri di paginazione non sono validi
     */
    Collection<Libro> getLibri(Pagina richiesta);

    /**
     * Modifica le informazioni di un libro esistente nella libreria.
     *
     * @param nuovoLibro    il libro con le nuove informazioni da aggiornare
     * @param vecchioLibro  il libro esistente che deve essere modificato
     * @return true se la modifica ha avuto successo, false altrimenti
     * @throws IOException in caso di errori durante l'operazione di modifica
     */
    boolean modificaLibro(Libro nuovoLibro, Libro vecchioLibro) throws IOException;

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
     * @return
     */
    boolean aggiungiLibro(Libro l) throws IOException;

    /**
     * Verifica se un libro con il dato codice ISBN esiste nella libreria.
     *
     * @param ISBN il codice ISBN del libro da verificare; non può essere null o vuoto
     * @return true se il libro con il codice ISBN specificato esiste, false altrimenti
     */
    boolean bookExists(String ISBN);

}