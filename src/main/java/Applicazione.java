import com.formdev.flatlaf.FlatLightLaf;
import gui.controller.ControllerLibreria;
import gui.view.VistaLibreria;
import libreria.memoria.LibreriaAbstract;
import libreria.memoria.LibreriaImpl;

import javax.swing.*;

public class Applicazione {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("FlatLaf not supported");
        }

        String type = args.length > 0 ? args[0] : "csv";
        String filename = args.length > 1 ? args[1] : "libri";
        LibreriaAbstract libreria = new LibreriaImpl(type, filename);

        VistaLibreria v = new VistaLibreria();
        SwingUtilities.invokeLater(() -> v.setVisible(true));
        ControllerLibreria c = new ControllerLibreria(libreria, v);
    }
}
