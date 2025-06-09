package libreria.persistente;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Libro;
import entities.Pagina;
import entities.Stato;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
        // Se c'è un file con una determinata lista di libri me la devo ritrovare quando faccio la lettura dal metodo
        @Test
        void booksShouldBeRead() throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            testFile = Files.createTempFile("test", ".json").toFile();
            Libro libro = new Libro("Il nome della rosa", "Eco", "12121-1313-1", "Storico", 3, Stato.LETTO);
            List<Libro> libri = List.of(libro);
            mapper.writeValue(testFile, libri);

            LibreriaPersistenteAbstract lib = new LibreriaPersistenteJSON(testFile.getAbsolutePath());
            assertEquals(libri, lib.leggiLibro(Pagina.CORRENTE));
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
            lib.leggiLibro(Pagina.CORRENTE);
            lib.aggiungiLibro(libro);
            List<Libro> libriDeserializzati = mapper.readValue(testFile, new TypeReference<>() {});
            // Se all'interno del JSON troviamo il libro da noi appena scritto allora il test è superato
            assertTrue(libriDeserializzati.contains(libro));
        }
        @Test
        void persistShouldModifyObjects() throws  IOException {
            ObjectMapper mapper = new ObjectMapper();
            testFile = Files.createTempFile("test", ".json").toFile();
            Libro libro = new Libro("Il nome della rose", "Eco", "12121-1313-1", "Storico", 3, Stato.LETTO);
            List<Libro> libri = List.of(libro, new Libro("Esempio", "test", "12121-1313-2", "Storico", 1, Stato.DA_LEGGERE));
            mapper.writeValue(testFile, libri);

            Libro libroCheNonEsiste = new Libro("Il nome della rosa", "Eco", "12121-1313-3", "Storico", 5, Stato.LETTO);

            Libro nuovoLibro = new Libro("Il nome della rosa", "Eco", "12121-1313-1", "Storico", 5, Stato.LETTO);

            LibreriaPersistenteAbstract lib = new LibreriaPersistenteJSON(testFile.getAbsolutePath());
            lib.leggiLibro(Pagina.CORRENTE);
            assertTrue(lib.modificaLibro(nuovoLibro, libro.isbn()));
            assertFalse(lib.modificaLibro(nuovoLibro, libroCheNonEsiste.isbn()));
            assertFalse(lib.modificaLibro(null, libroCheNonEsiste.isbn()));
            assertFalse(lib.modificaLibro(nuovoLibro, null));
        }
        @Test
        void bookShouldBeDeleted() throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            testFile = Files.createTempFile("test", ".json").toFile();
            Libro libro = new Libro("Il nome della rose", "Eco", "12121-1313-1", "Storico", 3, Stato.LETTO);
            Libro libroCheNonEsiste = new Libro("Il nome della rose", "Eco", "12123-1313-1", "Storico", 3, Stato.LETTO);

            mapper.writeValue(testFile, List.of(libro));
            LibreriaPersistenteAbstract lib = new LibreriaPersistenteJSON(testFile.getAbsolutePath());
            lib.leggiLibro(Pagina.CORRENTE);

            assertTrue(lib.eliminaLibro(libro));
            assertFalse(lib.eliminaLibro(libroCheNonEsiste));
            assertFalse(lib.eliminaLibro(null));
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
