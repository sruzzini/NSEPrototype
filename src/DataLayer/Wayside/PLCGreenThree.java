/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.EnumTypes.LineColor;
import DataLayer.TrackModel.Block;
import DataLayer.TrackModel.Switch;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author nwhachten
 */
public class PLCGreenThree extends PLC{
    int trainsInLoop;
    int trainsAway;
    int trainsComing;
    boolean enteringLoop;
    boolean trainExiting;
    boolean trainWaitingAt76;
    boolean trainPassingThru76;
    boolean trainWaitingAt100;
    boolean trainPassingThru100;

    public PLCGreenThree(int id, LineColor line, Hashtable<Integer, Block> blocks, ArrayList<Block> blockArray, Hashtable<Integer, Switch> switches) {
        super(id, line, blocks, blockArray, switches);
        this.trainsInLoop = 0;
        this.trainsAway = 0;
        this.trainsComing = 0;
        this.enteringLoop = false;
        this.trainExiting = false;
        this.trainWaitingAt76 = false;
        this.trainWaitingAt100 = false;
        this.trainPassingThru76 = false;
        this.trainPassingThru100 = false;
    }

    @Override
    protected Commands plcProgram() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
