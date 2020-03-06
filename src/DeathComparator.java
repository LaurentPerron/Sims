/* Laurent Perron : 1052137
 * Sacha Morin    : 1045304
 */

import java.util.*;

public class DeathComparator implements Comparator<Sims>{
    public int compare(Sims s1, Sims s2) {
        double mort1 = s1.getDeath();
        double mort2 = s2.getDeath();

        if (mort1 < mort2)
            return -1;

        else if (mort1 > mort2)
            return 1;

        else
            return 0;
    }
}
