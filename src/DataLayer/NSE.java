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
import DataLayer.EnumTypes.LineColor;
import DataLayer.TrackModel.*;
import DataLayer.Train.*;
import DataLayer.Wayside.*;
import java.util.*;


public class NSE 
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
    private boolean isRunning;
    
    
    //Constructors
    public NSE()
    {
        this.isRunning = false;
        this.timeMultiplier = REAL_TIME;
        this.CTCOffice = new CTC();
        this.Track = new TrackModel();
        this.Wayside = new Wayside(this.Track);
        this.TrainLocations = new ArrayList<TrainLocation>();
        this.Trains = new ArrayList<Train>();
        //creates 10 Train Objects and 10 TrainLocations
        for (int i = 0; i < 10; i++)
        {
            this.Trains.add(new Train(i));
            this.Trains.get(i).setTimeMultiplier(this.timeMultiplier);
            this.TrainLocations.add(new TrainLocation(LineColor.YARD));
        }
        
        this.CTCOffice.setTrainLocations(this.TrainLocations); //setting CTC Office's train locations to the newly created locations
        this.Track.theTrains = this.TrainLocations; //setting Track's Train Locaitons to the newly created TrainLocations
        this.timeMultiplier = REAL_TIME;
    }
    
    public NSE(int timeMultiplier, int numberOfTrains)
    {
        this.isRunning = false;
        this.timeMultiplier = timeMultiplier;
        this.CTCOffice = new CTC(numberOfTrains);
        this.Track = new TrackModel();
        this.Wayside = new Wayside(this.Track);
        this.TrainLocations = new ArrayList<TrainLocation>();
        this.Trains = new ArrayList<Train>();
        //creates 10 Train Objects and 10 TrainLocations
        for (int i = 0; i < numberOfTrains; i++)
        {
            this.Trains.add(new Train(i));
            this.Trains.get(i).setTimeMultiplier(this.timeMultiplier);
            this.TrainLocations.add(new TrainLocation(LineColor.YARD));
        }
        
        this.CTCOffice.setTrainLocations(this.TrainLocations); //setting CTC Office's train locations to the newly created locations
        this.Track.theTrains = this.TrainLocations; //setting Track's Train Locaitons to the newly created TrainLocations
        this.timeMultiplier = timeMultiplier;
    }
    
    
    //public methods
    public void RunAutomatic()
    {
        this.isRunning = true;
        
        //spawn new thread for each Train
        for(Train train : this.Trains)
        {
            new Thread(train).start();
        }
        
        while(this.isRunning)
        {
            //Communicate from CTC to Wayside
            ArrayList<BlockSignalBundle> toWaysideInfo = this.CTCOffice.getRouteInfo();
            for (BlockSignalBundle bundle : toWaysideInfo)
            {
                this.Wayside.sendTravelSignal(bundle);
            }
            
            //Communicate from Wayside to Track
            
            
            //Communicate from Track to Trains
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
