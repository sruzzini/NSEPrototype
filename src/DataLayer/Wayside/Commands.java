/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.Bundles.BlockInfoBundle;
import DataLayer.Bundles.BlockSignalBundle;
import DataLayer.TrackModel.Switch;
import java.util.ArrayList;

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
    
    public void pushBlockInfoCommand(BlockInfoBundle b)
    {
        blockInfoCommands.add(b);
    }
    
    public void pushBlockSignalCommand(BlockSignalBundle b)
    {
        blockSignalCommands.add(b);
    }
    
    public void pushSwitchCommand(Switch s)
    {
        switchCommands.add(s);
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
    
}
