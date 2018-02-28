package com.yura.lampak.controller;

import com.yura.lampak.model.Task;
import com.yura.lampak.model.TaskException;
import static com.yura.lampak.controller.ConsoleController.*;


class RemoveTaskController extends FunctionController {

    /**
     * Organize process of removing tasks depending on user choice.
     * User can remove by one tasks at the time or remove all tasks
     * at once, if it need to.
     *
     * @throws TaskException if removing is failed
     */
    RemoveTaskController() throws TaskException {
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
     * Removes task and overwrite file with list of tasks.
     *
     * @param task is tasks which need to remove
     * @throws TaskException if removes is failed
     */
    static boolean removeTask(Task task) throws TaskException {
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
        Task task = getExistingTask();
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
}
