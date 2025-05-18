package gui.view;

import entities.Libro;
import entities.Stato;

import javax.swing.*;
import java.awt.*;

public class VistaAggiungi extends JDialog {
    private final JTextField titoloField;
    private final JTextField autoreField;
    private final JTextField isbnField;
    private final JTextField genereField;
    private final JSpinner valutazione;
    private final JComboBox<Stato> statoField;

    public VistaAggiungi(VistaLibreria parent) {
        super(parent, "Nuovo Libro", true);
        setSize(600, 550);
        
        // Creiamo il pannello principale con il suo BoxLayout
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        titoloField = new JTextField();
        autoreField = new JTextField();
        isbnField = new JTextField();
        genereField = new JTextField();
        valutazione = new JSpinner(new SpinnerNumberModel(0,0,5,1));
        statoField = new JComboBox<>(Stato.values());

        JButton salva = new JButton("Salva");
        salva.addActionListener(e -> {
            String titolo = titoloField.getText();
            String autore = autoreField.getText();
            String isbn = isbnField.getText();
            String genere = genereField.getText();
            int rating = (int) valutazione.getValue();
            Stato stato = (Stato) statoField.getSelectedItem();

            Libro l = new Libro(titolo, autore, isbn, genere, rating, stato);
            parent.libroAggiunto(l);
            dispose();
        });

        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(Color.WHITE);
        
        // Aggiungiamo i componenti al pannello
        formPanel.add(new JLabel("Titolo:"));
        formPanel.add(titoloField);
        formPanel.add(new JLabel("Autore:"));
        formPanel.add(autoreField);
        formPanel.add(new JLabel("ISBN:"));
        formPanel.add(isbnField);
        formPanel.add(new JLabel("Genere:"));
        formPanel.add(genereField);
        formPanel.add(new JLabel("Valutazione (0–5):"));
        formPanel.add(valutazione);
        formPanel.add(new JLabel("Stato:"));
        formPanel.add(statoField);

        JPanel bottoniPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottoniPanel.setBackground(Color.WHITE);
        
        JButton annulla = new JButton("Annulla");
        annulla.addActionListener(e -> dispose());
        
        bottoniPanel.add(annulla);
        bottoniPanel.add(salva);

        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(bottoniPanel);

        // Aggiungiamo il pannello principale al dialog
        add(formPanel);
    }
}