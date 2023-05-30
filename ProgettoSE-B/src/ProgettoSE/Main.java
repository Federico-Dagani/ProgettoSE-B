package ProgettoSE;
//importa classi alimenti

//importa classi attori

import ProgettoSE.Model.Attori.Gestore.Gestore;

//importa classe MenuTematico
//importa classi utilità
import ProgettoSE.Model.Attori.Tempo;
import ProgettoSE.Controller.Controller;
import ProgettoSE.View.InterfacciaTestuale;
//importa classi per gestione input da tastiera
import java.io.IOException;
//importa classi per utilizzo costrutti
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Main {

    public static void main(String[] args) throws IOException, DateTimeParseException {

        InterfacciaTestuale.benvenuto();

        Gestore gestore = new Gestore(InterfacciaTestuale.inserisciNomeGestore(), null);

        Tempo data_attuale = new Tempo(LocalDate.now());

        Controller.inizializzazione(gestore);

        Controller.eseguiIterazioni(gestore, data_attuale);
    }

}