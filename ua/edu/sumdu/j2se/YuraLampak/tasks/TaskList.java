package ua.edu.sumdu.j2se.YuraLampak.tasks;


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

abstract class TaskList implements Iterable<Task>, Cloneable, Serializable {


    abstract void add(Task task) throws NullPointerException;

    abstract boolean remove(Task task) throws NullPointerException;

    abstract Task getTask(int index);

    abstract int size();

//    /**
//     * Returns subset of tasks which includes in range. Range is limited
//     * by <tt>from</tt> and <tt>end</tt>.
//     *
//     * @param from is start of range
//     * @param to   is end of range
//     * @return subset of tasks which includes in range
//     */
    public TaskList incoming(int from, int to) {
        if (from < 0 || to < 0)
            throw new IllegalArgumentException("from and to has not be negative");
        if (from > to || from == to)
            throw new IllegalArgumentException("to must be above from");
        TaskList sublist = this.getClass() == ArrayTaskList.class ? new ArrayTaskList() : new LinkedTaskList();
        for (int i = 0; i < size(); i++){
            if (this.getTask(i).nextTimeAfter(from) != -1 & to >= this.getTask(i).nextTimeAfter(from)){
                sublist.add(getTask(i));
            }
        } return sublist;
    }

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
        TaskList clone;
        Object cloned = null;
        try {
            clone = (TaskList) super.clone();
            cloned = getDeepCloning(clone);
        } catch (Exception e) {
            e.printStackTrace();
        } return (TaskList)cloned;
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
