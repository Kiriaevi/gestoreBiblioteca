package libreriaInMemoria;


import entities.Libro;

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
     *
     * @param size il numero massimo di libri da recuperare; deve essere non negativo
     * @return una collezione di libri, limitata alla dimensione specificata
     * @throws IllegalArgumentException se il parametro size è negativo
     */
    Collection<Libro> getLibri(int size);
}