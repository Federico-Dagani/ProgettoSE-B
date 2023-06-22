package ProgettoSE.Model.Produzione;

import ProgettoSE.Model.Alimentari.Alimento;

import java.time.LocalDate;
import java.util.ArrayList;

public class Piatto implements Prenotabile {
    //ATTRIBUTI
    private String nome;
    private ArrayList<LocalDate> disponibilità;
    private float lavoro_piatto; //coincide per definizione con lavoro_porzione
    private Ricetta ricetta;

    //METODI
    /**
     * <h2>Metodo costruttore che inizializza un piatto con i dati letti dal file XML</h2>
     * @param nome nome del piatto
     * @param disponibilità lista di date di disponibilità del piatto
     * @param lavoro_piatto lavoro per preparare il piatto
     * @param ricetta ricetta del piatto
     */
    public Piatto(String nome, ArrayList<LocalDate> disponibilità, float lavoro_piatto, Ricetta ricetta) {
        this.nome = nome;
        this.disponibilità = disponibilità;
        this.lavoro_piatto = lavoro_piatto;
        this.ricetta = ricetta;
    }

    public Piatto(){
        this.nome = "";
        this.disponibilità = new ArrayList<>();
        this.lavoro_piatto = 0;
        this.ricetta = new Ricetta();

    };

    /**
     * <h2>Metodo che restituisce il nome del piatto</h2>
     * @return String nome del piatto
     */
    @Override
    public String getNome() {
        return nome;
    }

    /**
     * <h2>Metodo che setta il nome del piatto</h2>
     * @param nome nome del piatto
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * <h2>Metodo che restituisce la disponibilità del piatto</h2>
     * @return ArrayList<LocalDate> disponibilità del piatto
     */
    public ArrayList<LocalDate> getDisponibilità() {
        return disponibilità;
    }

    public void setDisponibilita(ArrayList<LocalDate> disponibilità) {
        this.disponibilità = disponibilità;
    }

    /**
     * <h2>Metodo che setta la disponibilità del piatto</h2>
     * @param disponibilità disponibilità del piatto
     */
    public void setDisponibilità(ArrayList<LocalDate> disponibilità) {
        this.disponibilità = disponibilità;
    }

    /**
     * <h2>Metodo che restituisce il lavoro per preparare il piatto</h2>
     * @return float lavoro per preparare il piatto
     */
    public float getLavoro_piatto() {
        return lavoro_piatto;
    }

    /**
     * <h2>Metodo che setta il lavoro per preparare il piatto</h2>
     * @param lavoro_piatto lavoro per preparare il piatto
     */
    public void setLavoro(float lavoro_piatto) {
        this.lavoro_piatto = lavoro_piatto;
    }

    /**
     * <h2>Metodo che restituisce la ricetta del piatto</h2>
     * @return Ricetta ricetta del piatto
     */
    public Ricetta getRicetta() {
        return ricetta;
    }

    /**
     * <h2>Metodo che setta la ricetta del piatto</h2>
     * @param ricetta ricetta del piatto
     */
    public void setRicetta(Ricetta ricetta) {
        this.ricetta = ricetta;
    }

    /**
     * <h2>Metodo che aggiunge una data di disponibilità al piatto</h2>
     * @param disponibilita data di disponibilità da aggiungere
     */
    public void aggiungiDisponibilita(ArrayList<LocalDate> disponibilita){
        for (LocalDate data : disponibilita)
            disponibilità.add(data);
    }

    public ArrayList<String> mostraPrenotabile(){
        ArrayList<String> lista = new ArrayList<>();
        lista.add(this.nome);
        for (Alimento ingrediente : ricetta.getIngredienti())
            lista.add(ingrediente.getNome());
        return lista;
    }
}
