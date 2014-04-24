/******************************************************************************
 * 
 * TrackController class
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
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * <h1>TrackController</h1>
 * <p>
 * This class contains the implementation for a track controller, allowing 
 * communication between the CTC and Track and running a PLC program that will
 * make track decisions based on inputs from the track. </p>
 *
 * @author nwhachten
 * @version 2.0
 * @since 2014-04-02
 */
public class TrackController implements Runnable {
    public PLC plcProgram;                                          //this controllers plc
    private ArrayList<Block> blockArray;                            //arraylist of blocks that this controller can access
    private HashMap<Integer, Block> blockInfo;                      //HashMap of the same blocks, used for access by block id
    private final int[] blocksInSector;                             //array of ints indicating which blocks to control
    private final Lock commandBlockLock;                            //lock for the commandBlockQueue
    private final ArrayList<BlockInfoBundle> commandBlockQueue;     //queue for all BlockInfoBundle commands
    private final Lock commandSignalLock;                           //lock for the commandSignalQueue
    private final ArrayList<BlockSignalBundle> commandSignalQueue;  //queue for all BlockSignalBundle commands
    private final Lock commandSwitchLock;                           //lock for the commandSwitchQueue
    private final ArrayList<Switch> commandSwitchQueue;             //queue for all Switch commands
    private final int id;                                           //TrackController unique identifier
    private final LineColor line;                                   //line that this controller resides on
    private int plcVersion;                                         //version of plc that this controller is running: 0 - default, 1 - V1, 2 - V2
    private final Lock processCommandsLock;                         //lock for when the controller starts processing commands, prevents new commands from being added right before the queues are emptied
    private ArrayList<Switch> switchArray;                          //arraylist of switches that this controller can access
    private HashMap<Integer, Switch> switchInfo;                    //HashMap of the ssame switches, used for access by switch id
    
    
   /**
    * This method is the constructor for TrackController. A track controller
    * is created based on its id, line, and blocks.
    * 
    * @param id The unique id pertaining to this controller
    * @param line The track line that this controller will service
    * @param blocksInSector An array that indicates which blocks this controller will communicate with
    */
    public TrackController(int id, LineColor line, int[] blocksInSector)  {
        this.id = id;
        this.line = line;
        this.blocksInSector = blocksInSector;
        this.commandSignalQueue = new ArrayList<>();
        this.commandSwitchQueue = new ArrayList<>();
        this.commandBlockQueue = new ArrayList<>();
        this.blockInfo = new HashMap();
        this.switchInfo = new HashMap();
        this.blockArray = new ArrayList<>();
        this.switchArray = new ArrayList<>();
        this.commandSignalLock = new ReentrantLock();
        this.commandBlockLock = new ReentrantLock();
        this.commandSwitchLock = new ReentrantLock();
        this.processCommandsLock = new ReentrantLock();
        this.plcVersion = 0;            //start with default plc
        
    }
    
    //addBlock(Block b) adds a block to this controllers data structures for block storage
    //Parameters:
    //  Block b - block to be added
    public void addBlock(Block b)
    {
        this.blockArray.add(b);                 //add to arraylist
        this.blockInfo.put(b.getBlockID(), b);  //put in HashMap, using id as its key and block as its value
    }
    
    //addSwitch(Switch s) adds a switch to this controllers data structurs for switch storage
    //Parameters:
    //  Switch s - switch to be added
    public void addSwitch(Switch s)
    {
        boolean addSwitch = false;              //start assuming switch will not be added
        
        //check if this controller is one of the controllers in the middle of the red line. those controllers 
        //have access to switches that they only need to read occupancy from, so any switches along those
        //extra blocks should not be added
        if (this.line == LineColor.RED)
        {
            if (this.id == 4)
            {
                if (s.SwitchID == 2 || s.SwitchID == 3)     //if this controller is id 4 and this switch is 2 or 3 and line is red, add is ok
                {
                    addSwitch = true;
                }
            }
            else if (this.id == 5)
            {
                if (s.SwitchID == 4 || s.SwitchID == 5)     //if this controller is id 5 and this switch is 4 or 5 and line is red, add is ok
                {
                    addSwitch = true;
                }
            }
            else                                //if line is red and controller is not 4 or 5, add is ok
            {
                addSwitch = true;
            }
        }
        else    //if line is green, add is ok
        {
            addSwitch = true;
        }
        if (addSwitch)
        {
            this.switchInfo.put(s.SwitchID, s);     //add switch to HashMap with id as key and switch as value
            this.switchArray.add(s);                //add switch to arraylist
        }
        
        //System.out.println("Added switch: " + s.switchID + " to TC: " + this.line + "" + this.id);
    }
    
    //contrainsBlock(int n) checks to see if this controller has access to block nu
    public boolean containsBlock(int n)
    {
        boolean result = false;             //assume result is no
        for (int b : this.blocksInSector)   //iterate through all blocks in sector by integer
        {
            if (b == n)                     //if we have a match
            {
                result = true;              //set result to true
                break;                      //and stop searching
            }
        }
        return result;
    }
    
    
    //emptyCommandQueues() - safely clears the command queues. locks are used to prevent ConcurrentModificationException
    //that results from threads accessing same data at same time. 
    public void emptyCommandQueues()
    {
        //set the lock, the clear the queue, then unlock
        commandBlockLock.lock();
        try 
        {
            this.commandBlockQueue.clear();
        }
        finally 
        { 
            commandBlockLock.unlock(); 
        }

        //set the lock, the clear the queue, then unlock
        commandSignalLock.lock();
        try 
        {
            this.commandSignalQueue.clear();
        }
        finally 
        { 
            commandSignalLock.unlock(); 
        }

        //set the lock, the clear the queue, then unlock
        commandSwitchLock.lock();
        try 
        {
            this.commandSwitchQueue.clear();
        }
        finally 
        {   
            commandSwitchLock.unlock(); 
        }
    }
    
    //getBlockInfo returns an arraylist of the blocks
    //Returns - ArrayList<Block> - the blocks in this controller
    public ArrayList<Block> getBlockInfo()
    {
        return this.blockArray;
    }       
    
    //getBlockNums() gets the intergers of the blocks for this controller
    //Returns - int[], the array of block ints
    public int[] getBlockNums() {
        return blocksInSector;
    }
    
    //getCommandBlockQueue() is used by the PLCTestPanel and returns all
    //BlockInfoBundle commands
    //Returns - ArrayList<BlockInfoBundle>, commands 
    public ArrayList<BlockInfoBundle> getCommandBlockQueue() {
        return commandBlockQueue;
    }
    
    //getCommandSignalQueue() is used by the PLCTestPanel and returns all
    //BlockSignalBundle commands
    //Returns - ArrayList<BlockSignalBundle>, commands 
    public ArrayList<BlockSignalBundle> getCommandSignalQueue() {
        return commandSignalQueue;
    }
    
    //getCommandSwitchQueue() is used by the PLCTestPanel and returns all
    //Switch commands
    //Returns - ArrayList<Switch>, commands 
    public ArrayList<Switch> getCommandSwitchQueue() {
        return commandSwitchQueue;
    }
    
    //getBlockTable() is used by the PLCTestPanel and returns the
    //HashMap for the blocks in this controller
    //Returns - HashMap<Integer, Block>, the block map
    public HashMap<Integer, Block> getBlockTable()
    {
        return this.blockInfo;
    }
    
    //getId() returns this controllers unique id
    public int getId() {
        return id;
    }
    
    //getLine() returns this controllers line
    public LineColor getLine() {
        return line;
    }
    
    //getOccupiedBlocks() finds all occupied blocks in this controller's range
    //and returns an arraylist of these blocks
    public ArrayList<Block> getOccupiedBlocks()
    {
        ArrayList<Block> occ;
        occ = new ArrayList<>();            //create arraylist to stroe occupied blocks
        
        for (Block b : this.blockArray)     //iterate through all blocks
        {
            if (b.isOccupied())             //if the block is occupied
            {
                occ.add(b.copy());          //add a copy to the arraylist
            }
        }
        return occ;                         //return the occupied blocks
    }
    
    //getPLCVersion returns this controllers currently running plc version
    public int getPLCVersion()
    {
        return this.plcVersion;
    }
    
    //getSwitchInfo() returns this controllers switches
    //Returns - ArrayList<Switch>, list of copied switches
    public ArrayList<Switch> getSwitchInfo()
    {
        ArrayList<Switch> copiedArray = new ArrayList<>();  //crewate new arraylist to hold the switches
        for (Switch s : this.switchArray)                   //iterate through all switches
        {
            copiedArray.add(s.copy());                      //add a copy of the switch
        }
        return copiedArray;                                 //return the switches
    }
    
    
    //loadPLC(String plcVersion) will set the plcVersion variable according
    //to the passed in string and then make a call to setPLC() to change the currently running plc program 
    //if neccessary
    public void loadPLC(String plcVersion)
    {
        boolean changePLC = false;                          //assume keeping the current plc
        if (plcVersion != null && plcVersion.length() > 0)  //if the passed in string is valid
        {
            if (plcVersion.equals("Default"))               //if string is default
            {
                this.plcVersion = 0;                        //set version to 0
                changePLC = true;                           //and change it
            }
            else if (plcVersion.equals("Version1"))
            {
                this.plcVersion = 1;
                changePLC = true;
            }
            else if (plcVersion.equals("Version2"))
            {
                this.plcVersion = 2;
                changePLC = true;
            }
        }
        if (changePLC)
        {
            this.setPLC();              //will create and set a new plc
        }
    }

    //run() satisfies the implements runnable interface of TrackController
    //it is a loop that continusoly runs the plc program and then processes the command queues
    @Override
    public void run()
    {
        Commands c;// = new Commands();
        while (true)                    //run forever
        {
            c = this.plcProgram.runPLCProgram(this.blockSnapShot(), this.switchSnapShot()); //get the commands from the plc 
            
            //lock processCommands while this is taking place. this combines the actions of adding queue commands and emptying queus. 
            //in the case that the track controller receives a signal from the CTC after a queue's data has been added, but before emptying the queue,
            //this prevents emptying non processed commands
            processCommandsLock.lock();         
            try
            {
                //lock the commandSignalQueue
                commandSignalLock.lock();
                try 
                {
                    //add all BlockSignalBundle commands in the queue
                    for (BlockSignalBundle b : this.commandSignalQueue)
                    {
                        c.pushCommand(b);
                    }
                } 
                finally 
                { 
                    //unlock the commandsignalQueue
                    commandSignalLock.unlock(); 
                }
            
                //lock the commandblockQueue
                commandBlockLock.lock();
                try 
                {
                    //add all blockInfoBundle commands in the queue
                    for (BlockInfoBundle b : this.commandBlockQueue)
                    {
                        c.pushCommand(b);
                    }
                }
                finally 
                { 
                    //unlock the Commandblockqueue
                    commandBlockLock.unlock(); 
                }
            
                //lock the commandswitchqueue
                commandSwitchLock.lock();
                try 
                {
                    //add all switch commands in the queue
                    for (Switch s : this.commandSwitchQueue)
                    {
                        c.pushCommand(s);
                    }
                }
                finally 
                { 
                    //unlock the commandswitchqueue
                    commandSwitchLock.unlock(); 
                }
                
                //empty all of the queues
                this.emptyCommandQueues();
            }
            finally
            {
                //unlock the processCommandLock
                processCommandsLock.unlock();
            }   
            this.processCommands(c);            //process theses commands
            
            
            
            //sleep the thread for 100 milliseconds
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(TrackController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //sendSwitchStateSignal(Switch packet) is called when a switch command is send
    //to this controller
    //Parameters:
    //  Switch packet, switch command
    public void sendSwitchStateSignal(Switch packet)
    {
        //if the switch is null do nothing
        if (packet == null)
        {
            System.out.println("TrackController - sendSwitchStateSignal - packet is null");
        }
        else
        {
            //lock the processCommandsLock. if the controller is currently processing commands, this switch command will wait until after the queue is emptied
            this.processCommandsLock.lock();
            try
            {
                //lock the command switch queue
                this.commandSwitchLock.lock();
                try
                {
                    this.commandSwitchQueue.add(packet);    //add the switch command
                }
                finally
                {
                    //unlock the commandSwitchQueue
                    this.commandSwitchLock.unlock();
                }
            }
            finally
            {
                //unlock the processCommandsLock    
                this.processCommandsLock.unlock();
            }
        }
    }
    
    //sendTravelSignal(BlockSignalBundle packet, ArrayList<BlockSignalBundle> route) is called when a
    //route is sent to the controller. the controller will determine a safe authority before sending a 
    //travel signal
    //Paremeters:
    //  BlockSignalBundle packet - the first signal 
    //  ArrayList<blockSignalBundle> route - the route. null is none provided
    public void sendTravelSignal(BlockSignalBundle packet, ArrayList<BlockSignalBundle> route)
    {
        int safeAuthority = packet.Authority;   //packet cannot be null because Wayside checked that for us
        if (route != null)                      //if a route was provided
        {
            Block block;
            //figure out safe authority
            for (BlockSignalBundle b : route)   //iterate up the route
            {
                if (packet.BlockID != b.BlockID)    //do not search the first block
                {
                    if (this.containsBlock(b.BlockID))  //if this controller can see this block
                    {
                        block = this.blockInfo.get(b.BlockID);  
                        if (block.isOccupied())                 //if the block is occupied
                        {
                            safeAuthority = packet.Authority - b.Authority - 1; //calculate safe authrotity
                            break;
                        }
                    }
                }
            }
        }
        this.sendTravelSignal(new BlockSignalBundle(safeAuthority, packet.Destination, packet.Speed, packet.BlockID, this.line));   //call sendTravelSignal() with safe authority
    }
    
    //sendTravelSignal(BlockSignalBundle packet) adds the command to the queue after checking for a safe speed
    //Parameters:
    //  BlockSignalBundle packet - travel signal
    public void sendTravelSignal(BlockSignalBundle packet)
    {
        int blockNum = packet.BlockID;              //this packets block id to send to
        Block block = this.blockInfo.get(blockNum); //the block
        double speed;                               //used for setting the speed to either suggested or speed limit
        
            //check if the suggested speed is safe. if not, set speed to speed limit
            //if it is safe, set speed to suggeset
            if (packet.Speed > block.getSpeedLimit())
            {
                speed = block.getSpeedLimit();
            }
            else
            {
                speed = packet.Speed;
            }
            BlockSignalBundle copiedPacket = packet.copy();
            copiedPacket.Speed = speed;                     //set the safe speed for the copied packet
              
            //lock the processCommandsLock to ensure this packet is processed before the queue is emptied
            processCommandsLock.lock();
            try
            {
                //.lock the commandSignalQueue
                commandSignalLock.lock();
                try
                {
                    this.commandSignalQueue.add(copiedPacket);
                }
                finally
                {
                    //unock the commandSignalQueue
                    commandSignalLock.unlock();
                }
            }
            finally
            {
                //unlock the processCommandsLock
                processCommandsLock.unlock();
            }
        
        
    }
    
    //setBlockClosing(BlockSignalBundle packet) is used to set block closings. it checks for 
    //the speed = -1 flag for opening, or if closed = true
    //Parameters:
    //  BlockSignalBundle packet - the signal to indicate which block to open/close
    public void setBlockClosing(BlockSignalBundle packet)
    {
        //if this packet is a closure/open packet
        if (packet.Closed || packet.Speed == -1)    
        {
            processCommandsLock.lock();
            try
            {
                commandSignalLock.lock();
                try
                {
                    this.commandSignalQueue.add(packet);        //add the packet to the commandSignalQueue. its special case will be handled at processing time
                }
                finally
                {
                    commandSignalLock.unlock();
                }
            }
            finally
            {
                processCommandsLock.unlock();
            }
        }
        
    }
    
    //setPLC() sets this controllers plc based on its id. it also creates a route table for help with routing trains 
    //around switches
    public void setPLC()
    {
        //routeTable k:v = from:to where from is block id that a train will be on and to is block id that a train will move to if the next block is a switch
        HashMap routeTable = new HashMap(); //table that plc will reference when replicating signals and other actions that require knowledge of which direction a train will move at a switch, before the switch is set
        if (this.id == 0)   //PLCGreenOne
        {
            routeTable.put(13, 12);
            routeTable.put(1, 13);
            routeTable.put(150, 28);
            routeTable.put(28, 29);
            //functionallity for load different plc files
            if (this.plcVersion == 0)
            {
                this.plcProgram = new PLCGreenOne(id, line, routeTable);
            }
            else if (this.plcVersion == 1)
            {
                this.plcProgram = new PLCGreenOneV1(id, line, routeTable);
            }
            else if (this.plcVersion == 2)
            {
                this.plcProgram = new PLCGreenOneV2(id, line, routeTable);
            }
            else
            {
                this.plcProgram = new PLCGreenOne(id, line, routeTable);
            }
        }
        else if (id == 1)   //PLCGreenTwo
        {
            routeTable.put(57, 151);
            routeTable.put(152, 62);
            this.plcProgram = new PLCGreenTwo(id, line, routeTable);
        }
        else if (id == 2)   //PLCGreenThree
        {
            routeTable.put(76, 77);
            routeTable.put(85, 86);
            routeTable.put(100, 85);
            routeTable.put(77, 101);
            this.plcProgram = new PLCGreenThree(id, line, routeTable);
        }
        else if (id == 3)   //PLCRedOne
        {
            routeTable.put(77, 9);
            routeTable.put(1, 16);
            routeTable.put(16, 1);
            this.plcProgram = new PLCRedOne(id, line, routeTable);
        }
        else if (id == 4)   //PLCRedTwo
        {
            routeTable.put(27, 28);
            routeTable.put(28, 27);
            routeTable.put(32, 33);
            routeTable.put(33, 32);
            this.plcProgram = new PLCRedTwo(id, line, routeTable);
        }
        else if (id == 5)   //PLCRedThree
        {
            routeTable.put(38, 39);
            routeTable.put(39, 38);
            routeTable.put(43, 44);
            routeTable.put(44, 43);
            this.plcProgram = new PLCRedThree(id, line, routeTable);
        }
        else if (id == 6)   //PLCRedFour
        {
            routeTable.put(52, 53);
            routeTable.put(53, 66);
            this.plcProgram = new PLCRedFour(id, line, routeTable);
        }
        else
        {
            //this is bad and should not happen. 
            this.plcProgram = null;
        }
    }
         
    
    @Override
    public String toString()
    {
        return this.line + "" + this.id;
    }
 
    //blockSnapShot() is used to take a "snapShot" in time of all of the blocks
    //that this controller has access to. This method is called before running the plc program
    //so that the plc program can work on a static set of inputs
    //Returns - ArrayList<Block>, the blocks at the time this method was called
    private ArrayList<Block> blockSnapShot()
    {
        ArrayList<Block> snap;
        snap = new ArrayList<>();       //new arraylist to hold the snapshot
        
        for (Block b : this.blockArray)
        {
            snap.add(b.copy());         //add a copy of the block
        }
        
        return snap;
    }
    
    //processCommands(Commands c) goes through everycommand from the queues and the PLC
    //and sets the track to reflect theses commands
    //Parameters:
    //  Commands c - the commands object that holds all commands
    private void processCommands(Commands c)
    {
        //loop through the three types of commands
        //signals - set blocks with matching speed, authority ...
        int authority;      //the authority to set a block at
        double speed;       //the speed to set a block at
        int dest;           //the dest to set a block at
        int blockID;        //the block to send a command to
        boolean closed;     //if a block should be closed
        Block block;        //the block currently working on
        
        //iterate through all BlockSignalBundle commands
        for (BlockSignalBundle bsb : c.blockSignalCommands)
        {
            authority = bsb.Authority;
            speed = bsb.Speed;
            dest = bsb.Destination;
            blockID = bsb.BlockID;
            closed = bsb.Closed;
            block = this.blockInfo.get(blockID);
            //if this happens, something unexpected happened. this should never get called
            if (block == null)
            {
                System.out.println("TrackController - processCommands - block is null. Attempted to get blockID: " + blockID + " in controller " + this.id);
            }
            
            //check if this signal is a block closure packet
            if (closed || speed == -1)
            {
               block.setClosed(closed); //if it is a closure packet, set block to be closed
            }
            else    //if it is not a closure packet
            {
                block.setAuthority(authority);  //set the blocks authority
                block.setVelocity(speed);           //set the blocks speed
                block.setDestination(dest);         //set the blocks dest
            }

        }
        
       
        LightColor lc;          //light color to change the block to
        XingState xing;         //xing state to change the block to
        //iterate through all BlockInfoBundle commands
        for (BlockInfoBundle bib : c.blockInfoCommands)
        {
            lc = bib.LightColor;                    //get the new light color, if any
            xing = bib.RRXingState;                 //get the new xing state, if any
            blockID = bib.BlockID;
            block = this.blockInfo.get(blockID);    //get the block
            block.setLightColor(lc);                //set the new light color
            block.setRRXingState(xing);             //set the xing state
        }
        
        boolean dir;            //direction to set the switch towards
        int switchID;
        Switch theSwitch;
        //iterate through all switch commands
        for (Switch s : c.switchCommands)
        {
            dir = s.Straight;               //get the direction for the command
            switchID = s.SwitchID;
            theSwitch = this.switchInfo.get(switchID);  //the switch to change
            theSwitch.Straight = dir;                   //set the switch direciton

        }
    }
    
    //switchSnapShot() is used to take a "snapShot" in time of all of the switches
    //that this controller has access to. This method is called before running the plc program
    //so that the plc program can work on a static set of inputs
    //Returns - ArrayList<Switch>, the switches at the time this method was called
    private ArrayList<Switch> switchSnapShot()
    {
        ArrayList<Switch> snap;
        snap = new ArrayList<>();           //new list of switches
        
        for (Switch sw : this.switchArray)
        {
            snap.add(sw.copy());            //add a copy of the switch
        }
        
        
        return snap;
    }
}
