package ProgettoSE.Testing;

import ProgettoSE.Model.Alimentari.Alimento;
import ProgettoSE.Model.Alimentari.Bevanda;
import ProgettoSE.Model.Alimentari.Ingrediente;
import ProgettoSE.Model.Attori.Magazziniere.Magazziniere;
import ProgettoSE.Model.Attori.Magazziniere.Magazzino;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MagazziniereTest {

    @Test
    public void testCalcolaConsumoBevande() {
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


}






