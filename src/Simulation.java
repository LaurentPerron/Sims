/* Laurent Perron : 1052137
 * Sacha Morin    : 1045304
 */

import java.util.*;
import java.text.*;

//Classe principale pour gérer la simulation
public class Simulation {
    //Détermine le format du Standard.out
    //Si true, output des colonnes lisibles directement comme csv
    //Si false, output les séries une après l'autre en "mode console"
    private final boolean CSV = true;

    //Liste pour enregistrer les points
    private LinkedList<String[]> points;

    //Deux files de priorité
    private Heap<Event> eventQ;
    private Heap<Sims> simsQ;

    private double currentYear = 0;

    public Simulation() {
        points = new LinkedList<String[]>();
        eventQ = new Heap<Event>(new EventComparator());
        simsQ = new Heap<Sims>(new DeathComparator());
    }

    // Methode centrale qui s'occupe d'instancier la simulation
    // en créant la population fondatrice.
    // Roule ensuite la simulaton en lisant les événements
    public void simulate(int n, double Tmax) {
        //Initialisation de la population fondatrice
        for (int i = 0; i < n; i++) {
            Sims fondateur = new Sims();
            Naissance E = new Naissance(fondateur);
            eventQ.insert(E);
        }

        // On simule tant qu'il reste des événements et du temps
        while (!eventQ.isEmpty() && currentYear <= Tmax) {
            Event nextEvent = (Event) eventQ.getMin();

            // Tant qu'on trouve des événements qui se sont produits cette année
            // on les enlève de la file
            while (!eventQ.isEmpty() && nextEvent.getTime() <= currentYear) {
                Event e = eventQ.deleteMin();

                //Exécution de l'événement et mise à jour des structures
                e.execute(simsQ, eventQ, currentYear);

                //Lecture du prochain
                nextEvent = eventQ.getMin();
            }

            // On enfile un point à tous les cents ans
            if (currentYear % 100 == 0) {
                savePoint((double) currentYear);
            }

            currentYear++;
        }

        //Mémoriser le dernier point si la population est morte avant la fin de
        //la simulation
        if(currentYear <= Tmax) {
            savePoint(currentYear);
        }

        //Coalescence
        //Hommes
        Coalescence aieux = new Coalescence(simsQ, false);
        LinkedList<String[]> resultatAieux = aieux.getResults();

        //Femmes
        Coalescence aieules = new Coalescence(simsQ, true);
        LinkedList<String[]> resultatAieules = aieules.getResults();

        //Impression des résultats
        if(CSV) {
            //Headers
            System.out.println("Année, Population, Aieux, Aieules");
            for(String[] pair: points) {
                System.out.println(pair[0] + "," + pair[1] + ",,");
            }

            for(String[] pair: resultatAieux) {
                System.out.println(pair[0] + ",," + pair[1] + ",");
            }

            for(String[] pair: resultatAieules) {
                System.out.println(pair[0] + ",,," + pair[1]);
            }
        } else {

            //Mode console
            System.out.println("Résultats de la simulation");
            System.out.println("#############################################");
            System.out.println("Première série: Évolution de la population");
            for(String[] pair: points) {
                System.out.println("Année: " + pair[0]
                    + ", Population: " + pair[1]);
            }
            System.out.println("#############################################");
            System.out.println("Deuxième série: Coalescence chez les hommes");
            for(String[] pair: resultatAieux) {
                System.out.println("Année: " + pair[0]
                    + ", Lignées: " + pair[1]);
            }
            System.out.println("#############################################");
            System.out.println("Troisième série: Coalescence chez les femmes");
            for(String[] pair: resultatAieules) {
                System.out.println("Année: " + pair[0]
                    + ", Lignées: " + pair[1]);
            }
        }
    }

    //Méthode permettant d'enregistrer un point
    private void savePoint(Double t) {
        NumberFormat formatter = new DecimalFormat("#0.0000");
        String[] p = {formatter.format(t), Integer.toString(simsQ.getLast())};
        this.points.add(p);
    }


    public static void main(String[] args) {
        int p1 = Integer.parseInt(args[0]);
        double p2 = Double.parseDouble(args[1]);

        Simulation simulation = new Simulation();

        simulation.simulate(p1, p2);
    }
}
