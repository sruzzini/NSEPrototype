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
