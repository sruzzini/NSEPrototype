/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
 * @version 1.1
 * @since 2014-04-02
 */
public class TrackController implements Runnable {
    public PLC plcProgram;
    private ArrayList<Block> blockArray;
    private HashMap<Integer, Block> blockInfo; //change to hashmap
    private final int[] blocksInSector;
    private final Lock commandBlockLock;
    private final ArrayList<BlockInfoBundle> commandBlockQueue;
    private final Lock commandSignalLock;
    private final ArrayList<BlockSignalBundle> commandSignalQueue;
    private final ArrayList<BlockSignalBundle> commandSignalWaitQueue;
    private final Lock commandSwitchLock;
    private final ArrayList<Switch> commandSwitchQueue;
    private final int id;
    private final LineColor line;
    private final Lock processCommandsLock;
    private ArrayList<Switch> switchArray;
    private HashMap<Integer, Switch> switchInfo;  //change to hashmap
    private final Lock waitSignalLock;
    
    
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
        this.commandSignalWaitQueue = new ArrayList<>();
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
        this.waitSignalLock = new ReentrantLock();
        
    }
    
    public void addBlock(Block b)
    {
        //System.out.println("We would like to add block with blockID: " + b.getBlockID());
        this.blockArray.add(b);
        this.blockInfo.put(b.getBlockID(), b);
    }
    
    public void addSwitch(Switch s)
    {
        this.switchArray.add(s);
        this.switchInfo.put(s.SwitchID, s);
        //System.out.println("Added switch: " + s.switchID + " to TC: " + this.line + "" + this.id);
    }
    
    public boolean containsBlock(int n)
    {
        boolean result = false;
        for (int b : this.blocksInSector)
        {
            if (b == n)
            {
                result = true;
                break;
            }
        }
        return result;
    }
    
    
    
    public ArrayList<BlockSignalBundle> detectForgottenTrain()
    {
        ArrayList<BlockSignalBundle> trains = new ArrayList<>();
        
        for (Block b : this.blockArray)
        {
            if (b.isOccupied() && !b.isClosed() && b.getAuthority() == 0 && b.getDestination() != b.getBlockID())
            {
                trains.add(new BlockSignalBundle(0, b.getDestination(), b.getVelocity(), b.getBlockID(), this.line));
            }
                
        }

        return trains;
        
    }
    
    public void emptyCommandQueues()
    {
        //processCommandsLock.lock();
        //try
        //{
            commandBlockLock.lock();
            try 
            {
                this.commandBlockQueue.clear();
            }
            finally 
            { 
                commandBlockLock.unlock(); 
            }

            commandSignalLock.lock();
            try 
            {
                this.commandSignalQueue.clear();
            }
            finally 
            { 
                commandSignalLock.unlock(); 
            }

            commandSwitchLock.lock();
            try 
            {
                this.commandSwitchQueue.clear();
            }
            finally 
            {   
                commandSwitchLock.unlock(); 
            }
        //}
        //finally
        //{
          //  processCommandsLock.unlock();
        //}
    }
    
    public ArrayList<Block> getBlockInfo()
    {
        return this.blockArray;
    }       
    
    public int[] getBlockNums() {
        return blocksInSector;
    }
    
    public ArrayList<BlockInfoBundle> getCommandBlockQueue() {
        return commandBlockQueue;
    }
    
    public ArrayList<BlockSignalBundle> getCommandSignalQueue() {
        return commandSignalQueue;
    }
    
    public ArrayList<Switch> getCommandSwitchQueue() {
        return commandSwitchQueue;
    }
    
    public HashMap<Integer, Block> getBlockTable()
    {
        return this.blockInfo;
    }
    
    public int getId() {
        return id;
    }
    
    public LineColor getLine() {
        return line;
    }
    
    public ArrayList<Block> getOccupiedBlocks()
    {
        ArrayList<Block> occ;
        occ = new ArrayList<>();
        
        for (Block b : this.blockArray)
        {
            if (b.isOccupied())
            {
                occ.add(b);
            }
        }
        return occ;
    }
    
    public ArrayList<Switch> getSwitchInfo()
    {
        ArrayList<Switch> copiedArray = new ArrayList<>();
        for (Switch s : this.switchArray)
        {
            copiedArray.add(new Switch(s.LineID, s.SwitchID, s.ApproachBlock, s.StraightBlock, s.DivergentBlock, s.Straight));
        }
        return copiedArray;
    }

    
    @Override
    public void run()
    {
        Commands c;// = new Commands();
        while (true)
        {
            //c = this.plcProgram.runPLCProgram(this.blockSnapShot(), this.switchSnapShot());
            c = this.plcProgram.runPLCProgram();
            
            processCommandsLock.lock();
            try
            {
                commandSignalLock.lock();
                try 
                {
                    for (BlockSignalBundle b : this.commandSignalQueue)
                    {
                        c.pushCommand(b);
                    }
                } 
                finally 
                { 
                    commandSignalLock.unlock(); 
                }
            
                commandBlockLock.lock();
                try 
                {
                    for (BlockInfoBundle b : this.commandBlockQueue)
                    {
                        c.pushCommand(b);
                    }
                }
                finally 
                { 
                    commandBlockLock.unlock(); 
                }
            
                commandSwitchLock.lock();
                try 
                {
                    for (Switch s : this.commandSwitchQueue)
                    {
                        c.pushCommand(s);
                    }
                }
                finally 
                { 
                    commandSwitchLock.unlock(); 
                }
            
            
              
            
                this.emptyCommandQueues();
            }
            finally
            {
                processCommandsLock.unlock();
            }
            this.processCommands(c);
            
            
            
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(TrackController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void sendSwitchStateSignal(Switch packet)
    {
        if (packet == null)
        {
            System.out.println("TrackController - sendSwitchStateSignal - packet is null");
        }
        else
        {
            this.processCommandsLock.lock();
            try
            {
                this.commandSwitchLock.lock();
                try
                {
                    //System.out.println("TrackController - sendSwitchStateSignal - received packet for switch: " + packet.switchID);
                    this.commandSwitchQueue.add(packet);
                }
                finally
                {
                    this.commandSwitchLock.unlock();
                }
            }
            finally
            {
                this.processCommandsLock.unlock();
            }
        }
    }
    
  /* public BlockSignalBundle sendTravelSignal(BlockSignalBundle sentPacket)
    {
        BlockSignalBundle packet = sentPacket.copy();
        BlockSignalBundle returnSignal = null;
        int blockID = packet.BlockID;
        double speed;
        int next;
        Block currentBlock = this.blockInfo.get(packet.BlockID);
        
        //System.out.println("BlockSignal: " + packet.BlockID + " " + packet.Authority + " sent to tc: " + this.id + " and current block is: " + currentBlock.getBlockID());
        //System.out.println("Current block next" + currentBlock.next + " prev " + currentBlock.prev);
        
        for (int i = 0; i <= packet.Authority; i++)
        {
          // System.out.println("packet: " + packet.BlockID);
          // System.out.println("current block: " + currentBlock.getBlockID());
          // System.out.println("speed limit: " + currentBlock.getSpeedLimit());
          // System.out.println("sent speed: " + packet.Speed);
           if (packet.Speed > currentBlock.getSpeedLimit())
            {
            //packet.setSpeed(speedLimit);
                speed = currentBlock.getSpeedLimit();
            } 
           else
           {
               speed = packet.Speed;
           }
           
         //System.out.println("TrackController - sendTravelSignal - Adding signal to queue on tc: " + this.id);
         //System.out.println("TrackController - sendTravelSignal - Signal\n\tAuthority: " + (packet.Authority -i) + "\n\tDestination: " + packet.Destination + "\n\tSpeed: " + speed + "\n\tID: " + currentBlock.getBlockID() );
            
           commandSignalLock.lock();
           try 
           {
                //System.out.println("adding signal to queue");
                this.commandSignalQueue.add(new BlockSignalBundle(packet.Authority - i, packet.Destination, speed, currentBlock.getBlockID(), LineColor.GREEN));
                
           }
           finally 
           { 
                commandSignalLock.unlock(); 
           }
           if (packet.Authority - i == 0)
           {
                break;
           }
            
            if (currentBlock.prev < 0)
            {
                Switch nextSwitch = this.switchInfo.get(-currentBlock.prev);
                if (currentBlock.getBlockID() == nextSwitch.divergentBlock ||
                        currentBlock.getBlockID() == nextSwitch.straightBlock)
                {
                    next = nextSwitch.approachBlock;
                }
                else if (nextSwitch.straight)
                {
                    next = nextSwitch.straightBlock;
                }
                else
                {
                    next = nextSwitch.divergentBlock;
                }
                
            }
            else
            {
                next = currentBlock.prev;
            }
            
           // System.out.println("About to set current block at id: " + next);
            
            if (this.containsBlock(next))
            {
                currentBlock = this.blockInfo.get(next); 
            }
            else
            {
                returnSignal = new BlockSignalBundle(packet.Authority - i - 1, packet.Destination, packet.Speed, next, LineColor.GREEN);
                break;
            }
            
        }
        
        return returnSignal;

    }*/
    
    /*public void sendTravelSignal(ArrayList<BlockSignalBundle> route)
    {
        int k = 0;
        for (BlockSignalBundle b : route)
        {
            this.sendTravelSignal2(b, k++);
        }
    }*/
    
    public void sendTravelSignal(BlockSignalBundle packet)
    {
        int blockNum = packet.BlockID;
        Block block = this.blockInfo.get(blockNum);
        double speed;
        
        
                if (packet.Speed > block.getSpeedLimit())
                {
                    speed = block.getSpeedLimit();
                }
                else
                {
                    speed = packet.Speed;
                }
                BlockSignalBundle copiedPacket = packet.copy();
                copiedPacket.Speed = speed;
                
            processCommandsLock.lock();
            try
            {
                commandSignalLock.lock();
                try
                {
                    this.commandSignalQueue.add(copiedPacket);
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
    
    public void setBlockClosing(BlockSignalBundle packet)
    {
        if (packet.Closed || packet.Speed == -1)
        {
            processCommandsLock.lock();
            try
            {
                commandSignalLock.lock();
                try
                {
                    this.commandSignalQueue.add(packet);
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
    
   /* public void sendTravelSignal2(BlockSignalBundle packet, int count)
    {
        int blockNum = packet.BlockID;
        Block block = this.blockInfo.get(blockNum);
        double speed;
        if (block.isOccupied() && count > 0)
        //if (false)
        {
            waitSignalLock.lock();
            try 
            {
                this.commandSignalWaitQueue.add(packet.copy());
            }
            finally
            {
                waitSignalLock.unlock();
            }
        }
        else
        {
            commandSignalLock.lock();
            try
            {
                if (packet.Speed > block.getSpeedLimit())
                {
                    speed = block.getSpeedLimit();
                }
                else
                {
                    speed = packet.Speed;
                }
                BlockSignalBundle copiedPacket = packet.copy();
                copiedPacket.Speed = speed;
                this.commandSignalQueue.add(copiedPacket);
            }
            finally
            {
                commandSignalLock.unlock();
            }
        }
    }*/
            
    
    
    public void setPLC()
    {
        HashMap routeTable = new HashMap();
        if (this.id == 0)
        {
            routeTable.put(13, 12);
            routeTable.put(1, 13);
            routeTable.put(150, 28);
            routeTable.put(28, 29);
             this.plcProgram = new PLCGreenOne(id, line, this.blockInfo, this.blockArray, this.switchInfo, routeTable, this.switchArray);
        }
        else if (id == 1)
        {
            routeTable.put(57, 151);
            routeTable.put(152, 62);
            this.plcProgram = new PLCGreenTwo(id, line, this.blockInfo, this.blockArray, this.switchInfo, routeTable, this.switchArray);
        }
        else if (id == 2)
        {
            routeTable.put(76, 77);
            routeTable.put(85, 86);
            routeTable.put(100, 85);
            routeTable.put(77, 101);
            this.plcProgram = new PLCGreenThree(id, line, this.blockInfo, this.blockArray, this.switchInfo, routeTable, this.switchArray);
        }/*
        else if (id == 3)
        {
            this.plcProgram = new PLCRedOne(id, line, blockInfo);
        }
        else if (id == 4)
        {
            this.plcProgram = new PLCRedTwo(id, line, blockInfo);
        }
        else if (id == 5)
        {
            this.plcProgram = new PLCRedThree(id, line, blockInfo);
        }*/
        else
        {
            this.plcProgram = new PLCGreenOne(id, line, this.blockInfo, this.blockArray, this.switchInfo, routeTable, this.switchArray);
            //this is bad and should not happen. create exception to be thrown
        }
    }
    
    public void setTrackBlockInfo(ArrayList<Block> info)
    {
        this.blockArray = info;
        for (Block b : info)
        {
            this.blockInfo.put(b.getBlockID(), b);
        }
    }
    
    public void setSwitchInfo(ArrayList<Switch> info)
    {
        this.switchArray = info;
        for (Switch s : info)
        {
            this.switchInfo.put(s.SwitchID, s);
            //System.out.println("Setting switch with switch ID: " + s.switchID);
        }
    }
    
   /* public ArrayList<BlockSignalBundle> signalsNotWaiting()
    {
        ArrayList<BlockSignalBundle> commands = new ArrayList<>();
        
        waitSignalLock.lock();
        try
        {
            int blockNum;
            Block block;
            for (BlockSignalBundle b : this.commandSignalWaitQueue)
            {
                blockNum = b.BlockID;
                block = this.blockInfo.get(blockNum);
                if (!block.isOccupied())
                {
                    commands.add(b);
                    this.commandSignalWaitQueue.remove(b);
                }
            }
        }
        finally
        {
            waitSignalLock.unlock();
        }
        
        
        return commands;
    }*/
    
    @Override
    public String toString()
    {
        return "Track Controller " + this.line + "" + this.id;
    }
 
    private ArrayList<Block> blockSnapShot()
    {
        ArrayList<Block> snap;
        snap = new ArrayList<>();
        
        return snap;
    }
    
    private void processCommands(Commands c)
    {
        //loop through the three types of commands
        //signals - set blocks with matching speed, authority ...
        int authority;
        double speed;
        int dest;
        int blockID;
        boolean closed;
        Block block;
        //processCommandsLock.lock();
        //try
        //{
            for (BlockSignalBundle bsb : c.blockSignalCommands)
            {
                authority = bsb.Authority;
                speed = bsb.Speed;
                dest = bsb.Destination;
                blockID = bsb.BlockID;
                closed = bsb.Closed;
                block = this.blockInfo.get(blockID);
                if (block == null)
                {
                    System.out.println("TrackController - processCommands - block is null. Attempted to get blockID: " + blockID + " in controller " + this.id);
                }
                //System.out.println("blockID: " + blockID + " controllerID " + this.id);
                //System.out.println("In process commands. set block: " + block.getBlockID() + " with the values a,s,d: " + authority + "," + speed + "," + dest);
                if (closed || speed == -1)
                {
                   block.setClosed(closed);
                }
                else
                {
                    block.setAuthority(authority);
                    block.setVelocity(speed);
                    block.setDestination(dest);
                }



            }
            //info - set blocks with rrxing and light info
            LightColor lc;
            XingState xing;
            for (BlockInfoBundle bib : c.blockInfoCommands)
            {
                lc = bib.LightColor;
                xing = bib.RRXingState;
                blockID = bib.BlockID;
                block = this.blockInfo.get(blockID);
                block.setLightColor(lc);
                block.setRRXingState(xing);
            }
            //switch - change switches as needed
            boolean dir;
            //Switch switch;
            int switchID;
            Switch theSwitch;
            for (Switch s : c.switchCommands)
            {
                dir = s.Straight;
                switchID = s.SwitchID;
                theSwitch = this.switchInfo.get(switchID);
                theSwitch.Straight = dir;
                //System.out.println("Track Controller - processCommands - set switch: " + switchID + " " + dir + " towards straight:divergent: " + theSwitch.straightBlock + ":" + theSwitch.divergentBlock);

            }
    }
    
    private ArrayList<Switch> switchSnapShot()
    {
        ArrayList<Switch> snap;
        snap = new ArrayList<>();
        
        
        return snap;
    }
    
    //finally
    //{
      //  processCommandsLock.unlock();
    //}
    
    
    
}
