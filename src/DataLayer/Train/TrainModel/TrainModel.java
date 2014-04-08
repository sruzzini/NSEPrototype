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
    private final PhysicsInput physicsInput;
    public final TrainState state;
    public final PhysicsEngine physics;
    
    public TrainModel(PhysicsInput pi, StateInput si)
    {
        physicsInput = pi;
        stateInput = si;
        state = new TrainState(stateInput);
        physics = new PhysicsEngine(physicsInput);
    }
    
    public void updateState()
    {
        state.updateState();
    }
    
    public void startPhysics()
    {
        new Thread(physics).start();
    }
    
    public boolean getSBrakeStatus()
    {
        return physics.getServiceBrake();
    }
    
    public boolean getEBrakeStatus()
    {
        return physics.getEmergencyBrake();
    }
    
    public boolean getPassengerEBrakeStatus()
    {
        return physics.getPassengerEBrakeRequest();
    }
    
    public double getMass()
    {
        return physics.getMass();
    }
    
    public int getFailureCode()
    {
        return physics.getFailureCode();
    }
    
    public boolean getRightDoorStatus()
    {
        return state.getRightDoors();
    }
        
    public boolean getLeftDoorStatus()
    {
        return state.getLeftDoors();
    }
    
    public boolean getIntLightStatus()
    {
        return state.getInteriorLights();
    }

    public boolean getExtLightStatus()
    {
        return state.getExteriorLights();
    }
    
    public boolean getHeaterStatus()
    {
        return state.getHeater();
    }
    
    public String getAnnouncement()
    {
        return state.getAnnouncement();
    }
    
    public String getAdvertisement()
    {
        return state.getAdvertisement();
    }
}
