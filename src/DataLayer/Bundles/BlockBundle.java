/******************************************************************************
 * 
 * BlockBundle class
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
 *
 * @author nwhachten
 * @version 1.0
 * @since 2014-03-30
 */
public abstract class BlockBundle {
    public final int BlockID;
    public boolean Closed;
    public final LineColor LineID;
    

    public BlockBundle(int BlockID, LineColor LineID) {
        this.BlockID = BlockID;
        this.LineID = LineID;
        this.Closed = false;
    }
    
    public BlockBundle(int BlockID, LineColor LineID, boolean closed) {
        this.BlockID = BlockID;
        this.LineID = LineID;
        this.Closed = closed;
    }
    
    public boolean matches(BlockBundle b)
    {
        boolean result = true;
        if (this.BlockID != b.BlockID || this.LineID != b.LineID || this.Closed != b.Closed)
        {
            result = false;
        }
        
        return result;
    }
    
}
