package DataLayer.TrackModel;

public class BeaconSignal 
{
    public String StationName;
    public boolean StationOnRight;
    public double Velocity;
    
    public static final double DEFAULT_BEACON_VELOCITY = 15.28;
    
    
    //Constructors
    
    public BeaconSignal()
    {
        this.StationName = "";
        this.Velocity = DEFAULT_BEACON_VELOCITY;
        this.StationOnRight = true;
    }
    
    public BeaconSignal(String station, boolean stationOnRight) 
    {
        this.StationName = station;
        this.Velocity = DEFAULT_BEACON_VELOCITY;
        this.StationOnRight = stationOnRight;
    }
}
