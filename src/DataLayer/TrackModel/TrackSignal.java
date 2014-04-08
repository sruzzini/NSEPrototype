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
    public double VelocityCommand; // meters/sec
    public int Authority;
    public boolean IsUnderground;
    public String NextDestination;
    public double Gradient;
    public int DeltaPassengers;
    
    //Constructors
    public TrackSignal()
    {
        this.VelocityCommand = 0;
        this.Authority = 0;
        this.IsUnderground = false;
        this.NextDestination = null;
        this.Gradient = 0;
    }
    
    public TrackSignal(double velocity, int authority, boolean isUnderground, String nextDestination)
    {
        this.VelocityCommand = velocity;
        this.Authority = authority;
        this.IsUnderground = isUnderground;
        this.NextDestination = nextDestination;
        this.Gradient = 0;
    }
    
    public TrackSignal(double velocity, int authority, boolean isUnderground, String nextDestination, double gradient)
    {
        this.VelocityCommand = velocity;
        this.Authority = authority;
        this.IsUnderground = isUnderground;
        this.NextDestination = nextDestination;
        this.Gradient = gradient;
    }
    
    
    //Getters
    public double getVelocityCommand()
    {
        return this.VelocityCommand;
    }
    
    public int getAuthority()
    {
        return this.Authority;
    }
    
    public boolean getUndergroundStatus()
    {
        return this.IsUnderground;
    }
    
    public String getNextDestination()
    {
        String s = "**Destination Not Specified**";
        if (this.NextDestination != null)
        {
            s = this.NextDestination;
        }
        return s;
    }
    
    
    //Setters
    public void setVelocityCommand(double x)
    {
        this.VelocityCommand = x;
    }
    
    public void setAuthority(int x)
    {
        this.Authority = x;
    }
    
    public void setUndergroundStatus(boolean x)
    {
        this.IsUnderground = x;
    }
    
    public void setNextDestination(String x)
    {
        this.NextDestination = x;
    }
}
