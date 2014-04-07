/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Bundles;

import DataLayer.EnumTypes.LineColor;

/**
 *
 * @author nwhachten
 */
public class DispatchBundle extends BlockSignalBundle {
    public int trainID;

    public DispatchBundle(int trainID, int Authority, int Destination, double Speed, int blockID, LineColor lineID, boolean closed) {
        super(Authority, Destination, Speed, blockID, lineID, closed);
        this.trainID = trainID;
    }
    
}
