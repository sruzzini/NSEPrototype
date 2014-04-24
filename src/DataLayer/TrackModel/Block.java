package DataLayer.TrackModel;

import DataLayer.EnumTypes.*;


public class Block {
    
    public int Next; // the next block number from this one
    public int Prev; // the previous block number from this one
    private int authority; // the authority set on the block
    private BeaconSignal beacon; // the beacon signal on the block
    private double beaconLocation; // the location of the beacon
    private boolean beaconSent; // prevents the beacon from being sent to a train more than once
    private int blockID; // the number of this block
    private boolean closed; // whether or not the block is closed
    private double cumElev; // the cumulative elevation of the block
    private int destination; // the current set destination of a train on this block
    private double elevation; // the elevation from the last block
    private int failureState; // the state of the block: normal, power failure, broken rail, signal failure
    private double gradient; // the grade of the block
    private boolean hasBeacon; // whether or not the block has a beacon
    private double length; // the length of the block
    private boolean light; // whether or not the block has a light
    private LightColor lightColor; // the color the light is set to if there is one
    private boolean occupied; // whether or not the block is occupied
    private int occupyingTrainID; // the ID of the train occupying the block
    private boolean rrxing; // whether or not the block has a railroad crossing
    private XingState rrxingState; // the state of the railroad crossing if there is one
    private double speedLimit; // the speed limit for the block
    private boolean station; // whether or not the block has a station
    private int stationID; // the numeric ID of the station
    private String stationString; // the string representing the name of the station
    private boolean tswitch; // whether or not there is a switch on the block
    private int tswitchID; // the ID of the switch if there is one
    private boolean underground; // whether or not the block is underground
    private double velocity; // the current set velocity of the block
    
    // Block(int blockID, int next, int prev, double length, double speedLimit, double elevation, double cumElev,
    //         double gradient, boolean underground, boolean light, boolean rrxing,
    //         boolean station, boolean tswitch) creates a new instance of block with the specified attributes
    // Parameters:
    //     int blockID - the number of the block
    //     int next - the next block number
    //     int prev - the previous block number
    //     double length - length of the block
    //     double speedLimit - the speed limit in the block
    //     double elevation - the elevation of the block relative to the previous block
    //     double cumElev - the cumulative elevation of the block
    //     double gradient - the grade of the block
    //     boolean underground - whether or not the block is underground
    //     boolean light - whether or not the block has a light
    //     boolean rrxing - whether or not the block has a railroad crossing
    //     boolean station - whether or not the block has a station
    //     boolean tswitch - whether or not the block connects to a switch
    public Block(int blockID, int next, int prev, double length, double speedLimit, double elevation, double cumElev,
            double gradient, boolean underground, boolean light, boolean rrxing,
            boolean station, boolean tswitch)
    {
        this.blockID = blockID;
        this.Next = next;
        this.Prev = prev;
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
        this.beaconSent = false;
    }
    
    // copy() returns a new instance of block with attributes that match the 
    // attributes of the calling block
    // Returns - Block, a new Block instance
    public Block copy()
    {
        Block b = new Block(this.getBlockID(), this.Next, this.Prev, this.getLength(), this.getSpeedLimit(), this.getElevation(),
            this.getCumElev(), this.getGradient(), this.isUnderground(), this.hasLight(), this.hasRRXing(), this.hasStation(), this.hasTswitch());
    
        b.setAuthority(this.authority);
        b.setBeacon(this.beacon);
        b.setBeaconLocation(this.beaconLocation);
        b.setClosed(this.closed);
        b.setDestination(this.destination);
        b.setFailureState(this.failureState);
        b.setHasABeacon(this.hasBeacon);
        b.setLightColor(this.lightColor);
        b.setOccupied(this.occupied);
        b.setOccupyingTrainID(this.occupyingTrainID);
        b.setRRXingState(this.rrxingState);
        b.setStationID(this.stationID);
        b.setStationString(this.stationString);
        b.setTswitchID(this.tswitchID);
        b.setVelocity(this.velocity);
        b.setBeaconSent(this.beaconSent);
        
        return b;
    }
    
    // getAuthority() returns the authority set in the block
    // Returns - int, authority
    public int getAuthority() 
    {
        return authority;
    }
    
    // getBeacon() returns the Beacon in the block if there is one
    // Returns - BeaconSignal, beacon
    public BeaconSignal getBeacon() 
    {
        return beacon;
    }
    
    // getBeaconLocation() returns the location of the beacon in the block if there is one
    // Returns - double, the location of the beacon
    public double getBeaconLocation()
    {
        return beaconLocation;
    }
    
    // getBeaconSent() returns whether or not the beacon has been sent to a train
    // Returns - boolean, whether or not the beacon has been sent
    public boolean getBeaconSent()
    {
        return beaconSent;
    }
    
    // getBlockID() returns the block number
    // Returns - int, block number
    public int getBlockID() 
    {
        return blockID;
    }
    
    // getCumElev() returns the cumulative elevation of the block
    // Returns - double, the cumulative elevation
    public double getCumElev() 
    {
        return cumElev;
    }
    
    // getDestination() returns the destination a train on this block should eb going to
    // Returns - int, the number of that set next destination block
    public int getDestination() 
    {
        return destination;
    }
    
    // getElevation() returns the elevation relative to the previous block
    // Returns - double, the elevation
    public double getElevation() 
    {
        return elevation;
    }
    
    // getFailureState() returns the current Failure State of the block
    // Returns - int, the current failute state
    public int getFailureState() 
    {
        return failureState;
    }
    
    // getGradient() returns the gradient of the block
    // Returns - double, the gradient
    public double getGradient() 
    {
        return gradient;
    }
    
    // getLength() returns the length of the block
    // Returns - double, the length
    public double getLength() 
    {
        return length;
    }
    
    // getLightColor() returns the curruent state of the light color on the block
    // Returns - LightColor, the light color
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
    
    public void setBeaconLocation(double distanceIn) 
    {
        this.beaconLocation = distanceIn;
    }
    
    public void setBeaconSent(boolean b)
    {
        this.beaconSent = b;
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

    public void setHasABeacon(boolean hasBeacon) 
    {
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

    public void setStationString(String stationString) 
    {
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
