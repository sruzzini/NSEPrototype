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
    public String section_current;
    public String block;
    public int authority;
    public double speed;
    public String section_destination;
    public String destination; 
    
    public TrainsClass()
    {
        block = "";
        authority = 0;
        speed = 0;
        destination = "";
        section_destination = "";
    }
    
    public TrainsClass(LineColor l, String s, String b, int a,double sp, String d, String sd)
    {
        line = l;
        block = b;
        section_current = s;
        authority = a;
        speed = sp;
        destination = d;     
        section_destination = sd;
        
    }
    
    public String lineColor()
    {
        if(this.line == LineColor.RED)
        {
            return "RED";
        }
        else
        {
            return "GREEN";
        }        
    }
}
