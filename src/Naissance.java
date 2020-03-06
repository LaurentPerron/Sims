/* Laurent Perron : 1052137
 * Sacha Morin    : 1045304
 */

public class Naissance extends Event{
    public Naissance(Sims s) {
        //Planification de la naissance
        super(s);
        this.time = s.getNaissance();
    }

    //Lors de la naissance, on planifie la mort et, si on a une femme
    //on planifie aussi la naissance
    public void execute(Heap<Sims> simsQ, Heap<Event> eventQ, double currentTime) {
        Sims S = this.subject;
        Mort deathE = new Mort(S);
        eventQ.insert(deathE);

        if (S.getSexe()) {
            //Si c'est une femme
            Reproduction ReproductionE = new Reproduction(S, currentTime);
            eventQ.insert(ReproductionE);
        }

        simsQ.insert(S);
    }
}
