package ua.edu.sumdu.j2se.YuraLampak.tasks.model;


/**
 * Class Task
 * for
 * programm <b>Task manager</b>
 *
 * @author YuraLampak
 * @version 1.0
 */


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Cloneable, Serializable {

    /**
     * storage title of task.
     */
    private String title;

    /**
     * storage time of not repeated task
     */
    private Date time;

    /**
     * storage initial time of repeated task.
     */
    private Date start;

    /**
     * storage end time of repeated task.
     */
    private Date end;

    /**
     * storage the interval through which the task is repeated.
     */
    private int interval;

    /**
     * variable for set task status.
     */
    private boolean active;

    /**
     * variable for check repeatability.
     */
    private boolean isRepeat;

    /**
     * Constructs not repeated task with the initial title and time execution.
     *
     * @param title set title of task
     * @param time  set time execution of task
     */
    public Task(String title, Date time) {
        this.title = title;
        this.time = time;
        this.isRepeat = false;
    }

    /**
     * Constructs repeated task with the initial time,
     * end time and interval to repeat
     *
     * @param title    set title of task
     * @param start    set time to begin this task
     * @param end      set time until task is active
     * @param interval set time to repeat task
     */
    public Task(String title, Date start, Date end, int interval) throws TaskException {
        if (interval < 0){
            throw new TaskException("interval is incorrect");
        }
        this.title = title;
        this.start = start;
        this.interval = interval;
        this.end = end;
        isRepeat = true;
    }

    /**
     * Returns the title of this task.
     *
     * @return the title of this task
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets out the title of this task.
     *
     * @param title take title of this task
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns <tt>true</tt> if task status is active.
     *
     * @return <tt>true</tt> if task status is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets out the status of this task.
     *
     * @param active take the status of this task
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns <tt>time</tt> if task is not repeated.
     *
     * @return <tt>time</tt> if task is repeated
     */
    public Date getTime() {
        return (isRepeated() ? start : time);
    }

    /**
     * Sets out time execution for not repeated task.
     * If task was repeated, it will be not repeated
     *
     * @param time take time execution for not repeated task
     */
    public void setTime(Date time) {
        if (isRepeated())
            this.isRepeat = false;
        this.time = time;
    }

    /**
     * Returns <tt>start</tt> time if task is repeated.
     *
     * @return <tt>start</tt> time if task is repeated
     */
    public Date getStartTime() {
        return (isRepeated() ? start : time);
    }

    /**
     * Returns <tt>end</tt> time if task is repeated.
     *
     * @return <tt>end</tt> time if task is repeated
     */
    public Date getEndTime() {
        return (isRepeated() ? end : time);
    }

    /**
     * Returns <tt>interval</tt> to repeate if task is repeated.
     *
     * @return <tt>end</tt> time to repeate if task is repeated
     */
    public int getRepeatInterval() {
        return (isRepeated() ? interval : 0);
    }

    /**
     * Sets out parametrs of repeated task if it is.
     *
     * @param start    take initial time of repeated task
     * @param end      take end time of repeated task
     * @param interval take time to repeat task
     */
    public void setTime(Date start, Date end, int interval) throws TaskException {
        if (isRepeated()) {
            if (interval < 0){
                throw new TaskException("interval is incorrect");
            }
            this.start = start;
            this.end = end;
            this.interval = interval;
            this.isRepeat = true;
        }
    }

    /**
     * Returns <tt>true</tt> if task is repeated.
     *
     * @return <tt>true</tt> if task is repeated.
     */
    public boolean isRepeated() {
        return isRepeat;
    }

    /**
     * Returns next time execution of task after <tt>current</tt> time point
     * for all task. If task is not active at the moment, returns null.
     *
     * @param current is a point after that returns next time to execution of task
     * @return next time to execution of task if it is active status
     */
    public Date nextTimeAfter(Date current) {
        if (active) {
            if (isRepeated()) {
                if (current.before(start)) {
                    return start;
                } else {
                    int intervalInMilliSec = interval * 1000;
                    Date curr = new Date();
                    curr.setTime(start.getTime() + intervalInMilliSec);
                    do {
                        if (current.before(curr))
                            return curr;
                        curr.setTime(curr.getTime() + intervalInMilliSec);
                    } while (curr.compareTo(end) <= 0);
                }
            } else if (current.before(time))
                return time;
        } return null;
    }

    /**
     * Overrides method to generate hashcode for each task.
     *
     * @return generics hashcode
     */
    @Override
    public int hashCode() {
        int coeff = 33;
        int result = 1;
        if (isRepeated()) {
            result = coeff * result + (int) start.getTime();
            result = coeff * result + (int) end.getTime();
            result = coeff * result + interval;
        } else result = coeff * result + (int) time.getTime();
        result = coeff * result + (isActive() ? 1 : 0);
        return result;
    }

    /**
     * This method compare two objects by conditions and fields.
     * @param obj is object which is comparing with
     * @return value <tt>true</tt> if they is compare and <tt>false</tt> if isn't.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (this.getClass() != obj.getClass())
            return false;
        if (getTitle().equals(((Task) obj).getTitle())) {
            if (getTime().equals(((Task) obj).getTime())) {
                if (isRepeated() == ((Task) obj).isRepeated()) {
                    if (getStartTime().equals(((Task) obj).getStartTime()) & getEndTime().equals(((Task) obj).getEndTime()) &
                            getRepeatInterval() == ((Task) obj).getRepeatInterval()) {
                        if (isActive() == ((Task) obj).isActive())
                            return true;
                    }
                } else return isActive() == ((Task) obj).isActive();
            }
        } return false;
    }

    /**
     * By this method configuring linear mapping of tasks.
     * Include a maximum information in a convenient form.
     *
     * @return general information about task
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        SimpleDateFormat form = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        buf.append("\n\n\tTask: ")
                .append(getTitle())
                .append((isRepeated() ? ("\n\tStart: " + form.format(getStartTime())
                        + "\tEnd: " + form.format(getEndTime())
                        + "\t\tInterval: " + getRepeatInterval()) :
                        ("\n\tTime to go: " + getTime())))
                .append("\n\tStatus: ")
                .append(isActive() ? " active" : " passive");
        return String.valueOf(buf);
    }

    /**
     * Method clone() is realize cloning tasks
     *
     * @return clone of current task
     */
    @Override
    public Task clone() {
        Task clone = null;
        try {
            clone = (Task) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("In method clone(): " + e.getMessage());
        } return clone;
    }
}

