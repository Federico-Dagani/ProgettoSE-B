package ProgettoSE.Controller;

import ProgettoSE.Model.Attori.Gestore.Ristorante;
import ProgettoSE.Model.Produzione.Prenotabile;
import ProgettoSE.View.View;

public interface PrenotabileFactory {
    Prenotabile creaPrenotabile(Ristorante ristorante, View view);
}
