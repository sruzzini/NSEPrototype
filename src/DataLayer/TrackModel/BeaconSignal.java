package DataLayer.TrackModel;

public class BeaconSignal 
{
    public String StationName; // the station the beacon is for
    public boolean StationOnRight; // whether the station is on the left of the right of the track
    public double Velocity; // the velocity used in stoping distance calculation
    public double DistanceFromStation; // the distance the beacon is from the station
    
    public static final double DEFAULT_BEACON_VELOCITY = 15.28; // default beacon velocity value
    public static final double DEFAULT_DISTANCE_FROM_STATION = 173.35; // default distance from station
    
    // BeaconSignal() instantiates a new beacon with default values
    public BeaconSignal()
    {
        this.StationName = "";
        this.Velocity = DEFAULT_BEACON_VELOCITY;
        this.StationOnRight = true;
        this.DistanceFromStation = DEFAULT_DISTANCE_FROM_STATION;
    }
    
    // BeaconSignal(String station, boolean stationOnRight) instantiates a new beacon with specified values
    // Parameters:
    //     String station - the name of the station the beacon is for
    //     boolean stationOnRight - whether or not the station is on the right side of the track
    public BeaconSignal(String station, boolean stationOnRight) 
    {
        this.StationName = station;
        this.Velocity = DEFAULT_BEACON_VELOCITY;
        this.StationOnRight = stationOnRight;
        this.DistanceFromStation = DEFAULT_DISTANCE_FROM_STATION;
    }
    
    // BeaconSignal(String station, boolean stationOnRight) instantiates a new beacon with specified values
    // Parameters:
    //     String station - the name of the station the beacon is for
    //     boolean stationOnRight - whether or not the station is on the right side of the track
    //     double distanceFromStation - the distance the beacon is from the station
    public BeaconSignal(String station, boolean stationOnRight, double distanceFromStation) 
    {
        this.StationName = station;
        this.Velocity = DEFAULT_BEACON_VELOCITY;
        this.StationOnRight = stationOnRight;
        this.DistanceFromStation = distanceFromStation;
    }
}
