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

    public int authority;
    public int StopIndex;
    public double DistanceSoFar;
    public boolean Idle;
    public String block;
    public String destination; 
    public String section_current;    
    public String section_destination;
    
    
    
    public TrainsClass()
    {
        this.block = "";
        this.StopIndex = 0;
        this.authority = 0;
        this.DistanceSoFar = 0;
        this.destination = "";
        this.section_destination = "";
    }
    
    public TrainsClass(LineColor l, String s, String b, int a, String d, String sd)
    {
        this.line = l;
        this.block = b;
        this.section_current = s;
        this.authority = a;
        this.destination = d;     
        this.section_destination = sd;        
        
    }
    
    public void setStopIndex(int index)
    {
        this.StopIndex = index;
    }
    
    public void setDistanceSoFar(double distance)
    {
        this.DistanceSoFar = distance;
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
