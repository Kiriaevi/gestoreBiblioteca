import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import entities.Libro;
import loadingLibreria.LibreriaPersistente;

public class LibreriaImpl extends LibreriaAbstract {


    public LibreriaImpl() {
        super();
        onInit();
    }
	@Override
	protected void onInit() {
		// TODO Auto-generated method stub
        loadBooks();
	}
	@Override
    public void loadAll(LibreriaPersistente input) {
        try {
            List<Libro> libro = input.leggiLibro(1);
        } catch (IOException e) {
            throw new RuntimeException(e+" Errore nella lettura del libro");
        }
    }


	@Override
	protected void onClose() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'onClose'");
	}

	@Override
	protected void onChange() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'onChange'");
	}
    private void loadBooks() {
	}
}
