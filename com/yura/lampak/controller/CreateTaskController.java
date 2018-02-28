package com.yura.lampak.controller;

import com.yura.lampak.model.TaskException;
import java.text.ParseException;
import java.util.Date;
import static com.yura.lampak.controller.ConsoleController.theModel;
import static com.yura.lampak.controller.ConsoleController.theView;
import static com.yura.lampak.controller.ConsoleController.writeListToFile;


class CreateTaskController extends FunctionController {

    /**
     * Creates <tt>repeating</tt> task or <tt>non-repeating</tt>
     * tasks depending on user choice.
     */
    CreateTaskController() throws TaskException {
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
     * Gets <tt>title</tt>, which user entered.
     *
     * @return title
     */
    static String getInputTitle() {
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
    static Date getInputTime() {
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
    static Date getInputStartTime() {
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
    static Date getInputEndTime(Date comparisonTime) {
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
    private static boolean checkEndBeforeStart(Date comparisonTime){
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
    static int getInputInterval(){
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
    static boolean getStateOfTask() {
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
}
