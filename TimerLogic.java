package Main;// Timer Logic (Model)
// Start/stop timer function// Ask for productivity when stop is called
// Store task, start, stop, and productivity
// Find total time




//import java.util.TimerTask;
import java.util.Date;
//import java.util.Timer;

public class TimerLogic {
    public Date time;
    public long startTime;
    public long endTime;
    public long timeInterval;


    //public Timer myTimer = new Timer();
    //private int minutes;

    //public TimerTask task = new TimerTask(){
    //public void run(){
    //minutes++;

    //}
//System.currentTimeMillis()
    public void start(){
        startTime = System.currentTimeMillis();
        System.out.println("Start time is " + startTime);
    }

    public void end(){
        endTime = System.currentTimeMillis();
        timeInterval = endTime -startTime;
        System.out.println("End time is "+endTime);
        System.out.println("Time interval is  "+ timeInterval);

    }

}



