package ProgettoSE.Model.Attori.Gestore;

import ProgettoSE.Model.Alimentari.Alimento;
import ProgettoSE.Model.Attori.AddettoPrenotazione.Prenotazione;
import ProgettoSE.Model.Attori.Persona;
import ProgettoSE.Utility.LetturaFileXML;
import ProgettoSE.Utility.Costanti;
import ProgettoSE.Model.Produzione.Prenotabile;

import java.time.LocalDate;
import java.util.ArrayList;

public class Gestore extends Persona {
    //ATTRIBUTI
    private Ristorante ristorante;
    //METODI
    /**
     * <h2>Costruttore</h2>
     * @param nome nome del gestore
     * @param ristorante ristorante gestito dal gestore
     */
    public Gestore(String nome, Ristorante ristorante) {
        super(nome);
        this.ristorante = ristorante;
    }
    /**
     * <h2>Getter</h2>
     * @return ristorante
     */
    public Ristorante getRistorante() {
        return ristorante;
    }
    /**
     * <h2>Invariante di classe</h2>
     * @return true se ristorante non è null, false altrimenti
     */
    private boolean risOk() {
        return ristorante != null;
    }

    /**
     * <h2>Metodo che inizializza il ristorante leggendo i dati dal file XML</h2>
     * Dopo la lettura invoca i metodi controllaMenu() e controllaRicette() per controllare che i menu tematici e le ricette siano valide<br><br>
     * <b>Precondizione:</b> il ristorante è null<br>
     * <b>Postcondizione:</b> il ristorante non è null
     * @return stringa che contiene i messaggi di errore (se ci sono) del menu tematico e delle ricette rifiutati
     * @throws IllegalArgumentException se il ristorante è già stato inizializzato
     */
    public ArrayList<Prenotabile> inizializzaRistorante() {
        //precondizione: il ristorante è null
        if(ristorante != null) throw new IllegalArgumentException("Il ristorante è già stato inizializzato");
        //leggo il ristorante dal file xml e lo inizializzo
        LetturaFileXML letturaFileXML = new LetturaFileXML();
        ristorante = letturaFileXML.leggiRistorante(Costanti.FILE_RISTORANTE);
        //controllo che i menu tematici e le ricette siano valide
        ArrayList<Prenotabile> prenotabili_invalidi= new ArrayList<>();
        prenotabili_invalidi.addAll(ristorante.getAddettoPrenotazione().controllaMenu(ristorante.getLavoro_persona()));
        prenotabili_invalidi.addAll(ristorante.getAddettoPrenotazione().controllaRicette(ristorante.getLavoro_persona()));
        //postcondizione: il ristorante non è null
        assert risOk();
        return prenotabili_invalidi;
    }

    /**
     * <h2>Metodo che comunica al magazziniere la lista della spesa e al cuoco la lista dei piatti da cucinare per il giorno attuale</h2>
     * <b>Precondizione:</b> la data non è null<br>
     * <b>Postcondizione:</b> la lista della spesa è aggiornata<br>
     * @param data_attuale data attuale
     * @return String messaggio
     * @throws IllegalArgumentException se la data non è valida
     */
    public ArrayList<Alimento> comunica(LocalDate data_attuale) {

        //precondizione data_attuale non null
        if (data_attuale == null) throw new IllegalArgumentException("Data non valida");

        ArrayList<Prenotazione> prenotazioni_del_giorno = ristorante.getAddettoPrenotazione().filtraPrenotazioniPerData(data_attuale);

        //se non ci sono prenotazioni per il giorno attuale non faccio nulla e ritorno null
        if (prenotazioni_del_giorno.isEmpty()) return null;

        //altrimenti unisco le prenotazioni del giorno in un'unica grande prenotazione
        Prenotazione prenotazione_del_giorno = ristorante.getAddettoPrenotazione().unisciPrenotazioni(prenotazioni_del_giorno);

        //faccio calcolare al magazziniere la lista della spesa basandosi sulla grande prenotazione
        ristorante.getMagazziniere().creaListaSpesa(prenotazione_del_giorno);

        //STAMPA DELLA LISTA SPESA

        ArrayList<Alimento> lista_spesa = new ArrayList<>(ristorante.getMagazziniere().getLista_spesa());

        //faccio aggiungere al magazziniere gli alimenti acquistati e mi faccio comunicare cosa sta aggiungendo
        ristorante.getMagazziniere().aggiungiSpesaInMagazzino();
        //faccio eliminare dal magazzino gli alimenti che sono stati consumati in cucina
        ristorante.getMagazziniere().portaInCucina(prenotazione_del_giorno);
        //faccio eliminare dal addetto prenotazione le prenotazioni vecchie comprese quelle in data odierna(che sono state già elaborate)
        ristorante.getAddettoPrenotazione().rimuoviPrenotazioniVecchie(data_attuale);

        return lista_spesa;
    }
}