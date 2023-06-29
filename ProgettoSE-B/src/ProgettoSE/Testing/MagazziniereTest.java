package ProgettoSE.Testing;

import ProgettoSE.Model.Alimentari.Alimento;
import ProgettoSE.Model.Alimentari.Bevanda;
import ProgettoSE.Model.Alimentari.Extra;
import ProgettoSE.Model.Alimentari.Ingrediente;
import ProgettoSE.Model.Attori.AddettoPrenotazione.Prenotazione;
import ProgettoSE.Model.Attori.Magazziniere.Magazziniere;
import ProgettoSE.Model.Attori.Magazziniere.Magazzino;
import ProgettoSE.Model.Produzione.Piatto;
import ProgettoSE.Model.Produzione.Prenotabile;
import ProgettoSE.Model.Produzione.Ricetta;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class MagazziniereTest {

    @Test
    public void CalcolaConsumoBevandeTest() {
        Bevanda bevanda_test = new Bevanda("Sprite", 10, "l", 1);
        ArrayList<Alimento> bevande = new ArrayList<>();
        bevande.add(bevanda_test);

        Magazzino magazzino = new Magazzino(bevande, null, null);
        Magazziniere magazziniere = new  Magazziniere("Test1", magazzino, null);


        HashMap<Alimento,Float> consumi_bevande = new HashMap<>();
        consumi_bevande.put(bevanda_test, Float.valueOf(1));

        assert magazziniere.calcolaConsumoBevande(1).equals(consumi_bevande);

    }

    @Test
    public void aggiungiSpesaInMagazzinoTest(){
        ArrayList<Alimento> lista_spesa = new ArrayList<>();
        ArrayList<Alimento> ingredienti = new ArrayList<>();
        ingredienti.add(new Ingrediente("Pomodoro", 0, "kg"));
        lista_spesa.add(new Ingrediente("Pomodoro", 10, "kg"));

        Magazzino magazzino = new Magazzino(null, null, ingredienti);
        Magazziniere magazziniere = new  Magazziniere("Test1", magazzino, lista_spesa);

        magazziniere.aggiungiSpesaInMagazzino();

        assert ingredienti.get(0).getQta() == 10 && lista_spesa.isEmpty();
    }
    
    @Test
    public void creaListaSpesaTest(){
        Alimento ingrediente_test = new Ingrediente("Pomodoro", 1, "kg");
        Alimento bevanda_test = new Bevanda("Sprite", 1, "l", 1);
        Alimento extra_test = new Extra("Sale", 1, "kg", 1);

        ArrayList<Alimento> ingredienti_test = new ArrayList<>();
        ArrayList<Alimento> bevande_test = new ArrayList<>();
        ArrayList<Alimento> extras_test = new ArrayList<>();

        ingredienti_test.add(ingrediente_test);
        bevande_test.add(bevanda_test);
        extras_test.add(extra_test);

        Magazzino magazzino_test = new Magazzino(bevande_test, extras_test, ingredienti_test);

        Magazziniere magazziniere_test = new  Magazziniere("Test1", magazzino_test, new ArrayList<Alimento>());

        Ricetta ricetta_test = new Ricetta(ingredienti_test, 2, 1);

        Piatto piatto_test = new Piatto("Pasta al pomodoro", null, 1, ricetta_test);

        HashMap<Alimento, Float> scelteBevanda_test = new HashMap<>();
        scelteBevanda_test.put(bevanda_test, 1F);

        HashMap<Alimento, Float> sceleteExtra_test = new HashMap<>();
        sceleteExtra_test.put(extra_test, 1F);

        HashMap<Prenotabile, Integer> sceltePrenotabile_test = new HashMap<>();
        sceltePrenotabile_test.put(piatto_test, 3);

        Prenotazione prenotazione_test = new Prenotazione(sceltePrenotabile_test, scelteBevanda_test, sceleteExtra_test);

        magazziniere_test.creaListaSpesa(prenotazione_test);

        magazziniere_test.getLista_spesa().forEach((alimento -> System.out.println(alimento.getNome() + " " + alimento.getQta())));
    }


}






