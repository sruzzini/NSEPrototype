/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.EnumTypes.LineColor;
import DataLayer.TrackModel.Block;
import java.util.Hashtable;

/**
 *
 * @author nwhachten
 */
public class PLCRedTwo extends PLC {

    public PLCRedTwo(int id, LineColor line, Hashtable<Integer, Block> blockNums) {
        super(id, line, blockNums);
    }
    
}
