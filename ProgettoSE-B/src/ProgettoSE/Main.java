package ProgettoSE;
//importa classi alimenti

//importa classi attori

import ProgettoSE.Model.Attori.Gestore.Gestore;

//importa classe MenuTematico
//importa classi utilità
import ProgettoSE.Model.Attori.Tempo;
import ProgettoSE.Controller.Controller;
import ProgettoSE.View.InterfacciaTestuale;
import ProgettoSE.View.View;

//importa classi per gestione input da tastiera
import java.io.IOException;
//importa classi per utilizzo costrutti
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Main {

    public static void main(String[] args) throws IOException, DateTimeParseException {

        //scelta della interfaccia utente
        View view = new InterfacciaTestuale();

        Controller controller = new Controller(view);

        view.benvenuto();

        Gestore gestore = new Gestore(view.inserisciNomeGestore(), null);

        Tempo data_attuale = new Tempo(LocalDate.now());

        controller.inizializzazione(gestore);

        controller.eseguiIterazioni(gestore, data_attuale);
    }

}