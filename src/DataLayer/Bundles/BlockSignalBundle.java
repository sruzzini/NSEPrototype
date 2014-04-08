/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Bundles;

import DataLayer.EnumTypes.LineColor;

/**
 * This class provides functionality for sending signals to a block.
 * 
 * Information contained in the signal specifies the desired authority, destination, and speed.
 *
 * @author nwhachten
 * @version 1.0
 * @since 2014-03-30
 * 
 */
public class BlockSignalBundle extends BlockBundle {
    public final int Authority;
    public final int Destination;
    public double Speed;

    public BlockSignalBundle(int Authority, int Destination, double Speed, int blockID, LineColor lineID) {
        super(blockID, lineID);
        this.Authority = Authority;
        this.Destination = Destination;
        this.Speed = Speed;
    }
    
    public BlockSignalBundle(int Authority, int Destination, double Speed, int blockID, LineColor lineID, boolean closed) {
        super(blockID, lineID, closed);
        this.Authority = Authority;
        this.Destination = Destination;
        this.Speed = Speed;
    }
    
    public boolean matches(BlockSignalBundle b)
    {
        boolean result = true;
        
        if (this.Authority != b.Authority || this.Destination != b.Authority || this.Speed != b.Speed || !super.matches(b))
        {
            result = false;
        }
        
        return result;
    }
    
    
    
}
