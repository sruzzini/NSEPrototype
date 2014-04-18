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
public class PLCGreenOne extends PLC {
    private boolean enteringLoop;
    private boolean trainExiting;
    private boolean trainPassingThru1;
    private boolean trainPassingThru150;
    private int trainsAway;
    private int trainsComing;
    private int trainsInLoop;
    private boolean trainWaitingAt1;
    private boolean trainWaitingAt150;

    public PLCGreenOne(int id, LineColor line, Hashtable<Integer, Block> blockNums, ArrayList<Block> blockArray, Hashtable<Integer, Switch> switches, HashMap routeTable) {
        super(id, line, blockNums, blockArray, switches, routeTable);
        trainsAway = 0;
        trainsInLoop = 0;
        trainsComing = 0;
        trainWaitingAt150 = false;
        trainPassingThru150 = false;
        trainPassingThru1 = false;
        trainWaitingAt1 = false;
        enteringLoop = false;
        trainExiting = false;
    }
    
    @Override
    protected Commands plcProgram()
    {
        Commands c;
        c = new Commands();
        
        Block block1 = this.blocks.get(1);
        Block block12 = this.blocks.get(12);    //C - switch with A and D
        Block block13 = this.blocks.get(13);    //D - switch with A and C
        Block block28 = this.blocks.get(28);    //F - switch with Z and G
        Block block29 = this.blocks.get(29);    //G - switch with Z and F
        Block block149 = this.blocks.get(149);  //Y
        Block block150 = this.blocks.get(150);  //Z - switch with F and G
        Switch switch2 = this.switches.get(2); //A, C, D
        Switch switch3 = this.switches.get(3);  //Z, F, G
        
        //System.out.println("Block 149: " + block149.getBlockID() + " Block 150: " + block150.getBlockID());
        if (block149.isOccupied() || block150.isOccupied())
        {
            if (trainsComing > 0)
            {
                //push signal commands to stop at block 149 and 150
                c.pushCommand(new BlockSignalBundle(block149.getAuthority(), block149.getDestination(), 0.0, 149, LineColor.GREEN));
                c.pushCommand(new BlockSignalBundle(block150.getAuthority(), block150.getDestination(), 0.0, 150, LineColor.GREEN));
                trainWaitingAt150 = true;
                trainPassingThru150 = false;
            }
            else
            {
                //puh switch command to switch -3 to point towards block 150
                boolean dir = false;
                if (switch3.straightBlock == 150) dir = true;
                c.pushCommand(new Switch(LineColor.GREEN, switch3.switchID, 
                        switch3.approachBlock, switch3.straightBlock, 
                        switch3.divergentBlock, dir ));
                //push signal command to increase speed of blocks 149 and 150 to the speed limit
                c.pushCommand(new BlockSignalBundle(block149.getAuthority(), 
                        block149.getDestination(), block149.getSpeedLimit(), 149, LineColor.GREEN));
                c.pushCommand(new BlockSignalBundle(block150.getAuthority(), 
                        block150.getDestination(), block150.getSpeedLimit(), 150, LineColor.GREEN));
                trainWaitingAt150 = false;
                trainPassingThru150 = true;
            }
        }
        if (block28.isOccupied())
        {
           trainExiting = false;
           if (trainPassingThru150)
           {
                trainPassingThru150 = false;
                trainsAway++;
           }
           else 
           {
               trainExiting = true;
           }
        }   
        if (block29.isOccupied())
        {
            if (trainExiting)
            {
                trainsComing--;
                trainExiting = false;
            }
        }
        if (block13.isOccupied())
        {
            if (trainPassingThru1)
            {
                trainPassingThru1 = false;
                trainsInLoop--;
                trainsComing++;
            }
            else
            {
                //push switch signal to set switch -2 towards A
                boolean dir = false;
                if (switch2.straightBlock == 1) dir = true;
                c.pushCommand(new Switch(switch2.lineID, switch2.switchID, 
                        switch2.approachBlock, switch2.straightBlock, 
                        switch2.divergentBlock, dir));
                enteringLoop = true;
            }
        }  
        if (block12.isOccupied())
        {
            if (enteringLoop)
            {
               trainsInLoop++;
               trainsAway--; 
               enteringLoop = false;
            }
            
        }
        if (block1.isOccupied())
        {
            if (trainsAway == 0)
            {
                trainPassingThru1 = true;
                trainWaitingAt1 = false;
                //push switch signal to set switch -2 to point towards block 1
                boolean dir = false;
                if (switch2.straightBlock == 1) dir = true;
                c.pushCommand(new Switch(switch2.lineID, switch2.switchID, 
                        switch2.approachBlock, switch2.straightBlock, switch2.divergentBlock, dir));
                //push signal to block one to tell train to go
                c.pushCommand(new BlockSignalBundle(block1.getAuthority(), block1.getDestination(),
                        block1.getSpeedLimit(), block1.getBlockID(), LineColor.GREEN));
            }
            else 
            {
                trainPassingThru1 = false;
                trainWaitingAt1 = true;
                //push signal to tell train at block one to stop by zpeed = 0
                c.pushCommand(new BlockSignalBundle(block1.getAuthority(), block1.getDestination(),
                        0.0, block1.getBlockID(), LineColor.GREEN));
            }
        }
        
       
        
        
        return c;
    }
    
    
}
