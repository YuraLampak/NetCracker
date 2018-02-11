package com.yura.lampak.controller;

import com.yura.lampak.model.Task;
import com.yura.lampak.model.TaskException;
import com.yura.lampak.model.TaskList;
import com.yura.lampak.model.Tasks;
import com.yura.lampak.view.ConsoleView;


import java.util.*;

/**
 * The second thread <tt>NotifyThread</tt> need to
 * output message about doing some task. Controlled by
 * <tt>ConsoleController</tt>
 *
 * @see ConsoleController
 */
public class NotifyThread extends Thread {

    /**
     * Instance of class ConsoleController
     */
    private ConsoleController theController;
    /**
     * Instance of class ConsoleView
     */
    private ConsoleView theView;

    /**
     * A map for storing selected tasks that must be performed at the moment
     */
    private SortedMap<Date, Set<Task>> notifyMap;

    /**
     * Constructor gets instance of controller and view. Also, it
     * initializes a <tt>notifyMap</tt> for storage tasks.
     *
     * @param theController is instance of <tt>ConsoleController</tt>
     * @param theView is instance of <tt>ConsoleView</tt>
     */
    NotifyThread(ConsoleController theController, ConsoleView theView) {
        super("notifyThread");
        this.theController = theController;
        this.theView = theView;
        notifyMap = new TreeMap<>();
    }

    /**
     * Method selecting tasks, which have to be performed. It's a create calendar from
     * one minute before <tt>current time</tt> to one minute after <tt>current time</tt>.
     * Then it compares time of each task with <tt>current time</tt> and if it equals,
     * it's output message about this task and waiting user actions to go on.
     */
    @Override
    public void run() {
        TaskList taskList;
        Date currentTime, startTime = new Date();
        Date endTime = new Date();
        int balanceTime = 1000*60;
        while (true) {
            if (this.isInterrupted()) {
                break;
            }
            try {
                taskList = theController.readListFromFile();
                currentTime = new Date();
                startTime.setTime(currentTime.getTime() - balanceTime);
                endTime.setTime(currentTime.getTime() + balanceTime);
                notifyMap = Tasks.calendar(taskList, startTime, endTime);
                for (Map.Entry<Date, Set<Task>> entry : notifyMap.entrySet()) {
                    if (entry.getKey().compareTo(currentTime) == 0){
                        notifyUser(entry.getValue());
                        waitAction();
                    }
                }
            } catch (TaskException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Notifies the user by displaying a message with
     * a set of tasks that need to be performed.
     *
     * @param tasks is set of tasks which have to perform
     * @throws TaskException if removes <tt>non-repeated</tt> task is failed
     */
    private void notifyUser(Set<Task> tasks) throws TaskException {
        Task temp;
        theView.printTasksToGo(tasks);
        Iterator<Task> itr = tasks.iterator();
        while (itr.hasNext()){
            temp = itr.next();
            if (!temp.isRepeated()){
                try {
                    theController.removeTask(temp);
                } catch (TaskException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Method puts the thread into the standby mode. It's waiting, when user
     * enter something and then lock will be broken.
     */
    private void waitAction() {
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}
