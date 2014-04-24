/******************************************************************************
 * 
 * PLCRedThree class
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
 *
 * @author nwhachten
 */
public class PLCRedThree extends PLC {
    public PLCRedThree(int id, LineColor line, HashMap routeTable) 
    {
        super(id, line, routeTable);
    }
    
    @Override
    protected Commands plcProgram()
    {
        Commands c;
        c = new Commands();
        
        Block block38 = this.blocks.get(38);
        Block block39 = this.blocks.get(39);
        Block block43 = this.blocks.get(43);
        Block block44 = this.blocks.get(44);
        Switch switch4 = this.switches.get(4);
        Switch switch5 = this.switches.get(5);
        
        if (block38.isOccupied() || block39.isOccupied())
        {
            //set switch 2 towards 28
            boolean dir = false;
                if (switch4.StraightBlock == 39) dir = true;
                c.pushCommand(new Switch(LineColor.RED, switch4.SwitchID, 
                        switch4.ApproachBlock, switch4.StraightBlock, 
                        switch4.DivergentBlock, dir ));
        }
        
        if (block43.isOccupied() || block44.isOccupied())
        {
            //set switch 3 towards 32
            boolean dir = false;
                if (switch5.StraightBlock == 43) dir = true;
                c.pushCommand(new Switch(LineColor.RED, switch5.SwitchID, 
                        switch5.ApproachBlock, switch5.StraightBlock, 
                        switch5.DivergentBlock, dir ));
        }
        
        
        return c;
      
    }
    
}
