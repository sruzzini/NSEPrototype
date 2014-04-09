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
    
    public TrainLocation()
    {
        this.line = LineColor.YARD;
        this.currentBlock = 0;
        this.distanceSoFar = 0.0;
    }
    
    public void setStartLocation(int lineNum)
    {
        prevBlock = 0;
        distanceSoFar = 0.0;
        if (lineNum == 0) // green
        {
            line = LineColor.GREEN;
            currentBlock = 152;
        }
        else if (lineNum == 1) // red
        {
            line = LineColor.RED;
            currentBlock = 77;
        }
    }
    
    public void updateLocation(double deltaX, double length, int prev, int next)
    {
        this.distanceSoFar += deltaX;
        if(distanceSoFar > length)
        {
            distanceSoFar -= length;
            if(prev == prevBlock)
            {
                prevBlock = currentBlock;
                currentBlock = next;
            }
            else if(next == prevBlock)
            {
                prevBlock = currentBlock;
                currentBlock = prev;
            }
        }
    }
}
