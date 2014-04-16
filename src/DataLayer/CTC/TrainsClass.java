/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.CTC;
import DataLayer.EnumTypes.LineColor;

/**
 *
 * @author micha_000
 */
public class TrainsClass 
{
    
    public LineColor line;

    public int Authority;
    public int StopIndex;
    public int PreviousBlock;
    public boolean Idle;
    public double DistanceSoFar;
    
    public String BlockCurrent;    
    public String SectionCurrent;
    public String BlockDestination; 
    public String SectionDestination;
    
    public String StationCurrent;
    public String StationDestination;
        
    public TrainsClass()
    {
        this.BlockCurrent = "";
        this.StopIndex = 0;
        this.Authority = 0;
        this.DistanceSoFar = 0;
        this.BlockDestination = "";
        this.SectionDestination = "";
    }
    
    public TrainsClass(LineColor l, String s, String b, int a, String d, String sd)
    {
        this.line = l;
        this.BlockCurrent = b;
        this.SectionCurrent = s;
        this.Authority = a;
        this.BlockDestination = d;     
        this.SectionDestination = sd;                
    }
    
    public void setStopIndex(int index)
    {
        this.StopIndex = index;
    }
    
    public void setDistanceSoFar(double distance)
    {
        this.DistanceSoFar = distance;
    }
    
    public void setCurrentStation(String station)
    {
        this.StationCurrent = station;
    }
    
    public void setDestinationStation(String station)
    {
        this.StationDestination = station;
    }
    
    public void setPreviousBlock(int block)
    {
        this.PreviousBlock = block;
    }
    
    public void setIdle(boolean Idle)
    {
        this.Idle = Idle;
    }
    public String lineColor()
    {
        if(this.line == LineColor.RED)
        {
            return "RED";
        }
        else
        {
            if(this.line == LineColor.GREEN)
            {
                return "GREEN";
            }
            else
            {
                return "Yard";
            }
        }        
    }
}
