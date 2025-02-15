package ProgettoSE.View;

import ProgettoSE.Model.Alimentari.Alimento;
import ProgettoSE.Model.Produzione.Prenotabile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public interface View {
    boolean stampaMenuDelGiorno(ArrayList<Prenotabile> menu_del_giorno, LocalDate data);

    void benvenuto();

    String inserisciNomeGestore();

    void stampaInizializzazione(ArrayList<Prenotabile> list);

    void stampaElementiInvalidi(ArrayList<Prenotabile> list);

    void stampaScelte(HashMap<Prenotabile, Integer> scelte);

    void mostraConsumoProcapite(ArrayList<Alimento> alimenti);

    void mostraAlimenti(ArrayList<Alimento> alimenti);

    void mostraCaricoLavoroPersona(int lavoro_persona);

    void mostraPostiDisponibili(int n_posti);

    void mostraMenuTematici(ArrayList<Prenotabile> menu);

    void mostraPiatti(ArrayList<Prenotabile> menu);

    void mostraRicette(ArrayList<Prenotabile> menu);

    void stampaTesto(String messaggio, String elemento);

    void stampaTesto(String messaggio);

    void stampaListaSpesa(ArrayList<Alimento> lista_spesa);

    void ripulisciConsole();

    String leggiStringa(String messaggio);

    String leggiStringaConSpazio(String messaggio);

    String leggiStringaNonVuota(String messaggio);

    char leggiChar(String messaggio);

    char leggiUpperChar(String messaggio, String ammissibili);

    int leggiIntero(String messaggio);

    int leggiInteroPositivo(String messaggio);

    int leggiInteroNonNegativo(String messaggio);

    int leggiInteroConMinimoMassimo(String messaggio, int min, int max);

    int leggiInteroConMinimo(String messaggio, int minimo);

    int leggiIntero(String messaggio, int minimo, int massimo);

    double leggiDouble(String messaggio);

    double leggiDoubleConMinimo(String messaggio, double minimo);

    boolean yesOrNo(String messaggio);

    void premerePerContinuare();

    }
