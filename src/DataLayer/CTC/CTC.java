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
    
    private ArrayList<Switch> switchPostions;
    private ArrayList<TrainLocation> trainLocations;
    public ArrayList<BlockSignalBundle> BlockClosings;    
        
    public ArrayList<TrainsClass> trains;
    public final ArrayList<Station> GREEN_NEXT_STATION;
    public final ArrayList<Station> RED_NEXT_STATION;
    
    /**
     * Creates new form CTCGUI
     * @param nTrains
     */
    public CTC(int nTrains) 
    {
        
        
        this.numberOfTrains = nTrains;
        this.RED_ROUTE_BLOCKS = 66;
        this.GREEN_ROUTE_BLOCKS = 150;
        
        this.trainLocations = new ArrayList<>();
        this.switchPostions = new ArrayList<>();
        
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
    
    public CTC() 
    {
        this.numberOfTrains = 10;
        this.RED_ROUTE_BLOCKS = 66;
        this.GREEN_ROUTE_BLOCKS = 153;

        this.greenPath = new ArrayList<>();
        this.redPath = new ArrayList<>();
        
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
    public ArrayList<BlockSignalBundle> getRouteInfo()//int line, int block, double velocity, int authority, int destination)
    {
        updateTrainLocations();
        ArrayList<BlockSignalBundle> trainRouteInfo = new ArrayList<>(); 
        BlockSignalBundle newRoute;
        int trainIndex;
        
        for(TrainsClass get: this.trains)
        {
            trainIndex = this.trains.indexOf(get);
            if((this.trains.indexOf(get)) == 0)
            {
                //System.out.println("CTC Train Authority: " + (this.trains.get(trainIndex).Authority)); 
            }

           if((get.Authority == 0) && (this.trainLocations.get(this.trains.indexOf(get)).currentBlock) != (this.trainLocations.get(this.trains.indexOf(get)).prevBlock))
           {
                if((this.trainLocations.get(this.trains.indexOf(get)).distanceSoFar) == this.trains.get(this.trains.indexOf(get)).DistanceSoFar && !this.trains.get(trainIndex).Idle)
                {
                    this.trains.get(trainIndex).StopIndex++;
                    this.trains.get(trainIndex).setIdle(true);
                    newRoute = getNextStation(this.trains.get(trainIndex));
                    trainRouteInfo.add(newRoute);                    
                    
                    this.trains.get(trainIndex).Authority = newRoute.Authority;
                    this.trains.get(trainIndex).BlockDestination = Integer.toString(newRoute.Destination);
                    this.trains.get(trainIndex).SectionDestination = this.returnSection(this.trains.get(trainIndex).line, this.trains.get(trainIndex).BlockDestination);
                    this.trains.get(trainIndex).SectionDestination = this.returnStationBlock(this.trains.get(trainIndex).line, this.trains.get(trainIndex).BlockDestination);
 
                    //trainRouteInfo = this.calculateAuthority(trainIndex, this.trains.get(trainIndex).line);
                    //System.out.println(trainRouteInfo.size());
                    //break;
                    //System.out.println("CTC Train " + trainIndex + " is now Idle.");
                    //System.out.println("CTC Sending the new signal to block: " + newRoute.BlockID);
                    //System.out.println("CTC Stop Index for Train " + trainIndex + ": " + this.trains.get(trainIndex).StopIndex);
                    //System.out.println("CTC New Destination for Train " + trainIndex + ": " + newRoute.Destination);
               }
               //trainRouteInfo.add(new BlockSignalBundle(trains[i].authority, Integer.parseInt(trains[i].destination), trains[i].speed, Integer.parseInt(trains[i].block), trains[i].line));               
           }
           else
           {
               this.trains.get(trainIndex).setIdle(false);
           }
            //trainRouteInfo.add(new BlockSignalBundle(trains[i].authority, Integer.parseInt(trains[i].destination), trains[i].speed, Integer.parseInt(trains[i].block), trains[i].line));
        }
        //System.out.println("CTC Number of New Signals: " + trainRouteInfo.size());
        //System.out.println(trainRouteInfo.size());
        return trainRouteInfo;//new BlockSignalBundle(line, block, velocity, authority, destination);
    }

    public ArrayList<BlockSignalBundle> getRouteInfoNew()//int line, int block, double velocity, int authority, int destination)
    {
        updateTrainLocations();
        ArrayList<BlockSignalBundle> trainRouteInfo = new ArrayList<>(); 
        BlockSignalBundle newRoute;
        int trainIndex;
        
        for(TrainsClass get: this.trains)
        {
            trainIndex = this.trains.indexOf(get);
            if((this.trains.indexOf(get)) == 0)
            {
                //System.out.println("CTC Train Authority: " + (this.trains.get(trainIndex).Authority)); 
            }

           if((get.Authority == 0) && (this.trainLocations.get(this.trains.indexOf(get)).currentBlock) != (this.trainLocations.get(this.trains.indexOf(get)).prevBlock))
           {
                if((this.trainLocations.get(this.trains.indexOf(get)).distanceSoFar) == this.trains.get(this.trains.indexOf(get)).DistanceSoFar && !this.trains.get(trainIndex).Idle)
                {
                    this.trains.get(trainIndex).StopIndex++;
                    this.trains.get(trainIndex).setIdle(true);
                    newRoute = getNextStation(this.trains.get(trainIndex));
                    //trainRouteInfo.add(newRoute);                    
                    
                    this.trains.get(trainIndex).Authority = newRoute.Authority;
                    this.trains.get(trainIndex).BlockDestination = Integer.toString(newRoute.Destination);
                    this.trains.get(trainIndex).SectionDestination = this.returnSection(this.trains.get(trainIndex).line, this.trains.get(trainIndex).BlockDestination);
                    this.trains.get(trainIndex).SectionDestination = this.returnStationBlock(this.trains.get(trainIndex).line, this.trains.get(trainIndex).BlockDestination);
 
                    trainRouteInfo = this.calculateAuthority(trainIndex, this.trains.get(trainIndex).line);
                    //System.out.println(trainRouteInfo.size());
                    break;
                    //System.out.println("CTC Train " + trainIndex + " is now Idle.");
                    //System.out.println("CTC Sending the new signal to block: " + newRoute.BlockID);
                    //System.out.println("CTC Stop Index for Train " + trainIndex + ": " + this.trains.get(trainIndex).StopIndex);
                    //System.out.println("CTC New Destination for Train " + trainIndex + ": " + newRoute.Destination);
               }
               //trainRouteInfo.add(new BlockSignalBundle(trains[i].authority, Integer.parseInt(trains[i].destination), trains[i].speed, Integer.parseInt(trains[i].block), trains[i].line));               
           }
           else
           {
               this.trains.get(trainIndex).setIdle(false);
           }
            //trainRouteInfo.add(new BlockSignalBundle(trains[i].authority, Integer.parseInt(trains[i].destination), trains[i].speed, Integer.parseInt(trains[i].block), trains[i].line));
        }
        //System.out.println("CTC Number of New Signals: " + trainRouteInfo.size());
        //System.out.println(trainRouteInfo.size());
        return trainRouteInfo;//new BlockSignalBundle(line, block, velocity, authority, destination);
    }
        
    
    private void updateTrainLocations()
    {
        for(int i = 0; i < this.trainLocations.size(); i++)
        {   if(i == 0)
            {
                //System.out.println("Train Location " + i + " previous block: " + this.trainLocations.get(i).prevBlock);
                //System.out.println("Train Location " + i + " current block: " + this.trainLocations.get(i).currentBlock);
                //System.out.println("Trains " + i + " current block: " + this.trains.get(i).block);
                //System.out.println("Trains " + i + " current block: " + this.train.get(i).prevBlock)
            }
            if(this.trainLocations.get(i).prevBlock == Integer.parseInt(this.trains.get(i).BlockCurrent) && this.trainLocations.get(i).prevBlock != this.trainLocations.get(i).currentBlock)
            {                
                this.trains.get(i).Authority--;                
                this.trains.get(i).BlockCurrent = Integer.toString(this.trainLocations.get(i).currentBlock);
                this.trains.get(i).PreviousBlock = this.trainLocations.get(i).prevBlock;
                this.trains.get(i).SectionCurrent = this.returnSection(this.trains.get(i).line, this.trains.get(i).BlockCurrent);
                this.trains.get(i).StationCurrent = this.returnStationBlock(this.trains.get(i).line, this.trains.get(i).BlockCurrent);
            }
            
            if(this.trains.get(i).Authority == 0)
            {               
                this.trains.get(i).BlockCurrent = Integer.toString(this.trainLocations.get(i).currentBlock);
            }
            
            this.trains.get(i).setDistanceSoFar(this.trainLocations.get(i).distanceSoFar);
            
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
        }
        else //if(LineColor.RED == train.line)
        {
            newAuthority = this.RED_NEXT_STATION.get(train.StopIndex).AUTHORITY;
            newDestination = this.RED_NEXT_STATION.get(train.StopIndex).NEXTBLOCKID;            
        }
            
        return new BlockSignalBundle(newAuthority, (newDestination), ((double)(70*1000)/(double)(3600)),Integer.parseInt(train.BlockCurrent), train.line);//returnSection(LineColor.GREEN, newDestination))
        
    }
        
    public ArrayList<BlockSignalBundle> calculateAuthority(int trainID, LineColor line)
    {
        ArrayList<BlockSignalBundle> route = new ArrayList<>();        
        TrainsClass train = this.trains.get(trainID);
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
        
        if(Integer.parseInt(train.BlockCurrent) == 0)
        {
            for(int j = 0; j < path.size(); j++)
            {
                if(Integer.parseInt(train.BlockDestination) == path.get(j))
                {
                    for(int k = 0; k <= j; k++)
                    {
                        route.add(new BlockSignalBundle(j+1, Integer.parseInt(train.BlockDestination), (double)((double)70*1000)/(3600), path.get(k), train.line));
                    }
                    break;
                }
            }
        }
        else
        {
            for(int i = 0; i < path.size(); i++)
            {
                //System.out.println(path.get(i).toString());
                if(Integer.parseInt(train.BlockCurrent) == path.get(i))
                {
                    //System.out.println("CTC Train's Current Block: " + path.get(i).toString());
                    if(i > 0 && train.PreviousBlock == path.get(i-1))
                    {
                        //System.out.println("CTC Train's Previous Block: " + path.get(i-1).toString());
                        for(int j = 0; j < path.size(); j++)
                        {                            
                            //System.out.println("Train's Destination Block : " + (train.BlockDestination) + " | Checking Block: " + path.get(j));
                            if(Integer.parseInt(train.BlockDestination) == path.get(j))
                            {
                                if((j-i) > 0)
                                {
                                    for(int k = i; k <= j; k++)
                                    {
                                        route.add(new BlockSignalBundle(j-i-k, Integer.parseInt(train.BlockDestination), (double)((double)70*1000)/(3600), path.get(k), train.line));
                                    }                                        
                                }
                                else
                                {
                                    for(int k = i; k <= greenPath.size()-1; k++)
                                    {
                                        route.add(new BlockSignalBundle(150-k+j, Integer.parseInt(train.BlockDestination), (double)((double)70*1000)/(3600), path.get(k), train.line));
                                    }

                                    for(int k = 1; k <= j; k++)
                                    {
                                        route.add(new BlockSignalBundle(j-k, Integer.parseInt(train.BlockDestination), (double)((double)70*1000)/(3600), path.get(k), train.line));
                                    }                                    
                                }                                
                                break;
                            }
                            
                        }
                    }
                    else if(i == path.size()-1)
                    {
                        route.add(new BlockSignalBundle(1, Integer.parseInt(train.BlockDestination), (double)((double)70*1000)/(3600), path.get(i), train.line));
                    }
                    break;
                }            
            }
        }

        return route;
    }
    
    private void iniTrains(int numberOfTrains)
    {
        numberTrains = new String[numberOfTrains+1];
        String[][] trainInfo = new String[numberOfTrains][4];
        numberTrains[0] = "Train #";
        
        for(int i = 0; i < numberOfTrains; i++)
        {
            trains.add(new TrainsClass(LineColor.YARD, "", "0", 0 , "0", ""));
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
             trainInfo[i]= new String[] {Integer.toString(i), trains.get(i).lineColor(), trains.get(i).SectionCurrent, trains.get(i).BlockCurrent};
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
    
    public DispatchBundle getDispatcher()
    {
        //System.out.println("The number of trains: " + this.trainLocations.size());
        DispatchBundle train = null;
        for(int i = 0; i < this.trainLocations.size(); i++)
        {
            //System.out.println("Train " + i + "'s locations: " + this.trainLocations.get(i).currentBlock);
            if(this.trainLocations.get(i).currentBlock == 0)
            {
                train = new DispatchBundle(new BlockSignalBundle(4, 65, ((double)(70*1000)/(double)(3600)),0, LineColor.YARD), i , LineColor.GREEN);
                this.trains.get(i).line = LineColor.GREEN;
                this.trains.get(i).Authority = 5;
                this.trains.get(i).BlockDestination = Integer.toString(this.GREEN_NEXT_STATION.get(0).NEXTBLOCKID);
                this.trains.get(i).StationDestination = this.GREEN_NEXT_STATION.get(0).NEXTSTATION;
                this.trains.get(i).SectionDestination = this.returnSection(this.trains.get(i).line,Integer.toString(this.GREEN_NEXT_STATION.get(0).NEXTBLOCKID));
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
                //System.out.println(line);
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
    
    private String returnStationBlock(LineColor line, String blockID)
    {
        String station = "";
        switch(line)
        {
            case GREEN:
                for(Station stat: this.GREEN_NEXT_STATION)
                {
                    if(stat.BLOCKID == Integer.parseInt(blockID))
                    {
                        station = stat.STATION;
                        break;
                    }
                }
                break;
            case RED:
                for(Station stat: this.RED_NEXT_STATION)
                {
                    if(stat.BLOCKID == Integer.parseInt(blockID))
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
    
    public void SetMode()
    {
        
    }
    
}   

