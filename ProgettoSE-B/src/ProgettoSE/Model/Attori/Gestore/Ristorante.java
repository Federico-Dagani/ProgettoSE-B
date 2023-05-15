package ProgettoSE.Model.Attori.Gestore;

import ProgettoSE.Model.Attori.AddettoPrenotazione.AddettoPrenotazione;
import ProgettoSE.Model.Attori.Magazziniere.Magazziniere;

public class Ristorante {
    //ATTRIBUTI
    private int n_posti;
    private int lavoro_persona;
    private AddettoPrenotazione addettoPrenotazione;
    private Magazziniere magazziniere;

    //METODI
    /**
     * <h2>Metodo costruttore della classe Ristorante</h2>
     * @param n_posti numero di posti del ristorante
     * @param lavoro_persona lavoro per una persona
     * @param addettoPrenotazione addetto alla prenotazione
     * @param magazziniere magazziniere
     */
    public Ristorante(int n_posti, int lavoro_persona, AddettoPrenotazione addettoPrenotazione, Magazziniere magazziniere) {
        this.n_posti = n_posti;
        this.lavoro_persona = lavoro_persona;
        this.addettoPrenotazione = addettoPrenotazione;
        this.magazziniere = magazziniere;
    }

    //getter e setter
    public int getN_posti() {
        return n_posti;
    }

    public void setN_posti(int n_posti) {
        this.n_posti = n_posti;
    }

    public int getLavoro_persona() {
        return lavoro_persona;
    }

    public void setLavoro_persona(int lavoro_persona) {
        this.lavoro_persona = lavoro_persona;
    }

    public AddettoPrenotazione getAddettoPrenotazione() {
        return addettoPrenotazione;
    }

    public Magazziniere getMagazziniere() {
        return magazziniere;
    }
}
