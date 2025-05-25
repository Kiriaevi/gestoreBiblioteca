package gui.controller;

import commands.Command;
import commands.CommandAggiungiLibro;
import commands.CommandModificaLibro;
import commands.CommandRimuoviLibro;
import comparators.OrdinamentoAutore;
import comparators.OrdinamentoTitolo;
import comparators.OrdinamentoValutazione;
import entities.Libro;
import entities.Pagina;
import entities.Query;
import gui.view.VistaAggiungi;
import gui.view.VistaLibreria;
import gui.view.VistaModifica;
import libreria.memoria.LibreriaAbstract;
import ricerca.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

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
    private boolean isAdded = false;

    private boolean ordinamentoAscendente = false;
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
        this.vista.setPaginazioneBtnNext(e -> elaboraPagina(Pagina.PROSSIMA));
        this.vista.setPaginazioneBtnPrevious(e -> elaboraPagina(Pagina.PRECEDENTE));
        popolaLibreria();
    }
    private void ordinamento(String criterio) {
        switch (criterio) {
            case "autore":
                this.comparatore = new OrdinamentoAutore(ordinamentoAscendente).ottieniComparatore();
                break;
            case "titolo":
                this.comparatore = new OrdinamentoTitolo(ordinamentoAscendente).ottieniComparatore();
                break;
            default:
                this.comparatore = new OrdinamentoValutazione(ordinamentoAscendente).ottieniComparatore();
        }
        ordinamentoAscendente = !ordinamentoAscendente;
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
        if (q.autore() != null && !q.autore().trim().isEmpty())
            filtro = new FiltroAutore(filtro, q.autore());
        if (q.categoria() != null && !q.categoria().trim().isEmpty())
            filtro = new FiltroGenere(filtro, q.categoria());
        if (q.stato() != null)
            filtro = new FiltroStato(filtro, q.stato());

        libri = ricercaLibri(filtro);
        vista.setSearchButtonText("Torna alla Home");
        vista.mostraRisultatiRicerca(libri);
        backHome = true;
    }

    private Collection<Libro> ricercaLibri(Filtro filtro) {
        return libreria.cerca(filtro).stream().sorted(this.comparatore).toList();
    }

    private void aggiungiLibro() {
        VistaAggiungi vistaAggiungi = new VistaAggiungi(vista, libro -> {
            Command cmd = new CommandAggiungiLibro(libreria, libro);
            this.isAdded = cmd.execute();
            postAggiuntaLibro();
        }, "Aggiungi libro");
        vistaAggiungi.setVisible(true);
    }
    private void modificaLibro(ActionEvent e) {
        Libro libroSorg = ottieniLibro((JButton) e.getSource());
        VistaModifica vistaModifica = new VistaModifica(vista, libro -> {
            Command cmd = new CommandModificaLibro(libreria, libro, libroSorg.isbn());
            cmd.execute();
            aggiorna(true);
        }, libroSorg);
        vistaModifica.setVisible(true);
    }
    private void eliminaLibro(ActionEvent e) {
        Libro libroSorg = ottieniLibro((JButton) e.getSource());
        Command cmd = new CommandRimuoviLibro(libreria, libroSorg);
        cmd.execute();
        aggiorna(true);
    }
    private void popolaLibreria() {
        libri = ottieniLibriDallaLibreria();
        vista.addBooks(libri);
        aggiungiListeners();
    }
    private void postAggiuntaLibro() {
        String msg = this.isAdded ? "Libro aggiunto" : "ATTENZIONE! QUALCOSA È ANDATO STORTO";
        vista.libroAggiunto(msg);
        this.isAdded = false;
        vista.pulisciMatrice();
        popolaLibreria();
    }
    private Libro ottieniLibro(JButton bottone) {
        return vista.getLibroSelezionato(bottone);
    }
    private void aggiorna(boolean isCrud) {
        vista.pulisciMatrice();
        if(isCrud)
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
        return libreria.getLibri(Pagina.CORRENTE).stream().sorted(this.comparatore).toList();
    }
    private void elaboraPagina(Pagina richiesta) {
        this.libri = libreria.getLibri(richiesta).stream().sorted(this.comparatore).toList();
        aggiorna(false);
    }
}