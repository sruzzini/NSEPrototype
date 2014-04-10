/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.Bundles.BlockInfoBundle;
import DataLayer.Bundles.BlockSignalBundle;
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
    private int trainsInJ;
    private boolean enteringJ;
    private boolean leavingJ;
    private boolean stoppingAtJEnd;
    private boolean holdAtYard;

    public PLCGreenTwo(int id, LineColor line, Hashtable<Integer, Block> blocks, ArrayList<Block> blockArray, Hashtable<Integer, Switch> switches) {
        super(id, line, blocks, blockArray, switches);
        this.trainsInJ = 0;
        this.enteringJ = false;
        this.leavingJ = false;
        this.stoppingAtJEnd = false;
        this.holdAtYard = false;
    }
    
    

    @Override
    protected Commands plcProgram() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Commands c;
        c = new Commands();
        
        Block block152 = this.blocks.get(152);
        Block block57 = this.blocks.get(57);
        Block block58 = this.blocks.get(58);
        Block block62 = this.blocks.get(62);
        Block block61 = this.blocks.get(61);
        Block block60 = this.blocks.get(60);
        Switch switch1 = this.switches.get(1);
        Switch switch4 = this.switches.get(4);
        boolean dir;
       // System.out.println("PLC id: " + this.id + " PLC line " + this.line);
       // System.out.println("block152: " + block152.getBlockID());
        //System.out.println("switch1: " + switch1.switchID);
        
        if (block57.isOccupied())
        {
            if (block57.getDestination() == 151 || block57.getDestination() == 0)
            {
                dir = false;
                if (switch4.straightBlock == 151) dir = true;
                c.pushCommand(new Switch(LineColor.GREEN, switch4.switchID, switch4.approachBlock,
                        switch4.straightBlock, switch4.divergentBlock, dir));
            }
            else
            {
                dir = false;
                if (switch4.straightBlock == 58) dir = true;
                c.pushCommand(new Switch(LineColor.GREEN, switch4.switchID, switch4.approachBlock,
                        switch4.straightBlock, switch4.divergentBlock, dir));
                enteringJ = true;
            }
        }
        
        if (block58.isOccupied())
        {
            if (enteringJ)
            {
                enteringJ = false;
                trainsInJ++;
            }
        }
        
        if (block152.isOccupied())
        {
            if (!leavingJ)
            {
                 //set switch state
                 dir = false;
             //System.out.println("Switch 1: " + switch1.switchID);
             if (switch1.straightBlock == 152) dir = true;
             c.pushCommand(new Switch(LineColor.GREEN, switch1.switchID, switch1.approachBlock,
             switch1.straightBlock, switch1.divergentBlock, dir));
             /*c.pushCommand( new BlockSignalBundle(block60.getAuthority(), block60.getDestination(),
                     0.0, block60.getBlockID(), LineColor.GREEN));
             c.pushCommand( new BlockSignalBundle(block61.getAuthority(), block61.getDestination(),
                     0.0, block61.getBlockID(), LineColor.GREEN));*/
             stoppingAtJEnd = true;
             holdAtYard = false;
            }
            else
            {
                c.pushCommand(new BlockSignalBundle(block152.getAuthority(), block152.getDestination(),
                    0.0, block152.getBlockID(), LineColor.GREEN));
                holdAtYard = true;
            }
        }
        
        if (block60.isOccupied())
        {
            if (!stoppingAtJEnd)
            {
                c.pushCommand(new BlockSignalBundle(block60.getAuthority(), block60.getDestination(),
                    block60.getSpeedLimit(), block60.getBlockID(), LineColor.GREEN));
            }
            else
            {
                c.pushCommand(new BlockSignalBundle(block60.getAuthority(), block60.getDestination(),
                    0.0, block60.getBlockID(), LineColor.GREEN));
            }
        }
        
        if (block61.isOccupied())
        {
            //leavingJ = true;
            if (!stoppingAtJEnd)
            {
                leavingJ = true;
                dir = false;
                if (switch1.straightBlock == 61) dir = true;
                c.pushCommand(new Switch(LineColor.GREEN, switch1.switchID, switch1.approachBlock,
                    switch1.straightBlock, switch1.divergentBlock, dir));
                c.pushCommand(new BlockSignalBundle(block61.getAuthority(), block61.getDestination(),
                    block61.getSpeedLimit(), block61.getBlockID(), LineColor.GREEN));
                
            }
            else
            {
               /* c.pushCommand(new BlockSignalBundle(block61.getAuthority(), block61.getDestination(),
                    0.0, block61.getBlockID(), LineColor.GREEN));*/
            }
        }
                 
        
        if (block62.isOccupied())
        {
            if (leavingJ)
            {
                trainsInJ--;
                leavingJ = false;
            }
            if (holdAtYard)
            {
                c.pushCommand(new BlockSignalBundle(block152.getAuthority(), block152.getDestination(),
                    block152.getSpeedLimit(), block152.getBlockID(), LineColor.GREEN));
                dir = false;
                if (switch1.straightBlock == 152) dir = true;
                c.pushCommand(new Switch(LineColor.GREEN, switch1.switchID, switch1.approachBlock,
             switch1.straightBlock, switch1.divergentBlock, dir));
                holdAtYard = false;
            }
            
            stoppingAtJEnd = false;
        }
        
        
        
        
        return c;
        
        
    
    
    }
    
}
