package ProgettoSE.Controller.Factories;

import ProgettoSE.Controller.PrenotabileFactory;
import ProgettoSE.Model.Attori.Gestore.Ristorante;
import ProgettoSE.Model.Attori.Tempo;
import ProgettoSE.Model.Produzione.Menu.MenuTematico;
import ProgettoSE.Model.Produzione.Piatto;
import ProgettoSE.Model.Produzione.Prenotabile;
import ProgettoSE.Utility.Costanti;
import ProgettoSE.View.View;

import java.time.LocalDate;
import java.util.ArrayList;

public class MenuTematicoFactory implements PrenotabileFactory {

    @Override
    public Prenotabile creaPrenotabile(Ristorante ristorante, View view) {

        //precondizione: il ristorante non è null
        if (ristorante == null) throw new IllegalArgumentException(Costanti.RISTORANTE_NON_NULLO);

        view.stampaTesto("\nInserisci i dati del manu tematico: \n\n");
        String nome = view.leggiStringaConSpazio(Costanti.INS_NOME);
        float lavoro = (float) view.leggiDoubleConMinimo(Costanti.INS_LAVORO, 0);
        ArrayList<LocalDate> disponibilita = new ArrayList<>();

        do {
            boolean data_errata; //variabile per permettere di reinserire immediamente delle nuvo disponibilità in caso quelle inserite siano scorrette
            do {
                String data_inizio_da_parsare = view.leggiStringaNonVuota(Costanti.INS_DATA_INIZIO);
                String data_fine_da_parsare = view.leggiStringaNonVuota(Costanti.INS_DATA_FINE);

                LocalDate data_inizio_parsata = Tempo.parsaData(data_inizio_da_parsare);
                LocalDate data_fine_parsata = Tempo.parsaData(data_fine_da_parsare);

                if(data_inizio_parsata == null || data_fine_parsata == null){
                    view.stampaTesto(Costanti.DATA_NON_VALIDA);
                    data_errata = true;
                } else {
                    disponibilita.add(data_inizio_parsata);
                    disponibilita.add(data_fine_parsata);
                    data_errata = false;
                };

            } while (data_errata);
        } while (view.yesOrNo("\nVuoi aggiungere un'altra disponibilità?"));

        ArrayList<Piatto> piatti = new ArrayList<>();
        ArrayList<Prenotabile> menu_ristorante = ristorante.getAddettoPrenotazione().getMenu();
        do {
            //prima mostro i piatti presenti nel ristorante
            view.mostraPiatti(menu_ristorante);

            String nome_piatto = view.leggiStringaConSpazio(Costanti.INS_NOME);
            //flag per salvarmi se il piatto è stato trovato o meno
            boolean trovato = false;
            for (Prenotabile piatto : menu_ristorante) {
                //controllo che il piatto sia un piatto, che il nome sia uguale a quello inserito e che non sia già stato inserito nell'ArrayList piatti (ovvero quei piatti che saranno inseriti nel menù, solo successivamente)
                if (piatto instanceof Piatto && piatto.getNome().equalsIgnoreCase(nome_piatto) && !piatti.contains(piatto)) {
                    piatti.add((Piatto) piatto);
                    trovato = true;
                    break;
                }
            }
            view.ripulisciConsole();

            if (!trovato) view.stampaTesto("Piatto non trovato o già inserito nel menu");
            else view.stampaTesto("Piatto aggiunto al menu");

            view.premerePerContinuare();
            //tramite il cortocircuito evito di chiedere se si vuole aggiungere un altro piatto se il piatto non è stato trovato
        } while (piatti.size() < Costanti.MINIMO_PIATTI_PER_MENU || view.yesOrNo("Vuoi aggiungere un altro piatto al menu?"));

        return new MenuTematico(nome, piatti, lavoro, disponibilita);
    }

}
