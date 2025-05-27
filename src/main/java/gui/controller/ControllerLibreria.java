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

/**
 * La classe ControllerLibreria funge da controller in un'architettura MVC
 * per gestire le operazioni della biblioteca all'interno di un'interfaccia grafica.
 * Gestisce le interazioni tra il modello (LibreriaAbstract) e la vista (VistaLibreria).
 */
public class ControllerLibreria {
    private  final LibreriaAbstract libreria;
    private final VistaLibreria vista;
    private Collection<Libro> libri = null;
    private boolean isSuccessful = false;
    private boolean isSearching = false;
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
        this.vista.setPaginazioneBtnNext(e -> elaboraPagina(Pagina.PROSSIMA), false);
        this.vista.setPaginazioneBtnPrevious(e -> elaboraPagina(Pagina.PRECEDENTE), false);
        this.vista.setPaginazioneBtnNext(e -> elaboraPagina(Pagina.ULTIMA), true);
        this.vista.setPaginazioneBtnPrevious(e -> elaboraPagina(Pagina.PRIMA), true);
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
        if(isSearching) {
            isSearching = false;
            libri = ottieniLibriDallaLibreria();
            vista.pulisciRicerca();
            postRicerca();
            return;
        }
        Query q = vista.recuperaDatiDiRicerca();
        Filtro filtro = new FiltroBase();

        //TODO: SE AGGIUNGI VIRGOLETTE NON CERCA ', FARE ESCAPING DEI CARATTERI

        if (q.titolo() != null && !q.titolo().trim().isEmpty())
            filtro = new FiltroTitolo(filtro, q.titolo());
        if (q.autore() != null && !q.autore().trim().isEmpty())
            filtro = new FiltroAutore(filtro, q.autore());
        if (q.categoria() != null && !q.categoria().trim().isEmpty())
            filtro = new FiltroGenere(filtro, q.categoria());
        if (q.stato() != null)
            filtro = new FiltroStato(filtro, q.stato());

        libri = ricercaLibri(filtro);
        isSearching = true;
        postRicerca();
    }

    private void postRicerca() {
        vista.setSearchButtonText(isSearching ? "Torna alla Home" : "Cerca");
        vista.enablePaginationButtons(!isSearching);
        vista.mostraRisultatiRicerca(libri);
        aggiungiListeners();
    }

    private Collection<Libro> ricercaLibri(Filtro filtro) {
        return libreria.cerca(filtro).stream().sorted(this.comparatore).toList();
    }

    private void aggiungiLibro() {
        VistaAggiungi vistaAggiungi = new VistaAggiungi(vista, libro -> {
            Command cmd = new CommandAggiungiLibro(libreria, libro);
            this.isSuccessful = cmd.execute();
            postAggiuntaLibro();
        }, "Aggiungi libro");
        vistaAggiungi.setVisible(true);
    }
    private void modificaLibro(ActionEvent e) {
        Libro libroSorg = ottieniLibro((JButton) e.getSource());
        VistaModifica vistaModifica = new VistaModifica(vista, libro -> {
            Command cmd = new CommandModificaLibro(libreria, libro, libroSorg);
            this.isSuccessful = cmd.execute();
            mostraDialog(this.isSuccessful ? "Libro modificato con successo!" :
                    "IL LIBRO SELEZIONATO NON È PRESENTE NELL'ARCHIVIO");
            this.isSuccessful = false;
            aggiorna(true);
        }, libroSorg);
        postCrud();
        vistaModifica.setVisible(true);
    }
    private void eliminaLibro(ActionEvent e) {
        Libro libroSorg = ottieniLibro((JButton) e.getSource());
        Command cmd = new CommandRimuoviLibro(libreria, libroSorg);
        this.isSuccessful = cmd.execute();
        mostraDialog(this.isSuccessful ? "Libro eliminato con successo!" :
                "IL LIBRO SELEZIONATO NON È PRESENTE NELL'ARCHIVIO");
        this.isSuccessful = false;
        postCrud();
        aggiorna(true);
    }
    private void popolaLibreria() {
        libri = ottieniLibriDallaLibreria();
        vista.addBooks(libri);
        aggiungiListeners();
    }
    private void postCrud() {
        if(isSearching) {
            isSearching = false;
            postRicerca();
        }
    }
    private void postAggiuntaLibro() {
        postCrud();
        mostraDialog(this.isSuccessful ? "Libro aggiunto"
                : "ASSICURATI CHE NON ESISTA UN LIBRO CON LON LO STESSO ISBN ");
        this.isSuccessful = false;
        vista.pulisciMatrice();
        popolaLibreria();
    }
    private void mostraDialog(String msg) {
        String text = this.isSuccessful ? msg :
                "ATTENZIONE! QUALCOSA È ANDATO STORTO, "+msg;
        vista.libroAggiunto(text);
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