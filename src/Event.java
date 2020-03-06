/* Laurent Perron : 1052137
 * Sacha Morin    : 1045304
 */

// Objet général pour les événements
// Voir les sous-classes Mort, Naissance et Reproduction
public abstract class Event {
    protected Sims subject;
    protected double time;

    public Event(Sims s) {
        this.subject = s;
    }

    public abstract void execute(Heap<Sims> simsQ, Heap<Event> eventQ, double currentTime);

    public Sims getSubject() {
        return subject;
    }

    public double getTime() {
        return time;
    }
}
