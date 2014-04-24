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
import DataLayer.SystemTime;
import DataLayer.TrackModel.*;
import DataLayer.Train.TrainModel.*;
import DataLayer.Train.TrainController.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Train implements Runnable
{
    //Class Variables
    public boolean PassengerFlag;
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
    private SystemTime time;
    private int timeMultiplier; //time multiplier of a train
    private TrackSignal trackSignal; //signal from the current track block
    
    
    //Contructors
    public Train()
    {
        this.isRunning = new Boolean(false);
        this.iD = 1;
        this.timeMultiplier = 1;
        this.time = new SystemTime();
        this.status = new TrainStatus();
        this.commands = new TrainCommand();
        this.physicsInput = new PhysicsInput();
        this.stateInput = new StateInput();
        this.trackSignal = new TrackSignal();
        this.beaconSignal = null;
        this.Controller = new TrainController(this.timeMultiplier, this.time, this.status, this.trackSignal, this.beaconSignal);
        this.Model = new TrainModel(this.physicsInput, this.stateInput);
        lastDeltaX = 0;
        PassengerFlag = false;
    }
    
    public Train(int id, Boolean running, SystemTime time)
    {
        this.isRunning = running;
        this.iD = id;
        this.timeMultiplier = 1;
        this.time = time;
        this.status = new TrainStatus();
        this.commands = new TrainCommand();
        this.physicsInput = new PhysicsInput();
        this.stateInput = new StateInput();
        this.trackSignal = new TrackSignal();
        this.beaconSignal = null;
        this.Controller = new TrainController(this.timeMultiplier, this.time, this.status, this.trackSignal, this.beaconSignal);
        this.Model = new TrainModel(this.physicsInput, this.stateInput);
        lastDeltaX = 0;
        PassengerFlag = false;
    }
    
    public Train(int id, int multiplier, SystemTime time, Boolean running, TrainStatus status, TrackSignal signal, BeaconSignal beacon)
    {
        this.isRunning = running;
        this.iD = id;
        this.timeMultiplier = multiplier;
        this.time = time;
        this.status = status;
        this.commands = new TrainCommand();
        this.physicsInput = new PhysicsInput();
        this.stateInput = new StateInput();
        this.trackSignal = signal;
        this.beaconSignal = beacon;
        this.Controller = new TrainController(this.timeMultiplier, this.time, this.status, this.trackSignal, this.beaconSignal);
        this.Model = new TrainModel(this.physicsInput, this.stateInput);
        lastDeltaX = 0;
        PassengerFlag = false;
    }
    
    
    //Public methods
    /* GetDeltaX() gets distance travelled since last request
     * Returns - double, meters travelled
    */
    public double getDeltaX()
    {
        double tempDeltaX = 0;
        tempDeltaX =  physicsInput.Delta_x - lastDeltaX;
        lastDeltaX = physicsInput.Delta_x;
        return tempDeltaX;
    }
    
    /* GetTrainCommand() gets a train command from the controller
     * Returns - TrainCommand object
    */
    public TrainCommand getTrainCommand()
    {
        return this.commands;
    }
    
    /* GetTrainStatus() gets the status of the train from the model
     * Returns - TrainStatus object
    */
    public TrainStatus getTrainStatus()
    {
        return this.status;
    }
    
    /* run() used to implement Runnable.  Calls "Simulate()"
    */
    public void run()
    {
        this.simulate();
    }
    
    /*
    sendDeltaPassengers(int deltaPassengers) adds passengers to the train's
    current count345
    */
    public void sendDeltaPassengers(int deltaPassengers)
    {
        Model.addPassengers(deltaPassengers);
        System.out.println(deltaPassengers);
        PassengerFlag = true;
    }
    
    /* SetBeaconSignal(BeaconSignal s) sets the beacon of the train
     * Parameters:
     *     BeaconSignal s - sets train's beacon signal to "s"
    */
    public void setBeaconSignal(BeaconSignal s)
    {
        this.beaconSignal = s;
        this.Controller.setBeaconSignal(this.beaconSignal);
        //System.out.println("Beacon hit");
    }
    
    /* SetIsRunning(Boolean isRunning) sets isRunning of the train
     * Parameters:
     *     Boolean isRunning - sets train's isRunning to "isRunning"
    */
    public void setIsRunning(Boolean isRunning)
    {
        this.isRunning = isRunning;
    }
    
    /* SetTimeMultiplier(int i) sets the time multiplier of the train
     * Parameters:
     *     int i - sets train's time multiplier to "i"
    */
    public void setTimeMultiplier(int i)
    {
        this.timeMultiplier = i; //sets train time multiplier
        this.time.setMultiplier(i);
        this.Controller.setTimeMultiplier(this.timeMultiplier); //sets time multiplier for the controller
        this.Model.physics.setTimeMultiplier(this.timeMultiplier); //sets time multiplier for physics engine in the model
    }
    
    /* SetTrackSignal(TrackSignal s) sets the track signal of the train
     * Parameters:
     *     TrackSignal s - sets teh train's track signal to "s"
    */
    public void setTrackSignal(TrackSignal s)
    {
        this.trackSignal = s;
        this.Controller.setTrackSignal(this.trackSignal);
        // gradient
        physicsInput.Gradient = trackSignal.Gradient;
        // passengers
    }
    
    /* SetTrainStatus(TrainStatus s) sets the status of the train
     * Parameters:
     *     TrainStatus s - sets the train's status to "s"
    */
    public void setTrainStatus(TrainStatus s)
    {
        this.status = s;
        this.Controller.setTrainStatus(this.status);
    }
    
    /* Simulate() called by "run()".  Performs simulation on train object
    */
    public void simulate()
    {
        double oldVelocity = 0;
        Model.startPhysics();
        
        while(this.isRunning.booleanValue() == Boolean.TRUE)
        {
            this.commands = this.Controller.getTrainCommand();
            translateStateCommand(this.commands);
            Model.updateState();
            if ((physicsInput.Velocity > 0.0) && (oldVelocity == 0.0))
            {
                PassengerFlag = false;
            }
            oldVelocity = physicsInput.Velocity;
            translatePhysicsCommand(this.commands);
            try {
                if (this.timeMultiplier > 0) //check to make sure time multiplier is not zero
                {
                    Thread.sleep(100 / this.timeMultiplier); //sleep for .1 seconds
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Train.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateStatus();  // update status object
        }
    }
    
    /* ToString() returns the Trains name
     * Returns - String ("Train " + this.iD)
    */
    public String toString()
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
        status.setVelocity(physicsInput.Velocity); 
        status.setMass(Model.getMass());
        status.setTemperature(stateInput.Temperature);
        status.setFailure(Model.getFailureCode());
        status.setSBrakeStatus(Model.getSBrakeStatus());
        status.setEBrakeStatus(Model.getEBrakeStatus());
        status.setPassengerBrakeRequest(Model.getPassengerEBrakeStatus());
        status.setLeftDoorStatus(Model.getLeftDoorStatus());
        status.setRightDoorStatus(Model.getRightDoorStatus());
        status.setExteriorLightStatus(Model.getExtLightStatus());
        status.setInteriorLightStatus(Model.getIntLightStatus());
        status.setHeaterStatus(Model.getHeaterStatus());
        status.setAnnouncement(Model.getAnnouncement());
        status.setAdvertisement(Model.getAdvertisement());
    }
}
