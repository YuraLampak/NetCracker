package com.yura.lampak.controller;

import com.yura.lampak.model.Task;
import com.yura.lampak.model.TaskException;
import com.yura.lampak.view.ConsoleView;

import static com.yura.lampak.controller.ConsoleController.theModel;
import static com.yura.lampak.controller.ConsoleController.theView;
import static com.yura.lampak.controller.ConsoleController.writeListToFile;


class ChangeTaskController extends FunctionController {

    /**
     * Changes existing task by several parameters: title, time (for each
     * kind of task his own) and state of task. Get number of task, then
     * define type of task by this number and do switch-case to change
     * one of parameters, depending on user choice.
     */
    ChangeTaskController() throws TaskException {
        boolean conditionVar = true;
        Task task = getExistingTask();
        if (task == null){
            return;
        }
        do {
            theView.printTask(task);
            theView.inputOptionsToChangeTask(task.isRepeated());
            switch (ConsoleView.getScannerBuffer()) {
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
     * A few next methods provides changes <tt>title</tt>, <tt>time</tt>,
     * <tt>start time</tt>, <tt>end time</tt>, <tt>interval</tt> and
     * <tt>state</tt> by number of task and write it to file.
     *
     * @param task is number of existing task
     */
    private void changeTitle(Task task) {
        task.setTitle(CreateTaskController.getInputTitle());
        writeListToFile();
        theView.successfulChangedTask();
        backAction();
    }

    private void changeTime(Task task) {
        task.setTime(CreateTaskController.getInputTime());
        writeListToFile();
        theView.successfulChangedTask();
        backAction();
    }

    private void changeStartTime(Task task) throws TaskException {
        task.setTime(CreateTaskController.getInputStartTime(), task.getEndTime(), task.getRepeatInterval());
        writeListToFile();
        theView.successfulChangedTask();
        backAction();
    }

    private void changeEndTime(Task task) throws TaskException {
        theModel.setEnd(CreateTaskController.getInputEndTime(task.getStartTime()));
        task.setTime(task.getStartTime(), theModel.getEnd(), task.getRepeatInterval());
        writeListToFile();
        theView.successfulChangedTask();
        backAction();
    }

    private void changeInterval(Task task) throws TaskException {
        task.setTime(task.getStartTime(), task.getEndTime(), CreateTaskController.getInputInterval());
        writeListToFile();
        theView.successfulChangedTask();
        backAction();
    }

    private void changeState(Task task) {
        task.setActive(CreateTaskController.getStateOfTask());
        writeListToFile();
        theView.successfulChangedTask();
        backAction();
    }
}
