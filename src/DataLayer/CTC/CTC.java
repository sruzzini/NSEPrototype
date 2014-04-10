/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.CTC;
import DataLayer.Bundles.*;
import DataLayer.EnumTypes.LineColor;
import DataLayer.TrackModel.Switch;
import DataLayer.TrackModel.TrainLocation;

//package Bundles.class.BlockBundle;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 *
 * @author domino54
 */
public class CTC
{
    
    private int closings;
    private int numberOfTrains;
    private final int GREEN_ROUTE_BLOCKS;
    private final int RED_ROUTE_BLOCKS;
    private String[] greenSections;
    private String[] redSections;
    private String[] numberTrains;
    private String[] trackPath;
    public ArrayList<TrainsClass> trains;
    private ArrayList<String[]> green;
    private ArrayList<String[]> red;    
    private ArrayList<String[]> blockClosings;    
    private ArrayList<String[]> path;
    private ArrayList<BlockSignalBundle> closures;    
    private ArrayList<TrainLocation> trainLocations;
    private ArrayList<Switch> switchPostions;
    public final ArrayList<Station> GREEN_NEXT_STATION;
    private final ArrayList<Station> RED_NEXT_STATION;
    
    /**
     * Creates new form CTCGUI
     * @param nTrains
     */
    public CTC(int nTrains) 
    {        
        this.closings = 0;
        this.GREEN_ROUTE_BLOCKS = 150;
        this.RED_ROUTE_BLOCKS = 66;
        this.trainLocations = new ArrayList<>();
        this.switchPostions = new ArrayList<>();
        this.trackPath = ("YY,K,L,M,N,O,P,Q,N,R,S,T,U,V,W,X,Y,Z,F,E,D,C,B,A,D,E,F,G,H,I,ZZ|J").split(",");
        this.trains = new ArrayList<>();

        this.green = new ArrayList<>();
        this.red = new ArrayList<>();
        this.blockClosings = new ArrayList<>();
        this.closures = new ArrayList<>();
        this.GREEN_NEXT_STATION = new ArrayList<>(); 
        this.RED_NEXT_STATION = new ArrayList<>();
        
        //GREEN_NEXT_STATION = greenStationCreation(); 
        setNextStations();       
        iniTrack();
        iniTrains(nTrains);         
    }
    
    public CTC() 
    {
        numberOfTrains = 10;
        this.closings = 0;
        this.GREEN_ROUTE_BLOCKS = 150;
        this.RED_ROUTE_BLOCKS = 66;
        this.trackPath = ("YY,K,L,M,N,O,P,Q,N,R,S,T,U,V,W,X,Y,Z,F,E,D,C,B,A,D,E,F,G,H,I,ZZ|J").split(",");
        this.trains = new ArrayList<>();
        this.green = new ArrayList<>();
        this.red = new ArrayList<>();
        this.blockClosings = new ArrayList<>();
        this.closures = new ArrayList<>();
        
        this.GREEN_NEXT_STATION = new ArrayList<>(); 
        this.RED_NEXT_STATION = new ArrayList<>();
    
        setNextStations();
        iniTrack();
        iniTrains(numberOfTrains);         
    }    

    public ArrayList<BlockSignalBundle> closeBlocks()
    {
        LineColor line;
        
        for(int i = 0; i < blockClosings.size(); i++)
        {          
            if(blockClosings.get(i)[0].equals("RED"))
            {
                line = LineColor.RED;
            }
            else
            {
                line = LineColor.GREEN;
            }
            closures.add(new BlockSignalBundle(Integer.parseInt(blockClosings.get(i)[1]),0,0.0,0,line));
        }
        
        return closures;
    }

    //update the local train locations, block signals, switches positions    
    public ArrayList<BlockSignalBundle> getRouteInfo()//int line, int block, double velocity, int authority, int destination)
    {
        updateTrainLocations();
        ArrayList<BlockSignalBundle> trainRouteInfo = new ArrayList<>(); 
        
        for(TrainsClass get: this.trains)
        {
           //System.out.println("CTC Train Authority:" + get.authority); 
           //System.out.println("CTC Train Distance So Far:" + (this.trainLocations.get(this.trains.indexOf(get)).distanceSoFar)); 
            //(get.authority == 0)
           if((get.authority == 0) && (this.trainLocations.get(this.trains.indexOf(get)).distanceSoFar) == 0 )
           {
               //System.out.println("Sending New Signal");
               trainRouteInfo.add(getNextStation(get));
               this.trains.get(this.trains.indexOf(get)).StopIndex++;
               //trainRouteInfo.add(new BlockSignalBundle(trains[i].authority, Integer.parseInt(trains[i].destination), trains[i].speed, Integer.parseInt(trains[i].block), trains[i].line));               
           }
            //trainRouteInfo.add(new BlockSignalBundle(trains[i].authority, Integer.parseInt(trains[i].destination), trains[i].speed, Integer.parseInt(trains[i].block), trains[i].line));
        }
        //System.out.println(trainRouteInfo.size());
        return trainRouteInfo;//new BlockSignalBundle(line, block, velocity, authority, destination);
    }
    
    private void updateTrainLocations()
    {
        for(int i = 0; i < this.trainLocations.size(); i++)
        {
            //System.out.println("Train Location " + i + " previous block: " + this.trainLocations.get(i).prevBlock);
            //System.out.println("Train Location " + i + " current block: " + this.trainLocations.get(i).currentBlock);
            //System.out.println("Trains " + i + " current block: " + this.trainLocations.get(i).prevBlock);
            if(this.trainLocations.get(i).prevBlock == Integer.parseInt(this.trains.get(i).block))
            {
                this.trains.get(i).authority--;
                this.trains.get(i).block = Integer.toString(this.trainLocations.get(i).currentBlock);
                if((this.trains.get(i).section_current = returnSection(this.trains.get(i).line, this.trains.get(i).block)).equals("Not Found"))
                {
                    System.out.println("Section Not Found: " + this.trains.get(i).block);
                }
            }
        }
        
    }
    
    private String returnSection(LineColor line, String blockID)
    {
        //System.out.println("Line Color: " + line);
        if(blockID.equals("0"))
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
                    if(section[1].equals(blockID))
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
                        if(section[1].equals(blockID))
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
        int newDestination;
        int newAuthority;
        
        if(LineColor.GREEN == train.line)
        {
            newAuthority = this.GREEN_NEXT_STATION.get(train.StopIndex).AUTHORITY;
            newDestination = this.GREEN_NEXT_STATION.get(train.StopIndex).NEXTBLOCKID;
            return new BlockSignalBundle(newAuthority, (newDestination), (70*1000/36000),Integer.parseInt(train.block), train.line);//returnSection(LineColor.GREEN, newDestination))
        }
        return null;        
    }
        
    public int calculateAuthority(int trainID)
    {
        int authority = 0;
        //String[] path = trackPath.split(",");
        String destination = this.trains.get(trainID).destination;
        int i;
        int destinationIndex = -1;
        int currentIndex = -1;
        
          
        if(this.trains.get(trainID).section_current.equals(this.trains.get(trainID).section_destination))
        {
            for(i = 0; i < trackPath.length; i++)
            {
                if(this.trains.get(trainID).section_current.equals(trackPath[i]))
                {
                    break;
                }
            }
            
            for (String get : path.get(i)) {
                if (get.equals(this.trains.get(trainID).block)) {
                    currentIndex = Integer.parseInt(get);
                }
                if (get.equals(this.trains.get(trainID).destination)) {
                    destinationIndex = Integer.parseInt(get);
                }               
            }
            
            if( destinationIndex - currentIndex > 0 &&  this.trainLocations.get(trainID).currentBlock - this.trainLocations.get(trainID).prevBlock > 0)
            {
                return destinationIndex - currentIndex;
            }
            else
            {
                if(destinationIndex - currentIndex < 0 &&  this.trainLocations.get(trainID).currentBlock - this.trainLocations.get(trainID).prevBlock < 0)
                {
                    return currentIndex - destinationIndex;
                }
                else
                {
                       if(this.trains.get(trainID).line == LineColor.RED)
                       {
                            return Math.abs(destinationIndex - currentIndex) + this.RED_ROUTE_BLOCKS;
                       }
                       else                            
                       {
                           if(this.trains.get(trainID).line == LineColor.GREEN)
                           {
                                return Math.abs(destinationIndex - currentIndex) + this.GREEN_ROUTE_BLOCKS;
                           }
                       }
                }
            }
            
        }
        return authority;
    }
    
    private void iniTrains(int numberOfTrains)
    {
        numberTrains = new String[numberOfTrains+1];
        String[][] trainInfo = new String[numberOfTrains][4];
        numberTrains[0] = "Train #";
        
        for(int i = 0; i < numberOfTrains; i++)
        {
            trains.add(i, new TrainsClass(LineColor.YARD, "", "0", 0 , "0", ""));
            numberTrains[i+1] = Integer.toString(i+1);
            trainInfo[i] = new String[] {Integer.toString(i+1), "", "", "Yard"};
        }
    }
    
    public String[] returnTrainNumberArray()
    {
        return numberTrains;
    }
    
    public String[][] returnTrainInfoArray()
    {
        String[][] trainInfo = new String[numberTrains.length][4];
        
        for(int i  = 0; i < trainInfo.length; i++)
        {
             trainInfo[i]= new String[] {Integer.toString(i), trains.get(i).lineColor(), trains.get(i).section_current, trains.get(i).block};
        }
        
        return trainInfo;
    }
    
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
                    this.red.add(new String[] {block[1], block[2]});
                }
                else
                {
                    if(!greenSec.contains(block[1]))
                    {
                        greenSec.add(block[1]);
                    }
                    green.add(new String[] {block[1], block[2]});                   
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

    //Completed
    private boolean trainExistsOnBlock(int blockID)
    {
        for(TrainLocation train: this.trainLocations)
        {
            if(train.currentBlock == blockID)
            {
                return true;
            }
        }
        
        return false;
    }
    
    //Completed
    public void updateBlockInfo(ArrayList<BlockSignalBundle> blockOccupiences, ArrayList<Switch> newSwitches)
    {        
        for(BlockSignalBundle blockInfo: blockOccupiences)
        {
            if(!this.closures.contains(blockInfo))
            {
                if(!trainExistsOnBlock(blockInfo.BlockID) && blockInfo.Closed)
                {
                    this.closures.add(blockInfo);
                }
            }                                   
            else
            {
                if(!trainExistsOnBlock(blockInfo.BlockID) && blockInfo.Closed)
                {
                    this.closures.set(this.closures.indexOf(blockInfo), blockInfo);
                }
                else
                {
                    this.closures.remove(blockInfo);
                }
            }
        }
        
        this.switchPostions = newSwitches;       
    }
    
    //Completed
    public void setTrainLocations(ArrayList<TrainLocation> trainLocations)
    {
        this.trainLocations = trainLocations;
    }      
    
    public DispatchBundle getDispatcher()
    {
        DispatchBundle train = null;
        for(int i = 0; i < this.trainLocations.size(); i++)
        {
            if(this.trainLocations.get(i).currentBlock == 0)
            {
                train = new DispatchBundle(new BlockSignalBundle(4, 65, (70.0*1000/(3600)),0, LineColor.YARD), i , LineColor.GREEN);
                this.trains.get(i).line = LineColor.GREEN;
                break;
            }
            
        }        
        return train;    
    }
    
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
    
    public void SetMode()
    {
        
    }
}   
