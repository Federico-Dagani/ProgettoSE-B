package ProgettoSE.Controller.Handlers;

import ProgettoSE.Controller.Handler;
import ProgettoSE.View.View;
import ProgettoSE.Model.Attori.Gestore.Ristorante;

public class VisualizzaConsumoBevande implements Handler {
    @Override
    public void esegui(View view, Ristorante ristorante){
        view.mostraConsumoProcapite(ristorante.getMagazziniere().getMagazzino().getBevande());
    }
}
