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
    //Class Variables
    public TrainController Controller; //controller that sends commands
    public TrainModel Model; //model that acts on commands sent by controller
    private BeaconSignal beaconSignal; //signal from the last beacon passed
    private TrainCommand commands; //commands sent to the train by controller
    private int iD; //ID of a train
    private Boolean isRunning; //set to true if it is running
    private double lastDeltaX; //meters travelled since last request
    private PhysicsInput physicsInput; //physics commands
    private StateInput stateInput; //state commands
    private TrainStatus status; //current status of the train (calculated by model)
    private int timeMultiplier; //time multiplier of a train
    private TrackSignal trackSignal; //signal from the current track block
    
    
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
        this.Controller = new TrainController(this.timeMultiplier, this.status, this.trackSignal, this.beaconSignal);
        this.Model = new TrainModel(this.physicsInput, this.stateInput);
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
        this.Controller = new TrainController(this.timeMultiplier, this.status, this.trackSignal, this.beaconSignal);
        this.Model = new TrainModel(this.physicsInput, this.stateInput);
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
        this.Controller = new TrainController(this.timeMultiplier, this.status, this.trackSignal, this.beaconSignal);
        this.Model = new TrainModel(this.physicsInput, this.stateInput);
        lastDeltaX = 0;
    }
    
    
    //Public methods
    /* GetDeltaX() gets distance travelled since last request
     * Returns - double, meters travelled
    */
    public double GetDeltaX()
    {
        double tempDeltaX = 0;
        tempDeltaX =  physicsInput.Delta_x - lastDeltaX;
        lastDeltaX = physicsInput.Delta_x;
        return tempDeltaX;
    }
    
    /* GetTrainCommand() gets a train command from the controller
     * Returns - TrainCommand object
    */
    public TrainCommand GetTrainCommand()
    {
        return this.commands;
    }
    
    /* GetTrainStatus() gets the status of the train from the model
     * Returns - TrainStatus object
    */
    public TrainStatus GetTrainStatus()
    {
        return this.status;
    }
    
    /* run() used to implement Runnable.  Calls "Simulate()"
    */
    public void run()
    {
        this.Simulate();
    }
    
    /* SetBeaconSignal(BeaconSignal s) sets the beacon of the train
     * Parameters:
     *     BeaconSignal s - sets train's beacon signal to "s"
    */
    public void SetBeaconSignal(BeaconSignal s)
    {
        this.beaconSignal = s;
        this.Controller.SetBeaconSignal(this.beaconSignal);
    }
    
    /* SetIsRunning(Boolean isRunning) sets isRunning of the train
     * Parameters:
     *     Boolean isRunning - sets train's isRunning to "isRunning"
    */
    public void SetIsRunning(Boolean isRunning)
    {
        this.isRunning = isRunning;
    }
    
    /* SetTimeMultiplier(int i) sets the time multiplier of the train
     * Parameters:
     *     int i - sets train's time multiplier to "i"
    */
    public void SetTimeMultiplier(int i)
    {
        this.timeMultiplier = i;
        this.Controller.SetTimeMultiplier(this.timeMultiplier);
    }
    
    /* SetTrackSignal(TrackSignal s) sets the track signal of the train
     * Parameters:
     *     TrackSignal s - sets teh train's track signal to "s"
    */
    public void SetTrackSignal(TrackSignal s)
    {
        this.trackSignal = s;
        this.Controller.SetTrackSignal(this.trackSignal);
        // gradient
        physicsInput.Gradient = trackSignal.Gradient;
        // passengers
    }
    
    /* SetTrainStatus(TrainStatus s) sets the status of the train
     * Parameters:
     *     TrainStatus s - sets the train's status to "s"
    */
    public void SetTrainStatus(TrainStatus s)
    {
        this.status = s;
        this.Controller.SetTrainStatus(this.status);
    }
    
    /* Simulate() called by "run()".  Performs simulation on train object
    */
    public void Simulate()
    {
        Model.startPhysics();
        
        while(this.isRunning.booleanValue() == Boolean.TRUE)
        {
            this.commands = this.Controller.GetTrainCommand();
            translateStateCommand(this.commands);
            Model.updateState();
            
            translatePhysicsCommand(this.commands);
            try {
                Thread.sleep(100 / this.timeMultiplier); //sleep for .1 seconds
            } catch (InterruptedException ex) {
                Logger.getLogger(Train.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateStatus();  // update status object
        }
    }
    
    /* ToString() returns the Trains name
     * Returns - String ("Train " + this.iD)
    */
    public String ToString()
    {
        return "Train " + Integer.toString(this.iD);
    }
    
    
    // Private methods
    /* translatePhysicsCommand(TrainCommand c) sets physics input from train command
     * Parameters:
     *     TrainCommand c - sets the train's physics input from "c"
    */
    private void translatePhysicsCommand(TrainCommand c)
    {
        physicsInput.MotorPower = c.PowerCommand;
        physicsInput.SBrakeStatus = c.ServiceBrakeOn;
        physicsInput.EBrakeStatus = c.EmergencyBrakeOn;
        
        // do some additional work here not related to the Train Command
        physicsInput.Time_multiplier = timeMultiplier;
    }
    
    /* translateStateCommand(TrainCommand c) sets state input from train command
     * Parameters:
     *     TrainCommand c - sets the train's state input from "c"
    */
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
    
    /* updateStatus() updates the status object of the train
    */
    private void updateStatus()
    {
        status.SetVelocity(physicsInput.Velocity); 
        status.SetMass(Model.getMass());
        status.SetTemperature(stateInput.Temperature);
        status.SetFailure(Model.getFailureCode());
        status.SetSBrakeStatus(Model.getSBrakeStatus());
        status.SetEBrakeStatus(Model.getEBrakeStatus());
        status.SetPassengerBrakeRequest(Model.getPassengerEBrakeStatus());
        status.SetLeftDoorStatus(Model.getLeftDoorStatus());
        status.SetRightDoorStatus(Model.getRightDoorStatus());
        status.SetExteriorLightStatus(Model.getExtLightStatus());
        status.SetInteriorLightStatus(Model.getIntLightStatus());
        status.SetHeaterStatus(Model.getHeaterStatus());
        status.SetAnnouncement(Model.getAnnouncement());
        status.SetAdvertisement(Model.getAdvertisement());
    }
}
