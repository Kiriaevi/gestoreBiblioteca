package commands;

import entities.Libro;
import libreria.memoria.LibreriaAbstract;

public class CommandRimuoviLibro extends CommandAbstract{
    private final Libro l;
    public CommandRimuoviLibro(LibreriaAbstract lib, Libro l) {
        super(lib);
        this.l = l;
    }

    @Override
    public void execute() {
        super.libreria.eliminaLibro(l);
    }
}
