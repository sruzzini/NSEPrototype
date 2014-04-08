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
    private final int[] GREEN_ROUTE_AUTHORITY;
    private final int[] RED_ROUTE_AUTHORITY;
    private String[] greenSections;
    private String[] redSections;
    private String[] numberTrains;
    private String[] trackPath;
    private TrainsClass[] trains;
    private ArrayList<String[]> green;
    private ArrayList<String[]> red;    
    private ArrayList<String[]> blockClosings;    
    private ArrayList<String[]> path;
    private ArrayList<BlockSignalBundle> closures;    
    private ArrayList<TrainLocation> trainLocations;
    private ArrayList<Switch> switchPostions;
    
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
        this.trains = new TrainsClass[nTrains];

        this.green = new ArrayList<>();
        this.red = new ArrayList<>();
        this.blockClosings = new ArrayList<>();
        this.closures = new ArrayList<>();
        this.RED_ROUTE_AUTHORITY = new int[] {};
        this.GREEN_ROUTE_AUTHORITY = new int[] {4, 10, 4, 12};
               
        iniTrack();
        iniTrains();         
    }
    
    public CTC() 
    {
        numberOfTrains = 10;
        this.closings = 0;
        this.GREEN_ROUTE_BLOCKS = 150;
        this.RED_ROUTE_BLOCKS = 66;
        this.trackPath = ("YY,K,L,M,N,O,P,Q,N,R,S,T,U,V,W,X,Y,Z,F,E,D,C,B,A,D,E,F,G,H,I,ZZ|J").split(",");
        this.trains = new TrainsClass[10];
        this.green = new ArrayList<>();
        this.red = new ArrayList<>();
        this.blockClosings = new ArrayList<>();
        this.closures = new ArrayList<>();
        this.RED_ROUTE_AUTHORITY = new int[] {};
        this.GREEN_ROUTE_AUTHORITY = new int[] {};        
               
        iniTrack();
        iniTrains();         
    }    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     * @return 
     */
    
    
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
        ArrayList<BlockSignalBundle> trainRouteInfo = new ArrayList<BlockSignalBundle>(); 
        
        for(int i = 0; i < trains.length; i++)
        {
            trainRouteInfo.add(new BlockSignalBundle(trains[i].authority, Integer.parseInt(trains[i].destination), trains[i].speed, Integer.parseInt(trains[i].block), trains[i].line));
        }
        return trainRouteInfo;//new BlockSignalBundle(line, block, velocity, authority, destination);
    }
    
    
    
    public int calculateAuthority(int trainID)
    {
        int authority = 0;
        //String[] path = trackPath.split(",");
        String destination = this.trains[trainID].destination;
        int i;
        int destinationIndex = -1;
        int currentIndex = -1;
        
          
        if(this.trains[trainID].section_current.equals(this.trains[trainID].section_destination))
        {
            for(i = 0; i < trackPath.length; i++)
            {
                if(this.trains[trainID].section_current.equals(trackPath[i]))
                {
                    break;
                }
            }
            
            for(int j = 0; j < path.get(i).length; j++)
            {
                if(path.get(i)[j].equals(this.trains[trainID].block))
                {
                    currentIndex = Integer.parseInt(path.get(i)[j]);
                }
                
                if(path.get(i)[j].equals(this.trains[trainID].destination))
                {
                    destinationIndex = Integer.parseInt(path.get(i)[j]);
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
                       if(this.trains[trainID].line == LineColor.RED)
                       {
                            return Math.abs(destinationIndex - currentIndex) + this.RED_ROUTE_BLOCKS;
                       }
                       else                            
                       {
                           if(this.trains[trainID].line == LineColor.GREEN)
                           {
                                return Math.abs(destinationIndex - currentIndex) + this.GREEN_ROUTE_BLOCKS;
                           }
                       }
                }
            }
            
        }
        return authority;
    }
    private void iniTrains()
    {
        numberTrains = new String[trains.length+1];
        String[][] trainInfo = new String[trains.length][4];
        numberTrains[0] = "Train #";
        
        for(int i = 0; i < trains.length; i++)
        {
            trains[i] = new TrainsClass(null, "","Yard", 0, 0.0,"", "");
            numberTrains[i+1] = Integer.toString(i+1);
            trainInfo[i] = new String[] {Integer.toString(i+1), "", "", "Yard"};
        }
        //CTCGUI.setTrainLists
        //routeTrainTrainDrop.setModel(new javax.swing.DefaultComboBoxModel(trainNumbers));
        //trainLocationTable.setModel(new javax.swing.table.DefaultTableModel(trainInfo,new String [] {"Train", "Line","Section","Block"}));
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
             trainInfo[i]= new String[] {Integer.toString(i), trains[i].lineColor(), trains[i].section_current, trains[i].block};
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
                    red.add(new String[] {block[1], block[2]});
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
        System.out.println(redSec.size());
        greenSections = greenSec.toArray(new String[0]);
        //System.out.println(greenSections.length);
        redSections = redSec.toArray(new String[0]);
        System.out.println(redSections.length);
	System.out.println("Done");
    }

    public int isBlockAlreadyClosed(String block[])
    {
        for(int i = 0; i < closings;i++)
        {
            if(blockClosings.get(i)[0].equals(block[0]) && blockClosings.get(i)[1].equals(block[1]))
            {
                return i;
            }
        }
        return -1;
    }
    
    public void updateBlockInfo(ArrayList<BlockSignalBundle> blockOccupiences, ArrayList<Switch> newSwitches)
    {
      for(BlockSignalBundle blockInfo: blockOccupiences)
      {
          for(int i = 0; i < blockClosings.size(); i++)
          {
              if(closures.get(i).BlockID == blockInfo.BlockID)
              {
                  if(!blockInfo.Closed)
                  {
                      closures.remove(i);
                  }
                  closures.set(i, blockInfo);
              }                     
          }
      }
    }
    
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
                train = new DispatchBundle(new BlockSignalBundle(4, 65, 70.0,0, LineColor.YARD), i , LineColor.GREEN);
            }
            break;
        }        
        return train;
    }
}   
 

