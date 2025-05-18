package gui.controller;

import gui.vista.VistaLibreria;
import libreriaInMemoria.LibreriaAbstract;

public class ControllerLibreria {
    private  final LibreriaAbstract libreria;
    private final VistaLibreria vista;

    public ControllerLibreria(LibreriaAbstract libreria, VistaLibreria vista) {
        this.libreria = libreria;
        this.vista = vista;

        vista.setEditButtonListener(e -> modificaLibro());
        vista.addBooks(libreria.getLibri(Integer.MAX_VALUE));
    }
    private void modificaLibro() {

    }
}
