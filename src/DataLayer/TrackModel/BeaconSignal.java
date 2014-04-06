package DataLayer.TrackModel;

public class BeaconSignal 
{
    private int beaconID;
    private String signal;
    
    public BeaconSignal(int beaconID, String signal) 
    {
        this.beaconID = beaconID;
        this.signal = signal;
    }

    public int getBeaconID() 
    {
        return beaconID;
    }

    public void setBeaconID(int beaconID) 
    {
        this.beaconID = beaconID;
    }

    public String getSignal() 
    {
        return signal;
    }

    public void setSignal(String signal) 
    {
        this.signal = signal;
    }
    
    
}
