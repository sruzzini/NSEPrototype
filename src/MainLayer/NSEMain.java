/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package MainLayer;

import DataLayer.*;
import GUILayer.*;

/**
 *
 * @author domino54
 */
public class NSEMain 
{
    public static void main(String args[]) 
    {
        NSEFrame gui = new NSEFrame();
        NSE nse = new NSE(1, 10);
        
        nse.RunAutomatic();
        
    }
}
