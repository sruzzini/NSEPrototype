/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.Bundles.BlockInfoBundle;
import DataLayer.Bundles.BlockSignalBundle;
import DataLayer.EnumTypes.LineColor;
import DataLayer.EnumTypes.XingState;
import DataLayer.TrackModel.Block;
import DataLayer.TrackModel.Switch;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author nwhachten
 */
public abstract class PLC {
    protected final int id;
    protected final LineColor line;
    protected final Hashtable<Integer, Block> blocks;
    protected final Hashtable<Integer, Switch> switches;
    protected final ArrayList<Integer> blocksWithCrossing;

    public PLC(int id, LineColor line,  Hashtable<Integer, Block> blocks, ArrayList<Block> blockArray, Hashtable<Integer, Switch> switches) {
        this.id = id;
        this.line = line;
        this.blocks = blocks;
        this.switches = switches;
        this.blocksWithCrossing = new ArrayList<>();
        
        for (Block b : blockArray)
        {
            if (b.hasRRXing())
            {
                blocksWithCrossing.add(b.getBlockID());
            }
        }
    }
    
    public Commands runPLCProgram()
    {
        Commands tryOne = plcProgram();
        Commands tryTwo = plcProgram();
        Commands tryThree = plcProgram();
        
        Commands votingResult = commandsFromVote(tryOne, tryTwo, tryThree);
        
        
        
        return votingResult;
    }
    
    protected Commands commandsFromVote(Commands a, Commands b, Commands c)
    {
        Commands result;
        
        if (a.matches(b) || a.matches(c))
        {
            result =  a;
        }
        else if (b.matches(c))
        {
            result =  b;
        }
        else
        {
            result = null;
        }
        
        return result;
    }
    
    protected ArrayList<BlockInfoBundle> checkRRCrossings()
    {
        ArrayList<BlockInfoBundle> commands;
        commands = new ArrayList<>();
        
        Block prev;
        Block next;
        Block b;
        for (int n : this.blocksWithCrossing)
        {
            b = this.blocks.get(n);
            prev = this.blocks.get(b.prev);
            next = this.blocks.get(b.next);
            
            if (b.isOccupied() || prev.isOccupied() || next.isOccupied())
            {
                b.setRRXingState(XingState.ACTIVE);
            }
            else 
            {
                b.setRRXingState(XingState.NOT_ACTIVE);
            }
        }
        
        return commands;
    }
    
    protected ArrayList<BlockSignalBundle> checkTrainsTooClose()
    {
        ArrayList<BlockSignalBundle> commands;
        commands = new ArrayList<>();
        
        return commands;
    }
    
    protected abstract Commands plcProgram();
    

    
    
    
    
}
