package DataLayer.TrackModel;

public class BeaconSignal 
{
    public String StationName;
    public boolean StationOnRight;
    public double Velocity;
    public double DistanceFromStation;
    
    public static final double DEFAULT_BEACON_VELOCITY = 15.28;
    public static final double DEFAULT_DISTANCE_FROM_STATION = 173.35;
    
    
    //Constructors
    
    public BeaconSignal()
    {
        this.StationName = "";
        this.Velocity = DEFAULT_BEACON_VELOCITY;
        this.StationOnRight = true;
        this.DistanceFromStation = DEFAULT_DISTANCE_FROM_STATION;
    }
    
    public BeaconSignal(String station, boolean stationOnRight) 
    {
        this.StationName = station;
        this.Velocity = DEFAULT_BEACON_VELOCITY;
        this.StationOnRight = stationOnRight;
        this.DistanceFromStation = DEFAULT_DISTANCE_FROM_STATION;
    }
    
    public BeaconSignal(String station, boolean stationOnRight, double distanceFromStation) 
    {
        this.StationName = station;
        this.Velocity = DEFAULT_BEACON_VELOCITY;
        this.StationOnRight = stationOnRight;
        this.DistanceFromStation = distanceFromStation;
    }
}
