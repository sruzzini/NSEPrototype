/******************************************************************************
 * 
 * TrackSignal class
 * 
 * Developed by AJility
 * April 2014
 * 
 * Contributers:
 *  Ryan Mertz
 *  Stephen T. Ruzzini
 *
 *****************************************************************************/

package DataLayer.TrackModel;

public class TrackSignal 
{
    private double velocityCommand; // meters/sec
    private int authority;
    private boolean isUnderground;
    private String nextDestination;
    private int gradient;
    
    //Constructors
    public TrackSignal()
    {
        this.velocityCommand = 0;
        this.authority = 0;
        this.isUnderground = false;
        this.nextDestination = null;
        this.gradient = 0;
    }
    
    public TrackSignal(double velocity, int authority, boolean isUnderground, String nextDestination)
    {
        this.velocityCommand = velocity;
        this.authority = authority;
        this.isUnderground = isUnderground;
        this.nextDestination = nextDestination;
        this.gradient = 0;
    }
    
    
    //Getters
    public double getVelocityCommand()
    {
        return this.velocityCommand;
    }
    
    public int getAuthority()
    {
        return this.authority;
    }
    
    public boolean getUndergroundStatus()
    {
        return this.isUnderground;
    }
    
    public String getNextDestination()
    {
        String s = "**Destination Not Specified**";
        if (this.nextDestination != null)
        {
            s = this.nextDestination;
        }
        return s;
    }
    
    
    //Setters
    public void setVelocityCommand(double x)
    {
        this.velocityCommand = x;
    }
    
    public void setAuthority(int x)
    {
        this.authority = x;
    }
    
    public void setUndergroundStatus(boolean x)
    {
        this.isUnderground = x;
    }
    
    public void setNextDestination(String x)
    {
        this.nextDestination = x;
    }
}
