/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.Bundles.BlockInfoBundle;
import DataLayer.Bundles.BlockSignalBundle;
import DataLayer.EnumTypes.LightColor;
//import DataLayer.Bundles.Switch;
import DataLayer.TrackModel.Switch;
import DataLayer.EnumTypes.LineColor;
import DataLayer.EnumTypes.XingState;
import DataLayer.TrackModel.Block;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nwhachten
 */
public class TrackController implements Runnable {
    private final int id;
    private final LineColor line;
    private final int[] blocksInSector;
   // private Hashtable<Integer, BlockInfoBundle> trackBlockInfo;
    //private Hashtable<Integer, BlockSignalBundle> trackSignalInfo;
    private ArrayList<Block> blockArray;
    private ArrayList<Switch> switchArray;
    private Hashtable<Integer, Block> blockInfo;
    private Hashtable<Integer, Switch> switchInfo;
    private final ArrayList<BlockSignalBundle> commandSignalQueue;
    private final ArrayList<Switch> commandSwitchQueue;
    private final ArrayList<BlockInfoBundle> commandBlockQueue;
    private PLC plcProgram;

    public TrackController(int id, LineColor line, int[] blocksInSector)  {
        this.id = id;
        this.line = line;
        this.blocksInSector = blocksInSector;
        this.commandSignalQueue = new ArrayList<>();
        this.commandSwitchQueue = new ArrayList<>();
        this.commandBlockQueue = new ArrayList<>();
        this.blockInfo = new Hashtable();
        this.switchInfo = new Hashtable();
        this.blockArray = new ArrayList<>();
        this.switchArray = new ArrayList<>();
        //this.trackBlockInfo = new Hashtable();
        //this.trackSignalInfo = new Hashtable();
        
        
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
        this.switchInfo.put(s.switchID, s);
    }
    public void setPLC()
    {
        if (this.id == 0)
        {
             this.plcProgram = new PLCGreenOne(id, line, this.blockInfo, this.blockArray, this.switchInfo);
        }
        else if (id == 1)
        {
            this.plcProgram = new PLCGreenTwo(id, line, this.blockInfo, this.blockArray, this.switchInfo);
        }/*
        else if (id == 2)
        {
            this.plcProgram = new PLCGreenThree(id, line, blockInfo);
        }
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
            this.plcProgram = new PLCGreenOne(id, line, this.blockInfo, this.blockArray, this.switchInfo);
            //this is bad and should not happen. create exception to be thrown
        }
    }
    
    @Override
    public void run()
    {
        Commands c;// = new Commands();
        while (true)
        {
            c = this.plcProgram.runPLCProgram();
            for (BlockSignalBundle b : this.commandSignalQueue)
            {
                c.pushCommand(b);
            }
            for (BlockInfoBundle b : this.commandBlockQueue)
            {
                c.pushCommand(b);
            }
            for (Switch s : this.commandSwitchQueue)
            {
                c.pushCommand(s);
            }
            for (BlockSignalBundle b : this.replicateSignals())
            {
                c.pushCommand(b);
            }
                
            this.processCommands(c);
            
            this.emptyCommandQueues();
            
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(TrackController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void sendTravelSignal(BlockSignalBundle packet)
    {
        //do work
        int blockID = packet.BlockID;
        
        double speedLimit;
        speedLimit = this.blockInfo.get(blockID).getSpeedLimit();
        
        if (packet.Speed > speedLimit)
        {
            //packet.setSpeed(speedLimit);
            packet.Speed = speedLimit;
        }
        
        this.commandSignalQueue.add(packet);
        
    }
    
    private ArrayList<BlockSignalBundle> replicateSignals()
    {
        ArrayList<BlockSignalBundle> commands;
        commands = new ArrayList<>();
        int authority;
        double speed;
        int dest;
        int prev;
        int next;
        int switchID;
        boolean dir;
        int straightBlock;
        int divergentBlock;
        int approachBlock;
        int nextThruSwitch;
        Switch s;
        
        
        for (Block b : this.blockArray)
        {
            if (b.isOccupied())
            {
                authority = b.getAuthority();
                speed = b.getVelocity();
                dest = b.getDestination();
                prev = b.prev;
                next = b.next;
                if (next < 0)
                    switchID = -next;
                else if (prev < 0)
                    switchID = -prev;
                else
                    switchID = 0;
                
                if (switchID != 0) {
                    s = this.switchInfo.get(switchID);
                    if (b.getBlockID() == s.approachBlock)
                    {
                        if (s.straight)
                            nextThruSwitch = s.straightBlock;
                        else
                            nextThruSwitch = s.divergentBlock;              
                    }
                    else
                        nextThruSwitch = s.approachBlock;
                    
                    if (next < 0)
                        next = nextThruSwitch;
                    else
                        prev = nextThruSwitch;
                    
                    
                }
               // System.out.println("Replicate signal from block id: " + b.getBlockID() + " to blocks with ids " + prev +" " + next);
                if (next > 0) commands.add(new BlockSignalBundle(authority, dest, speed, next, LineColor.GREEN));
                if (prev > 0) commands.add(new BlockSignalBundle(authority, dest, speed, prev, LineColor.GREEN));   
            }
        }
        
        return commands;
        
    }
    
    public void sendSwitchStateSignal(Switch packet)
    {
        this.commandSwitchQueue.add(packet);
    }

    public int getId() {
        return id;
    }

    public LineColor getLine() {
        return line;
    }

    public int[] getBlockNums() {
        return blocksInSector;
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
    
    public void setTrackBlockInfo(ArrayList<Block> info)
    {
        this.blockArray = info;
        for (Block b : info)
        {
            this.blockInfo.put(b.getBlockID(), b);
        }
    }
    
    public ArrayList<Block> getBlockInfo()
    {
        return this.blockArray;
    }
    
    public void setSwitchInfo(ArrayList<Switch> info)
    {
        this.switchArray = info;
        for (Switch s : info)
        {
            this.switchInfo.put(s.switchID, s);
            //System.out.println("Setting switch with switch ID: " + s.switchID);
        }
    }
    
    public ArrayList<Switch> getSwitchInfo()
    {
        return this.switchArray;
    }

    public ArrayList<BlockSignalBundle> getCommandSignalQueue() {
        return commandSignalQueue;
    }

    public ArrayList<Switch> getCommandSwitchQueue() {
        return commandSwitchQueue;
    }

    public ArrayList<BlockInfoBundle> getCommandBlockQueue() {
        return commandBlockQueue;
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
    
    private void emptyCommandQueues()
    {
        this.commandBlockQueue.clear();
        this.commandSignalQueue.clear();
        this.commandSwitchQueue.clear();
    }
    
    private void processCommands(Commands c)
    {
        //loop through the three types of commands
        //signals - set blocks with matching speed, authority ...
        int authority;
        double speed;
        int dest;
        int blockID;
        Block block;
        for (BlockSignalBundle bsb : c.blockSignalCommands)
        {
            authority = bsb.Authority;
            speed = bsb.Speed;
            dest = bsb.Destination;
            blockID = bsb.BlockID;
            block = this.blockInfo.get(blockID);
           // System.out.println("blockID: " + blockID + " controllerID " + this.id);
            block.setAuthority(authority);
            block.setVelocity(speed);
            block.setDestination(dest);
            
            
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
            dir = s.straight;
            switchID = s.switchID;
            theSwitch = this.switchInfo.get(switchID);
            theSwitch.straight = dir;
            
        }
    }
    
    
    
}
