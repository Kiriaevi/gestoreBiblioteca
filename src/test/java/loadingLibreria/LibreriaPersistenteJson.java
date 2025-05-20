package loadingLibreria;

import libreriaInMemoria.LibreriaAbstract;
import libreriaInMemoria.LibreriaImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LibreriaPersistenteJson {

    @BeforeAll
    public static void onInit() {
        LibreriaAbstract lib = new LibreriaImpl("json");
    }
}
