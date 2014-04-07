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
 * @version 1.0
 * @since 2014-03-30
 */
public abstract class BlockBundle {
    public final int BlockID;
    public final LineColor LineID;
    public boolean Closed;

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
    
}
