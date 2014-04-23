/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.EnumTypes.LineColor;
import DataLayer.TrackModel.Block;
import DataLayer.TrackModel.Switch;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author nwhachten
 */
public class PLCGreenOneV1 extends PLC{

    public PLCGreenOneV1(int id, LineColor line, HashMap routeTable) {
        super(id, line, routeTable);
    }

    @Override
        public Commands plcProgram() {
        Commands c;
        c = new Commands();
        
        return c;
    }
    
    
}
