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
public class TrackController {
    private final int id;
    private final LineColor line;
    private final int[] blocksInSector;
   // private Hashtable<Integer, BlockInfoBundle> trackBlockInfo;
    //private Hashtable<Integer, BlockSignalBundle> trackSignalInfo;
    private ArrayList<Block> blockArray;
    private Hashtable<Integer, Block> blockInfo;
    private Hashtable<Integer, Switch> switchInfo;
    private final ArrayList<BlockSignalBundle> commandSignalQueue;
    private final ArrayList<Switch> commandSwitchQueue;
    private final ArrayList<BlockInfoBundle> commandBlockQueue;
    private final PLC plcProgram;

    public TrackController(int id, LineColor line, int[] blocksInSector)  {
        this.id = id;
        this.line = line;
        this.blocksInSector = blocksInSector;
        this.plcProgram = new PLCGreenOne(id, line, blocksInSector);
        this.commandSignalQueue = new ArrayList<>();
        this.commandSwitchQueue = new ArrayList<>();
        this.commandBlockQueue = new ArrayList<>();
        this.blockInfo = new Hashtable();
        this.switchInfo = new Hashtable();
        //this.trackBlockInfo = new Hashtable();
        //this.trackSignalInfo = new Hashtable();
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
    
    public void setSwitchInfo(List<Switch> info)
    {
        for (Switch s : info)
        {
            this.switchInfo.put(s.switchID, s);
        }
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
    
    
    
    
    
}
