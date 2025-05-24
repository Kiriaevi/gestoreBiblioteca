package commands;

import libreria.memoria.LibreriaAbstract;

public abstract class CommandAbstract implements Command{
    protected final LibreriaAbstract libreria;
    public CommandAbstract(LibreriaAbstract libreria) {
        this.libreria = libreria;
    }
}
