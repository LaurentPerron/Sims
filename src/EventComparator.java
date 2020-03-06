/* Laurent Perron : 1052137
 * Sacha Morin    : 1045304
 */

import java.util.*;

public class EventComparator implements Comparator<Event>{
    public int compare(Event e1, Event e2) {
        double time1 = e1.getTime();
        double time2 = e2.getTime();

        if (time1 < time2)
            return -1;

        else if (time1 > time2)
            return 1;

        else
            return 0;
    }
}
