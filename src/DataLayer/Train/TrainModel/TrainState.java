/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Train.TrainModel;
import DataLayer.Train.StateInput;
import java.util.Calendar;
import java.util.Random;

/**
 *
 * @author drewwinfield
 */
public class TrainState {
    
    private String announcement;
    private String[] actions = {"Buy", "Sell"};
    private String[] things = {"Cars", "Stocks", "Houses", "Cows"};
    private String advertisement;
    private long adTimer;
    private long adInterval = 30 * 1000;
    private boolean extLights;
    private boolean heater;
    private boolean leftDoors;
    private boolean intLights;
    private boolean rightDoors;
    private StateInput stateInput;
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
        advertisement = "";
        adTimer = 0;
        generateAd();
    }
    
    // unless we have a train state thread doing the updating, pretty sure 
    // we'll need the train to update these values
    public TrainState(StateInput ms)
    {
        stateInput = ms;
    }
    public void generateAd()
    {
        String newAd;
        Random r = new Random(Calendar.getInstance().getTimeInMillis());
        if (Calendar.getInstance().getTimeInMillis() > adTimer + adInterval)
        {
            newAd = actions[r.nextInt(actions.length)] + " " + things[r.nextInt(things.length)] + " Now!";
            setAdvertisement(newAd);
            adTimer = Calendar.getInstance().getTimeInMillis();
        }
    
    }
    public String getAdvertisement()
    {
        generateAd();
        return advertisement;
    }
    public String getAnnouncement()
    {
        return announcement;
    }
    public boolean getExteriorLights()
    {
        return extLights;
    }
    public boolean getHeater()
    {
        return heater;
    }
    public boolean getInteriorLights()
    {
        return intLights;
    }
    public boolean getLeftDoors()
    {
        return leftDoors;
    }
    public boolean getRightDoors()
    {
        return rightDoors;
    }
    public int getTemperature()
    {
        return temperature;
    }
    
    public void setAnnouncement(String newAnnouncement)
    {
        announcement = newAnnouncement;
    }  
    public void setAdvertisement(String newAdvertisement)
    {
        advertisement = newAdvertisement;
    }
    public void setExteriorLights(boolean state)
    {
        extLights = state;
    }
    public void setHeater(boolean state)
    {
        heater = state;
    }
    public void setInteriorLights(boolean state)
    {
        intLights = state;
    }
    public void setLeftDoors(boolean state)
    {
        leftDoors = state;
    }
    // right doors
    public void setRightDoors(boolean state)
    {
        rightDoors = state;
    }

    public void setTemperature(int newTemp)
    {
        temperature = newTemp;
    }    
    
    public void updateState()
    {
        rightDoors = stateInput.RightDoors;
        leftDoors = stateInput.LeftDoors;
        intLights = stateInput.IntLights;
        extLights = stateInput.ExtLights;
        announcement = stateInput.Announcement;
        heater = stateInput.Heater;
        stateInput.Advertisement = advertisement; 
        stateInput.Temperature = temperature;
    }
}
