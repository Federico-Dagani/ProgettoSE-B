package ProgettoSE.View;

import ProgettoSE.Model.Alimentari.Alimento;
import ProgettoSE.Model.Alimentari.Bevanda;
import ProgettoSE.Model.Alimentari.Extra;
import ProgettoSE.Model.Produzione.Menu.MenuTematico;
import ProgettoSE.Model.Produzione.Piatto;
import ProgettoSE.Model.Produzione.Prenotabile;
import ProgettoSE.Utility.Costanti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InterfacciaTestuale implements View {

    private static Scanner lettore = creaScanner();


    /**
     * <h2>Metodo che stampa a video il menu del giorno</h2>
     * <b>Precondizione: </b>ristorante e data non nulli<br>
     * @param data data di cui si vuole visualizzare il menu
     * @return true se il menu è vuoto, false altrimenti
     * @throws IllegalArgumentException se i parametri non sono validi
     */
    public boolean stampaMenuDelGiorno(ArrayList<Prenotabile> menu_del_giorno, LocalDate data) {
        //precondizione: menu_del_giorno e data non nulli
        if(menu_del_giorno == null || data == null) throw new IllegalArgumentException("Parametri non validi");
        ripulisciConsole();
        //se il menu è vuoto stampo un messaggio e ritorno true
        if (menu_del_giorno.isEmpty()) {
            System.out.println("Non ci sono piatti disponibili per il giorno " + data);
            return true;
        } else {
            System.out.println("\nIl menù disponibile per il giorno " + data + " offre queste specialità:");
            System.out.println("(può scegliere sia i piatti all'interno del menù alla carta che i menù tematici presenti) \n");
            //ciclo l'arraylist di prenotabili e stampo a video i piatti e i menù tematici
            for (Prenotabile prenotabile : menu_del_giorno) {
                ArrayList<String> elementi = prenotabile.mostraPrenotabile();
                System.out.printf("- " + elementi.get(0).toUpperCase() + ": (  ");
                for (int i =1; i< elementi.size()-1; i++){
                    System.out.printf(elementi.get(i) + "  ");
                }
                System.out.printf(")\n\n");
            }
            //ritorno false perchè il menu non è vuoto
            return false;
        }
    }

    /**
     * <h2>Metodo per migliorare l'interazinoe con il programma</h2>
     * Comunica in output un messaggio i benvenuto con all'interno le info essenziali
     */
    public void benvenuto() {
        System.out.println(Costanti.CORNICE_SUP);
        System.out.println("|\t" + Costanti.BENVENUTO + "\t|");
        System.out.println(Costanti.CORNICE_INF);
        System.out.println();
    }


    /**
     * <h2>Metodo per l'inserimento del nome del gestore</h2>
     * @return nome del gestore
     */
    public String inserisciNomeGestore() {
        return leggiStringaConSpazio("Benvenuto, inserisca il nome del gestore del ristorante: ");
    }

    /**
     * metodo che stampa le fasi di inizializzazione del ristorante
     * @param list lista contenente i prenotabili presenti nei dati di inizializzazione che sono stati
     *             ritenuti invalidi e quindi eliminati dal modello
     */
    public void stampaInizializzazione(ArrayList<Prenotabile> list){
        System.out.printf("\nIl gestore sta inizializzando il ristorante ...");
        stampaElementiInvalidi(list);
        System.out.println("\nInizializzazione automatica del ristorante completata.\n");
    }

    public void stampaElementiInvalidi(ArrayList<Prenotabile> list){
        if(!list.isEmpty()){
            System.out.println("\nElementi invalidi trovati:");
            list.forEach(prenotabile -> System.out.printf("\n\t " + prenotabile.getNome()));
        }
    }

    /**
     * <h2>Metodo che stampa a video la lista dei piatti e menu scelti fino a quel momento in una prenotazione</h2>
     * <b>Precondizione: </b>mappa scelte non nulla<br>
     * @param scelte HashMap contenente i piatti e i menù tematici scelti finora con relativa quantità
     * @throws IllegalArgumentException se i parametri non sono validi
     */
    public void stampaScelte(HashMap<Prenotabile, Integer> scelte) {
        //precondizione: scelte non nulle
        if(scelte == null) throw new IllegalArgumentException("Parametri non validi");
        //se è vuoto evito di stampare delle stringhe inutili ed esco dal metodo
        if (scelte.isEmpty()) {
            return;
        }
        System.out.println("Le scelte effettuate finora sono le seguenti: ");
        //scorro le key dell'hashmap e stampo a video i piatti e i menù tematici con la relativa value che rappresenta la quantità scelta
        for (Prenotabile prenotabile : scelte.keySet()) {
                System.out.println("- " + prenotabile.getNome().toUpperCase() + ", " + "quantità: " + scelte.get(prenotabile) + "\n\n");
        }
    }

    /**
     * <h2>Metodo che utilizza il polimorfismo per stampare a video la lista di extra o di bevande con relativi consumi procapite</h2>
     * <b>Precondizione: </b>arraylist alimenti non nullo<br>
     * @param alimenti ArrayList di tipo Alimento contenente gli extra o le bevande
     * @throws IllegalArgumentException se i parametri non sono validi
     */
    public void mostraConsumoProcapite(ArrayList<Alimento> alimenti) {
        //precodizione: alimenti non nullo
        if(alimenti == null) throw new IllegalArgumentException("Parametri non validi");

        //stampo a video il titolo sfruttando il polimorfismo
        System.out.println("\nLista di " + alimenti.get(0).getClass().getSimpleName() + " con i relativi consumi procapite: ");

        //stampo a video gli alimenti con i relativi consumi procapite sfruttando il polimorfismo
        for (Alimento alimento : alimenti) {
            System.out.printf("\n- " + alimento.getNome() + ", ");
            System.out.printf("consumo procapite: " + alimento.getCons_procapite() + "\n");
        }
    }

    /**
     * <h2>Metodo che utilizza il polimorfismo per stampare a video la lista dei nomi di extra o di bevande</h2>
     * <b>Precondizione: </b>arraylist alimenti non nullo<br>
     * @param alimenti ArrayList di tipo Alimento contenente gli extra o le bevande
     * @throws IllegalArgumentException se alimenti è nullo
     */
    public void mostraAlimenti(ArrayList<Alimento> alimenti) {
        //precondizione: alimenti non nullo
        if(alimenti == null) throw new IllegalArgumentException("Parametri non validi");

        System.out.println("\nLista di " + alimenti.get(0).getClass().getSimpleName() + " presenti nel ristorante: ");
        //stampo a video gli alimenti sfruttando il polimorfismo
        for (Alimento alimento : alimenti) {
            System.out.printf("\n- " + alimento.getNome() );
        }
    }

    /**
     * <h2>Metodo che stampa a video il carico di lavoro per persona</h2>
     * <b>Precondizione: </b>lavoro persona non nullo<br>
     * @param lavoro_persona
     * @throws IllegalArgumentException se i parametri non sono validi
     */
    public void mostraCaricoLavoroPersona(int lavoro_persona){
        //precondizione: gestore non nullo
        if(lavoro_persona == 0) throw new IllegalArgumentException("Il lavoro persona non può essere nullo");
        System.out.println("\nIl carico di lavoro per persona è: " + lavoro_persona);
    }

    /**
     * <h2>Metodo che stampa a video il numero di posti disponibili nel ristorante</h2>
     * <b>Precondizione: </b>n_posti non nullo<br>
     * @param n_posti
     * @throws IllegalArgumentException se il gestore è nullo
     */
    public void mostraPostiDisponibili(int n_posti){
        //precondizione: gestore non nullo
        if(n_posti == 0) throw new IllegalArgumentException("Il numero di posti non può essere nullo");
        System.out.println("\nIl numero di posti disponibili nel ristorante è: " + n_posti);
    }

    /**
     * <h2>Metodo che stampa a video i menu tematici presenti nel menu e le relative disponibilità</h2>
     * <b>Precondizione: </b>menu non nullo<br>
     * @param menu ArrayList di tipo Prenotabile contenente i menu tematici e i piatti
     * @throws IllegalArgumentException se il menu è nullo
     */
    public void mostraMenuTematici(ArrayList<Prenotabile> menu) {
        //precondizione: menu non nullo
        if(menu == null) throw new IllegalArgumentException("Il menu non può essere nullo");
        System.out.println("\n\nI menu tematici del menù alla carta sono i seguenti: ");
        for (Prenotabile prenotabile : menu) {
            //ciclo su i prenotabili e stampo a video solo i menu tematici
            if (prenotabile instanceof MenuTematico) {
                MenuTematico menuTematico = (MenuTematico) prenotabile;
                System.out.println("\nNome " + menuTematico.getNome());
                //stampo i relativi periodi di disponibilità in righe diverse
                System.out.println("Periodi disponibilità: ");
                int inizio = 0;
                for (int i = 0; i < menuTematico.getDisponibilità().toArray().length / 2; i++) {
                    System.out.println("Inizio: " + menuTematico.getDisponibilità().get(inizio) + "\tFine: " + menuTematico.getDisponibilità().get(inizio + 1));
                    inizio += 2;
                }
            }
        }
    }

    /**
     * <h2>Metodo che stampa a video i piatti presenti nel menu e le relative disponibilità</h2>
     * <b>Precondizione: </b>menu non nullo<br>
     * @param menu ArrayList di tipo Prenotabile contenente i piatti e i menu tematici
     * @throws IllegalArgumentException se il menu è nullo
     */
    public void mostraPiatti(ArrayList<Prenotabile> menu) {
        //precondizione: menu non nullo
        if(menu == null) throw new IllegalArgumentException("Il menu non può essere nullo");
        System.out.println("\n\nI piatti del menù alla carta sono i seguenti: ");
        for (Prenotabile prenotabile : menu) {
            if (prenotabile instanceof Piatto) {
                Piatto piatto = (Piatto) prenotabile;
                System.out.println("\nNome " + piatto.getNome());
                //stampo i relativi periodi di disponibilità in righe diverse
                System.out.println("Periodi disponibilità: ");
                int inizio = 0;
                for (int i = 0; i < piatto.getDisponibilità().toArray().length / 2; i++) {
                    System.out.println("Inizio: " + piatto.getDisponibilità().get(inizio) + "\tFine: " + piatto.getDisponibilità().get(inizio + 1));
                    inizio += 2;
                }
            }
        }
    }

    /**
     * <h2>Metodo che stampa a video le ricette dei piatti presenti nel menu</h2>
     * <b>Precondizione: </b>menu non nullo<br>
     * @param menu ArrayList di tipo Prenotabile contenente i piatti e i menu tematici
     * @throws IllegalArgumentException se il menu è nullo
     */
    public void mostraRicette(ArrayList<Prenotabile> menu) {
        //precondizione: menu non nullo
        if(menu == null) throw new IllegalArgumentException("Il menu non può essere nullo");
        System.out.println("\n\nLe ricette del menù alla carta sono le seguenti: ");
        for (Prenotabile prenotabile : menu) {
            if (prenotabile instanceof Piatto) {
                Piatto piatto = (Piatto) prenotabile;
                System.out.println("\n" + piatto.getNome().toUpperCase());
                System.out.println("Ricetta: ");
                //ciclo su tutti gli ingredienti del piatto e stampo a video su righe diverse
                for (Alimento ingrediente : piatto.getRicetta().getIngredienti()) {
                    System.out.println("  °\t" + ingrediente.getQta() + " " + ingrediente.getMisura() + " di " + ingrediente.getNome());
                }
            }
        }
    }

    public void stampaTesto(String messaggio){
        System.out.printf("\n"+messaggio);
    }


    public void stampaTesto(String messaggio, String elemento){
        System.out.printf("\n" + String.format(messaggio, elemento));
    }


    public void stampaListaSpesa(ArrayList<Alimento> lista_spesa){
        if (lista_spesa == null) System.out.println("\nNon c'è nessuna prenotazione per questa data");
        else if(lista_spesa.isEmpty()) System.out.println("\n\nLe disponibilità in magazzino riescono a soddisfare tutte le prenotazioni della giornata senza richiedere l'acquisto di ulteriori alimenti\n\n");
        else{
            String messaggio = "";
            for (Alimento alimento : lista_spesa) {
                messaggio += "\n- " + alimento.getNome() + " in quantità pari a: " + String.format("%.2f", alimento.getQta()) + " " + alimento.getMisura() + "\n";
            }
            System.out.println(messaggio);
        }
    }

    /**
     * <h2>Metodo utilizzato nella stampa a video per pulire l'interfaccia</h2>
     */
    public void ripulisciConsole() {
        for (int i = 0; i < 30; i++)
            System.out.println();
    }

    private static Scanner creaScanner() {
        return new Scanner(System.in).useDelimiter("\n");
    }

    public String leggiStringa(String messaggio) {
        System.out.print("\n"+messaggio);
        return lettore.next();
    }

    public String leggiStringaConSpazio(String messaggio){
        System.out.printf("\n"+messaggio);
        return lettore.next();
    }

    public String leggiStringaNonVuota(String messaggio) {
        boolean finito = false;
        String lettura = null;
        do {
            lettura = leggiStringa(messaggio);
            lettura = lettura.trim();
            if (lettura.length() > 0)
                finito = true;
            else
                System.out.println("\n"+ Costanti.ERRORE_STRINGA_VUOTA);
        } while (!finito);

        return lettura;
    }

    public char leggiChar(String messaggio) {
        boolean finito = false;
        char valoreLetto = '\0';
        do {
            System.out.print(messaggio);
            String lettura = lettore.next();
            if (lettura.length() > 0) {
                valoreLetto = lettura.charAt(0);
                finito = true;
            } else {
                System.out.println("\n"+ Costanti.ERRORE_STRINGA_VUOTA);
            }
        } while (!finito);
        return valoreLetto;
    }

    public char leggiUpperChar(String messaggio, String ammissibili) {
        boolean finito = false;
        char valoreLetto = '\0';
        do {
            valoreLetto = leggiChar(messaggio);
            valoreLetto = Character.toUpperCase(valoreLetto);
            if (ammissibili.indexOf(valoreLetto) != -1)
                finito = true;
            else
                System.out.println(Costanti.MESSAGGIO_AMMISSIBILI + ammissibili);
        } while (!finito);
        return valoreLetto;
    }


    public int leggiIntero(String messaggio) {
        boolean finito = false;
        int valoreLetto = 0;
        do {
            System.out.print("\n"+messaggio);
            try {
                valoreLetto = lettore.nextInt();
                finito = true;
            } catch (InputMismatchException e) {
                System.out.println("\n"+ Costanti.ERRORE_FORMATO);
                String daButtare = lettore.next();
            }
        } while (!finito);
        return valoreLetto;
    }

    public int leggiInteroPositivo(String messaggio) {
        return leggiInteroConMinimo(messaggio, 1);
    }

    public int leggiInteroNonNegativo(String messaggio) {
        return leggiInteroConMinimo(messaggio, 0);
    }

    public int leggiInteroConMinimoMassimo(String messaggio, int min, int max) {
        boolean finito = false;
        int valoreLetto = 0;
        do{
            valoreLetto = leggiIntero(messaggio);
            if(valoreLetto >= min && valoreLetto <= max)
                finito = true;
            else
                System.out.printf(Costanti.ERRORE_MINIMO_MASSIMO + "\n", min, max);
        }while(!finito);

        return valoreLetto;
    }

    public int leggiInteroConMinimo(String messaggio, int minimo) {
        boolean finito = false;
        int valoreLetto = 0;
        do {
            valoreLetto = leggiIntero(messaggio);
            if (valoreLetto >= minimo)
                finito = true;
            else
                System.out.println("\n"+ Costanti.ERRORE_MINIMO + minimo);
        } while (!finito);

        return valoreLetto;
    }

    public int leggiIntero(String messaggio, int minimo, int massimo) {
        boolean finito = false;
        int valoreLetto = 0;
        do {
            valoreLetto = leggiIntero(messaggio);
            if (valoreLetto >= minimo && valoreLetto <= massimo)
                finito = true;
            else if (valoreLetto < minimo)
                System.out.println("\n" + Costanti.ERRORE_MINIMO + minimo);
            else
                System.out.println("\n" + Costanti.ERRORE_MASSIMO + massimo);
        } while (!finito);

        return valoreLetto;
    }


    public double leggiDouble(String messaggio) {
        boolean finito = false;
        double valoreLetto = 0;
        do {
            System.out.print("\n"+messaggio);
            try {
                valoreLetto = lettore.nextDouble();
                finito = true;
            } catch (InputMismatchException e) {
                System.out.println(Costanti.ERRORE_FORMATO);
                String daButtare = lettore.next();
            }
        } while (!finito);
        return valoreLetto;
    }

    public double leggiDoubleConMinimo(String messaggio, double minimo) {
        boolean finito = false;
        double valoreLetto = 0;
        do {
            valoreLetto = leggiDouble(messaggio);
            if (valoreLetto >= minimo)
                finito = true;
            else
                System.out.println(Costanti.ERRORE_MINIMO + minimo);
        } while (!finito);

        return valoreLetto;
    }


    public boolean yesOrNo(String messaggio) {
        String mioMessaggio = messaggio + "(" + Costanti.RISPOSTA_SI + "/" + Costanti.RISPOSTA_NO + ") ";
        char valoreLetto = leggiUpperChar(mioMessaggio, String.valueOf(Costanti.RISPOSTA_SI) + String.valueOf(Costanti.RISPOSTA_NO));

        if (valoreLetto == Costanti.RISPOSTA_SI)
            return true;
        else
            return false;
    }

    public void premerePerContinuare(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("\n\nPremere un tasto per continuare ... ");
            br.readLine();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
