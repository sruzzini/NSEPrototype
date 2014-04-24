/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.Bundles.BlockSignalBundle;
import DataLayer.EnumTypes.LineColor;
import DataLayer.TrackModel.Block;
import DataLayer.TrackModel.Switch;
import java.util.HashMap;

/**
 *
 * @author nwhachten
 */
public class PLCRedFour extends PLC {
    private int trainsInLoop;
    private int trainsComing;       //headed towards loop
    private int trainsGoing;        //headed out of plc dominion
    private boolean comingThru49;   //train on block 49 headed towards loop
    private boolean goingThru49;    //train on block 49 headed out of plc dominion
    private boolean enteringLoop;
    private boolean leavingLoop;
    private boolean holdAt66;
    
    
    public PLCRedFour(int id, LineColor line, HashMap routeTable) 
    {
        super(id, line, routeTable);
        this.trainsComing = 0;
        this.trainsGoing = 0;
        this.trainsInLoop = 0;
        this.comingThru49 = false;
        this.goingThru49 = false;
        this.leavingLoop = false;
        this.enteringLoop = false;
        this.holdAt66 = false;
    }
    
    @Override
    protected Commands plcProgram()
    {
        Commands c;
        c = new Commands();
        
        Block block48 = this.blocks.get(48);
        Block block49 = this.blocks.get(49);
        Block block50 = this.blocks.get(50);
        Block block52 = this.blocks.get(52);
        Block block53 = this.blocks.get(53);
        Block block66 = this.blocks.get(66);
        Switch switch6 = this.switches.get(6);
        
        if (block49.isOccupied() && !block48.isOccupied() && !block50.isOccupied())
        {
            if (trainsGoing == 0)
            {
                if (!comingThru49)
                {
                    comingThru49 = true;
                    trainsComing++;
                }
            }
            else
            {
                goingThru49 = true;
            }
        }
        
        if (block50.isOccupied() && !block49.isOccupied())
        {
            comingThru49 = false;
        }
        
        if (block48.isOccupied() && !block49.isOccupied())
        {
            if (goingThru49)
            {
                goingThru49 = false;
                trainsGoing--;
            }
        }
        
        if (block52.isOccupied() && !block53.isOccupied() && !block66.isOccupied())
        {
            if (trainsComing > 0)
            {
                enteringLoop = true;
                holdAt66 = true;
                leavingLoop = false;
                boolean dir = false;
                if (switch6.StraightBlock == 53) dir = true;
                c.pushCommand(new Switch(LineColor.RED, switch6.SwitchID, 
                        switch6.ApproachBlock, switch6.StraightBlock, 
                        switch6.DivergentBlock, dir ));
            }
            else
            {
                if (leavingLoop)
                {
                    leavingLoop = false;
                    trainsInLoop--;
                    trainsGoing++;
                }
            }
        }
        
        if (block53.isOccupied() && !block52.isOccupied())
        {
            if (enteringLoop)
            {
                enteringLoop = false;
                trainsComing--;
                trainsInLoop++;
                if (trainsComing == 0)
                {
                    holdAt66 = false;
                }
            }
        }
        
        if (block66.isOccupied() && !block52.isClosed())
        {
            if (trainsComing > 0)
            {
                holdAt66 = true;
                leavingLoop = false;
                //tell block 66 to stop
                c.pushCommand(new BlockSignalBundle(block66.getAuthority(), block66.getDestination(), 0.0, 66, LineColor.RED));
            }
            else
            {
                if (holdAt66)
                {
                    c.pushCommand(new BlockSignalBundle(block66.getAuthority(), block66.getDestination(), block66.getSpeedLimit(), 66, LineColor.RED));
                }
                holdAt66 = false;
                leavingLoop = true;
                boolean dir = false;
                if (switch6.StraightBlock == 66) dir = true;
                c.pushCommand(new Switch(LineColor.RED, switch6.SwitchID, 
                        switch6.ApproachBlock, switch6.StraightBlock, 
                        switch6.DivergentBlock, dir ));
            }
        }
        
        
        
        return c;
      
    }
    
}
