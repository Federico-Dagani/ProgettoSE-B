package ProgettoSE.Controller;

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
import ProgettoSE.Model.Produzione.Ricetta;
import ProgettoSE.Utility.Costanti;
import ProgettoSE.Utility.MyMenu;

import ProgettoSE.View.InputDatiTestuale;
import ProgettoSE.View.View;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Controller {

    private View view;

    public Controller(View view) {
        this.view = view;
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
        /*in questo caso l'interfaccia prenotabile fa da contratto tra la view ed il controller, infatti la view accede
         solamente al nome del piatto/menu, non conosce nient'altro */
        view.stampaInizializzazione(gestore.inizializzaRistorante());
    }

    public void eseguiIterazioni(Gestore gestore, Tempo data_attuale){
        //creazione dei menu a scelta
        MyMenu menu_attori = MyMenu.creaMenuStruttura(Costanti.ATTORI);
        MyMenu menu_gestore = MyMenu.creaMenuStruttura(Costanti.GESTORE);
        MyMenu menu_tempo = MyMenu.creaMenuStruttura(Costanti.TEMPO);
        MyMenu menu_inizializza = MyMenu.creaMenuStruttura(Costanti.INIZIALIZZAZIONE);

        if (InputDatiTestuale.yesOrNo("Vuoi modificare ulteriormente i dati di inizializzazione?")){
            view.ripulisciConsole();
            modificaDatiIniziali(gestore.getRistorante(), menu_inizializza);
        }
        view.ripulisciConsole();

        int scelta_attore = menu_attori.scegliConUscita();
        view.ripulisciConsole();
        while (scelta_attore != 0) {
            switch (scelta_attore) {

                case 1:
                    int scelta_funz_gestore = menu_gestore.scegliConUscita();
                    view.ripulisciConsole();
                    while (scelta_funz_gestore != 0) {
                        scegliFunzionalitaGestore(scelta_funz_gestore, gestore.getRistorante());
                        InputDatiTestuale.premerePerContinuare();
                        view.ripulisciConsole();
                        scelta_funz_gestore = menu_gestore.scegliConUscita();
                        view.ripulisciConsole();
                    }
                    view.stampaTesto(Costanti.USCITA_MENU + Costanti.GESTORE.toUpperCase(Locale.ROOT));
                    break;

                case 2:
                    inserisciPrenotazione(gestore.getRistorante(), data_attuale.getData_corrente());
                    break;

                case 3:
                    int scelta_funz_tempo = menu_tempo.scegliConUscita();
                    view.ripulisciConsole();
                    while (scelta_funz_tempo != 0) {
                        scegliFunzionalitaTemporali(scelta_funz_tempo, data_attuale, gestore);
                        InputDatiTestuale.premerePerContinuare();
                        view.ripulisciConsole();
                        scelta_funz_tempo = menu_tempo.scegliConUscita();
                        view.ripulisciConsole();
                    }
                    view.stampaTesto(Costanti.USCITA_MENU + Costanti.TEMPO.toUpperCase(Locale.ROOT));
                    break;
            }
            InputDatiTestuale.premerePerContinuare();
            view.ripulisciConsole();
            scelta_attore = menu_attori.scegliConUscita();
            view.ripulisciConsole();
        }

        view.stampaTesto("\n" + Costanti.USCITA_MENU + Costanti.ATTORI.toUpperCase(Locale.ROOT));
        view.stampaTesto("\n" + Costanti.END);
    }

    /**
     * <h2>Metodo che gestisce le varie funzionalità (di visualizzazione) disponibili al gestore</h2>
     * <b>Precondizione: </b>il ristorante deve essere istanziato
     * @param scelta scelta del gestore
     * @param ristorante
     * @throws IllegalArgumentException se il gestore è null
     */
    private void scegliFunzionalitaGestore(int scelta, Ristorante ristorante) {
        //precondizione: ristorante != null
        if(ristorante == null) throw new IllegalArgumentException(Costanti.RISTORANTE_NON_NULLO);

        ArrayList<Alimento> bevande = ristorante.getMagazziniere().getMagazzino().getBevande();
        ArrayList<Alimento> extras = ristorante.getMagazziniere().getMagazzino().getExtras();
        ArrayList<Prenotabile> menu = ristorante.getAddettoPrenotazione().getMenu();

        switch (scelta) {
            case 1:
                view.mostraCaricoLavoroPersona(ristorante.getLavoro_persona());
                break;
            case 2:
                view.mostraPostiDisponibili(ristorante.getN_posti());
                break;
            case 3:
                view.mostraAlimenti(bevande);
                break;
            case 4:
                view.mostraAlimenti(extras);
                break;
            case 5:
                view.mostraConsumoProcapite(bevande);
                break;
            case 6:
                view.mostraConsumoProcapite(extras);
                break;
            case 7:
                view.mostraMenuTematici(menu);
                break;
            case 8:
                view.mostraPiatti(menu);
                break;
            case 9:
                view.mostraRicette(menu);
                break;
        }
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
                    String stringa_data_prenotazione = InputDatiTestuale.leggiStringa(Costanti.INS_DATA);
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
        String nome_cliente = InputDatiTestuale.leggiStringaNonVuota("Nome cliente: ");
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

        int n_coperti = InputDatiTestuale.leggiInteroConMinimoMassimo("Numero persone: ", 1, Math.min(posti_liberi_effettivi, posti_liberi_stimati * 2));

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

            boolean validità = false;
            Prenotabile portata = null;

            do {
                String scelta = InputDatiTestuale.leggiStringa("Inserisca il nome della portata da ordinare: ");
                //controllo che la portata scelta sia presente nel menu del giorno
                for (Prenotabile prenotabile : menu_del_giorno) {
                    if (prenotabile.getNome().equalsIgnoreCase(scelta)) {
                        portata = prenotabile;
                        validità = true;
                    }
                }
                if (!validità)
                    InputDatiTestuale.leggiStringa("Portata non presente nel menu del giorno.");
            } while (!validità);

            int quantità = InputDatiTestuale.leggiInteroConMinimo("Inserisca le porzioni desiderate di " + portata.getNome().toLowerCase(Locale.ROOT) + ": ", 0);

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

            InputDatiTestuale.premerePerContinuare();
            view.ripulisciConsole();

            n_portate = 0;
            for (Integer value : scelte.values())
                n_portate += value;

        } while (n_portate < n_coperti || InputDatiTestuale.yesOrNo("Ogni commensale ha ordinato almeno una portata ciascuno, vuole ordinare altre portate?"));

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
            String stringa_data_prenotazione = InputDatiTestuale.leggiStringa(Costanti.INS_DATA);
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

        int scelta_inizializza = menu_inizializza.scegliConUscita();

        view.ripulisciConsole();

        Magazzino magazzino = ristorante.getMagazziniere().getMagazzino();

        ArrayList<Prenotabile> invalidi;

        while (scelta_inizializza != 0) {

            switch (scelta_inizializza) {

                case 1: //modifica n_posti ristorante
                    int n_posti = InputDatiTestuale.leggiInteroConMinimo("\nInserisci il nuovo numero di posti del ristorante: ", 1);
                    //comuninco che il numero di posti è uguale a quello attuale
                    if(n_posti == ristorante.getN_posti())
                        view.stampaTesto(Costanti.UGUALE_ATTUALE ,"posti");
                    else
                        ristorante.setN_posti(n_posti);
                    InputDatiTestuale.premerePerContinuare();
                    break;

                case 2: //modifica lavoro_persona
                    int lavoro_persona = InputDatiTestuale.leggiInteroConMinimo("\n" + Costanti.INS_LAVORO_PERSONA, 1);
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
                    InputDatiTestuale.premerePerContinuare();
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
                    creaMenuTematico(ristorante);
                    invalidi = ristorante.getAddettoPrenotazione().controllaMenu(ristorante.getLavoro_persona());
                    aggiungiPrenotabile(invalidi, "Menu tematico creato");
                    break;

                case 7://aggiungi piatto
                    creaPiatto(ristorante);
                    invalidi = ristorante.getAddettoPrenotazione().controllaRicette(ristorante.getLavoro_persona());
                    aggiungiPrenotabile(invalidi, "Piatto creato");
                    break;
            }
            view.ripulisciConsole();
            scelta_inizializza = menu_inizializza.scegliConUscita();
            view.ripulisciConsole();
        }
    }

    public void aggiungiAlimento(Alimento nuovo_alimento, Magazzino magazzino){
        view.ripulisciConsole();
        boolean result = magazzino.inserisciAlimento(nuovo_alimento);
        String tipo = nuovo_alimento.getClass().getSimpleName();
        if(result) view.stampaTesto("%s aggiunt* al magazzino", tipo);
        else view.stampaTesto("%s già presente in magazzino", tipo);
        InputDatiTestuale.premerePerContinuare();
    }

    public void aggiungiPrenotabile(ArrayList<Prenotabile> invalidi, String mex) {
        view.ripulisciConsole();
        if (invalidi.isEmpty()){
            view.stampaTesto(mex);
            InputDatiTestuale.premerePerContinuare();
        } else{
            view.stampaElementiInvalidi(invalidi);
            InputDatiTestuale.premerePerContinuare();
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
        String nome = InputDatiTestuale.leggiStringaConSpazio(Costanti.INS_NOME);
        float quantita = (float) InputDatiTestuale.leggiDoubleConMinimo(Costanti.INS_QTA, 0);
        String unita_misura = InputDatiTestuale.leggiStringaNonVuota(Costanti.INS_MISURA);
        //dentro lo switch andrò a settare il consumo procapite solo se il tipo è Extra o Bevanda
        switch (tipo) {
            case Costanti.INGREDIENTE:
                return new Ingrediente(nome, quantita, unita_misura);
            case Costanti.EXTRA:
                float consumo_procapite = (float) InputDatiTestuale.leggiDoubleConMinimo(Costanti.INS_CONS_PROCAPITE, 0);
                return new Extra(nome, quantita, unita_misura, consumo_procapite);
            case Costanti.BEVANDA:
                consumo_procapite = (float) InputDatiTestuale.leggiDoubleConMinimo(Costanti.INS_CONS_PROCAPITE, 0);
                return new Bevanda(nome, quantita, unita_misura, consumo_procapite);
            default:
                return null;
        }
    }

    /**
     * <h2>Metodo che crea un prenotabile</h2><br>
     * <b>Precondizione:</b> la tipologia non è null<br>
     * @param tipologia tipologia del prenotabile da creare (MenuTematico o Piatto)
     * @throws IllegalArgumentException se il gestore è null
     * @return Prenotabile
     */
    private Prenotabile creaPrenotabile(String tipologia) {
        //precondizione: la tipologia non è null
        if (tipologia == null) throw new IllegalArgumentException("Tipologia non valida");

        System.out.printf("\nInserisci i dati del %s \n\n", tipologia);
        String nome = InputDatiTestuale.leggiStringaConSpazio(Costanti.INS_NOME);
        float lavoro = (float) InputDatiTestuale.leggiDoubleConMinimo(Costanti.INS_LAVORO, 0);
        ArrayList<LocalDate> disponibilita = new ArrayList<>();

        do {
            boolean data_errata; //variabile per permettere di reinserire immediamente delle nuvo disponibilità in caso quelle inserite siano scorrette
            do {
                String data_inizio_da_parsare = InputDatiTestuale.leggiStringaNonVuota(Costanti.INS_DATA_INIZIO);
                String data_fine_da_parsare = InputDatiTestuale.leggiStringaNonVuota(Costanti.INS_DATA_FINE);

                LocalDate data_inizio_parsata = Tempo.parsaData(data_inizio_da_parsare);
                LocalDate data_fine_parsata = Tempo.parsaData(data_fine_da_parsare);

                if(data_inizio_parsata == null || data_fine_parsata == null){
                    System.out.println(Costanti.DATA_NON_VALIDA);
                    data_errata = true;
                } else {
                    disponibilita.add(data_inizio_parsata);
                    disponibilita.add(data_fine_parsata);
                    data_errata = false;
                };

            } while (data_errata);
        } while (InputDatiTestuale.yesOrNo("\nVuoi aggiungere un'altra disponibilità?"));
        //in base alla tipologia creo oggetti diversi
        if (tipologia.equals(Costanti.MENU_TEMATICO)) {
            return new MenuTematico(nome, new ArrayList<>(), lavoro, disponibilita);
        } else if (tipologia.equals(Costanti.PIATTO)) {
            return new Piatto(nome, disponibilita, lavoro, new Ricetta());
        }
        return null;
    }

    /**
     * <h2>Metodo che crea un menu tematico</h2><br>
     * <b>Precondizione:</b> il ristorante non è null<br>
     * <b>Postcondizione:</b> il menu tematico è creato e aggiunto al menu del ristorante<br>
     * @param ristorante ristorante a cui aggiungere il menu tematico
     * @throws IllegalArgumentException se il ristorante è null
     */
    public void creaMenuTematico(Ristorante ristorante) {
        //precondizione: il ristorante non è null
        if (ristorante == null) throw new IllegalArgumentException(Costanti.RISTORANTE_NON_NULLO);

        MenuTematico menu_tematico = (MenuTematico) creaPrenotabile(Costanti.MENU_TEMATICO);
        ArrayList<Piatto> piatti = new ArrayList<>();
        ArrayList<Prenotabile> menu_ristorante = ristorante.getAddettoPrenotazione().getMenu();

        do {
            //prima mostro i piatti presenti nel ristorante
            view.mostraPiatti(menu_ristorante);

            String nome_piatto = InputDatiTestuale.leggiStringaConSpazio(Costanti.INS_NOME);
            //flag per salvarmi se il piatto è stato trovato o meno
            boolean trovato = false;
            for (Prenotabile piatto : menu_ristorante) {
                //controllo che il piatto sia un piatto, che il nome sia uguale a quello inserito e che non sia già stato inserito nell'ArrayList piatti (ovvero quei piatti che saranno inseriti nel menù, solo sucessivamente)
                if (piatto instanceof Piatto && piatto.getNome().equalsIgnoreCase(nome_piatto) && !piatti.contains(piatto)) {
                    piatti.add((Piatto) piatto);
                    trovato = true;
                    break;
                }
            }
            view.ripulisciConsole();

            if (!trovato) view.stampaTesto("Piatto non trovato o già inserito nel menu");
            else view.stampaTesto("Piatto aggiunto al menu");

            InputDatiTestuale.premerePerContinuare();
            //tramite il cortocircuito evito di chiedere se si vuole aggiungere un altro piatto se il piatto non è stato trovato
        } while (piatti.size() < Costanti.MINIMO_PIATTI_PER_MENU || InputDatiTestuale.yesOrNo("Vuoi aggiungere un altro piatto al menu?"));
        menu_tematico.setPiatti_menu(piatti);
        menu_ristorante.add(menu_tematico);
        //postcondizione: il menu tematico è creato e aggiunto al menu del ristorante
        assert menu_ristorante.contains(menu_tematico);
    }

    /**
     * <h2>Metodo che crea un piatto</h2><br>
     * <b>Precondizione:</b> il gestore non è null<br>
     * <b>Postcondizione:</b> il piatto è creato e aggiunto al menu del ristorante<br>
     * @param ristorante ristorante dove il piatto è creato
     * @throws IllegalArgumentException se il ristorante è null
     */
    public void creaPiatto(Ristorante ristorante) {
        //precondizione: il ristorante non è null
        if (ristorante == null) throw new IllegalArgumentException(Costanti.GESTORE_NON_NULLO);

        Piatto piatto = (Piatto) creaPrenotabile(Costanti.PIATTO);

        int n_porzioni = InputDatiTestuale.leggiInteroConMinimo("\nInserisci il numero di porzioni delle ricetta per cucinare il piatto: ", 1);
        float lavoro_porzione = piatto.getLavoro_piatto();

        //aggiunta degli ingredienti alla ricetta
        ArrayList<Alimento> ingredienti_nuovo_piatto = new ArrayList<>();
        do {
            view.mostraAlimenti(ristorante.getMagazziniere().getMagazzino().getIngredienti());

            String nome_ingrediente = InputDatiTestuale.leggiStringaConSpazio("\nInserisci il nome dell'ingrediente: ");
            Ingrediente nuovo_ingrediente = new Ingrediente();
            boolean trovato = false;
            for (Alimento ingrediente : ristorante.getMagazziniere().getMagazzino().getIngredienti()) {
                //controllo se ho già inserito l'ingrediente, in tal caso non lo aggiungo
                boolean gia_presente = ingredienti_nuovo_piatto.stream().anyMatch(ingrediente_da_valutare -> ingrediente_da_valutare.getNome().equalsIgnoreCase(nome_ingrediente));
                //controllo che l'ingrediente sia un ingrediente, che il nome sia uguale a quello inserito e che non sia già stato inserito nell'ArrayList ingredienti_nuovo_piatto (ovvero quegli ingredienti che saranno inseriti nella ricetta, solo sucessivamente)
                if (ingrediente instanceof Ingrediente && ingrediente.getNome().equalsIgnoreCase(nome_ingrediente) && !gia_presente) {
                    nuovo_ingrediente.setNome(ingrediente.getNome());
                    nuovo_ingrediente.setQta((float) InputDatiTestuale.leggiDoubleConMinimo("Inserisci la quantità di " + nome_ingrediente + " in " + ingrediente.getMisura() + ": ", 0));
                    nuovo_ingrediente.setMisura(ingrediente.getMisura());
                    //controllo che la nuova qta sia diversa da 0, in tal caso aggiungo l'ingrediente alla ricetta
                    if(nuovo_ingrediente.getQta() != 0.0) ingredienti_nuovo_piatto.add(nuovo_ingrediente);
                    trovato = true;
                    break; //esco dal for
                }
            }

            view.ripulisciConsole();
            //informo l'utente dell'esito dell'operazione di aggiunta dell'ingrediente alla ricetta
            if (!trovato) view.stampaTesto("Ingrediente non trovato o già inserito nella ricetta");
            else if(nuovo_ingrediente.getQta() == 0.0) view.stampaTesto("Quantità non valida");
            else view.stampaTesto("Ingrediente aggiunto alla ricetta");

            InputDatiTestuale.premerePerContinuare();
            //tramite il cortocircuito evito di chiedere se si vuole aggiungere un altro ingrediente se l'ingrediente non è stato trovato
        } while (ingredienti_nuovo_piatto.size() < Costanti.MINIMO_INGRED_PER_RICETTA || InputDatiTestuale.yesOrNo("\nVuoi aggiungere un altro ingrediente alla ricetta?"));

        Ricetta ricetta = new Ricetta(ingredienti_nuovo_piatto, n_porzioni, lavoro_porzione);
        piatto.setRicetta(ricetta);
        ristorante.getAddettoPrenotazione().getMenu().add(piatto);
        //postcondizione: il piatto è creato e aggiunto al menu del ristorante
        assert ristorante.getAddettoPrenotazione().getMenu().contains(piatto);
    }

}
