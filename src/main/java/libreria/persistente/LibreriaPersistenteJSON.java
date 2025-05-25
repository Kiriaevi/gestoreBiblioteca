package libreria.persistente;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import comparators.OrdinamentoValutazione;
import entities.Libro;
import entities.Pagina;
import exceptions.DocumentoMalFormatoException;
import exceptions.ErroreNellaCreazioneDelFile;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entities.Pagina.*;

public class LibreriaPersistenteJSON extends LibreriaPersistenteAbstract{

	private final File file;
	private final ObjectMapper mapper = new ObjectMapper();
	public LibreriaPersistenteJSON(String pathFile) {
		super(pathFile);
		this.file = new File(pathFile);
		onInit();
	}

	@Override
	public boolean onInit() {
		if(!file.exists()) {
            try {
                boolean isCreated = file.createNewFile();
                if(!isCreated)
                    throw new ErroreNellaCreazioneDelFile("Non è stato possibile creare il file libri.json");
            mapper.writeValue(file, new ArrayList<>());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    return true;
}

	@Override
    protected void close() {
		System.out.println("Sistema in chiusura...");
	}

	@Override
	protected void persist() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, super.libri);
			super.isBookAdded = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	@Override
	public List<Libro> leggiLibro(Pagina richiesta) {
		List<Libro> ret;
        try {
            JsonNode root = mapper.readTree(file);
			if(root.isArray()) {
				ret = mapper.readValue(file, new TypeReference<>() {});
			} else {
				throw new DocumentoMalFormatoException("Mi aspetto come parametro di root un array");
			}
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
		super.libri = super.ordinaLibreria(ret, new OrdinamentoValutazione(true).ottieniComparatore());
		return super.libri;
    }
	@Override
	public int getSize() {
		if(super.libri != null)
			return super.libri.size();
        try {
            return mapper.readValue(file, new TypeReference<List<Libro>>() {}).size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}