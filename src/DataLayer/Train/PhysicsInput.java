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
public class PhysicsInput {
    
    // Consumed by PhysicsEngine
    public double MotorPower;
    public boolean SBrakeStatus;
    public boolean EBrakeStatus;
    public int Time_multiplier;
    public int PassengerChange;
    public double Gradient;
    
    // Produced by PhysicsEngine
    public boolean EngineFailure;
    public boolean SignalFailure;
    public boolean  SBrakeFailure;
    public boolean  EBrakeFailure;
    public double Delta_x;
    public double Velocity;
    public double Mass;
    
    public PhysicsInput()
    {
        MotorPower = 0;
        SBrakeStatus = false;
        EBrakeStatus = false;
        Time_multiplier = 1;
        PassengerChange = 0;
        Gradient = 0;
        
        EngineFailure = false;
        SignalFailure = false;
        SBrakeFailure = false;
        EBrakeFailure = false;
        Gradient = 0;
        Delta_x = 0;
        Velocity = 0;
        Mass = 0;
    }
    
}
