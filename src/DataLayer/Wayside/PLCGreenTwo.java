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
public class PLCGreenTwo extends PLC{

    public PLCGreenTwo(int id, LineColor line, Hashtable<Integer, Block> blocks, ArrayList<Block> blockArray, Hashtable<Integer, Switch> switches) {
        super(id, line, blocks, blockArray, switches);
    }
    
    

    @Override
    protected Commands plcProgram() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Commands c;
        c = new Commands();
        
        Block block152 = this.blocks.get(152);
        Switch switch1 = this.switches.get(1);
       // System.out.println("PLC id: " + this.id + " PLC line " + this.line);
       // System.out.println("block152: " + block152.getBlockID());
        //System.out.println("switch1: " + switch1.switchID);
        
        if (block152.isOccupied())
        {
            //set switch state
            boolean dir = false;
            //System.out.println("Switch 1: " + switch1.switchID);
            if (switch1.straightBlock == 152) dir = true;
            c.pushCommand(new Switch(LineColor.GREEN, switch1.switchID, switch1.approachBlock,
            switch1.straightBlock, switch1.divergentBlock, dir));
        }
        
        return c;
    
    
    }
    
}
