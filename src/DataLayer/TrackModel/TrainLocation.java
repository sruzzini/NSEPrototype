/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.TrackModel;

import DataLayer.EnumTypes.*;

/**
 *
 * @author rmertz92
 */
public class TrainLocation {
    public LineColor line;
    public int currentBlock;
    public int prevBlock;
    public double distanceSoFar;
    
    public TrainLocation(LineColor line)
    {
        this.line = line;
        switch (line)
        {
            case GREEN:
                currentBlock = 151;
            case RED:
                currentBlock = 77;
        }
        distanceSoFar = 0.0;
    }
    
}
