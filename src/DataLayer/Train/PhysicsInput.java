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
    
    public double Delta_x;
    public boolean  EBrakeFailure;
    public boolean EBrakeStatus;
    public boolean EngineFailure;
    public double Gradient;
    public double Mass;
    public double MotorPower;
    public int PassengerChange;
    public boolean  SBrakeFailure;
    public boolean SBrakeStatus;
    public boolean SignalFailure;
    public int Time_multiplier;
    public double Velocity;
    
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
