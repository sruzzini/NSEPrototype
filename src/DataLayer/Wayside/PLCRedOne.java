/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.EnumTypes.LineColor;
import java.util.HashMap;

/**
 *
 * @author nwhachten
 */
public class PLCRedOne extends PLC{
    
    public PLCRedOne(int id, LineColor line, HashMap routeTable) 
    {
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
