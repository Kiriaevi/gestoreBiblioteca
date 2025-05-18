package libreriaInMemoria;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import entities.Libro;
import loadingLibreria.LibreriaPersistenteCSV;
import loadingLibreria.LibreriaPersistenteJSON;

public class LibreriaImpl extends LibreriaAbstract {

    public LibreriaImpl(String type) {
        super();
        onInit(type);
    }
	@Override
	protected void onInit(String type) {
        switch (type) {
            case "csv":
                super.lib = new LibreriaPersistenteCSV(this);
                break;
            case "json":
                super.lib = new LibreriaPersistenteJSON(this);
                break;
            default:
                throw new IllegalArgumentException("Sistema compatibile solo con file CSV e JSON");
        }
	}
	@Override
    public void loadAll() {
        try {
            super.libri = super.lib.leggiLibro(Integer.MAX_VALUE);
        } catch (IOException e) {
            throw new RuntimeException(e+" Errore nella lettura del libro");
        }
    }

    @Override
    public Collection<Libro> getLibri(int size) {
        if (size < 0)
            throw new IllegalArgumentException("size non può essere un valore negativo");
        if(size > super.lib.getSize())
            return Collections.unmodifiableCollection(super.libri);
        return Collections.unmodifiableCollection(super.libri.subList(0, size));
    }

    @Override
	protected void onClose() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'onClose'");
	}

}
