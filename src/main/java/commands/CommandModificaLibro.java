package commands;

import entities.Libro;
import libreria.memoria.LibreriaAbstract;

import java.io.IOException;

public class CommandModificaLibro extends CommandAbstract{
    private final Libro l;
    private final String ISBN;
    public CommandModificaLibro(LibreriaAbstract libreria, Libro l, String ISBN) {
        super(libreria);
        this.l = l;
        this.ISBN = ISBN;
    }

    @Override
    public boolean execute() {
        try {
            super.libreria.modificaLibro(l, ISBN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
