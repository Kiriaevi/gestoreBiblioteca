package ordering;

import entities.Libro;
import entities.Stato;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrdinamentoTest {

    @Nested
    class Ordinamenti {
        @Test
        // ordina la lista, se effettivamente le valutazioni sono in ordine (ascendente) allora il test è superato
        public void shouldBeOrderedByValutazione() {
            List<Libro> listaNonOrdinata = new ArrayList<>(List.of(
                    new Libro("titolo1", "autore1", "1234-5678-9", "Storico", 4, Stato.DA_LEGGERE),
                    new Libro("titolo1", "autore1", "1234-5678-9", "Storico", 2, Stato.DA_LEGGERE),
                    new Libro("titolo1", "autore1", "1234-5678-9", "Storico", 1, Stato.DA_LEGGERE)
            ));
            Comparator<Libro> cAscendente = new OrdinamentoValutazione(false).ottieniComparatore();
            Comparator<Libro> cDiscendente = new OrdinamentoValutazione(true).ottieniComparatore();
            List<Libro> listaOrdinataAscendente = listaNonOrdinata.stream().sorted(cAscendente).toList();
            List<Libro> listaOrdinataDiscendente = listaNonOrdinata.stream().sorted(cDiscendente).toList();
            assertTrue(checkIfOrderedByValutazione(false, listaOrdinataAscendente));
            assertTrue(checkIfOrderedByValutazione(true, listaOrdinataDiscendente));
        }
        @Test
        public void shouldBeOrderedByTitolo() {
            List<Libro> listaNonOrdinata = new ArrayList<>(List.of(
                    new Libro("titolo1", "autore1", "1234-5678-9", "Storico", 4, Stato.DA_LEGGERE),
                    new Libro("bitolo1", "autore1", "1234-5678-9", "Storico", 4, Stato.DA_LEGGERE),
                    new Libro("aitolo1", "autore1", "1234-5678-9", "Storico", 4, Stato.DA_LEGGERE)
            ));
            Comparator<Libro> cAscendente = new OrdinamentoTitolo(false).ottieniComparatore();
            Comparator<Libro> cDiscendente = new OrdinamentoTitolo(true).ottieniComparatore();
            List<Libro> listaOrdinataAscendente = listaNonOrdinata.stream().sorted(cAscendente).toList();
            List<Libro> listaOrdinataDiscendente = listaNonOrdinata.stream().sorted(cDiscendente).toList();
            List<String> titoliAscendente = listaOrdinataAscendente.stream().map(Libro::titolo).toList();
            List<String> titoliDiscendente = listaOrdinataDiscendente.stream().map(Libro::titolo).toList();
            assertEquals(List.of("aitolo1", "bitolo1", "titolo1"), titoliAscendente);
            assertEquals(List.of("titolo1", "bitolo1", "aitolo1"), titoliDiscendente);
        }
        @Test
        public void shouldBeOrderedByAutore() {
            List<Libro> listaNonOrdinata = new ArrayList<>(List.of(
                    new Libro("titolo1", "autore1", "1234-5678-9", "Storico", 4, Stato.DA_LEGGERE),
                    new Libro("titolo1", "dautore1", "1234-5678-9", "Storico", 4, Stato.DA_LEGGERE),
                    new Libro("titolo1", "bautore1", "1234-5678-9", "Storico", 4, Stato.DA_LEGGERE)
            ));
            Comparator<Libro> cAscendente = new OrdinamentoAutore(false).ottieniComparatore();
            Comparator<Libro> cDiscendente = new OrdinamentoAutore(true).ottieniComparatore();
            List<Libro> listaOrdinataAscendente = listaNonOrdinata.stream().sorted(cAscendente).toList();
            List<Libro> listaOrdinataDiscendente = listaNonOrdinata.stream().sorted(cDiscendente).toList();
            List<String> autoriAscendente = listaOrdinataAscendente.stream().map(Libro::autore).toList();
            List<String> autoriDiscendenti = listaOrdinataDiscendente.stream().map(Libro::autore).toList();
            assertEquals(List.of("autore1", "bautore1", "dautore1"), autoriAscendente);
            assertEquals(List.of("dautore1", "bautore1", "autore1"), autoriDiscendenti);
        }
        private boolean checkIfOrderedByValutazione(boolean discendente, List<Libro> listaOrdinata) {
            if (!discendente) {
                int val = 0;
                for (Libro l : listaOrdinata) {
                    if (l.valutazione() < val)
                        return false;
                    val = l.valutazione();
                }
                return true;
            } else {
                int val = 5;
                for (Libro l : listaOrdinata) {
                    if(l.valutazione() > val)
                        return false;
                    val = l.valutazione();
                }
            }
            return true;
        }
    }
}
