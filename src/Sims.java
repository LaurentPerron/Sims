/* Laurent Perron : 1052137
 * Sacha Morin    : 1045304
 */

import java.util.Random;

//Classe implémentant la description des sims
public class Sims {

    public double MIN_MATING_AGE_F = 16;
    public double MIN_MATING_AGE_M = 16;
    public double MAX_MATING_AGE_F = 50;
    public double MAX_MATING_AGE_M = 73;

    public double fidelity = 0.90;

    private Sims pere;
    private Sims mere;
    private Sims partner = null;
    private double naissance;
    private double mort;
    private boolean sexe; // true pour Femme, false pour Homme
    private AgeModel model = new AgeModel();

    // Fonction pour la creation des premiers Sims, les fondateurs
    public Sims() {

        pere = null;
        mere = null;
        naissance = 0.0;

        mort = model.randomAge(new Random());

        sexe = createSexe();
    }

    public Sims(Sims mere, Sims pere, double time) {

        this.pere = pere;
        this.mere = mere;
        naissance = time; // Temps present de la simulation

        mort = model.randomAge(new Random()) + naissance;

        sexe = createSexe();
    }

    public Sims getPere() {
        return pere;
    }

    public Sims getMere() {
        return mere;
    }

    public Sims getPartner() {
        return partner;
    }

    public double getDeath() {
        return mort;
    }

    public double getNaissance() {
        return naissance;
    }

    public boolean getSexe() {
        return sexe;
    }

    public void setPartner(Sims s) {
        partner = s;
    }

    public double age(double currentTime) {
        return currentTime - naissance;
    }

    // 50% chance to have a boy or girl
    public boolean createSexe() {
        // True pour femme, False pour homme
        Random rand = new Random();

        if (rand.nextFloat() < 0.49)
            return false;

        else
            return true;
    }
}
