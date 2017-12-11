package ua.edu.sumdu.j2se.YuraLampak.tasks;


/**
 * Class Task
 * for
 * programm <b>Task manager</b>
 *
 * @author YuraLampak
 * @version 1.0
 */


import java.io.Serializable;

public class Task implements Cloneable, Serializable {

    /**
     * storage title of task.
     */
    private String title;

    /**
     * storage time of not repeated task
     */
    private int time;

    /**
     * storage initial time of repeated task.
     */
    private int start;

    /**
     * storage end time of repeated task.
     */
    private int end;

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
     * @param time set time execution of task
     */
    public Task(String title, int time){
        if (time < 0){
            throw new IllegalArgumentException("The time has not be negative");
        } else {
            this.title = title;
            this.time = time;
            this.isRepeat = false;
        }
    }

    /**
     * Constructs repeated task with the initial time,
     * end time and interval to repeat
     *
     * @param title set title of task
     * @param start set time to begin this task
     * @param end set time until task is active
     * @param interval set time to repeat task
     */
    public Task(String title, int start, int end, int interval){
        this.title = title;
        if (start < 0 || end < 0){
            throw new IllegalArgumentException("start, end has not be negative");
        } else if (interval <= 0) {
            throw new IllegalArgumentException("interval must be above zero");
        } else if(end < start) {
            throw new IllegalArgumentException("The end has not be less than start");
        } else {
            this.start = start;
            this.interval = interval;
            this.end = end;
            isRepeat = true;
        }
    }

    /**
     * Returns the title of this task.
     *
     * @return the title of this task
     */
    public String getTitle(){ return title; }

    /**
     * Sets out the title of this task.
     *
     * @param title take title of this task
     */
    public void setTitle(String title){ this.title = title; }

    /**
     * Returns <tt>true</tt> if task status is active.
     *
     * @return <tt>true</tt> if task status is active
     */
    public boolean isActive(){
        return active;
    }

    /**
     * Sets out the status of this task.
     *
     * @param active take the status of this task
     */
    public void setActive(boolean active){
        this.active = active;
    }

    /**
     * Returns <tt>time</tt> if task is not repeated.
     *
     * @return <tt>time</tt> if task is repeated
     */
    public int getTime(){
        return (isRepeated() ? start : time);
    }

    /**
     * Sets out time execution for not repeated task.
     * If task was repeated, it will be not repeated
     *
     * @param time take time execution for not repeated task
     */
    public void setTime(int time){
        if (isRepeated())
            this.isRepeat = false;
        if (time < 0){
            throw new IllegalArgumentException("The time has not be negative");
        } else this.time = time;
    }

    /**
     * Returns <tt>start</tt> time if task is repeated.
     *
     * @return <tt>start</tt> time if task is repeated
     */
    public int getStartTime(){
        return (isRepeated() ?  start : time);
    }

    /**
     * Returns <tt>end</tt> time if task is repeated.
     *
     * @return <tt>end</tt> time if task is repeated
     */
    public int getEndTime(){
        return (isRepeated() ?  end : time);
    }

    /**
     * Returns <tt>interval</tt> to repeate if task is repeated.
     *
     * @return <tt>end</tt> time to repeate if task is repeated
     */
    public int getRepeatInterval(){
        return (isRepeated() ? interval : 0);
    }

    /**
     * Sets out parametrs of repeated task if it is.
     *
     * @param start take initial time of repeated task
     * @param end take end time of repeated task
     * @param interval take time to repeat task
     */
    public void setTime (int start, int end, int interval){
         if(!isRepeated()) {
             if (start < 0 || end < 0){
                 throw new IllegalArgumentException("start, end has not be negative");
             } else if (interval <= 0) {
                 throw new IllegalArgumentException("interval must be above zero");
             } else if(end < start) {
                 throw new IllegalArgumentException("The end has not be less than start");
             } else {
                 this.start = start;
                 this.end = end;
                 this.interval = interval;
             } this.isRepeat = true;
        }
    }

    /**
     * Returns <tt>true</tt> if task is repeated.
     *
     * @return <tt>true</tt> if task is repeated.
     */
    public boolean isRepeated(){
        return isRepeat;
    }

    /**
     * Returns next time execution of task after <tt>current</tt> time point
     * for all task. If task is not active at the moment, returns -1.
     *
     * @param current is a point after that returns next time to execution of task
     * @return next time to execution of task if it is active status
     */

    public int nextTimeAfter (int current) {
        if (active) {
            if (isRepeated()) {
                if (current < start) {
                    return start;
                } else {
                    int countCurrent = start + interval;
                    do {
                        if (current < countCurrent)
                            return countCurrent;
                        countCurrent += interval;
                    } while (countCurrent <= end);
                }
            } else if (current < time)
                return time;
        }
        return -1;
    }

    @Override
    public int hashCode() {
        int coeff = 33;
        int result = 1;
        result = coeff * result + time;
        if (isRepeated()) {
            result = coeff * result + start;
            result = coeff * result + end;
            result = coeff * result + interval;
        } else result = coeff * result;
        result = coeff * result + (isActive() ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (this.getClass() != obj.getClass())
            return false;

        if (getTitle().equals(((Task) obj).getTitle())) {
            if (getTime() == ((Task) obj).getTime()) {
                if (isRepeated() == ((Task) obj).isRepeated()){
                    if (getStartTime() == ((Task) obj).getStartTime() & getEndTime() == ((Task) obj).getEndTime() &
                        getRepeatInterval() == ((Task) obj).getRepeatInterval()) {
                        if (isActive() == ((Task) obj).isActive())
                            return true;
                    }
                } else return isActive() == ((Task) obj).isActive();
            }
        } return false;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("\n\n\tTask: ").append(getTitle()).append((isRepeated() ?
                ("\n\tStart: " + getStartTime() + "\tEnd: " + getEndTime() + "\t\tInterval: " + getRepeatInterval()) :
                ("\n\tTime to go: " + getTime()))).append("\n\tStatus: ").append(isActive() ? " active" : " passive");
        return String.valueOf(buf);
    }

    @Override
    public Task clone() {
        Task clone = null;
        try {
            clone = (Task) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

}

