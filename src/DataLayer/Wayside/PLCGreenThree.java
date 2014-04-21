/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.Bundles.*;
import DataLayer.EnumTypes.*;
import DataLayer.TrackModel.*;
import java.util.*;

/**
 *
 * @author nwhachten
 */
public class PLCGreenThree extends PLC{
    private boolean enteringLoop;
    private int trainsAway;
    private int trainsComing;
    private boolean trainExiting;
    private int trainsInLoop;
    private boolean trainPassingThru100;
    private boolean trainPassingThru76;
    private boolean trainWaitingAt100;
    private boolean trainWaitingAt76;


    public PLCGreenThree(int id, LineColor line, Hashtable<Integer, Block> blocks, ArrayList<Block> blockArray, Hashtable<Integer, Switch> switches, HashMap routeTable) {
        super(id, line, blocks, blockArray, switches, routeTable);
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
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    Commands c;
        c = new Commands();
        
        Block block100 = this.blocks.get(100);
        Block block86 = this.blocks.get(86);    
        Block block85 = this.blocks.get(85);    
        Block block77 = this.blocks.get(77);    
        Block block101 = this.blocks.get(101);    
        Block block76 = this.blocks.get(76);  
        Switch switch6 = this.switches.get(6); 
        Switch switch5 = this.switches.get(5);  
        
        //System.out.println("Block 149: " + block149.getBlockID() + " Block 150: " + block150.getBlockID());
        if ( block76.isOccupied() && !block77.isOccupied())
        {
            if (trainsComing > 0)
            {
                //push signal commands to stop at block 149 and 150
                //c.pushCommand(new BlockSignalBundle(block149.getAuthority(), block149.getDestination(), 0.0, 149, LineColor.GREEN));
                c.pushCommand(new BlockSignalBundle(block76.getAuthority(), block76.getDestination(), 0.0, block76.getBlockID(), LineColor.GREEN));
                trainWaitingAt76 = true;
                trainPassingThru76 = false;
            }
            else
            {
                //puh switch command to switch -3 to point towards block 150
                boolean dir = false;
                if (switch5.straightBlock == 76) dir = true;
                c.pushCommand(new Switch(LineColor.GREEN, switch5.switchID, 
                        switch5.approachBlock, switch5.straightBlock, 
                        switch5.divergentBlock, dir ));
                //push signal command to increase speed of blocks 149 and 150 to the speed limit
               // c.pushCommand(new BlockSignalBundle(block149.getAuthority(), 
                //        block149.getDestination(), block149.getSpeedLimit(), 149, LineColor.GREEN));
                //c.pushCommand(new BlockSignalBundle(block76.getAuthority(), 
                  //      block76.getDestination(), block76.getSpeedLimit(), block76.getBlockID(), LineColor.GREEN));
                trainWaitingAt76 = false;
                trainPassingThru76 = true;
            }
        }
        if (block77.isOccupied() && !block76.isOccupied() && !block101.isOccupied())
        {
           trainExiting = false;
           if (trainPassingThru76)
           {
                trainPassingThru76 = false;
                trainsAway++;
           }
           else if (trainsComing > 0)
           {
               trainExiting = true;
               boolean dir = false;
               if (switch5.straightBlock == 101) dir = true;
               c.pushCommand(new Switch(LineColor.GREEN, switch5.switchID, 
                        switch5.approachBlock, switch5.straightBlock, 
                        switch5.divergentBlock, dir ));
           }
        }   
        if (block101.isOccupied() && !block77.isOccupied())
        {
            if (trainExiting)
            {
                trainsComing--;
                trainExiting = false;
            }
        }
        if (block85.isOccupied() && !block86.isOccupied() && !block100.isOccupied())
        {
            if (trainPassingThru100)
            {
                trainPassingThru100 = false;
                trainsInLoop--;
                trainsComing++;
            }
            else if (trainsAway > 0)
            {
                //push switch signal to set switch -2 towards A
                boolean dir = false;
                if (switch6.straightBlock == 86) dir = true;
                c.pushCommand(new Switch(switch6.lineID, switch6.switchID, 
                        switch6.approachBlock, switch6.straightBlock, 
                        switch6.divergentBlock, dir));
                enteringLoop = true;
            }
        }  
        if (block86.isOccupied() && !block85.isOccupied())
        {
            if (enteringLoop)
            {
               trainsInLoop++;
               trainsAway--; 
               enteringLoop = false;
            }
            
        }
        if (block100.isOccupied() && !block85.isOccupied())
        {
            //System.out.println("Trains away = " + this.trainsAway);
            if (trainsAway == 0)
            {
                trainPassingThru100 = true;
                trainWaitingAt100 = false;
                //push switch signal to set switch -2 to point towards block 1
                boolean dir = false;
                if (switch6.straightBlock == 100) dir = true;
                c.pushCommand(new Switch(switch6.lineID, switch6.switchID, 
                        switch6.approachBlock, switch6.straightBlock, switch6.divergentBlock, dir));
                //push signal to block one to tell train to go
                //c.pushCommand(new BlockSignalBundle(block100.getAuthority(), block100.getDestination(),
                        //block100.getSpeedLimit(), block100.getBlockID(), LineColor.GREEN));
            }
            else 
            {
                trainPassingThru100 = false;
                trainWaitingAt100 = true;
                //push signal to tell train at block one to stop by zpeed = 0
                c.pushCommand(new BlockSignalBundle(block100.getAuthority(), block100.getDestination(),
                        0.0, block100.getBlockID(), LineColor.GREEN));
            }
        }
        
       
        
        
        return c;
    }
    
    
 }
    
    
    

