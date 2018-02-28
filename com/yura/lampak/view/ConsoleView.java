package com.yura.lampak.view;

import com.yura.lampak.model.Task;
import com.yura.lampak.model.TaskList;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Implementation <tt>ConsoleView</tt> of <tt>View</tt> interfaces.
 * Displays steps running program, prompts to user what should he do,
 * indicates errors, and gives all the information the user needs.
 * @see View
 *
 * @author Yura Lampak
 * @version 1.0
 */
public class ConsoleView implements View {
    /**
     * A few next variable is a items of menu.
     */
    private static final String createItem = "1. Create a new task";
    private static final String changeItem = "2. Change existing task";
    private static final String removeItem = "3. Remove task";
    private static final String showTasksItem = "4. Print list of tasks";
    private static final String showCalendarItem = "5. Calendar for period";
    private static final String exitItem = "0. Exit";

    /**
     * Special format for dates.
     */
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm");

    /**
     * Listener for getting user actions
     */
    private final static Scanner scanner = new Scanner(System.in);

    /**
     * Constructor creates instance of Scanner to gets user input.
     */
    public ConsoleView() {
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

    /**
     * Method to interaction with user, reads his actions
     *
     * @return buffer of entered data
     */
    public static String getScannerBuffer() {
        return scanner.nextLine();
    }

    public SimpleDateFormat getFormat() {
        return format;
    }

    public void outEmptyList(){
        System.out.println("\nList of task is empty, try to create a new task ");
    }

    public void inputTitle(){
        System.out.print("Title: ");
    }

    public void inputTime() { System.out.print("Date (in format dd.mm.yy hh:mm): "); }

    public void inputStartTime() { System.out.print("Start date (in format dd.mm.yy hh:mm): "); }

    public void inputEndTime() {
        System.out.print("End date: (in format dd.mm.yy hh:mm): ");
    }

    public void errorDate(){ System.out.println("enter correct date!"); }

    public void errorEndBeforeStart(){ System.out.println("end date must be above start date! "); }

    public void inputInterval() {
        System.out.print("Interval (in minutes): ");
    }

    public void errorInterval(){
        System.out.println("Interval must be above zero!");
    }

    public void inputStateOfTask() { System.out.print("Activate (true/false): "); }

    public void errorState(){ System.out.println("enter correct state(only true or false)"); }

    public void inputTypeOfTask() {
        System.out.println("You can create the next task: ");
        System.out.println("\n1. One time task");
        System.out.println("2. Repetitive task");
        System.out.println("\n0. Back to menu");
    }

    public void inputBack(){ System.out.println("\n0. Back"); }

    public void errorValue(){ System.out.println("enter correct value!"); }

    public void inputExistTask() { System.out.println("\nSelect a task by number \t 0. Back to menu"); }

    public void invalidInputNumber() { System.out.println("enter only integer number!"); }

    public void inputRemove(){ System.out.println("\n1. Remove one task\n2. Remove all tasks\n\n0. Back"); }

    public void inputOptionsToChangeTask(boolean isRep){
        System.out.println("1. Change title");
        if (isRep){
            System.out.println("2. Change start time");
            System.out.println("3. Change end time");
            System.out.println("4. Change interval time");
            System.out.println("5. Change state(activate/deactivate)");
        } else {
            System.out.println("2. Change time");
            System.out.println("3. Change state(activate/deactivate)");
        } System.out.println("0. Back to menu");
    }

    public void incorrectNumberOfTask(){ System.out.println("enter correct number of existing tasks!"); }

    public void incorrectItem(){
        System.out.println("choose correct item from menu!");
    }

    public void successfulCreateTask(){
        System.out.println("\ntask is successful created, check it in task list!");
    }

    public void successfulChangedTask(){
        System.out.println("\ntask is successful changed, check it in task list!");
    }

    public void successfulRemovedTask(){ System.out.println("removed successful"); }

    public void fallingRemove(){ System.out.println("this task is not exist in task list!"); }

    /**
     * Displays options for creating a calendar.
     */
    public void inputPeriodForCalendar(){
        System.out.println("Create calendar: ");
        System.out.println("1. For a day");
        System.out.println("2. For a three days");
        System.out.println("3. For a week");
        System.out.println("4. For a specify period");
        System.out.println("\n0. Back to menu");
    }

    /**
     * Displays a set of tasks, which have to performed at this time
     *
     * @param tasks is set of tasks
     */
    public static void printTasksToGo(Set<Task> tasks){
        System.out.println("\n\n********************************************************");
        System.out.println("Alarm! Time to go next tasks :\n\n ");
        System.out.println(tasks.toString());
        System.out.println("**********************************************************");
        System.out.println("For still working with the program press ENTER...");
    }

}
