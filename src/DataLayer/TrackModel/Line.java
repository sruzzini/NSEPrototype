package DataLayer.TrackModel;

import DataLayer.EnumTypes.*;
import java.util.*;

public class Line 
{
    public ArrayList<Block> theBlocks; //Array of blocks in the line
    public ArrayList<Switch> theSwitches; //Array of switches in the line
    private LineColor lineID; //Line Identification
    
    // Line(LineColor lineID) creates a new line with the specified Line Identifier
    // Parameters:
    //     LineColor lineID - the specified Line Identifier
    public Line(LineColor lineID)
    {
        this.lineID = lineID;
        theBlocks = new ArrayList<>();
        theSwitches = new ArrayList<>();
    }
    
    // addBlock(Block b) adds a new block to the theBlocks arraylist
    // Parameters:
    //     Block b - the block object to be added to the line
    public void addBlock(Block b)
    {
        theBlocks.add(b);
    }
    
    // addSwitch(Switch s) adds a new switch to the theSwitches arraylist
    // Parameters:
    //     Switch s - the switch object to be added to the line
    public void addSwitch(Switch s)
    {
        theSwitches.add(s);
    }
    
    // getBlock(int blockID) returns the block object specified in the parameters
    // Parameters:
    //     int blockID - the specified block to be returned
    // Returns - Block, specified block
    public Block getBlock(int blockID) 
    {
        return theBlocks.get(blockID - 1);
    }
    
    // getLineID() returns the identifier of the line
    // Returns - LineColor, line identifier
    public LineColor getLineID() 
    {
        return lineID;
    }
    
    // getLineString() returns the String representation of the lineID
    // Returns - String, string representation of the lineID
    public String getLineString()
    {
        String temp;
        if (lineID == LineColor.GREEN)
        {
            temp =  "Green";
        }
        else
        {
            temp = "Red";
        }
        
        return temp;
    }
    
    // getSwitch(int switchID) returns the switch object specified in the parameters
    // Parameters:
    //     int switchID - the specified switch to be returned
    // Returns - Switch, specified switch
    public Switch getSwitch(int switchID) 
    {
        return theSwitches.get(switchID - 1);
    }
}
