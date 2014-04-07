package DataLayer.TrackModel;

import DataLayer.EnumTypes.*;
import java.util.*;

public class Line 
{
    private LineColor lineID;
    private String lineString;
    public ArrayList<Block> theBlocks;
    public ArrayList<Switch> theSwitches;
    
    public Line(LineColor lineID, String lineString)
    {
        this.lineID = lineID;
        this.lineString = lineString;
        theBlocks = new ArrayList<>();
        theSwitches = new ArrayList<>();
    }
    
    public void addBlock(Block b)
    {
        theBlocks.add(b);
    }

    public Block getBlock(int blockID) {
        return theBlocks.get(blockID - 1);
    }
    
    public void addSwitch(Switch s)
    {
        theSwitches.add(s);
    }
    
    public Switch getSwitch(int switchID) {
        return theSwitches.get(switchID - 1);
    }

    public LineColor getLineID() {
        return lineID;
    }

    public String getLineString() {
        return lineString;
    }
    
    
}
