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
public class ModelState {
    private boolean rightDoors;
    private boolean leftDoors;
    private boolean intLights;
    private boolean extLights;
    public int temperature;
    private boolean heater;
    private String announcement;
    private String advertisement;
    
    public ModelState()
    {
        rightDoors = false;
        leftDoors = false;
        intLights = false;
        extLights = false;
        temperature = 65;
        heater = false;
        announcement = "";
        advertisement = "";            
    }
}
