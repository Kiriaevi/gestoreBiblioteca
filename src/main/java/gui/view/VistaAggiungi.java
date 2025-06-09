package gui.view;

import entities.Libro;
import entities.Stato;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class VistaAggiungi extends JDialog {
    private final JTextField titoloField;
    private final JTextField autoreField;
    private final JTextField isbnField;
    private final JTextField genereField;
    private final JSpinner valutazione;
    private final JComboBox<Stato> statoField;
    private final Consumer<Libro> onSalvaCallback;

    public VistaAggiungi(VistaLibreria parent, Consumer<Libro> onSalvaCallback, String titolo) {
        super(parent, titolo, true);
        this.onSalvaCallback = onSalvaCallback;
        setSize(600, 550);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        titoloField = new JTextField();
        autoreField = new JTextField();
        isbnField = new JTextField();
        genereField = new JTextField();
        valutazione = new JSpinner(new SpinnerNumberModel(0,0,5,1));
        statoField = new JComboBox<>(Stato.values());

        JButton salva = getJButton();

        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(Color.WHITE);
        
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

        add(formPanel);
    }

    private JButton getJButton() {
        JButton salva = new JButton("Salva");
        salva.addActionListener(e -> {
            Libro l = recuperaDati();
            onSalvaCallback.accept(l);
            dispose();
        });
        return salva;
    }
    private Libro recuperaDati() {
        String titolo = titoloField.getText();
        String autore = autoreField.getText();
        String isbn = isbnField.getText();
        String genere = genereField.getText();
        int rating = (int) valutazione.getValue();
        Stato stato = (Stato) statoField.getSelectedItem();
        return new Libro(titolo,autore,isbn,genere,rating,stato);
    }
    public void setTitoloField(String titolo) {
        this.titoloField.setText(titolo);
    }
    public void setAutoreField(String autore) {
        this.autoreField.setText(autore);
    }
    public void setIsbnField(String ISBN) {
        this.isbnField.setText(ISBN);
    }
    public void setGenereField(String genere) {
        this.genereField.setText(genere);
    }
    public void setValutazione(int valutazione) {
        this.valutazione.setValue(valutazione);
    }
    public void setStatoField(Stato stato) { this.statoField.setSelectedItem(stato);}
}