package com.yura.lampak.model;


import java.io.*;
import java.util.Iterator;


/**
 * Class TaskList
 * for
 * programm <b>Task manager</b>
 *
 * @author YuraLampak
 * @version 1.0
 */

public abstract class TaskList implements Iterable<Task>, Cloneable, Serializable {


    public abstract void add(Task task) throws NullPointerException;

    public abstract boolean remove(Task task) throws NullPointerException;

    public abstract Task getTask(int index);

    public abstract int size();

    @Override
    public Iterator<Task> iterator(){
        return new Iterator<Task>(){

            private int counter = 0;
            private int lastTaskRet = -1;

            @Override
            public boolean hasNext() { return (counter != size()); }

            @Override
            public Task next() {
                if (!hasNext()) {
                    return null;
                } else {
                    int i = counter;
                    Task next = getTask(i);
                    lastTaskRet = i;
                    counter++;
                    return next;
                }
            }

            @Override
            public void remove() {
                if (lastTaskRet < 0)
                    throw new IllegalStateException();
                TaskList.this.remove(TaskList.this.getTask(lastTaskRet));
                if (lastTaskRet < counter)
                    counter--;
                lastTaskRet = -1;
            }
        };
    }

    @Override
    public int hashCode() {
        int coeff = 31;
        int result = 1;
        for (int i = 0; i < size(); i++) {
            result = coeff * result + getTask(i).hashCode();
        }
        result = coeff * result + size();
        return result;
    }

    @Override
    public boolean equals(Object list) {
        if (list == null)
            return false;
        if (this == list)
            return true;
        if (this.getClass() != list.getClass())
            return false;
        if (this.hashCode() != list.hashCode())
            return false;

        TaskList l = (TaskList) list;
        if (size() == (l.size())){
            for (int i = 0; i < size(); i++){
                if (!(getTask(i)).equals(l.getTask(i))) {
                    return false;
                }
            }
        } return true;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        Iterator itr = iterator();
        buf.append("\nTaskList: ");
        while (itr.hasNext()){
            buf.append(itr.next());
        } return String.valueOf(buf);
    }

    @Override
    public TaskList clone() {
        TaskList clone = null;
        try {
            clone = (TaskList) super.clone();
            clone = getDeepCloning(this);
        } catch (Exception e) {
            e.printStackTrace();
        } return clone;
    }

    private TaskList getDeepCloning(Object obj) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (TaskList) ois.readObject();
    }
}
