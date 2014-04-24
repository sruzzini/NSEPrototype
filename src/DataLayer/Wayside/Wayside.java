/******************************************************************************
 * 
 * Wayside class
 * 
 * Developed by AJility
 * April 2014
 * CoE 1186
 * 
 * Contributers:
 *  Nathaniel W. Hachten
 *
 *****************************************************************************/

package DataLayer.Wayside;

import DataLayer.Bundles.*;
import DataLayer.EnumTypes.*;
import DataLayer.TrackModel.*;
import java.io.*;
import java.util.*;

/**
 * <h1>Wayside</h1>
 * <p>
 * The Wayside class acts as a communication helper between the CTC office and the
 * Track Controllers. It forwards signals from the CTC to the appropriate Track Controller. </p>
 * <p>
 * It also provides the means for the NSE simulation to setup and start and pause the 
 * Track Controllers. </p>
 *
 * @author nwhachten
 * @version 2.0
 * @since 2014-04-02
 */
public final class Wayside 
{
    private int controllerCount;            //the total number of controllers
    private TrackController[] controllers;  //array containing the controllers
    private final TrackModel track;         //reference to the trackmodel
    
    /**
     * This method is the constructor for the Wayside class. It handles instantiation 
     * of all Track Controllers 
     * @param track This is a reference to the TrackModel class that is used in the simulation
     */
    public Wayside(TrackModel track)
    { 
        BufferedReader br;                                              //will read in from file to get wayside configuration
        this.track = track;
        
        try 
        {
            String fileName = "wayside_layout.txt";                     //name of the file will wayside layout info
            String currentLine;
            br = new BufferedReader(new FileReader(fileName));
            
            controllerCount = Integer.parseInt(br.readLine());          //the first line in the file indicates the number of controllers
            this.controllers = new TrackController[controllerCount];    //creates an appropriately sized array
            
            
            String[] controllerInfo;                                    //each line is broken down like this... <id>:<line>:<blocks comma seperated>
            String[] blocks;
            int[] blockNums;
            LineColor line;
            for (int i = 0; i < controllerCount; i++)                   //iterate loop to get info about each controller
            {
                currentLine = br.readLine();
                controllerInfo = currentLine.split(":");                //split line into id:line:blocks
                if (controllerInfo[1].equals("Green"))                  //sets the line 
                {
                    line = LineColor.GREEN;
                }
                else
                {
                    line = LineColor.RED;
                }
                blocks = controllerInfo[2].split(",");                 //split the blocks
                blockNums = new int[blocks.length];
                for (int j =0; j < blockNums.length; j++)
                {
                    blockNums[j] = Integer.parseInt(blocks[j]);         //convet blocks to int
                }
                
                controllers[i] = new TrackController(Integer.parseInt(controllerInfo[0]), line, blockNums);         //create new TrackController and put in array. controllerInfo[0] is unique controller id
               
                
            }
        
        }
        //catch any file errors and print to stdout
        catch (FileNotFoundException e)
        {
            //catch error
            e.printStackTrace();
        }
        catch (IOException e1)
        {
            //catch error
            e1.printStackTrace();
        }
        
        //call methods for setting the block info and switch info of the controllers
        this.setBlockInfoArray(track.TheLines.get(0).TheBlocks, LineColor.GREEN);
        this.setBlockInfoArray(track.TheLines.get(1).TheBlocks, LineColor.RED);
        this.setSwitchArray(track.TheLines.get(0).TheSwitches);
        this.setSwitchArray(track.TheLines.get(1).TheSwitches);
        
        //create the plc's based on each controllers id
        this.configurePLCs();
  
    }
    
    //getController(int n) returns the nth controller in the controller array
    //Parameters:
    //  int n - index into the array
    //Returns - TrackController, reference to that controller object
    public TrackController getController(int n)
    {
        return this.controllers[n];
    }
    
    //getControllerNames() accesses each controllers toString() method and places the returned string
    //in an array of strings
    //Returns - String[], array of strings corresponding to the array of controllers
    public String[] getControllerNames()
    {
        String[] names = new String[controllerCount];
        int i = 0;
        
        for (TrackController tc : this.controllers)
        {
            names[i++] = tc.toString();
        }
        
        
        return names;
    }
    
    //getControllers() returns the array of controller objects
    //Returns - TrackController[], the array of controllers
    public TrackController[] getControllers()
    {
        return this.controllers;
    }
    
    
    /**
     * This method returns an array of signals indicating which blocks are currently
     * being observed as occupied.
     * @return ArrayList<BlockSignalBundle> This returns an array containing info for all occupied
     * blocks in the track model.
     */
    public ArrayList<BlockSignalBundle> getOccupancyInfo()
    {
        ArrayList<BlockSignalBundle> occupiedBlocks;
        occupiedBlocks = new ArrayList<>();                     //create new arraylist of BlockSignalBundles to indicate which blocks are occupied
        BlockSignalBundle currentSignal;
        
        for (TrackController tc : this.controllers)             //iterate through all controllers
        {
            for (Block b : tc.getOccupiedBlocks())              //iterate through all occupied blocks that tc can access
            {
                currentSignal = new BlockSignalBundle(b.getAuthority(),b.getDestination(),b.getSpeedLimit(),b.getBlockID(),tc.getLine());   //create a new signal and add to arraylist
                occupiedBlocks.add(currentSignal);
            }
        }

        return occupiedBlocks;
    }
    
    //getSwitchInfo() returns an arraylist of all of the switches on the track
    //Returns - ArrayList<Switch>, unsorted list of all switches
    public ArrayList<Switch> getSwitchInfo()
    {
        ArrayList<Switch> switchInfo;
        switchInfo = new ArrayList<>();                 //create new arraylist for switches
        boolean addSwitch;
        
        for (TrackController tc : this.controllers)     //iterate through controllers
        {
            for (Switch s : tc.getSwitchInfo())         //iterate through switches
            {
                addSwitch = true;                       //assume this switch has not been added yet
                for (Switch s1 : switchInfo)            //iterate through switches that were already added
                {
                    if (s.matches(s1))                  //if the switch was already added
                    {
                        addSwitch = false;              //then do not add this switchs
                        break;
                    }
                }
                if (addSwitch)
                {
                    switchInfo.add(s);                  //add the the switch to the list
                }
                
            }
        }

        return switchInfo;
    }
    
    //sendDispathSignal(DispathBundle packet) is used for dispatching a train from the yard to the track
    //A train can be placed on either the Red Line or the Green Line
    //Paramerters:
    //  DispatchBundle packet - this bundle indicates which line to dispatch to as well as normal BlockSignalBundle
    //                          info like Authority, Speed, and Destination
    public void sendDispatchSignal(DispatchBundle packet)
    {
        if (packet == null)
        {
            //System.out.println("Wayside - sendDispatchBundle - packet is null");        //if the packet that was passed is null, print to screen. this can happen when all trains are away
            return;
        }
        this.track.setDispatchSignal(packet.copy());                                        //notify the track that a train needs dispatched

        //based on where the dispatch is sending to, send the appropriate BlockSignalBundle to the first block
        if (packet.toLine == LineColor.GREEN)
        {
            this.sendTravelSignal(new BlockSignalBundle(packet.Authority, packet.Destination, packet.Speed, 152, LineColor.GREEN), null);   //send a travel signal to the first block with no route info
        }
        else
        {
            this.sendTravelSignal(new BlockSignalBundle(packet.Authority, packet.Destination, packet.Speed, 77, LineColor.RED), null);      //send a travel signal to the first block with no route info
        }
    }
    
    //sendSwitchStateSignal(Switch packet) is used by not Wayside or TrackController objects to manually set switches. Its primary
    //user is the CTC Office
    //Parameters:
    //  Switch packet - a switch object that will be used as a command to set the straight (boolean) field of the switch that matches line and id
    public void sendSwitchStateSignal(Switch packet)
    {
        //if switch is null, do nothing
        if (packet == null)
        {
            System.out.println("Wayside - sendSwitchStateSignal - packet is null");         //if the switch passed is null, print to stdout
        }
        else
        {
            for (TrackController tc : this.controllers)                                         //iterate through all controllers
            {
                if (tc.getLine() == packet.LineID && tc.containsBlock(packet.ApproachBlock) )   //if this controller is of the same line and the controller contains the approach block, then this controller
                                                                                                //controls this switch
                {
                    tc.sendSwitchStateSignal(packet.copy());   //create a new switch command and send to the controller
                    break;                                      //stop searching controllers
                }
            }
        }
    }
 
    
    //sendTravelSignal(ArrayList<BlockSignalBundle> signal) is the public interface
    //for sending routes to track controllers. It strips the first signal and sends 
    //signal and route to a private method that will send to the correct controller
    //Parameters:
    //  ArrayList<BlockSignalBundle> signal - a route
    public void sendTravelSignal(ArrayList<BlockSignalBundle> signal)
    {
        //if the route is null, do nothing
        if (signal == null)
        {
            System.out.println("Wayside - sendTravelSignal(ArrayList) - list is null");         //print to stdout if route is null
        }
        else
        {
            if (signal.size() > 0)                                  //if route size is greater than 0
            { 
                BlockSignalBundle packet = signal.get(0);           //get the first signal
                this.sendTravelSignal(packet,signal);               //call private method 
            }
        }
    }
    
    //setBlockClosing(BlockSignalBundle packet) is used to closed a block. It is very similar to
    //sendTravelSignal() when just one signal is sent
    //Parameters:
    //  BlockSignalBundle packet - the specially constructed bundle to indicate closing. speed = -1 is flag
    public void setBlockClosing(BlockSignalBundle packet)
    {
        //if the packet is null, do nothing
        if (packet == null)
        {
            System.out.println("Wayside - setBlockClosing - packet is null");           //print to stdout if packet is null
        }
        else
        {
            for (TrackController tc : this.controllers)                                 //iterate throught controllers
            {
                if (tc.getLine() == packet.LineID && tc.containsBlock(packet.BlockID))  //if the block belongs to this controller
                {
                    tc.setBlockClosing(packet.copy());                                  //then call setBlockClosing for this controller
                    break;                                                              //and stop[ searching controllers
                }
            }
        }
        
    }
    
    //StartSimulation() is used to start all controller threads
    public void StartSimulation()
    {
        this.startControllers();            //put each tc into a thread and start those bad boys
    }
    
    //configurePLCs() is called after the TrackControllers are created and initialized with
    //the their unique id. This method calls each track controllers setPLC() method that will
    //set its plc program based on its id
    private void configurePLCs()
    {
        for (TrackController tc : this.controllers)
        {
            tc.setPLC();
        }
    }
    
    //sendTravelSignal(BlockSignalBundle packet, ArrayList<BlockSignalBundle> route) is a private method
    //that is called by sendTravelSignal(ArrayList<BlockSignalBundle> signal). This method sends the first
    //signal in the route, and the route to the controller
    //Parameters:
    //  BlockSignalBundle packet - the first signal in the route
    //  ArrayList<BlockSignalBundle> route - the sequence of blocks to get to destination
    private void sendTravelSignal(BlockSignalBundle packet, ArrayList<BlockSignalBundle> route)
    {
        //if the first signal is null, do nothing
        if (packet == null)
        {
            System.out.println("Wayside - sendTravelSignal array version - packet is null");        //print message to screen indicating that first signal is null
        }
        else
        {
            //get the line and block number of the packet
            LineColor line = packet.LineID;
            int blockNum = packet.BlockID;

            for (TrackController tc : this.controllers)     //iterate throught controllers
            {
                if (tc.getLine() == line && tc.containsBlock(blockNum)) //if this controller has access to this block
                {
                    tc.sendTravelSignal(packet.copy(), route);          //send a copy of the first packet and the route
                    break;                                              //stop searching through controllers
                }   
            }
        }
    }
    
    //setBlockInfoArray(ArrayList<Block>, LineColor line) sets the blockinfo for
    //all of the controllers by adding a block to a controller if that controller
    //should be able to access that block 
    private void setBlockInfoArray(ArrayList<Block> blockArray, LineColor line)
    {
        for (Block b : blockArray)                          //iterate through all blocks
        {
            for (TrackController tc : this.controllers)     //iterate through all controllers
            {
                if (tc.getLine() == line && tc.containsBlock(b.getBlockID()))   //if this block should be accessed by this controller
                {
                    tc.addBlock(b);                                             //then add the block to the controller
                }
                      
            }
        }    
    }
    
    //setSwitchArray(List<Switch> switchArray) sets the switchinfo
    //fo all of the controllers by adding a switch to a controller if that controller
    //has the right to control that switch
    private void setSwitchArray(List<Switch> switchArray)
    {
        LineColor line;
        
        for (Switch s : switchArray)                    //iterate through all switches
        {
            line = s.LineID;
            for (TrackController tc : this.controllers) //iterate through all controllers
            {
                if (tc.getLine() == s.LineID && ( tc.containsBlock(s.ApproachBlock)))   //if this switch belongs to this controller
                {
                    tc.addSwitch(s);                                                        //add this switch to this controller
                }
                      
            }
        }
    }
    
    //startController() - creates and starts thread for each TrackController object
    private void startControllers()
    {
        for (TrackController tc : this.controllers)
        {
            new Thread(tc).start();
        }
        
        
    }
    
}
