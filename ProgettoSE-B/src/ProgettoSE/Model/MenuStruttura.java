package ProgettoSE.Model;

import ProgettoSE.Utility.Costanti;
import ProgettoSE.Utility.MyMenu;

import java.util.Locale;

public class MenuStruttura {

    /**
     * <h2>Metodo che crea un menu per la visualizzazione delle funzionalità disponibili</h2><br>
     * <b>Precondizione:</b> la funzione non è null<br>
     * @param funzione funzionalità che dovrà aver il menu
     * @return il menu creato
     * @throws IllegalArgumentException se la funzione è null
     */
    public static MyMenu creaMenuStruttura(String funzione) {
        //precondizione: la funzione non è null
        if(funzione == null) throw new IllegalArgumentException("La funzione non può essere null");

        switch (funzione) {

            case Costanti.ATTORI:

                String[] utenti = new String[3];
                utenti[0] = Costanti.GESTORE;
                utenti[1] = Costanti.UTENTE;
                utenti[2] = Costanti.TEMPO;
                return new MyMenu(Costanti.ATTORI.toUpperCase(Locale.ROOT), utenti);

            case Costanti.GESTORE:

                String[] azioni_gestore = new String[9];
                azioni_gestore[0] = "Visualizza il carico di lavoro per persona";
                azioni_gestore[1] = "Visualizza il numero di posti a sedere disponibili";
                azioni_gestore[2] = "Visualizza l'insieme delle bevande";
                azioni_gestore[3] = "Visualizza l'insieme dei generi extra";
                azioni_gestore[4] = "Visualizza il consumo pro-capite di bevande";
                azioni_gestore[5] = "Visualizza il consumo pro-capite di generi extra";
                azioni_gestore[6] = "Visualizza i menu tematici presenti nel menu";
                azioni_gestore[7] = "Visualizza i piatti presenti nel menu";
                azioni_gestore[8] = "Visualizza il ricettario";
                return new MyMenu("     " + Costanti.FUNZIONALITA.toUpperCase(Locale.ROOT) + Costanti.GESTORE.toUpperCase(Locale.ROOT) + "     ", azioni_gestore);

            case Costanti.TEMPO:

                String[] azioni_tempo = new String[2];
                azioni_tempo[0] = "Incrementa di un giorno";
                azioni_tempo[1] = "Scegli una data";
                return new MyMenu(Costanti.FUNZIONALITA.toUpperCase(Locale.ROOT) + Costanti.TEMPO.toUpperCase(Locale.ROOT), azioni_tempo);

            case Costanti.INIZIALIZZAZIONE:

                String[] azioni_inizializzazione = new String[7];
                azioni_inizializzazione[0] = "Modifica il numero di posti del ristorante";
                azioni_inizializzazione[1] = "Modifica il lavoro in carico ad ogni persona";
                azioni_inizializzazione[2] = "Aggiungi un ingrediente";
                azioni_inizializzazione[3] = "Aggiungi un'extra";
                azioni_inizializzazione[4] = "Aggiungi una bevanda";
                azioni_inizializzazione[5] = "Aggiungi un menu";
                azioni_inizializzazione[6] = "Aggiungi un piatto";
                return new MyMenu(Costanti.FUNZIONALITA.toUpperCase(Locale.ROOT) + "di " + Costanti.INIZIALIZZAZIONE.toUpperCase(Locale.ROOT), azioni_inizializzazione);
        }
        return null;
    }
}
