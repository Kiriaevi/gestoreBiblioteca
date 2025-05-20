package loadingLibreria;

import entities.Libro;
import entities.Stato;
import libreriaInMemoria.LibreriaImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LibreriaPersistenteCSVTest {

    private LibreriaPersistenteCSV libreriaPersistenteCSV;
    @BeforeEach
    //FIXME
    public void setup() {
     //   libreriaPersistenteCSV = new LibreriaPersistenteCSV();
     //   libreriaPersistenteCSV.onInit();
    }
    @Test
    //FIXME
    public void initializationShouldBeSuccessful() {
       // LibreriaPersistenteCSV istanza = new LibreriaPersistenteCSV();
        //
        // assertTrue(istanza.onInit());
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, Integer.MAX_VALUE, 1})
    public void bookShouldBeSaved(int valutazione) {
        Libro libro = new Libro(
                "La legge dell'elefante",
                "Lirilì",
                "LUNGOISBN342",
                "fantasy/brainrot",
                valutazione,
                Stato.DA_LEGGERE);
        assertEquals("La legge dell'elefante,Lirilì,LUNGOISBN342,fantasy/brainrot,"+valutazione+",DA_LEGGERE",
                libreriaPersistenteCSV.salvaLibro(libro));
    }

    @Test
    public void bookShouldBeRead() {
    }

    @Test
    public void bookShouldBeUpdated() {
    }

    @Test
    public void bookShouldBeDeleted() {
    }
}