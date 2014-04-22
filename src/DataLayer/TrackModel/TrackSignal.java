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
    public int Authority; // authority of the block
    public boolean IsUnderground; // whether or not the block is underground
    public String NextDestination; // next station the train should be going to
    public double Gradient; // gradient of the current track block
    public int DeltaPassengers; // the change in the number of passangers on the train
    
    // getAuthority() returns the current set authority in the track signal
    // Returns - int, the current authority
    public int getAuthority()
    {
        return this.Authority;
    }
    
    // getNextDestination() returns the current set next destination in the track signal
    // Returns - String, the current next destination
    public String getNextDestination()
    {
        String s = "**Destination Not Specified**";
        if (this.NextDestination != null)
        {
            s = this.NextDestination;
        }
        return s;
    }
    
    // getUndergroundStatus() returns that current underground status in the track signal
    // Returns - boolean, the current underground status
    public boolean getUndergroundStatus()
    {
        return this.IsUnderground;
    }
    
    // getVelocityCommand() returns the current set velocity command in the track signal
    // Returns - double, the current velocity command
    public double getVelocityCommand()
    {
        return this.VelocityCommand;
    }
    
    // setAuthority(int x) updates the authority in the track signal
    // Parameters:
    //     int x - the new authority to be set
    public void setAuthority(int x)
    {
        this.Authority = x;
    }
    
    // setNextDestination(String x) updates the next destination in the track signal
    // Parameters:
    //     String s - the new next destination to be set
    public void setNextDestination(String x)
    {
        this.NextDestination = x;
    }
    
    // setUndergroundStatus(boolean x) updates the underground status in the track signal
    // Parameters:
    //     boolean x - the new underground status to be set
    public void setUndergroundStatus(boolean x)
    {
        this.IsUnderground = x;
    }
    
    // setVelocityCommand(double x) updates the velocity command in the track signal
    // Parameters:
    //     double x - the new velocity command to be set
    public void setVelocityCommand(double x)
    {
        this.VelocityCommand = x;
    }
    
    // TrackSignal() instantiates a new TrackSignal object with default values
    public TrackSignal()
    {
        this.VelocityCommand = 0;
        this.Authority = 0;
        this.IsUnderground = false;
        this.NextDestination = null;
        this.Gradient = 0;
    }
    
    // TrackSignal() instantiates a new TrackSignal object with parameterized values
    // Parameters:
    //     double velocity - the commanded velocity to be set to the tracksignal
    //     int authority - the commanded authority to be set to the tracksignal
    //     boolean isUnderground - whether or not the track associated with this track signal is underground
    //     String nextDestination - the name of the next station the train should be going to
    public TrackSignal(double velocity, int authority, boolean isUnderground, String nextDestination)
    {
        this.VelocityCommand = velocity;
        this.Authority = authority;
        this.IsUnderground = isUnderground;
        this.NextDestination = nextDestination;
        this.Gradient = 0;
    }
    
    // TrackSignal() instantiates a new TrackSignal object with parameterized values
    // Parameters:
    //     double velocity - the commanded velocity to be set to the tracksignal
    //     int authority - the commanded authority to be set to the tracksignal
    //     boolean isUnderground - whether or not the track associated with this track signal is underground
    //     String nextDestination - the name of the next station the train should be going to
    //     double gradient - the grade of the section of track associated with the tracksignal
    public TrackSignal(double velocity, int authority, boolean isUnderground, String nextDestination, double gradient)
    {
        this.VelocityCommand = velocity;
        this.Authority = authority;
        this.IsUnderground = isUnderground;
        this.NextDestination = nextDestination;
        this.Gradient = gradient;
    }
}
