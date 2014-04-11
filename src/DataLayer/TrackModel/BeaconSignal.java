package DataLayer.TrackModel;

public class BeaconSignal 
{
    public String StationName;
    public boolean StationOnRight;
    public double Velocity;
    
    
    //Constructors
    
    public BeaconSignal()
    {
        this.StationName = "";
        this.Velocity = 0;
        this.StationOnRight = true;
    }
    
    public BeaconSignal(String station, double velocity, boolean stationOnRight) 
    {
        this.StationName = station;
        this.Velocity = velocity;
        this.StationOnRight = stationOnRight;
    }
}
