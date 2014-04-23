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
    public double DistanceSoFar;
    
    public int BlockCurrent;    
    public int BlockDestination; 
    public String SectionCurrent;    
    public String SectionDestination;
    
    public String StationCurrent;
    public String StationDestination;
        
    public TrainsClass()
    {
        this.StopIndex = 0;
        this.Authority = 0;
        
        this.BlockCurrent = 0;
        this.BlockDestination = 0;       

        this.DistanceSoFar = 0.0;
        
        this.SectionCurrent = "";
        this.SectionDestination = "";
    }
    
    /*
        Set the Line Color, current block and section, and destination block and section
    */
    
    public TrainsClass(LineColor l, String s, int b, int a, String sd, int d)
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
