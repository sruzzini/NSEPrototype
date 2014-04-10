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
    ArrayList<Block> blockArray;

    public PLC(int id, LineColor line,  Hashtable<Integer, Block> blocks, ArrayList<Block> blockArray, Hashtable<Integer, Switch> switches) {
        this.id = id;
        this.line = line;
        this.blocks = blocks;
        this.switches = switches;
        this.blockArray = blockArray;
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
        Commands tryOne = runAllPLCTasks();
        //Commands tryTwo = runAllPLCTasks();
        //Commands tryThree = runAllPLCTasks();
        
        //Commands votingResult = commandsFromVote(tryOne, tryTwo, tryThree);
        
        
        
        //return votingResult;
        return tryOne;
    }
    
    private Commands runAllPLCTasks()
    {
        Commands c;
        c = plcProgram();
        
        
        /*
         ArrayList<BlockInfoBundle> rrCommands = this.checkRRCrossings();
         ArrayList<BlockSignalBundle> replicateCommands = this.replicateSignals();
        //ArrayList<BlockSignalBundle> safetyCommands = this.checkTrainsTooClose();
        
        for (BlockInfoBundle b : rrCommands)
        {
            c.pushCommand(b);
        }
        
        for (BlockSignalBundle b : replicateCommands)
        {
            if (!c.containsCommandForBlockID(b.BlockID))
            {
                c.pushCommand(b);  
            }
            
        }
        
        for (BlockSignalBundle b : safetyCommands)
        {
            c.pushCommand(b);
        }*/
        
        return c;
        
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
    /*
    protected ArrayList<BlockSignalBundle> replicateSignals()
    {
        ArrayList<BlockSignalBundle> commands;
        commands = new ArrayList<>();
        int next;
        Block nextBlock;
        
        for (Block b : this.blockArray)
        {
            if (b.isOccupied())
            {
                next = b.next;
                //nextBlock = this.blocks.get(next);
                commands.add(new BlockSignalBundle(b, next, LineColor.GREEN));
            }
        }
        
        
        return commands;
    }
    */
    protected abstract Commands plcProgram();
    

    
    
    
    
}
