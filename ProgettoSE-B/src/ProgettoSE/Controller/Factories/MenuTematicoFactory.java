package ProgettoSE.Controller.Factories;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ProgettoSE.Controller.PrenotabileFactory;
import ProgettoSE.Model.Attori.Gestore.Ristorante;
import ProgettoSE.Model.Attori.Tempo;
import ProgettoSE.Model.Produzione.Menu.Menu;
import ProgettoSE.Model.Produzione.Menu.MenuTematico;
import ProgettoSE.Model.Produzione.Piatto;
import ProgettoSE.Model.Produzione.Prenotabile;
import ProgettoSE.Utility.Costanti;
import ProgettoSE.View.View;

import java.time.LocalDate;
import java.util.ArrayList;

public class MenuTematicoFactory extends PrenotabileFactory {

    @Override
    protected void datiSpecificiPrenotabile(View view, Ristorante ristorante, Prenotabile menu_tematico) {


        ArrayList<Piatto> piatti = new ArrayList<>();
        ArrayList<Prenotabile> menu_ristorante = ristorante.getAddettoPrenotazione().getMenu();


        do {
            //prima mostro i piatti presenti nel ristorante
            view.mostraPiatti(menu_ristorante);

            String nome_piatto = view.leggiStringaConSpazio(Costanti.INS_NOME);
            //flag per salvarmi se il piatto è stato trovato o meno
            boolean trovato = false;
            for (Prenotabile piatto : menu_ristorante) {
                //controllo che il piatto sia un piatto, che il nome sia uguale a quello inserito e che non sia già stato inserito nell'ArrayList piatti (ovvero quei piatti che saranno inseriti nel menù, solo successivamente)
                if (piatto instanceof Piatto && piatto.getNome().equalsIgnoreCase(nome_piatto) && !piatti.contains(piatto)) {
                    piatti.add((Piatto) piatto);
                    trovato = true;
                    break;
                }
            }
            view.ripulisciConsole();

            if (!trovato) view.stampaTesto("Piatto non trovato o già inserito nel menu");
            else view.stampaTesto("Piatto aggiunto al menu");

            view.premerePerContinuare();
            //tramite il cortocircuito evito di chiedere se si vuole aggiungere un altro piatto se il piatto non è stato trovato
        } while (piatti.size() < Costanti.MINIMO_PIATTI_PER_MENU || view.yesOrNo("Vuoi aggiungere un altro piatto al menu?"));

        if(menu_tematico instanceof MenuTematico){
            ((MenuTematico) menu_tematico).setPiatti(piatti);
        }
    }
}
