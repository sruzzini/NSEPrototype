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
public class PLCRedFour extends PLC {
    private int trainsInLoop;
    private int trainsComing;       //headed towards loop
    private int trainsGoing;        //headed out of plc dominion
    private boolean comingThru49;   //train on block 49 headed towards loop
    private boolean goingThru49;    //train on block 49 headed out of plc dominion
    
    public PLCRedFour(int id, LineColor line, HashMap routeTable) 
    {
        super(id, line, routeTable);
        this.trainsComing = 0;
        this.trainsGoing = 0;
        this.trainsInLoop = 0;
        this.comingThru49 = false;
        this.goingThru49 = false;
    }
    
    @Override
    protected Commands plcProgram()
    {
        Commands c;
        c = new Commands();
        
        Block block48 = this.blocks.get(48);
        Block block49 = this.blocks.get(49);
        Block block50 = this.blocks.get(50);
        Switch switch7 = this.switches.get(7);
        
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
        
        return c;
      
    }
    
}
