package gui;

import com.formdev.flatlaf.FlatLightLaf;
import gui.controller.ControllerLibreria;
import gui.view.VistaLibreria;
import libreriaInMemoria.LibreriaAbstract;
import libreriaInMemoria.LibreriaImpl;

import javax.swing.*;

public class Gui {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("FlatLaf not supported");
        }

        LibreriaAbstract libreria = new LibreriaImpl("csv");
        VistaLibreria v = new VistaLibreria();
        libreria.loadAll();
        ControllerLibreria c = new ControllerLibreria(libreria, v);
        SwingUtilities.invokeLater(() -> v.setVisible(true));
    }
}
