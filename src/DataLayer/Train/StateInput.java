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
    
    public String Advertisement;
    public String Announcement;
    public boolean ExtLights;
    public boolean Heater;
    public boolean IntLights;
    public boolean LeftDoors;
    public boolean RightDoors;
    public int Temperature;

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
