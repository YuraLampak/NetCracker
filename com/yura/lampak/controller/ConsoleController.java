package com.yura.lampak.controller;




import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import com.yura.lampak.model.*;
import com.yura.lampak.view.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



public class ConsoleController implements Controller {
    private ConsoleView theView;
    private Model theModel;
    private String title;
    private Date time, start, end;
    private int interval;
    private static final File file = new File("TaskList.txt").getAbsoluteFile();
    private static final Logger logger = LogManager.getLogger(ConsoleController.class);


    public ConsoleController(Model theModel, ConsoleView theView) throws TaskException {
        this.theModel = theModel;
        this.theView = theView;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("creating file is failed", e);
            }
        }
        TaskList tempList = new LinkedTaskList();
        try {
            TaskIO.readText(tempList, file);
            theModel.setTaskList(tempList);
        } catch (StringIndexOutOfBoundsException e){
            theModel.setTaskList(new LinkedTaskList());
        }
        new NotifyThread(this, theView);
    }

    TaskList readListFromFile() throws TaskException {
        TaskList tempList = new LinkedTaskList();
        try {
            TaskIO.readText(tempList, file);
            return tempList;
        } catch (NumberFormatException|StringIndexOutOfBoundsException e){
            return new LinkedTaskList();
        }
    }

    private void writeListToFile() throws TaskException {
        try {
            TaskIO.writeText(theModel.getTaskList(), file);
        } catch (Exception e){
            logger.error("writing file is failed", e);
        }

    }

    @Override
    public void execute() throws TaskException {
        int temp;
        theView.printMenu();
        switch (getParseItem()) {
            case 0:
                break;
            case 1:
                title = getInputTitle();
                if (checkForBack(title)){
                    execute();
                    break;
                }
                theView.inputIsRepeated();
                if (getInputState()){
                    createRepeatTask();
                } else {
                    createTask();
                }
                backAction();
                execute();
                break;
            case 2:
                if (checkForEmpty()){
                    theView.printTaskList(theModel.getTaskList(), theModel.getTaskList().size());
                    temp = getExistTask();
                    if (checkForBack(temp)){
                        execute();
                        break;
                    }
                    theView.printTask(theModel.getTask(temp));
                    theView.inputOptionToChangeTask(theModel.getTask(temp).isRepeated());
                    changeExistTask(temp, theModel.getTask(temp).isRepeated());
                } else {
                    backAction();
                }
                execute();
                break;
            case 3:
                if (checkForEmpty()){
                    theView.inputRemove();
                    removeActions();
                } else {
                    backAction();
                    execute();
                }
                break;
            case 4:
                if (checkForEmpty()){
                    theView.printTaskList(theModel.getTaskList(), theModel.getTaskList().size());
                }
                backAction();
                execute();
                break;
            case 5:
                if (checkForEmpty()){
                    theView.inputPeriodForCalendar();
                    createCalendar();
                } else {
                    backAction();
                }
                execute();
                break;
            default:
                theView.incorrectItem();
                execute();
                break;
        }
    }

    private boolean checkForEmpty() throws TaskException {
        if (theModel.getTaskList() == null || theModel.getTaskList().size() == 0){
            theView.outEmptyList();
            return false;
        } return true;
    }

    private void createTask() throws TaskException {
        theView.inputTime();
        time = getInputTime();
        theModel.setTask(title, time);
        theView.inputState();
        theModel.getTask().setActive(getInputState());
        theModel.getTaskList().add(theModel.getTask());
        writeListToFile();
        theView.successfulCreateTask();
    }

    private void createRepeatTask() throws TaskException {
        theView.inputStartTime();
        start = getInputTime();
        theView.inputEndTime();
        end = checkEndBeforeStart(start);
        theView.inputInterval();
        interval = getInputInterval();
        theModel.setTask(title, start, end, interval);
        theView.inputState();
        theModel.getTask().setActive(getInputState());
        theModel.getTaskList().add(theModel.getTask());
        writeListToFile();
        theView.successfulCreateTask();
    }

    private void changeExistTask (int numOfTask, boolean isRepeat) throws TaskException {
        switch (getParseItem()) {
            case 0:
                break;
            case 1:
                changeTitle(numOfTask);
                backAction();
                break;
            case 2:
                if (isRepeat) {
                    theView.inputStartTime();
                    changeStartTime(numOfTask);
                    backAction();
                    break;
                } else {
                    theView.inputTime();
                    changeTime(numOfTask);
                    backAction();
                    break;
                }
            case 3:
                if (isRepeat){
                    theView.inputEndTime();
                    changeEndTime(numOfTask);
                    backAction();
                    break;
                } else {
                    theView.inputState();
                    changeState(numOfTask);
                    backAction();
                    break;
                }
            case 4:
                theView.inputInterval();
                changeInterval(numOfTask);
                backAction();
                break;
            case 5:
                theView.inputState();
                changeState(numOfTask);
                backAction();
                break;
            default:
                theView.incorrectItem();
                changeExistTask(numOfTask, theModel.getTask(numOfTask).isRepeated());
                break;
        }
    }

    private void changeTitle(int numOfTask) throws TaskException {
        title = getInputTitle();
        theModel.getTaskList().getTask(numOfTask).setTitle(title);
        writeListToFile();
        theView.successfulChangedTask();
    }

    private void changeTime(int numOfTask) throws TaskException {
        time = getInputTime();
        theModel.getTask(numOfTask).setTime(time);
        writeListToFile();
        theView.successfulChangedTask();
    }

    private void changeStartTime(int numOfTask) throws TaskException {
        start = getInputTime();
        theModel.getTask(numOfTask).setTime(time, theModel.getTask(numOfTask).getEndTime(),
                theModel.getTask(numOfTask).getRepeatInterval());
        writeListToFile();
        theView.successfulChangedTask();
    }

    private void changeEndTime(int numOfTask) throws TaskException {
        end = checkEndBeforeStart(theModel.getTask(numOfTask).getStartTime());
        theModel.getTask(numOfTask).setTime(theModel.getTask(numOfTask).getStartTime(), end,
                theModel.getTask(numOfTask).getRepeatInterval());
        writeListToFile();
        theView.successfulChangedTask();
    }

    private void changeState(int numOfTask) throws TaskException {
        theModel.getTask(numOfTask).setActive(getInputState());
        writeListToFile();
        theView.successfulChangedTask();
    }

    private void changeInterval(int numOfTask) throws TaskException {
        interval = getInputInterval();
        theModel.getTask(numOfTask).setTime(theModel.getTask(numOfTask).getStartTime(),
                theModel.getTask(numOfTask).getEndTime(), interval);
        writeListToFile();
        theView.successfulChangedTask();
    }

    private void createCalendar() throws TaskException{
        end = new Date();
        switch (getParseItem()){
            case 0:
                break;
            case 1:
                end.setTime(end.getTime() + 86400*1000);
                theView.printCalendar(Tasks.calendar(theModel.getTaskList(), new Date(), end));
                backAction();
                break;
            case 2:
                end.setTime(end.getTime() + 86400*1000*3);
                theView.printCalendar(Tasks.calendar(theModel.getTaskList(), new Date(), end));
                backAction();
                break;
            case 3:
                end.setTime(end.getTime() + 86400*1000*7);
                theView.printCalendar(Tasks.calendar(theModel.getTaskList(), new Date(), end));
                backAction();
                break;
            case 4:
                theView.inputEndTime();
                end = getInputTime();
                theView.printCalendar(Tasks.calendar(theModel.getTaskList(), new Date(), end));
                backAction();
                break;
            default:
                theView.incorrectItem();
                createCalendar();
                break;
        }
    }
    private String getInputTitle() {
        theView.inputTitle();
        title = theView.getUserInput();
        return title;
    }

    private Date getInputTime() {
        do {
            try {
                return ConsoleView.getFormat().parse(theView.getUserInput());
            } catch (ParseException e) {
                theView.errorDate();
            }
        } while (true);
    }

    private Date checkEndBeforeStart(Date start){
        do {
            end = getInputTime();
            if (end.compareTo(start)<=0){
                theView.errorEndBeforeStart();
            } else return end;
        } while (true);
    }

    private int getParseItem(){
        do {
            try {
                return Integer.parseInt(theView.getUserInput());
            } catch (NumberFormatException e){
                theView.incorrectParsedItem();
            }
        } while (true);
    }

    private int getInputInterval(){
        try {
            interval = Integer.parseInt(theView.getUserInput());     //интервал в минутах
        } catch (NumberFormatException e){
            theView.errorInterval();
            getInputInterval();
        } return interval;
    }

    private boolean getInputState() {
        String state;
        do {
            state = theView.getUserInput();
            switch (state) {
                case "true":
                    return true;
                case "false":
                    return false;
                default:
                    theView.errorState();
                    break;
            }
        } while (true);
    }

    private void backAction(){
        theView.inputBack();
        do {
            try {
                if (Integer.parseInt(theView.getUserInput()) != 0){
                    theView.errorValue();
                } else break;
            } catch (NumberFormatException e) {
                theView.errorValue();
            }
        } while (true);
    }

    private int getExistTask() throws TaskException {
        int temp;
        theView.inputExistTask();
        do {
            try {
                temp = Integer.parseInt(theView.getUserInput()) - 1;
                if (temp >= theModel.getTaskList().size() | temp < -1) {
                    theView.incorrectNumberOfTask();
                } else break;
            } catch (IllegalArgumentException e) {
                theView.invalidInputNumber();
            }
        } while (true);
        return temp;
    }

    private void removeActions() throws TaskException {
        switch (getParseItem()){
            case 0:
                execute();
                break;
            case 1:
                removeByOne();
                execute();
                break;
            case 2:
                removeAll();
                backAction();
                execute();
                break;
            default:
                theView.incorrectItem();
                removeActions();
                break;
        }
    }

    private void removeByOne() throws TaskException {
        int numTask;
        theView.printTaskList(theModel.getTaskList(), theModel.getTaskList().size());
        numTask = getExistTask();
        if (checkForBack(numTask)) {
            return;
        }
        do {
            if (numTask > theModel.getTaskList().size()){
                theView.incorrectNumberOfTask();
            } else {
                removeTask(theModel.getTaskList().getTask(numTask));
                break;
            }
        } while (true);
    }

    void removeTask(Task task) throws TaskException {
        if (theModel.getTaskList().remove(task)) {
            writeListToFile();
            theModel.setTaskList(readListFromFile());
            theView.successfulRemoveTask();
        } else {
            theView.fallingRemove();
        }
    }

    private void removeAll() throws TaskException {
        theModel.getTaskList().removeAll();
        writeListToFile();
        theModel.setTaskList(readListFromFile());
        theView.successfulRemoveTask();
    }


    private boolean checkForBack(String str){ return str.equals("0"); }

    private boolean checkForBack(int var) {
        return var == -1;
    }
}
