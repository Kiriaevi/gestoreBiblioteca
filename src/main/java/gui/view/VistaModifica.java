package gui.view;

import entities.Libro;

import java.util.function.Consumer;

public class VistaModifica extends VistaAggiungi{

    public VistaModifica(VistaLibreria parent, Consumer<Libro> onSalvaCallback, Libro l) {
        super(parent, onSalvaCallback, "Modifica libro");
        super.setTitoloField(l.titolo());
        super.setAutoreField(l.autore());
        super.setIsbnField(l.isbn());
        super.setGenereField(l.genere());
        super.setStatoField(l.stato());
    }
}
