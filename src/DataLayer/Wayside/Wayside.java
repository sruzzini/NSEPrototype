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
import java.util.List;

/**
 *
 * @author nwhachten
 * @version 1.0
 * @since 2014-04-02
 */
public final class Wayside {
    private final TrackController[] controllers;
    private final int controllerCount = 6;
    //private final int[][] blockNums;
    //private final LineColor[] lines;
    
    
    public Wayside()
    {
        int[][] blockNums = new int[][]{{0,1},{2,3},{4,5},{5,6},{6,7},{8,9}};
        LineColor[] lines = new LineColor[] {LineColor.GREEN, LineColor.GREEN, LineColor.GREEN,
            LineColor.RED, LineColor.RED, LineColor.RED};
        this.controllers = new TrackController[controllerCount];
        
        for (int i = 0; i < controllerCount; i++)
        {
            controllers[i] = new TrackController(i, lines[i], blockNums[i]);
        }
        
       // this.setBlockInfoArray(blockInfoArray);
        //this.setBlockSignalArray(blockSignalArray);
        
        
    }
    
    public void sendTravelSignal(BlockSignalBundle packet)
    {
        LineColor line = packet.LineID;
        int blockNum = packet.BlockID;
        
        for (int i=0; i < controllerCount; i++)
        {
            if (controllers[i].getLine() == line)
            {
                for (int b : controllers[i].getBlockNums() )
                {
                    if (b == blockNum)
                    {
                        this.controllers[i].sendTravelSignal(packet);
                    }
                }
            }
        }
    }
    
    public void sendSwitchStateSignal(Switch packet)
    {
        for (TrackController tc : this.controllers)
        {
            if (tc.getLine() == packet.lineID && (tc.containsBlock(packet.straightBlock) || 
                    tc.containsBlock(packet.approachBlock) || tc.containsBlock(packet.divergentBlock) ))
            {
                tc.sendSwitchStateSignal(packet);
                break;
            }
        }
    }
    
    public void setBlockInfoArray(List<Block> blockArray, LineColor line)
    {
        List<List<Block>> list;
        list = new ArrayList<>(controllerCount);
        
        for (Block b : blockArray)
        {
            for (TrackController tc : this.controllers)
            {
                if (tc.getLine() == line && tc.containsBlock(b.getBlockID()))
                {
                    list.get(tc.getId()).add(b);
                }
                      
            }
        }
        
        for (TrackController tc : this.controllers)
        {
            if (tc.getLine() == line)
            {
                tc.setTrackBlockInfo(list.get(tc.getId()));
            }
        }
        
    }
    
    public void setSwitchArray(List<Switch> switchArray)
    {
        List<List<Switch>> list;
        list = new ArrayList<>(controllerCount);
        LineColor line = switchArray.get(0).lineID;
        
        for (Switch s : switchArray)
        {
            for (TrackController tc : this.controllers)
            {
                if (tc.getLine() == s.lineID && ( tc.containsBlock(s.approachBlock) || tc.containsBlock(s.divergentBlock) || tc.containsBlock(s.straightBlock) ))
                {
                    list.get(tc.getId()).add(s);
                }
                      
            }
        }
        
        for (TrackController tc : this.controllers)
        {
            if (tc.getLine() == line)
            {
                tc.setSwitchInfo(list.get(tc.getId()));
            }
        }
    }
    
    public ArrayList<BlockSignalBundle> getBlockSignalCommands()
    {
        ArrayList<BlockSignalBundle> commands;
        commands = new ArrayList<>();
        
        for (TrackController tc : this.controllers)
        {
            for (BlockSignalBundle c : tc.getCommandSignalQueue())
            {
                commands.add(c);
            }
        }
        
        return commands;
    }
    
    public ArrayList<BlockInfoBundle> getBlockInfoCommands()
    {
        ArrayList<BlockInfoBundle> commands;
        commands = new ArrayList<>();
        
        for (TrackController tc : this.controllers)
        {
            for (BlockInfoBundle c : tc.getCommandBlockQueue())
            {
                commands.add(c);
            }
        }
        
        return commands;
    }
    
    public ArrayList<Switch> getSwitchCommands()
    {
        ArrayList<Switch> commands;
        commands = new ArrayList<>();
        
        for (TrackController tc : this.controllers)
        {
            for (Switch c : tc.getCommandSwitchQueue())
            {
                commands.add(c);
            }
        }
        
        return commands;
    }
    
   
    
    
    
}
