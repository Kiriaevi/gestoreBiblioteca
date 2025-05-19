package gui.view;

import entities.Libro;

import java.util.function.Consumer;

public class VistaModifica extends VistaAggiungi{

    public VistaModifica(VistaLibreria parent, Consumer<Libro> onSalvaCallback, Libro l) {
        super(parent, onSalvaCallback, "Modifica libro");
        super.setTitoloField(l.getTitolo());
        super.setAutoreField(l.getAutore());
        super.setIsbnField(l.getISBN());
        super.setGenereField(l.getGenere());
        super.setStatoField(l.getStato());
    }
}
