package ProgettoSE.Model.Attori.AddettoPrenotazione;

import ProgettoSE.Model.Alimentari.Alimento;
import ProgettoSE.Model.Attori.Cliente;
import ProgettoSE.Model.Produzione.Menu.MenuTematico;
import ProgettoSE.Model.Produzione.Piatto;
import ProgettoSE.Model.Produzione.Prenotabile;

import java.time.LocalDate;
import java.util.*;

public class Prenotazione {

    //ATTRIBUTI
    private Cliente nominativo;
    private int n_coperti;
    private LocalDate data;
    private HashMap<Prenotabile, Integer> scelte;
    private HashMap<Alimento,Float> cons_bevande;  //calcolo del cons_procapite delle bevande * n_coperti
    private HashMap<Alimento,Float> cons_extra; //calcolo del cons_procapite degli extra * n_coperti

    //METODI

    /**
     * <h2>Metodo costruttore della classe Prenotazione</h2>
     * @param nominativo nome del cliente che ha effettuato la prenotazione
     * @param n_coperti numero di coperti della prenotazione
     * @param data data della prenotazione
     * @param scelte HashMap contenente i piatti e i menu prenotati con le relative quantità prenotate
     * @param cons_bevande HashMap contenente le bevande prenotate con le relative quantità
     * @param cons_extra HashMap contenente gli extra prenotati con le relative quantità
     */
    public Prenotazione(Cliente nominativo, int n_coperti, LocalDate data, HashMap<Prenotabile, Integer> scelte, HashMap<Alimento, Float> cons_bevande, HashMap<Alimento, Float> cons_extra) {
        this.nominativo = nominativo;
        this.n_coperti = n_coperti;
        this.data = data;
        this.scelte = scelte;
        this.cons_bevande = cons_bevande;
        this.cons_extra = cons_extra;
    }

    /**
     * <h2>Metodo costruttore della classe Prenotazione</h2>
     * meotodo costruttore fittizio perchè serve in AddettoPrenotazioni, nel metodo calcolaLavoro()
     * @param scelte HashMap contenente i piatti e i menu prenotati con le relative quantità prenotate
     * @param cons_bevande HashMap contenente le bevande prenotate con le relative quantità
     * @param cons_extra HashMap contenente gli extra prenotati con le relative quantità
     */
    public Prenotazione(HashMap<Prenotabile, Integer> scelte, HashMap<Alimento, Float> cons_bevande, HashMap<Alimento, Float> cons_extra){
        this.nominativo = null;
        this.n_coperti = 0;
        this.data = null;
        this.scelte = scelte;
        this.cons_bevande = cons_bevande;
        this.cons_extra = cons_extra;

    }

    //getter e setter
    public int getN_coperti() {
        return n_coperti;
    }

    public LocalDate getData() {
        return data;
    }

    public HashMap<Prenotabile, Integer> getScelte() {
        return scelte;
    }

    public HashMap<Alimento, Float> getCons_bevande() {
        return cons_bevande;
    }

    public HashMap<Alimento, Float> getCons_extra() {
        return cons_extra;
    }

    /**
     * <h2>Metodo che calcola il lavoro totale della prenotazione</h2>
     * <b>Postcondizione:</b> il lavoro totale della prenotazione è stato calcolato
     * @return il lavoro totale della prenotazione
     */
    public float getLavoro_prenotazione(){
        float lavoro_tot = 0;
        //devo avere i prenotabili per poter risalire ai loro lavori
        Set<Prenotabile> prenotabili_presenti = scelte.keySet();
        //scorro i prenotabili e sommo i loro lavori moltiplicati per il  numero di presone che hanno scelto quel prenotabile
        for (Prenotabile prenotabile : prenotabili_presenti){
            if(prenotabile instanceof MenuTematico)
                //se è un menù userò il lavoro del menù
                lavoro_tot += ((MenuTematico) prenotabile).getLavoro_menu() * scelte.get(prenotabile);
            else if(prenotabile instanceof Piatto){
                //se è un piatto devo risalire alla ricetta ed usare il lavoro della ricetta in relazione al numero delle porzioni che genera
                /*calcolo il resto della divisione:

                    numero di porzioni del piatto
                    ------------------------------
                    numero di porzioni che la ricetta crea

                    se il resto è diverso da zero allora prendo il valore della divisione (intero) aumentato di uno e moltiplico per il lavoro della ricetta
                 */
                float resto_divisione = scelte.get(prenotabile)%((Piatto) prenotabile).getRicetta().getN_porzioni();
                if(resto_divisione == 0){
                    //se il resto è zero allora è easy
                    lavoro_tot += scelte.get(prenotabile) * ((Piatto) prenotabile).getLavoro_piatto();
                }else{
                    //se il numero dei piatti è diverso non è un multiplo del numero delle porzioni della corrispettiva ricetta allora arrondo per eccesso la divisione e poi faccio la moltiplicazione
                    int valore_divisione = (int) Math.ceil(scelte.get(prenotabile)/((Piatto) prenotabile).getRicetta().getN_porzioni());
                    lavoro_tot += valore_divisione * ((Piatto) prenotabile).getRicetta().getN_porzioni() * ((Piatto) prenotabile).getLavoro_piatto();
                }
            }
        }
        //postcondizione: il lavoro totale della prenotazione è stato calcolato
        assert lavoro_tot >= 0 : "Il lavoro totale della prenotazione non può essere negativo";
        return lavoro_tot;
    }

}
