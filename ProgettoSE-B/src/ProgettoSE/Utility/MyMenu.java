package ProgettoSE.Utility;

import ProgettoSE.View.InputDatiTestuale;

import java.util.Locale;

/*
Questa classe rappresenta un menu testuale generico a piu' voci
Si suppone che la voce per uscire sia sempre associata alla scelta 0 
e sia presentata in fondo al menu

*/
public class MyMenu {
    final private static String CORNICE_ANGOLO_SX_SUP = "┌";
    final private static String CORNICE_ANGOLO_DX_SUP = "┐";
    final private static String CORNICE_ANGOLO_SX_INF = "└";
    final private static String CORNICE_ANGOLO_DX_INF = "┘";
    final private static String CORNICE_CONTORNO_ORIZZ = "-";
    final private static String CORNICE_CONTORNO_VERT = "|";
    final private static String FRECCETTA = "→";//⟼
    final private static String VOCE_USCITA = "0 → Esci";
    final private static String RICHIESTA_INSERIMENTO = "Digita il numero dell'opzione desiderata > ";

    private String titolo;
    private String[] voci;
    private int lunghezza_cornice_orizz;

    public MyMenu(String titolo, String[] voci) {
        this.titolo = titolo;
        this.voci = voci;
        this.lunghezza_cornice_orizz = this.titolo.length();
    }

    public int scegliConUscita() {
        stampaMenuConUscita();
        return InputDatiTestuale.leggiIntero(RICHIESTA_INSERIMENTO, 0, voci.length);
    }

    public int scegliSenzaUscita() {
        stampaMenuSenzaUscita();
        return InputDatiTestuale.leggiIntero(RICHIESTA_INSERIMENTO, 0, voci.length);
    }

    public void stampaMenuConUscita() {
        String cornice_sup = "";
        cornice_sup += CORNICE_ANGOLO_SX_SUP;
        cornice_sup += generaCorniceSenzaAngolo();
        cornice_sup += CORNICE_ANGOLO_DX_SUP;
        String cornice_inf = "";
        cornice_inf += CORNICE_ANGOLO_SX_INF;
        cornice_inf += generaCorniceSenzaAngolo();
        cornice_inf += CORNICE_ANGOLO_DX_INF;
        System.out.println(cornice_sup);
        System.out.println(generaRigaCentrale());
        System.out.println(cornice_inf);
        System.out.println(cornice_sup);
        for (int i = 0; i < voci.length; i++) {
            System.out.println(generaVoce((i + 1) + " " + FRECCETTA + " " + voci[i]));
        }
        System.out.println(generaVoce((0) + " " + FRECCETTA + " " + "esci"));
        System.out.println(cornice_inf);
    }

    public void stampaMenuSenzaUscita() {
        String cornice_sup = "";
        cornice_sup += CORNICE_ANGOLO_SX_SUP;
        cornice_sup += generaCorniceSenzaAngolo() ;
        cornice_sup += CORNICE_ANGOLO_DX_SUP;
        String cornice_inf = "";
        cornice_inf += CORNICE_ANGOLO_SX_INF;
        cornice_inf += generaCorniceSenzaAngolo();
        cornice_inf += CORNICE_ANGOLO_DX_INF;
        System.out.println(cornice_sup);
        System.out.println(generaRigaCentrale());
        System.out.println(cornice_inf);
        System.out.println(cornice_sup);
        for (int i = 0; i < voci.length; i++) {
            System.out.println(generaVoce(FRECCETTA + " " + voci[i]));
        }
        System.out.println(cornice_inf);
        System.out.println();
    }

    private String generaCorniceSenzaAngolo() {
        String cornice = "";
        for (int i = 0; i < titolo.length(); i++) {
            cornice += CORNICE_CONTORNO_ORIZZ;
            cornice += CORNICE_CONTORNO_ORIZZ;
        }
        lunghezza_cornice_orizz = titolo.length() * 2 + 2;
        return cornice;
    }

    private String generaRigaCentrale() {
        char[] riga_centrale = new char[lunghezza_cornice_orizz];
        riga_centrale[0] = CORNICE_CONTORNO_VERT.charAt(0);
        riga_centrale[lunghezza_cornice_orizz - 1] = CORNICE_CONTORNO_VERT.charAt(0);
        int caratteri_vuoti_meta = ((lunghezza_cornice_orizz - 2) - titolo.length()) / 2 + 1;
        int c = 0;
        for (int i = caratteri_vuoti_meta; i < (titolo.length() + caratteri_vuoti_meta); i++) {
            riga_centrale[i] = titolo.charAt(c);
            c++;
        }
        for (int i = 0; i < lunghezza_cornice_orizz; i++) {
            if (riga_centrale[i] == Character.MIN_VALUE) {
                riga_centrale[i] = ' ';
            }
        }
        return new String(riga_centrale);
    }

    private String generaVoce(String voce) {
        char[] voce_finale_char = new char[lunghezza_cornice_orizz];
        voce_finale_char[0] = CORNICE_CONTORNO_VERT.charAt(0);
        voce_finale_char[lunghezza_cornice_orizz - 1] = CORNICE_CONTORNO_VERT.charAt(0);
        char[] voce_char = new char[voce.length()];
        int c = 0;
        for (int i = 1; i < voce_char.length + 1; i++) {
            voce_finale_char[i] = voce.charAt(c);
            c++;
        }
        for (int i = 0; i < lunghezza_cornice_orizz; i++) {
            if (voce_finale_char[i] == Character.MIN_VALUE) {
                voce_finale_char[i] = ' ';
            }
        }
        return new String(voce_finale_char);
    }


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

