/* Laurent Perron : 1052137
 * Sacha Morin    : 1045304
 */

import java.util.*;

public class BirthComparator implements Comparator<Sims>{
    public int compare(Sims s1, Sims s2) {
        double birth1 = s1.getNaissance();
        double birth2 = s2.getNaissance();

        if (birth1 > birth2)
            return -1;

        else if (birth1 < birth2)
            return 1;

        else
            return 0;
    }
}
