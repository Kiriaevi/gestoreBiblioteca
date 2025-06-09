package ricerca;

import entities.Libro;
import entities.Stato;
import libreria.memoria.LibreriaAbstract;
import libreria.memoria.LibreriaImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ricerca.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FiltroTest {
    private static File testFile;

    @BeforeAll
    public static void setup() throws IOException {
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
        testFile = Files.createTempFile("tmpLibri", ".json").toFile();
    }
    @Nested
    class Ricerca {
        @ParameterizedTest
        @MethodSource("filtraTutti")
        public void testRicercaConFiltro(Filtro filtro, Libro libroAtteso, Libro libroAssente) throws IOException {
            LibreriaAbstract lib = new LibreriaImpl("json", testFile.getAbsolutePath());
            lib.aggiungiLibro(libroAtteso);

            assertTrue(lib.cerca(filtro).contains(libroAtteso));
            assertThrows(IllegalArgumentException.class, () -> lib.cerca(null));
            assertFalse(lib.cerca(filtro).contains(libroAssente));
        }

        // creo uno stream di Arguments, cioè inserisco tutti i diversi esempi e filtri i uno stream e permetto di rendere il test parametrizzato
        private static Stream<Arguments> filtraTutti() {
            // modifico solo l'ISBN
            Libro l1 = new Libro("test", "autore", "1234-5678-9", "Storico", 2, Stato.DA_LEGGERE);
            Libro l1NonTrovato = new Libro("test", "autore", "1234-5678-1", "Storico", 2, Stato.DA_LEGGERE);

            // modifico solo l'autore
            Libro l2 = new Libro("test", "autore", "1234-5678-9", "Storico", 2, Stato.DA_LEGGERE);
            Libro l2NonTrovato = new Libro("test", "qualcosaDiDiverso", "1234-5678-1", "Storico", 2, Stato.DA_LEGGERE);

            // modifico solo il titolo
            Libro l3 = new Libro("test", "autore", "1234-5678-9", "Storico", 2, Stato.DA_LEGGERE);
            Libro l3NonTrovato = new Libro("qualcosaDiDiverso", "autore", "1234-5678-1", "Storico", 2, Stato.DA_LEGGERE);

            // modifico solo il genere
            Libro l4 = new Libro("test", "autore", "1234-5678-9", "Storico", 2, Stato.DA_LEGGERE);
            Libro l4NonTrovato = new Libro("test", "autore", "1234-5678-1", "Fumetto", 2, Stato.DA_LEGGERE);

            // modifico solo lo stato
            Libro l5 = new Libro("test", "autore", "1234-5678-9", "Storico", 2, Stato.DA_LEGGERE);
            Libro l5NonTrovato = new Libro("test", "autore", "1234-5678-1", "Storico", 2, Stato.LETTO);

            return Stream.of(
                    Arguments.of(new FiltroISBN(new FiltroBase(), l1.isbn()), l1, l1NonTrovato),
                    Arguments.of(new FiltroAutore(new FiltroBase(), l2.autore()), l2, l2NonTrovato),
                    Arguments.of(new FiltroTitolo(new FiltroBase(), l3.titolo()), l3, l3NonTrovato),
                    Arguments.of(new FiltroGenere(new FiltroBase(), l4.genere()), l4, l4NonTrovato),
                    Arguments.of(new FiltroStato(new FiltroBase(), l5.stato()), l5, l5NonTrovato)
            );
        }
    }
}
