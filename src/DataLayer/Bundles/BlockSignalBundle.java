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
    private final int authority;
    private final int destination;
    private double speed;

    public BlockSignalBundle(int authority, int destination, double speed, int blockID, LineColor lineID) {
        super(blockID, lineID);
        this.authority = authority;
        this.destination = destination;
        this.speed = speed;
    }

    public int getAuthority() {
        return authority;
    }

    public int getDestination() {
        return destination;
    }

    public double getSpeed() {
        return speed;
    }

    /**
     * This method is needed because when a signal is sent from CTC to Wayside,
     * the speed may need to be adjusted to a more safe speed.
     * 
     * @param speed 
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    
    
    
}
