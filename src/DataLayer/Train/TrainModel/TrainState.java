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
    /*
    generateAd() generates a new advertisement every adInterval number of 
    milliseconds
    */
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
    /*
    getAdvertisement() returns advertisement
    Returns - String
    */
    public String getAdvertisement()
    {
        generateAd();
        return advertisement;
    }
    /*
    getAnnouncement() returns announcement
    Returns - String
    */
    public String getAnnouncement()
    {
        return announcement;
    }
    /*
    getExteriorLights() returns extLights
    Returns - boolean
    */
    public boolean getExteriorLights()
    {
        return extLights;
    }
    /*
    getHeater() returns heater
    Returns - boolean
    */
    public boolean getHeater()
    {
        return heater;
    }
    /*
    getInteriorLights() returns intLights
    Returns - boolean
    */
    public boolean getInteriorLights()
    {
        return intLights;
    }
    /*
    getLeftDoors() returns leftDoors
    Returns - boolean
    */
    public boolean getLeftDoors()
    {
        return leftDoors;
    }
    /*
    getRightDoors() returns rightDoors
    Returns - boolean
    */
    public boolean getRightDoors()
    {
        return rightDoors;
    }
    /*
    getTemperature() returns temperature
    Returns - boolean
    */
    public int getTemperature()
    {
        return temperature;
    }
    /*
    setAnnouncement(String newAdvertisement) sets newAdvertisement
    Parameters:
        String
    */
    public void setAdvertisement(String newAdvertisement)
    {
        advertisement = newAdvertisement;
    }
    /*
    setAnnouncement(String newAnnouncement) sets announcement
    Parameters:
        String
    */
    public void setAnnouncement(String newAnnouncement)
    {
        announcement = newAnnouncement;
    }  
    /*
    setExteriorLights(boolean newAnnouncement) sets extLights
    Parameters:
        boolean
    */
    public void setExteriorLights(boolean state)
    {
        extLights = state;
    }
    /*
    setHeater(boolean state) sets heater
    Parameters:
        boolean
    */
    public void setHeater(boolean state)
    {
        heater = state;
    }
    /*
    setInteriorLights(boolean state) sets intLights
    Parameters:
        boolean
    */
    public void setInteriorLights(boolean state)
    {
        intLights = state;
    }
    /*
    setLeftDoors(boolean state) sets leftDoors
    Parameters:
        boolean
    */
    public void setLeftDoors(boolean state)
    {
        leftDoors = state;
    }
    /*
    setRightDoors(boolean state) sets rightDoors
    Parameters:
        boolean
    */
    
    public void setRightDoors(boolean state)
    {
        rightDoors = state;
    }
    /*
    setTemperature(boolean state) sets temperature
    Parameters:
        int
    */
    public void setTemperature(int newTemp)
    {
        temperature = newTemp;
    }  
    /*
    updateState() sets the TrainState object's values to it's stateInput object's
    values and also updates the adverstisement and temp in it's stateInput object
    */
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
