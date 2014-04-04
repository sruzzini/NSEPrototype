/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.EnumTypes.LineColor;

/**
 *
 * @author nwhachten
 */
public abstract class PLC {
    private final int id;
    private final LineColor line;
    private final int[] blockNums;

    public PLC(int id, LineColor line, int[] blockNums) {
        this.id = id;
        this.line = line;
        this.blockNums = blockNums;
    }
    

    
    
    
    
}
