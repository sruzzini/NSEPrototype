/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.CTC;
import DataLayer.Bundles.*;
import DataLayer.EnumTypes.LineColor;
import DataLayer.TrackModel.Switch;

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
    private ArrayList<String[]> green;
    private ArrayList<String[]> red;
    
    private ArrayList<String[]> blockClosings;
    private int closings;
    
    private String[] greenSections;
    private String[] redSections;
    
    private TrainsClass[] trains;
    private String[] numberTrains;
    private ArrayList<BlockSignalBundle> closures;
    //private String pathDefine = "YY,K,L,M,N,O,P,Q,N,R,S,T,U,V,W,X,Y,Z,F,E,D,C,B,A,D,E,F,G,H,I,ZZ|J";
    private String[] trackPath = ("YY,K,L,M,N,O,P,Q,N,R,S,T,U,V,W,X,Y,Z,F,E,D,C,B,A,D,E,F,G,H,I,ZZ|J").split(",");
    private ArrayList<String[]> path;
    /**
     * Creates new form CTCGUI
     */
    public CTC(int nTrains) 
    {
        nTrains = 10;
        trains = new TrainsClass[nTrains];
        green = new ArrayList<>();
        red = new ArrayList<>();
        blockClosings = new ArrayList<>();
        closures = new ArrayList<>();
        closings = 0;
               
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
    
    public BlockSignalBundle[] getRouteInfo()//int line, int block, double velocity, int authority, int destination)
    {
        BlockSignalBundle[] trainRouteInfo = new BlockSignalBundle[trains.length]; 
        
        int line = 0;
        
        for(int i = 0; i < trains.length; i++)
        {
            if(blockClosings.get(i)[0].equals("RED"))
            {
                line = 1;
            }
            else
            {
                line = 0;
            }
            
            trainRouteInfo[i] = new BlockSignalBundle(line, Integer.parseInt(trains[i].block), trains[i].speed, trains[i].authority, Integer.parseInt(trains[i].destination), trains[i].line);
        }
        return trainRouteInfo;//new BlockSignalBundle(line, block, velocity, authority, destination);
    }
    
    
    public int calculateAuthority(TrainsClass train)
    {
        int authority = 0;
        //String[] path = trackPath.split(",");
        String destination = train.destination;
        int i;
        int destinationIndex = -1;
        int currentIndex = -1;
        
        if(train.section_current.equals(train.section_destination))
        {
            for(i = 0; i < trackPath.length; i++)
            {
                if(train.section_current.equals(trackPath[i]))
                {
                    break;
                }
            }
            
            for(int j = 0; j < path.get(i).length; j++)
            {
                if(path.get(i)[j].equals(train.block))
                {
                    currentIndex = j;
                }
                
                if(path.get(i)[j].equals(train.destination))
                {
                    destinationIndex = j;
                }               
            }
        
            return Math.abs(currentIndex - destinationIndex);
        }
    
        for(i = 0; i < path.size(); i++)
        {
            if(path.get(i).equals(train.section_current))
            {
                if(train.line == LineColor.GREEN)
                {
                    if(train.section_current.equals("N"))// || train.section_current.equals("D") || train.section_current.equals("E") || train.section_current.equals("F"))
                    {
                        if(train.section_destination.equals("Q") || train.section_destination.equals("O") || train.section_destination.equals("P"))
                        {
                            
                        }
                    }
                }
            }
        }
        
        if(train.block.equals("Yard"))
        {
            authority = 1;
            for(int i = 0; i < path.size(); i++)
            {
                for(int j = 0; j < green.size(); j++)
                {
                    //if()
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
    
    public void updateBlockInfo(BlockSignalBundle[] packet, Switch[] switches)
    {
      for(BlockSignalBundle pack: packet)
      {
          for(int i = 0; i < blockClosings.size(); i++)
          {
              if(closures.get(i).BlockID == pack.BlockID)
              {
                  if(!pack.Closed)
                  {
                      closures.remove(i);
                  }
                  closures.set(i, pack);
              }                     
          }
      }
    }
    
    //public void updateTrainLocations(TrainLocation[] loc)
    //{        
    //    
    //}
    
    //public BlockSignalBundle[] getRouteInfo()
      
}   
 

