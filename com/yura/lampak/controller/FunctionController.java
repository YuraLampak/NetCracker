package com.yura.lampak.controller;


import com.yura.lampak.model.Task;
import com.yura.lampak.model.TaskException;
import com.yura.lampak.view.ConsoleView;
import static com.yura.lampak.controller.ConsoleController.notifyThread;
import static com.yura.lampak.controller.ConsoleController.theModel;
import static com.yura.lampak.controller.ConsoleController.theView;


abstract class FunctionController {


    static void execute (String operation) throws TaskException {
        switch (operation){
            case "1":
                new CreateTaskController();
                break;
            case "2":
                new ChangeTaskController();
                break;
            case "3":
                new RemoveTaskController();
                break;
            case "4":
                printTaskList();
                break;
            case "5":
                new CreateCalendarController();
                break;
        }
    }

    /**
     * Method, which takes actions from view and notify <tt>notifyThread</tt>
     * to go on.
     *
     * @return string of input.
     */
    static String getUserInput() {
        notifyToContinue();
        return ConsoleView.getScannerBuffer();
    }

    /**
     * Method to notify second thread to output message
     */
    private static void notifyToContinue() {
        synchronized (notifyThread) {
            notifyThread.notify();
        }
    }

    /**
     * Returns a one step back in program.
     */
    static void backAction(){
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
     * Returns <tt>false</tt> if list of tasks is empty
     *
     * @return <tt>true</tt> if list of task isn't empty
     */
    static boolean checkForEmptyTaskList() {
        if (theModel.getTaskList() == null || theModel.getTaskList().size() == 0){
            theView.outEmptyList();
            return false;
        } return true;
    }

    /**
     * Method to get list of tasks. Before that, check if it's not empty.
     */
    static void printTaskList(){
        if (FunctionController.checkForEmptyTaskList()){
            theView.printTaskList(theModel.getTaskList(), theModel.getTaskList().size());
        } FunctionController.backAction();
    }

    /**
     * Method to get number of existing task. First of all, checks for
     * empty list, then user sees list of task and selects one of them.
     * Next, gets line with number of existing task and after parsing
     * it to Integer. If choice is correctly, returns it.
     *
     * @return number of task, which user selected.
     */
    Task getExistingTask() {
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

}
