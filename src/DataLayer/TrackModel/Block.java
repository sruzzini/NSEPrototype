package DataLayer.TrackModel;

import DataLayer.EnumTypes.*;


public class Block {
    
    public int next;
    public int prev;
    private int authority;
    private BeaconSignal beacon;
    private double beaconLocation;
    private int blockID;
    private boolean closed;
    private double cumElev;
    private int destination;
    private double elevation;
    private int failureState;
    private double gradient;
    private boolean hasBeacon;
    private double length;
    private boolean light;
    private LightColor lightColor;
    private boolean occupied;
    private int occupyingTrainID;
    private boolean rrxing;
    private XingState rrxingState;
    private double speedLimit;
    private boolean station;
    private int stationID;
    private String stationString;
    private boolean tswitch;
    private int tswitchID;
    private boolean underground;
    private double velocity;
    
    public Block(int blockID, int next, int prev, double length, double speedLimit, double elevation, double cumElev,
            double gradient, boolean underground, boolean light, boolean rrxing,
            boolean station, boolean tswitch)
    {
        this.blockID = blockID;
        this.next = next;
        this.prev = prev;
        this.length = length;
        this.speedLimit = speedLimit;
        this.elevation = elevation;
        this.cumElev = cumElev;
        this.gradient = gradient;
        this.underground = underground;
        this.light = light;
        this.rrxing = rrxing;
        this.station = station;
        this.tswitch = tswitch;
        
        this.failureState = 0;
        this.closed = false;
        this.occupied = false;
        this.velocity = 0;
        this.authority = 0;
        this.destination = 0;
    }
    
    public int getAuthority() 
    {
        return authority;
    }
    
    public BeaconSignal getBeacon() 
    {
        return beacon;
    }
    
    public double getBeaconLocation()
    {
        return beaconLocation;
    }
    
    public int getBlockID() 
    {
        return blockID;
    }
    
    public double getCumElev() 
    {
        return cumElev;
    }
    
    public int getDestination() 
    {
        return destination;
    }
    
    public double getElevation() 
    {
        return elevation;
    }
    
    public int getFailureState() 
    {
        return failureState;
    }
    
    public double getGradient() 
    {
        return gradient;
    }
    
    public double getLength() 
    {
        return length;
    }
    
    public LightColor getLightColor() 
    {
        return lightColor;
    }
    
    public int getOccupyingTrainID() 
    {
        return occupyingTrainID;
    }
    
    public XingState getRRXingState() 
    {
        return rrxingState;
    }

    public double getSpeedLimit() 
    {
        return speedLimit;
    }

    public int getStationID() 
    {
        return stationID;
    }

    public String getStationString() {
        return stationString;
    }

    public int getTswitchID() 
    {
        return tswitchID;
    }
    
    public double getVelocity() 
    {
        return velocity;
    }

    public boolean hasABeacon() {
        return hasBeacon;
    }

    public boolean hasLight() 
    {
        return light;
    }
    
    public boolean hasRRXing() 
    {
        return rrxing;
    }
    
    public boolean hasStation() 
    {
        return station;
    }
    
    public boolean hasTswitch() 
    {
        return tswitch;
    }
    
    public boolean isClosed() 
    {
        return closed;
    }
    
    public boolean isOccupied() 
    {
        return occupied;
    }
    
    public boolean isUnderground() 
    {
        return underground;
    }
    
    public void setAuthority(int authority) 
    {
        this.authority = authority;
    }
    
    public void setBeacon(BeaconSignal beacon) 
    {
        this.beacon = beacon;
    }
    
    void setBeaconLocation(double distanceIn) 
    {
        this.beaconLocation = distanceIn;
    }
    
    public void setClosed(boolean closed) 
    {
        this.closed = closed;
    }
    
    public void setDestination(int destination) 
    {
        this.destination = destination;
    }
    
    public void setFailureState(int failureState) 
    {
        this.failureState = failureState;
    }

    public void setHasABeacon(boolean hasBeacon) {
        this.hasBeacon = hasBeacon;
    }
    
    public void setLightColor(LightColor lightColor) 
    {
        this.lightColor = lightColor;
    }

    public void setOccupied(boolean occupied) 
    {
        this.occupied = occupied;
    }

    public void setOccupyingTrainID(int occupyingTrainID) 
    {
        this.occupyingTrainID = occupyingTrainID;
    }

    public void setRRXingState(XingState rrxingState) 
    {
        this.rrxingState = rrxingState;
    }

    public void setStationID(int stationID) 
    {
        this.stationID = stationID;
    }

    public void setStationString(String stationString) {
        this.stationString = stationString;
    }

    public void setTswitchID(int tswitchID) 
    {
        this.tswitchID = tswitchID;
    }

    public void setVelocity(double velocity) 
    {
        this.velocity = velocity;
    }
}
