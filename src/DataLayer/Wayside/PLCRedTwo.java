/******************************************************************************
 * 
 * PLCRedTwo class
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

import DataLayer.EnumTypes.LineColor;
import DataLayer.TrackModel.Block;
import DataLayer.TrackModel.Switch;
import java.util.HashMap;

/**
 *<h1>PLCRedTwo</h1>
 * <p>
 * This class contains the PLC for the Red Line controller that controls the top-middle portion
 * of the Red Line according to the excel layout</p>
 * @author nwhachten
 */
public class PLCRedTwo extends PLC {
    public PLCRedTwo(int id, LineColor line, HashMap routeTable) 
    {
        super(id, line, routeTable);
    }
    
    //plcProgram() is the method that overrides PLC abstract method. This is the portion of code
    //specific to this portion of the Track
    //Returns - Commands, the commands for this program given a set of inputs
    @Override
    protected Commands plcProgram()
    {
        Commands c;
        c = new Commands();
        
        Block block27 = this.blocks.get(27);
        Block block28 = this.blocks.get(28);
        Block block32 = this.blocks.get(32);
        Block block33 = this.blocks.get(33);
        Switch switch2 = this.switches.get(2);
        Switch switch3 = this.switches.get(3);
        
        if (block27.isOccupied() || block28.isOccupied())
        {
            //set switch 2 towards 28
            boolean dir = false;
                if (switch2.StraightBlock == 28) dir = true;
                c.pushCommand(new Switch(LineColor.RED, switch2.SwitchID, 
                        switch2.ApproachBlock, switch2.StraightBlock, 
                        switch2.DivergentBlock, dir ));
        }
        
        if (block32.isOccupied() || block33.isOccupied())
        {
            //set switch 3 towards 32
            boolean dir = false;
                if (switch3.StraightBlock == 32) dir = true;
                c.pushCommand(new Switch(LineColor.RED, switch3.SwitchID, 
                        switch3.ApproachBlock, switch3.StraightBlock, 
                        switch3.DivergentBlock, dir ));
        }
        
        return c;
      
    }
    
}
