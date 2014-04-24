/******************************************************************************
 * 
 * BlocInfokBundle class
 * 
 * Developed by AJility
 * April 2014
 * CoE 1186
 * 
 * Contributers:
 *  Nathaniel W. Hachten
 *  Ryan Mertz
 *  Michael Kudlaty
 *
 *****************************************************************************/

package DataLayer.Bundles;

import DataLayer.EnumTypes.*;

/**
 * This class contains information pertaining to a block on the track.
 * 
 * It is used for things like setting light colors, setting railroad crossings, 
 * getting speed limits, and getting track occupancies.
 *
 * @author nwhachten
 * @version 1.0
 * @since 03-30-2014
 */
public class BlockInfoBundle extends BlockBundle {
    public LightColor LightColor;
    public XingState RRXingState;

    public BlockInfoBundle(LightColor LightColor, XingState RRXingState, int BlockID, LineColor LineID) {
        super(BlockID, LineID);
        this.LightColor = LightColor;
        this.RRXingState = RRXingState;
    }

    public BlockInfoBundle(LightColor LightColor, XingState RRXingState, int BlockID, LineColor LineID, boolean closed) {
        super(BlockID, LineID, closed);
        this.LightColor = LightColor;
        this.RRXingState = RRXingState;
    }

    public boolean matches(BlockInfoBundle b)
    {
        boolean result = true;
        
        if (this.LightColor != b.LightColor || this.RRXingState != b.RRXingState || !super.matches(b))
        {
            result = false;
        }
        
        
        
        return result;
    }
    
    @Override
    public String toString()
    {
        return "BlockInfoBundle - Block: " + this.BlockID + "  Line: " + this.LineID + "  Light: " + this.LightColor + "  Xing: " + this.RRXingState + "\n";
    }
    
    
}
