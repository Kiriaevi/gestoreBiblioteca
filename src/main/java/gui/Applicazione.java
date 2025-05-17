package gui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.util.SystemInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Applicazione extends JFrame {

    private final JTextField searchField = new JTextField();
    private final JComboBox<String> authorCombo = new JComboBox<>();
    private final JComboBox<String> categoryCombo = new JComboBox<>();
    private final JTable table = new JTable();
    private final DefaultTableModel model;

    public Applicazione() {
        super("Library Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // NORTH PANEL - Filters
        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        searchField.setPreferredSize(new Dimension(150, 30));
        authorCombo.setPreferredSize(new Dimension(100, 30));
        categoryCombo.setPreferredSize(new Dimension(100, 30));

        gbc.gridx = 0;
        filterPanel.add(searchField, gbc);
        gbc.gridx = 1;
        filterPanel.add(authorCombo, gbc);
        gbc.gridx = 2;
        filterPanel.add(categoryCombo, gbc);

        add(filterPanel, BorderLayout.NORTH);

        // CENTER PANEL - Table
        model = new DefaultTableModel(new String[]{"Title", "Author", "Category"}, 0);
        table.setModel(model);
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // SOUTH PANEL - Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Populate data
        addBook("Il Nome della Rosa", "Umberto Eco", "Storico");
        addBook("La Divina Commedia", "Dante Allighieri", "Classico");
        addBook("1984", "George Orwell", "Fantascienza");
        addBook("Il Signore degli Anelli", "J.R.R. Tolkien", "Fantasy");
        addBook("Harry Potter", "J.K. Rowling", "Fantasy");
        addBook("Il Gattopardo", "Giuseppe Tomasi di Lampedusa", "Storico");

        updateFilters();
    }

    private void addBook(String title, String author, String category) {
        model.addRow(new Object[]{title, author, category});
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

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("FlatLaf non supportato");
        }

        SwingUtilities.invokeLater(() -> new Applicazione().setVisible(true));
    }
}

