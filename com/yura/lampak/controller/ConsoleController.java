package com.yura.lampak.controller;

import java.io.File;
import java.io.IOException;
import java.util.*;
import com.yura.lampak.model.*;
import com.yura.lampak.view.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Implementation of the <tt>Controller</tt> interface.
 * Provides method to create/change/remove tasks,
 * shows list of tasks, creates calendar for specific period.
 *
 * @see Controller;
 *
 * @author Yura Lampak
 * @version 1.0
 */

public class ConsoleController implements Controller {

    /**
     * instance of class ConsoleView to have connection with view
     */
    static ConsoleView theView;

    /**
     * instance of class Model to have connection with model
     */
    static Model theModel;

    /**
     * Second thread for checking tasks time and calls notification
     */
    static NotifyThread notifyThread;

    /**
     * Storage of tasks in file system
     */
    private static final File file = new File("TaskList.txt").getAbsoluteFile();

    /**
     * Connect logging for tracking some actions
     */
    private static final Logger logger = LogManager.getLogger(ConsoleController.class);


    /**
     * Constructor get instances of Model and ConsoleView, creating file to read/write tasks, if it don`t exist.
     * Read list of tasks from file and start another thread <tt>notifyThread</tt> to make notification
     * if task must be performed.
     *
     * @param theModel is instance of class Model
     * @param theView is instance of class ConsoleView
     * @throws TaskException if the list of tasks isn't read from file
     */
    public ConsoleController(Model theModel, ConsoleView theView) throws TaskException {
        ConsoleController.theModel = theModel;
        ConsoleController.theView = theView;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("creating file is failed", e);
            }
        } theModel.setTaskList(readListFromFile());
        notifyThread = new NotifyThread();
        notifyThread.setDaemon(true);
        notifyThread.start();
    }

    /**
     * Method for read list of tasks from file.
     *
     * @return list of tasks. If file is empty, return new list.
     * @throws TaskException if the list of tasks isn't read from file.
     */
    static TaskList readListFromFile() throws TaskException {
        TaskList tempList = new LinkedTaskList();
        try {
            TaskIO.readText(tempList, file);
            return checkForFinishedTasks(tempList);
        } catch (NumberFormatException|StringIndexOutOfBoundsException e){
            return new LinkedTaskList();
        }
    }

    /**
     * This method check all tasks for overdue. Compares time of each task with
     * <tt>current time</tt> and if it is overdue, deactivate task.
     *
     * @param tempList is list of tasks for check
     * @return checked <tt>tempList</tt>
     */
    private static TaskList checkForFinishedTasks(TaskList tempList) {
        Iterator<Task> itr = tempList.iterator();
        Date currentDate = new Date();
        Task task;
        while (itr.hasNext()){
            task = itr.next();
            if (task.isRepeated()){
                if (task.getEndTime().compareTo(currentDate) < 0){
                    task.setActive(false);
                }
            } else if (task.getTime().compareTo(currentDate) < 0){
                task.setActive(false);
            }
        } return tempList;
    }

    /**
     * Method to save current list in file system.
     */
    static void writeListToFile() {
        try {
            TaskIO.writeText(theModel.getTaskList(), file);
        } catch (Exception e){
            logger.error("writing file is failed", e);
        }
    }

    /**
     * This method responsible for creating/changing/removing task,
     * give information about existing tasks, and
     * making calendar of tasks for a given period of time.
     */
    @Override
    public void execute() throws TaskException {
        theView.printMenu();
        switch (FunctionController.getUserInput()) {
            case "0":
                shutDown();
                break;
            case "1":
                createTask();
                execute();
                break;
            case "2":
                changeTask();
                execute();
                break;
            case "3":
                removeTask();
                execute();
                break;
            case "4":
                printTaskList();
                execute();
                break;
            case "5":
                createCalendar();
                execute();
                break;
            case "":
                execute();
                break;
            default:
                theView.incorrectItem();
                execute();
                break;
        }
    }

    @Override
    public void createTask() throws TaskException {
        FunctionController.execute("1");
    }

    @Override
    public void changeTask() throws TaskException {
        FunctionController.execute("2");
    }

    @Override
    public void removeTask() throws TaskException {
        FunctionController.execute("3");
    }

    @Override
    public void printTaskList() throws TaskException {
        FunctionController.execute("4");
    }

    @Override
    public void createCalendar() throws TaskException {
        FunctionController.execute("5");
    }

    /**
     * Writes current list of tasks in file and interrupt second thread.
     */
    private void shutDown(){
        writeListToFile();
        notifyThread.interrupt();
    }
}