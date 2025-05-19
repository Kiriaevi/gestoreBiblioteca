package gui.controller;

import commands.Command;
import commands.CommandAggiungiLibro;
import commands.CommandModificaLibro;
import commands.CommandRimuoviLibro;
import entities.Libro;
import gui.view.VistaAggiungi;
import gui.view.VistaLibreria;
import gui.view.VistaModifica;
import libreriaInMemoria.LibreriaAbstract;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControllerLibreria {
    private  final LibreriaAbstract libreria;
    private final VistaLibreria vista;

    public ControllerLibreria(LibreriaAbstract libreria, VistaLibreria vista) {
        this.libreria = libreria;
        this.vista = vista;

        vista.addBooks(libreria.getLibri(Integer.MAX_VALUE));
        aggiungiListeners();

    }
    private void aggiungiLibro() {
        // <- qui dovrei usare command per creare un comando di aggiunta libro
        VistaAggiungi vistaAggiungi = new VistaAggiungi(vista, libro -> {
            Command cmd = new CommandAggiungiLibro(libreria, libro);
            cmd.execute();
            vista.libroAggiunto(libro);
            vista.pulisci();
            vista.ricaricaLibri(libreria.getLibri(Integer.MAX_VALUE));
            aggiungiListeners();
        }, "Aggiungi libro");
        vistaAggiungi.setVisible(true);

    }
    private Libro ottieniLibro(JButton bottone) {
        return vista.getLibroSelezionato(bottone);
    }
    private void modificaLibro(ActionEvent e) {
        Libro libroSorg = ottieniLibro((JButton) e.getSource());
        VistaModifica vistaModifica = new VistaModifica(vista, libro -> {
            Command cmd = new CommandModificaLibro(libreria, libro, libroSorg.getISBN());
            cmd.execute();
            aggiorna();
        }, libroSorg);
        vistaModifica.setVisible(true);
    }
    private void eliminaLibro(ActionEvent e) {
        Libro libroSorg = ottieniLibro((JButton) e.getSource());
        Command cmd = new CommandRimuoviLibro(libreria, libroSorg);
        cmd.execute();
        aggiorna();
        System.out.println("Elimina libro");
    }
    private void aggiorna() {
        vista.pulisci();
        vista.ricaricaLibri(libreria.getLibri(Integer.MAX_VALUE));
        aggiungiListeners();
    }
    private void aggiungiListeners() {
        vista.setEditButtonListener(this::modificaLibro);
        vista.setAddButtonListener(e -> aggiungiLibro());
        vista.setDeleteButtonListener(this::eliminaLibro);
    }
}
