/******************************************************************************
 * 
 * CTC class
 * 
 * Developed by AJility
 * April 2014
 * 
 * Contributers:
 *  Michael Kudlaty
 * 
 *****************************************************************************/

package DataLayer.CTC;
import DataLayer.Bundles.*;
import DataLayer.EnumTypes.LineColor;
import DataLayer.TrackModel.Switch;
import DataLayer.TrackModel.TrainLocation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class CTC
{
    public boolean mode;
    private int numberOfTrains;
    private final int GREEN_ROUTE_BLOCKS;
    private final int RED_ROUTE_BLOCKS;
    public String[] greenSections;
    public String[] redSections;
    private String[] numberTrains;
    private String[] trackPath;   
    
    private ArrayList<Integer> greenPath;
    private ArrayList<Integer> redPath;
    
    public ArrayList<String[]> green;
    public ArrayList<String[]> red;        
    private ArrayList<String[]> path;
    
    public ArrayList<BlockSignalBundle> setRoute;
    public ArrayList<BlockSignalBundle> closureBundle;
    public ArrayList<Switch> switchBundle;
    
    private ArrayList<TrainLocation> trainLocations;
    
    public ArrayList<Switch> switchPostions;
    public ArrayList<BlockSignalBundle> BlockClosings;    
        
    
    public ArrayList<TrainsClass> trains;
    public final ArrayList<Station> GREEN_NEXT_STATION;
    public final ArrayList<Station> RED_NEXT_STATION;
    
    //Initializes the all the local and public variables
    //Receives the number of trains at the beginning with nTrains
    public CTC(int nTrains) 
    {      
        this.numberOfTrains = nTrains;
        this.RED_ROUTE_BLOCKS = 66;
        this.GREEN_ROUTE_BLOCKS = 150;
        
        this.trainLocations = new ArrayList<>();
        this.switchPostions = new ArrayList<>();
        this.closureBundle = new ArrayList<>();
        this.switchBundle = new ArrayList<>();
        this.setRoute = new ArrayList<>();
        
        this.greenPath = new ArrayList<>();
        this.redPath = new ArrayList<>();
        
        this.red = new ArrayList<>();
        this.green = new ArrayList<>();
        this.trains = new ArrayList<>();
        this.BlockClosings = new ArrayList<>();
        this.RED_NEXT_STATION = new ArrayList<>();
        this.GREEN_NEXT_STATION = new ArrayList<>(); 
                
        this.trackPath = ("YY,K,L,M,N,O,P,Q,N,R,S,T,U,V,W,X,Y,Z,F,E,D,C,B,A,D,E,F,G,H,I,ZZ|J").split(",");
        
        setNextStations();       
        iniTrack();
        setPath();
        iniTrains(nTrains);         
    }
    
    //Initializes the all the local and public variables
    //Defaults the number of trains in the yard to 10
    public CTC() 
    {
        this.numberOfTrains = 10;
        this.RED_ROUTE_BLOCKS = 66;
        this.GREEN_ROUTE_BLOCKS = 153;

        this.greenPath = new ArrayList<>();
        this.redPath = new ArrayList<>();
        this.closureBundle = new ArrayList<>();
        this.switchPostions = new ArrayList<>();
        this.setRoute = new ArrayList<>();
        this.switchBundle = new ArrayList<>();
        
        this.red = new ArrayList<>();
        this.green = new ArrayList<>();
        this.trains = new ArrayList<>();
        this.BlockClosings = new ArrayList<>();
        
        this.GREEN_NEXT_STATION = new ArrayList<>(); 
        this.RED_NEXT_STATION = new ArrayList<>();
    
        this.trackPath = ("YY,K,L,M,N,O,P,Q,N,R,S,T,U,V,W,X,Y,Z,F,E,D,C,B,A,D,E,F,G,H,I,ZZ|J").split(",");
        
        setNextStations();
        iniTrack();
        setPath();
        iniTrains(numberOfTrains);         
    }    

    //update the local train locations, block signals, switches positions    
    public ArrayList<BlockSignalBundle> getRouteInfo()
    {
        updateTrainLocations();
        ArrayList<BlockSignalBundle> trainRouteInfo = new ArrayList<>(); 
        BlockSignalBundle newRoute;
        int trainIndex;
        
        for(TrainsClass get: this.trains)
        {
            trainIndex = this.trains.indexOf(get);
            if(mode)
            {
                if((get.Authority == 0) && (this.trainLocations.get(trainIndex).CurrentBlock) != (this.trainLocations.get(trainIndex).PrevBlock))
                {
                    if(this.trainLocations.get(trainIndex).CurrentBlock == this.trains.get(trainIndex).BlockDestination)
                    {
                        this.trains.get(trainIndex).StopIndex++;                            
                        newRoute = getNextStation(this.trains.get(trainIndex));

                        //this.trains.get(trainIndex).Authority = newRoute.Authority;
                        this.trains.get(trainIndex).BlockDestination = newRoute.Destination;
                        this.trains.get(trainIndex).SectionDestination = this.returnSection(this.trains.get(trainIndex).line, this.trains.get(trainIndex).BlockDestination);
                        this.trains.get(trainIndex).StationDestination = this.returnStationBlock(this.trains.get(trainIndex).line, this.trains.get(trainIndex).BlockDestination);
                        trainRouteInfo = this.calculateAuthority(this.trains.get(trainIndex), this.trains.get(trainIndex).line);
                        this.trains.get(trainIndex).Authority = trainRouteInfo.size() - 1;
                        //System.out.println("CTC Moving onto the next staion");
                    }
                    else
                    {                        
                        trainRouteInfo = this.calculateAuthority(this.trains.get(trainIndex), this.trains.get(trainIndex).line);
                        this.trains.get(trainIndex).Authority = trainRouteInfo.size() - 1;                                        
                    }                                           
                    //System.out.println("CTC Train's new authority: " + this.trains.get(trainIndex).Authority);
                    break;
                }
            }
            else if(this.trainLocations.get(trainIndex).CurrentBlock != this.trains.get(trainIndex).BlockDestination && (get.Authority == 0))
            {
                trainRouteInfo = this.calculateAuthority(this.trains.get(trainIndex), this.trains.get(trainIndex).line);
                this.trains.get(trainIndex).Authority = trainRouteInfo.size() - 1;                                        
                break;
            }
        }
        return trainRouteInfo;//new BlockSignalBundle(Line, block, velocity, authority, destination);
    }    
    
    //A private method that updates the train current block location as well as the previous block
    //Updates the each trains authority if it has moved
    private void updateTrainLocations()
    {
        for(int i = 0; i < this.trainLocations.size(); i++)
        {   
            if(this.trainLocations.get(i).PrevBlock == this.trains.get(i).BlockCurrent && this.trainLocations.get(i).PrevBlock != this.trainLocations.get(i).CurrentBlock)
            {                
                this.trains.get(i).Authority--;                
                this.trains.get(i).BlockCurrent = this.trainLocations.get(i).CurrentBlock;
                this.trains.get(i).PreviousBlock = this.trainLocations.get(i).PrevBlock;
                this.trains.get(i).SectionCurrent = this.returnSection(this.trains.get(i).line, this.trains.get(i).BlockCurrent);
                this.trains.get(i).StationCurrent = this.returnStationBlock(this.trains.get(i).line, this.trains.get(i).BlockCurrent);
            }
            
            if(this.trains.get(i).Authority == 0)
            {               
                this.trains.get(i).BlockCurrent = this.trainLocations.get(i).CurrentBlock;
                
            }            
            this.trains.get(i).setDistanceSoFar(this.trainLocations.get(i).DistanceSoFar);            
        }        
    }
    
    //Given a line and block #, this method will return the corresponding section
    public String returnSection(LineColor line, int blockID)
    {
        //System.out.println("Line Color: " + Line);
        if(blockID == 0)
        {
            return "";
        }
        else
        {
            if(line == LineColor.GREEN)
            {                
                for(String[] section: this.green)
                {
                    //System.out.println(section);
                    if(Integer.parseInt(section[1]) == blockID)
                    {
                        return(section[0]);
                    }
                }
            }
            else
            {
                if(line == LineColor.RED)
                {
                    for(String[] section: this.green)
                    {
                        if(Integer.parseInt(section[1]) == (blockID))
                        {
                            return(section[0]);
                        }
                    }
                }
            }
        }        
        return "Not Found";            
    }
    
    
    private BlockSignalBundle getNextStation(TrainsClass train)
    {
        int newDestination = 0;
        int newAuthority = 0;
        
        if(LineColor.GREEN == train.line)
        {
            newAuthority = this.GREEN_NEXT_STATION.get(train.StopIndex).AUTHORITY;
            newDestination = this.GREEN_NEXT_STATION.get(train.StopIndex).NEXTBLOCKID;
            
        }
        else if(LineColor.RED == train.line)
        {
            newAuthority = this.RED_NEXT_STATION.get(train.StopIndex).AUTHORITY;
            newDestination = this.RED_NEXT_STATION.get(train.StopIndex).NEXTBLOCKID;            
        }
        
        return new BlockSignalBundle(newAuthority, (newDestination), ((double)(70*1000)/(double)(3600)), train.BlockCurrent, train.line);//returnSection(LineColor.GREEN, newDestination))
    }
        
    public ArrayList<BlockSignalBundle> calculateAuthority(TrainsClass train, LineColor line)
    {
        ArrayList<BlockSignalBundle> route = new ArrayList<>();        
        boolean destinationFound = false;
        ArrayList<Integer> path;
        
        if(line == LineColor.GREEN)
        {
            path = greenPath;
        }    
        else if(line == LineColor.RED)
        {
            path = redPath;
        }
        else
        {
            path = greenPath;   
        }
        
        if(train.BlockCurrent == 0)
        {
            for(int j = 0; j < path.size(); j++)
            {
                if(train.BlockDestination == path.get(j))
                {
                    for(int k = 0; k <= j; k++)
                    {
                        route.add(new BlockSignalBundle(j-k-1, train.BlockDestination, (double)((double)70*1000)/(3600), path.get(k), train.line));
                    }
                    break;
                }
            }
        }
        else if(train.BlockCurrent == train.BlockDestination)
        {
                    
        }
        else
        {
            for(int i = 0; i < path.size(); i++)
            {
                //System.out.println(path.get(i).toString());
                if(train.BlockCurrent == path.get(i))
                {
                    //System.out.println("CTC Train's Current Block: " + path.get(i).toString());
                    //System.out.println("CTC Train's Previous block: " + train.PreviousBlock);
                    if(i > 0 && train.PreviousBlock == path.get(i-1) || train.PreviousBlock == 0)
                    {
                        //System.out.println("CTC Train's Previous Block: " + path.get(i-1).toString());
                        for(int j = i; j < path.size(); j++)
                        {                            
                            //System.out.println("Train's Destination Block : " + (train.BlockDestination) + " | Checking Block: " + path.get(j));
                            if(train.BlockDestination == path.get(j))
                            {
                                //System.out.println("CTC Destination: " + path.get(j) + " | Current Block: " + path.get(i));
                                //System.out.println("CTC Destination Block: " + j + " | Current Block Index: " + i);                                
                                for(int k = 0; k <= j-i; k++)
                                {
                                    //System.out.println("CTC Block: " + path.get(i+k) + " | Train's Authority: " + (j-i-k));
                                    route.add(new BlockSignalBundle(j-i-k, train.BlockDestination, (double)((double)70*1000)/(3600), path.get(i+k), train.line));
                                }                                        
                                destinationFound = true;
                                break;
                            }                            
                        }
                        if(!destinationFound)
                        {
                            for(int j = 0; j < i; j++)                            
                            {
                                if(train.BlockDestination == path.get(j))
                                {
                                    for(int k = i; k <= path.size()-1; k++)
                                    {
                                        route.add(new BlockSignalBundle(path.size()-k+j-2, train.BlockDestination, (double)((double)70*1000)/(3600), path.get(k), train.line));
                                    }

                                    for(int k = 1; k <= j; k++)
                                    {
                                        route.add(new BlockSignalBundle(j-k, train.BlockDestination, (double)((double)70*1000)/(3600), path.get(k), train.line));
                                    }                                                                        
                                }
                            }
                        }
                        break;
                    }
                    else if(i == path.size()-1)
                    {
                        route.add(new BlockSignalBundle(1, train.BlockDestination, (double)((double)70*1000)/(3600), path.get(i), train.line));
                        break;
                    }                    
                }            
            }
        }
        return route;
    }
    
    
    //creates all the trains in the yard
    private void iniTrains(int numberOfTrains)
    {
        numberTrains = new String[numberOfTrains+1];
        String[][] trainInfo = new String[numberOfTrains][4];
        numberTrains[0] = "Train #";
        
        for(int i = 0; i < numberOfTrains; i++)
        {
            trains.add(new TrainsClass(LineColor.YARD, "", 0, 0 , "", 0));
            numberTrains[i+1] = Integer.toString(i+1);
            trainInfo[i] = new String[] {Integer.toString(i+1), "", "", "Yard"};
        }
    }
    
    //reads the track.csv and parse it for block #, section, and line
    private void iniTrack()
    {
        ArrayList<String> greenSec = new ArrayList<>();
        ArrayList<String> redSec = new ArrayList<>();
        
        path = new ArrayList<>();

        ArrayList<String> sections = new ArrayList<>();
        
        for(int i = 0; i < trackPath.length; i++)
        {
            for(int j = 0; j < green.size(); j++)
            {
                if(trackPath[i].equals(green.get(j)[1]))
                {
                   sections.add(green.get(j)[1]);                                                            
                }
            }
            path.add(i, sections.toArray(new String[0]));
        }
        
        greenSec.add("Section");
        redSec.add("Section");
        
        String csvFile = "trackInfo.txt";
        BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
        
	try 
        {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) 
            { 
                // use comma as separator
                String[] block = line.split(cvsSplitBy);
                if(block[0].toUpperCase().equals("RED"))
                {
                    if(!redSec.contains(block[1]))
                    {
                        redSec.add(block[1]);
                    }
                    
                    this.red.add(new String[] {block[1], block[2], block[6]});
                }
                else
                {
                    if(!greenSec.contains(block[1]))
                    {
                        greenSec.add(block[1]);
                    }
                    green.add(new String[] {block[1], block[2], block[6]});                   
                }
            } 
	} 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
	} 
        catch (IOException e) 
        {
            e.printStackTrace();
	} 
        finally 
        {
            if (br != null) 
            {
                try 
                {
                    br.close();
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                }
            }
	}        
        greenSections = greenSec.toArray(new String[0]);
        redSections = redSec.toArray(new String[0]);
    }

    //Defines the route for each line as defined in route.
    private void setPath()
    {
        String csvFile = "Route.txt";
        BufferedReader br = null;
        String lineColor = "";
	String line = "";
	String cvsSplitBy = ",";
        
	try 
        {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) 
            { 
                line = line.trim();
                if(line.equals("GREEN") || line.equals("RED"))
                {
                    lineColor = line.trim();
                }
                else if(lineColor.equals("RED"))
                {
                    redPath.add(Integer.parseInt(line));
                }
                else if(lineColor.equals("GREEN"))
                {
                    greenPath.add(Integer.parseInt(line));
                }
            } 
	} 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
	} 
        catch (IOException e) 
        {
            e.printStackTrace();
	} 
        finally 
        {
            if (br != null) 
            {
                try 
                {
                    br.close();
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                }
            }
	}        
    }
    
    //checks to see if train is on a given block
    private boolean trainExistsOnBlock(int blockID)
    {
        boolean exists = false;
        for(TrainLocation train: this.trainLocations)
        {
            if(train.CurrentBlock == blockID)
            {
                exists = true;
            }
        }        
        return exists;
    }
    
    //Updates CTC's local switches positions and block closure 
    public void updateBlockInfo(ArrayList<BlockSignalBundle> blockOccupiences, ArrayList<Switch> newSwitches)
    {   
        
        int blockIndex;
        
        for(BlockSignalBundle blockInfo: blockOccupiences)
        {
            blockIndex = this.containsClosings(blockInfo.LineID, blockInfo.BlockID);
            if(blockIndex != -1)
            {
                
                if(!trainExistsOnBlock(blockInfo.BlockID) && blockInfo.Closed)
                {
                    this.BlockClosings.add(blockInfo);
                }
            }                                   
            else
            {
                if(!trainExistsOnBlock(blockInfo.BlockID) && blockInfo.Closed)
                {
                    this.BlockClosings.set(blockIndex, blockInfo);
                }
            }
        }
        
        this.switchPostions = newSwitches;       
    }
       
    
    //returns a arraylist of closures, the size should only should be 1 or 0
    //if the size is equal to 0, then there are no closure to set
    //if the size is equal to 1, then wayside will act accordingly 
    public ArrayList<BlockSignalBundle> setClosing()
    {
        ArrayList<BlockSignalBundle> newSignal = new ArrayList<>();
        if(this.closureBundle.size() == 1)
        {
            
            newSignal.add(this.closureBundle.get(0));
            this.closureBundle.clear();
        }
        
        return newSignal;
    } 
    
    //returns the suggest route for train and clears immediately
    public ArrayList<BlockSignalBundle> setRoute()
    {
        //System.out.println("CTC size of route: " + setRoute.size());
        ArrayList<BlockSignalBundle> route = new ArrayList<>();
        if(this.setRoute.size() >= 1)
        {
            for(BlockSignalBundle b: this.setRoute)
            {
                route.add(b);
            }
            
            this.setRoute.clear();
            //System.out.println("CTC size of route: " + route.size());
        }
        
        return route;        
    }
    
    
    //Sets the changed switch a local variable and returns
    //clears the switch Bundle
    public ArrayList<Switch> setSwitch()
    {
        ArrayList<Switch> newSwitch = new ArrayList<>();
        if(this.switchBundle.size() == 1)
        {            
            newSwitch.add(this.switchBundle.get(0));
            this.switchBundle.clear();
        }
        
        return newSwitch;
    }
    
    public int containsClosings(LineColor line, int blockID)
    {
        for(int i = 0; i < this.BlockClosings.size(); i++)
        {
            if(this.BlockClosings.get(i).BlockID == blockID && this.BlockClosings.get(i).LineID == line)
            {
                return i;
            }
        }
        
        return -1;    
    }
    
    //Completed
    public void setTrainLocations(ArrayList<TrainLocation> trainLocations)
    {
        this.trainLocations = trainLocations;
    }      
    
    public ArrayList<DispatchBundle> getDispatcher()
    {
        //System.out.println("The number of trains: " + this.trainLocations.size());
        ArrayList<DispatchBundle> train = new ArrayList<>();
        for(int i = 0; i < this.trainLocations.size(); i+= 2)
        {
            //System.out.println("Train " + i + "'s locations: " + this.trainLocations.get(i).CurrentBlock);
            if(this.trainLocations.get(i).CurrentBlock == 0)
            {
                train.add(new DispatchBundle(new BlockSignalBundle(4, 65, ((double)(70*1000)/(double)(3600)),0, LineColor.YARD), i , LineColor.GREEN));
                this.trains.get(i).line = LineColor.GREEN;
                this.trains.get(i).Authority = 5;
                this.trains.get(i).BlockDestination = this.GREEN_NEXT_STATION.get(0).NEXTBLOCKID;
                this.trains.get(i).StationDestination = this.GREEN_NEXT_STATION.get(0).NEXTSTATION;
                this.trains.get(i).SectionDestination = this.returnSection(this.trains.get(i).line, this.GREEN_NEXT_STATION.get(0).NEXTBLOCKID);
                break;
            }
            
        }        
        
        for(int i = 1; i < this.trainLocations.size(); i+= 2)
        {
            //System.out.println("Train " + i + "'s locations: " + this.trainLocations.get(i).CurrentBlock);
            if(this.trainLocations.get(i).CurrentBlock == 0)
            {
                train.add(new DispatchBundle(new BlockSignalBundle(4, 65, ((double)(70*1000)/(double)(3600)),0, LineColor.YARD), i , LineColor.RED));
                this.trains.get(i).line = LineColor.RED;
                this.trains.get(i).Authority = 5;
                this.trains.get(i).BlockDestination = this.RED_NEXT_STATION.get(0).NEXTBLOCKID;
                this.trains.get(i).StationDestination = this.RED_NEXT_STATION.get(0).NEXTSTATION;
                this.trains.get(i).SectionDestination = this.returnSection(this.trains.get(i).line, this.RED_NEXT_STATION.get(0).NEXTBLOCKID);
                break;
            }
        }
        return train;    
    }
    
    //reads the nextStation.txt file converts into a ArrayList of Stations
    //Each Stations contains the current station, the next stations, and what the authority should be    
    private void setNextStations()
    {
        String csvFile = "nextStation.txt";
        BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";

	try 
        {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) 
            { 
                // use comma as separator
                String[] block = line.split(cvsSplitBy);
                //System.out.println(Line);
                if(block[0].equals("GREEN"))
                {
                    this.GREEN_NEXT_STATION.add(new Station(Integer.parseInt(block[3].trim()), Integer.parseInt(block[1].trim()), Integer.parseInt(block[4].trim()), block[2], block[5]));
                }
                else
                {
                    if(block[0].equals("RED"))    
                    {
                        this.RED_NEXT_STATION.add(new Station(Integer.parseInt(block[3].trim()), Integer.parseInt(block[1].trim()), Integer.parseInt(block[4].trim()), block[2], block[5]));
                    }
                }
                
            }
        }
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
	} 
        catch (IOException e) 
        {
            e.printStackTrace();
	} 
        finally 
        {
            if (br != null) 
            {
                try 
                {
                    br.close();
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                }
            }
	}        
        
    }
    
    public String returnStationBlock(LineColor line, int blockID)
    {
        String station = "";
        switch(line)
        {
            case GREEN:
                for(Station stat: this.GREEN_NEXT_STATION)
                {
                    if(stat.BLOCKID == (blockID))
                    {
                        station = stat.STATION;
                        break;
                    }
                }
                break;
            case RED:
                for(Station stat: this.RED_NEXT_STATION)
                {
                    if(stat.BLOCKID == (blockID))
                    {
                        station = stat.STATION;
                        break;
                    }
                }
                break;
            case YARD:
                break;
                
        }
        
        return station;
    }
    
    public void SetMode(boolean mode)
    {
        //Mode
        //  True - Automatic
        //  False - Manual 
        this.mode = mode;
    }
    
}   

