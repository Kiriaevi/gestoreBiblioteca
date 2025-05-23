package loadingLibreria;

import entities.Libro;
import entities.Stato;
import exceptions.DocumentoMalFormatoException;
import libreriaInMemoria.LibreriaImpl;
import org.junit.jupiter.api.BeforeEach;
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
import java.nio.file.Path;
import java.util.ArrayList;
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
        @ParameterizedTest
        @ValueSource(ints = {-20, Integer.MAX_VALUE, 0})
        void booksShouldBeReadBasedOnQnt(int qnt) throws IOException {
            testFile = Files.createTempFile("test", ".csv").toFile();
            Libro libro = new Libro("Il nome della rosa", "Eco", "12121-1313-1", "Storico", 3, Stato.LETTO);

            if(qnt != 0) {
                try (PrintWriter writer = new PrintWriter(testFile)) {
                    writer.println(convertToCSVLine(libro));
                }
            }

            LibreriaPersistenteAbstract lib = new LibreriaPersistenteCSV(testFile.getAbsolutePath());

            if(qnt < 0) {
                assertThrowsExactly(IllegalArgumentException.class, () -> lib.leggiLibro(qnt));
            }

            if(qnt > 0) {
                try {
                    lib.leggiLibro(qnt);
                } catch (DocumentoMalFormatoException e) {
                    fail("Il file CSV dovrebbe essere leggibile: " + e.getMessage());
                }
            }

            if (qnt >= lib.getSize()) {
                assertEquals(lib.getSize(), lib.leggiLibro(qnt).size());
            }

            if(qnt == 0) {
                assertEquals(0, lib.leggiLibro(qnt).size());
            }
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
            lib.leggiLibro(Integer.MAX_VALUE);
            lib.aggiungiLibro(libro);
            lib.persist();

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
            assertThrows(DocumentoMalFormatoException.class, () -> lib.leggiLibro(1));
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
