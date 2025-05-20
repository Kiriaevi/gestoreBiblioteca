package gui.controller;

import commands.Command;
import commands.CommandAggiungiLibro;
import commands.CommandModificaLibro;
import commands.CommandRimuoviLibro;
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
import java.util.List;

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
    public ControllerLibreria(LibreriaAbstract libreria, VistaLibreria vista) {
        this.libreria = libreria;
        this.vista = vista;
        onInit();
    }
    private void onInit() {
        this.vista.setSearchButtonListener(this::cercaLibro);
        popolaLibreria();
    }

    private void cercaLibro(ActionEvent actionEvent) {
        Query q = vista.recuperaDatiDiRicerca();
        Filtro filtro = new FiltroBase();

        if(backHome) {
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

        List<Libro> ret = libri.stream().filter(filtro::filtro).toList();
        vista.setSearchButtonText("Torna alla Home");
        vista.mostraRisultatiRicerca(ret);
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
            Command cmd = new CommandModificaLibro(libreria, libro, libroSorg.getISBN());
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
        libri = libreria.getLibri(Integer.MAX_VALUE);
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
        libri = libreria.getLibri(Integer.MAX_VALUE);
        vista.addBooks(libri);
        aggiungiListeners();
    }
    private void aggiungiListeners() {
        vista.setEditButtonListener(this::modificaLibro);
        vista.setAddButtonListener(e -> aggiungiLibro());
        vista.setDeleteButtonListener(this::eliminaLibro);
    }
}