/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.Bundles.BlockInfoBundle;
import DataLayer.Bundles.BlockSignalBundle;
import DataLayer.TrackModel.Switch;
import DataLayer.EnumTypes.LineColor;
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
    private Hashtable<Integer, BlockInfoBundle> trackBlockInfo;
    private Hashtable<Integer, BlockSignalBundle> trackSignalInfo;
    private Hashtable<Integer, Switch> switchInfo;
    private final ArrayList<BlockSignalBundle> commandSignalQueue;
    private final ArrayList<Switch> commandSwitchQueue;
    private final ArrayList<BlockInfoBundle> commandBlockQueue;
    private final PLC plcProgram;

    public TrackController(int id, LineColor line, int[] blocksInSector) {
        this.id = id;
        this.line = line;
        this.blocksInSector = blocksInSector;
        plcProgram = new PLCGreenOne(id, line, blocksInSector);
        this.commandSignalQueue = new ArrayList<>();
        this.commandSwitchQueue = new ArrayList<>();
        this.commandBlockQueue = new ArrayList<>();
        this.trackBlockInfo = new Hashtable();
        this.trackSignalInfo = new Hashtable();
    }
    
    public void sendTravelSignal(BlockSignalBundle packet)
    {
        //do work
        int blockID = packet.getBlockID();
        
        double speedLimit;
        speedLimit = this.trackBlockInfo.get(blockID).getSpeedLimit();
        
        if (packet.getSpeed() > speedLimit)
        {
            packet.setSpeed(speedLimit);
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
    
    public void setTrackBlockInfo(List<BlockInfoBundle> info)
    {
        //this.trackBlockInfo = info;
        for (BlockInfoBundle b : info)
        {
            this.trackBlockInfo.put(b.getBlockID(), b);
        }
    }
    
    public void setTrackSignalInfo(List<BlockSignalBundle> info)
    {
        for (BlockSignalBundle b : info)
        {
            this.trackSignalInfo.put(b.getBlockID(), b);
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
