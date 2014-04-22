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
    public LineColor Line; // the Line the train is on
    public int CurrentBlock; // the current block the train is on
    public int PrevBlock; // the block the train was on before the current block
    public double DistanceSoFar; // distance into the current block the train has traveled
    
    // setStartLocation(int lineNum) places the train on the first block of the 
    // specified Line when dispatched from the yard
    // Parameters:
    //     int lineNum - the identifier for the Line the train should be dispatched to
    public void setStartLocation(int lineNum)
    {
        PrevBlock = 0;
        DistanceSoFar = 0.0;
        if (lineNum == 0) // green
        {
            Line = LineColor.GREEN;
            CurrentBlock = 152;
        }
        else if (lineNum == 1) // red
        {
            Line = LineColor.RED;
            CurrentBlock = 77;
        }
    }
    
    // TrainLocation() creates a new instance of a train location object
    public TrainLocation()
    {
        this.Line = LineColor.YARD;
        this.CurrentBlock = 0;
        this.DistanceSoFar = 0.0;
    }
    
    // updateLocation(double deltaX, double length, int prev, int next, int switchAlternate) updates
    // the current location of the train on the track
    // Parameters:
    //     double deltaX - the distance the train has traveled since the last update
    //     double length - the length of the current block the train is on
    //     int prev - the previous block to the current block the train is on
    //     int next - the next block to the current block the train is on
    //     int switchAlternate - used for resolving discrepancies caused by switch positions in the previous block
    public void updateLocation(double deltaX, double length, int prev, int next, int switchAlternate)
    {
        DistanceSoFar = deltaX + DistanceSoFar;
        if(DistanceSoFar > length)
        {
            DistanceSoFar -= length;
            if(prev == PrevBlock || prev == switchAlternate)
            {
                PrevBlock = CurrentBlock;
                CurrentBlock = next;
            }
            else if(next == PrevBlock || next == switchAlternate)
            {
                PrevBlock = CurrentBlock;
                CurrentBlock = prev;
            }
        }
    }
}
