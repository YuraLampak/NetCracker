package com.yura.lampak.view;

import com.yura.lampak.model.Task;
import com.yura.lampak.model.TaskList;
import java.util.*;

/**
 * Interface <tt>View</tt> for connection with <tt>Controller</tt>
 * @see ConsoleView
 *
 * @author Yura Lampak
 * @version 1.0
 *
 */
public interface View {
    /**
     * Displays the start menu to give the user a choice of what to do
     */
    void printMenu();

    /**
     * Displays the task the user is working with
     * @param task is current task, which displays
     */
    void printTask(Task task);

    /**
     * Displays the list of tasks and his size.
     * @param taskList is current list of tasks the user is working with
     * @param size is current size of list of tasks
     */
    void printTaskList(TaskList taskList, int size);

    /**
     * Displays calendar for specific period
     * @param calendar is calendar for specific period
     */
    void printCalendar(SortedMap<Date, Set<Task>> calendar);
}
