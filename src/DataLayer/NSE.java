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
import java.util.*;
import java.util.Calendar;


public class NSE implements Runnable
{
    //Constatns
    public static final int REAL_TIME = 1;
    public static final int SPEED_UP_10X = 10;
    
    //Class variables
    public CTC CTCOffice;
    public TrackModel Track;
    public Wayside Wayside;
    public ArrayList<TrainLocation> TrainLocations;
    public ArrayList<Train> Trains;
    public int timeMultiplier;
    private Boolean isRunning;
    
    private long lastDispatchTime;
    private final long dispatchIntervalMin = 1;
    private final long millisInMin = 60000;
    private final long dispatchInterval = dispatchIntervalMin * millisInMin; // convert minutes to milliseconds 
    
    
    //Constructors
    public NSE()
    {
        this.isRunning = new Boolean(false);
        this.timeMultiplier = REAL_TIME;
        this.CTCOffice = new CTC();
        this.Track = new TrackModel();
        this.Wayside = new Wayside(this.Track);
        this.TrainLocations = new ArrayList<TrainLocation>();
        this.Trains = new ArrayList<Train>();
        //creates 10 Train Objects and 10 TrainLocations
        for (int i = 0; i < 10; i++)
        {
            this.Trains.add(new Train(i, this.isRunning));
            this.Trains.get(i).SetTimeMultiplier(this.timeMultiplier);
            this.TrainLocations.add(new TrainLocation());
        }
        
        this.CTCOffice.setTrainLocations(this.TrainLocations); //setting CTC Office's train locations to the newly created locations
        this.Track.theTrainLocations = this.TrainLocations; //setting Track's Train Locaitons to the newly created TrainLocations
        this.Track.theTrains = this.Trains; //setting Track's Trains to the newly created Trains
        this.timeMultiplier = REAL_TIME;
    }
    
    public NSE(int timeMultiplier, int numberOfTrains)
    {
        this.isRunning = new Boolean(false);
        this.timeMultiplier = timeMultiplier;
        this.CTCOffice = new CTC(numberOfTrains);
        this.Track = new TrackModel();
        this.Wayside = new Wayside(this.Track);
        this.TrainLocations = new ArrayList<TrainLocation>();
        this.Trains = new ArrayList<Train>();
        //creates 10 Train Objects and 10 TrainLocations
        for (int i = 0; i < numberOfTrains; i++)
        {
            this.Trains.add(new Train(i, this.isRunning));
            this.Trains.get(i).SetTimeMultiplier(this.timeMultiplier);
            this.TrainLocations.add(new TrainLocation());
        }
        
        this.CTCOffice.setTrainLocations(this.TrainLocations); //setting CTC Office's train locations to the newly created locations
        this.Track.theTrainLocations = this.TrainLocations; //setting Track's Train Locaitons to the newly created TrainLocations
        this.Track.theTrains = this.Trains; //setting Track's Trains to the newly created Trains
        this.timeMultiplier = timeMultiplier;
    }
    
    
    public void run()
    {
        this.RunAutomatic();
    }
    
    //public methods
    public void RunAutomatic()
    {
        this.isRunning = Boolean.TRUE;
        lastDispatchTime = 0;
        
        //spawn new thread for each Train
        for(Train train : this.Trains)
        {
            train.Controller.VelocitySetPoint = TrainController.MAX_TRAIN_SPEED;
            train.SetIsRunning(this.isRunning.booleanValue());
            new Thread(train).start();
        }
        
        this.Wayside.StartSimulation(); //Start up the wayside controller
        long interval = 5000;
        long lastPrint = 0;
        while(this.isRunning.booleanValue() == Boolean.TRUE)
        {
            //check for 10 min elapsed, if so, dispatch new train
            if ((Calendar.getInstance().getTimeInMillis() - lastDispatchTime) * timeMultiplier > dispatchInterval)
            {
                this.Wayside.sendDispatchSignal(CTCOffice.getDispatcher());
                lastDispatchTime = Calendar.getInstance().getTimeInMillis();
            }  
            
            //Get info from Wayside to send ot CTC
            ArrayList<BlockSignalBundle> occInfo = this.Wayside.getOccupancyInfo();
            ArrayList<Switch> switchInfo = this.Wayside.getSwitchInfo();
            
            //Send info from Wayside to CTC
            this.CTCOffice.updateBlockInfo(occInfo, switchInfo);
            
            
            ArrayList<BlockSignalBundle> toWaysideInfo = this.CTCOffice.getRouteInfo();
            for (BlockSignalBundle bundle : toWaysideInfo)
            {
                if (Calendar.getInstance().getTimeInMillis() > lastPrint  + interval)
                {
                    System.out.println(bundle.BlockID);
                }
                this.Wayside.sendTravelSignal(bundle);
            }
            
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
    
    
    public void RunManual()
    {
        this.isRunning = true;
        
        //spawn new thread for each Train
        for(Train train : this.Trains)
        {
            new Thread(train).start();
        }
        
        while(this.isRunning)
        {
            //do shit
        }
    }
}
