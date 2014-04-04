/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.Bundles.BlockInfoBundle;
import DataLayer.Bundles.BlockSignalBundle;
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
    private final int[] blockNums;
    private Hashtable<Integer, BlockInfoBundle> trackBlockInfo;
    private ArrayList<BlockSignalBundle> commandSignalQueue;

    public TrackController(int id, LineColor line, int[] blockNums) {
        this.id = id;
        this.line = line;
        this.blockNums = blockNums;
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
        
        commandSignalQueue.add(packet);
        
    }

    public int getId() {
        return id;
    }

    public LineColor getLine() {
        return line;
    }

    public int[] getBlockNums() {
        return blockNums;
    }
    
    public boolean containsBlock(int n)
    {
        boolean result = false;
        for (int b : this.blockNums)
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
    
    
    
}
