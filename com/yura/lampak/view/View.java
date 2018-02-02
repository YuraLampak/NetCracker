package com.yura.lampak.view;

import com.yura.lampak.model.Task;
import com.yura.lampak.model.TaskList;

import java.util.*;

public interface View {
    void printMenu();
    void printTask(Task task);
    void printTaskList(TaskList task, int size);
    void printCalendar(SortedMap<Date, Set<Task>> calendar);
}
