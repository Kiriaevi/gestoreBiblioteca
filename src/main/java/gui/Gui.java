package gui;

import com.formdev.flatlaf.FlatLightLaf;
import gui.controller.ControllerLibreria;
import gui.vista.VistaLibreria;
import libreriaInMemoria.LibreriaAbstract;
import libreriaInMemoria.LibreriaImpl;

import javax.swing.*;
import java.awt.*;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class Gui extends JFrame {

    private final JTextField searchField = new JTextField();
    private final JComboBox<String> authorCombo = new JComboBox<>();
    private final JComboBox<String> categoryCombo = new JComboBox<>();
    private final JComboBox<String> statusCombo = new JComboBox<>();
    private final JPanel cardsPanel = new JPanel();
    private final DefaultTableModel model;
    private final int MAX_COLUMNS = 4;

    public Gui() {
        super("Library Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 700);
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

        // Model for data storage
        model = new DefaultTableModel(new String[]{
                "Title", "Author", "Category", "ISBN", "Genre", "Rating", "Status"
        }, 0);

        // SOUTH PANEL - Buttons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // Populate with sample data
        addSampleBooks();
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
        panel.add(new JLabel("Search:"), gbc);
        gbc.gridx = 1;
        panel.add(searchField, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 3;
        panel.add(authorCombo, gbc);
        gbc.gridx = 4;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 5;
        panel.add(categoryCombo, gbc);
        gbc.gridx = 6;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 7;
        panel.add(statusCombo, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addBtn = new JButton("Add Book");
        JButton editBtn = new JButton("Edit Book");
        JButton deleteBtn = new JButton("Delete Book");
        JButton toggleViewBtn = new JButton("Toggle View");

        addBtn.addActionListener(e -> showAddBookDialog());
        toggleViewBtn.addActionListener(e -> toggleView());
        editBtn.addActionListener(e -> editDialog());

        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);
        panel.add(toggleViewBtn);

        return panel;
    }

    private void editDialog() {
        // dici al controller di modificare il libro

        // recupera i libri

        // aggiorna
    }

    private void addSampleBooks() {
        addBook("Il Nome della Rosa", "Umberto Eco", "Storico",
                "978-8845207013", "Historical Fiction", 4.5f, "LETTO");
        addBook("La Divina Commedia", "Dante Alighieri", "Classico",
                "978-8804497545", "Poetry", 5.0f, "LETTO");
        addBook("1984", "George Orwell", "Fantascienza",
                "978-8804668235", "Dystopian", 4.8f, "LETTO");
        addBook("Il Signore degli Anelli", "J.R.R. Tolkien", "Fantasy",
                "978-8845207891", "High Fantasy", 4.7f, "IN LETTURA");
        addBook("Harry Potter e la Pietra Filosofale", "J.K. Rowling", "Fantasy",
                "978-8877827023", "Young Adult", 4.6f, "DA LEGGERE");
        addBook("Il Gattopardo", "Giuseppe Tomasi di Lampedusa", "Storico",
                "978-8807885336", "Historical Novel", 4.3f, "DA LEGGERE");
        addBook("Orgoglio e Pregiudizio", "Jane Austen", "Classico",
                "978-8807900052", "Romance", 4.2f, "LETTO");
        addBook("Cime tempestose", "Emily Brontë", "Classico",
                "978-8806225404", "Gothic Fiction", 4.1f, "LETTO");
    }

    private void addBook(String title, String author, String category,
                         String isbn, String genre, float rating, String status) {
        model.addRow(new Object[]{title, author, category, isbn, genre, rating, status});
    }

    private void updateFilters() {
        Vector<String> authors = new Vector<>();
        Vector<String> categories = new Vector<>();

        authors.add("All");
        categories.add("All");

        for (int i = 0; i < model.getRowCount(); i++) {
            String author = (String) model.getValueAt(i, 1);
            String category = (String) model.getValueAt(i, 2);
            if (!authors.contains(author)) authors.add(author);
            if (!categories.contains(category)) categories.add(category);
        }

        authorCombo.setModel(new DefaultComboBoxModel<>(authors));
        categoryCombo.setModel(new DefaultComboBoxModel<>(categories));
    }

    private void refreshCards() {
        cardsPanel.removeAll();

        int columns = calculateColumnCount();
        cardsPanel.setLayout(new GridLayout(0, columns, 15, 15));

        for (int i = 0; i < model.getRowCount(); i++) {
            String title = (String) model.getValueAt(i, 0);
            String author = (String) model.getValueAt(i, 1);
            String category = (String) model.getValueAt(i, 2);
            String isbn = (String) model.getValueAt(i, 3);
            String genre = (String) model.getValueAt(i, 4);
            float rating = (float) model.getValueAt(i, 5);
            String status = (String) model.getValueAt(i, 6);

            JPanel card = createBookCard(title, author, category, isbn, genre, rating, status);
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
                                  String isbn, String genre, float rating, String status) {
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

        detailsPanel.add(createDetailLabel("Author: " + author));
        detailsPanel.add(createDetailLabel("Category: " + category));
        detailsPanel.add(createDetailLabel("Genre: " + genre));
        detailsPanel.add(createDetailLabel("ISBN: " + isbn));

        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        ratingPanel.setBackground(Color.WHITE);
        ratingPanel.add(new JLabel("Rating: "));
        for (int i = 1; i <= 5; i++) {
            JLabel star = new JLabel(i <= rating ? "★" : "☆");
            star.setForeground(i <= rating ? new Color(255, 215, 0) : Color.GRAY);
            star.setFont(new Font("SansSerif", Font.PLAIN, 16));
            ratingPanel.add(star);
        }
        ratingPanel.add(new JLabel(String.format(" (%.1f)", rating)));
        detailsPanel.add(ratingPanel);

        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        switch (status) {
            case "LETTO":
                statusLabel.setForeground(new Color(0, 128, 0));
                break;
            case "IN LETTURA":
                statusLabel.setForeground(new Color(0, 0, 255));
                break;
            case "DA LEGGERE":
                statusLabel.setForeground(new Color(255, 165, 0));
                break;
        }

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(statusLabel, BorderLayout.SOUTH);

        return card;
    }

    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return label;
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

    private int calculateColumnCount() {
        int screenWidth = this.getWidth();
        if (screenWidth == 0) screenWidth = 900;

        Dimension cardSize = calculateCardSize();
        int cardWidth = cardSize.width + 30;

        int columns = Math.max(1, screenWidth / cardWidth);

        if ((this.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
            columns = Math.max(2, columns);
        }
        return Math.min(columns, MAX_COLUMNS);
    }

    @Override
    public void setExtendedState(int state) {
        super.setExtendedState(state);
        if ((state & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
            refreshCards();
        }
    }

    private void showAddBookDialog() {
        JDialog dialog = new JDialog(this, "Add New Book", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(0, 1, 10, 10));

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField genreField = new JTextField();
        JSpinner ratingSpinner = new JSpinner(new SpinnerNumberModel(3.0, 0.0, 5.0, 0.5));
        JComboBox<String> statusCombo = new JComboBox<>(
                new String[]{"DA LEGGERE", "LETTO", "IN LETTURA"});

        dialog.add(new JLabel("Title:"));
        dialog.add(titleField);
        dialog.add(new JLabel("Author:"));
        dialog.add(authorField);
        dialog.add(new JLabel("Category:"));
        dialog.add(categoryField);
        dialog.add(new JLabel("ISBN:"));
        dialog.add(isbnField);
        dialog.add(new JLabel("Genre:"));
        dialog.add(genreField);
        dialog.add(new JLabel("Rating (0-5):"));
        dialog.add(ratingSpinner);
        dialog.add(new JLabel("Status:"));
        dialog.add(statusCombo);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            addBook(titleField.getText(), authorField.getText(), categoryField.getText(),
                    isbnField.getText(), genreField.getText(),
                    (float) ratingSpinner.getValue(),
                    (String) statusCombo.getSelectedItem());
            updateFilters();
            refreshCards();
            dialog.dispose();
        });

        dialog.add(saveBtn);
        dialog.setVisible(true);
    }

    private void toggleView() {
        JOptionPane.showMessageDialog(this, "View toggled - implement table/card switching");
    }

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
