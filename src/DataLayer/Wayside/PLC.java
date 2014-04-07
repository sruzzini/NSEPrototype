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
public abstract class PLC {
    private final int id;
    private final LineColor line;
    private final Hashtable<Integer, Block> blocks;

    public PLC(int id, LineColor line,  Hashtable<Integer, Block> blocks) {
        this.id = id;
        this.line = line;
        this.blocks = blocks;
    }
    

    
    
    
    
}
