package loadingLibreria;

import java.io.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import comparators.OrdinamentoValutazione;
import entities.Libro;
import entities.Stato;
import exceptions.DocumentoMalFormatoException;

public class LibreriaPersistenteCSV extends LibreriaPersistenteAbstract{

	private String fileName = "libri.csv";
	private BufferedReader br = null;
	private PrintWriter pw = null;
	private StringBuilder sb = null;
	private List<Libro> nuoveAggiunte = new LinkedList<>();
	public LibreriaPersistenteCSV(String pathFile) {
		super();
		this.fileName = pathFile;
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
			if (br == null)
				br = new BufferedReader(new FileReader(file));
			//if(pw == null)
			//	pw = new PrintWriter(new FileWriter(file, true), true);
			if(sb == null)
				sb = new StringBuilder();
		} catch (IOException e) {
			System.out.println("Errore nell'inizializzazione del CSV: " + e.getMessage());
			return false;
		}
		return true;
	}

	//FIXME: nel file JSON non lancia eccezioni e qui si?
	@Override
	protected void close() {
		try {
			if (br != null)
				br.close();
			if (pw !=null)
				pw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// FIXME: o salvi il singolo libro o salvi tutti i libri
	@Override
	public String salvaLibro(Libro libro) {
		if(libro != null) {
			nuoveAggiunte.add(libro);
			super.libri.add(libro);
			aggiunte++;
			persist();
		}
		return null;
	}

	@Override
	public int getSize() {
		return nLinee();
	}


	@Override
	public List<Libro> leggiLibro(int size) throws IOException {
		if(size < 0)
			throw new IllegalArgumentException("La size non può essere negativa");
		if(!super.libri.isEmpty())
			return super.libri;
		List<String> libriInStringhe = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			// se l'ingresso ricevuto `size` è più grande del numero di linee del file allora fermiamoci
			if(super.size <= i)
				break;
			String book = br.readLine();
			libriInStringhe.add(book);
		}
		List<Libro> ret =  libriInStringhe.stream().map(this::convertiInLibro).toList();
		super.libri = super.ordinaLibreria(ret, new OrdinamentoValutazione(true).ottieniComparatore());
		return super.libri;
	}

	private Libro convertiInLibro(String libro) {
		String[] splitLibro = libro.split(",");
		if(splitLibro.length < 6)
			throw new DocumentoMalFormatoException("Il documento passato non è correttamente formattato, dovrebbero esserci 6 campi!");
		String titolo = splitLibro[0];
		String autor = splitLibro[1];
		String isbn = splitLibro[2];
		String genere = splitLibro[3];
		int valutazione = Integer.parseInt(splitLibro[4]);
		Stato stato = splitLibro[5].isEmpty() ? Stato.DA_LEGGERE : Stato.valueOf(splitLibro[5]);
		return new Libro(titolo, autor, isbn, genere, valutazione, stato);
	}

	private int nLinee() {
		// per ottimizzazione: apprendiamo il numero di linee del file solo la prima volta
		if(super.size != -1)
			return super.size;
		try(BufferedReader brLines = new BufferedReader(new FileReader(fileName))) {
			int cnt = 0;
			while(brLines.readLine() != null)
				cnt++;
			System.out.println(cnt);
			return cnt;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
	protected void persist() {
		if(hasBeenModified) {
			hasBeenModified = false;
			try(PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
				for(Libro libro : super.libri)
					writer.println(convertiInCSV(libro));
			} catch (IOException e) {
				throw new RuntimeException("Errore nel salvare i libri nel file", e);
			}
		}
		if(aggiunte == 0) return;
		try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
			for (Libro libro : nuoveAggiunte) {
				aggiunte--;
				// per ottimizzazione avevamo messo un contatore che tiene conto di quanti libri abbiamo nel file
				// siccome ora ne stiamo aggiungendo di nuovi andiamo ad incrementare questo contatore
				super.size++;
				writer.println(convertiInCSV(libro));
			}
		} catch (IOException e) {
			throw new RuntimeException("Errore nel salvare i libri nel file", e);
		}
		nuoveAggiunte.clear();
	}
	/**
	 * convertiInCSV
	 * @param libro, il libro da convertire in una Stringa conforme allo standard CSV
	 * @return la Stringa formattata in CSV che rappresenta il libro
	 */
	private String convertiInCSV(Libro libro) {
		if(sb != null) {
			sb.append(libro.titolo());
			sb.append(",");
			sb.append(libro.autore());
			sb.append(",");
			sb.append(libro.isbn());
			sb.append(",");
			sb.append(libro.genere());
			sb.append(",");
			sb.append(libro.valutazione());
			sb.append(",");
			sb.append(libro.stato());
			String ret = sb.toString();
			sb.setLength(0);
			return ret;
		}
		return null;
	}
}
