package com.yura.lampak.view;


import com.yura.lampak.model.Task;
import com.yura.lampak.model.TaskList;
import java.text.SimpleDateFormat;
import java.util.*;


public class ConsoleView implements View {
    private static final String createItem = "1. Create a new task";
    private static final String changeItem = "2. Change existing task";
    private static final String removeItem = "3. Remove task";
    private static final String showTasksItem = "4. Print task list";
    private static final String showCalendarItem = "5. Print calendar of tasks for period";
    private static final String exitItem = "0. Exit";
    private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm");
    private Scanner scanner;

    public ConsoleView() {
        scanner = new Scanner(System.in);
    }

    @Override
    public void printMenu() {
        System.out.println("Menu:");
        System.out.println(createItem);
        System.out.println(changeItem);
        System.out.println(removeItem);
        System.out.println(showTasksItem);
        System.out.println(showCalendarItem);
        System.out.println(exitItem);
    }

    @Override
    public void printTask(Task task) {
        System.out.println(task.toString());
    }

    @Override
    public void printTaskList(TaskList taskList, int size) {
        System.out.println(taskList.toString());
        System.out.println("size: " + size);
    }

    @Override
    public void printCalendar(SortedMap <Date, Set<Task>> calendar) {
        System.out.println(calendar.toString());
    }

    public static SimpleDateFormat getFormat() {
        return format;
    }

    public void outEmptyList(){
        System.out.println("task list is empty, try to create a new task ");
    }

    public void inputTitle(){
        System.out.println("Title: \t 0.Back");
    }

    public void inputTime() { System.out.println("date:(dd.mm.yy hh:mm:ss)"); }

    public void inputStartTime() { System.out.println("start date:(dd.mm.yy hh:mm:ss)"); }

    public void inputEndTime() {
        System.out.println("end date:(dd.mm.yy hh:mm:ss)");
    }

    public void errorDate(){ System.out.println("enter correct date!"); }

    public void errorEndBeforeStart(){ System.out.println("end date must be above start date! "); }

    public void inputInterval() {
        System.out.println("interval (in minutes): ");
    }

    public void errorInterval(){
        System.out.println("enter correct interval!");
    }

    public void inputState() { System.out.println("activate: (true/false)"); }

    public void errorState(){ System.out.println("enter correct state(only true or false)"); }

    public void inputIsRepeated() {
        System.out.println("is repeated?: (true/false)");
    }

    public String getUserInput(){
        return scanner.nextLine();
    }

    public void inputBack(){ System.out.println("\n0. Back"); }

    public void errorValue(){ System.out.println("enter correct value!"); }

    public void inputExistTask() { System.out.println("\nSelect a task by number \t 0. Back to menu"); }

    public void invalidInputNumber() { System.out.println("enter only integer number!"); }

    public void inputRemove(){ System.out.println("\n1. Remove one task\n2. Remove all tasks\n\n0. Back"); }

    public void inputOptionToChangeTask(boolean isRep){
        System.out.println("1. Change title");
        if (isRep){
            System.out.println("2. Change start time");
            System.out.println("3. Change end time");
            System.out.println("4. Change interval time");
            System.out.println("5. Change state(activate/deactivate)");
        } else {
            System.out.println("2. Change time");
            System.out.println("3. Change state(activate/deactivate)");
        }
        System.out.println("0. Back to menu");
    }

    public void incorrectNumberOfTask(){ System.out.println("enter correct number of existing tasks"); }

    public void incorrectItem(){
        System.out.println("choose item from menu!");
    }

    public void incorrectParsedItem(){
        System.out.println("enter correct value!(only number)");
    }

    public void successfulCreateTask(){
        System.out.println("task is successful created, check it in task list!");
    }

    public void successfulChangedTask(){
        System.out.println("task is successful changed, check it in task list!");
    }

    public void successfulRemoveTask(){
        System.out.println("removed successful");
    }

    public void fallingRemove(){ System.out.println("this task is not exist in task list!"); }

    public void inputPeriodForCalendar(){
        System.out.println("Create calendar: ");
        System.out.println("1. For a day");
        System.out.println("2. For a three days");
        System.out.println("3. For a week");
        System.out.println("4. For a month");
        System.out.println("\n0. Back to menu");
    }

    public void printTasksToGo(Set<Task> tasks){
        System.out.println("***********************************");
        System.out.println("Alarm! Time to go next tasks :\n\n ");
        System.out.println(tasks.toString());
        System.out.println("\nstill working with the program..");
        System.out.println("***********************************");
    }
}
