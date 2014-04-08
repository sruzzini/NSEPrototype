/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Train.TrainModel;
import DataLayer.Train.StateInput;

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
    private StateInput stateInput;
    private String advertisement;
    
    public TrainState()
    {
        rightDoors = false;
        leftDoors = false;
        intLights = false;
        extLights = false;
        announcement = "";
        heater = false;
        temperature = 65;
        advertisement = "";
        
    }
    
    // unless we have a train state thread doing the updating, pretty sure 
    // we'll need the train to update these values
    public TrainState(StateInput ms)
    {
        stateInput = ms;
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
    // advertisement
    public void setAdvertisement(String newAdvertisement)
    {
        advertisement = newAdvertisement;
    }
    public String getAdvertisement()
    {
        return advertisement;
    }
    
    public void updateState()
    {
        rightDoors = stateInput.RightDoors;
        leftDoors = stateInput.LeftDoors;
        intLights = stateInput.IntLights;
        extLights = stateInput.ExtLights;
        announcement = stateInput.Announcement;
        heater = stateInput.Heater;
        advertisement = stateInput.Advertisement; 
        stateInput.Temperature = temperature;
    }
}
