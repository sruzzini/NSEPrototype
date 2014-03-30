/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Bundles;

import DataLayer.EnumTypes.LightColor;
import DataLayer.EnumTypes.LineColor;
import DataLayer.EnumTypes.XingState;

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
    private LightColor lightColor;
    private boolean occupied;
    private XingState rrXingState;
    private final double speedLimit;

    /**
     * BlockInfoBundle constructor
     *
     * @param speedLimit  Specifies the maximum speed in KM/H for this block
     * @param lightColor  Specifies if there is a light on this block, and if so what its color should be initialized as.
     * @param rrXingState Specifies if there is a railroad crossing on this block, and if so what its state should be initialized as.
     * @param blockID     Specifies the block ID number for this block.
     * @param lineID      Specifies the line color that this block resides in.
     */
    public BlockInfoBundle(double speedLimit, LightColor lightColor, XingState rrXingState, int blockID, LineColor lineID) {
        super(blockID, lineID);
        this.speedLimit = speedLimit;
        this.lightColor = lightColor;
        this.rrXingState = rrXingState;
        this.occupied = false;
    }

    public LightColor getLightColor() {
        return lightColor;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public XingState getRrXingState() {
        return rrXingState;
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public void setLightColor(LightColor lightColor) {
        this.lightColor = lightColor;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public void setRrXingState(XingState rrXingState) {
        this.rrXingState = rrXingState;
    }
    
    
    
    
}
