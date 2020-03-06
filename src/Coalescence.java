/* Laurent Perron : 1052137
 * Sacha Morin    : 1045304
 */

import java.util.*;
import java.text.*;

//La classe coalescence permet de calculer la coalescence pour les hommes
//ou pour les femmes à partir d'une simulation terminée
public class Coalescence {
    private boolean GENDER; //true pour femme ou false pour homme
    private LinkedList<String[]> points; //Liste des résultats
    private Heap<Sims> ancestors; //Trouver facilement le plus jeune ancêtre
    private HashSet<Sims> PA; //Trouver facilement si un parent est déjà là

    public Coalescence(Heap pop, boolean gender) {
        this.GENDER = gender;
        this.points = new LinkedList<String[]>();
        this.ancestors = new Heap<Sims>(new BirthComparator());
        this.PA = new HashSet<Sims>();
        Object[] popArray = pop.getArray();

        // Initialiser le heap de la population courante du bon sexe
        // en fonction de la date de naissance (le min est le plus jeune)
        // Voir BirthComparator
        // On bâtit aussi un set pour repérer les ancêtres facilement
        for(int i = 1; i <= pop.getLast(); i++) {
            if(((Sims) popArray[i]).getSexe() == this.GENDER) {
                this.ancestors.insert((Sims) popArray[i]);
                this.PA.add((Sims) popArray[i]);
            }
        }

        //Le heap sera vide si la population est morte
        //On teste donc pour s'assurer qu'il reste des vivants
        if(!this.ancestors.isEmpty())
            this.compute();
    }

    //Méthode permettant de calculer la coalescence
    //Les points sont enregistrés dans une liste chaînée
    private void compute() {
        while(true) {
            //Plus jeune sujet et suppression dans les structures existantes
            Sims subject = (Sims) this.ancestors.deleteMin();
            this.PA.remove(subject);

            //Chercher le parent
            Sims parent = this.getParent(subject);

            //Si le plus jeune sujet est un fondateur, il ne reste que des
            //fondateurs donc on enregistre le nombre de lignées au temps 0
            //et on break
            if(parent == null) {
                this.savePoint(0.0);
                break;
            }

            if(this.PA.contains(parent)) {
                //Point de coalescence. On enregistre un point
                this.savePoint((double) subject.getNaissance());
            } else {
                //Parent pas présent: on l'ajoute aux structures existantes
                this.ancestors.insert(parent);
                this.PA.add(parent);
            }

            //Un seul parent restant
            if(this.PA.size() == 1) {
                Sims last = (Sims) this.ancestors.deleteMin();
                this.savePoint((double) last.getNaissance());
                break;
            }
        }
    }

    //Méthode permettant d'enregistrer un point
    //On utilise addFirst pour avoir des résultats en ordre chronologique
    //après la coalescence
    private void savePoint(Double t) {
        NumberFormat formatter = new DecimalFormat("#0.0000");
        String[] p = {formatter.format(t), Integer.toString(this.PA.size())};
        this.points.addFirst(p);
    }

    //Retourne le bon parent selon le sexe assigné à la coalescence
    private Sims getParent(Sims s) {
        Sims parent;
        if(this.GENDER) {
            parent = s.getMere();
        } else {
            parent = s.getPere();
        }

        return parent;
    }

    //Retourne la liste de résultats
    public LinkedList<String[]> getResults() {
        return this.points;
    }
}
