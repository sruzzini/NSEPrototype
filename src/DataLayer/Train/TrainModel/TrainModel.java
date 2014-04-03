/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Train.TrainModel;
import DataLayer.Train.ModelState;
import DataLayer.Train.ModelPhysics;

/**
 *
 * @author drewwinfield
 */



public class TrainModel {
        
    private final ModelState modelState;
    private final ModelPhysics modelPhysics;
    public final TrainState state;
    public final PhysicsEngine physics;
    
    public TrainModel(ModelPhysics mp, ModelState ms)
    {
        modelPhysics = mp;
        modelState = ms;
        state = new TrainState(modelState);
        physics = new PhysicsEngine(modelPhysics);
    }
}
