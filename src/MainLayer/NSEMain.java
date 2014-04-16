/******************************************************************************
 * 
 * NSEMain class
 * 
 * Developed by AJility
 * April 2014
 * 
 * Contributers:
 *  Nathan Hachten
 *  Michael Kudlaty
 *  Ryan Mertz
 *  Stephen Ruzzini
 *  Drew Winfield
 *
 *****************************************************************************/

package MainLayer;

import DataLayer.*;
import GUILayer.*;

public class NSEMain
{
    public static void main(String args[]) 
    {
        NSEFrame gui = new NSEFrame();
        gui.setVisible(true);
        NSE nse = new NSE(1, 10);
        nse.SetGUI(gui);
        gui.SetNSE(nse);
        gui.UpdateTrainSelectList();
        gui.SetTrackModel(nse.Track);
        gui.SetWayside(nse.Wayside);
        new Thread(gui).start();
    }
}
