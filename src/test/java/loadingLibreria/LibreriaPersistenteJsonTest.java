package loadingLibreria;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Libro;
import entities.Stato;
import exceptions.DocumentoMalFormatoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LibreriaPersistenteJsonTest {

    private static File testFile;

    @AfterEach
    void cleanUp() {
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }
    @BeforeAll
    public static void setup() throws IOException {
        testFile = Files.createTempFile("tmpLibri", "json").toFile();
        LibreriaPersistenteAbstract lib = new LibreriaPersistenteJSON(testFile.getAbsolutePath());
        assertTrue(lib.onInit());
    }
    @Nested
    class TestDiInizializzazione {
        @Test
        void shouldHandleEmptyFileName() {
            assertThrows(IllegalArgumentException.class,
                    () -> new LibreriaPersistenteJSON(""));
        }

        @Test
        void shouldHandleNullFileName() {
            assertThrows(IllegalArgumentException.class,
                    () -> new LibreriaPersistenteJSON(null));
        }
    }
    @Nested
    class TestPerLeOperazioniCRUD {
        @ParameterizedTest
        @ValueSource(ints = {-20, Integer.MAX_VALUE, 0})
        void booksShouldBeReadBasedOnQnt(int qnt) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            testFile = Files.createTempFile("test", ".json").toFile();
            Libro libro = new Libro("Il nome della rosa", "Eco", "12121-1313-1", "Storico", 3, Stato.LETTO);
            List<Libro> libri = List.of(libro);
            if(qnt != 0)
                mapper.writeValue(testFile,libri);
            else
                mapper.writeValue(testFile, new ArrayList<>());

            LibreriaPersistenteAbstract lib = new LibreriaPersistenteJSON(testFile.getAbsolutePath());
            if(qnt < 0)
                assertThrowsExactly(IllegalArgumentException.class, () -> lib.leggiLibro(qnt));

            // mi aspetto che il nodo root sia un array
            if( qnt > 0)
                try {
                    lib.leggiLibro(qnt);
                } catch (DocumentoMalFormatoException e) {
                    fail("Il primo nodo deve essere un array: " + e.getMessage());
                }
            // se l'utente inserisce una quantità che supera il numero di libri salvati in libreria allora ci aspettiamo che questo venga troncato
            if (qnt >= lib.getSize())  {
                // la libreria finale deve avere una dimensione pari al numero di libri letti
                assertEquals(lib.getSize(), lib.leggiLibro(qnt).size());
            }
            if(qnt == 0)
                assertEquals(0,lib.leggiLibro(qnt).size());
        }
        @Test
        void persistShouldWriteNewJsonObjects() throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            testFile = Files.createTempFile("test", ".json").toFile();
            Libro libro = new Libro("Il nome della rosa", "Eco", "12121-1313-1", "Storico", 3, Stato.LETTO);
            List<Libro> libri = List.of(
                    new Libro("Il labirinto di specchi", "Laura Neri", "931-1-48508-8", "Narrativa", 2, Stato.LETTO),
                    new Libro("L’eco delle montagne", "Luca Bianchi", "995-9-28771-8", "Fantascienza", 0, Stato.IN_LETTURA)
            );
            mapper.writeValue(testFile, libri);

            LibreriaPersistenteAbstract lib = new LibreriaPersistenteJSON(testFile.getAbsolutePath());
            lib.leggiLibro(Integer.MAX_VALUE);
            lib.salvaLibro(libro);
            lib.persist();
            List<Libro> libriDeserializzati = mapper.readValue(testFile, new TypeReference<>() {});
            // Se all'interno del JSON troviamo il libro da noi appena scritto allora il test è superato
            assertTrue(libriDeserializzati.contains(libro));
        }
    }
    @Nested
    class TestGestioneEccezioni {
        @Test
        void shouldHandleEmptyFileName() {
            assertThrows(IllegalArgumentException.class,
                    () -> new LibreriaPersistenteJSON(""));
        }

        @Test
        void shouldHandleNullFileName() {
            assertThrows(IllegalArgumentException.class,
                    () -> new LibreriaPersistenteJSON(null));
        }
    }
}
