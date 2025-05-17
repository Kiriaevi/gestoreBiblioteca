package loadingLibreria;

import entities.Libro;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.stream.JsonGenerator;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class LibreriaPersistenteJSON extends LibreriaPersistenteAbstract{

	private final String fileName = "libri.json";
	private JsonGenerator jsonGen = null;
	private JsonReader jsonReader = null;
	@Override
	public boolean onInit() {
		try {
			if (jsonGen == null)
				jsonGen = Json.createGenerator(new FileWriter(fileName));
			if (jsonReader == null)
				jsonReader = Json.createReader(Files.newInputStream(Paths.get(fileName)));
		} catch (IOException e) {
			System.out.println("Errore nell'inizializzazione del componente JSON: " + e.getMessage());
			return false;
		}
		return true;
	}

	@Override
    protected void onClose() {
		if(jsonGen != null) {
			jsonGen.close();
		}
		if(jsonReader != null) {
			jsonReader.close();
		}
	}

	@Override
	protected void persist() {

	}

	@Override
	public ArrayList<Libro> leggiLibro(int size) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'leggiLibro'");
	}

	@Override
	public boolean modificaLibro(Libro libro, String ISBN) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'modificaLibro'");
	}

	@Override
	public boolean eliminaLibro(Libro libro) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'eliminaLibro'");
	}

	@Override
	public String salvaLibro(Libro libro) {
        return null;
    }

	@Override
	public int getSize() {
		return 0;
	}

}
