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
    public TrainController controller;
    public TrainModel model;
    private double lastDeltaX;
    
    
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
        lastDeltaX = 0;
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
        lastDeltaX = 0;
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
        lastDeltaX = 0;
    }
    
    public TrackSignal getTrackSignal()
    {
        return this.trackSignal;
    }
    
    //Public methods
    public double GetDeltaX()
    {
        double tempDeltaX = 0;
        tempDeltaX =  physicsInput.Delta_x - lastDeltaX;
        lastDeltaX = physicsInput.Delta_x;
        return tempDeltaX;
    }
    public double getDeltaX()
    {
        return physicsInput.Delta_x;
    }
    
    public TrainCommand GetTrainCommand()
    {
        return this.commands;
    }
    
    public TrainStatus GetTrainStatus()
    {
        return this.status;
    }
    
    public void run()
    {
        this.Simulate();
    }
    
    public void SetBeaconSignal(BeaconSignal s)
    {
        this.beaconSignal = s;
        this.controller.SetBeaconSignal(this.beaconSignal);
    }
    
    public void SetIsRunning(Boolean isRunning)
    {
        this.isRunning = isRunning;
    }
    
    public void SetTimeMultiplier(int i)
    {
        this.timeMultiplier = i;
        this.controller.SetTimeMultiplier(this.timeMultiplier);
    }
    
    public void SetTrackSignal(TrackSignal s)
    {
        this.trackSignal = s;
        this.controller.SetTrackSignal(this.trackSignal);
        // gradient
        physicsInput.Gradient = trackSignal.Gradient;
        // passengers
    }
    
    public void SetTrainStatus(TrainStatus s)
    {
        this.status = s;
        this.controller.SetTrainStatus(this.status);
    }
    
    public void Simulate()
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
    
    public String ToString()
    {
        return "Train " + Integer.toString(this.iD);
    }
    
    
    // Private method
    private void translatePhysicsCommand(TrainCommand c)
    {
        physicsInput.MotorPower = c.PowerCommand;
        physicsInput.SBrakeStatus = c.ServiceBrakeOn;
        physicsInput.EBrakeStatus = c.EmergencyBrakeOn;
        
        // do some additional work here not related to the Train Command
        physicsInput.Time_multiplier = timeMultiplier;
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
        status.SetVelocity(physicsInput.Velocity); 
        status.SetMass(model.getMass());
        status.SetTemperature(stateInput.Temperature);
        status.SetFailure(model.getFailureCode());
        status.SetSBrakeStatus(model.getSBrakeStatus());
        status.SetEBrakeStatus(model.getEBrakeStatus());
        status.SetPassengerBrakeRequest(model.getPassengerEBrakeStatus());
        status.SetLeftDoorStatus(model.getLeftDoorStatus());
        status.SetRightDoorStatus(model.getRightDoorStatus());
        status.SetExteriorLightStatus(model.getExtLightStatus());
        status.SetInteriorLightStatus(model.getIntLightStatus());
        status.SetHeaterStatus(model.getHeaterStatus());
        status.SetAnnouncement(model.getAnnouncement());
        status.SetAdvertisement(model.getAdvertisement());
    }
}
