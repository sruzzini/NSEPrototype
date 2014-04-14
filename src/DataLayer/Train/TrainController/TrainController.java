/******************************************************************************
 * 
 * TrainController class
 * 
 * Developed by AJility
 * April 2014
 * 
 * Contributers:
 *  Stephen T. Ruzzini
 *
 *****************************************************************************/

package DataLayer.Train.TrainController;
import DataLayer.TrackModel.TrackSignal;
import DataLayer.TrackModel.BeaconSignal;
import DataLayer.Train.*;
import DataLayer.Wayside.*;


//Train Controller Class
public class TrainController 
{
    //Constants
    private static final double INTEGRAL_GAIN = 27; //Ki
    public static final double MAX_TRAIN_MOTOR_POWER = 120000; //120 KW
    public static final double MAX_TRAIN_SPEED = 19.444; //70 km/hr in m/s
    public static final double MILLISECONDS_MINUTE = 60000; //number of milliseconds in a minute
    private static final double MIN_VERROR_FOR_S_BRAKE = -.89408; //-5 mph
    private static final double PROPORTIONAL_GAIN = 100000; //Kp
    public static final double ROOM_TEMP = 21.1; //70 degrees fahrenheit, in celsius
    public static final double SERVICE_BRAKE_FORCE = 61718.7; //force of the service brake
    public static final double STANDARD_SAMPLE_PERIOD = 0.1; //sample period of the train controller
    
    //Enumerated types
    public enum OperatorInputStatus //status of a user input
    {
        AUTO,  //if AUTO all decisions will be made by the controller
        OFF,  //if OFF, will try to turn off if safe to do so
        ON  //if ON, will try to turn on if safe to do so
    }
    
    //Class variables
    public double DesiredTemperature; //desired train temperature (celcius)
    public OperatorInputStatus OperatorEBrake; //emergency brake request status
    public OperatorInputStatus OperatorExtLights; //exteior light request status
    public OperatorInputStatus OperatorIntLights; //interior light request status
    public OperatorInputStatus OperatorLeftDoor; //left door request status
    public OperatorInputStatus OperatorRightDoor; //right door request status
    public OperatorInputStatus OperatorSBrake; //service brake request status
    public double VelocitySetPoint; //Velcotiy set poitn (meters/sec)
    private long beaconSignalReceived; //time the last beacon signal was received
    private boolean engagingStop; //boolean true if sBrake is on going into a station
    private final int iD; //id of the controller
    private BeaconSignal lastBeacon; //the last beacon passed
    private double lastIntermediary; //last intermediary value in power calculation
    private double lastVelocityError; //last veloicty error in power calculation
    private boolean preparingStop; //boolean true if passed a beacon before station to sop at
    private double samplePeriod; //sample period to calculate power
    private double stopBrakeEngageDelay; //time delay from passing a beacon to engaging the service brake
    private boolean stoppedAtStation; //boolean true if train is stopped at a station
    private long stoppedAtStationTime; //time the train first stops at a station
    private long time; //current time
    private int timeMultiplier; //multiplier for time representaiton
    private TrackSignal trackSignal; //TrackSignal used ot calculate outputs
    private TrainStatus trainStatus; //TrainStatus used to calculate outputs
    
    
    //Contructors 
    public TrainController()
    {
        this.iD = 0;
        this.engagingStop = false;
        this.preparingStop = false;
        this.stoppedAtStation = false;
        this.timeMultiplier = 1;
        this.beaconSignalReceived = 0;
        this.stoppedAtStationTime = 0;
        this.time = System.currentTimeMillis();
        this.stopBrakeEngageDelay = 0;
        this.samplePeriod = TrainController.STANDARD_SAMPLE_PERIOD;
        this.trainStatus = new TrainStatus();
        this.trackSignal = new TrackSignal();
        this.lastBeacon = null;
        this.lastIntermediary = 0;
        this.lastVelocityError = 0;
        this.VelocitySetPoint = 0;
        this.OperatorEBrake = OperatorInputStatus.AUTO;
        this.OperatorSBrake = OperatorInputStatus.AUTO;
        this.OperatorExtLights = OperatorInputStatus.AUTO;
        this.OperatorIntLights = OperatorInputStatus.AUTO;
        this.OperatorLeftDoor = OperatorInputStatus.AUTO;
        this.OperatorRightDoor = OperatorInputStatus.AUTO;
        this.DesiredTemperature = TrainController.ROOM_TEMP;
    }
    
    public TrainController(int multiplier, TrainStatus status, TrackSignal signal, BeaconSignal beacon)
    {
        this.iD = 0;
        this.engagingStop = false;
        this.preparingStop = false;
        this.stoppedAtStation = false;
        this.timeMultiplier = multiplier;
        this.beaconSignalReceived = 0;
        this.time = System.currentTimeMillis();
        this.stopBrakeEngageDelay = 0;
        this.stoppedAtStationTime = 0;
        this.samplePeriod = TrainController.STANDARD_SAMPLE_PERIOD;
        this.trainStatus = status;
        this.trackSignal = signal;
        this.lastBeacon = beacon;
        this.lastIntermediary = 0;
        this.lastVelocityError = 0;
        this.VelocitySetPoint = 0;
        this.OperatorEBrake = OperatorInputStatus.AUTO;
        this.OperatorSBrake = OperatorInputStatus.AUTO;
        this.OperatorExtLights = OperatorInputStatus.AUTO;
        this.OperatorIntLights = OperatorInputStatus.AUTO;
        this.OperatorLeftDoor = OperatorInputStatus.AUTO;
        this.OperatorRightDoor = OperatorInputStatus.AUTO;
        this.DesiredTemperature = TrainController.ROOM_TEMP;
    }
    
    public TrainController(int id, int multiplier, double period, TrainStatus t, TrackSignal s, double velocity,
                           OperatorInputStatus ebrake, OperatorInputStatus sbrake, OperatorInputStatus elights, 
                           OperatorInputStatus ilights, OperatorInputStatus ldoor, OperatorInputStatus rdoor, double temp)
    {
        this.iD = id;
        this.engagingStop = false;
        this.preparingStop = false;
        this.stoppedAtStation = false;
        this.timeMultiplier = multiplier;
        this.beaconSignalReceived = 0;
        this.time = System.currentTimeMillis();
        this.stopBrakeEngageDelay = 0;
        this.stoppedAtStationTime = 0;
        this.samplePeriod = period;
        this.trainStatus = t;
        this.trackSignal = s;
        this.lastBeacon = null;
        this.lastIntermediary = 0;
        this.lastVelocityError = 0;
        this.VelocitySetPoint = velocity;
        this.OperatorEBrake = ebrake;
        this.OperatorSBrake = sbrake;
        this.OperatorExtLights = elights;
        this.OperatorIntLights = ilights;
        this.OperatorLeftDoor = ldoor;
        this.OperatorRightDoor = rdoor;
        this.DesiredTemperature = temp;
    }
    
    
    
    public TrackSignal getTrackSignal()
    {
        return this.trackSignal;
    }
    
    
    
    /* GetTrainCommand() gets safe command to send to a train
     * Returns - TrainCommand
    */
    public TrainCommand GetTrainCommand()
    {
        TrainCommand toReturn;
        TrainCommand[] safetyArray = new TrainCommand[3];
        
        for(int i = 0; i < 3; i++)
        {
            safetyArray[i] = calculateTrainCommand();
        }
        
        int chosenIndex = chooseSafeCommand(safetyArray[0], safetyArray[1], safetyArray[2]);
        
        if (chosenIndex != -1) //a bundle was chosen
        {
            toReturn = safetyArray[chosenIndex];
        }
        else //no bundle was chosen, so make a super safe "emergency" bundle
        {
            toReturn = new TrainCommand(0, false, false, false, false, this.trainStatus.GetExteriorLightStatus(), 
                                                   this.trainStatus.GetInteriorLightStatus(), this.trainStatus.GetHeaterStatus(),
                                                   this.trackSignal.getNextDestination());
            int trainFailure = this.trainStatus.GetFailure();
            if (hasSBrakeFailure(trainFailure) && hasEBrakeFailure(trainFailure)) //if both brakes are failed
            {
                toReturn.EmergencyBrakeOn = false;
                toReturn.ServiceBrakeOn = false;
            }
            else if (hasEBrakeFailure(trainFailure)) //if just the ebrake failed
            {
                toReturn.ServiceBrakeOn = true;
            }
            else
            {
                toReturn.EmergencyBrakeOn = true; //ebrake did not fail
            }
        }
        return toReturn;
    }
    
    /* ReadTrainToControllerBundle(TrainStatustrainS, TrackSignal trackS, BeaconSignal beacon) reads
     *   bundle and sets appropriate inner signals
     * Paramenters:
     *    TrainStatus trainS - status of the train
     *    TrackSignal trackS - signal from the track block
     *    BeaconSignal beacon - beacon signal (only sent if passing a beacon)
    */
    public void ReadTrainToControllerBundle(TrainStatus trainS, TrackSignal trackS, BeaconSignal beacon)
    {
        this.trainStatus = trainS;
        this.trackSignal = trackS;
        if (beacon != null)
        {
            this.lastBeacon = beacon;
        }
    }
    
    /* SetBeaconSignal(BeaconSignal s) set the controller's beaocn signal and begins calculating stop
     *   information if necessary
     * Parameters:
     *    BeaconSignal s - becaon sent by the track block
    */
    public void SetBeaconSignal(BeaconSignal s)
    {
        this.lastBeacon = s;
        this.calculateStop();
    }
    
    /* SetTimeMultiplier(int multiplier) sets the time multiplier of the controller
     * Parameters:
     *     int multiplier - multiplier to set
    */
    public void SetTimeMultiplier(int multiplier)
    {
        this.timeMultiplier = multiplier;
    }
    
    /* SetTrackSignal(TrackSignal s) sets the track signal of the controller
     * Paramters:
     *     TrackSignal s - the track signal to set
    */
    public void SetTrackSignal(TrackSignal s)
    {
        this.trackSignal = s;
    }
    
    /* SetTrainStatus(TrainStatus s) sets teh train status of the controller
     * Parameters:
     *     TrainStatus s - the train status to set
    */
    public void SetTrainStatus(TrainStatus s)
    {
        this.trainStatus = s;
    }
    
    
    //Private methods
    /* calculateAnnouncement() calculates the announcement command
     * Returns - String, announcement
    */
    private String calculateAnnouncement()
    {
        String s;
        if (this.stoppedAtStation)
        {
            s = "Stopped at: ";
        }
        else if (this.preparingStop || this.engagingStop)
        {
            s = "Approaching: ";
        }
        else
        {
            s = "Next Stop: ";
        }
        return (s + this.trackSignal.NextDestination);
    }
    
    /* calculateEBrakeCommand(int failure) calculates the emergency brake command
     * Parameters:
     *     int failure - current train failure
     * Returns - boolean, true if brake should be engaged
    */
    private boolean calculateEBrakeCommand(int failure)
    {
        boolean eBrake = false;
        boolean eBrakeFailure = hasEBrakeFailure(failure); //check if their is an e-brake failure
        
        if(((this.OperatorEBrake == OperatorInputStatus.ON) && !eBrakeFailure) || //Operator requests EBrake and no EBrake failure
           (this.trainStatus.GetPassengerBrakeRequest() && !eBrakeFailure) || //Passenger requests EBrake and no EBrake failure
           ((failure != 0) && !eBrakeFailure)) //There is some failure and no EBrake failure
        {
            eBrake = true;
        }
        
        return eBrake;
    }
    
    /* calculateExteriorLights() calculates the exterior lights command
     * Returns - boolean, true if should turn exterior lights on
    */
    private boolean calculateExteriorLights()
    {
        boolean lights = false;
        boolean underground = this.trackSignal.getUndergroundStatus();
        if ((underground && this.OperatorExtLights == OperatorInputStatus.AUTO) || //we're underground and the operator hasn't specified light command
            (this.OperatorExtLights == OperatorInputStatus.ON)) //Operator turns the light on
        {
            lights = true;
        }
        return lights;
    }
    
    /* calculateInteriorLights() calculates the interior lights command
     * Returns - boolean, true if should turn interior lights on
    */
    private boolean calculateInteriorLights()
    {
        boolean lights = false;
        boolean underground = this.trackSignal.getUndergroundStatus();
        boolean doorsOpen = (this.trainStatus.GetLeftDoorStatus() || this.trainStatus.GetRightDoorStatus());
        if ((underground && this.OperatorIntLights == OperatorInputStatus.AUTO) || //we're underground and the operator hasn't specified light command
            (doorsOpen) || //doors are open (keep lights on for passengers entering and exiting
            (this.OperatorIntLights == OperatorInputStatus.ON)) //Operator turns the light on
        {
            lights = true;
        }
        return lights;
    }
    
    /* calculateLeftDoorCommand(double vCurr) calculates the left door command
     * Parameters:
     *     double vCurr - the current velocity of the train
     * Returns - boolean, true if should open the left doors
    */
    private boolean calculateLeftDoorCommand(double vCurr)
    {
        boolean door = false;
        if ((vCurr == 0) && (this.OperatorLeftDoor == OperatorInputStatus.ON) ||  //Train is stopped and conductor has left door input on
            (this.stoppedAtStation && !this.lastBeacon.StationOnRight && (((System.currentTimeMillis() - this.stoppedAtStationTime) * this.timeMultiplier) >= MILLISECONDS_MINUTE))) //stopped and station on the left
        {
            door = true;
        }
        return door;
    }
    
    /* calculateRightDoorCommand(double vCurr) calculates the right door command
     * Parameters:
     *     double vCurr - the current velocity of the train
     * Returns - boolean, true if should open the right doors
    */
    private boolean calculateRightDoorCommand(double vCurr)
    {
        boolean door = false;
        if ((vCurr == 0) && (this.OperatorRightDoor == OperatorInputStatus.ON) || //Train is stopped and conductor has right door input on
            (this.stoppedAtStation && this.lastBeacon.StationOnRight && (((System.currentTimeMillis() - this.stoppedAtStationTime) * this.timeMultiplier) >= MILLISECONDS_MINUTE))) // stopped and station on the right
        {
            door = true;
        }
        return door;
    }
    
    /* calculateServiceBrakeCommand(double vError, double vSafe, int failure) calculates whether to 
     *   engage the service brake or not
     * Paramenters:
     *     double vError - velocity error calculated when determining power
     *     double vSafe - safe commanded velocity
     *     int failure - failure number (0 if no failure)
     * Returns - boolean, true if should engage service brake
    */
    private boolean calculateServiceBrakeCommand(double vError, double vSafe, int failure)
    {
        boolean sBrake = false;
        boolean sBrakeFailure = hasSBrakeFailure(failure); //check if there is an s-brake failure
        boolean eBrakeFailure = hasEBrakeFailure(failure); //check if there is an e-brake failure
        
        if (((this.OperatorSBrake == OperatorInputStatus.ON) && !sBrakeFailure) || //Operator pulls brake an no s-brake failure
            (vError <= TrainController.MIN_VERROR_FOR_S_BRAKE && !sBrakeFailure) || //Verror is under -5mph and no s-brake failure
            (vSafe == 0 && !sBrakeFailure) || //Vcommand safe is 0 and no s-brake failure
            (!sBrakeFailure && (this.trackSignal.getAuthority() == 0)) || //Authority is 0 and no s-brake failure
            (!sBrakeFailure &&  eBrakeFailure) || //There is an e-brake failure and no s-brake failure
            (this.engagingStop && !sBrakeFailure)) //stopping at station and no sBrakeFailure
        {
            sBrake = true;
        }
        
        //check for releasing brake after done stopping
        if (this.stoppedAtStation &&  //stopped at a station
            (((System.currentTimeMillis() - this.stoppedAtStationTime) * this.timeMultiplier) >= MILLISECONDS_MINUTE) && //it's been a minute
            !this.trainStatus.GetRightDoorStatus() && //right doors closed
            !this.trainStatus.GetLeftDoorStatus()) //left doors closed
        {
            sBrake = false;
            this.stoppedAtStation = false;
            this.engagingStop = false;
            this.preparingStop = false;
        }
        
        
        return sBrake;
    }
    
    //calculateStop() checks to see if train should be stopping at station or not
    private void calculateStop()
    {
        //If we get a beacon signal for a station that we want to stop at
        if (this.trackSignal.NextDestination.equals(this.lastBeacon.StationName))
        {
            this.preparingStop = true;
            //if we're at max weight start engaging the brake now
            if ((TrainStatus.MAX_TRAIN_MASS - this.trainStatus.GetMass()) <= .03) 
            {
                this.engagingStop = true;
            }
            else
            {
                this.beaconSignalReceived = System.currentTimeMillis();
                calculateStopBrakeDelay();
            }
        }
    }
    
    // calculateStopBrakeDelay() calculates the delay to engage the service brake when stopping
    private void calculateStopBrakeDelay()
    {
        this.stopBrakeEngageDelay = (((TrainStatus.MAX_TRAIN_MASS / TrainController.SERVICE_BRAKE_FORCE) * Wayside.STOP_SPEED) -
                                     ((this.trainStatus.GetMass() / TrainController.SERVICE_BRAKE_FORCE) * Wayside.STOP_SPEED));
    }
    
    /* calculateTrainCommand() calculates a command ot send to the train
     * Returns - TrainCommand to send to train
    */
    private TrainCommand calculateTrainCommand()
    {
        TrainCommand command = new TrainCommand();
        
        double safeVelocity = 0; //velocity to calculate VError
        double velocityCommand = this.trackSignal.getVelocityCommand(); //vCommand
        double currTrainVelocity = this.trainStatus.GetVelocity(); //curr Velocity
        int trainFailure = this.trainStatus.GetFailure(); //failure
        
        //Choose safe velocity
        if (trainFailure == 0) //as long as there isn't a failure
        {
            if (this.VelocitySetPoint < velocityCommand) //if Vsetpoint is less than Vcommand, that is the safe speed
            {
                safeVelocity = this.VelocitySetPoint;
            } 
            else 
            {
                safeVelocity = velocityCommand;
            }
            if (safeVelocity > TrainController.MAX_TRAIN_SPEED)
            {
                safeVelocity = TrainController.MAX_TRAIN_SPEED;
            }
        }
        
        //check for stopped at station
        if (this.engagingStop && (this.trainStatus.GetVelocity() == 0))
        {
            this.stoppedAtStation = true;
            this.stoppedAtStationTime = System.currentTimeMillis();
        }
        
        //calculate power output
        double currVError = safeVelocity - currTrainVelocity;
        //check for stopping stuff
        if ((this.stoppedAtStation) ||  //if stopped at station and it's been less than 1 minute
            (this.engagingStop) || //engaging stop at station
            (this.preparingStop && (((System.currentTimeMillis() - this.beaconSignalReceived) * this.timeMultiplier) >= this.stopBrakeEngageDelay))) //if about to engage stop at station
        {
            currVError = 0 - currTrainVelocity;
            this.engagingStop = true;
        }
        
        double currIntermediary = (this.lastIntermediary + (((this.samplePeriod * this.timeMultiplier)/2) * (currVError + this.lastVelocityError)));
        double power = (TrainController.PROPORTIONAL_GAIN * currVError) + (TrainController.INTEGRAL_GAIN * currIntermediary);
        if (power > TrainController.MAX_TRAIN_MOTOR_POWER)
        {
            power = TrainController.MAX_TRAIN_MOTOR_POWER;
        }
        else if (power < 0)
        {
            power = 0;
        }
        
        this.lastVelocityError = currVError;
        this.lastIntermediary = currIntermediary;
        
        command.PowerCommand = power; //set power for output bundle
        
        command.EmergencyBrakeOn = this.calculateEBrakeCommand(trainFailure); //set E-Brake status
        
        command.ServiceBrakeOn = calculateServiceBrakeCommand(currVError, safeVelocity, trainFailure); //set the Service-Brake status
        
        //if a brake is engaged or there is a failure or a door is open, cut the power
        if (command.EmergencyBrakeOn ||  //E-Brake On
            command.ServiceBrakeOn ||   //S-Brake On
            (trainFailure != 0) || //There is a failure
            this.trainStatus.GetRightDoorStatus() || //Right Door is open
            this.trainStatus.GetLeftDoorStatus()) //Left Door is open
        {
            command.PowerCommand = 0.0;
        }
        
        command.LeftDoorsOpen = calculateLeftDoorCommand(currTrainVelocity); //set Left Door
        
        command.RightDoorsOpen = calculateRightDoorCommand(currTrainVelocity); //set Right Door
        
        command.InteriorLightsOn = calculateInteriorLights(); //set interior lights
        
        command.ExteriorLightsOn = calculateExteriorLights(); //set exterior lights
        
        command.HeaterOn = (this.trainStatus.GetTemperature() < this.DesiredTemperature); //set heater
        
        command.Announcement = calculateAnnouncement(); //set announcement
        
        return command;
    }
    
    /* checkBundleSafety(TrainCommand command) checks if a command is safe or not
     * Parameters:
     *     TrainCommand command - command to check
     * Returns - boolean, true if safe, false if not
    */
    private boolean checkBundleSafety(TrainCommand command)
    {
        boolean safe = false;
        int trainFailure = this.trainStatus.GetFailure();
        //Power safe
        if (command.PowerCommand <= MAX_TRAIN_MOTOR_POWER && //motor power is not greater than max power
            command.PowerCommand >= 0) //motor power is not negative
        {
            //if power is positive
            if(command.PowerCommand > 0 &&  
               !command.RightDoorsOpen && //right door closed
               !command.LeftDoorsOpen &&  //left door closed
               (this.trackSignal.getAuthority() > 0) && //authority is positive
               (trainFailure == 0)) //no failure
            {
                //check brakes and doors
                if (!command.EmergencyBrakeOn && //e-brake off
                    !command.ServiceBrakeOn) //s-brake off
                {
                    safe = true;
                }
            }
            else if (command.PowerCommand == 0)//power is 0
            {
                if ((this.trainStatus.GetVelocity() > 0) && (!command.RightDoorsOpen && !command.LeftDoorsOpen) || //train moving and doors closed
                    (this.trainStatus.GetVelocity() == 0))//train is stopped
                {
                    if (trainFailure == 0) //no train failure
                    {
                        if (!(command.EmergencyBrakeOn && command.ServiceBrakeOn)) //e-brake and s-brake are both not on
                        {
                            safe = true;
                        }
                    } 
                    else //there is one or more failure(s)
                    {
                        //ebrake and sbrake failure
                        if (hasEBrakeFailure(trainFailure) && hasSBrakeFailure(trainFailure))
                        {
                            if (!command.EmergencyBrakeOn && !command.ServiceBrakeOn)
                            {
                                safe = true;
                            }
                        }
                        //ebrake failure
                        else if (hasEBrakeFailure(trainFailure))
                        {
                            if (!command.EmergencyBrakeOn && command.ServiceBrakeOn)
                            {
                                safe = true;
                            }
                        }//no brake failures
                        else if (command.EmergencyBrakeOn && !command.ServiceBrakeOn)
                        {
                            safe = true;
                        }
                    }
                }
            }
        }
        return safe;
    }
    
    /* chooseSafeCommand(TrainCommand command1, TrainCommand command2, TrainCommand command3) 
     *   chooses the safest command of the 3, else returns -1 if no safe command
     * Parameters:
     *     TrainCommand command1 - command to  check
     *     TrainCommand command2 - command to check
     *     TrainCommand command3 - command to check
     * Returns - int, index of safe command (-1 if none are safe)
    */
    private int chooseSafeCommand(TrainCommand command1, TrainCommand command2, TrainCommand command3)
    {
        int selection = -1;
        double command1Score = -1;
        double command2Score = -1;
        double command3Score = -1;
        
        //give each bundle a score
        if (checkBundleSafety(command1))
        {
            command1Score = scoreBundle(command1);
        }
        if (checkBundleSafety(command2))
        {
            command2Score = scoreBundle(command2);
        }
        if (checkBundleSafety(command3))
        {
            command3Score = scoreBundle(command3);
        }
        /*System.out.println("Bundle 1 score: " + bundle1Score);
        System.out.println("Bundle 2 score: " + bundle2Score);
        System.out.println("Bundle 3 score: " + bundle3Score);*/
        
        //choose highest scorre
        if (command1Score != -1 && command2Score != -1 && command3Score != -1)
        {
            if (command3Score >= command2Score && command3Score >= command1Score)
            {
                selection = 2;
            }
            else if (command2Score > command3Score && command2Score >= command1Score)
            {
                selection = 1;
            }
            else
            {
                selection = 0;
            }
        }
        return selection;
    }
    
    /* hasEBrakeFailure(int failure) checks if there is an emergency brake failure
     * Parameters:
     *     int failure - failure to check
     * Returns - boolean, true if there is a failure, false if not
    */
    private boolean hasEBrakeFailure(int failure)
    {
        return (failure == TrainStatus.E_BRAKE_FAILURE |
                failure == (TrainStatus.E_BRAKE_FAILURE  + TrainStatus.ENGINE_FAILURE) ||
                failure == (TrainStatus.E_BRAKE_FAILURE  + TrainStatus.SIGNAL_PICKUP_FAILURE) ||
                failure == (TrainStatus.E_BRAKE_FAILURE  + TrainStatus.S_BRAKE_FAILURE) ||
                failure == (TrainStatus.E_BRAKE_FAILURE  + TrainStatus.ENGINE_FAILURE + TrainStatus.S_BRAKE_FAILURE) ||
                failure == (TrainStatus.E_BRAKE_FAILURE  + TrainStatus.ENGINE_FAILURE + TrainStatus.SIGNAL_PICKUP_FAILURE) ||
                failure == (TrainStatus.E_BRAKE_FAILURE  + TrainStatus.SIGNAL_PICKUP_FAILURE + TrainStatus.S_BRAKE_FAILURE) ||
                failure == (TrainStatus.E_BRAKE_FAILURE  + TrainStatus.ENGINE_FAILURE + TrainStatus.S_BRAKE_FAILURE + TrainStatus.SIGNAL_PICKUP_FAILURE));
    }
    
    /* hasSbrakeFailure(int failure) checks if there is a service brake failure
     * Parameters:
     *     int failure - failure to check
     * Returns - boolean, true if there is a failure, false if not
    */
    private boolean hasSBrakeFailure(int failure)
    {
        return (failure == TrainStatus.S_BRAKE_FAILURE |
                failure == (TrainStatus.S_BRAKE_FAILURE  + TrainStatus.ENGINE_FAILURE) ||
                failure == (TrainStatus.S_BRAKE_FAILURE  + TrainStatus.SIGNAL_PICKUP_FAILURE) ||
                failure == (TrainStatus.S_BRAKE_FAILURE  + TrainStatus.E_BRAKE_FAILURE) ||
                failure == (TrainStatus.S_BRAKE_FAILURE  + TrainStatus.ENGINE_FAILURE + TrainStatus.E_BRAKE_FAILURE) ||
                failure == (TrainStatus.S_BRAKE_FAILURE  + TrainStatus.ENGINE_FAILURE + TrainStatus.SIGNAL_PICKUP_FAILURE) ||
                failure == (TrainStatus.S_BRAKE_FAILURE  + TrainStatus.SIGNAL_PICKUP_FAILURE + TrainStatus.E_BRAKE_FAILURE) ||
                failure == (TrainStatus.S_BRAKE_FAILURE  + TrainStatus.ENGINE_FAILURE + TrainStatus.E_BRAKE_FAILURE + TrainStatus.SIGNAL_PICKUP_FAILURE));
    }
    
    /* scoreBundle(TrainCommand command) score the value of the bundle based on power and brakes
     * Parameters:
     *     TrainCommand command - command to score
     * Returns - double, score
    */
    private double scoreBundle(TrainCommand command)
    {
        double score = command.PowerCommand;
        if (command.ServiceBrakeOn)
        {
            score = TrainController.MAX_TRAIN_MOTOR_POWER + 1;
        }
        else if(command.EmergencyBrakeOn)
        {
            score = TrainController.MAX_TRAIN_MOTOR_POWER + 2;
        }
        return score;
    }
}
