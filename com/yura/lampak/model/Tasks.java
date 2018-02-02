package com.yura.lampak.model;

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
    public static Iterable<Task> incoming(Iterable<Task> tasks, Date start, Date end) throws TaskException {
        Iterable<Task> data;
        try {
            data = tasks.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new TaskException("Exception in static method incoming(): ", e);
        }
        Iterator<Task> itr = tasks.iterator();
        Task task;
        while (itr.hasNext()) {
            task = itr.next();
            if (task.nextTimeAfter(start) != null && end.compareTo(task.nextTimeAfter(start)) >= 0)
                ((TaskList)data).add(task);
        } return data;
    }

    /**
     * Build a calendar of tasks for a given period from <tt>start</tt> to <tt>end</tt>. Represents table where
     * each date corresponds to the set the tasks to be performed at that time, at which one task can meet
     * in accordance with several dates if it has to be executed several times during the specified period.
     *
     * @param tasks is base list of tasks to storage all tasks
     * @param start is a start point of calendar period
     * @param end is end point of calendar period
     * @return table with tasks which runs on preset time
     */

    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, Date start, Date end) throws TaskException {
        SortedMap<Date, Set<Task>> sortMap = new TreeMap<>();
        Iterable<Task> data = incoming(tasks, start, end);
        Iterator<Task> itr = data.iterator();
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