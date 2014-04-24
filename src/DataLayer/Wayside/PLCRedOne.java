/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.EnumTypes.LineColor;
import DataLayer.TrackModel.Block;
import DataLayer.TrackModel.Switch;
import java.util.HashMap;

/**
 *
 * @author nwhachten
 */
public class PLCRedOne extends PLC{
    private int trainsDeparting;
    private int trainsReturning;
    private int trainsComing;
    private int trainsGoing;
    private boolean leavingYard;
    private boolean returningToYard;
    private boolean leavingLoop;
    private boolean enteringLoop;
    private boolean entering;
    private boolean exiting;
    
    public PLCRedOne(int id, LineColor line, HashMap routeTable) 
    {
        super(id, line, routeTable);
        this.trainsComing = 0;
        this.trainsDeparting = 0;
        this.trainsGoing = 0;
        this.trainsReturning = 0;
        this.leavingLoop = false;
        this.leavingYard = false;
        this.enteringLoop = false;
        this.returningToYard = false;
    }
    
    @Override
    protected Commands plcProgram()
    {
        Commands c;
        c = new Commands();
        
        Block block9 = this.blocks.get(9);
        Block block77 = this.blocks.get(77);
        Block block1 = this.blocks.get(1);
        Block block15 = this.blocks.get(15);
        Block block16 = this.blocks.get(16);
        Block block10 = this.blocks.get(10);
        Block block20 = this.blocks.get(20);
        Block block21 = this.blocks.get(21);
        Switch switch1 = this.switches.get(1);
        Switch switch7 = this.switches.get(7);
        
        if (block77.isOccupied() && !block9.isOccupied())
        {
            if (returningToYard)
            {
                returningToYard = false;
                trainsReturning--;
            }
            else
            {
                leavingYard = true;
                boolean dir = false;
                if (switch7.StraightBlock == 77) dir = true;
                c.pushCommand(new Switch(LineColor.RED, switch7.SwitchID, 
                        switch7.ApproachBlock, switch7.StraightBlock, 
                        switch7.DivergentBlock, dir ));
            }
        }
        
        if (block9.isOccupied() && !block77.isOccupied())
        {
            if (leavingYard)
            {
                leavingYard = false;
                trainsDeparting++;
            }
            else if (trainsDeparting == 0)
            {
                returningToYard = true;
                boolean dir = false;
                if (switch7.StraightBlock == 77) dir = true;
                c.pushCommand(new Switch(LineColor.RED, switch7.SwitchID, 
                        switch7.ApproachBlock, switch7.StraightBlock, 
                        switch7.DivergentBlock, dir ));
            }
        }
        
        if (block1.isOccupied() && !block16.isOccupied())
        {
            if (trainsDeparting > 0)
            {
                leavingLoop = true;
                boolean dir = false;
                if (switch1.StraightBlock == 1) dir = true;
                c.pushCommand(new Switch(LineColor.RED, switch1.SwitchID, 
                        switch1.ApproachBlock, switch1.StraightBlock, 
                        switch1.DivergentBlock, dir ));
            }
            else if (enteringLoop)
            {
                enteringLoop = false;
                trainsComing--;
                trainsReturning++;
            }
        }
        
        if (block16.isOccupied() && !block1.isOccupied())
        {
            if (leavingLoop)
            {
                leavingLoop = false;
                trainsDeparting--;
                trainsGoing++;
            }
            else if (trainsComing > 0)
            {
                enteringLoop = true;
                if (trainsDeparting > 0)
                {
                    //set switch towards block 15
                    boolean dir = false;
                    if (switch1.StraightBlock == 15) dir = true;
                    c.pushCommand(new Switch(LineColor.RED, switch1.SwitchID, 
                        switch1.ApproachBlock, switch1.StraightBlock, 
                        switch1.DivergentBlock, dir ));
                    
                }
                else
                {
                    //set switch towards block 1
                    boolean dir = false;
                    if (switch1.StraightBlock == 1) dir = true;
                    c.pushCommand(new Switch(LineColor.RED, switch1.SwitchID, 
                        switch1.ApproachBlock, switch1.StraightBlock, 
                        switch1.DivergentBlock, dir ));
                }
            }
        }
        
        if (block10.isOccupied() && !block9.isOccupied())
        {
            boolean dir = false;
            if (switch7.StraightBlock == 10) dir = true;
                c.pushCommand(new Switch(LineColor.RED, switch7.SwitchID, 
                        switch7.ApproachBlock, switch7.StraightBlock, 
                        switch7.DivergentBlock, dir ));
        }
        
        if (block15.isOccupied() && !block16.isOccupied())
        {
            if (enteringLoop)
            {
                enteringLoop = false;
                trainsComing--;
                trainsDeparting++;
            }
        }
        
        if (block20.isOccupied() && !block21.isOccupied())
        {
            if (trainsGoing > 0)
            {
                exiting = true;
            }
            else if (entering)
            {
                entering = false;
                trainsComing++;
            }
        }
        
        if (block21.isOccupied() && !block20.isOccupied())
        {
            if (exiting)
            {
                exiting = false;
                trainsGoing--;
            }
            else
            {
                entering = true;
            }
        }
        
        return c;
      
    }
    
}
