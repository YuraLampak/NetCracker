package com.yura.lampak.model;

import java.util.*;

public class Model {
    private Task task;
    private TaskList taskList;

    public Model(){
        taskList = new LinkedTaskList();
    }

    public void setTask (String title, Date time) {
        this.task = new Task(title, time);
    }

    public void setTask (String title, Date start, Date end, int interval) throws TaskException {
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
