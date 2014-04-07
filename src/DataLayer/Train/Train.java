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
    private TrainStatus status;
    private TrainCommand commands;
    private ModelPhysics modelPhysics;
    private ModelState modelState;
    private TrackSignal trackSignal;
    private BeaconSignal beaconSignal;
    private TrainController controller;
    private TrainModel model;
    
    
    //Contructors
    public Train()
    {
        this.iD = 1;
        this.timeMultiplier = 1;
        this.status = new TrainStatus();
        this.commands = new TrainCommand();
        this.modelPhysics = new ModelPhysics();
        this.modelState = new ModelState();
        this.trackSignal = new TrackSignal();
        this.beaconSignal = null;
        this.controller = new TrainController(this.timeMultiplier, this.status, this.trackSignal, this.beaconSignal);
        this.model = new TrainModel(this.modelPhysics, this.modelState);
    }
    
    public Train(int id)
    {
        this.iD = id;
        this.timeMultiplier = 1;
        this.status = new TrainStatus();
        this.commands = new TrainCommand();
        this.modelPhysics = new ModelPhysics();
        this.modelState = new ModelState();
        this.trackSignal = new TrackSignal();
        this.beaconSignal = null;
        this.controller = new TrainController(this.timeMultiplier, this.status, this.trackSignal, this.beaconSignal);
        this.model = new TrainModel(this.modelPhysics, this.modelState);
    }
    
    public Train(int id, int multiplier, TrainStatus status, TrackSignal signal, BeaconSignal beacon)
    {
        this.iD = id;
        this.timeMultiplier = multiplier;
        this.status = status;
        this.commands = new TrainCommand();
        this.trackSignal = signal;
        this.beaconSignal = beacon;
        this.controller = new TrainController(this.timeMultiplier, this.status, this.trackSignal, this.beaconSignal);
        this.model = new TrainModel(this.modelPhysics, this.modelState);
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
    public double getDeltaX()
    {
        return this.modelPhysics.delta_x;
    }
    // Private method
    private void updateModelPhysics(TrainCommand c)
    {
        modelPhysics.motorPower = c.PowerCommand;
        modelPhysics.sBrakeStatus = c.ServiceBrakeOn;
        modelPhysics.eBrakeStatus = c.EmergencyBrakeOn;
        modelPhysics.time_multiplier = timeMultiplier;
        // get this from track
        //passengerChange;
        //public double gradient;
    
    }
    private void updateModelState()
    {
        
    }
    
    //Public Methods
    public void run()
    {
        this.simulate();
    }
    
    public void simulate()
    {
        //new Thread(this.model.physicsEngine).start();
        
        while(true)
        {
            this.commands = this.controller.GetTrainCommand();
            updateModelPhysics(commands);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Train.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
