package DataLayer.TrackModel;

import DataLayer.EnumTypes.LineColor;

public class Switch 
{
    public LineColor lineID;
    public int switchID;
    public int approachBlock;
    public int straightBlock;
    public int divergentBlock;
    public boolean straight;
    
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
    
    public boolean isStraight() 
    {
        return straight;
    }
    
    public boolean matches(Switch s)
    {
        boolean result = true;
        
        if (this.lineID != s.lineID || this.switchID != s.switchID || this.approachBlock != s.approachBlock ||
                this.straightBlock != s.straightBlock || this.divergentBlock != s.divergentBlock || this.straight != s.straight)
        {
            result = false;
        }
        
        return false;
    }
}
