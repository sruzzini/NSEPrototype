/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Train.TrainModel;
import DataLayer.Train.StateInput;
import DataLayer.Train.PhysicsInput;

/**
 *
 * @author drewwinfield
 */



public class TrainModel {
        
    private final StateInput stateInput;
    public final TrainState state;
    public final PhysicsEngine physics;
    private final PhysicsInput physicsInput;
    
    public TrainModel(PhysicsInput pi, StateInput si)
    {
        physicsInput = pi;
        stateInput = si;
        state = new TrainState(stateInput);
        physics = new PhysicsEngine(physicsInput);
    }
    
     /*
    getAdvertisement() returns advertisement
    Returns - String
    */
    public String getAdvertisement()
    {
        return state.getAdvertisement();
    }
    /*
    getAnnouncement() returns announcement
    Returns - String
    */
    public String getAnnouncement()
    {
        return state.getAnnouncement();
    }
    /*
    getEmergencyBrake() returns the status of the emergency brake
    Returns - boolean
    */
    public boolean getEBrakeStatus()
    {
        return physics.getEmergencyBrake();
    }
    /*
    getExteriorLights() returns extLights
    Returns - boolean
    */
    public boolean getExtLightStatus()
    {
        return state.getExteriorLights();
    }
    /*
    getFailureCode() returns the value of the failure code
    Returns - int
    */
    public int getFailureCode()
    {
        return physics.getFailureCode();
    }
    /*
    getHeater() returns heater
    Returns - boolean
    */
    public boolean getHeaterStatus()
    {
        return state.getHeater();
    }
    /*
    getInteriorLights() returns intLights
    Returns - boolean
    */
    public boolean getIntLightStatus()
    {
        return state.getInteriorLights();
    }
    /*
    getLeftDoors() returns leftDoors
    Returns - boolean
    */
    public boolean getLeftDoorStatus()
    {
        return state.getLeftDoors();
    }
    /*
    getMass() returns the train's mass
    Returns - double
    */
    public double getMass()
    {
        return physics.getMass();
    }
    /*
    getPassengerEBrakeStatus() returns the state of the E brake request
    Returns - boolean
    */
    public boolean getPassengerEBrakeStatus()
    {
        return physics.getPassengerEBrakeRequest();
    }
    /*
    getRightDoors() returns rightDoors
    Returns - boolean
    */
    public boolean getRightDoorStatus()
    {
        return state.getRightDoors();
    }
    /*
    getServiceBrakeStatus() returns sBrakeStatus
    Returns - boolean
    */
    public boolean getSBrakeStatus()
    {
        return physics.getServiceBrake();
    }
    /*
    getTemperature() returns temperature
    Returns - boolean
    */
    public int getTemperature()
    {
        return state.getTemperature();
    }
    /*
    startPhysics() spawns a new thread and begins the physics.run() method
    */
    public void startPhysics()
    {
        new Thread(physics).start();
    }
    
    /*
    updateState() sets the TrainState object's values to it's stateInput object's
    values and also updates the adverstisement and temp in it's stateInput object
    */
    public void updateState()
    {
        state.updateState();
    }
}
