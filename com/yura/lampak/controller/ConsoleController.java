package com.yura.lampak.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
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
    private ConsoleView theView;

    /**
     * instance of class Model to have connection with model
     */
    private Model theModel;

    /**
     * Storage of tasks in file system
     */
    private final File file = new File("TaskList.txt").getAbsoluteFile();

    /**
     * Connect logging for tracking some actions
     */
    private final Logger logger = LogManager.getLogger(ConsoleController.class);

    /**
     * Second thread for checking tasks time and calls notification
     */
    private final NotifyThread notifyThread;

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
        this.theModel = theModel;
        this.theView = theView;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("creating file is failed", e);
            }
        } theModel.setTaskList(readListFromFile());
        notifyThread = new NotifyThread(this, theView);
        notifyThread.setDaemon(true);
        notifyThread.start();
    }

    /**
     * Method for read list of tasks from file.
     *
     * @return list of tasks. If file is empty, return new list.
     * @throws TaskException if the list of tasks isn't read from file.
     */
    TaskList readListFromFile() throws TaskException {
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
    private TaskList checkForFinishedTasks(TaskList tempList) {
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
    private void writeListToFile() {
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
        switch (getUserInput()) {
            case "0":
                shutDown();
                break;
            case "1":
                createTaskByType();
                execute();
                break;
            case "2":
                changeExistingTask();
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

    /**
     * Creates <tt>repeating</tt> task or <tt>non-repeating</tt>
     * tasks depending on user choice.
     */
    @Override
    public void createTaskByType() throws TaskException {
        String state;
        boolean conditionVar = true;
        do {
            theView.inputTypeOfTask();
            state = getUserInput();
            switch (state) {
                case "0":
                    conditionVar = false;
                    break;
                case "1":
                    createTask();
                    conditionVar = false;
                    break;
                case "2":
                    createRepeatingTask();
                    conditionVar = false;
                    break;
            }
        } while (conditionVar);
    }

    /**
     * Changes existing task by several parameters: title, time (for each
     * kind of task his own) and state of task. Get number of task, then
     * define type of task by this number and do switch-case to change
     * one of parameters, depending on user choice.
     */
    @Override
    public void changeExistingTask() throws TaskException {
        boolean conditionVar = true;
        Task task = getExistingTask();
        if (task == null){
            return;
        }
        do {
            theView.printTask(task);
            theView.inputOptionsToChangeTask(task.isRepeated());
            switch (getUserInput()) {
                case "0":
                    conditionVar = false;
                    break;
                case "1":
                    changeTitle(task);
                    conditionVar = false;
                    break;
                case "2":
                    if (task.isRepeated())
                        changeStartTime(task);
                    else
                        changeTime(task);
                    conditionVar = false;
                    break;
                case "3":
                    if (task.isRepeated())
                        changeEndTime(task);
                    else
                        changeState(task);
                    conditionVar = false;
                    break;
                case "4":
                    changeInterval(task);
                    conditionVar = false;
                    break;
                case "5":
                    changeState(task);
                    conditionVar = false;
                    break;
                case "":
                    break;
                default:
                    theView.incorrectItem();
                    break;
            }
        } while (conditionVar);
    }

    /**
     * Organize process of removing tasks depending on user choice.
     * User can remove by one tasks at the time or remove all tasks
     * at once, if it need to.
     *
     * @throws TaskException if removing is failed
     */
    @Override
    public void removeTask() throws TaskException {
        boolean conditionVar = true;
        if (!checkForEmptyTaskList()){
            backAction();
            return;
        } do {
            theView.inputRemove();
            switch (getUserInput()){
                case "0":
                    conditionVar = false;
                    break;
                case "1":
                    removeByOne();
                    conditionVar = false;
                    break;
                case "2":
                    removeAll();
                    conditionVar = false;
                    break;
                case "":
                    break;
                default:
                    theView.incorrectItem();
                    break;
            }
        } while (conditionVar);

    }

    /**
     * Shows information about existing tasks.
     */
    @Override
    public void printTaskList() throws TaskException {
        if (checkForEmptyTaskList()){
            theView.printTaskList(theModel.getTaskList(), theModel.getTaskList().size());
        } backAction();
    }

    /**
     * Creates calendar of tasks for one day, three days or a week
     * and for a period which user specify.
     */
    @Override
    public void createCalendar() throws TaskException {
        if (!checkForEmptyTaskList()){
            backAction();
            return;
        }
        long forDay = 86400*1000;
        long forThreeDays = 86400*1000*3;
        long forWeek = 86400*1000*7;
        Date end = new Date();
        boolean conditionVar = true;
        do {
            theView.inputPeriodForCalendar();
            switch (getUserInput()){
                case "0":
                    conditionVar = false;
                    break;
                case "1":
                    end.setTime(end.getTime() + forDay);
                    theView.printCalendar(Tasks.calendar(theModel.getTaskList(), new Date(), end));
                    backAction();
                    conditionVar = false;
                    break;
                case "2":
                    end.setTime(end.getTime() + forThreeDays);
                    theView.printCalendar(Tasks.calendar(theModel.getTaskList(), new Date(), end));
                    backAction();
                    conditionVar = false;
                    break;
                case "3":
                    end.setTime(end.getTime() + forWeek);
                    theView.printCalendar(Tasks.calendar(theModel.getTaskList(), new Date(), end));
                    backAction();
                    conditionVar = false;
                    break;
                case "4":
                    theView.printCalendar(Tasks.calendar(theModel.getTaskList(), new Date(),
                            getInputEndTime(new Date())));
                    backAction();
                    conditionVar = false;
                    break;
                case "":
                    break;
                default:
                    theView.incorrectItem();
                    break;
            }
        } while (conditionVar);
    }

    /**
     * Creates a <tt>non-repeating</tt> task. For that, user enters
     * <tt>title</tt>, <tt>time</tt> and <tt>state</tt> of task.
     */
    private void createTask() throws TaskException {
        theModel.setTask(getInputTitle(), getInputTime());
        theModel.getTask().setActive(getStateOfTask());
        theModel.getTaskList().add(theModel.getTask());
        writeListToFile();
        theView.successfulCreateTask();
        backAction();
    }

    /**
     * Creates a <tt>repeating</tt> task. For that, user enters
     * <tt>title</tt>, <tt>start time</tt>, <tt>end time</tt>,
     * <tt>interval</tt> and <tt>state</tt> of task.
     */
    private void createRepeatingTask() throws TaskException {
        theModel.setRepeatTask(getInputTitle(), getInputStartTime(), getInputEndTime(theModel.getStart()), getInputInterval());
        theModel.getTask().setActive(getStateOfTask());
        theModel.getTaskList().add(theModel.getTask());
        writeListToFile();
        theView.successfulCreateTask();
        backAction();
    }

    /**
     * Returns <tt>false</tt> if list of tasks is empty
     *
     * @return <tt>true</tt> if list of task isn't empty
     */
    private boolean checkForEmptyTaskList() {
        if (theModel.getTaskList() == null || theModel.getTaskList().size() == 0){
            theView.outEmptyList();
            return false;
        } return true;
    }

    /**
     * Gets <tt>title</tt>, which user entered.
     *
     * @return title
     */
    private String getInputTitle() {
        String title;
        do {
            theView.inputTitle();
            title = getUserInput();
            if (!title.equals("")){
                return title;
            }
        } while (true);
    }

    /**
     * Gets line with time of <tt>non-repeating</tt> task.
     * and parsing it to preset format.
     *
     * @return <tt>time</tt> in format ("dd.MM.yy HH:mm")
     */
    private Date getInputTime() {
        String line;
        Date time;
        do {
            try {
                theView.inputTime();
                line = getUserInput();
                if (!line.equals("")){
                    time = theView.getFormat().parse(line);
                    return time;
                }
            } catch (ParseException e) {
                theView.errorDate();
            }
        } while (true);
    }

    /**
     * Gets line with start time of <tt>repeating</tt> task
     * and parsing it to preset format.
     *
     * @return <tt>start time</tt> in format ("dd.MM.yy HH:mm")
     */
    private Date getInputStartTime() {
        String date;
        do {
            try {
                theView.inputStartTime();
                date = getUserInput();
                if (!date.equals("")){
                    theModel.setStart(theView.getFormat().parse(date));
                    return theModel.getStart();
                }
            } catch (ParseException e) {
                theView.errorDate();
            }
        } while (true);
    }

    /**
     * Gets line with end time of <tt>repeating</tt> task, parsing it
     * to preset format and checks that it before @param.
     *
     * @param comparisonTime is time to compare with end time
     * @return <tt>start time</tt> in format ("dd.MM.yy HH:mm")
     */
    private Date getInputEndTime(Date comparisonTime) {
        String line;
        do {
            try {
                theView.inputEndTime();
                line = getUserInput();
                if (!line.equals("")){
                    theModel.setEnd(theView.getFormat().parse(line));
                    if (checkEndBeforeStart(comparisonTime)){
                        return theModel.getEnd();
                    }
                }
            } catch (ParseException e) {
                theView.errorDate();
            }
        } while (true);
    }

    /**
     * Returns <tt>true</tt> if end time is above @param.
     *
     * @param comparisonTime is time to compare with end time
     * @return <tt>false</tt> if end time is less than @param
     */
    private boolean checkEndBeforeStart(Date comparisonTime){
        if (theModel.getEnd().compareTo(comparisonTime) <= 0){
            theView.errorEndBeforeStart();
            return false;
        } else return true;
    }

    /**
     * Gets line with interval and parsing it to Integer.
     * Interval sets in minutes.
     *
     * @return interval of repeat task
     */
    private int getInputInterval(){
        String line;
        int interval;
        do {
            try {
                theView.inputInterval();
                line = getUserInput();
                if (!line.equals("")){
                    interval = Integer.parseInt(line);
                    if (interval <= 0){
                        theView.errorInterval();
                    } else return interval;
                }
            } catch (NumberFormatException e){
                theView.errorValue();
            }
        } while (true);
    }

    /**
     * Returns state of task. State of task will be <tt>activate</tt>,
     * when user enter <tt>true</tt> in such field. Another way, state
     * will be <tt>deactivate</tt>, if user entered <tt>false</tt>.
     *
     * @return state of existing task
     */
    private boolean getStateOfTask() {
        do {
            theView.inputStateOfTask();
            switch (getUserInput()) {
                case "true":
                    return true;
                case "false":
                    return false;
                case "":
                    break;
                default:
                    theView.errorState();
                    break;
            }
        } while (true);
    }

    /**
     * Method to get number of existing task. First of all, checks for
     * empty list, then user sees list of task and selects one of them.
     * Next, gets line with number of existing task and after parsing
     * it to Integer. If choice is correctly, returns it.
     *
     * @return number of task, which user selected.
     */
    private Task getExistingTask() {
        String line;
        int numTask;
        do {
            if (!checkForEmptyTaskList()){
                backAction();
                return null;
            }
            try {
                theView.printTaskList(theModel.getTaskList(), theModel.getTaskList().size());
                theView.inputExistTask();
                line = getUserInput();
                if (!line.equals("")){
                    numTask = Integer.parseInt(line) - 1;
                    if (numTask >= theModel.getTaskList().size() | numTask < -1) {
                        theView.incorrectNumberOfTask();
                    } else if (numTask == -1){
                        return null;
                    } else {
                        return theModel.getTask(numTask);
                    }
                }
            } catch (IllegalArgumentException e) {
                theView.invalidInputNumber();
            }
        } while (true);
    }

    /**
     * A few next methods provides changes <tt>title</tt>, <tt>time</tt>,
     * <tt>start time</tt>, <tt>end time</tt>, <tt>interval</tt> and
     * <tt>state</tt> by number of task and write it to file.
     *
     * @param task is number of existing task
     */
    private void changeTitle(Task task) {
        task.setTitle(getInputTitle());
        writeListToFile();
        theView.successfulChangedTask();
        backAction();
    }

    private void changeTime(Task task) {
        task.setTime(getInputTime());
        writeListToFile();
        theView.successfulChangedTask();
        backAction();
    }

    private void changeStartTime(Task task) throws TaskException {
        task.setTime(getInputStartTime(), task.getEndTime(), task.getRepeatInterval());
        writeListToFile();
        theView.successfulChangedTask();
        backAction();
    }

    private void changeEndTime(Task task) throws TaskException {
        theModel.setEnd(getInputEndTime(task.getStartTime()));
        task.setTime(task.getStartTime(), theModel.getEnd(), task.getRepeatInterval());
        writeListToFile();
        theView.successfulChangedTask();
        backAction();
    }

    private void changeInterval(Task task) throws TaskException {
        task.setTime(task.getStartTime(), task.getEndTime(), getInputInterval());
        writeListToFile();
        theView.successfulChangedTask();
        backAction();
    }

    private void changeState(Task task) {
        task.setActive(getStateOfTask());
        writeListToFile();
        theView.successfulChangedTask();
        backAction();
    }

    /**
     * Removes task and overwrite file with list of tasks.
     *
     * @param task is tasks which need to remove
     * @throws TaskException if removes is failed
     */
    boolean removeTask(Task task) throws TaskException {
        if (theModel.getTaskList().remove(task)) {
            writeListToFile();
            theModel.setTaskList(readListFromFile());
            return true;
        } else return false;
    }

    /**
     * Removes one task by number and overwrite file with list of tasks.
     */
    private void removeByOne() throws TaskException {
        Task task = getExistingTask();;
        if (task == null){
            return;
        } if (removeTask(task)){
            theView.successfulRemovedTask();
        } else theView.fallingRemove();
        backAction();
    }

    /**
     * Removes all tasks at once and overwrite file with list of tasks.
     */
    private void removeAll() throws TaskException {
        theModel.getTaskList().removeAll();
        writeListToFile();
        theModel.setTaskList(readListFromFile());
        theView.successfulRemovedTask();
        backAction();
    }

    /**
     * Returns a one step back in program.
     */
    private void backAction(){
        String line;
        do {
            try {
                theView.inputBack();
                line = getUserInput();
                if (line.equals("") || Integer.parseInt(line) == 0) {
                    break;
                }
                else theView.errorValue();
            } catch (NumberFormatException e) {
                theView.errorValue();
            }
        } while (true);
    }

    /**
     * Method, which takes actions from view and notify <tt>notifyThread</tt>
     * to go on.
     *
     * @return string of input.
     */
    private String getUserInput() {
        notifyToContinue();
        return theView.getScannerBuffer();
    }

    /**
     * Method to notify second thread to output message
     */
    private void notifyToContinue() {
        synchronized (notifyThread) {
            notifyThread.notify();
        }
    }

    /**
     * Writes current list of tasks in file and interrupt second thread.
     */
    private void shutDown(){
        writeListToFile();
        notifyThread.interrupt();
    }
}