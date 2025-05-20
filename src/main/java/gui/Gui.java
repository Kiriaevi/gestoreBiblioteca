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

        LibreriaAbstract libreria = new LibreriaImpl("json");
        VistaLibreria v = new VistaLibreria();
        SwingUtilities.invokeLater(() -> v.setVisible(true));
        ControllerLibreria c = new ControllerLibreria(libreria, v);
    }
}
