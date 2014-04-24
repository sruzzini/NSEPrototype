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

/**
 *
 * @author nwhachten
 * @version 1.1
 * @since 04-04-2014
 */
public abstract class PLC {
    protected ArrayList<Block> blockArray;
    protected HashMap<Integer, Block> blocks;
    protected ArrayList<Integer> blocksWithCrossing;
    protected final int id;
    protected final LineColor line;
    protected ArrayList<Switch> switchArray;
    protected HashMap<Integer, Switch> switches;
    private final HashMap routeTable;

    public PLC(int id, LineColor line,  HashMap routeTable) {
        this.id = id;
        this.line = line;
        //this.blocks = blocks;
        //this.switches = switches;
        //this.blockArray = blockArray;
        //this.blocksWithCrossing = new ArrayList<>();
        this.routeTable = routeTable;
        //this.switchArray = switchArray;
        
        /*for (Block b : blockArray)
        {
            if (b.hasRRXing())
            {
                blocksWithCrossing.add(b.getBlockID());
            }
        }*/
    }
    
    public Commands runPLCProgram(ArrayList<Block> blocks, ArrayList<Switch> switches)
    {
        this.setupInputs(blocks, switches);
        
        Commands tryOne = runAllPLCTasks();
        Commands tryTwo = runAllPLCTasks();
        Commands tryThree = runAllPLCTasks();
        
        Commands votingResult = commandsFromVote(tryOne, tryTwo, tryThree);

        return votingResult;
        //return tryOne;
    }
    
    private ArrayList<BlockInfoBundle> checkLights()
    {
        ArrayList<BlockInfoBundle> commands;
        commands = new ArrayList<>();
        Block approach, straight, divergent;
        
        for (Switch sw : this.switchArray)
        {
            approach = this.blocks.get(sw.ApproachBlock);
            straight = this.blocks.get(sw.StraightBlock);
            divergent = this.blocks.get(sw.DivergentBlock);
           /* if (approach.getLightColor() != LightColor.GREEN)
            {
                commands.add(new BlockInfoBundle(LightColor.GREEN, approach.getRRXingState(), approach.getBlockID(), this.line, approach.isClosed()));
            }*/
            
            if (sw.Straight)
            {
                if (divergent.getLightColor() != LightColor.RED)
                {
                    commands.add(new BlockInfoBundle(LightColor.RED, divergent.getRRXingState(), divergent.getBlockID(), this.line, divergent.isClosed()));   
                }
                
                if (straight.getVelocity() > 0 && straight.getAuthority() > 0)
                {
                    if (straight.getLightColor() != LightColor.GREEN)
                    {
                        commands.add(new BlockInfoBundle(LightColor.GREEN, straight.getRRXingState(), straight.getBlockID(), this.line, straight.isClosed()));   
                    }
                    
                }
                else
                {
                    if (straight.getLightColor() != LightColor.RED)
                    {
                        commands.add(new BlockInfoBundle(LightColor.RED, straight.getRRXingState(), straight.getBlockID(), this.line, straight.isClosed()));
                    }
                }
                if (this.routeTable.get(approach.getBlockID()) != null && (int)this.routeTable.get(approach.getBlockID()) == straight.getBlockID())
                {
                    if (approach.getLightColor() != LightColor.GREEN)
                    {
                        commands.add(new BlockInfoBundle(LightColor.GREEN, approach.getRRXingState(), approach.getBlockID(), this.line, approach.isClosed()));
                    }
                }
                else
                {
                    if (approach.getLightColor() != LightColor.RED)
                    {
                        commands.add(new BlockInfoBundle(LightColor.RED, approach.getRRXingState(), approach.getBlockID(), this.line, approach.isClosed()));
                    }
                }
            }
            else
            {
                if (straight.getLightColor() != LightColor.RED)
                {
                    commands.add(new BlockInfoBundle(LightColor.RED, straight.getRRXingState(), straight.getBlockID(), this.line, straight.isClosed()));
                }
                if (divergent.getVelocity() > 0 && divergent.getAuthority() > 0)
                {
                    if (divergent.getLightColor() != LightColor.GREEN)
                    {
                        commands.add(new BlockInfoBundle(LightColor.GREEN, divergent.getRRXingState(), divergent.getBlockID(), this.line, divergent.isClosed()));             
                    }
                }
                else
                {
                    if (divergent.getLightColor() != LightColor.RED)
                    {
                        commands.add(new BlockInfoBundle(LightColor.RED, divergent.getRRXingState(), divergent.getBlockID(), this.line, divergent.isClosed()));
                    }
                }
                if (this.routeTable.get(approach.getBlockID()) != null && (int)this.routeTable.get(approach.getBlockID()) == divergent.getBlockID())
                {
                    if (approach.getLightColor() != LightColor.GREEN)
                    {
                        commands.add(new BlockInfoBundle(LightColor.GREEN, approach.getRRXingState(), approach.getBlockID(), this.line, approach.isClosed()));
                    }
                }
                else
                {
                    if (approach.getLightColor() != LightColor.RED)
                    {
                        commands.add(new BlockInfoBundle(LightColor.RED, approach.getRRXingState(), approach.getBlockID(), this.line, approach.isClosed()));
                    }
                }
            }
        }
        
        
        return commands;
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
            prev = this.blocks.get(b.Prev);
            next = this.blocks.get(b.Next);
            
            if (b.isOccupied() || prev.isOccupied() || next.isOccupied())
            {
                //b.setRRXingState(XingState.ACTIVE);
                commands.add(new BlockInfoBundle(b.getLightColor(), XingState.ACTIVE, b.getBlockID(), this.line));
            }
            else 
            {
               // b.setRRXingState(XingState.NOT_ACTIVE);
                
                    commands.add(new BlockInfoBundle(b.getLightColor(), XingState.NOT_ACTIVE, b.getBlockID(), this.line));
                
            }
        }
        
        return commands;
    }
    
    protected ArrayList<BlockSignalBundle> checkTrainsTooClose()
    {
        ArrayList<BlockSignalBundle> commands;
        commands = new ArrayList<>();
        Block next, nextNext, prev, prevPrev;
        
        if (this.line == LineColor.GREEN)
        {
            return commands;
        }
        
        for (Block b : this.blockArray)
        {
            if (b.isOccupied() && !(this.line == LineColor.RED && b.getBlockID() == 77) && !(this.line == LineColor.GREEN && b.getBlockID() == 152))
            {
                next = this.findBlock(b.Next, b.getBlockID());
                if (next != null)
                {
                    nextNext = this.findBlock(next.Next, next.getBlockID());
                }
                else
                {
                    nextNext = null;
                }

                prev = this.findBlock(b.Prev, b.getBlockID());
                if (prev != null)
                {
                    prevPrev = this.findBlock(prev.Prev, prev.getBlockID());
                }
                else
                {
                   prevPrev = null; 
                }
                
                if (nextNext != null && nextNext.isOccupied() &&  b.getBlockID() != nextNext.getBlockID())
                {
                    //halt b, next, and nextNext
                    commands.add(new BlockSignalBundle(0, b.getBlockID(), 0, b.getBlockID(), this.line));
                    commands.add(new BlockSignalBundle(0, next.getBlockID(), 0, next.getBlockID(), this.line));
                    commands.add(new BlockSignalBundle(0, nextNext.getBlockID(), 0, nextNext.getBlockID(), this.line));
                }
                if (prevPrev != null && prevPrev.isOccupied() && b.getBlockID() != prevPrev.getBlockID())
                {
                    //halt b, prev, and prevPrev
                    commands.add(new BlockSignalBundle(0, b.getBlockID(), 0, b.getBlockID(), this.line));
                    commands.add(new BlockSignalBundle(0, prev.getBlockID(), 0, prev.getBlockID(), this.line));
                    commands.add(new BlockSignalBundle(0, prevPrev.getBlockID(), 0, prevPrev.getBlockID(), this.line));
                }
            }
        }
        
        return commands;
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
       
    
    protected abstract Commands plcProgram();
    
    
    protected ArrayList<BlockSignalBundle> replicateSignals()
    {
        ArrayList<BlockSignalBundle> signals;
        signals = new ArrayList<>();
        int prev, next;
        int authority;
        double speed;
        boolean sendSignal;
        Block nextBlock;
        
        for (Block b : this.blockArray)
        {
                if (b.isOccupied() && b.getVelocity() != 0)
                {
                    nextBlock = this.findBlock(b.Prev, b.getBlockID());
                    if (nextBlock != null && !nextBlock.isOccupied())
                    {
                        sendSignal = true;
                        prev = b.Prev;
                        if (prev < 0)
                        {
                            try
                            {
                                prev = (int)this.routeTable.get(b.getBlockID());
                          
                            }
                            catch (NullPointerException e)
                            {   
                                //System.out.println("PLC - replicateSignals - null when referencing the routing table for block: " + b.getBlockID() + " for prev: " + prev);
                                sendSignal = false;
                            }
                        }
                        if (sendSignal)
                        {
                            authority = b.getAuthority() - 1;
                            if (authority < 0)
                            {
                                authority = 0;
                            }
                            if (b.getVelocity() > nextBlock.getSpeedLimit())
                            {
                                speed = nextBlock.getSpeedLimit();
                            }
                            else
                            {
                                speed = b.getVelocity();
                            }
                            signals.add(new BlockSignalBundle(authority, b.getDestination(), speed, prev, this.line, b.isClosed()));
                        }
// System.out.println("PLC - replicateSignals - added signal auth: " + (b.getAuthority() - 1) + " dest: " + b.getDestination() + 
                          //              " speed: " + b.getVelocity() + " to block " + prev);
                        
                    }
                    nextBlock = this.findBlock(b.Next, b.getBlockID());
                    if (nextBlock != null && !nextBlock.isOccupied())
                    {
                        sendSignal = true;
                        next = b.Next;
                        if (next < 0)
                        {
                            try
                            {   
                                next = (int)this.routeTable.get(b.getBlockID()); 
                                
                            }
                            catch (NullPointerException e1)
                            {
                                //System.out.println("PLC - replicateSignals - null when referencing the routing table for block: " + b.getBlockID() + " for next: " + next);
                                sendSignal = false;
                            }
                        }
                        if (sendSignal)
                        {
                            authority = b.getAuthority() - 1;
                            if (authority < 0)
                            {
                                authority = 0;
                            }
                            if (b.getVelocity() > nextBlock.getSpeedLimit())
                            {
                                speed = nextBlock.getSpeedLimit();
                            }
                            else
                            {
                                speed = b.getVelocity();
                            }
                            signals.add(new BlockSignalBundle(authority, b.getDestination(), speed, next, this.line, b.isClosed()));
                        }
                            //signals.add(new BlockSignalBundle(b.getAuthority() - 1, b.getDestination(), b.getVelocity(), next, this.line, b.isClosed()));
                       // System.out.println("PLC - replicateSignals - added signal auth: " + (b.getAuthority() - 1) + " dest: " + b.getDestination() + 
                         //               " speed: " + b.getVelocity() + " to block " + next); 
                        
                    }
                }

            }
        
        return signals;
    }
    
    private Block findBlock(int next, int current)
    {
        Block b = null;
        Switch s;
        int approach, divergent, straight;
        
        if (next < 0)
        {
            //follow switch
            try 
            {
                s = this.switches.get(-next);
                if (s == null)
                {
                    //System.out.println("PLC - findBlock - switch is null. switch lookup: " + next + " in plc: " + this.id);
                }
                approach = s.ApproachBlock;
                divergent = s.DivergentBlock;
                straight = s.StraightBlock;
                if (current == straight || current == divergent)
                {
                    b = this.blocks.get(approach);
                }
                else
                {
                    if (s.Straight)
                    {
                        b = this.blocks.get(straight);
                    }
                    else
                    {
                        b = this.blocks.get(divergent);
                    }
                }
            }
            catch (NullPointerException e)
            {}
        }
        
          else
          {
            b = this.blocks.get(next);
          }
            //if (b == null)
            //System.out.println("PLC - findBlock - returning null instead of block. current: " + current + " next: " + next);
        return b;
    }
    
    private void setupInputs(ArrayList<Block> blocks, ArrayList<Switch> switches)
    {
        this.blockArray = blocks;
        this.switchArray = switches;
        this.blocks = new HashMap();
        this.switches = new HashMap();
        this.blocksWithCrossing = new ArrayList<>();
        
        for (Block b : blocks)
        {
            this.blocks.put(b.getBlockID(), b);
            if (b.hasRRXing())
            {
                this.blocksWithCrossing.add(b.getBlockID());
            }
        }
        for (Switch s : switches)
        {
            this.switches.put(s.SwitchID, s);
        }
    }
    
    private Commands runAllPLCTasks()
    {
        Commands c;
        c = plcProgram();
        
        
        
         ArrayList<BlockInfoBundle> rrCommands = this.checkRRCrossings();
         ArrayList<BlockSignalBundle> replicateCommands = this.replicateSignals();
         ArrayList<BlockSignalBundle> safetyCommands = this.checkTrainsTooClose();
         ArrayList<BlockInfoBundle> lightCommands = this.checkLights();
        
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
            else
            {
                //System.out.println("PLC - runAllPLCTasks - leaving out a replication siginal due to conflict for block: " + b.BlockID);
            }
            
        }
        
        for (BlockInfoBundle b : lightCommands)
        {
            c.pushCommand(b);
        }
        
        for (BlockSignalBundle b : safetyCommands)
        {
            c.pushCommand(b);
        }
        
        return c;
        
    }
    
    
}
