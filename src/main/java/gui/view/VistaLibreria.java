package gui.view;

import utility.Utility;
import entities.Libro;
import entities.Query;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class VistaLibreria extends JFrame {
    private final int MAX_COLUMNS = 3;

    private final JTextField searchField = new JTextField();
    private final JTextField authorField = new JTextField();
    private final JTextField categoryField = new JTextField();
    private final JComboBox<String> statusCombo = new JComboBox<>();
    private final JButton searchButton = new JButton("Invia");

    private final JPanel cardsPanel = new JPanel();
    private final List<JButton> editBtns = new LinkedList<>();
    private final List<JButton> deleteBtns = new LinkedList<>();
    private final HashMap<JButton, Libro> libroBottone = new HashMap<>();
    private final JButton addBtn = new JButton("Aggiungi libro");
    private final List<Libro> listaLibri = new ArrayList<>();
    private final JButton[] paginazioneBtns = new JButton[4];
    HashMap<String, JButton> bottoniOrdinamento = new HashMap<>();

    public VistaLibreria() {
        super("Libreria");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel filterPanel = createFilterPanel();
        add(filterPanel, BorderLayout.NORTH);

        bottoniOrdinamento.put("ordinamentoTitolo", new JButton("Titolo"));
        bottoniOrdinamento.put("ordinamentoAutore", new JButton("Autore"));
        bottoniOrdinamento.put("ordinamentoStato", new JButton("Valutazione"));
        // HEADER PANEL - Custom clickable headers
        JPanel headerPanel = new JPanel(new GridLayout(1, 3));
        for (String s : bottoniOrdinamento.keySet()) {
            JButton b = bottoniOrdinamento.get(s);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
            b.setFont(new Font("SansSerif", Font.BOLD, 14));
            b.setBackground(new Color(230, 230, 230));
            b.setOpaque(true);
            b.setBorderPainted(true);
            headerPanel.add(b);
        }

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(headerPanel, BorderLayout.NORTH);

        cardsPanel.setLayout(new GridLayout(0, MAX_COLUMNS, 15, 15));
        Color cardBackground = new Color(240, 240, 240);
        cardsPanel.setBackground(cardBackground);
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // paginazione
        paginazioneBtns[0] = new JButton("<");
        paginazioneBtns[1] = new JButton(">");
        paginazioneBtns[2] = new JButton("<<");
        paginazioneBtns[3] = new JButton(">>");

        add(centerPanel, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
        refreshCards();
    }

    public void setAutoreOnClickListener(ActionListener actionEvent) {
        JButton bottone = bottoniOrdinamento.get("ordinamentoAutore");
        bottone.addActionListener(actionEvent);
    }
    public void setTitoloOnClickListener(ActionListener actionEvent) {
        JButton bottone = bottoniOrdinamento.get("ordinamentoTitolo");
        bottone.addActionListener(actionEvent);
    }
    public void setStatoOnClickListener(ActionListener actionEvent) {
        JButton bottone = bottoniOrdinamento.get("ordinamentoTitolo");
        bottone.addActionListener(actionEvent);
    }
    public void setPaginazioneBtnNext(ActionListener listener, boolean isLast) {
        if(!isLast)
            paginazioneBtns[1].addActionListener(listener);
        else
            paginazioneBtns[3].addActionListener(listener);
    }
    public void setPaginazioneBtnPrevious(ActionListener listener, boolean isFirst) {
        if(!isFirst)
            paginazioneBtns[0].addActionListener(listener);
        else
            paginazioneBtns[2].addActionListener(listener);
    }
    public void setSearchButtonListener(ActionListener listener) {
        this.searchButton.addActionListener(listener);
    }

    public void setSearchButtonText(String testo) {
        this.searchButton.setText(testo);
    }

    public void setEditButtonListener(ActionListener listener) {
        for (JButton button : editBtns) {
            for(ActionListener al : button.getActionListeners())
                button.removeActionListener(al);
            button.addActionListener(listener);
        }
    }

    public void setDeleteButtonListener(ActionListener listener) {
        for (JButton button : deleteBtns) {
            for(ActionListener al : button.getActionListeners())
                button.removeActionListener(al);
            button.addActionListener(listener);
        }
    }

    public void setAddButtonListener(ActionListener listener) {
        for(ActionListener al : addBtn.getActionListeners())
            addBtn.removeActionListener(al);
        addBtn.addActionListener(listener);
    }

    @Override
    public void setExtendedState(int state) {
        super.setExtendedState(state);
        if ((state & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
            refreshCards();
        }
    }

    public Query recuperaDatiDiRicerca() {
        return new Query(
                searchField.getText(),
                authorField.getText(),
                categoryField.getText(),
                Utility.fromStringToStato((String) statusCombo.getSelectedItem()));
    }


    public void addBooks(Collection<Libro> libri) {
        listaLibri.clear();
        listaLibri.addAll(libri);
        refreshCards();
    }

    public Libro getLibroSelezionato(JButton sourceButton) {
        return libroBottone.get(sourceButton);
    }

    public void pulisciMatrice() {
        this.deleteBtns.clear();
        this.editBtns.clear();
        libroBottone.clear();
    }

    public void pulisciRicerca() {
        searchField.setText("");
        authorField.setText("");
        categoryField.setText("");
        statusCombo.setSelectedIndex(0);
    }

    public void libroAggiunto(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void mostraRisultatiRicerca(Collection<Libro> ret) {
        addBooks(ret);
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        searchField.setPreferredSize(new Dimension(150, 30));
        authorField.setPreferredSize(new Dimension(120, 30));
        categoryField.setPreferredSize(new Dimension(120, 30));
        statusCombo.setPreferredSize(new Dimension(120, 30));

        statusCombo.setModel(new DefaultComboBoxModel<>(
                new String[]{"Qualsiasi", "DA LEGGERE", "LETTO", "IN LETTURA"}));

        gbc.gridx = 0;
        panel.add(new JLabel("Cerca:"), gbc);
        gbc.gridx = 1;
        panel.add(searchField, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Autore:"), gbc);
        gbc.gridx = 3;
        panel.add(authorField, gbc);
        gbc.gridx = 4;
        panel.add(new JLabel("Genere:"), gbc);
        gbc.gridx = 5;
        panel.add(categoryField, gbc);
        gbc.gridx = 6;
        panel.add(new JLabel("Stato:"), gbc);
        gbc.gridx = 7;
        panel.add(statusCombo, gbc);
        gbc.gridx = 8;
        panel.add(searchButton, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(new EmptyBorder(2, 5, 15, 5));

        panel.add(paginazioneBtns[0]);                     // <<
        panel.add(Box.createRigidArea(new Dimension(5, 0)));

        panel.add(paginazioneBtns[2]);                     // <
        panel.add(Box.createHorizontalGlue());

        panel.add(addBtn);
        panel.add(Box.createHorizontalGlue());

        panel.add(paginazioneBtns[3]);                     // >
        panel.add(Box.createRigidArea(new Dimension(5, 0)));

        panel.add(paginazioneBtns[1]);                     // >>

        return panel;
    }

    private void refreshCards() {
        cardsPanel.removeAll();
        int columns = MAX_COLUMNS;
        cardsPanel.setLayout(new GridLayout(0, columns, 15, 15));
        for (Libro l : listaLibri) {
            JPanel card = createBookCard(l);
            cardsPanel.add(card);
        }
        int remainder = listaLibri.size() % columns;
        if (remainder != 0) {
            for (int i = 0; i < columns - remainder; i++) {
                cardsPanel.add(new JPanel());
            }
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel createBookCard(Libro libro) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        int cardWidth = 250;
        int cardHeight = 190;
        card.setPreferredSize(new Dimension(cardWidth, cardHeight));
        card.setMaximumSize(new Dimension(cardWidth, cardHeight));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(libro.titolo());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.add(createDetailLabel("Autore: " + libro.autore()));
        detailsPanel.add(createDetailLabel("Genere: " + libro.genere()));
        detailsPanel.add(createDetailLabel("ISBN: " + libro.isbn()));

        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        ratingPanel.setBackground(Color.WHITE);
        ratingPanel.add(new JLabel("Valutazione: "));
        for (int i = 1; i <= 5; i++) {
            JLabel star = new JLabel(i <= libro.valutazione() ? "★" : "☆");
            star.setForeground(i <= libro.valutazione() ? new Color(255, 215, 0) : Color.GRAY);
            star.setFont(new Font("SansSerif", Font.PLAIN, 16));
            ratingPanel.add(star);
        }
        ratingPanel.add(new JLabel(String.format(" (%.1f)", (float) libro.valutazione())));
        detailsPanel.add(ratingPanel);

        JLabel statusLabel = new JLabel(String.valueOf(libro.stato()));
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        switch (libro.stato()) {
            case LETTO -> statusLabel.setForeground(new Color(0, 128, 0));
            case IN_LETTURA -> statusLabel.setForeground(new Color(0, 0, 255));
            case DA_LEGGERE -> statusLabel.setForeground(new Color(255, 165, 0));
        }

        JButton edit = new JButton("Modifica");
        JButton delete = new JButton("Elimina");
        this.editBtns.add(edit);
        this.deleteBtns.add(delete);
        libroBottone.put(edit, libro);
        libroBottone.put(delete, libro);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(detailsPanel, BorderLayout.CENTER);

        JPanel inferiore = new JPanel(new BorderLayout());
        JPanel sceltaInferiore = new JPanel(new BorderLayout());
        inferiore.add(statusLabel, BorderLayout.WEST);
        sceltaInferiore.add(edit, BorderLayout.WEST);
        sceltaInferiore.add(delete, BorderLayout.EAST);
        sceltaInferiore.setBackground(Color.WHITE);
        inferiore.add(sceltaInferiore, BorderLayout.EAST);
        inferiore.setBackground(Color.WHITE);
        card.add(inferiore, BorderLayout.SOUTH);

        return card;
    }

    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return label;
    }


    public void enablePaginationButtons(boolean b) {
        paginazioneBtns[0].setEnabled(b);
        paginazioneBtns[1].setEnabled(b);
    }

}