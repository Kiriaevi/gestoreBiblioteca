package libreria.memoria;

import libreria.persistente.LibreriaPersistenteCSV;
import libreria.persistente.LibreriaPersistenteJSON;

public class LibreriaImpl extends LibreriaAbstract {

    public LibreriaImpl(String type, String n) {
        super(n);
        onInit(type);
        loadAll();
    }
	@Override
	protected void onInit(String type) {
        switch (type) {
            case "csv":
                super.lib = new LibreriaPersistenteCSV(super.nomeStruturaPersistente+".csv");
                break;
            case "json":
                super.lib = new LibreriaPersistenteJSON(super.nomeStruturaPersistente+".json");
                break;
            default:
                throw new IllegalArgumentException("Sistema compatibile solo con file CSV e JSON");
        }
	}
    @Override
	protected void onClose() {
        System.out.println("Disconnessione della libreria");
	}

}
