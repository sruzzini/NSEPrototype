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
    public final int blockID;
    public final LineColor lineID;
    public boolean closed;

    public BlockBundle(int blockID, LineColor lineID) {
        this.blockID = blockID;
        this.lineID = lineID;
        this.closed = false;
    }

    
    
    
    
    
    
    
    
}
