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


//Train Controller Class
public class TrainController 
{
    public static final double STANDARD_SAMPLE_PERIOD = 0.1;
    private static final double PROPORTIONAL_GAIN = 100000;
    private static final double INTEGRAL_GAIN = 27;
    public static final double MAX_TRAIN_MOTOR_POWER = 120000; //120 KW
    public static final double MAX_TRAIN_SPEED = 19.444; //70 km/hr in m/s
    private static final double MIN_VERROR_FOR_S_BRAKE = -.89408; //-5 mph
    public static final double ROOM_TEMP = 21.1; //70 degrees fahrenheit, in celsius
    
    public enum OperatorInputStatus
    {
        AUTO, OFF, ON
    }
    
    private final int iD;
    private int timeMultiplier;
    private long time;
    private double samplePeriod;
    private TrainStatus trainStatus;
    private TrackSignal trackSignal;
    private BeaconSignal lastBeacon;
    private double lastIntermediary;
    private double lastVelocityError;
    public double VelocitySetPoint; // meters/sec
    public OperatorInputStatus OperatorEBrake;
    public OperatorInputStatus OperatorSBrake;
    public OperatorInputStatus OperatorLeftDoor;
    public OperatorInputStatus OperatorRightDoor;
    public OperatorInputStatus OperatorExtLights;
    public OperatorInputStatus OperatorIntLights;
    public double DesiredTemperature; //celsius
    
    
    //Contructors 
    public TrainController()
    {
        this.iD = 0;
        this.timeMultiplier = 1;
        this.time = System.currentTimeMillis();
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
        this.timeMultiplier = multiplier;
        this.time = System.currentTimeMillis();
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
        this.timeMultiplier = multiplier;
        this.time = System.currentTimeMillis();
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
    
    
    //Public methods
    public void readTrainToControllerBundle(TrainStatus trainS, TrackSignal trackS, BeaconSignal beacon)
    {
        this.trainStatus = trainS;
        this.trackSignal = trackS;
        if (beacon != null)
        {
            this.lastBeacon = beacon;
        }
    }
    
    public void setTimeMultiplier(int multiplier)
    {
        this.timeMultiplier = multiplier;
    }
    
    public TrainCommand getTrainCommand()
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
            toReturn = new TrainCommand(0, false, false, false, false, this.trainStatus.getExteriorLightStatus(), 
                                                   this.trainStatus.getInteriorLightStatus(), this.trainStatus.getHeaterStatus(),
                                                   this.trackSignal.getNextDestination());
            int trainFailure = this.trainStatus.getFailure();
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
    
    
    //Private methods
    private TrainCommand calculateTrainCommand()
    {
        TrainCommand command = new TrainCommand();
        
        double safeVelocity = 0; //velocity to calculate VError
        double velocityCommand = this.trackSignal.getVelocityCommand(); //vCommand
        double currTrainVelocity = this.trainStatus.getVelocity(); //curr Velocity
        int trainFailure = this.trainStatus.getFailure(); //failure
        
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
        
        //calculate power output
        double currVError = safeVelocity - currTrainVelocity;
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
            this.trainStatus.getRightDoorStatus() || //Right Door is open
            this.trainStatus.getLeftDoorStatus()) //Left Door is open
        {
            command.PowerCommand = 0.0;
        }
        
        command.LeftDoorsOpen = calculateLeftDoorCommand(currTrainVelocity); //set Left Door
        
        command.RightDoorsOpen = calculateRightDoorCommand(currTrainVelocity); //set Right Door
        
        command.InteriorLightsOn = calculateInteriorLights(); //set interior lights
        
        command.ExteriorLightsOn = calculateExteriorLights(); //set exterior lights
        
        command.HeaterOn = (this.trainStatus.getTemperature() < this.DesiredTemperature); //set heater
        
        command.Announcement = this.trackSignal.getNextDestination(); //set announcement
        
        return command;
    }
    
    private boolean calculateEBrakeCommand(int failure)
    {
        boolean eBrake = false;
        boolean eBrakeFailure = hasEBrakeFailure(failure); //check if their is an e-brake failure
        
        if(((this.OperatorEBrake == OperatorInputStatus.ON) && !eBrakeFailure) || //Operator requests EBrake and no EBrake failure
           (this.trainStatus.getPassengerBrakeRequest() && !eBrakeFailure) || //Passenger requests EBrake and no EBrake failure
           ((failure != 0) && !eBrakeFailure)) //There is some failure and no EBrake failure
        {
            eBrake = true;
        }
        
        return eBrake;
    }
    
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
    
    private boolean calculateInteriorLights()
    {
        boolean lights = false;
        boolean underground = this.trackSignal.getUndergroundStatus();
        boolean doorsOpen = (this.trainStatus.getLeftDoorStatus() || this.trainStatus.getRightDoorStatus());
        if ((underground && this.OperatorIntLights == OperatorInputStatus.AUTO) || //we're underground and the operator hasn't specified light command
            (doorsOpen) || //doors are open (keep lights on for passengers entering and exiting
            (this.OperatorIntLights == OperatorInputStatus.ON)) //Operator turns the light on
        {
            lights = true;
        }
        return lights;
    }
    
    private boolean calculateLeftDoorCommand(double vCurr)
    {
        boolean door = false;
        if ((vCurr == 0) && (this.OperatorLeftDoor == OperatorInputStatus.ON))  //Train is stopped and conductor has left door input on
        {
            door = true;
        }
        return door;
    }
    
    private boolean calculateRightDoorCommand(double vCurr)
    {
        boolean door = false;
        if ((vCurr == 0) && (this.OperatorRightDoor == OperatorInputStatus.ON)) //Train is stopped and conductor has right door input on
        {
            door = true;
        }
        return door;
    }
    
    private boolean calculateServiceBrakeCommand(double vError, double vSafe, int failure)
    {
        boolean sBrake = false;
        boolean sBrakeFailure = hasSBrakeFailure(failure); //check if there is an s-brake failure
        boolean eBrakeFailure = hasEBrakeFailure(failure); //check if there is an e-brake failure
        
        if (((this.OperatorSBrake == OperatorInputStatus.ON) && !sBrakeFailure) || //Operator pulls brake an no s-brake failure
            (vError <= TrainController.MIN_VERROR_FOR_S_BRAKE && !sBrakeFailure) || //Verror is under -5mph and no s-brake failure
            (vSafe == 0 && !sBrakeFailure) || //Vcommand safe is 0 and no s-brake failure
            (!sBrakeFailure && (this.trackSignal.getAuthority() == 0)) || //Authority is 0 and no s-brake failure
            (!sBrakeFailure &&  eBrakeFailure)) //There is an e-brake failure and no s-brake failure
        {
            sBrake = true;
        }
        
        return sBrake;
    }
    
    private boolean checkBundleSafety(TrainCommand command)
    {
        boolean safe = false;
        int trainFailure = this.trainStatus.getFailure();
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
                if ((this.trainStatus.getVelocity() > 0) && (!command.RightDoorsOpen && !command.LeftDoorsOpen) || //train moving and doors closed
                    (this.trainStatus.getVelocity() == 0))//train is stopped
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
