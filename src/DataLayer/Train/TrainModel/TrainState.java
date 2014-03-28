/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Train.TrainModel;

/**
 *
 * @author drewwinfield
 */
public class TrainState {
    
    private boolean rightDoors;
    private boolean leftDoors;
    private boolean intLights;
    private boolean extLights;
    private String announcement;
    private boolean heater;
    private int temperature;
    
    public TrainState()
    {
        rightDoors = false;
        leftDoors = false;
        intLights = false;
        extLights = false;
        announcement = "";
        heater = false;
        temperature = 65;
    }
    
    // right doors
    public void setRightDoors(boolean state)
    {
        rightDoors = state;
    }
    public boolean getRightDoors()
    {
        return rightDoors;
    }
    // left doors
    public void setLeftDoors(boolean state)
    {
        leftDoors = state;
    }
    public boolean getLeftDoors()
    {
        return leftDoors;
    }
    // interior lights
    public void setInteriorLights(boolean state)
    {
        intLights = state;
    }
    public boolean getInteriorLights()
    {
        return intLights;
    }
    // exterior lights
    public void setExteriorLights(boolean state)
    {
        extLights = state;
    }
    public boolean getExteriorLights()
    {
        return extLights;
    }
    // announcement
    public void setAnnouncement(String newAnnouncement)
    {
        announcement = newAnnouncement;
    }
    public String getAnnouncement()
    {
        return announcement;
    }
    // heater
    public void setHeater(boolean state)
    {
        heater = state;
    }
    public boolean getHeater()
    {
        return heater;
    }
    // temperature
    public void setTemperature(int newTemp)
    {
        temperature = newTemp;
    }
    public int getTemperature()
    {
        return temperature;
    }
    
}
