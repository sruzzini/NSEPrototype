/******************************************************************************
 * 
 * SystemTime class
 * 
 * Developed by AJility
 * April 2014
 * 
 * Contributers:
 *  Stephen T. Ruzzini
 *
 *****************************************************************************/

package DataLayer;

//SytemTime class

import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemTime implements Runnable
{
    public static final int SECONDS_IN_DAY = 86400; //number of seconds in a day
    public static final int SECONDS_IN_HOUR = 3600; //number of seconds in an hour
    public static final int SECONDS_IN_MINUTE = 60; //number of seconds in a minute
    
    public int Hour; //24 hour time (0-23)
    public int Minute; //60 minutes in an hour (0-59)
    public int Second; //60 seconds in a minute (0-59)
    private int multiplier; //must be greater than 1
    
    public SystemTime()
    {
        this.Hour = 12;
        this.Minute = 0;
        this.Second = 0;
        this.multiplier = 1;
    }
    
    public SystemTime(int multiplier)
    {
        this.Hour = 12;
        this.Minute = 0;
        this.Second = 0;
        this.multiplier = multiplier;
    }
    
    public SystemTime(int hours, int minutes, int seconds, int multiplier)
    {
        this.Hour = hours;
        this.Minute = minutes;
        this.Second = seconds;
        this.multiplier = multiplier;
    }
    
    
    
    /* run() used to implement Runnable.  Calls "secondTick()" every second/multiplier
    */
    public void run()
    {
        while(true)
        {
            //Sleep for 1 second
            try 
            {
                if (this.multiplier > 0)
                {
                    Thread.sleep(1000 / this.multiplier);
                }
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(SystemTime.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (this.multiplier > 0)
            {
                this.secondTick();
            }
        }
    }
    
    /* Seconds() returns the number of seconds that have elapsed since 00 : 00 ; 00
     * Returns - int, number of seconds elapsed
    */
    public int getSeconds()
    {
        return ((SECONDS_IN_HOUR * this.Hour) + (SECONDS_IN_MINUTE * this.Minute) + this.Second);
    }
    
    /* SecondsSince(SystemTime t) gets the number of seconds between the current time and the parameter
     * Parameters:
     *     SystemTime t - time to calculate from
     * Returns - int, (this.Time - t.Time)
    */
    public int secondsSince(SystemTime t)
    {
        int tSeconds = t.getSeconds();
        int currSeconds = this.getSeconds();
        if (currSeconds < tSeconds)
        {
            currSeconds = currSeconds + SystemTime.SECONDS_IN_DAY;
        }
        return (currSeconds - tSeconds);
    }
    
    /* SetMultiplier(int m) sets the multiplier
     * Parameters:
     *     int m - new multiplier
    */
    public void setMultiplier(int m)
    {
        this.multiplier = m;
    }
    
    /* ToString() returns a string representation of the time in 24 hour time
     * Returns - String, represented in 24 hour time
    */
    public String toString()
    {
        String hour = "" + this.Hour;
        String minute = "" + this.Minute;
        String second = "" + this.Second;
        if (this.Hour < 10)
        {
            hour = "0" + hour;
        }
        if (this.Minute < 10)
        {
            minute = "0" + minute;
        }
        if (this.Second < 10)
        {
            second = "0" + second;
        }
        return (hour + " : " + minute + " : " + second);
    }
    
    /* secondTick() increments the system time by 1 second
    */
    private void secondTick()
    {
        this.Second += 1;
        if (this.Second == 60)
        {
            this.Second = 0;
            this.Minute += 1;
            if (this.Minute == 60)
            {
                this.Minute = 0;
                this.Hour += 1;
                if (this.Hour == 24)
                {
                    this.Hour = 0;
                }
            }
        }
    }
}
