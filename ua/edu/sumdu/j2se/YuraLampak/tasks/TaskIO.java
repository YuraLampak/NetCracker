package ua.edu.sumdu.j2se.YuraLampak.tasks;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;
import java.util.Iterator;


public class TaskIO {
    private static final int DAY = 86400;
    private static final int HOUR = 3600;
    private static final int MINUTE = 60;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    public static void write(TaskList tasks, OutputStream out) throws TaskException {
        try (DataOutputStream dos = new DataOutputStream(out)){
            dos.writeInt(tasks.size());
            Iterator<Task> itr = tasks.iterator();
            while (itr.hasNext()) {
                Task task = itr.next();
                dos.writeUTF(task.getTitle());
                dos.writeBoolean(task.isActive());
                dos.writeInt(task.getRepeatInterval());
                if (task.isRepeated()) {
                    dos.writeLong(task.getStartTime().getTime());
                    dos.writeLong(task.getEndTime().getTime());
                } else dos.writeLong(task.getTime().getTime());
            }
            dos.flush();
        } catch (IOException e) {
            throw new TaskException("IOException in method write(): " + e);
        }
    }

    public static void read(TaskList tasks, InputStream in) throws TaskException {
        try (DataInputStream dis = new DataInputStream(in)) {
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                String title = dis.readUTF();
                boolean active = dis.readBoolean();
                int interval = dis.readInt();
                if (interval > 0) {
                    long startTime = dis.readLong();
                    long endTime = dis.readLong();
                    Task task = new Task(title, new Date(startTime), new Date(endTime), interval);
                    task.setActive(active);
                    tasks.add(task);
                } else {
                    long time = dis.readLong();
                    Task task = new Task(title, new Date(time));
                    task.setActive(active);
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new TaskException("IOException in static method read(): " + e);
        }
    }

    public static void writeBinary(TaskList tasks, File file) throws TaskException {
        try (OutputStream out = new FileOutputStream(file)){
            write(tasks, out);
        } catch (FileNotFoundException e) {
            throw new TaskException("Exception in static method writeBinary(): " + e);
        } catch (IOException e){
            throw new TaskException("IOException in static method writeBinary(): " + e);
        }
    }

    public static void readBinary(TaskList tasks, File file) throws TaskException{
        try (InputStream in = new FileInputStream(file)){
            read(tasks, in);
        } catch (FileNotFoundException e) {
            throw new TaskException("Exception in static method readBinary(): " + e);
        } catch (IOException e){
            throw new TaskException("IOException in static method readBinary(): " + e);
        }
    }

    private static void intervalFormat(int seconds, PrintWriter pw) {
        int currentDay = seconds / DAY;
        int currentHour = seconds % DAY / HOUR;
        int currentMinute = seconds % HOUR / MINUTE;
        int currentSecond = seconds % MINUTE;
        if (currentDay != 0) {
            if (currentDay > 1) {
                pw.print(currentDay + " days");
            } else
                pw.print(currentDay + " day");
        }
        if (currentHour != 0) {
            if (currentDay > 0) pw.print(" ");
            if (currentHour > 1) {
                pw.print(currentHour + " hours");
            } else
                pw.print(currentHour + " hour");
        }
        if (currentMinute != 0) {
            if ((currentDay > 0) || (currentHour > 0)) pw.print(" ");
            if (currentMinute > 1) {
                pw.print(currentMinute + " minutes");
            } else
                pw.print(currentMinute + " minute");
        }
        if (currentSecond != 0) {
            if ((currentDay > 0) || (currentHour > 0) || (currentMinute > 0)) pw.print(" ");
            if (currentSecond > 1) {
                pw.print(currentSecond + " seconds");
            } else
                pw.print(currentSecond + " second");
        }
    }

    public static void write(TaskList tasks, Writer out) {
        PrintWriter pw = new PrintWriter(new BufferedWriter(out));
        Iterator<Task> itr = tasks.iterator();
        while (itr.hasNext()) {
            Task task = itr.next();
            String title = task.getTitle();
            pw.print("\"" + title + "\"");
            if (task.isRepeated()) {
                pw.print(" from [");
                pw.print(DATE_FORMAT.format(task.getStartTime()));
                pw.print("] to [");
                pw.print(DATE_FORMAT.format(task.getEndTime()));
                pw.print("] every [");
                intervalFormat(task.getRepeatInterval(), pw);
            } else {
                pw.print(" at [");
                pw.print(DATE_FORMAT.format(task.getTime()));
            }
            if (task.isActive()) {
                pw.print("]");
            } else {
                pw.print("] inactive");
            }
            if (itr.hasNext()) {
                pw.print(";\n");
            }
        }
        pw.print(".\n");
        pw.println("");
        pw.flush();
        pw.close();
    }

    public static void read(TaskList tasks, Reader in) throws TaskException {
        String line = "";
        try (BufferedReader buffer = new BufferedReader(in);){
            while (((line = buffer.readLine()) != null) && (buffer.ready())) {
                int startIndex = line.indexOf("\"");
                int endIndex = line.lastIndexOf("\"");
                String title = line.substring(startIndex + 1, endIndex);
                boolean active = line.indexOf("inactive", endIndex) <= 0;

                if (line.indexOf("at [", endIndex) > 0) {
                    Date time = readDate(line, "at [", endIndex);
                    Task task = new Task(title, new Date(time.getTime()));
                    task.setActive(active);
                    tasks.add(task);
                } else {
                    Date startTime = readDate(line, "from [", endIndex);
                    Date endTime = readDate(line, "to [", endIndex);

                    int startInterval = line.indexOf("every [", endIndex);
                    int endInterval = line.indexOf("]", startInterval);
                    String lineInterval = line.substring(startInterval, endInterval);

                    int repeatInterval = readInterval("day", lineInterval) * DAY;
                    repeatInterval += readInterval("hour", lineInterval) * HOUR;
                    repeatInterval += readInterval("minute", lineInterval) * MINUTE;
                    repeatInterval += readInterval("second", lineInterval);

                    Task task = new Task(title, new Date(startTime.getTime()), new Date(endTime.getTime()), repeatInterval);
                    task.setActive(active);
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new TaskException("IOException in static method readBinary(): " + e);
        }
    }

    private static Date readDate(String line, String s, int index) throws TaskException {
        int currentIndex = line.indexOf("]", line.indexOf(s, index));
        String inDate = line.substring(currentIndex - "yyyy-MM-dd HH:mm:ss:SSS".length(), currentIndex);
        Date date;
        try {
            date = DATE_FORMAT.parse(inDate);
        } catch (ParseException e) {
            throw new TaskException("Incorrect parsing in static method readDate(): " + e);
        } return date;
    }

    private static int readInterval(String current, String line) {
        if (line.indexOf(current) <= 0) {
            return 0;
        }
        int currentIndex = line.indexOf(current) - 2;
        String result = "";
        for (int i = currentIndex; i >= 0; i--) {
            char ch = line.charAt(i);
            if (!Character.isDigit(ch)) break;
            String temp = Character.toString(ch);
            result = temp.concat(result);
        }
        return Integer.parseInt(result);
    }

    public static void writeText(TaskList tasks, File file) throws TaskException {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            write(tasks, pw);
        } catch (IOException e) {
            throw new TaskException("IOException in static method writeText(): " + e);
        }
    }

    public static void readText(TaskList tasks, File file) throws TaskException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            read(tasks, br);
        } catch (IOException e) {
            throw new TaskException("IOException in static method readText(): " + e);
        }
    }
}
