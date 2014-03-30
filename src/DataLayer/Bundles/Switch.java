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
public class Switch {
    private final int blockID;
    private final LineColor lineID;
    private final int approachBlock;
    private final int divergentBlock;
    private final int straightBlock;
    private boolean straight;

    public Switch(int blockID, LineColor lineID, int approachBlock, int divergentBlock, int straightBlock) {
        this.blockID = blockID;
        this.lineID = lineID;
        this.approachBlock = approachBlock;
        this.divergentBlock = divergentBlock;
        this.straightBlock = straightBlock;
        this.straight = true;
    }

    public int getBlockID() {
        return blockID;
    }

    public LineColor getLineID() {
        return lineID;
    }

    public int getApproachBlock() {
        return approachBlock;
    }

    public int getDivergentBlock() {
        return divergentBlock;
    }

    public int getStraightBlock() {
        return straightBlock;
    }

    public boolean isStraight() {
        return straight;
    }

    public void setStraight(boolean straight) {
        this.straight = straight;
    }
    
    
    
    
    
}
