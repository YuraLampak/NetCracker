package ua.edu.sumdu.j2se.YuraLampak.tasks.view;

import ua.edu.sumdu.j2se.YuraLampak.tasks.model.Task;
import ua.edu.sumdu.j2se.YuraLampak.tasks.model.TaskList;

import java.util.*;

public interface View {
    void printMenu();
    void printTask(Task task);
    void printTaskList(TaskList task);
    void printCalendar(SortedMap<Date, Set<Task>> calendar);
}
