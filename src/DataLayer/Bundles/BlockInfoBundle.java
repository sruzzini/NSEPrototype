/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Bundles;

import DataLayer.EnumTypes.LightColor;
import DataLayer.EnumTypes.LineColor;
import DataLayer.EnumTypes.XingState;

/**
 * This class contains information pertaining to a block on the track.
 * 
 * It is used for things like setting light colors, setting railroad crossings, 
 * getting speed limits, and getting track occupancies.
 *
 * @author nwhachten
 * @version 1.0
 * @since 03-30-2014
 */
public class BlockInfoBundle extends BlockBundle {
    public LightColor lightColor;
    public XingState rrXingState;

    public BlockInfoBundle(LightColor lightColor, XingState rrXingState, int blockID, LineColor lineID) {
        super(blockID, lineID);
        this.lightColor = lightColor;
        this.rrXingState = rrXingState;
    }

    
    
}
