/******************************************************************************
 * 
 * Dispatch class
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
 */
public class DispatchBundle extends BlockSignalBundle {
    public int trainID;
    public LineColor toLine;

    public DispatchBundle(int trainID, LineColor toLine, int Authority, int Destination, double Speed, int blockID, LineColor lineID, boolean closed) {
        super(Authority, Destination, Speed, blockID, lineID, closed);
        this.trainID = trainID;
        this.toLine = toLine;
    }
    
    public DispatchBundle(BlockSignalBundle b, int trainID, LineColor toLine)
    {
        super(b.Authority, b.Destination, b.Speed, b.BlockID, b.LineID, b.Closed);
        this.trainID = trainID;
        this.toLine = toLine;
    }
    
    public DispatchBundle copy()
    {
        return new DispatchBundle(this.trainID, this.toLine, this.Authority, this.Destination, this.Speed, this.BlockID, this.LineID, this.Closed);
    }
    
}
