/******************************************************************************
 * 
 * TrainStatus class
 * 
 * Developed by AJility
 * April 2014
 * 
 * Contributers:
 *   Stephen T. Ruzzini
 *
 *****************************************************************************/

package DataLayer.Train;

public class TrainStatus 
{
    //Failures
    public static final int ENGINE_FAILURE = 1; //engine failure number
    public static final int SIGNAL_PICKUP_FAILURE = 3; //signal pickup failure number
    public static final int E_BRAKE_FAILURE = 7; //emergency brake failure number
    public static final int S_BRAKE_FAILURE = 13; //service brake failure number
    
    public static final double EMPTY_TRAIN_MASS = 40.9; //empty train weight (metric Tons)
    public static final double MAX_TRAIN_MASS = 56.7; //max train weight
    public static final double ROOM_TEMP = 21.1; //70 degrees fahrenheit, in celsius
    
    private double velocity; // meters/sec
    private double mass; // metric tons
    private double temperature; // degrees C
    private int failure; //number representing which failure(s) it has
    private boolean sBrakeStatus; //status of the emergency brake
    private boolean eBrakeStatus; //status of the service brake
    private boolean passengerEBrakeRequest; //status of the passenger emergency brake request
    private boolean lDoorStatus; //status of the left doors
    private boolean rDoorStatus; //status of the right doors
    private boolean extLightStatus; //status of the exterior lights
    private boolean intLightStatus; //status of the interior lights
    private boolean heaterStatus; //status of the heater
    private String announcement; //current train annoncement
    private String advertisement; //current train advertisement
    
    //Constructors
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
    
    
    /* GetAnouncement() returns the anouncement of the Train
     * Returns - String, anouncement
    */
    public String GetAnnouncement()
    {
        String s = this.announcement;
        if (s == null)
        {
            s = "";
        }
        return s;
    }
    
    /* GetAdvertisement() returns the advertisement of the Train
     * Returns - String, advertisement
    */
    public String GetAdvertisement()
    {
        String s = this.advertisement;
        if (s == null)
        {
            s = "";
        }
        return s;
    }
    
    /* GetEBrakeStatus() returns the status of the emergency brake
     * Returns - boolean, true if engaged, false if disengaged
    */
    public boolean GetEBrakeStatus()
    {
        return this.eBrakeStatus;
    }
    
    /* GetExteriorLightStatus() returns the status of the exterior lights
     * Returns - boolean, true if on, false if off
    */
    public boolean GetExteriorLightStatus()
    {
        return this.extLightStatus;
    }
    
    /* GetFailure() returns the failure of the train
     * Returns - int, number of the failure
    */
    public int GetFailure()
    {
        return this.failure;
    }
    
    /* GetHeaterStatus() returns the status of the Train's heater
     * Returns - boolean, true if on, false if off
    */
    public boolean GetHeaterStatus()
    {
        return this.heaterStatus;
    }
    
    /* GetInteriorLightStatus() returns the status of the interior lights
     * Returns - boolean, true if on, false if off
    */
    public boolean GetInteriorLightStatus()
    {
        return this.intLightStatus;
    }
    
    /* GetLeftDoorStatus() reutrns the status of the left doors
     * Returns - boolean, true if open, false if closed
    */
    public boolean GetLeftDoorStatus()
    {
        return this.lDoorStatus;
    }
    
    /* GetMass() returns the mass of the train including passengers
     * Returns - double 
    */
    public double GetMass()
    {
        return this.mass;
    }
    
    /* GetPassengerBrakeRequest() returns the status of the passegner brake request
     * Returns - boolean, true if brake requested, false if not
    */
    public boolean GetPassengerBrakeRequest()
    {
        return this.passengerEBrakeRequest;
    }
    
    /* GetRightDoorStatus() returns the status of the right doors
     * Returns - boolean, true if open, false if closed
    */
    public boolean GetRightDoorStatus()
    {
        return this.rDoorStatus;
    }
    
    /* GetSBrakeStatus() gets the status of the service brake
     * Returns - boolean, ture if engaged, fals eif disengaged
    */
    public boolean GetSBrakeStatus()
    {
        return this.sBrakeStatus;
    }
    
    /* GetTemperature() gets the temperature inside train
     * Returns - double (in celcius)
    */
    public double GetTemperature()
    {
        return this.temperature;
    }
    
    /* GetVelocity() gets the current velocity of the train
     * Returns - double
    */
    public double GetVelocity()
    {
        return this.velocity;
    }
    
    /* SetAnnouncement(String x) sets the anouncment of the train
     * Parementers:
     *   String x - anouncement to set
    */
    public void SetAnnouncement(String x)
    {
        this.announcement = x;
    }
    
    /* SetAdvertisement(String x) sets the advertisement of the train
     * Parementers:
     *   String x - advertisement to set
    */
    public void SetAdvertisement(String x)
    {
        this.advertisement = x;
    }
    
    /* setEBrakeStatus(boolean x) sets the emergency brake status
     * Parementers:
     *   boolean x - brake status
    */
    public void SetEBrakeStatus(boolean x)
    {
        this.eBrakeStatus = x;
    }
    
    /* SetExteriorLightStatus(boolean x) sets the status of the exterior lights
     * Parementers:
     *   boolean x - light status
    */
    public void SetExteriorLightStatus(boolean x)
    {
        this.extLightStatus = x;
    }
    
    /* SetFailure(int x) sets the failure of the train
     * Parementers:
     *   int x - failure number
    */
    public void SetFailure(int x)
    {
        this.failure = x;
    }
    
    /* SetHeaterStuats(boolean x) sets the status of the heater
     * Parementers:
     *   boolean x - heater status
    */
    public void SetHeaterStatus(boolean x)
    {
        this.heaterStatus = x;
    }
    
    /* SetInteriorLightStatus(boolean x) sets the status of the interior lights
     * Parementers:
     *   boolean x - light status
    */
    public void SetInteriorLightStatus(boolean x)
    {
        this.intLightStatus = x;
    }
    
    /* SetLeftDoorStatus(boolean x) sets the status of the left doors
     * Parementers:
     *   boolean x - door status
    */
    public void SetLeftDoorStatus(boolean x)
    {
        this.lDoorStatus = x;
    }
    
    /* SetMass(double x) sets the mass of the train
     * Parementers:
     *   double x - train mass
    */
    public void SetMass(double x)
    {
        this.mass = x;
    }
    
    /* SetPassengerBrakeRequest(boolean x) sets the status of the passenger brake request
     * Parementers:
     *   boolean x - request status
    */
    public void SetPassengerBrakeRequest(boolean x)
    {
        this.passengerEBrakeRequest = x;
    }
    
    /* SetSBrakeStatus(boolean x) sets the status of the service brake
     * Parementers:
     *   boolean x - brake status
    */
    public void SetSBrakeStatus(boolean x)
    {
        this.sBrakeStatus = x;
    }
    
    /* SetRightDoorStatus(boolean x) sets the status of the right doors
     * Parementers:
     *   boolean x - door status
    */
    public void SetRightDoorStatus(boolean x)
    {
        this.rDoorStatus = x;
    }
    
    /* SetTemperature(double x) sets the temperature inside the train
     * Parementers:
     *   double x - temperature (in celcius)
    */
    public void SetTemperature(double x)
    {
        this.temperature = x;
    }
    
    /* SetVelocity(double x) sets the velocity of the train
     * Parementers:
     *   double x - velocity (meter/sec.)
    */
    public void SetVelocity(double x)
    {
        this.velocity = x;
    }
}
