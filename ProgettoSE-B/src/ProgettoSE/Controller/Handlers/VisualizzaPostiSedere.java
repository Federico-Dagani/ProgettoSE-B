package ProgettoSE.Controller.Handlers;

import ProgettoSE.Controller.Handler;
import ProgettoSE.View.View;
import ProgettoSE.Model.Attori.Gestore.Ristorante;


public class VisualizzaPostiSedere implements Handler {

    @Override
    public void esegui(View view, Ristorante ristorante){
        view.mostraPostiDisponibili(ristorante.getN_posti());
    }

}
