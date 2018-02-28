package com.yura.lampak.controller;

import com.yura.lampak.model.TaskException;
import com.yura.lampak.model.Tasks;
import java.util.Date;
import static com.yura.lampak.controller.ConsoleController.theModel;
import static com.yura.lampak.controller.ConsoleController.theView;


class CreateCalendarController extends FunctionController{

    /**
     * Creates calendar of tasks for one day, three days or a week
     * and for a period which user specify.
     */
    CreateCalendarController() throws TaskException {
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
                            CreateTaskController.getInputEndTime(new Date())));
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
}
