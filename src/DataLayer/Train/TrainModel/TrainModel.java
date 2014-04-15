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
    
    public String getAnnouncement()
    {
        return state.getAnnouncement();
    }
    public String getAdvertisement()
    {
        return state.getAdvertisement();
    }
    public boolean getEBrakeStatus()
    {
        return physics.getEmergencyBrake();
    }
    public boolean getExtLightStatus()
    {
        return state.getExteriorLights();
    }
    public int getFailureCode()
    {
        return physics.getFailureCode();
    }
    public boolean getHeaterStatus()
    {
        return state.getHeater();
    }
    public boolean getIntLightStatus()
    {
        return state.getInteriorLights();
    }
    public boolean getLeftDoorStatus()
    {
        return state.getLeftDoors();
    }
    public double getMass()
    {
        return physics.getMass();
    }
    public boolean getPassengerEBrakeStatus()
    {
        return physics.getPassengerEBrakeRequest();
    }
    public boolean getRightDoorStatus()
    {
        return state.getRightDoors();
    }
    public boolean getSBrakeStatus()
    {
        return physics.getServiceBrake();
    }
    public void startPhysics()
    {
        new Thread(physics).start();
    }
    public void updateState()
    {
        state.updateState();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
        
    
    
    

    
    
    
    
    
    
    
}
