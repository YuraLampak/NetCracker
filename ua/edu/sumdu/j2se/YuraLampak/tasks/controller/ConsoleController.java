package ua.edu.sumdu.j2se.YuraLampak.tasks.controller;




import java.io.File;
import java.text.ParseException;
import java.util.*;

import ua.edu.sumdu.j2se.YuraLampak.tasks.model.*;
import ua.edu.sumdu.j2se.YuraLampak.tasks.view.*;


public class ConsoleController implements Controller {
    private ConsoleView theView;
    private Model theModel;
    private String title;
    private Date time, start, end;
    private int interval;
    private static final File file = new File(".\\src\\main\\java\\TaskList.txt");


    public ConsoleController(Model theModel, ConsoleView theView) throws TaskException {
        this.theModel = theModel;
        this.theView = theView;
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
        TaskIO.writeText(theModel.getTaskList(), file);
    }

    @Override
    public void execute() throws TaskException {
        int temp;
        //theModel.setTaskList(readListFromFile());
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
                    theView.inputStartTime();
                    start = getInputTime();
                    theView.inputEndTime();
                    end = getInputTime();
                    theView.inputInterval();
                    interval = getInputInterval();
                    theModel.setTask(title, start, end, interval);
                    theView.inputState();
                    theModel.getTask().setActive(getInputState());
                    theModel.getTaskList().add(theModel.getTask());
                    writeListToFile();
                    theView.successfulCreateTask();
                    backAction();
                    execute();
                    break;
                }
                theView.inputTime();
                time = getInputTime();
                theModel.setTask(title, time);
                theView.inputState();
                theModel.getTask().setActive(getInputState());
                theModel.getTaskList().add(theModel.getTask());
                writeListToFile();
                theView.successfulCreateTask();
                backAction();
                execute();
                break;
            case 2:
                if (theModel.checkForEmpty()){
                    backAction();
                    execute();
                    break;
                }
                theView.printTaskList(theModel.getTaskList());
                temp = getExistTask();
                if (checkForBack(temp)) {
                    execute();
                    break;
                }
                theView.printTask(theModel.getTask(temp));
                theView.inputOptionToChangeTask(theModel.getTask(temp).isRepeated());
                changeExistTask(temp, theModel.getTask(temp).isRepeated());
                execute();
                break;
            case 3:
                if (theModel.checkForEmpty()){
                    backAction();
                    execute();
                    break;
                }
                theView.printTaskList(theModel.getTaskList());
                temp = getExistTask();
                if (temp < 0) {
                    execute();
                    break;
                }
                if (temp > theModel.getTaskList().size()){
                    theView.incorrectNumberOfTask();
                    execute();
                    break;
                }
                removeTask(theModel.getTaskList().getTask(temp));
                //theView.successfulRemoveTask();
                backAction();
                execute();
                break;
            case 4:
                if (theModel.checkForEmpty()){
                    backAction();
                    execute();
                    break;
                }
                theView.printTaskList(theModel.getTaskList());
                System.out.print("size: " + theModel.getTaskList().size());
                backAction();
                execute();
                break;
            case 5:
                if (theModel.checkForEmpty()){
                    backAction();
                    execute();
                    break;
                }
                theView.inputPeriodForCalendar();
                createCalendar();
                backAction();
                execute();
                break;
            default:
                theView.incorrectItem();
                execute();
                break;
        }
    }

    private void changeExistTask (int numOfTask, boolean isRepeat) throws TaskException {
        switch (getParseItem()) {
            case 0:
                break;
            case 1:
                title = getInputTitle();
                theModel.getTask(numOfTask).setTitle(title);
                writeListToFile();
                theView.successfulChangedTask();
                backAction();
                break;
            case 2:
                if (isRepeat) {
                    theView.inputStartTime();
                    start = getInputTime();
                    theModel.getTask(numOfTask).setTime(start, theModel.getTask(numOfTask).getEndTime(),
                            theModel.getTask(numOfTask).getRepeatInterval());
                    writeListToFile();
                    theView.successfulChangedTask();
                    backAction();
                    break;
                } else {
                    theView.inputTime();
                    time = getInputTime();
                    theModel.getTask(numOfTask).setTime(time);
                    writeListToFile();
                    theView.successfulChangedTask();
                    backAction();
                    break;
                }
            case 3:
                if (isRepeat){
                    theView.inputEndTime();
                    end = getInputTime();
                    theModel.getTask(numOfTask).setTime(theModel.getTask(numOfTask).getStartTime(), end,
                            theModel.getTask(numOfTask).getRepeatInterval());
                    writeListToFile();
                    theView.successfulChangedTask();
                    backAction();
                    break;
                } else {
                    theView.inputState();
                    theModel.getTask(numOfTask).setActive(getInputState());
                    writeListToFile();
                    theView.successfulChangedTask();
                    backAction();
                    break;
                }
            case 4:
                theView.inputInterval();
                interval = getInputInterval();
                theModel.getTask(numOfTask).setTime(theModel.getTask(numOfTask).getStartTime(),
                        theModel.getTask(numOfTask).getEndTime(), interval);
                writeListToFile();
                theView.successfulChangedTask();
                backAction();
                break;
            case 5:
                theView.inputState();
                theModel.getTask(numOfTask).setActive(getInputState());
                writeListToFile();
                theView.successfulChangedTask();
                backAction();
                break;
            default:
                theView.incorrectItem();
                changeExistTask(numOfTask, theModel.getTask(numOfTask).isRepeated());
                break;
        }
    }

    private void createCalendar() throws TaskException{
        end = new Date();
        switch (getParseItem()){
            case 1:
                end.setTime(end.getTime() + 86400*1000);
                theView.printCalendar(Tasks.calendar(theModel.getTaskList(), new Date(), end));
                break;
            case 2:
                end.setTime(end.getTime() + 86400*1000*3);
                theView.printCalendar(Tasks.calendar(theModel.getTaskList(), new Date(), end));
                break;
            case 3:
                end.setTime(end.getTime() + 86400*1000*7);
                theView.printCalendar(Tasks.calendar(theModel.getTaskList(), new Date(), end));
                break;
            case 4:
                theView.inputEndTime();
                end = getInputTime();
                theView.printCalendar(Tasks.calendar(theModel.getTaskList(), new Date(), end));
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
            interval = Integer.parseInt(theView.getUserInput());
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

    private int getExistTask() {
        int temp;
        theView.inputExistTask();
        do {
            try {
                temp = Integer.parseInt(theView.getUserInput()) - 1;
                if (temp >= theModel.getTaskList().size() | temp < -1) {
                    theView.incorrectNumberOfTask();
                } else return temp;
            } catch (IllegalArgumentException e) {
                theView.errorExistTask();
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

    private boolean checkForBack(String str){ return str.equals("0"); }

    private boolean checkForBack(int var) {
        return var == -1;
    }

}
