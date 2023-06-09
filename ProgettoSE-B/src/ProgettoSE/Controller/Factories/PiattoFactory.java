package ProgettoSE.Controller.Factories;

import ProgettoSE.Controller.PrenotabileFactory;
import ProgettoSE.Model.Alimentari.Alimento;
import ProgettoSE.Model.Alimentari.Ingrediente;
import ProgettoSE.Model.Attori.Gestore.Ristorante;
import ProgettoSE.Model.Attori.Tempo;
import ProgettoSE.Model.Produzione.Piatto;
import ProgettoSE.Model.Produzione.Prenotabile;
import ProgettoSE.Model.Produzione.Ricetta;
import ProgettoSE.Utility.Costanti;
import ProgettoSE.View.View;

import java.time.LocalDate;
import java.util.ArrayList;

public class PiattoFactory implements PrenotabileFactory {

    @Override
    public Prenotabile creaPrenotabile(Ristorante ristorante, View view) {

        view.stampaTesto("\nInserisci i dati del piatto \n\n");
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

                if (data_inizio_parsata == null || data_fine_parsata == null) {
                    view.stampaTesto(Costanti.DATA_NON_VALIDA);
                    data_errata = true;
                } else {
                    disponibilita.add(data_inizio_parsata);
                    disponibilita.add(data_fine_parsata);
                    data_errata = false;
                }
                ;

            } while (data_errata);
        } while (view.yesOrNo("\nVuoi aggiungere un'altra disponibilità?"));


        int n_porzioni = view.leggiInteroConMinimo("\nInserisci il numero di porzioni delle ricetta per cucinare il piatto: ", 1);

        //aggiunta degli ingredienti alla ricetta
        ArrayList<Alimento> ingredienti_nuovo_piatto = new ArrayList<>();
        ArrayList<Alimento> ingredienti = ristorante.getMagazziniere().getMagazzino().getIngredienti();

        do {
            view.mostraAlimenti(ingredienti);

            String nome_ingrediente = view.leggiStringaConSpazio("\nInserisci il nome dell'ingrediente: ");
            Ingrediente nuovo_ingrediente = new Ingrediente();
            boolean trovato = false;
            for (Alimento ingrediente : ingredienti) {
                //controllo se ho già inserito l'ingrediente, in tal caso non lo aggiungo
                boolean gia_presente = ingredienti_nuovo_piatto.stream().anyMatch(ingrediente_da_valutare -> ingrediente_da_valutare.getNome().equalsIgnoreCase(nome_ingrediente));
                //controllo che l'ingrediente sia un ingrediente, che il nome sia uguale a quello inserito e che non sia già stato inserito nell'ArrayList ingredienti_nuovo_piatto (ovvero quegli ingredienti che saranno inseriti nella ricetta, solo sucessivamente)
                if (ingrediente instanceof Ingrediente && ingrediente.getNome().equalsIgnoreCase(nome_ingrediente) && !gia_presente) {
                    nuovo_ingrediente.setNome(ingrediente.getNome());
                    nuovo_ingrediente.setQta((float) view.leggiDoubleConMinimo("Inserisci la quantità di " + nome_ingrediente + " in " + ingrediente.getMisura() + ": ", 0));
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

            view.premerePerContinuare();
            //tramite il cortocircuito evito di chiedere se si vuole aggiungere un altro ingrediente se l'ingrediente non è stato trovato
        } while (ingredienti_nuovo_piatto.size() < Costanti.MINIMO_INGRED_PER_RICETTA || view.yesOrNo("\nVuoi aggiungere un altro ingrediente alla ricetta?"));

        return new Piatto(nome, disponibilita, lavoro, new Ricetta(ingredienti_nuovo_piatto, n_porzioni, lavoro));

    }
}
