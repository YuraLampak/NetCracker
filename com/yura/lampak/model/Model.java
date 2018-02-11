package com.yura.lampak.model;

import java.util.*;


public class Model {

    /**
     * This dates is necessary for setting correct time of repeated tasks
     */
    private Date startTime, endTime;

    private Task task;

    private TaskList taskList;

    public Model(){
        taskList = new LinkedTaskList();
    }

    public void setStart(Date start) {
        this.startTime = start;
    }

    public void setEnd(Date end) {
        this.endTime = end;
    }

    public Date getStart() {
        return startTime;
    }

    public Date getEnd() {
        return endTime;
    }

    public void setTask (String title, Date time) {
        this.task = new Task(title, time);
    }

    public void setRepeatTask (String title, Date start, Date end, int interval) {
        this.task = new Task(title, start, end, interval);
    }

    public Task getTask() {
        return task;
    }

    public Task getTask (int numbTask){
        return taskList.getTask(numbTask);
    }

    public void setTaskList(TaskList taskList) { this.taskList = taskList; }

    public TaskList getTaskList() {
        return taskList;
    }
}
