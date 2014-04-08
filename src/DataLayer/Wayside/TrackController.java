/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.Bundles.BlockInfoBundle;
import DataLayer.Bundles.BlockSignalBundle;
//import DataLayer.Bundles.Switch;
import DataLayer.TrackModel.Switch;
import DataLayer.EnumTypes.LineColor;
import DataLayer.TrackModel.Block;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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
        }/*
        else if (id == 1)
        {
            this.plcProgram = new PLCGreenTwo(id, line, blockInfo);
        }
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
            
            this.emptyCommandQueues();
            
            this.processCommands(c);
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
        //info - set blocks with rrxing and light info
        //switch - change switches as needed
    }
    
    
    
}
