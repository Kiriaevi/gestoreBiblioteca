package libreria.persistente;

import java.io.*;

import java.io.FileReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import utility.Utility;
import ordering.OrdinamentoValutazione;
import entities.Libro;
import entities.Pagina;
import libreria.persistente.chunk.ChunkCSV;
import ricerca.Filtro;

public class LibreriaPersistenteCSV extends LibreriaPersistenteAbstract{

	private final String fileName;
	private final List<Libro> nuoveAggiunte = new LinkedList<>();
	private final ChunkCSV chunk;
	public LibreriaPersistenteCSV(String pathFile) {
		super(pathFile);
		this.fileName = pathFile;
		chunk = new ChunkCSV(pathFile, this);
		onInit();
		super.size = this.getSize();
	}

	@Override
	protected boolean onInit() {
		try {
			File file = new File(fileName);
			if(!file.exists()) {
				System.out.println("Il file non esiste, lo creo");
				file.createNewFile();
			}
		} catch (IOException e) {
			System.out.println("Errore nell'inizializzazione del CSV: " + e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	protected void close() {}
	@Override
	public String aggiungiLibro(Libro libro) throws IOException {
		if(libro != null) {
			nuoveAggiunte.add(libro);
			super.libri.add(libro);
			super.isBookAdded = true;
			persist();
		}
		return null;
	}
	@Override
	public boolean modificaLibro(Libro libro, String ISBN) throws IOException {
		if (libro == null || ISBN == null) return false;
		int found = cercaLibroPerISBN(ISBN);
		if(found == -1) return false;
		libri.set(found, libro);
		nuoveAggiunte.add(libro);
		hasBeenModified = true;
		persist();
		return true;
	}
	@Override
	public int cercaLibroPerISBN(String ISBN) {
		return chunk.cercaLibroPerISBN(ISBN);
	}
	@Override
	public int getSize() {
		return nLinee();
	}
	@Override
	public List<Libro> leggiLibro(Pagina richiesta) {
		List<Libro> ret = chunk.leggi(richiesta);
		super.libri = super.ordinaLibreria(ret, new OrdinamentoValutazione(true).ottieniComparatore());
		return super.libri;
	}

	@Override
	public Collection<Libro> cerca(Filtro f) {
		return chunk.cerca(f);
	}

	@Override
	protected void persist() throws IOException {
		if(hasBeenModified) {
			hasBeenModified = false;
			chunk.riscritturaCompletaDelFile(libroDaEliminare);
		}
		if(!isBookAdded) return;
		try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
			for (Libro libro : nuoveAggiunte) {
				super.size++;
				writer.println(Utility.convertiInCSV(libro));
			}
		} catch (IOException e) {
			throw new RuntimeException("Errore nel salvare i libri nel file", e);
		}
		nuoveAggiunte.clear();
	}
	private int nLinee() {
		// per ottimizzazione: apprendiamo il numero di linee del file solo la prima volta
		if(super.size != -1)
			return super.size;
		try(BufferedReader brLines = new BufferedReader(new FileReader(fileName))) {
			int cnt = 0;
			while(brLines.readLine() != null)
				cnt++;
			return cnt;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}



}