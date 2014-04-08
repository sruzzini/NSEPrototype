/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Bundles;

import DataLayer.EnumTypes.LineColor;

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
    
}
