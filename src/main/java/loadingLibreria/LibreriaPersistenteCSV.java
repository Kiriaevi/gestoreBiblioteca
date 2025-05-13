package loadingLibreria;

import java.io.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import entities.Libro;
import entities.Stato;
import exceptions.DocumentoMalFormatoException;

public class LibreriaPersistenteCSV extends LibreriaPersistenteAbstract{

	public LibreriaPersistenteCSV() {
		super();
	}
	@Override
	public List<Libro> leggiLibro(int size) throws IOException {
		// FIXME: gestire meglio il lettore da file
		BufferedReader reader = new BufferedReader(new FileReader(super.fileName));
		for (int i = 0; i < size; i++) {
			String book = reader.readLine();
			super.libri.add(book);
		}
		reader.close();
		return super.libri.stream().map(this::convertiInLibro).toList();
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
	private Libro convertiInLibro(String libro) {
		String[] splitLibro = libro.split(",");
		if(splitLibro.length < 6)
			throw new DocumentoMalFormatoException("Il documento passato non è correttamente formattato, dovrebbero esserci 6 campi!");
		//FIXME: tutti i dati dovrebbero venire compilati?
		Libro.LibroBuilder libroBuilder = Libro.builder();
		String titolo = splitLibro[0].isEmpty() ? "Nessun titolo trovato" : splitLibro[0];
		String autor = splitLibro[1].isEmpty() ? "Nessun autore" : splitLibro[1];
		String isbn = splitLibro[2].isEmpty() ? "Nessun ISBN" : splitLibro[2];
		String genere = splitLibro[3].isEmpty() ? "Nessun genere" : splitLibro[3];
		int valutazione = !splitLibro[4].isEmpty() ? Integer.parseInt(splitLibro[4]) : 0;
		Stato stato = splitLibro[5].isEmpty() ? Stato.DA_LEGGERE : Stato.valueOf(splitLibro[5]);

		libroBuilder.setTitolo(titolo);
		libroBuilder.setAutore(autor);
		libroBuilder.setISBN(isbn);
		libroBuilder.setGenere(genere);
		libroBuilder.setValutazione(valutazione);
		libroBuilder.setStato(stato);
		return libroBuilder.build();
	}
}
