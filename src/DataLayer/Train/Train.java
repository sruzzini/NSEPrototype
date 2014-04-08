/******************************************************************************
 * 
 * Train class
 * 
 * Developed by AJility
 * April 2014
 * 
 * Contributers:
 *  Drew Winfield
 *  Stephen T. Ruzzini
 *
 *****************************************************************************/

package DataLayer.Train;
import DataLayer.TrackModel.*;
import DataLayer.Train.TrainModel.*;
import DataLayer.Train.TrainController.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Train implements Runnable
{
    private int iD;
    private int timeMultiplier;
    private Boolean isRunning;
    private TrainStatus status;
    private TrainCommand commands;
    private PhysicsInput physicsInput;
    private StateInput stateInput;
    private TrackSignal trackSignal;
    private BeaconSignal beaconSignal;
    private TrainController controller;
    private TrainModel model;
    
    
    //Contructors
    public Train()
    {
        this.isRunning = new Boolean(false);
        this.iD = 1;
        this.timeMultiplier = 1;
        this.status = new TrainStatus();
        this.commands = new TrainCommand();
        this.physicsInput = new PhysicsInput();
        this.stateInput = new StateInput();
        this.trackSignal = new TrackSignal();
        this.beaconSignal = null;
        this.controller = new TrainController(this.timeMultiplier, this.status, this.trackSignal, this.beaconSignal);
        this.model = new TrainModel(this.physicsInput, this.stateInput);
    }
    
    public Train(int id, Boolean running)
    {
        this.isRunning = running;
        this.iD = id;
        this.timeMultiplier = 1;
        this.status = new TrainStatus();
        this.commands = new TrainCommand();
        this.physicsInput = new PhysicsInput();
        this.stateInput = new StateInput();
        this.trackSignal = new TrackSignal();
        this.beaconSignal = null;
        this.controller = new TrainController(this.timeMultiplier, this.status, this.trackSignal, this.beaconSignal);
        this.model = new TrainModel(this.physicsInput, this.stateInput);
    }
    
    public Train(int id, int multiplier, Boolean running, TrainStatus status, TrackSignal signal, BeaconSignal beacon)
    {
        this.isRunning = running;
        this.iD = id;
        this.timeMultiplier = multiplier;
        this.status = status;
        this.commands = new TrainCommand();
        this.physicsInput = new PhysicsInput();
        this.stateInput = new StateInput();
        this.trackSignal = signal;
        this.beaconSignal = beacon;
        this.controller = new TrainController(this.timeMultiplier, this.status, this.trackSignal, this.beaconSignal);
        this.model = new TrainModel(this.physicsInput, this.stateInput);
    }
    
    
    //Getters and Setters
    public TrainStatus getTrainStatus()
    {
        return this.status;
    }
    
    public TrainCommand getTrainCommand()
    {
        return this.commands;
    }
    
    public void setTimeMultiplier(int i)
    {
        this.timeMultiplier = i;
    }
    
    public void setTrainStatus(TrainStatus s)
    {
        this.status = s;
    }
    
    public void setTrackSignal(TrackSignal s)
    {
        this.trackSignal = s;
    }
    
    public void setBeaconSignal(BeaconSignal s)
    {
        this.beaconSignal = s;
    }
    
    public void setIsRunning(Boolean isRunning)
    {
        this.isRunning = isRunning;
    }
    
    public double getDeltaX()
    {
        double tempDeltaX = 0;
        tempDeltaX =  physicsInput.Delta_x;
        physicsInput.Delta_x = 0;
        return tempDeltaX;
    }
    // Private method
    private void translatePhysicsCommand(TrainCommand c)
    {
        physicsInput.MotorPower = c.PowerCommand;
        physicsInput.SBrakeStatus = c.ServiceBrakeOn;
        physicsInput.EBrakeStatus = c.EmergencyBrakeOn;
        
        // do some additional work here not related to the Train Command
        physicsInput.Time_multiplier = timeMultiplier;
        // gradient
        // passengers
    }
    private void translateStateCommand(TrainCommand c)
    { 
        stateInput.RightDoors = c.RightDoorsOpen;
        stateInput.LeftDoors = c.LeftDoorsOpen;
        stateInput.IntLights = c.InteriorLightsOn;
        stateInput.ExtLights = c.ExteriorLightsOn;
        stateInput.Heater = c.HeaterOn;
        stateInput.Announcement = c.Announcement;
        //stateInput.advertisement = c.advertisement;
    }
    private void updateStatus()
    {
        status.setVelocity(physicsInput.Velocity); 
        status.setMass(model.getMass());
        status.setTemperature(stateInput.Temperature);
        status.setFailure(model.getFailureCode());
        status.setSBrakeStatus(model.getSBrakeStatus());
        status.setEBrakeStatus(model.getEBrakeStatus());
        status.setPassengerBrakeRequest(model.getPassengerEBrakeStatus());
        status.setLeftDoorStatus(model.getLeftDoorStatus());
        status.setRightDoorStatus(model.getRightDoorStatus());
        status.setExteriorLightStatus(model.getExtLightStatus());
        status.setInteriorLightStatus(model.getIntLightStatus());
        status.setHeaterStatus(model.getHeaterStatus());
        status.setAnnouncement(model.getAnnouncement());
        status.setAdvertisement(model.getAdvertisement());
    }
    
    
    //Public Methods
    public void run()
    {
        this.simulate();
    }
    
    public void simulate()
    {
        model.startPhysics();
        
        while(this.isRunning.booleanValue() == Boolean.TRUE)
        {
            this.commands = this.controller.GetTrainCommand();
            translateStateCommand(this.commands);
            model.updateState();
            
            translatePhysicsCommand(this.commands);
            try {
                Thread.sleep(100); //sleep for .1 seconds
            } catch (InterruptedException ex) {
                Logger.getLogger(Train.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateStatus();  // update status object
        }
    }
}
