package DataLayer.TrackModel;

import DataLayer.EnumTypes.LineColor;

public class Switch 
{
    public int ApproachBlock; // number of block that is the approach block of the switch
    public int DivergentBlock; // number of block that is the divergent block of the switch
    public LineColor LineID; // line identifier for the line that the switch belongs to
    public boolean Straight; // boolean - is the switch in the Straight or divergent position
    public int StraightBlock; // number of block that is the Straight block of the switch
    public int SwitchID; // switch identifier
    
    // copy() returns a new instance of switch with attributes that match the 
    // attributes of the calling switch
    // Returns - a new switch instance
    public Switch copy()
    {
        Switch s = new Switch(this.LineID, this.SwitchID, this.ApproachBlock, 
                this.StraightBlock, this.DivergentBlock, this.Straight);
        
        return s;
    }
    // isStraight() returns whether or not the switch is in the Straight position
    // Returns - boolean, whether or not the switch is in the Straight position
    public boolean isStraight() 
    {
        return Straight;
    }
    
    // matches(Switch s) returns whether or not this switch object has all the same 
    // instance variable values of the switch passed in
    // Parameters:
    //     Switch s - the switch to be compared to this switch
    // Returns - boolean, whether or not the switches match
    public boolean matches(Switch s)
    {
        boolean result = true;
        
        if (this.LineID != s.LineID || this.SwitchID != s.SwitchID || this.ApproachBlock != s.ApproachBlock ||
                this.StraightBlock != s.StraightBlock || this.DivergentBlock != s.DivergentBlock || this.Straight != s.Straight)
        {
            result = false;
        }
        
        return result;
    }
    
    // Switch(LineColor LineID, int SwitchID, int ApproachBlock, int StraightBlock, int DivergentBlock, boolean Straight)
    // instantiates a new switch object with the specified parameters
    // Parameters:
    //     LineColor LineID - line that the new switch belongs to
    //     int SwitchID - identifier for the new switch
    //     int ApproachBlock - identifier for the approach block
    //     int StraightBlock - identifier for the Straight block
    //     int DivergentBlock - identifier for the divergent block
    //     boolean Straight - initial position of the switch upon instantiation
    public Switch(LineColor lineID, int switchID, int approachBlock, 
            int straightBlock, int divergentBlock, boolean straight)
    {
        this.LineID = lineID;
        this.SwitchID = switchID;
        this.ApproachBlock = approachBlock;
        this.StraightBlock = straightBlock;
        this.DivergentBlock = divergentBlock;
        this.Straight = straight;
    }
    
}
