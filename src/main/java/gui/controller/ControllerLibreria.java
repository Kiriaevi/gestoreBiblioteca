package gui.controller;

import commands.Command;
import commands.CommandAggiungiLibro;
import gui.view.VistaAggiungi;
import gui.view.VistaLibreria;
import libreriaInMemoria.LibreriaAbstract;

public class ControllerLibreria {
    private  final LibreriaAbstract libreria;
    private final VistaLibreria vista;

    public ControllerLibreria(LibreriaAbstract libreria, VistaLibreria vista) {
        this.libreria = libreria;
        this.vista = vista;

        vista.setEditButtonListener(e -> modificaLibro());
        vista.setAddButtonListener(e -> aggiungiLibro());
        vista.addBooks(libreria.getLibri(Integer.MAX_VALUE));
    }
    private void aggiungiLibro() {
        // <- qui dovrei usare command per creare un comando di aggiunta libro
        VistaAggiungi vistaAggiungi = new VistaAggiungi(vista, libro -> {
            Command cmd = new CommandAggiungiLibro(libreria, libro);
            cmd.execute();
            vista.libroAggiunto(libro);
            vista.ricaricaLibri(libreria.getLibri(Integer.MAX_VALUE));
        });
        vistaAggiungi.setVisible(true);

    }
    private void modificaLibro() {

    }
}
