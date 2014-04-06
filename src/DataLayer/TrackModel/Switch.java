
package DataLayer.TrackModel;

public class Switch 
{
    public int lineID;
    public int switchID;
    public int typeOfSwitch; // 1 = diverge, 2 = convergere
    public int approachBlock;
    public int straightBlock;
    public int divergentBlock;
    public boolean straight;
    
    public Switch(int lineID, int switchID, int typeOfSwitch, int approachBlock, 
            int straightBlock, int divergentBlock, boolean straight)
    {
        this.lineID = lineID;
        this.switchID = switchID;
        this.typeOfSwitch = typeOfSwitch;
        this.approachBlock = approachBlock;
        this.straightBlock = straightBlock;
        this.divergentBlock = divergentBlock;
        this.straight = straight;
    }
    
    public boolean isStraight() 
    {
        return straight;
    }
}
