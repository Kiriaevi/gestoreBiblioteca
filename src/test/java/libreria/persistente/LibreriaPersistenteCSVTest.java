package libreria.persistente;

import entities.Libro;
import entities.Pagina;
import entities.Stato;
import exceptions.DocumentoMalFormatoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LibreriaPersistenteCSVTest {
    private static File testFile;

    @AfterEach
    void cleanUp() {
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }

    @BeforeAll
    public static void setup() throws IOException {
        testFile = Files.createTempFile("tmpLibri", ".csv").toFile();
        LibreriaPersistenteAbstract lib = new LibreriaPersistenteCSV(testFile.getAbsolutePath());
        assertTrue(lib.onInit());
    }

    @Nested
    class TestDiInizializzazione {
        @Test
        void shouldHandleEmptyFileName() {
            assertThrows(IllegalArgumentException.class,
                    () -> new LibreriaPersistenteCSV(""));
        }

        @Test
        void shouldHandleNullFileName() {
            assertThrows(IllegalArgumentException.class,
                    () -> new LibreriaPersistenteCSV(null));
        }
    }

    @Nested
    class TestPerLeOperazioniCRUD {
        @Test
        void bookShouldBeRead() throws IOException {
            testFile = Files.createTempFile("test", ".csv").toFile();
            Libro libro = new Libro("Il nome della rosa", "Eco", "12121-1313-1", "Storico", 3, Stato.LETTO);
            try (PrintWriter writer = new PrintWriter(testFile)) {
                writer.println(convertToCSVLine(libro));
            }
            LibreriaPersistenteAbstract lib = new LibreriaPersistenteCSV(testFile.getAbsolutePath());
            lib.leggiLibro(Pagina.CORRENTE);
            assertTrue(lib.libri.contains(libro));
            assertNull(lib.aggiungiLibro(null));
        }

        @Test
        void persistShouldWriteNewCSVObjects() throws IOException {
            testFile = Files.createTempFile("test", ".csv").toFile();
            Libro libro = new Libro("Il nome della rosa", "Eco", "12121-1313-1", "Storico", 3, Stato.LETTO);

            try (PrintWriter writer = new PrintWriter(testFile)) {
                writer.println(convertToCSVLine(new Libro("Il labirinto di specchi", "Laura Neri", "931-1-48508-8", "Narrativa", 2, Stato.LETTO)));
                writer.println(convertToCSVLine(new Libro("L'eco delle montagne", "Luca Bianchi", "995-9-28771-8", "Fantascienza", 0, Stato.IN_LETTURA)));
            }

            LibreriaPersistenteAbstract lib = new LibreriaPersistenteCSV(testFile.getAbsolutePath());
            lib.leggiLibro(Pagina.CORRENTE);
            lib.aggiungiLibro(libro);

            List<String> lines = Files.readAllLines(testFile.toPath());
            assertTrue(lines.contains(convertToCSVLine(libro)));
        }

    }

    @Nested
    class TestGestioneEccezioni {
        @Test
        void shouldHandleMalformedCSV() throws IOException {
            testFile = Files.createTempFile("malformed", ".csv").toFile();
            try (PrintWriter writer = new PrintWriter(testFile)) {
                writer.println("Titolo,Autore,ISBN"); // Mancano campi
            }

            LibreriaPersistenteCSV lib = new LibreriaPersistenteCSV(testFile.getAbsolutePath());
            lib.leggiLibro(Pagina.CORRENTE);
            // dato che aggiungiamo solo un libro e questo è pure malformato allora mi aspetto che venga creata una lista vuota
            assertEquals(0, lib.libri.size());
        }
    }

    private String convertToCSVLine(Libro libro) {
        return String.join(",",
                libro.titolo(),
                libro.autore(),
                libro.isbn(),
                libro.genere(),
                String.valueOf(libro.valutazione()),
                libro.stato().name());
    }
}
