/* Laurent Perron : 1052137
 * Sacha Morin    : 1045304
 */

public class Mort extends Event {
    public Mort(Sims s) {
        super(s);
        this.time = s.getDeath();
    }

    public void execute(Heap<Sims> simsQ, Heap<Event> eventQ, double currentTime) {
        Sims s = (Sims) simsQ.deleteMin();

        if (s.getPartner() != null)
            s.getPartner().setPartner(null);
    }
}
