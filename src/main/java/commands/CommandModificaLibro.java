package commands;

import entities.Libro;
import libreria.memoria.LibreriaAbstract;

public class CommandModificaLibro extends CommandAbstract{
    private final Libro l;
    private final String ISBN;
    public CommandModificaLibro(LibreriaAbstract libreria, Libro l, String ISBN) {
        super(libreria);
        this.l = l;
        this.ISBN = ISBN;
    }

    @Override
    public void execute() {
        super.libreria.modificaLibro(l, ISBN);
    }
}
