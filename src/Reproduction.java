/* Laurent Perron : 1052137
 * Sacha Morin    : 1045304
 */

import java.util.Random;

public class Reproduction extends Event {
    private final double CHILDPERWOMAN = 2.035;

    public Reproduction(Sims s, double time) {
        super(s);
        // Utilisation des methodes de la classe AgeModel du professeur
        AgeModel reproduction = new AgeModel();

        double rate = CHILDPERWOMAN
            / reproduction.expectedParenthoodSpan(subject.MIN_MATING_AGE_F,
                                                  subject.MAX_MATING_AGE_F);

        this.time = reproduction.randomWaitingTime(new Random(), rate)
            + time;
    }

    // Méthode traitant la reproduction
    public void execute(Heap<Sims> simsQ, Heap<Event> eventQ, double time) {
        Sims S = this.subject;
        double age = S.age(time);

        // si le sujet est deja mort on arrete le traitement
        // Inutile de replanifier si la femme est trop vieille
        if (S.getDeath() < this.time || age > S.MAX_MATING_AGE_F)
            return;

        // si elle n'est pas morte on regarde si elle est en age
        // de se reproduire, si oui
        // on choisit un partenaire, sinon on replanifie la reproduction
        else {
            if (age < S.MIN_MATING_AGE_F) {
                // On replanifie la reproduction
                Reproduction newReproduction = new Reproduction(S, time);
                eventQ.insert(newReproduction);
            } else {
                //On vérifie qu'il reste  au moins un homme en âge
                //de se reproduire
                boolean oneMale = false;
                Object[] population = simsQ.getArray();
                for(int i = 1 ; i <= simsQ.getLast(); i++) {
                    Sims h = (Sims) population[i];
                    if(!h.getSexe()
                       && h.age(time) >= h.MIN_MATING_AGE_M
                       && h.age(time) <= h.MAX_MATING_AGE_M) {
                        //On a au moins un mâle pouvant se reproduire
                        oneMale = true;
                        break;
                    }
                }

                if(oneMale) {
                    Sims partner= findPartner(S, simsQ, time);

                    // La naissance de l'enfant se produit au même moment
                    // que la reproduction
                    // Et sera donc traitée tout de suite après.
                    Sims enfant = new Sims(S, partner, this.time);

                    Naissance newBirth = new Naissance(enfant);
                    eventQ.insert(newBirth);

                    Reproduction newRep = new Reproduction(S, time);
                    eventQ.insert(newRep);
                }
            }
        }
    }

    // Méthode pour trouver des partenaires de reproduction aux femmes en âge de
    // procréer
    private Sims findPartner(Sims s, Heap<Sims> simsQ, double time) {
        Sims mate = null;

        if (s.getPartner() == null) {
            // Chercher un Sim celibataire (ou pas) dans le tas
            mate = findPartner(simsQ, time, true);
        } else {

            double fideleOuPas = Math.random();

            if (fideleOuPas < s.fidelity) {
                // Reste avec le meme partenaire
                mate = s.getPartner();
            } else {
                // On remet ces partenaires celibataire
                s.getPartner().setPartner(null);
                s.setPartner(null);

                mate = findPartner(simsQ, time, false);
                s.setPartner(mate);
                mate.setPartner(s);
            }
        }
        return mate;
    }

    private Sims findPartner(Heap<Sims> simsQ, double time, boolean femmeCelib) {
        Sims mate = null;
        Sims s;
        double fideleOuPas;
        Random random = new Random();
        Object[] population = simsQ.getArray();

        // Si apres un tour de population on n'a toujours pas trouvé
        // un partenaire, on recommencera a faire le tour.
        while (true) {
            s = (Sims) population[random.nextInt(simsQ.getLast()) + 1];

            // On cherche un homme en age de se reproduire
            if (!s.getSexe() || s.age(time) >= s.MIN_MATING_AGE_M
                || s.age(time) <= s.MAX_MATING_AGE_M) {

                // l'homme en question est celibataire
                if (s.getPartner() == null) {
                    mate = s;
                    break;
                } else {
                    fideleOuPas = random.nextFloat();

                    // Il y aura reproduction si l'homme est infidèle ou si
                    // la femme était déjà infidèle (donc en couple) Dans
                    // ce dernier cas on ignore la fidelité de l'homme
                    if (fideleOuPas >= s.fidelity || !femmeCelib) {
                        // s'il n'est pas fidele on supprime ses liens
                        // avec son present partenaire et il
                        // devient
                        // le partenaire
                        s.getPartner().setPartner(null);
                        s.setPartner(null);
                        mate = s;
                        break;
                    }
                }
            }
        }
        return mate;
    }
}
