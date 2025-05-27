package commands;

import entities.Libro;
import libreria.memoria.LibreriaAbstract;

import java.io.IOException;

public class CommandModificaLibro extends CommandAbstract{
    private final Libro libroDaAggiungere;
    private final Libro libroDaEliminare;
    public CommandModificaLibro(LibreriaAbstract libreria, Libro libroDaAggiungere, Libro libroDaEliminare) {
        super(libreria);
        this.libroDaAggiungere = libroDaAggiungere;
        this.libroDaEliminare = libroDaEliminare;
    }

    @Override
    public boolean execute() {
        try {
           return super.libreria.modificaLibro(libroDaAggiungere, libroDaEliminare);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
