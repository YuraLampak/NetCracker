package ua.edu.sumdu.j2se.YuraLampak.tasks;

import java.util.*;

/**
 * Class Tasks
 * for
 * programm <b>Task manager</b>
 *
 * @author YuraLampak
 * @version 1.0
 */

public class Tasks {

    /**
     * Returns subset of tasks which includes in range. Range is limited
     * by <tt>from</tt> and <tt>end</tt>.
     *
     * @param tasks is list of tasks, in which we carry out a selection of tasks
     * @param start is start point of range
     * @param end is end point of range
     * @return
     */
    public static Iterable<Task> incoming (Iterable<Task> tasks, Date start, Date end) {
        Set<Task> incomList = new HashSet<>();
        Iterator<Task> tsk = tasks.iterator();
        Task t;
        while (tsk.hasNext()) {
            t = tsk.next();
            if(t.nextTimeAfter(start) != null && end.compareTo(t.nextTimeAfter(start)) >= 0)
                incomList.add(t);
            } return incomList;
    }
}
