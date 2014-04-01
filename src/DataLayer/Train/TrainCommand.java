/******************************************************************************
 * 
 * TrainCommand class
 * 
 * Developed by AJility
 * April 2014
 * 
 * Contributers:
 *  Stephen T. Ruzzini
 *
 *****************************************************************************/

package DataLayer.Train;

public class TrainCommand 
{
    public double PowerCommand;
    public boolean ServiceBrakeOn;
    public boolean EmergencyBrakeOn;
    public boolean LeftDoorsOpen;
    public boolean RightDoorsOpen;
    public boolean ExteriorLightsOn;
    public boolean InteriorLightsOn;
    public boolean HeaterOn;
    public String Announcement;
    
    
    public TrainCommand()
    {
        this.PowerCommand = 0; //Watts
        this.ServiceBrakeOn = false;
        this.EmergencyBrakeOn = false;
        this.LeftDoorsOpen = false;
        this.RightDoorsOpen = false;
        this.ExteriorLightsOn = false;
        this.InteriorLightsOn = false;
        this.HeaterOn = false;
        this.Announcement = null;
    }
    
    public TrainCommand(double power, boolean sBrake, boolean eBrake, boolean lDoor,
                                   boolean rDoor, boolean eLights, boolean iLights, boolean heater,
                                   String announcement)
    {
        this.PowerCommand = power;
        this.ServiceBrakeOn = eBrake;
        this.EmergencyBrakeOn = sBrake;
        this.LeftDoorsOpen = lDoor;
        this.RightDoorsOpen = rDoor;
        this.ExteriorLightsOn = eLights;
        this.InteriorLightsOn = iLights;
        this.HeaterOn = heater;
        this.Announcement = announcement;
    }
}
