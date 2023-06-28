package ProgettoSE.Controller;

import ProgettoSE.Controller.Factories.MenuTematicoFactory;
import ProgettoSE.Controller.Factories.PiattoFactory;
import ProgettoSE.Controller.Handlers.*;
import ProgettoSE.Model.Alimentari.Alimento;
import ProgettoSE.Model.Alimentari.Bevanda;
import ProgettoSE.Model.Alimentari.Extra;
import ProgettoSE.Model.Alimentari.Ingrediente;

import ProgettoSE.Model.Attori.AddettoPrenotazione.AddettoPrenotazione;
import ProgettoSE.Model.Attori.AddettoPrenotazione.Prenotazione;
import ProgettoSE.Model.Attori.Cliente;
import ProgettoSE.Model.Attori.Gestore.Ristorante;
import ProgettoSE.Model.Attori.Magazziniere.Magazzino;
import ProgettoSE.Model.Attori.Gestore.Gestore;
import ProgettoSE.Model.Attori.Tempo;

import ProgettoSE.Model.Produzione.Menu.MenuTematico;
import ProgettoSE.Model.Produzione.Piatto;
import ProgettoSE.Model.Produzione.Prenotabile;
import ProgettoSE.Utility.Costanti;
import ProgettoSE.Utility.MyMenu;

import ProgettoSE.View.View;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Controller {

    private final View view;

    private final ArrayList<Option> options;

    public Controller(View view) {
        this.view = view;
        this.options = new ArrayList<>();
    }

    private void setOptions() {
        options.clear();
        options.add(new Option("Visualizza il carico di lavoro per persona", new VisualizzaCaricoLavoro()));
        options.add(new Option("Visualizza il numero di posti a sedere disponibili", new VisualizzaPostiSedere()));
        options.add(new Option("Visualizza l'insieme delle bevande", new VisualizzaBevande()));
        options.add(new Option("Visualizza l'insieme dei generi extra", new VisualizzaExtra()));
        options.add(new Option("Visualizza il consumo pro-capite di bevande", new VisualizzaConsumoBevande()));
        options.add(new Option("Visualizza il consumo pro-capite di generi extra", new VisualizzaConsumoExtra()));
        options.add(new Option("Visualizza i menu tematici presenti nel menu", new VisualizzaMenuTematici()));
        options.add(new Option("Visualizza i piatti presenti nel menu", new VisualizzaPiatti()));
        options.add(new Option("Visualizza il ricettario", new VisualizzaRicettario()));
    }

    /**
     * <h2>Metodo che inizializza il ristorante automaticamente</h2><br>
     * <b>Precondizione:</b> il gestore non deve essere nullo.<br>
     * @param gestore gestore del ristorante
     * @throws IllegalArgumentException se il gestore è nullo
     */
    public void inizializzazione(Gestore gestore) {
        //precondizione: il gestore non deve essere nullo
        if (gestore == null) throw new IllegalArgumentException(Costanti.GESTORE_NON_NULLO);
        /*in questo caso l'interfaccia prenotabile fa da contratto tra la view e il controller, infatti la view accede
         solamente al nome del piatto/menu, non conosce nient'altro */
        view.stampaInizializzazione(gestore.inizializzaRistorante());
    }

    public void eseguiIterazioni(Gestore gestore, Tempo data_attuale){
        //creazione dei menu a scelta
        MyMenu menu_attori = MyMenu.creaMenuStruttura(Costanti.ATTORI);
        //MyMenu menu_gestore = MyMenu.creaMenuStruttura(Costanti.GESTORE);
        MyMenu menu_tempo = MyMenu.creaMenuStruttura(Costanti.TEMPO);
        MyMenu menu_inizializza = MyMenu.creaMenuStruttura(Costanti.INIZIALIZZAZIONE);

        if (view.yesOrNo("Vuoi modificare ulteriormente i dati di inizializzazione?")){
            view.ripulisciConsole();
            modificaDatiIniziali(gestore.getRistorante(), menu_inizializza);
        }
        view.ripulisciConsole();

        int scelta_attore = menu_attori.scegliConUscita(view);
        view.ripulisciConsole();
        while (scelta_attore != 0) {
            switch (scelta_attore) {

                case 1:

                    scegliFunzionalitaGestore(gestore.getRistorante());
                    view.ripulisciConsole();
                    view.ripulisciConsole();

                    view.stampaTesto(Costanti.USCITA_MENU + Costanti.GESTORE.toUpperCase(Locale.ROOT));
                    break;

                case 2:
                    inserisciPrenotazione(gestore.getRistorante(), data_attuale.getData_corrente());
                    break;

                case 3:
                    int scelta_funz_tempo = menu_tempo.scegliConUscita(view);
                    view.ripulisciConsole();
                    while (scelta_funz_tempo != 0) {
                        scegliFunzionalitaTemporali(scelta_funz_tempo, data_attuale, gestore);
                        view.premerePerContinuare();
                        view.ripulisciConsole();
                        scelta_funz_tempo = menu_tempo.scegliConUscita(view);
                        view.ripulisciConsole();
                    }
                    view.stampaTesto(Costanti.USCITA_MENU + Costanti.TEMPO.toUpperCase(Locale.ROOT));
                    break;
            }
            view.premerePerContinuare();
            view.ripulisciConsole();
            scelta_attore = menu_attori.scegliConUscita(view);
            view.ripulisciConsole();
        }

        view.stampaTesto("\n" + Costanti.USCITA_MENU + Costanti.ATTORI.toUpperCase(Locale.ROOT));
        view.stampaTesto("\n" + Costanti.END);
    }

    /**
     * <h2>Metodo che gestisce le varie funzionalità (di visualizzazione) disponibili al gestore</h2>
     * <b>Precondizione: </b>il ristorante deve essere istanziato
     * @param ristorante
     * @throws IllegalArgumentException se il gestore è null
     */
    private void scegliFunzionalitaGestore(Ristorante ristorante) {
        //precondizione: ristorante != null
        if(ristorante == null) throw new IllegalArgumentException(Costanti.RISTORANTE_NON_NULLO);

        int scelta;
        setOptions();
        MyMenu menu_gestore = new MyMenu("     " + Costanti.FUNZIONALITA.toUpperCase(Locale.ROOT) + Costanti.GESTORE.toUpperCase(Locale.ROOT) + "     ", this.getVoci());
        do {
            scelta = menu_gestore.scegliConUscita(view);
            if(scelta == 0) break;
            Handler handler = options.get(scelta).getAction();
            handler.esegui(view, ristorante);
            view.premerePerContinuare();
        }while (scelta != 0);
    }


    /**
     * <h2>Metodo che gestisce le varie funzionalità temporali disponibili</h2>
     * Queste funzionalità sono puramente di debug perchè non sono presenti nel sistema reale<br>
     * <b>Precondizione: </b>il gestore deve essere istanziato
     * @param scelta scelta del gestore
     * @param data_attuale data attuale del sistema
     * @param gestore gestore che ha effettuato l'accesso
     */
    private void scegliFunzionalitaTemporali(int scelta, Tempo data_attuale, Gestore gestore) {
        //precondizione: gestore != null
        if(gestore == null) throw new IllegalArgumentException(Costanti.GESTORE_NON_NULLO);

        switch (scelta) {
            case 1:
                data_attuale.scorriGiorno();
                break;
            case 2:
                boolean data_errata;
                do{
                    String stringa_data_prenotazione = view.leggiStringa(Costanti.INS_DATA);
                    LocalDate data_prenotazione = Tempo.parsaData(stringa_data_prenotazione);

                    if(data_prenotazione == null){
                        view.stampaTesto(Costanti.DATA_NON_VALIDA);
                        data_errata = true;
                    }else if(data_prenotazione.isBefore(data_attuale.getData_corrente())){
                        view.stampaTesto("La data inserita è precedente alla data attuale (" + data_attuale.getData_corrente() + ")");
                        data_errata = true;
                    }else{
                        data_attuale.setData_corrente(data_prenotazione);
                        data_errata = false;
                    }
                }while (data_errata);
                break;
        }
        view.stampaTesto("La data attuale è stata incrementata, ora è: " + data_attuale.getData_corrente() + ". La lista spesa è stata aggiornata.");

        //Stampo la lista
        view.stampaListaSpesa(gestore.comunica(data_attuale.getData_corrente()));

    }


    /**
     * <h2>Metodo che gestisce l'inserimento di una nuova prenotazione</h2>
     * <b>Precondizione:</b> ristorante != null && data_attuale != null
     * @param ristorante
     * @param data_attuale data attuale
     * @throws IllegalArgumentException se i parametri non sono validi
     */
    private void inserisciPrenotazione(Ristorante ristorante, LocalDate data_attuale) {

        //precondizione: gestore != null && data_attuale != null
        if(ristorante == null || data_attuale == null) throw new IllegalArgumentException("Parametri non validi");

        view.stampaTesto("Inserimento dati NUOVA PRENOTAZIONE");

        //NOME
        String nome_cliente = view.leggiStringaNonVuota("Nome cliente: ");
        Cliente cliente = new Cliente(nome_cliente);

        //DATA
        LocalDate data_prenotazione = gestisciData(ristorante, data_attuale);


        //variabili di supporto
        int lavoro_persona = ristorante.getLavoro_persona();
        int n_posti = ristorante.getN_posti();
        int posti_liberi_stimati = ristorante.getAddettoPrenotazione().stimaPostiRimanenti(data_prenotazione, lavoro_persona, n_posti);
        int posti_liberi_effettivi = n_posti - ristorante.getAddettoPrenotazione().calcolaPostiOccupati(data_prenotazione);

        //comunico l'esito della stima dei posti
        if (posti_liberi_stimati > 0) {
            view.stampaTesto("I posti liberi nel ristorante sono %s.", Integer.toString(posti_liberi_effettivi));
            view.stampaTesto("Abbiamo stimato di poter cucinare %s portate (solitamente 2 portate a testa).", Integer.toString(posti_liberi_stimati * 2));
        } else {
            view.stampaTesto("Ci scusiamo ma la stima del carico di lavoro non ci permette di accettare altre prenotazioni in questa data");
            return;
        }

        int n_coperti = view.leggiInteroConMinimoMassimo("Numero persone: ", 1, Math.min(posti_liberi_effettivi, posti_liberi_stimati * 2));

        inserisciSceltePrenotazione(ristorante, data_prenotazione, cliente, n_coperti);

        view.ripulisciConsole();

    }


    private void inserisciSceltePrenotazione(Ristorante ristorante, LocalDate data_prenotazione, Cliente cliente, int n_coperti){

        int lavoro_persona = ristorante.getLavoro_persona();
        int n_posti = ristorante.getN_posti();

        //CALCOLO CONSUMO BEVANDE E GENERI EXTRA
        HashMap<Alimento, Float> cons_bevande = ristorante.getMagazziniere().calcolaConsumoBevande(n_coperti);
        HashMap<Alimento, Float> cons_extra = ristorante.getMagazziniere().calcolaConsumoExtras(n_coperti);

        //Gestisco le scelte dei commensali
        HashMap<Prenotabile, Integer> scelte = new HashMap<>();
        int n_portate = 0;
        ArrayList<Prenotabile> menu_del_giorno = ristorante.getAddettoPrenotazione().calcolaMenuDelGiorno(data_prenotazione);
        //Se non il menu è vuoto non faccio fare nessuna scelta
        if (menu_del_giorno.isEmpty()){
            view.stampaTesto("Il menu è attualmente vuoto, le chiediamo di cambiare data della prenotazione");
            return;
        }

        do {

            view.stampaMenuDelGiorno(ristorante.getAddettoPrenotazione().calcolaMenuDelGiorno(data_prenotazione), data_prenotazione);
            //mostro un riepilogo delle scelyìte già effettuate
            view.stampaScelte(scelte);

            if (n_portate < n_coperti)
                view.stampaTesto("Deve scelgliere almeno altre %s portate per convalidare la prenotazione.", String.valueOf(n_coperti - n_portate));

            boolean validita = false;
            Prenotabile portata = null;

            do {
                String scelta = view.leggiStringa("Inserisca il nome della portata da ordinare: ");
                //controllo che la portata scelta sia presente nel menu del giorno
                for (Prenotabile prenotabile : menu_del_giorno) {
                    if (prenotabile.getNome().equalsIgnoreCase(scelta)) {
                        portata = prenotabile;
                        validita = true;
                    }
                }
                if (!validita)
                    view.leggiStringa("Portata non presente nel menu del giorno.");
            } while (!validita);

            int quantità = view.leggiInteroConMinimo("Inserisca le porzioni desiderate di " + portata.getNome().toLowerCase(Locale.ROOT) + ": ", 0);

            //devo leggere il value precedente e sommarlo alla nuova quantità aggiunta, dopodichè rimetto la value nuova nella Map
            Integer quantita_precedente = scelte.get(portata);
            if (quantita_precedente == null)
                quantita_precedente = 0;
            if (quantita_precedente + quantità != 0)
                scelte.put(portata, quantità + quantita_precedente);

            Prenotazione prenotazione = new Prenotazione(null, n_coperti, data_prenotazione, scelte, cons_bevande, cons_extra);

            boolean lavoro_validato = ristorante.getAddettoPrenotazione().validaCaricoLavoro(data_prenotazione, lavoro_persona, n_posti, prenotazione);

            view.ripulisciConsole();

            if (lavoro_validato && quantità > 0)
                view.stampaTesto("Portata aggiunta all'ordine.");
            else if (!lavoro_validato) {

                view.stampaTesto("Il carico di lavoro non ci permette di accettare un così alto numero di portate. Rimosso dalla lista: " + portata.getNome() + " x" + quantità);
                //tolgo la portata inserita che fa eccedere il carico di lavoro totale e reinserisco
                scelte.remove(portata);
                if (quantita_precedente != 0) scelte.put(portata, quantita_precedente);

            } else if (quantità == 0) view.stampaTesto("Portata non aggiunta all'ordine.");

            view.premerePerContinuare();
            view.ripulisciConsole();

            n_portate = 0;
            for (Integer value : scelte.values())
                n_portate += value;

        } while (n_portate < n_coperti || view.yesOrNo("Ogni commensale ha ordinato almeno una portata ciascuno, vuole ordinare altre portate?"));

        //Costruzione Prenotazione
        Prenotazione prenotazione = new Prenotazione(cliente, n_coperti, data_prenotazione, scelte, cons_bevande, cons_extra);
        ristorante.getAddettoPrenotazione().getPrenotazioni().add(prenotazione);
        view.stampaTesto("Prenotazione Registrata.\n");
    }

    /**
     * <h2>Metodo che compie diversi controlli sulla data inserita</h2>
     * <b>Precondizione: </b>il ristorante deve essere istanziato
     * @param ristorante
     * @param data_attuale data attuale
     * @return void
     */
    private LocalDate gestisciData(Ristorante ristorante, LocalDate data_attuale) {
        //precondizione: ristorante != null
        if(ristorante == null) throw new IllegalArgumentException(Costanti.RISTORANTE_NON_NULLO);

        LocalDate data_prenotazione = null;
        do {
            String stringa_data_prenotazione = view.leggiStringa(Costanti.INS_DATA);
            data_prenotazione = Tempo.parsaData(stringa_data_prenotazione);
        }while(!controllaData(data_prenotazione, data_attuale, ristorante));
        return data_prenotazione;
    }

    public boolean controllaData(LocalDate data_prenotazione, LocalDate data_attuale, Ristorante ristorante){
        AddettoPrenotazione addettoPrenotazioni = ristorante.getAddettoPrenotazione();
        if (data_prenotazione == null) {
            view.stampaTesto(Costanti.DATA_NON_VALIDA);
            return false;
        } else if (!addettoPrenotazioni.controlloDataSiaSuccessiva(data_attuale, data_prenotazione)) {
            view.stampaTesto("La data inserita deve essere sucessiva alla data attuale (" + data_attuale + ")");
            return false;
        }else if (!addettoPrenotazioni.controlloDataSiaFeriale(data_prenotazione)) {
            view.stampaTesto("La data inserita corrisponde ad un giorno festivo, sono ammessi solo giorni feriali.");
            return false;
        }else if (!addettoPrenotazioni.controlloPostiDisponibiliInData(data_prenotazione, ristorante.getN_posti())) {
            view.stampaTesto("Il ristorante non ha posti disponibili in questa data (" + data_prenotazione.toString() + ")");
            return false;
        }
        return true;
    }


    /**
     * <h2>Metodo che permette di aggiungere o modificare i dati del ristorante prima di accettare prenotazioni</h2>
     * <b>Precondizione: </b>Il ristorante non può essere null<br>
     * @param ristorante
     * @throws IllegalArgumentException se il gestore è null
     */
    private void modificaDatiIniziali(Ristorante ristorante, MyMenu menu_inizializza){
        //precondizione
        if(ristorante == null) throw new IllegalArgumentException(Costanti.RISTORANTE_NON_NULLO);

        int scelta_inizializza = menu_inizializza.scegliConUscita(view);

        MenuTematicoFactory menuTematicoFactory = new MenuTematicoFactory();
        PiattoFactory piattoFactory = new PiattoFactory();

        view.ripulisciConsole();

        Magazzino magazzino = ristorante.getMagazziniere().getMagazzino();

        ArrayList<Prenotabile> invalidi = new ArrayList<>();

        while (scelta_inizializza != 0) {

            switch (scelta_inizializza) {

                case 1: //modifica n_posti ristorante
                    modificaNumeroPostiRistorante(ristorante);
                    break;

                case 2: //modifica lavoro_persona
                    modificaLavoroPersona(ristorante);
                    break;

                case 3://aggiungi ingrediente in magazzino
                    Alimento nuovo_ingrediente = creaAlimento(Costanti.INGREDIENTE);
                    aggiungiAlimento(nuovo_ingrediente, magazzino);
                    break;

                case 4://aggiungi extra in magazzino
                    Alimento nuovo_extra = creaAlimento(Costanti.EXTRA);
                    aggiungiAlimento(nuovo_extra, magazzino);
                    break;

                case 5://aggiungi bevanda in magazzino
                    Alimento nuova_bevanda = creaAlimento(Costanti.BEVANDA);
                    aggiungiAlimento(nuova_bevanda, magazzino);
                    break;

                case 6://aggiungi menu tematico
                    aggiungiMenuTematico(ristorante, menuTematicoFactory, invalidi);
                    break;

                case 7://aggiungi piatto
                    aggiungiPiatto(ristorante, piattoFactory, invalidi);
                    break;
            }
            view.ripulisciConsole();
            scelta_inizializza = menu_inizializza.scegliConUscita(view);
            view.ripulisciConsole();
        }
    }

    private void modificaNumeroPostiRistorante(Ristorante ristorante){
        int n_posti = view.leggiInteroConMinimo("\nInserisci il nuovo numero di posti del ristorante: ", 1);
        //comuninco che il numero di posti è uguale a quello attuale
        if(n_posti == ristorante.getN_posti())
            view.stampaTesto(Costanti.UGUALE_ATTUALE ,"posti");
        else
            ristorante.setN_posti(n_posti);
        view.premerePerContinuare();
    }

    private void modificaLavoroPersona(Ristorante ristorante){
        int lavoro_persona = view.leggiInteroConMinimo("\n" + Costanti.INS_LAVORO_PERSONA, 1);
        //comuninco che il numero di lavoro per persona è uguale a quello attuale
        if(lavoro_persona == ristorante.getLavoro_persona())
            view.stampaTesto(Costanti.UGUALE_ATTUALE,"lavoro per persona");
        else
            ristorante.setLavoro_persona(lavoro_persona);

        ArrayList<Prenotabile> prenotabili_invalidi = new ArrayList<>();
        prenotabili_invalidi.addAll(ristorante.getAddettoPrenotazione().controllaMenu(ristorante.getLavoro_persona()));
        prenotabili_invalidi.addAll(ristorante.getAddettoPrenotazione().controllaRicette(ristorante.getLavoro_persona()));

        //se il messaggio è vuoto vuol dire che non ci sono errori sia nei menu che nelle ricette
        view.stampaElementiInvalidi(prenotabili_invalidi);
        view.premerePerContinuare();
    }

    private void aggiungiMenuTematico(Ristorante ristorante, MenuTematicoFactory menuTematicoFactory, ArrayList<Prenotabile> invalidi){
        MenuTematico menuTematico = new MenuTematico();
        menuTematicoFactory.creaPrenotabile(menuTematico, ristorante, view);
        ristorante.getAddettoPrenotazione().getMenu().add(menuTematico);
        invalidi = ristorante.getAddettoPrenotazione().controllaMenu(ristorante.getLavoro_persona());
        aggiungiPrenotabile(invalidi, "Menu tematico creato");
    }

    public void aggiungiAlimento(Alimento nuovo_alimento, Magazzino magazzino){
        view.ripulisciConsole();
        boolean result = magazzino.inserisciAlimento(nuovo_alimento);
        String tipo = nuovo_alimento.getClass().getSimpleName();
        if(result) view.stampaTesto("%s aggiunt* al magazzino", tipo);
        else view.stampaTesto("%s già presente in magazzino", tipo);
        view.premerePerContinuare();
    }

    private void aggiungiPiatto(Ristorante ristorante, PiattoFactory piattoFactory, ArrayList<Prenotabile> invalidi){
        Piatto piatto = new Piatto();
        piattoFactory.creaPrenotabile(piatto, ristorante, view);
        ristorante.getAddettoPrenotazione().getMenu().add(piatto);
        invalidi = ristorante.getAddettoPrenotazione().controllaRicette(ristorante.getLavoro_persona());
        aggiungiPrenotabile(invalidi, "Piatto creato");
    }

    public void aggiungiPrenotabile(ArrayList<Prenotabile> invalidi, String mex) {
        view.ripulisciConsole();
        if (invalidi.isEmpty()){
            view.stampaTesto(mex);
            view.premerePerContinuare();
        } else{
            view.stampaElementiInvalidi(invalidi);
            view.premerePerContinuare();
        }
    }

    /**
     * <h2>Metodo che crea un alimento</h2><br>
     * <b>Precondizione:</b> il tipo dell'alimento non è null<br>
     * @param tipo tipo dell'alimento da creare (Extra, Bevanda o Ingrediente)
     * @throws IllegalArgumentException se il tipo dell'alimento non è valido
     * @return Alimento
     */
    public Alimento creaAlimento(String tipo) {
        //precondizione: il tipo dell'alimento non è null
        if (tipo == null) throw new IllegalArgumentException("Tipo alimento non valido");

        view.stampaTesto("Inserisci i dati dell'alimento di tipo: %s", tipo);
        String nome = view.leggiStringaConSpazio(Costanti.INS_NOME);
        float quantita = (float) view.leggiDoubleConMinimo(Costanti.INS_QTA, 0);
        String unita_misura = view.leggiStringaNonVuota(Costanti.INS_MISURA);
        //dentro lo switch andrò a settare il consumo procapite solo se il tipo è Extra o Bevanda
        switch (tipo) {
            case Costanti.INGREDIENTE:
                return new Ingrediente(nome, quantita, unita_misura);
            case Costanti.EXTRA:
                float consumo_procapite = (float) view.leggiDoubleConMinimo(Costanti.INS_CONS_PROCAPITE, 0);
                return new Extra(nome, quantita, unita_misura, consumo_procapite);
            case Costanti.BEVANDA:
                consumo_procapite = (float) view.leggiDoubleConMinimo(Costanti.INS_CONS_PROCAPITE, 0);
                return new Bevanda(nome, quantita, unita_misura, consumo_procapite);
            default:
                return null;
        }
    }

    private ArrayList<String> getVoci() {
        ArrayList<String> voci = new ArrayList<>();
        for (Option opt : options) {
            voci.add(opt.getVoce());
        }
        return voci;
    }

}
