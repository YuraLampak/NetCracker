package ua.edu.sumdu.j2se.YuraLampak.tasks;


import java.io.*;



/**
 * Class LinkedTaskList
 * for
 * programm <b>Task manager</b>
 *
 * @author YuraLampak
 * @version 1.0
 */


public class LinkedTaskList extends TaskList {

    /**
     * it's a head of list
     */
    private Node firstNode;

    /**
     * it's a tail of list
     */
    private Node lastNode;

    /**
     * The size of the ArrayList (the number of elements it contains)
     */
    private int size = 0;


    /**
     * Constructs a list which consists of head and tail.
     */
    public LinkedTaskList(){
        firstNode = new Node (null, null, lastNode);
        lastNode = new Node (null, firstNode, null);
    }

    /**
     * Appends the task to the end of list.
     *
     * @param task is appends to the list
     */
    public void add(Task task) {
        if(firstNode.getNextElement() == null){
            Node prev = lastNode;
            firstNode.setNextElement(prev);
            prev.setItem(task);
            lastNode = new Node (null, prev, firstNode);
            prev.setNextElement(lastNode);
            size++;
        } else {
            Node prev = lastNode;
            prev.setItem(task);
            lastNode = new Node (null, prev, firstNode);
            prev.setNextElement(lastNode);
            size++;
        }
    }

    /**
     * Removes the task at the specified position in this list.
     * Shifts all subsequent tasks to the left (subtracts one from their indices).
     *
     * @param task which is removes from TaskList
     * @return the <tt>true</tt> if such a task was on the list.
     */
    public boolean remove(Task task) {
        Node target = firstNode.getNextElement();
        while (target != lastNode){
            if (target.getItem().equals(task)) {
                (target.getPreviousElement()).setNextElement((target.getNextElement()));
                (target.getNextElement()).setPreviousElement(target.getPreviousElement());
                size--;
                return true;
            } target = target.getNextElement();
        } return false;
    }


    /**
     * Returns task of this TaskList on specified ID.
     *
     * @param index is specified ID in TaskList
     * @return task of this TaskList on specified ID.
     */
    public Task getTask(int index) {
        Node target = firstNode.getNextElement();
        for (int i = 0; i < index; i++) {
            if (target.getItem() == null) {
                target = target.getPreviousElement();
                break;
            } target = target.getNextElement();
        } return target.getItem();
    }

    /**
     * Returns amount of tasks in the TaskList
     *
     * @return amount of tasks in the TaskList
     */
    public int size(){ return size; }


    /**
     * Inner class of LinkedTaskList which realizes logic of doubly linked list
     * Consists of body, link to the next element and previous in the list
     *
     */
    private class Node implements Serializable{
        private Task item;
        private Node previousElement;
        private Node nextElement;

        Node(Task item, Node previousElement, Node nextElement) {
            this.item = item;
            this.previousElement = previousElement;
            this.nextElement = nextElement;
        }

        Task getItem() {
            return item;
        }

        void setItem (Task item) { this.item = item; }

        void setNextElement(Node nextElement) {
            this.nextElement = nextElement;
        }

        Node getNextElement() { return nextElement; }

        void setPreviousElement(Node previousElement) { this.previousElement = previousElement; }

        Node getPreviousElement() { return previousElement; }

    }
}
