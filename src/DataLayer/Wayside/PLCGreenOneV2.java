/******************************************************************************
 * 
 * PLCGreenOneV2 class
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
import java.util.HashMap;

/**
 *<h1>PLCGreenOneV2</h1>
 * <p>
 * This class contains a modified PLC for the Green Line controller that controls the top portion
 * of the Green Line according to the excel layout</p>
 * @author nwhachten
 */
public class PLCGreenOneV2 extends PLC {

    public PLCGreenOneV2(int id, LineColor line, HashMap routeTable) {
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
        
        return c;
    }
    
}
