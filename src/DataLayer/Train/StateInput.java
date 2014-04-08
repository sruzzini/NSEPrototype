/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Train;

/**
 *
 * @author drewwinfield
 */
public class StateInput {
    public boolean RightDoors;
    public boolean LeftDoors;
    public boolean IntLights;
    public boolean ExtLights;
    public int Temperature;
    public boolean Heater;
    public String Announcement;
    public String Advertisement;
    
    public StateInput()
    {
        RightDoors = false;
        LeftDoors = false;
        IntLights = false;
        ExtLights = false;
        Temperature = 65;
        Heater = false;
        Announcement = "";
        Advertisement = "";            
    }
}
