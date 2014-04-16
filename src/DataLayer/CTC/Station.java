/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.CTC;

/**
 *
 * @author micha_000
 */
public class Station 
{
    public final int AUTHORITY;
    public final int BLOCKID;    
    public final int NEXTBLOCKID;
    public final String NEXTSTATION;
    public final String STATION;    
    
    public Station(int a , int b, int nb, String s, String ns)
    {
        this.AUTHORITY = a;
        this.BLOCKID = b;
        this.NEXTBLOCKID = nb; 
        this.NEXTSTATION = ns;
        this.STATION = s;
    }
    
}
