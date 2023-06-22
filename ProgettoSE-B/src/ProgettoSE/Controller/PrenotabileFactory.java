package ProgettoSE.Controller;

import ProgettoSE.Model.Attori.Gestore.Ristorante;
import ProgettoSE.Model.Attori.Tempo;
import ProgettoSE.Model.Produzione.Prenotabile;
import ProgettoSE.Utility.Costanti;
import ProgettoSE.View.View;

import java.time.LocalDate;
import java.util.ArrayList;

public abstract class PrenotabileFactory {

    //metodo template
    public final void creaPrenotabile(Prenotabile prenotabile, Ristorante ristorante, View view){
        datiInizialiPrenotabile(view, prenotabile);
        datiSpecificiPrenotabile(view, ristorante, prenotabile);
    };

    protected void datiInizialiPrenotabile(View view, Prenotabile prenotabile){
        view.stampaTesto("\nInserisci i dati del %s: \n\n", prenotabile.getClass().getSimpleName());
        prenotabile.setNome(view.leggiStringaConSpazio(Costanti.INS_NOME));
        prenotabile.setLavoro((float)view.leggiDoubleConMinimo(Costanti.INS_LAVORO, 0));
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
        prenotabile.setDisponibilita(disponibilita);
    };

    protected void datiSpecificiPrenotabile(View view, Ristorante ristorante, Prenotabile prenotabile){};
}
