/******************************************************************************
 * 
 * PLCGreenOne class
 * 
 * Developed by AJility
 * April 2014
 * CoE 1186
 * 
 * Contributers:
 *  Nathaniel W. Hachten
 *
 *****************************************************************************/

package DataLayer.Wayside;

import DataLayer.Bundles.*;
import DataLayer.EnumTypes.*;
import DataLayer.TrackModel.*;
import java.util.*;

/**
 *<h1>PLCGreenOne</h1>
 * <p>
 * This class contains the PLC for the Green Line controller that controls the top portion
 * of the Green Line according to the excel layout</p>
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

    public PLCGreenOne(int id, LineColor line, HashMap routeTable) {
        super(id, line, routeTable);
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
    
    //plcProgram() is the method that overrides PLC abstract method. This is the portion of code
    //specific to this portion of the Track
    //Returns - Commands, the commands for this program given a set of inputs
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
        if (block150.isOccupied()) //would usually also include if block149.isOccupied || but it never gets unset
        {
            if (trainsComing > 0)
            {
                //push signal commands to stop at block 149 and 150
                c.pushCommand(new BlockSignalBundle(block149.getAuthority(), block149.getDestination(), 0.0, 149, LineColor.GREEN));
                c.pushCommand(new BlockSignalBundle(block150.getAuthority(), block150.getDestination(), 0.0, 150, LineColor.GREEN));
               // c.pushCommand(new BlockInfoBundle(LightColor.RED, block150.getRRXingState(), block150.getBlockID(), LineColor.GREEN, block150.isClosed()));
                trainWaitingAt150 = true;
                trainPassingThru150 = false;
            }
            else
            {
                //puh switch command to switch -3 to point towards block 150
                boolean dir = false;
                if (switch3.StraightBlock == 150) dir = true;
                c.pushCommand(new Switch(LineColor.GREEN, switch3.SwitchID, 
                        switch3.ApproachBlock, switch3.StraightBlock, 
                        switch3.DivergentBlock, dir ));
                //push signal command to increase speed of blocks 149 and 150 to the speed limit
               /* c.pushCommand(new BlockSignalBundle(block149.getAuthority(), 
                        block149.getDestination(), block149.getSpeedLimit(), 149, LineColor.GREEN));
                c.pushCommand(new BlockSignalBundle(block150.getAuthority(), 
                        block150.getDestination(), block150.getSpeedLimit(), 150, LineColor.GREEN));*/
                trainWaitingAt150 = false;
                trainPassingThru150 = true;
            }
        }
        if (block28.isOccupied() && !block150.isOccupied() && ! block29.isOccupied())
        {
           trainExiting = false;
           if (trainPassingThru150)
           {
                trainPassingThru150 = false;
                trainsAway++;
               //System.out.println("PLCGreenOne - plcProgram - trainsAway++ = " + trainsAway);
           }
           else if (trainsComing > 0)
           {
               //System.out.println("PLCGreenOne - plcProgram - setting switch 2 towards 29");
               boolean dir = false;
                if (switch3.StraightBlock == 29) dir = true;
                c.pushCommand(new Switch(LineColor.GREEN, switch3.SwitchID, 
                        switch3.ApproachBlock, switch3.StraightBlock, 
                        switch3.DivergentBlock, dir ));
               trainExiting = true;
           }
        }   
        if (block29.isOccupied() && !block28.isOccupied() && !block150.isOccupied())
        {
            if (trainExiting)
            {
                trainsComing--;
                trainExiting = false;
            }
        }
        if (block13.isOccupied() && !block1.isOccupied() && !block12.isOccupied())
        {
            if (trainPassingThru1)
            {
                trainPassingThru1 = false;
                trainsInLoop--;
                trainsComing++;
            }
            else if (trainsAway > 0)
            {
                //push switch signal to set switch -2 towards A
                boolean dir = false;
                if (switch2.StraightBlock == 12) dir = true;
                c.pushCommand(new Switch(switch2.LineID, switch2.SwitchID, 
                        switch2.ApproachBlock, switch2.StraightBlock, 
                        switch2.DivergentBlock, dir));
                enteringLoop = true;
            }
        }  
        if (block12.isOccupied() && !block1.isOccupied() && !block13.isOccupied())
        {
            if (enteringLoop)
            {
               trainsInLoop++;
               trainsAway--; 
               enteringLoop = false;
            }
            
        }
        if (block1.isOccupied() && !block12.isOccupied() && !block13.isOccupied())
        {
            //System.out.println("PLCGreenOne - plcProgram - block1 is occupied with no adjacent occupancies. TrainsAway =" + trainsAway + " TrainsComing =" + trainsComing);
            if (trainsAway == 0)
            {
                trainPassingThru1 = true;
                trainWaitingAt1 = false;
                //push switch signal to set switch -2 to point towards block 1
                boolean dir = false;
                if (switch2.StraightBlock == 1) dir = true;
                    c.pushCommand(new Switch(switch2.LineID, switch2.SwitchID, 
                        switch2.ApproachBlock, switch2.StraightBlock, switch2.DivergentBlock, dir));
                   // System.out.println("PLCGreenOne - plcProgram - setting switch 2 towards block1");
                //push signal to block one to tell train to go
               /* c.pushCommand(new BlockSignalBundle(block1.getAuthority(), block1.getDestination(),
                        block1.getSpeedLimit(), block1.getBlockID(), LineColor.GREEN));*/
            }
            else if (trainsComing > 0)
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
