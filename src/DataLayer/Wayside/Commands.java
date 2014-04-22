/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.Bundles.*;
import DataLayer.TrackModel.*;
import java.util.*;

/**
 *
 * @author nwhachten
 */
public class Commands {
    public ArrayList<BlockInfoBundle> blockInfoCommands;
    public ArrayList<BlockSignalBundle> blockSignalCommands;
    public ArrayList<Switch> switchCommands;
    
    public Commands()
    {
        blockInfoCommands = new ArrayList<>();
        blockSignalCommands = new ArrayList<>();
        switchCommands = new ArrayList<>();
        
    }
    
    public boolean containsCommandForBlockID(int id)
    {
        boolean result = false;
        for (BlockSignalBundle b : this.blockSignalCommands)
        {
            if (b.BlockID == id)
            {
                result = true;
                break;
            }
        }
        
        return result;
    }
    
    public boolean matches(Commands a)
    {
        boolean result = true;
        boolean commandFound;
        
        for (BlockInfoBundle b : this.blockInfoCommands)
        {
            commandFound = false;
           for (BlockInfoBundle c : a.blockInfoCommands)
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
        
        if (result == true)
        {
           for (BlockSignalBundle b : this.blockSignalCommands)
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
        
        if (result == true)
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
    
    public void pushCommand(BlockInfoBundle b)
    {
        blockInfoCommands.add(b);
    }
    
    public void pushCommand(BlockSignalBundle b)
    {
        blockSignalCommands.add(b);
    }
    
    public void pushCommand(Switch s)
    {
        switchCommands.add(s);
    }
    
    
    
    
}
