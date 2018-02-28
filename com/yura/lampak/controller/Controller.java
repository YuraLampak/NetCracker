package com.yura.lampak.controller;

import com.yura.lampak.model.*;

/**
 * Interface <tt>Controller</tt> is connecting Model and View.
 * Takes user actions, updates data in Model and uses View to display information.
 *
 * @author Yura Lampak
 * @version 1.0
 */

public interface Controller {
    /**
     * Main method, which running all project
     */
    void execute() throws TaskException;

    /**
     * This method must create <tt>repeating</tt> and <tt>non-repeating</tt> tasks
     * and add them to list of tasks.
     */
    void createTask() throws TaskException;

    /**
     * This method must change existing task and save changes to list of tasks.
     *
     */
    void changeTask() throws TaskException;

    /**
     * This method must remove existing task from list of tasks and
     * save other tasks in the order, in which they were.
     */
    void removeTask() throws TaskException;

    /**
     * This method shows to user information about existing tasks.
     */
    void printTaskList() throws TaskException;

    /**
     * This method must create a calendar of tasks for different periods. For
     * example, create calendar for a one day or a week. Besides that, method
     * have to create calendar for period which user specify.
     *
     */
    void createCalendar() throws TaskException;



}
