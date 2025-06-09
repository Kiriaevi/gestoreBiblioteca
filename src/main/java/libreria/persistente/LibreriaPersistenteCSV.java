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
				//FIXME: il file ha un nome? Viene messo da qualche parte? A che ci serve questo output?
				file.createNewFile();
			}
		} catch (IOException e) {
			System.out.println("Errore nell'inizializzazione del CSV: " + e.getMessage());
			return false;
		}
		return true;
	}

	//FIXME: nel file JSON non lancia eccezioni e qui si?
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
	/**
	 * Salva i dati della libreria persistente su un file CSV.
	 * Se i libri sono stati modificati o eliminati il file attuale viene sovrascritto con
	 * il contenuto aggiornato dei libri. Se sono stati aggiunti file allora questi vengono appesi
	 *
	 *
	 * Il metodo verifica se vi sono stati cambiamenti nei dati della libreria (tramite il flag `hasBeenModified`).
	 * Se sono presenti modifiche si effettua una sovrascrittura totale.
	 * Per le nuove aggiunte registrate, il metodo aggiorna il file CSV in modalità di
	 * accodamento, aggiungendo esclusivamente i dati dei nuovi libri.
	 *
	 * @throws RuntimeException Se si verifica un errore di I/O durante l'accesso o la scrittura del file CSV.
	 */
	@Override
	protected void persist() throws IOException {
		if(hasBeenModified) {
			hasBeenModified = false;
			chunk.riscritturaCompletaDelFile(libroDaEliminare);
		}
		if(!isBookAdded) return;
		try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
			for (Libro libro : nuoveAggiunte) {
				// per ottimizzazione avevamo messo un contatore che tiene conto di quanti libri abbiamo nel file
				// siccome ora ne stiamo aggiungendo di nuovi andiamo a incrementare questo contatore
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