package commands;

import entities.Libro;
import libreria.memoria.LibreriaAbstract;

import java.io.IOException;

public class CommandRimuoviLibro extends CommandAbstract{
    private final Libro l;
    public CommandRimuoviLibro(LibreriaAbstract lib, Libro l) {
        super(lib);
        this.l = l;
    }

    @Override
    public boolean execute() {
        try {
            super.libreria.eliminaLibro(l);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
