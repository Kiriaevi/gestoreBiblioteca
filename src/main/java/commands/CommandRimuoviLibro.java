package commands;

import entities.Libro;
import libreriaInMemoria.LibreriaAbstract;

public class CommandRimuoviLibro extends CommandAbstract{
    private final Libro l;
    public CommandRimuoviLibro(LibreriaAbstract lib, Libro l) {
        super(lib);
        this.l = l;
    }

    @Override
    public void execute() {
        System.out.println(l);
       // super.libreria.eliminaLibro(l);
    }
}
