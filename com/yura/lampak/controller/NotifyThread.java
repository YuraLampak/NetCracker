package com.yura.lampak.controller;

import com.yura.lampak.model.Task;
import com.yura.lampak.model.TaskException;
import com.yura.lampak.model.TaskList;
import com.yura.lampak.model.Tasks;
import com.yura.lampak.view.ConsoleView;


import java.util.*;


public class NotifyThread extends Thread {
    private ConsoleController controller;
    private ConsoleView theView;
    private SortedMap<Date, Set<Task>> notifyMap;


    NotifyThread(ConsoleController controller, ConsoleView theView) {
        super("notifyThread");
        this.controller = controller;
        this.theView = theView;
        notifyMap = new TreeMap<>();
        this.setDaemon(true);
        start();
    }

    @Override
    public void run() {
        TaskList taskList;
        Date currentTime, startTime = new Date();
        Date endTime = new Date();
        int balanceTime = 1000;
        startTime.setTime(startTime.getTime() - balanceTime);
        while (true) {
            if (this.isInterrupted()) {
                break;
            }
            try {
                taskList = controller.readListFromFile();
                currentTime = new Date();
                endTime.setTime(currentTime.getTime() + balanceTime);
                notifyMap = Tasks.calendar(taskList, startTime, endTime);
                Iterator<Task> itr_task;
                for (Map.Entry<Date, Set<Task>> entry : notifyMap.entrySet()) {
                    itr_task = entry.getValue().iterator();
                    while (itr_task.hasNext()) {
                        if (itr_task.next().isActive() & entry.getKey().compareTo(currentTime) == 0) {
                            notifyUser(entry.getValue());
                        }
                    }
                }
            } catch (TaskException e) {
                e.printStackTrace();
            }
        }
    }

    private void notifyUser(Set<Task> tasks){
        Task temp;
        theView.printTasksToGo(tasks);
        Iterator<Task> itr = tasks.iterator();
        while (itr.hasNext()){
            temp = itr.next();
            if (!temp.isRepeated()){
                try {
                    controller.removeTask(temp);
                } catch (TaskException e) {
                    e.printStackTrace();
                }
            }
        }
        try {                               //этот участок кода для того, чтобы не выводилось оповещение несколько раз
            Thread.sleep(10);         // подряд для одного и того же сета тасков
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

