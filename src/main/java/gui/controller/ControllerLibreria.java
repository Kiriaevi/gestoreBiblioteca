package gui.controller;

import commands.Command;
import commands.CommandAggiungiLibro;
import commands.CommandModificaLibro;
import commands.CommandRimuoviLibro;
import comparators.OrdinamentoAutore;
import comparators.OrdinamentoTitolo;
import comparators.OrdinamentoValutazione;
import entities.Libro;
import entities.Query;
import gui.view.VistaAggiungi;
import gui.view.VistaLibreria;
import gui.view.VistaModifica;
import libreriaInMemoria.LibreriaAbstract;
import ricerca.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Comparator;

/**
 * La classe ControllerLibreria funge da controller in un'architettura MVC
 * per gestire le operazioni della biblioteca all'interno di un'interfaccia grafica.
 * Gestisce le interazioni tra il modello (LibreriaAbstract) e la vista (VistaLibreria).
 */
public class ControllerLibreria {
    private  final LibreriaAbstract libreria;
    private final VistaLibreria vista;
    private Collection<Libro> libri = null;
    private boolean backHome = false;
    private Comparator<Libro> comparatore = new OrdinamentoValutazione(false).ottieniComparatore();
    public ControllerLibreria(LibreriaAbstract libreria, VistaLibreria vista) {
        this.libreria = libreria;
        this.vista = vista;
        onInit();
    }
    private void onInit() {
        this.vista.setSearchButtonListener(this::cercaLibro);
        this.vista.setAutoreOnClickListener(e -> ordinamento("autore"));
        this.vista.setTitoloOnClickListener(e -> ordinamento("titolo"));
        this.vista.setStatoOnClickListener(e -> ordinamento("stato"));
        popolaLibreria();
    }
    private void ordinamento(String criterio) {
        switch (criterio) {
            case "autore":
                this.comparatore = new OrdinamentoAutore(false).ottieniComparatore();
                break;
            case "titolo":
                this.comparatore = new OrdinamentoTitolo(false).ottieniComparatore();
                break;
            case "stato":
                this.comparatore = new OrdinamentoValutazione(false).ottieniComparatore();
                break;
            default:
                this.comparatore = new OrdinamentoValutazione(true).ottieniComparatore();
        }
        vista.mostraRisultatiRicerca(libri.stream().sorted(comparatore).toList());

    }
    /**
     * Gestisce l'azione di ricerca dei libri in base ai criteri definiti dall'utente e aggiorna la vista di conseguenza.
     * Se viene avviata la ricerca, applica una serie di filtri alla lista dei libri.
     * Se viene premuto il pulsante "Torna alla Home", reimposta i criteri di ricerca e i risultati, tornando allo stato iniziale.
     *
     */
    private void cercaLibro(ActionEvent actionEvent) {
        Query q = vista.recuperaDatiDiRicerca();
        Filtro filtro = new FiltroBase();

        //TODO: SE AGGIUNGI VIRGOLETTE NON CERCA ', FARE ESCAPING DEI CARATTERI
        if(backHome) {
            libri = ottieniLibriDallaLibreria();
            vista.setSearchButtonText("Cerca");
            vista.mostraRisultatiRicerca(libri.stream().filter(filtro::filtro).toList());
            vista.pulisciRicerca();
            backHome = false;
            return;
        }

        if (q.titolo() != null && !q.titolo().trim().isEmpty())
            filtro = new FiltroTitolo(filtro, q.titolo());
        if (q.autore() != null && !q.autore().equals("Qualsiasi"))
            filtro = new FiltroAutore(filtro, q.autore());
        if (q.categoria() != null && !q.categoria().equals("Qualsiasi"))
            filtro = new FiltroGenere(filtro, q.categoria());
        if (q.stato() != null)
            filtro = new FiltroStato(filtro, q.stato());

        libri = libri.stream().filter(filtro::filtro).sorted(comparatore).toList();
        vista.setSearchButtonText("Torna alla Home");
        vista.mostraRisultatiRicerca(libri);
        backHome = true;
    }
    private void aggiungiLibro() {
        VistaAggiungi vistaAggiungi = new VistaAggiungi(vista, libro -> {
            Command cmd = new CommandAggiungiLibro(libreria, libro);
            cmd.execute();
            postAggiuntaLibro();
        }, "Aggiungi libro");
        vistaAggiungi.setVisible(true);
    }
    private void modificaLibro(ActionEvent e) {
        Libro libroSorg = ottieniLibro((JButton) e.getSource());
        VistaModifica vistaModifica = new VistaModifica(vista, libro -> {
            Command cmd = new CommandModificaLibro(libreria, libro, libroSorg.isbn());
            cmd.execute();
            aggiorna();
        }, libroSorg);
        vistaModifica.setVisible(true);
    }
    private void eliminaLibro(ActionEvent e) {
        Libro libroSorg = ottieniLibro((JButton) e.getSource());
        Command cmd = new CommandRimuoviLibro(libreria, libroSorg);
        cmd.execute();
        aggiorna();
    }
    private void popolaLibreria() {
        libri = ottieniLibriDallaLibreria();
        vista.addBooks(libri);
        aggiungiListeners();
    }
    private void postAggiuntaLibro() {
        vista.libroAggiunto();
        vista.pulisciMatrice();
        popolaLibreria();
    }
    private Libro ottieniLibro(JButton bottone) {
        return vista.getLibroSelezionato(bottone);
    }
    private void aggiorna() {
        vista.pulisciMatrice();
        libri = ottieniLibriDallaLibreria();
        vista.addBooks(libri);
        aggiungiListeners();
    }
    private void aggiungiListeners() {
        vista.setEditButtonListener(this::modificaLibro);
        vista.setAddButtonListener(e -> aggiungiLibro());
        vista.setDeleteButtonListener(this::eliminaLibro);
    }
    private Collection<Libro> ottieniLibriDallaLibreria() {
        return libreria.getLibri(Integer.MAX_VALUE).stream().sorted(this.comparatore).toList();
    }
}