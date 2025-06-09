package libreria.memoria;

import entities.Libro;
import entities.Stato;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LibreriaAbstractTest {
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
    }

    @Nested
    class TestCrud {
        @Test
        void bookShouldBeAdded() throws IOException {
            testFile = Files.createTempFile("test", ".json").toFile();
            LibreriaAbstract lib = new LibreriaImpl("json", testFile.getAbsolutePath());
            Libro l = new Libro("test", "autore", "1324-1213-1", "Storico", 2, Stato.DA_LEGGERE);

            assertTrue(lib.aggiungiLibro(l));
            // non possiamo aggiungere lo stesso libro
            assertFalse(lib.aggiungiLibro(l));

            assertFalse(lib.aggiungiLibro(null));
        }
        @Test
        void bookShouldBeModified() throws IOException {
            testFile = Files.createTempFile("test", ".json").toFile();
            LibreriaAbstract lib = new LibreriaImpl("json", testFile.getAbsolutePath());
            Libro l = new Libro("test", "autore", "1324-1213-1", "Storico", 2, Stato.DA_LEGGERE);
            Libro l2 = new Libro("test", "autore", "1324-1213-2", "Storico", 2, Stato.DA_LEGGERE);

            lib.aggiungiLibro(l);
            lib.aggiungiLibro(l2);
            Libro nuovoLibroStessoISBN = new Libro("test", "autore2", "1324-1213-1", "Storico", 2, Stato.DA_LEGGERE);
            Libro nuovoLibroDiversoISBN2 = new Libro("test", "autore2", "1324-1213-2", "Storico", 2, Stato.DA_LEGGERE);
            Libro nuovoLibroDiversoISBN3 = new Libro("test", "autore2", "1324-1213-3", "Storico", 2, Stato.DA_LEGGERE);

            assertTrue(lib.modificaLibro(nuovoLibroStessoISBN, l));
            assertFalse(lib.modificaLibro(nuovoLibroStessoISBN, null));
            assertFalse(lib.modificaLibro(null, l));
            assertFalse(lib.modificaLibro(null, null));
            // vogliamo modificare il libro esistente con un ISBN diverso ma che già esiste
            assertFalse(lib.modificaLibro(nuovoLibroDiversoISBN2, l));
            // vogliamo modificare il libro esistente con un ISBN diverso che non è già presente
            assertTrue(lib.modificaLibro(nuovoLibroDiversoISBN3, l));
        }
        @Test
        void bookShouldBeDeleted() throws IOException {
            testFile = Files.createTempFile("test", ".json").toFile();
            LibreriaAbstract lib = new LibreriaImpl("json", testFile.getAbsolutePath());
            Libro l = new Libro("test", "autore", "1324-1213-1", "Storico", 2, Stato.DA_LEGGERE);
            Libro l2 = new Libro("test", "autore", "1324-1213-2", "Storico", 2, Stato.DA_LEGGERE);
            lib.aggiungiLibro(l);

            assertTrue(lib.eliminaLibro(l));
            assertFalse(lib.eliminaLibro(null));
            assertFalse(lib.eliminaLibro(l2));
        }
    }

}
