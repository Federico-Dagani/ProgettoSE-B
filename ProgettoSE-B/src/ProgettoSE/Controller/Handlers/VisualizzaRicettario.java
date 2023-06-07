package ProgettoSE.Controller.Handlers;

import ProgettoSE.Controller.Handler;
import ProgettoSE.Model.Attori.Gestore.Ristorante;
import ProgettoSE.View.View;

public class VisualizzaRicettario implements Handler {
    @Override
    public void esegui(View view, Ristorante ristorante) {
        view.mostraRicette(ristorante.getAddettoPrenotazione().getMenu());
    }
}
