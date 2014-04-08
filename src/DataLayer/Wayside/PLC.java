/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.Bundles.BlockInfoBundle;
import DataLayer.Bundles.BlockSignalBundle;
import DataLayer.EnumTypes.LineColor;
import DataLayer.TrackModel.Block;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author nwhachten
 */
public abstract class PLC {
    private final int id;
    private final LineColor line;
    private final Hashtable<Integer, Block> blocks;
    private final ArrayList<Integer> blocksWithCrossing;

    public PLC(int id, LineColor line,  Hashtable<Integer, Block> blocks, ArrayList<Block> blockArray) {
        this.id = id;
        this.line = line;
        this.blocks = blocks;
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
