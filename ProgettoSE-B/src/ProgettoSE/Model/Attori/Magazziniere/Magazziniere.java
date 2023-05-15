package ProgettoSE.Model.Attori.Magazziniere;

import ProgettoSE.Model.Alimentari.Alimento;
import ProgettoSE.Model.Alimentari.Bevanda;
import ProgettoSE.Model.Alimentari.Extra;
import ProgettoSE.Model.Alimentari.Ingrediente;
import ProgettoSE.Model.Attori.AddettoPrenotazione.Prenotazione;
import ProgettoSE.Model.Attori.Persona;
import ProgettoSE.Model.Produzione.Menu.MenuTematico;
import ProgettoSE.Model.Produzione.Piatto;
import ProgettoSE.Model.Produzione.Prenotabile;
import ProgettoSE.Utility.Costanti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Magazziniere extends Persona {
    //ATTRIBUTI
    private Magazzino magazzino;
    private ArrayList<Alimento> lista_spesa;

    //INVARIANTI DI CLASSE
    /**
     * <h2>Metodo che verifica l'invariante di classe del magazziniere (magazzino!=null)
     * @return true se l'invariante è rispettato, false altrimenti
     */
    private boolean magOk(){
        return magazzino!=null;
    }

    //METODI
    public Magazziniere(String nome) {
        super(nome);
    }
    /**
     * <h2>Metodo costruttore della classe Magazziniere
     * @param nome nome del magazziniere
     * @param magazzino magazzino del ristorante
     * @param lista_spesa lista degli alimenti da acquistare
     */
    public Magazziniere(String nome, Magazzino magazzino, ArrayList<Alimento> lista_spesa) {
        super(nome);
        this.magazzino = magazzino;
        this.lista_spesa = lista_spesa;
    }
    //getters
    public Magazzino getMagazzino() {
        return magazzino;
    }
    public ArrayList<Alimento> getLista_spesa() {
        return lista_spesa;
    }
    //setters
    public void setMagazzino(Magazzino magazzino) {
        this.magazzino = magazzino;
    }
    public void setLista_spesa(ArrayList<Alimento> lista_spesa) {
        this.lista_spesa = lista_spesa;
    }

    /**
     * <h2>Metodo che calcola il consumo di bevande per un numero di persone</h2>
     * <b>Precondizione:</b> il numero delle persone sia positivo e ci siano bevande nel magazzino<br>
     * <b>Postcondizione:</b> il numero di bevande calcolate sia uguale al numero di bevande nel magazzino
     * @param n_persone numero di persone presenti nella prenotazione
     * @throws IllegalArgumentException se il numero di persone è minore o uguale a 0 o non ci sono bevande nel magazzino
     * @return HashMap contenente le bevande e il loro consumo
     */
    public HashMap<Alimento, Float> calcolaConsumoBevande(int n_persone) {
        //precondizione: n_persone > 0 e ci siano bevande nel magazzino
        if(n_persone <= 0) throw new IllegalArgumentException("Il numero di persone deve essere maggiore di 0");
        if(magazzino.getBevande().isEmpty()) throw new IllegalArgumentException("Non ci sono bevande nel magazzino");
        HashMap<Alimento, Float> consumo_bevande = new HashMap<>();
        //doppio cast perchè la consegna richiede che sia arrotondato all' intero superiore ma lo storiamo come float
        magazzino.getBevande().forEach(bevanda -> consumo_bevande.put(bevanda, (float) (int) Math.ceil(n_persone * ((Bevanda) bevanda).getCons_procapite())));
        //postcondizione: il numero di bevande calcolate è uguale al numero di bevande nel magazzino
        assert consumo_bevande.size() == magazzino.getBevande().size();
        return consumo_bevande;
    }

    /**
     * <h2>Metodo che calcola il consumo di extras per un certo numero di persone</h2>
     * <b>Precondizione:</b> il numero delle persone sia positivo e ci siano extras nel magazzino<br>
     * <b>Postcondizione:</b> il numero di extras calcolati sia uguale al numero di extras nel magazzino
     * @param n_persone numero di persone presenti nella prenotazione
     * @throws IllegalArgumentException se il numero di persone è minore o uguale a 0 o non ci sono extras nel magazzino
     * @return HashMap contenente gli extras e il loro consumo
     */
    public HashMap<Alimento, Float> calcolaConsumoExtras(int n_persone) {
        //precondizione: n_persone > 0 e ci siano extras nel magazzino
        if(n_persone <= 0) throw new IllegalArgumentException("Il numero di persone deve essere maggiore di 0");
        if(magazzino.getExtras().isEmpty()) throw new IllegalArgumentException("Non ci sono extras nel magazzino");
        HashMap<Alimento, Float> consumo_extras = new HashMap<>();
        //anche in questo caso devo fare doppio cast perchè la consegna richiede che sia arrotondato all' intero superiore
        magazzino.getExtras().forEach(extra -> consumo_extras.put(extra,(float) (int) Math.ceil(n_persone * ((Extra) extra).getCons_procapite())));
        //postcondizione: il numero di extras calcolati è uguale al numero di extras nel magazzino
        assert consumo_extras.size() == magazzino.getExtras().size();
        return consumo_extras;
    }

    /**
     * <h2>metodo che aggiunge alla lista spesa gli ingredienti necessari per preparare i piatti delle prenotazione totale della giornata</h2>
     * <b>Precondizione:</b> la prenotazione non è nulla<br>
     * <b>Postcondizione:</b> la lista della spesa non è nulla
     * @param prenotazione_totale prenotaione totale della giornata contenenete tutti i menu e i piatti ordinati dai commensali e le bevande e gli extras ordinati
     * @throws IllegalArgumentException se la prenotazione è nulla
     * @return void
     */
    public void creaListaSpesa(Prenotazione prenotazione_totale) {
        //precondizione: la prenotazione non è nulla
        if(prenotazione_totale == null) throw new IllegalArgumentException("La prenotazione non può essere nulla");
        //calcolo una mappa di piatti e quantità perchè è più comodo per calcolare la quantità di ingredienti rispetto a una mappa di piatti e menu
        HashMap<Piatto, Integer> consumi = calcolaPiattiPrenotazione(prenotazione_totale);
        //per ogni piatto, calcolo la quantità di ingredienti necessaria
        consumi.keySet().forEach(piatto -> valutaQtaIngredientiPiatto(piatto, consumi.get(piatto)));
        //ciclare sugli extra e sulle bevande
        prenotazione_totale.getCons_extra().forEach((key, value) -> {
            if (key instanceof Extra) {
                value += value * Costanti.IMPREVISTI_CUCINA;
                float qta_rimanente = magazzino.getAlimento(key.getNome()).getQta() - value;
                if (qta_rimanente < 0)
                    //se la quantità rimanente è minore di 0, aggiungo l'extra alla lista spesa in valore assoluto
                    this.lista_spesa.add(new Extra(key.getNome(), Math.abs(qta_rimanente), key.getMisura(), ((Extra) key).getCons_procapite()));
            }
        });
        //esguo lo stesso ciclo per le bevande
        prenotazione_totale.getCons_bevande().forEach((key, value) -> {
            if (key instanceof Bevanda) {
                value += value * Costanti.IMPREVISTI_CUCINA;
                float qta_rimanente = magazzino.getAlimento(key.getNome()).getQta() - value;
                if (qta_rimanente < 0)
                    this.lista_spesa.add(new Bevanda(key.getNome(), Math.abs(qta_rimanente), key.getMisura(), ((Bevanda) key).getCons_procapite()));
            }
        });
        //postcondizione: la lista spesa non è nulla
        assert lista_spesa != null;
    }

    /**
     * <h2>Metodo che calcola la quantità di ingredienti necessaria per preparare un certo numero di porzioni di un piatto</h2>
     * <b>Precondizione:</b> il piatto non è nullo e la quantità è maggiore di 0<br>
     * <b>Postcondizione:</b> la lista della spesa non è nulla
     * @param piatto piatto di cui calcolare la quantità di ingredienti
     * @param qta_richiesta_piatto quantità di porzioni di quel piatto che si vogliono preparare
     * @throws IllegalArgumentException se il piatto è nullo o la quantità è minore o uguale a 0
     * @return void
     */
    private void valutaQtaIngredientiPiatto(Piatto piatto, int qta_richiesta_piatto) {
        //precondizione: il piatto non è nullo e la quantità è maggiore di 0
        if(piatto == null) throw new IllegalArgumentException("Il piatto non può essere nullo");
        if(qta_richiesta_piatto < 0) throw new IllegalArgumentException("La quantità di piatti deve essere maggiore o uguale a 0");
        //calcolo dei valori per facilitare la stesura del codice
        int n_porzioni_ricetta = piatto.getRicetta().getN_porzioni();   //ricetta x5
        int n_ricette = (int) Math.ceil(qta_richiesta_piatto / n_porzioni_ricetta);  //   14/5   = 3 ricette
        //ciclo sugli ingredienti del piatto
        for (Alimento ingrediente : piatto.getRicetta().getIngredienti()) {
            //per trovare il numero di porzioni da fare mi interessa il numero di porzioni che genera la ricetta, in relazione al numero di porzioni che mi servono di quel piatto
            float qta_richiesta_ingrediente = ingrediente.getQta() * n_ricette;
            qta_richiesta_ingrediente += qta_richiesta_piatto * Costanti.IMPREVISTI_CUCINA;
            float qta_rimanente = magazzino.getAlimento(ingrediente.getNome()).getQta() - qta_richiesta_ingrediente;
            if (qta_rimanente < 0)
                //compro l'ingrediente in una quantità arrotondata per eccesso
                this.lista_spesa.add(new Ingrediente(ingrediente.getNome(), Math.abs(qta_rimanente), ingrediente.getMisura()));
        }
        //postcondizione: la lista spesa non è nulla
        assert lista_spesa != null;
    }

    /**
     * <h2>Metodo che aggiunge gli alimenti della lista spesa al magazzino</h2>
     * <b>Precondizione:</b> la lista spesa non è nulla<br>
     * <b>Postcondizione:</b> la lista spesa è vuota
     * @throws IllegalArgumentException se la lista spesa è nulla
     * @return String messaggio di aggiornamento del magazzino
     */
    public String aggiungiSpesaInMagazzino() {
        //precondizione: la lista spesa non è nulla
        if(lista_spesa == null) throw new IllegalArgumentException("La lista spesa non può essere nulla");
        if (this.lista_spesa.isEmpty())
            return "\nLe disponibilità in magazzino riescono a soddisfare tutte le prenotazioni della giornata senza richiedere l'acquisto di ulteriori alimenti\n";
        String messaggio = "\nIl magazziniere sta aggiornando il magazzino andando ad aggiungere degli alimenti secondo la seguente lista della spesa: \n";
        for (Alimento alimento : lista_spesa) {
            messaggio += "- " + alimento.getNome() + " in quantità pari a: " + String.format("%.2f", alimento.getQta()) + " " + alimento.getMisura() + "\n";
            float qta_in_magazzino = magazzino.getAlimento(alimento.getNome()).getQta();
            float nuova_qta = qta_in_magazzino + alimento.getQta();
            alimento.setQta(nuova_qta);
            magazzino.setQtaAlimento(alimento);
        }
        //svuoto la lista della spesa una volta aggiornato il magazzino
        this.lista_spesa.clear();
        //postcondizione: la lista spesa è vuota e il messaggio non è nullo
        assert lista_spesa.isEmpty();
        assert messaggio != null;
        return messaggio;
    }

    /**
     * <h2>Metodo che porta in cucina gli alimenti necessari, gli extra e le bevande</h2>
     * <b>Precondizione:</b> la prenotazione complessiva non è nulla e la lista spesa è vuota<br>
     * <b>Postcondizione:</b> il magazzino è aggiornato <br>
     * La quantità di ingredienti segue le richieste effettive, non quelle delle ricette.
     * Inoltre si porta una % in più del dovuto (pari a Costanti.ALIMENTI_SCARTATI) sia di ingredienti, di extra e di bevande
     * @param prenotazione_complessiva prenotazione complessiva di cui calcolare gli alimenti, gli extra e le bevande da portare in cucina
     * @throws IllegalArgumentException se la prenotazione complessiva è nulla o la lista spesa non è vuota
     * @return void
     */
    public void portaInCucina(Prenotazione prenotazione_complessiva) {
        //precondizione: la prenotazione complessiva non è nulla e la lista spesa è vuota
        if(prenotazione_complessiva == null) throw new IllegalArgumentException("La prenotazione complessiva non può essere nulla");
        if (!this.lista_spesa.isEmpty()) throw new IllegalArgumentException("La lista spesa deve essere vuota");
        HashMap<Piatto, Integer> consumi_prenotazione = calcolaPiattiPrenotazione(prenotazione_complessiva);
        //porto gli ingredienti necessari in cucina
        for (Piatto piatto : consumi_prenotazione.keySet()) {
            for (Alimento ingrediente : piatto.getRicetta().getIngredienti()) {//setto all'ingrediente la quantità richiesta dai clienti (non dalla ricetta)
                float qta_da_prelevare = ingrediente.getQta() / piatto.getRicetta().getN_porzioni() * consumi_prenotazione.get(piatto);
                qta_da_prelevare += qta_da_prelevare * Costanti.ALIMENTI_SCARTATI;
                this.magazzino.prelevaAlimento(ingrediente.getNome(), qta_da_prelevare);
            }
        }
        //porto le bevande necessarie in cucina
        for(Map.Entry<Alimento, Float> cons_bevande : prenotazione_complessiva.getCons_bevande().entrySet()){
            float qta_da_prelevare = cons_bevande.getValue();
            qta_da_prelevare += qta_da_prelevare * Costanti.ALIMENTI_SCARTATI;
            this.magazzino.prelevaAlimento(cons_bevande.getKey().getNome(), qta_da_prelevare);
        }
        //porto gli extra necessari in cucina
        for(Map.Entry<Alimento, Float> cons_extra : prenotazione_complessiva.getCons_extra().entrySet()){
            float qta_da_prelevare = cons_extra.getValue();
            qta_da_prelevare += qta_da_prelevare * Costanti.ALIMENTI_SCARTATI;
            this.magazzino.prelevaAlimento(cons_extra.getKey().getNome(), qta_da_prelevare);
        }
        //postcondizione: il magazzino è aggiornato
        assert magOk();
    }

    /**
     * <h2>Metodo che genera la map contenente soli piatti della prenotazione complessiva e la quantità di ciascuno</h2>
     * <b>Precondizione:</b> la prenotazione complessiva non è nulla<br>
     * <b>Postcondizione:</b> l'insieme dei consumi non è nullo
     * @param prenotazione_totale prenotazione complessiva di cui calcolare i piatti e la quantità di ciascuno
     * @throws IllegalArgumentException se la prenotazione complessiva è nulla
     * @return HashMap<Piatto, Integer> consumi
     */
    private HashMap<Piatto, Integer> calcolaPiattiPrenotazione(Prenotazione prenotazione_totale) {
        //precondizione: la prenotazione totale non è nulla
        if (prenotazione_totale == null) throw new IllegalArgumentException("La prenotazione totale non può essere nulla");
        HashMap<Piatto, Integer> consumi = new HashMap<>();
        for (Prenotabile prenotabile : prenotazione_totale.getScelte().keySet()) {
            //controllo se l'oggetto è un piatto
            if (prenotabile instanceof Piatto) {
                //se non è già presente lo aggiungo semplicemenbte
                if (!consumi.containsKey((Piatto) prenotabile))
                    consumi.put((Piatto) prenotabile, prenotazione_totale.getScelte().get(prenotabile));
                else
                    //in caso fosee già presente aggiorno la quantità, sommandola a quella già presente
                    consumi.put((Piatto) prenotabile, consumi.get(prenotabile) + prenotazione_totale.getScelte().get(prenotabile));
            } else if (prenotabile instanceof MenuTematico) {
                //ciclo sui piatti presenti nel menu
                for (Piatto piatto : ((MenuTematico) prenotabile).getPiatti_menu()) {
                    //esuguo la stessa operazione fatta sopra
                    if (!consumi.containsKey(piatto))
                        consumi.put(piatto, prenotazione_totale.getScelte().get(prenotabile));
                    else
                        consumi.put(piatto, consumi.get(piatto) + prenotazione_totale.getScelte().get(prenotabile));
                }
            }
        }
        //postcondizione: l'insieme dei consumi non è nullo
        assert consumi != null;
        return consumi;
    }
}
