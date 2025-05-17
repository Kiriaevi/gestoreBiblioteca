package loadingLibreria;

import java.io.*;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import entities.Libro;
import entities.Stato;
import exceptions.DocumentoMalFormatoException;

public class LibreriaPersistenteCSV extends LibreriaPersistenteAbstract{

	private final String fileName = "libri.csv";
	private BufferedReader br = null;
	private PrintWriter pw = null;
	private StringBuilder sb = null;

	public LibreriaPersistenteCSV() {
		super();
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
	protected void onClose() {
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
		if(libro != null && pw != null) {
			String line = convertiInCSV(libro);
			pw.println(convertiInCSV(libro));
			super.size++;
			return line;
		}
		return null;
	}

	@Override
	public int getSize() {
		return nLinee();
	}

	/**
	 * convertiInCSV
	 * @param libro, il libro da convertire in una Stringa conforme allo standard CSV
	 * @return la Stringa formattata in CSV che rappresenta il libro
	 */
	private String convertiInCSV(Libro libro) {
		if(sb != null) {
			sb.append(libro.getTitolo());
			sb.append(",");
			sb.append(libro.getAutore());
			sb.append(",");
			sb.append(libro.getISBN());
			sb.append(",");
			sb.append(libro.getGenere());
			sb.append(",");
			sb.append(libro.getValutazione());
			sb.append(",");
			sb.append(libro.getStato());
			String ret = sb.toString();
			sb.setLength(0);
			return ret;
		}
		return null;
	}

	@Override
	public List<Libro> leggiLibro(int size) throws IOException {
		if(size < 0)
			throw new IllegalArgumentException("La size non può essere negativa");
		List<String> libriInStringhe = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			// se l'ingresso ricevuto `size` è più grande del numero di linee del file allora fermiamoci
			if(super.size <= i)
				break;
			String book = br.readLine();
			libriInStringhe.add(book);
		}
		List<Libro> ret =  libriInStringhe.stream().map(this::convertiInLibro).toList();
		super.libri.addAll(ret);
		return ret;
	}

	//TODO: viene solo modificato in RAM, implementare salvataggio persistente definitivo
	@Override
	public boolean modificaLibro(Libro libro, String ISBN) {
		if (libro == null || ISBN == null) return false;
		int found = cercaLibroPerISBN(ISBN);
		if(found == -1) return false;
		super.libri.set(found, libro);
		return true;
	}

	// TODO: anche qua, modificato in RAM ma non salvato in maniera persistente
	@Override
	public boolean eliminaLibro(Libro libro) {
		if(libro == null) return false;
		int found = cercaLibroPerISBN(libro.getISBN());
		if(found == -1) return false;
		super.libri.remove(found);
		return true;
	}
	private Libro convertiInLibro(String libro) {
		String[] splitLibro = libro.split(",");
		if(splitLibro.length < 6)
			throw new DocumentoMalFormatoException("Il documento passato non è correttamente formattato, dovrebbero esserci 6 campi!");
		//FIXME: tutti i dati dovrebbero venire compilati?
		String titolo = splitLibro[0].isEmpty() ? "Nessun titolo trovato" : splitLibro[0];
		String autor = splitLibro[1].isEmpty() ? "Nessun autore" : splitLibro[1];
		String isbn = splitLibro[2].isEmpty() ? "Nessun ISBN" : splitLibro[2];
		String genere = splitLibro[3].isEmpty() ? "Nessun genere" : splitLibro[3];
		int valutazione = !splitLibro[4].isEmpty() ? Integer.parseInt(splitLibro[4]) : 0;
		Stato stato = splitLibro[5].isEmpty() ? Stato.DA_LEGGERE : Stato.valueOf(splitLibro[5]);
		return new Libro(titolo, autor, isbn, genere, valutazione, stato);
	}

	/**
	 * Restituisce l'indice in cui si trova, se presente, il libro identificato da ISBN passato in input.
	 * @param ISBN, il libro da cercare
	 * @return la posizione nella lista in cui si trova il libro se esiste, altrimenti -1
	 */
	private int cercaLibroPerISBN(String ISBN) {
		int found = -1;
		int cnt = 0;
		for (Libro l : super.libri) {
			if(l.getISBN().equals(ISBN)) {
				found = cnt;
				break;
			}
			cnt++;
		}
		return found;
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
	@Override
	public void persist() {
		try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) { // Sovrascrive il file
			for (Libro libro : super.libri) {
				writer.println(convertiInCSV(libro));
			}
		} catch (IOException e) {
			throw new RuntimeException("Errore nel salvare i libri nel file", e);
		}
	}
}
