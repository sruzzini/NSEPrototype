/******************************************************************************
 * 
 * NSE class
 * 
 * Developed by AJility
 * April 2014
 * 
 * Contributers:
 *  Nathan Hachten
 *  Michael Kudlaty
 *  Ryan Mertz
 *  Stephen Ruzzini
 *  Drew Winfield
 *
 *****************************************************************************/

package DataLayer;

import DataLayer.Bundles.*;
import DataLayer.CTC.*;
import DataLayer.TrackModel.*;
import DataLayer.Train.*;
import DataLayer.Train.TrainController.TrainController;
import DataLayer.Wayside.*;
import GUILayer.NSEFrame;
import java.util.*;
import java.util.Calendar;


public class NSE implements Runnable
{
    //Constatns
    public static final int REAL_TIME = 1; //multiplier for real time
    public static final int SPEED_UP_10X = 10; //multiplier for 10x real time
    private static final long dispatchIntervalMin = 10; //dispatch interval
    private static final long secondsInMin = 60; //number of milliseconds in a minute
    private static final long dispatchInterval = dispatchIntervalMin * secondsInMin; // convert minutes to milliseconds 
    
    //Class variables
    public CTC CTCOffice;
    public boolean IsAutomatic;
    public Boolean isRunning;
    public SystemTime Time;
    public int TimeMultiplier;
    public TrackModel Track;
    public ArrayList<TrainLocation> TrainLocations;
    public ArrayList<Train> Trains;
    public Wayside Wayside;
    private NSEFrame nseGUI;
    private SystemTime lastDispatchTime;
    
    
    //Constructors
    public NSE()
    {
        this.IsAutomatic = true;
        this.isRunning = new Boolean(false);
        this.TimeMultiplier = REAL_TIME;
        this.Time = new SystemTime(this.TimeMultiplier);
        this.CTCOffice = new CTC();
        this.Track = new TrackModel();
        this.Wayside = new Wayside(this.Track);
        this.TrainLocations = new ArrayList<TrainLocation>();
        this.Trains = new ArrayList<Train>();
        //creates 10 Train Objects and 10 TrainLocations
        for (int i = 0; i < 10; i++)
        {
            this.Trains.add(new Train(i, this.isRunning, this.Time));
            this.Trains.get(i).setTimeMultiplier(this.TimeMultiplier);
            this.TrainLocations.add(new TrainLocation());
        }
        
        this.CTCOffice.setTrainLocations(this.TrainLocations); //setting CTC Office's train locations to the newly created locations
        this.Track.theTrainLocations = this.TrainLocations; //setting Track's Train Locaitons to the newly created TrainLocations
        this.Track.theTrains = this.Trains; //setting Track's Trains to the newly created Trains
        this.TimeMultiplier = REAL_TIME;
        this.nseGUI = null;
    }
    
    public NSE(int timeMultiplier, int numberOfTrains)
    {
        this.IsAutomatic = true;
        this.isRunning = new Boolean(false);
        this.TimeMultiplier = timeMultiplier;
        this.Time = new SystemTime(this.TimeMultiplier);
        this.CTCOffice = new CTC(numberOfTrains);
        this.Track = new TrackModel();
        this.Wayside = new Wayside(this.Track);
        this.TrainLocations = new ArrayList<TrainLocation>();
        this.Trains = new ArrayList<Train>();
        //creates 10 Train Objects and 10 TrainLocations
        for (int i = 0; i < numberOfTrains; i++)
        {
            this.Trains.add(new Train(i, this.isRunning, this.Time));
            this.Trains.get(i).setTimeMultiplier(this.TimeMultiplier);
            this.TrainLocations.add(new TrainLocation());
        }
        
        this.CTCOffice.setTrainLocations(this.TrainLocations); //setting CTC Office's train locations to the newly created locations
        this.Track.theTrainLocations = this.TrainLocations; //setting Track's Train Locaitons to the newly created TrainLocations
        this.Track.theTrains = this.Trains; //setting Track's Trains to the newly created Trains
        this.TimeMultiplier = timeMultiplier;
        this.nseGUI = null;
    }
    // reset() restores NSE to default settings
    public void reset()
    {
        this.IsAutomatic = true;
        this.isRunning = new Boolean(false);
        this.TimeMultiplier = REAL_TIME;
        this.Time = new SystemTime(this.TimeMultiplier);
        this.CTCOffice = new CTC();
        this.Track = new TrackModel();
        this.Wayside = new Wayside(this.Track);
        this.TrainLocations = new ArrayList<TrainLocation>();
        this.Trains = new ArrayList<Train>();
        //creates 10 Train Objects and 10 TrainLocations
        for (int i = 0; i < 10; i++)
        {
            this.Trains.add(new Train(i, this.isRunning, this.Time));
            this.Trains.get(i).setTimeMultiplier(this.TimeMultiplier);
            this.TrainLocations.add(new TrainLocation());
        }
        
        this.CTCOffice.setTrainLocations(this.TrainLocations); //setting CTC Office's train locations to the newly created locations
        this.Track.theTrainLocations = this.TrainLocations; //setting Track's Train Locaitons to the newly created TrainLocations
        this.Track.theTrains = this.Trains; //setting Track's Trains to the newly created Trains
        this.TimeMultiplier = REAL_TIME;
        
        this.nseGUI.setNSE(this);
        this.nseGUI.setSystemTime(Time.toString());
    }
    
    // run() used to implement Runnable.  Calls "RunAutomatic()"
    public void run()
    {
        this.runAutomatic();
    }
    
    //RunAutomatic() runs NSE simulation in automatic mode
    public void runAutomatic()
    {
        this.isRunning = Boolean.TRUE;
        lastDispatchTime = new SystemTime(-1, -1, -1, 1); //set lastDispatch to invalid number (so immediate dispatch in auto mode)
        
        //spawn new thread for the SystemTime
        new Thread(this.Time).start();
        
        //spawn new thread for each Train
        for(Train train : this.Trains)
        {
            train.Controller.VelocitySetPoint = TrainController.MAX_TRAIN_SPEED;
            train.setIsRunning(this.isRunning.booleanValue());
            new Thread(train).start();
        }
        
        this.Wayside.StartSimulation(); //Start up the wayside controller
        long interval = 5000;
        long lastPrint = 0;
        while(this.isRunning.booleanValue() == Boolean.TRUE)
        {
            //Set time
            if(this.nseGUI != null)
            {
                this.nseGUI.setSystemTime(this.Time.toString());
            }
            
            //dispatch a train?
            if (this.IsAutomatic) //running in manual mode
            {
                //check for 10 min elapsed, if so, dispatch new train
                
                if ((this.lastDispatchTime.Hour == -1) || //the simulation just started
                    (this.Time.secondsSince(this.lastDispatchTime) >= dispatchInterval)) //it's been 10 minutes
                {
                    this.Wayside.sendDispatchSignal(CTCOffice.getDispatcher());
                    this.lastDispatchTime = new SystemTime(this.Time.Hour, this.Time.Minute, this.Time.Second, this.TimeMultiplier);
                }
            }
            else
            {
                //check for dispatching a train
                
                //check for block closings
                
                //check for switch positions
                
            }
            
            //Get info from Wayside to send ot CTC
            ArrayList<BlockSignalBundle> occInfo = this.Wayside.getOccupancyInfo();
            ArrayList<Switch> switchInfo = this.Wayside.getSwitchInfo();
            
            //Send info from Wayside to CTC
            this.CTCOffice.updateBlockInfo(occInfo, switchInfo);
            
            
            ArrayList<BlockSignalBundle> toWaysideInfo = this.CTCOffice.getRouteInfo();
            this.Wayside.sendTravelSignal(toWaysideInfo);
            
            //Communicate from Track to Trains
            this.Track.updateTrainLocations();
            /*
            if (Calendar.getInstance().getTimeInMillis() > lastPrint  + interval)
            {
                System.out.println("TRAIN 0 Physics delta x: " + this.Trains.get(0).getDeltaX());
                //this.Track.updateTrainLocations();

                System.out.println("TRAIN 0 Current Block: " + this.TrainLocations.get(0).currentBlock);
                System.out.println("Current Authority: " + this.Track.theLines.get(0).theBlocks.get(this.TrainLocations.get(0).currentBlock).getAuthority());
                System.out.println("Current Train Controller Authority: " + this.Trains.get(0).controller.getTrackSignal().Authority);
                System.out.println("Current Train Authority: " + this.Trains.get(0).getTrackSignal().Authority);
                System.out.println("Current Train Brake " + this.Trains.get(0).GetTrainCommand().ServiceBrakeOn);
                System.out.println("TRAIN 0 distance: " + this.TrainLocations.get(0).distanceSoFar);

                System.out.println("TRAIN 0 Physics model vel: " + this.Trains.get(0).model.physics.getVelocity());
                System.out.println("TRAIN 0 Physics model power: " + this.Trains.get(0).model.physics.getPower());
                System.out.println("Current Train Power" + this.Trains.get(0).GetTrainCommand().PowerCommand);
                lastPrint = Calendar.getInstance().getTimeInMillis();
            }
            */
        }
    }
    
    /* setGUI(NSEFrame gui) sets a gui to the NSE object
     * Parameters:
     *     NSEFrame gui - gui bound to NSE
    */
    public void setGUI(NSEFrame gui)
    {
        this.nseGUI = gui;
    }
    
    /* SetTimeMultiplier(int multiplier) sets the NSE's time multiplier
     * Parameters:
     *     int multiplier - new time multiplier
    */
    public void setTimeMultiplier(int multiplier)
    {
        this.TimeMultiplier = multiplier; //set new time multiplier
        this.Time.setMultiplier(multiplier); //set time multiplier for the syste time
        
        //set time multiplier for each train
        for (Train train : this.Trains)
        {
            train.setTimeMultiplier(multiplier);
        }
    }
}
