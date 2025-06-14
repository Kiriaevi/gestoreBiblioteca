package libreria.memoria;


import entities.Libro;
import entities.Pagina;

import java.io.IOException;
import java.util.Collection;

public interface Libreria {
    /**
     * Carica tutti i libri appartenenti alla pagina specificata interna al sistema di persistenza.
     * La pagina specificata determina il blocco di dati da caricare, che può essere
     * una delle seguenti opzioni: CORRENTE, PROSSIMA, PRECEDENTE, ULTIMA, PRIMA.
     *
     * @param richiesta l'oggetto Pagina che stabilisce quale blocco di dati caricare
     *                  (CORRENTE, PROSSIMA, PRECEDENTE, ULTIMA, PRIMA)
     */
    void loadAll(Pagina richiesta);

    /**
     * Recupera una collezione di libri dalla lista in memoria in base alla pagina richiesta.
     *
     * @param richiesta l'oggetto Pagina che stabilisce quale chunk leggere, (CORRENTE, PROSSIMA, PRECEDENTE, ULTIMA, PRIMA).
     * @return una collezione di libri corrispondente alla pagina richiesta
     * @throws IllegalArgumentException se i parametri di paginazione non sono validi
     */
    Collection<Libro> getLibri(Pagina richiesta);

    /**
     * Modifica le informazioni di un libro nella libreria con il contenuto di un altro libro.
     * L'operazione si compone di una rimozione del libro esistente e dell'aggiunta del nuovo libro.
     * Se l'ISBN del libro modificato differisce da quello originale, viene controllato che non esistano duplicati
     * con lo stesso ISBN prima di procedere con l'operazione.
     *
     * @param nuovoLibro il nuovo libro che sostituirà il vecchio libro; non può essere null
     * @param vecchioLibro il libro da sostituire nella libreria; non può essere null
     * @return true se l'operazione è stata completata con successo, false altrimenti (ad esempio in caso di duplicati ISBN
     *         o se i parametri forniti sono null)
     * @throws IOException se si verifica un errore durante l'accesso al sistema di persistenza
     */
    boolean modificaLibro(Libro nuovoLibro, Libro vecchioLibro) throws IOException;

    /**
     * Rimuove un libro dalla libreria, se presente.
     * Il libro da eliminare viene identificato dall'oggetto passato come parametro.
     * Se il libro non è presente nella libreria, non verrà effettuata alcuna modifica e viene restituito false.
     *
     * @param l il libro da rimuovere dalla libreria; non può essere null
     * @return true se il libro è stato rimosso con successo, false se il libro non era presente
     *         nella libreria o se l'operazione di rimozione non è stata possibile
     */
    boolean eliminaLibro(Libro l) throws IOException;

    /**
     * Aggiunge un libro alla libreria se non è già presente.
     * Il metodo verifica se il libro con lo stesso ISBN esiste già nella libreria e, in caso contrario,
     * procede ad aggiungerlo.
     *
     * @param l il libro da aggiungere; non può essere null e il suo ISBN non deve essere già presente nella libreria
     * @return true se il libro è stato aggiunto con successo, false se il libro è già presente o se l'operazione fallisce
     * @throws IOException se si verifica un errore durante l'accesso al sistema di persistenza
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