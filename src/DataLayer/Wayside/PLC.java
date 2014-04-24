/******************************************************************************
 * 
 * PLC class
 * 
 * Developed by AJility
 * April 2014
 * CoE 1186
 * 
 * Contributers:
 *  Nathaniel W. Hachten
 *
 *****************************************************************************/

package DataLayer.Wayside;

import DataLayer.Bundles.*;
import DataLayer.EnumTypes.*;
import DataLayer.TrackModel.*;
import java.util.*;

/**
 * <h1>PLC</h1>
 * <p>
 * This class contains the basic functionality for a PLC program. It is abstract
 * and has method plcProgram() that is also abstract. This method will be implemented
 * by the concrete subclasses that will be run by each TrackController</p>
 *
 * @author nwhachten
 * @version 2.0
 * @since 04-04-2014
 */
public abstract class PLC {
    protected ArrayList<Block> blockArray;              //array of blocks to make decisions about based on occupancy
    protected HashMap<Integer, Block> blocks;           //HashMap of blocks for quick access
    protected ArrayList<Integer> blocksWithCrossing;    //list of blocks that have a crossing on them
    protected final int id;                             //unique id to match TrackController
    protected final LineColor line;                     //line for this plc's controller
    protected ArrayList<Switch> switchArray;            //array of switches to create commands for
    protected HashMap<Integer, Switch> switches;        //HashMap of switches for quick access
    private final HashMap routeTable;                   //route table to reference when replicating signals

    //PLC(int id, LineColor line, HashMap routeTable) is the constructor for the PLC
    //class
    //Parameters:
    //  int id - the unique id
    //  LineColor line - the line that this will operate on
    //  HashMap routeTable - the routeTable to reference for several methods
    public PLC(int id, LineColor line,  HashMap routeTable) {
        this.id = id;
        this.line = line;
        this.routeTable = routeTable;
    }
    
    //runPLCProgram(ArrayList<Block> blocks, ArrayList<Switch> switches) will take a snapshot
    //of the track as a parameter, run the program three times, perform a voting algorithm,
    //and return the winner fo the vote
    //Paremeters:
    //  ArrayList<Block> blocks - a snapshot of the blocks of the track
    //  ArrayList<Switch> switches - a snapshot of the switches of the track
    //Reurns: Commands, the set of commands to be processed by the TrackController
    public Commands runPLCProgram(ArrayList<Block> blocks, ArrayList<Switch> switches)
    {
        this.setupInputs(blocks, switches);     //sets up Block and Switch data structures based on track snapshot
        
        //runs the program three times for safety critical architecture
        Commands tryOne = runAllPLCTasks();
        Commands tryTwo = runAllPLCTasks();
        Commands tryThree = runAllPLCTasks();
        
        //determines the voting results based on the three runs of the program
        Commands votingResult = commandsFromVote(tryOne, tryTwo, tryThree);

        return votingResult;    //return the voting result
        
    }
    
    //checkLights() will create track light commands based on the status of the switches
    //and the blocks surrounding those switches
    //Returns: ArrayList<BlockInfoBundle>, an array of light commands
    private ArrayList<BlockInfoBundle> checkLights()
    {
        ArrayList<BlockInfoBundle> commands;
        commands = new ArrayList<>();           //new arrylist to hold commands
        Block approach, straight, divergent;    //refernces to each block of ther switch
        
        for (Switch sw : this.switchArray)      //iterate through all switches in this controller
        {
            //get all blocks for this switch
            approach = this.blocks.get(sw.ApproachBlock);
            straight = this.blocks.get(sw.StraightBlock);
            divergent = this.blocks.get(sw.DivergentBlock);
            
            if (sw.Straight)        // if the switch is currently set straight
            {
                if (divergent.getLightColor() != LightColor.RED)        //set the divergent blocks light to red
                {
                    commands.add(new BlockInfoBundle(LightColor.RED, divergent.getRRXingState(), divergent.getBlockID(), this.line, divergent.isClosed()));   
                }
                
                if (straight.getVelocity() > 0 && straight.getAuthority() > 0)  //if the straight block has the signal to go, then set it to green
                {
                    if (straight.getLightColor() != LightColor.GREEN)
                    {
                        commands.add(new BlockInfoBundle(LightColor.GREEN, straight.getRRXingState(), straight.getBlockID(), this.line, straight.isClosed()));   
                    }
                    
                }
                else    //straight block has signal set to stop it
                {
                    if (straight.getLightColor() != LightColor.RED)     //set the straight block light to red
                    {
                        commands.add(new BlockInfoBundle(LightColor.RED, straight.getRRXingState(), straight.getBlockID(), this.line, straight.isClosed()));
                    }
                }
                if (this.routeTable.get(approach.getBlockID()) != null && (int)this.routeTable.get(approach.getBlockID()) == straight.getBlockID()) //if the approach block's route table to entry is the straight block
                {
                    if (approach.getLightColor() != LightColor.GREEN)   //set the approach block green
                    {
                        commands.add(new BlockInfoBundle(LightColor.GREEN, approach.getRRXingState(), approach.getBlockID(), this.line, approach.isClosed()));
                    }
                }   
                else        //if the route table indicates a train should not move from approach block to straight block
                {   
                    if (approach.getLightColor() != LightColor.RED)     //set the light red
                    {
                        commands.add(new BlockInfoBundle(LightColor.RED, approach.getRRXingState(), approach.getBlockID(), this.line, approach.isClosed()));
                    }
                }
            }
            else    //switch is diverent
            {
                if (straight.getLightColor() != LightColor.RED) //set the straight block red
                {
                    commands.add(new BlockInfoBundle(LightColor.RED, straight.getRRXingState(), straight.getBlockID(), this.line, straight.isClosed()));
                }
                if (divergent.getVelocity() > 0 && divergent.getAuthority() > 0)    //if the divergent block has a signal to go
                {
                    if (divergent.getLightColor() != LightColor.GREEN)      //set the diverent block green
                    {
                        commands.add(new BlockInfoBundle(LightColor.GREEN, divergent.getRRXingState(), divergent.getBlockID(), this.line, divergent.isClosed()));             
                    }
                }
                else    //divergent block has stop signal
                {
                    if (divergent.getLightColor() != LightColor.RED)    //set divertnt block light red
                    {
                        commands.add(new BlockInfoBundle(LightColor.RED, divergent.getRRXingState(), divergent.getBlockID(), this.line, divergent.isClosed()));
                    }
                }
                if (this.routeTable.get(approach.getBlockID()) != null && (int)this.routeTable.get(approach.getBlockID()) == divergent.getBlockID())    //if approach block route table to entry is diverent block
                {
                    if (approach.getLightColor() != LightColor.GREEN)   //set diverent light green
                    {
                        commands.add(new BlockInfoBundle(LightColor.GREEN, approach.getRRXingState(), approach.getBlockID(), this.line, approach.isClosed()));
                    }
                }
                else        //route talbe indicates train should not proceed from approach to diverent
                {
                    if (approach.getLightColor() != LightColor.RED) //set the approach light red
                    {
                        commands.add(new BlockInfoBundle(LightColor.RED, approach.getRRXingState(), approach.getBlockID(), this.line, approach.isClosed()));
                    }
                }
            }
        }
        
        
        return commands;    //return light commands
    }

    //checkRRCrossings() determines how to set a RR crossing based on occupancy info
    //Returns: ArrayList<BlockInfoBundle>, the crossing commands
    protected ArrayList<BlockInfoBundle> checkRRCrossings()
    {
        ArrayList<BlockInfoBundle> commands;
        commands = new ArrayList<>();           //crossing commands
        
        Block prev;     //adjacent block
        Block next;     //adjacent block
        Block b;        //block with crossing
        
        //iterate through blocks with crossings
        for (int n : this.blocksWithCrossing)
        {
            //get the three blocks on and around corssing
            b = this.blocks.get(n);
            prev = this.blocks.get(b.Prev);
            next = this.blocks.get(b.Next);
            
            if (b.isOccupied() || prev.isOccupied() || next.isOccupied())       //if any of them are occupied
            {
                commands.add(new BlockInfoBundle(b.getLightColor(), XingState.ACTIVE, b.getBlockID(), this.line));  //set crossing active
            }
            else    //none of them are occupied
            {
                commands.add(new BlockInfoBundle(b.getLightColor(), XingState.NOT_ACTIVE, b.getBlockID(), this.line));  //set crossing inactive    
            }
        }
        
        return commands;
    }
    
    //checkTrainsTooClose() will determine if there are two occupancies that are too close.
    //if there are, it will send commands to stop all trains in the area
    //Returns: ArrayList<BlockSignalBundle>, stop commands
    protected ArrayList<BlockSignalBundle> checkTrainsTooClose()
    {
        ArrayList<BlockSignalBundle> commands;
        commands = new ArrayList<>();
        Block next, nextNext, prev, prevPrev;   //need access to 5 blocks in a row
        
        if (this.line == LineColor.GREEN)       //this method is not currently running on the green line
        {
            return commands;
        }
        
        for (Block b : this.blockArray)         //iterate through all lbocks
        {
            if (b.isOccupied() && !(this.line == LineColor.RED && b.getBlockID() == 77) && !(this.line == LineColor.GREEN && b.getBlockID() == 152))    //if there is an occupancy on the dispatch block, do nothing
            {
                next = this.findBlock(b.Next, b.getBlockID());  //get the next block
                if (next != null)
                {
                    nextNext = this.findBlock(next.Next, next.getBlockID());    //get next's next block
                }
                else
                {
                    nextNext = null;
                }

                prev = this.findBlock(b.Prev, b.getBlockID());      //get the previous block
                if (prev != null)
                {
                    prevPrev = this.findBlock(prev.Prev, prev.getBlockID());    //get prev's previous block
                }
                else
                {
                   prevPrev = null; 
                }
                
                if (nextNext != null && nextNext.isOccupied() &&  b.getBlockID() != nextNext.getBlockID())  //if the next next block was found and is occupied, and if next next did not return to b
                {
                    //halt b, next, and nextNext
                    commands.add(new BlockSignalBundle(0, b.getBlockID(), 0, b.getBlockID(), this.line));
                    commands.add(new BlockSignalBundle(0, next.getBlockID(), 0, next.getBlockID(), this.line));
                    commands.add(new BlockSignalBundle(0, nextNext.getBlockID(), 0, nextNext.getBlockID(), this.line));
                }
                if (prevPrev != null && prevPrev.isOccupied() && b.getBlockID() != prevPrev.getBlockID())   //if the prev prev block was found and is occupied, and if prev prev did not return to b
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
    
    //commandsFromVote(commands a, Commands b, Commands c) is the safety critical architecture
    //portion of the plc. it determines based on best two of three which command to return
    //Parameters:
    //  Commands a - run one of program
    //  Commands b - run two of program
    //  Commands c - run three of program
    protected Commands commandsFromVote(Commands a, Commands b, Commands c)
    {
        Commands result;
        
        if (a.matches(b) || a.matches(c))   //2 of the three match
        {   
            result =  a;                    //set result to a because is mathches one of the others
        }
        else if (b.matches(c))              //2 of the three match
        {
            result =  b;                    //set result to b because it matches c
        }
        else
        {
            result = null;                  //if none match, set to null
        }
        
        return result;
    }
       
    //plcProgram() is the abstract method that will be run the subclass
    //Returns - Commands, the resultant commands of the program
    protected abstract Commands plcProgram();
    
    //replicateSignals() will copy a signal from an occupied block to its adjacent blocks with a decremented authority
    //Returns ArrayList<blockSignalBundle>, the signals to replicate
    protected ArrayList<BlockSignalBundle> replicateSignals()
    {
        ArrayList<BlockSignalBundle> signals;
        signals = new ArrayList<>();            //new commands to process
        int prev, next;                         //ids from block.prev and block.nexty
        int authority;                          //authority to set for new commands
        double speed;                           //speed of block, dont replicate if speed is 0
        boolean sendSignal;                     //flag for sending a signatl
        Block nextBlock;
        
        for (Block b : this.blockArray)         //iterate through all blocks
        {
                if (b.isOccupied() && b.getVelocity() != 0) //if the block is occupied and the velocity is not set to be 0
                {
                    nextBlock = this.findBlock(b.Prev, b.getBlockID()); //set the next block for b.prev
                    if (nextBlock != null && !nextBlock.isOccupied())   //if the controller has access to that block and that block is occupied
                    {
                        sendSignal = true;  //set sendSignal true because all conditions have been met to send the signal
                        prev = b.Prev;
                        if (prev < 0)   //if prev is a switch
                        {
                            try
                            {
                                prev = (int)this.routeTable.get(b.getBlockID());    //get the next block number from the route table
                          
                            }
                            catch (NullPointerException e)
                            {   
                                //System.out.println("PLC - replicateSignals - null when referencing the routing table for block: " + b.getBlockID() + " for prev: " + prev);
                                sendSignal = false; //if the route table indicates that the next block should be traveled to, do not send a signal
                            }
                        }
                        if (sendSignal) //if sending a signal is true
                        {
                            authority = b.getAuthority() - 1;   //set authrotity to one less thatn block b
                            if (authority < 0)
                            {
                                authority = 0;      //do not set negative authrotity
                            }
                            if (b.getVelocity() > nextBlock.getSpeedLimit())    //set safe speed
                            {
                                speed = nextBlock.getSpeedLimit();
                            }
                            else
                            {
                                speed = b.getVelocity();
                            }
                            signals.add(new BlockSignalBundle(authority, b.getDestination(), speed, prev, this.line, b.isClosed()));    //add new signal command
                        }
                    }
                    //follow the same steps as above just with the other adjeacent block
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
                    }
                }

            }
        
        return signals; //return the replication signals
    }
    
    //findBlock(int next, int current) will find the next block that a train on block with id = to current should travel to
    //this method resolves case where next is negative indicating a switch
    //Parameters:
    //  int next - the id of the next field for block with id current, negative for a switch
    //  int current - the id of the block looking for next block of
    private Block findBlock(int next, int current)
    {
        Block b = null; //set block = to null to check at end if it still is null
        Switch s;
        int approach, divergent, straight;  
        
        if (next < 0)                   //next < 0 indicates that the current block is connected to a switch
        {
            //follow switch
            try 
            {
                s = this.switches.get(-next);   //get the switch object connect to block current
                if (s == null)
                {
                    //System.out.println("PLC - findBlock - switch is null. switch lookup: " + next + " in plc: " + this.id);
                }
                
                //get all int block references for blocks connected to the switch
                approach = s.ApproachBlock; 
                divergent = s.DivergentBlock;
                straight = s.StraightBlock;
                
                if (current == straight || current == divergent)    //if the current block is not the apporach block
                {
                    b = this.blocks.get(approach);  //set the block eqaul to the approach block. this is the next block
                }
                else    //current is the approach block
                {
                    if (s.Straight)     //if the switch is set straight
                    {
                        b = this.blocks.get(straight);  //set next block to the straight block
                    }
                    else        //the switch is diverent
                    {
                        b = this.blocks.get(divergent); //set next block to the divergent block
                    }
                }
            }
            catch (NullPointerException e)
            {
                //if switch or block can not be accessed, a null pointer will be generated. catch it. method will eventually return null
            }
        }
        
        else //next is not a switch
          {
            b = this.blocks.get(next);  //just get the next block
          }
            //if (b == null)
            //System.out.println("PLC - findBlock - returning null instead of block. current: " + current + " next: " + next);
        return b;
    }
    
    //setupInputs(ArrayList<Block> blocks, ArayList<Switch> switches) will set the block and switch
    //data structures for the PLC
    //Parameters:
    //  ArrayList<Block> blocks - block list
    //  ArrayList<Switch> switches - switch list
    private void setupInputs(ArrayList<Block> blocks, ArrayList<Switch> switches)
    {
        //set all data structures and initialize
        this.blockArray = blocks;
        this.switchArray = switches;
        this.blocks = new HashMap();
        this.switches = new HashMap();
        this.blocksWithCrossing = new ArrayList<>();
        
        //iterate through all blocks
        for (Block b : blocks)
        {
            this.blocks.put(b.getBlockID(), b); //add the block to the hashmap
            if (b.hasRRXing())  //if the block has a crossing
            {
                this.blocksWithCrossing.add(b.getBlockID());    //add the blocks id to the blocksWithCrossing list
            }
        }
        for (Switch s : switches)   //iterate through the switches
        {
            this.switches.put(s.SwitchID, s);   //add the switch to the hashmap
        }
    }
    
    //runAllPLCTasks() this is the bread and butter of the PLC class. it runs the subclasses plcprogram
    //and performs all other plc tasks and returns the commands
    //Returns - Commands, the commands that result
    private Commands runAllPLCTasks()
    {
        Commands c;
        c = plcProgram();       //get the commands from the subclass plc
        
        
        
         ArrayList<BlockInfoBundle> rrCommands = this.checkRRCrossings();               //get rr crossing commands
         ArrayList<BlockSignalBundle> replicateCommands = this.replicateSignals();      //get the replication signal commands
         ArrayList<BlockSignalBundle> safetyCommands = this.checkTrainsTooClose();      //get commands to prevent trains from crashing
         ArrayList<BlockInfoBundle> lightCommands = this.checkLights();                 //get light commands
        
         // add all railroad commands to c
        for (BlockInfoBundle b : rrCommands)
        {
            c.pushCommand(b);
        }
        
        //add all replication commands to c
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
        
        //add all light commands to c
        for (BlockInfoBundle b : lightCommands)
        {
            c.pushCommand(b);
        }
        
        //add all safety commands to c
        for (BlockSignalBundle b : safetyCommands)
        {
            c.pushCommand(b);
        }
        
        return c;
        
    }
    
    
}
