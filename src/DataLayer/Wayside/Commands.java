/******************************************************************************
 * 
 * Commands class
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
import DataLayer.TrackModel.*;
import java.util.*;

/**
 *<h1>Commands</h1>
 * <p>
 * This class contains functionality to add commands and read commands</p>
 * @author nwhachten
 */
public class Commands {
    public ArrayList<BlockInfoBundle> blockInfoCommands;
    public ArrayList<BlockSignalBundle> blockSignalCommands;
    public ArrayList<Switch> switchCommands;
    
    public Commands()
    {
        //initialize the arraylists
        blockInfoCommands = new ArrayList<>();
        blockSignalCommands = new ArrayList<>();
        switchCommands = new ArrayList<>();
        
    }
    
    //containsCommandForBlockID(ind id) checks to see if this Commands object contains
    //a signal command to be sent to a block as specified by the input id
    //Parameters:
    //  int id - the block number to send the signal to
    //Returns - boolean, true if a signal is found, false if not
    public boolean containsCommandForBlockID(int id)
    {
        boolean result = false;             //start by assuming false
        for (BlockSignalBundle b : this.blockSignalCommands)    //iterate through all blockSignalCommands
        {
            if (b.BlockID == id)        //if a signal is already present
            {
                result = true;          //set return value to true
                break;                  //stop searching
            }
        }
        
        return result;                  //return result
    }
    
    //matches(Commands a) goes through all commands in this and a to determine if tey match
    //this method is used by the PLC voting algorithm to determine if a set of commands differs
    //Paramereters:
    //  Commands a - a Commands object to be compared to
    //Returns boolean - true if the objects match, else false
    public boolean matches(Commands a)
    {
        boolean result = true;          //start by assuming they match
        boolean commandFound;
        
        for (BlockInfoBundle b : this.blockInfoCommands)        //iterate through all blockInfoBundle commands in this
        {
            commandFound = false;                               //for each command start by assuming another command is not found
           for (BlockInfoBundle c : a.blockInfoCommands)        //iterate through all blockInfoBundle commands in a
           {
               if (b.matches(c))                                //if the two bundles match
               {
                   commandFound = true;                         //set commandfound true
                   break;                                       //and stop searching through a
               }
           }
           if (commandFound = false)                            //if commandFound is still false at this point, there was no signal in a that matched this
           {
               result = false;                                  //therefore the objects do not match
               break;                                           //stop searching
           }
        }
        
        if (result == true)                                     //if result is still true after searching the blockInfoCommands
        {
           for (BlockSignalBundle b : this.blockSignalCommands) //search through the blocksignalcommands
            {
            commandFound = false;
           for (BlockSignalBundle c : a.blockSignalCommands)
           {
               if (b.matches(c))
               {
                   commandFound = true;
                   break;
               }
           }
           if (commandFound = false)
           {
               result = false;
               break;
           }
        } 
        }
        
        if (result == true)                                 //if the result is stil true after search signals and info bundles, search through all switch commands
        {
            for (Switch b : this.switchCommands)
            {
                commandFound = false;
            for (Switch c : a.switchCommands)
            {
               if (b.matches(c))
               {
                   commandFound = true;
                   break;
               }
            }
            if (commandFound = false)
            {
               result = false;
               break;
            }
        }
       }
        
        
        return result;
    }
    
    //display this command object as a string
    @Override
    public String toString()
    {
        String s = new String();
        
        for (BlockSignalBundle b : this.blockSignalCommands)
        {
            //s += "Signal Command - block: " + b.BlockID + " authority: " + b.Authority + " speed: " + b.Authority + " destination: " + b.Destination + "\n";
            s += b.toString();
        }
        for (BlockInfoBundle b : this.blockInfoCommands)
        {
            //s += "Info Command - block: " + b.BlockID + " light color: " + b.LightColor + " crossing state: " + b.RRXingState + "\n";
            s += b.toString();
        }
        for (Switch sw : this.switchCommands)
        {
            s += "Switch Command - switch: " + (sw.SwitchID -1) + " straight block: " + sw.StraightBlock + " divergent block: " + sw.DivergentBlock + " is straight: " + sw.Straight + "\n";
            //s += sw.toString();
        }
        
        
        return s;
    }
    
    //pushCommand(BlockInfoBundle b) pushes a command of type BlockInfoBundle
    //Parameters:
    //  BlockInfoBundle b - command to add
    public void pushCommand(BlockInfoBundle b)
    {
        blockInfoCommands.add(b);
    }
    
    //pushCommand(BlockSignalBundle b) pushes a command of type BlockSignalBundle
    //Parameters:
    //  BlockInfoBundle b - command to add
    public void pushCommand(BlockSignalBundle b)
    {
        blockSignalCommands.add(b);
    }
    
    //pushCommand(Switch s) pushes a command of type Switch
    //Parameters:
    //  BlockInfoBundle b - command to add
    public void pushCommand(Switch s)
    {
        switchCommands.add(s);
    }
    
    
    
    
}
