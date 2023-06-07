package ProgettoSE.Controller;

import ProgettoSE.Model.Attori.Gestore.Ristorante;
import ProgettoSE.View.View;

public interface Handler {
    void esegui(View view, Ristorante ristorante);
}
