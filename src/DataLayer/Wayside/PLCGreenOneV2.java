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
 *
 * @author nwhachten
 */
public class PLCGreenOneV2 extends PLC {

    public PLCGreenOneV2(int id, LineColor line, HashMap routeTable) {
        super(id, line, routeTable);
    }
    
    @Override
    protected Commands plcProgram()
    {
        Commands c;
        c = new Commands();
        
        return c;
    }
    
}
