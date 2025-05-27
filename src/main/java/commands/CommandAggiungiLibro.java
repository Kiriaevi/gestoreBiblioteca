package commands;

import entities.Libro;
import libreria.memoria.LibreriaAbstract;

import java.io.IOException;

public class CommandAggiungiLibro extends CommandAbstract{
    private final Libro l;
    public CommandAggiungiLibro(LibreriaAbstract lib, Libro l) {
        super(lib);
        this.l = l;
    }

    @Override
    public boolean execute() {
        try {
            return super.libreria.aggiungiLibro(l);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
