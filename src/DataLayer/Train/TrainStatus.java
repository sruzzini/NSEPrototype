/******************************************************************************
 * 
 * TrainSatus class
 * 
 * Developed by AJility
 * April 2014
 *
 *****************************************************************************/

package DataLayer.Train;

public class TrainStatus 
{
    //Failures
    public static final int ENGINE_FAILURE = 1;
    public static final int SIGNAL_PICKUP_FAILURE = 3;
    public static final int E_BRAKE_FAILURE = 7;
    public static final int S_BRAKE_FAILURE = 13;
    
    public static final double EMPTY_TRAIN_MASS = 40.9; //empty train weight (metric Tons)
    public static final double ROOM_TEMP = 21.1; //70 degrees fahrenheit, in celsius
    
    private double velocity; // meters/sec
    private double mass; // metric tons
    private double temperature; // degrees C
    private int failure;
    private boolean sBrakeStatus;
    private boolean eBrakeStatus;
    private boolean passengerEBrakeRequest;
    private boolean lDoorStatus;
    private boolean rDoorStatus;
    private boolean extLightStatus;
    private boolean intLightStatus;
    private boolean heaterStatus;
    private String announcement;
    private String advertisement;
    
    public TrainStatus()
    {
        this.velocity = 0;
        this.mass = TrainStatus.EMPTY_TRAIN_MASS;
        this.temperature = TrainStatus.ROOM_TEMP;
        this.failure = 0;
        this.sBrakeStatus = false;
        this.eBrakeStatus = false;
        this.passengerEBrakeRequest = false;
        this.lDoorStatus = false;
        this.rDoorStatus = false;
        this.extLightStatus = false;
        this.intLightStatus = false;
        this.heaterStatus = false;
        this.announcement = "**No Announcement**";
        this.advertisement = null;
    }
    
    public TrainStatus(double velocity, double mass, double temperature)
    {
        this.velocity = velocity;
        this.mass = mass;
        this.temperature = temperature;
        this.failure = 0;
        this.sBrakeStatus = false;
        this.eBrakeStatus = false;
        this.passengerEBrakeRequest = false;
        this.lDoorStatus = false;
        this.rDoorStatus = false;
        this.extLightStatus = false;
        this.intLightStatus = false;
        this.heaterStatus = false;
        this.announcement = "**No Announcement**";
        this.advertisement = null;
    }
    
    public TrainStatus(double velocity, double mass, double temperature, int failure, boolean sBrake,
                       boolean eBrake, boolean pBrake, boolean lDoor, boolean rDoor, boolean extLight,
                       boolean intLight, boolean heater, String announcement, String advertisement)
    {
        this.velocity = velocity;
        this.mass = mass;
        this.temperature = temperature;
        this.failure = failure;
        this.sBrakeStatus = eBrake;
        this.eBrakeStatus = sBrake;
        this.passengerEBrakeRequest = pBrake;
        this.lDoorStatus = lDoor;
        this.rDoorStatus = rDoor;
        this.extLightStatus = extLight;
        this.intLightStatus = intLight;
        this.heaterStatus = heater;
        this.announcement = announcement;
        this.advertisement = advertisement;
    }
    
    
    //Getters
    public double getVelocity()
    {
        return this.velocity;
    }
    
    public double getMass()
    {
        return this.mass;
    }
    
    public double getTemperature()
    {
        return this.temperature;
    }
    
    public int getFailure()
    {
        return this.failure;
    }
    
    public boolean getEBrakeStatus()
    {
        return this.eBrakeStatus;
    }
    
    public boolean getSBrakeStatus()
    {
        return this.sBrakeStatus;
    }
    
    public boolean getPassengerBrakeRequest()
    {
        return this.passengerEBrakeRequest;
    }
    
    public boolean getLeftDoorStatus()
    {
        return this.lDoorStatus;
    }
    
    public boolean getRightDoorStatus()
    {
        return this.rDoorStatus;
    }
    
    public boolean getExteriorLightStatus()
    {
        return this.extLightStatus;
    }
    
    public boolean getInteriorLightStatus()
    {
        return this.intLightStatus;
    }
    
    public boolean getHeaterStatus()
    {
        return this.heaterStatus;
    }
    
    public String getAnnouncement()
    {
        String s = this.announcement;
        if (s == null)
        {
            s = "";
        }
        return s;
    }
    
    public String getAdvertisement()
    {
        String s = this.advertisement;
        if (s == null)
        {
            s = "";
        }
        return s;
    }
    
    
    //Setters
    public void setVelocity(double x)
    {
        this.velocity = x;
    }
    
    public void setMass(double x)
    {
        this.mass = x;
    }
    
    public void setTemperature(double x)
    {
        this.temperature = x;
    }
    
    public void setFailure(int x)
    {
        this.failure = x;
    }
    
    public void setEBrakeStatus(boolean x)
    {
        this.eBrakeStatus = x;
    }
    
    public void setSBrakeStatus(boolean x)
    {
        this.sBrakeStatus = x;
    }
    
    public void setPassengerBrakeRequest(boolean x)
    {
        this.passengerEBrakeRequest = x;
    }
    
    public void setExteriorLightStatus(boolean x)
    {
        this.extLightStatus = x;
    }
    
    public void setInteriorLightStatus(boolean x)
    {
        this.intLightStatus = x;
    }
    
    public void setLeftDoorStatus(boolean x)
    {
        this.lDoorStatus = x;
    }
    
    public void setRightDoorStatus(boolean x)
    {
        this.rDoorStatus = x;
    }
    
    public void setHeaterStatus(boolean x)
    {
        this.heaterStatus = x;
    }
    
    public void setAnnouncement(String x)
    {
        this.announcement = x;
    }
    
    public void setAdvertisement(String x)
    {
        this.advertisement = x;
    }
}
