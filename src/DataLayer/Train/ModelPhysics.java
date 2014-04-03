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
public class ModelPhysics {
    
    // Consumed by PhysicsEngine
    public double motorPower;
    public boolean sBrakeStatus;
    public boolean eBrakeStatus;
    public int time_multiplier;
    public int passengerChange;
    public double gradient;
    
    // Produced by PhysicsEngine
    public boolean engineFailure;
    public boolean signalFailure;
    public boolean  sBrakeFailure;
    public boolean  eBrakeFailure;
    public double delta_x;
    public double velocity;
    public double mass;
    
    public ModelPhysics()
    {
        motorPower = 0;
        sBrakeStatus = false;
        eBrakeStatus = false;
        time_multiplier = 1;
        passengerChange = 0;
        gradient = 0;
        
        engineFailure = false;
        signalFailure = false;
        sBrakeFailure = false;
        eBrakeFailure = false;
        gradient = 0;
        delta_x = 0;
        velocity = 0;
        mass = 0;
    }
    
}
