package gui.view;

import entities.Libro;
import entities.Stato;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Vector;

public class VistaLibreria extends JFrame{

    private final JTextField searchField = new JTextField();
    private final JComboBox<String> authorCombo = new JComboBox<>();
    private final int MAX_COLUMNS = 4;
    private final JComboBox<String> categoryCombo = new JComboBox<>();
    private final JComboBox<String> statusCombo = new JComboBox<>();
    private final JPanel cardsPanel = new JPanel();
    private final DefaultTableModel model;
    JButton editBtn = new JButton("Edit Book");
    public VistaLibreria() {
        super("Libreria");
        model = new DefaultTableModel(new String[]{
                "Titolo", "Autore", "ISBN", "Categoria", "Genere", "Valutazione", "Stato"
        }, 0);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // NORTH PANEL - Filters
        JPanel filterPanel = createFilterPanel();
        add(filterPanel, BorderLayout.NORTH);

        // CENTER PANEL - Cards Grid
        cardsPanel.setLayout(new GridLayout(0, MAX_COLUMNS, 15, 15));
        cardsPanel.setBackground(new Color(240, 240, 240));
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // SOUTH PANEL - Buttons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        updateFilters();
        refreshCards();

    }
    @Override
    public void setExtendedState(int state) {
        super.setExtendedState(state);
        if ((state & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
            refreshCards();
        }
    }
    public void setEditButtonListener(ActionListener listener) {
        editBtn.addActionListener(listener);
    }
    public void addBooks(Collection<Libro> libri) {
        model.setRowCount(0);
        for (Libro l : libri) {
            model.addRow(new Object[] {
                    l.getTitolo(),
                    l.getAutore(),
                    l.getGenere(),
                    l.getISBN(),
                    l.getValutazione(),
                    l.getStato()
            });
        }
        updateFilters();
        refreshCards();
    }
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        searchField.setPreferredSize(new Dimension(150, 30));
        authorCombo.setPreferredSize(new Dimension(120, 30));
        categoryCombo.setPreferredSize(new Dimension(120, 30));
        statusCombo.setPreferredSize(new Dimension(120, 30));

        statusCombo.setModel(new DefaultComboBoxModel<>(
                new String[]{"All", "DA LEGGERE", "LETTO", "IN LETTURA"}));

        gbc.gridx = 0;
        panel.add(new JLabel("Cerca:"), gbc);
        gbc.gridx = 1;
        panel.add(searchField, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Autore:"), gbc);
        gbc.gridx = 3;
        panel.add(authorCombo, gbc);
        gbc.gridx = 4;
        panel.add(new JLabel("Genere:"), gbc);
        gbc.gridx = 5;
        panel.add(categoryCombo, gbc);
        gbc.gridx = 6;
        panel.add(new JLabel("Stato:"), gbc);
        gbc.gridx = 7;
        panel.add(statusCombo, gbc);

        return panel;
    }
    public void libroAggiunto(Libro l) {
        JOptionPane.showMessageDialog(this, "Libro aggiunto: " + l);
        // qui aggiorni la lista o la vista se serve
    }
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addBtn = new JButton("Add Book");
        JButton deleteBtn = new JButton("Delete Book");
        JButton toggleViewBtn = new JButton("Toggle View");


        addBtn.addActionListener(e -> apriForm());
        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);
        panel.add(toggleViewBtn);

        return panel;
    }
    private void apriForm() {
        VistaAggiungi form = new VistaAggiungi(this);
        form.setVisible(true);
    }
    private void refreshCards() {
        cardsPanel.removeAll();

        int columns = 4;
        cardsPanel.setLayout(new GridLayout(0, columns, 15, 15));

        for (int i = 0; i < model.getRowCount(); i++) {
            String title = (String) model.getValueAt(i, 0);
            String author = (String) model.getValueAt(i, 1);
            String category = (String) model.getValueAt(i, 2);
            String isbn = (String) model.getValueAt(i, 3);
            int rating = (int) model.getValueAt(i, 4);
            Stato status = (Stato) model.getValueAt(i, 5);

            JPanel card = createBookCard(title, author, category, isbn, rating, status);
            cardsPanel.add(card);
        }

        int remainder = model.getRowCount() % columns;
        if (remainder != 0) {
            for (int i = 0; i < columns - remainder; i++) {
                cardsPanel.add(new JPanel());
            }
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }
    private JPanel createBookCard(String title, String author, String category,
                                  String isbn, float rating, Stato status) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(5, 5));

        Dimension cardSize = calculateCardSize();
        card.setPreferredSize(cardSize);
        card.setMaximumSize(cardSize);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(0, 1, 5, 5));
        detailsPanel.setBackground(Color.WHITE);

        detailsPanel.add(createDetailLabel("Autore: " + author));
        detailsPanel.add(createDetailLabel("Genere: " + category));
        detailsPanel.add(createDetailLabel("ISBN: " + isbn));

        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        ratingPanel.setBackground(Color.WHITE);
        ratingPanel.add(new JLabel("Valutazione: "));
        for (int i = 1; i <= 5; i++) {
            JLabel star = new JLabel(i <= rating ? "★" : "☆");
            star.setForeground(i <= rating ? new Color(255, 215, 0) : Color.GRAY);
            star.setFont(new Font("SansSerif", Font.PLAIN, 16));
            ratingPanel.add(star);
        }
        ratingPanel.add(new JLabel(String.format(" (%.1f)", rating)));
        detailsPanel.add(ratingPanel);

        JLabel statusLabel = new JLabel(String.valueOf(status));
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        switch (status) {
            case Stato.LETTO:
                statusLabel.setForeground(new Color(0, 128, 0));
                break;
            case Stato.IN_LETTURA:
                statusLabel.setForeground(new Color(0, 0, 255));
                break;
            case Stato.DA_LEGGERE:
                statusLabel.setForeground(new Color(255, 165, 0));
                break;
        }
        JButton edit = new JButton("Modifica");

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(detailsPanel, BorderLayout.CENTER);

        JPanel inferiore = new JPanel(new BorderLayout());
        inferiore.add(statusLabel, BorderLayout.WEST);
        inferiore.add(edit, BorderLayout.EAST);
        inferiore.setBackground(Color.WHITE);

        card.add(inferiore, BorderLayout.SOUTH);
        return card;
    }
    private Dimension calculateCardSize() {
        int baseWidth = 250;
        int baseHeight = 180;
        int maxWidth = (int)(Toolkit.getDefaultToolkit().getScreenSize().width * 0.3);

        if ((this.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
            baseWidth = Math.min(350, maxWidth);
            baseHeight = 220;
        }
        return new Dimension(baseWidth, baseHeight);
    }
    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return label;
    }
    private void updateFilters() {
        Vector<String> authors = new Vector<>();
        Vector<String> categories = new Vector<>();

        authors.add("All");
        categories.add("All");

        for (int i = 0; i < model.getRowCount(); i++) {
            String autore = (String) model.getValueAt(i, 1);
            String genere = (String) model.getValueAt(i, 3);
            if (!authors.contains(autore)) authors.add(autore);
            if (!categories.contains(genere)) categories.add(genere);
        }

        authorCombo.setModel(new DefaultComboBoxModel<>(authors));
        categoryCombo.setModel(new DefaultComboBoxModel<>(categories));
    }
}
