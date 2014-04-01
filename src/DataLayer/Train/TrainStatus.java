/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Train;

/**
 *
 * @author domino54
 */
public class TrainStatus 
{
    //Failures
    public static final int ENGINE_FAILURE = 1;
    public static final int SIGNAL_PICKUP_FAILURE = 3;
    public static final int E_BRAKE_FAILURE = 7;
    public static final int S_BRAKE_FAILURE = 13;
    
    private double velocity; // meters/sec
    private double mass; // metric tons
    private double temperature; // degrees C
    private int failure;
    private boolean sBrakeStatus;
    private boolean eBrakeStatus;
    private boolean passengerEBrakeRequest;
    private boolean lDoorStatus;
    private boolean rDoorStatus;
    private boolean extLightStatus;
    private boolean intLightStatus;
    private boolean heaterStatus;
}
