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
     * @param end   is end point of range
     * @return
     */

    public static Iterable<Task> incoming(Iterable<Task> tasks, Date start, Date end) {
        Iterable<Task> incomList = new HashSet<>();
        Iterator<Task> itr = tasks.iterator();
        while (itr.hasNext()) {
            Task task = itr.next();
            if (task.nextTimeAfter(start) != null && end.compareTo(task.nextTimeAfter(start)) >= 0)
                ((HashSet<Task>)incomList).add(task);
        } return incomList;
    }

    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, Date start, Date end) {
        SortedMap<Date, Set<Task>> sortMap = new TreeMap<>();
        Iterable<Task> incomList = incoming(tasks, start, end);
        Iterator<Task> itr = incomList.iterator();
        while (itr.hasNext()){
            Task task = itr.next();
            Date date = task.nextTimeAfter(start);
            while(date != null && date.compareTo(end) <= 0) {
                if (sortMap.containsKey(date)) {
                    sortMap.get(date).add(task);
                } else {
                    Set<Task> temp = new HashSet<>();
                    temp.add(task);
                    sortMap.put(date, temp);
                } date = task.nextTimeAfter(date);
            }
        } return sortMap;
    }
}