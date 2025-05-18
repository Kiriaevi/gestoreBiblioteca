package commands;

import entities.Libro;
import libreriaInMemoria.LibreriaAbstract;

public class CommandAggiungiLibro extends CommandAbstract{
    private final Libro l;
    public CommandAggiungiLibro(LibreriaAbstract lib, Libro l) {
        super(lib);
        this.l = l;
    }

    @Override
    public void execute() {
        super.libreria.aggiungiLibro(l);
    }
}
