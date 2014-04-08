/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.EnumTypes.LineColor;
import DataLayer.TrackModel.Block;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author nwhachten
 */
public class PLCGreenOne extends PLC {

    public PLCGreenOne(int id, LineColor line, Hashtable<Integer, Block> blockNums, ArrayList<Block> blockArray) {
        super(id, line, blockNums, blockArray);
    }
    
    
}
