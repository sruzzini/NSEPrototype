package DataLayer.TrackModel;

import DataLayer.EnumTypes.LineColor;

public class Switch 
{
    public int approachBlock; // number of block that is the approach block of the switch
    public int divergentBlock; // number of block that is the divergent block of the switch
    public LineColor lineID; // line identifier for the line that the switch belongs to
    public boolean straight; // boolean - is the switch in the straight or divergent position
    public int straightBlock; // number of block that is the straight block of the switch
    public int switchID; // switch identifier
    
    // Switch(LineColor lineID, int switchID, int approachBlock, int straightBlock, int divergentBlock, boolean straight)
    // instantiates a new switch object with the specified parameters
    // Parameters:
    //     LineColor lineID - line that the new switch belongs to
    //     int switchID - identifier for the new switch
    //     int approachBlock - identifier for the approach block
    //     int straightBlock - identifier for the straight block
    //     int divergentBlock - identifier for the divergent block
    //     boolean straight - initial position of the switch upon instantiation
    public Switch(LineColor lineID, int switchID, int approachBlock, 
            int straightBlock, int divergentBlock, boolean straight)
    {
        this.lineID = lineID;
        this.switchID = switchID;
        this.approachBlock = approachBlock;
        this.straightBlock = straightBlock;
        this.divergentBlock = divergentBlock;
        this.straight = straight;
    }
    
    // isStraight() returns whether or not the switch is in the straight position
    // Returns - boolean, whether or not the switch is in the straight position
    public boolean isStraight() 
    {
        return straight;
    }
    
    // matches(Switch s) returns whether or not this switch object has all the same 
    // instance variable values of the switch passed in
    // Parameters:
    //     Switch s - the switch to be compared to this switch
    // Returns - boolean, whether or not the switches match
    public boolean matches(Switch s)
    {
        boolean result = true;
        
        if (this.lineID != s.lineID || this.switchID != s.switchID || this.approachBlock != s.approachBlock ||
                this.straightBlock != s.straightBlock || this.divergentBlock != s.divergentBlock || this.straight != s.straight)
        {
            result = false;
        }
        
        return result;
    }
}
