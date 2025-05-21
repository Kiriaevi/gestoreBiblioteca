package libreriaInMemoria;

import loadingLibreria.LibreriaPersistenteCSV;
import loadingLibreria.LibreriaPersistenteJSON;

public class LibreriaImpl extends LibreriaAbstract {

    private final static String nomeFile = "libri";
    public LibreriaImpl(String type) {
        super();
        onInit(type);
        loadAll();
    }
	@Override
	protected void onInit(String type) {
        switch (type) {
            case "csv":
                super.lib = new LibreriaPersistenteCSV(nomeFile+".csv");
                break;
            case "json":
                super.lib = new LibreriaPersistenteJSON(nomeFile+".json");
                break;
            default:
                throw new IllegalArgumentException("Sistema compatibile solo con file CSV e JSON");
        }
	}
    @Override
	protected void onClose() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'onClose'");
	}

}
