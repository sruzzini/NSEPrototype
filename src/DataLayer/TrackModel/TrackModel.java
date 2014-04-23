package DataLayer.TrackModel;

import DataLayer.Bundles.*;
import java.util.*;
import DataLayer.EnumTypes.*;
import DataLayer.Train.*;
import java.io.*;

public class TrackModel 
{
    public ArrayList<Line> TheLines; // array of lines within the track model
    public ArrayList<TrainLocation> TheTrainLocations; // array of train locations for the trains within the track model
    public ArrayList<Train> TheTrains; // array of trains within the track model
    
    // getBlockInfoBundle(LineColor Line, int block) returns the current block info for the specified block on the specified Line
    // Parameters:
    //     LineColor Line - the Line that is to be accessed
    //     int Block - the block that is to be accessed
    // Returns - BlockInfoBundle, the current info set in the specified block
    public BlockInfoBundle getBlockInfoBundle(LineColor line, int block)
    {
        int lineNum = 0;
        if (line == LineColor.GREEN)
        {
            lineNum = 0;
        }
        else if(line == LineColor.RED)
        {
            lineNum = 1;
        }
        XingState s = TheLines.get(lineNum).TheBlocks.get(block).getRRXingState();
        LightColor l = TheLines.get(lineNum).TheBlocks.get(block).getLightColor();
        boolean c = TheLines.get(lineNum).TheBlocks.get(block).isClosed();
        BlockInfoBundle b = new BlockInfoBundle(l, s, block, line, c);
        return b;
    }
    
    // getBlockSignalBundle(LineColor Line, int block) returns the current block signal for the specified block on the specified Line
    // Parameters:
    //     LineColor Line - the Line that is to be accessed
    //     int Block - the block that is to be accessed
    // Returns - BlockSignalBundle, the current signal set in the specified block
    public BlockSignalBundle getBlockSignalBundle(LineColor line, int block) 
    {
        int lineNum = 0;
        if (line == LineColor.GREEN)
        {
            lineNum = 0;
        }
        else if(line == LineColor.RED)
        {
            lineNum = 1;
        }
        int a = TheLines.get(lineNum).TheBlocks.get(block).getAuthority();
        int d = TheLines.get(lineNum).TheBlocks.get(block).getDestination();
        double s = TheLines.get(lineNum).TheBlocks.get(block).getVelocity();
        boolean c = TheLines.get(lineNum).TheBlocks.get(block).isClosed();
        BlockSignalBundle b = new BlockSignalBundle(a, d, s, block, line, c);
        return b;
    }
    
    // getTrackSignal(LineColor Line, int block) returns the current track signal for the specified block on the specified Line
    // Parameters:
    //     LineColor Line - the Line that is to be accessed
    //     int block - the block that is to be accessed
    // Returns - TrackSignal, the current signal set in the specified block
    public TrackSignal getTrackSignal(LineColor line, int block)
    {
        int lineNum = 0;
        if (line == LineColor.GREEN)
        {
            lineNum = 0;
        }
        else if(line == LineColor.RED)
        {
            lineNum = 1;
        }
        int a = TheLines.get(lineNum).TheBlocks.get(block).getAuthority();
        int d = TheLines.get(lineNum).TheBlocks.get(block).getDestination();
        String dd = TheLines.get(lineNum).TheBlocks.get(d).getStationString();
        double s = TheLines.get(lineNum).TheBlocks.get(block).getVelocity();
        double g = TheLines.get(lineNum).TheBlocks.get(block).getGradient();
        boolean u = TheLines.get(lineNum).TheBlocks.get(block).isUnderground();
        TrackSignal t = new TrackSignal(s, a, u, dd, g);
        return t;
    }
    
    // setBlockInfo(BlockInfoBundle b) sets the block info of the block specified in the passed in BlockInfoBundle
    // to the information contained within the BlockInfoBundle
    // Parameters:
    //      BlockInfoBundle b - the inforation packet to be set to the block specified within the BlockInfoBundle object
    public void setBlockInfo(BlockInfoBundle b)
    {
        int line = 0;
        if(b.LineID == LineColor.GREEN)
        {
            line = 0;
        }
        else if(b.LineID == LineColor.RED)
        {
            line = 1;
        }
        int block = b.BlockID;
        TheLines.get(line).TheBlocks.get(block).setRRXingState(b.RRXingState);
        TheLines.get(line).TheBlocks.get(block).setLightColor(b.LightColor);
    }
    
    // setBlockSignal(BlockSignalBundle b) sets the block signal of the block specified in the passed in BlockSignalBundle
    // to the information contained within the BlockSignalBundle
    // Parameters:
    //      BlockSignalBundle b - the signal packet to be set to the block specified within the BlockSignalBundle object
    public void setBlockSignal(BlockSignalBundle b)
    {
        int line = 0;
        if(b.LineID == LineColor.GREEN)
        {
            line = 0;
        }
        else if(b.LineID == LineColor.RED)
        {
            line = 1;
        }
        int block = b.BlockID;
        TheLines.get(line).TheBlocks.get(block).setAuthority(b.Authority);
        TheLines.get(line).TheBlocks.get(block).setDestination(b.Destination);
        TheLines.get(line).TheBlocks.get(block).setVelocity(b.Speed);
    }
    
    // setDispatchSignal(DispatchBundle b) sets the block signal of the first block out of the yard of the Line specified in the passed in DispatchBundle
    // to the information contained within the DispatchBundle
    // Parameters:
    //      DispatchBundle d - the signal packet to be set to the first block of the Line specified within the DispatchBundle object
    public void setDispatchSignal(DispatchBundle d)
    {
    	int line = 0;
    	int block = 0;
        if (d.toLine == LineColor.GREEN)
        {
            line = 0;
            block = 152;
        }
        else if(d.toLine == LineColor.RED)
        {
            line = 1;
            block = 77;
        }
        TheLines.get(line).TheBlocks.get(block).setAuthority(d.Authority);
        TheLines.get(line).TheBlocks.get(block).setDestination(d.Destination);
        TheLines.get(line).TheBlocks.get(block).setVelocity(d.Speed);
        
        TheTrainLocations.get(d.trainID).setStartLocation(line);
        
        boolean u = TheLines.get(line).TheBlocks.get(block).isUnderground();
        int dest = TheLines.get(line).TheBlocks.get(block).getDestination();
        String dd = TheLines.get(line).TheBlocks.get(dest).getStationString();
        double g = TheLines.get(line).TheBlocks.get(block).getGradient();
        TheLines.get(line).TheBlocks.get(block).setOccupied(true);
        
        TrackSignal t = new TrackSignal(d.Speed, d.Authority, u, dd, g);
        
        TheTrains.get(d.trainID).setTrackSignal(t);
    }
    
    // TrackModel() creates a new instance of a Track Model
    public TrackModel()
    {
        
        TheLines = new ArrayList<>();
        TheLines.add(new Line(LineColor.GREEN));
        TheLines.add(new Line(LineColor.RED));
        uploadTrackSpec();
        
    }
    
    // updateTrainLocations() updates the locations of the trains on the track
    public void updateTrainLocations()
    {
    	for(int i = 0; i < TheTrainLocations.size(); i++)
    	{
    	    LineColor line = TheTrainLocations.get(i).Line;
    	    int lineNum = 0;
            if (line == LineColor.GREEN)
            {
                lineNum = 0;
            }
            else if(line == LineColor.RED)
            {
                lineNum = 1;
            }
    	    int block = TheTrainLocations.get(i).CurrentBlock;
    	    int switchAlternate = -1;
    	    int prev = TheLines.get(lineNum).TheBlocks.get(block).Prev;
    	    if(prev < 0)
    	    {
    	        prev = (-prev) - 1;
    	        Switch aSwitch = TheLines.get(lineNum).TheSwitches.get(prev);
    	        if (block == aSwitch.ApproachBlock)
    	        {
    	            if (aSwitch.isStraight())
    	            {
    	            	prev = aSwitch.StraightBlock;
    	            	switchAlternate = aSwitch.DivergentBlock;
    	            }
    	            else
    	            {
    	            	prev = aSwitch.DivergentBlock;
    	            	switchAlternate = aSwitch.StraightBlock;
    	            }
    	        }
    	        else if(block == aSwitch.StraightBlock)
    	        {
    	            prev = aSwitch.ApproachBlock;
    	            if (!aSwitch.isStraight())
    	            {
    	            	//System.out.println("the switch was in the wrong position");
    	            }
    	        }
    	        else if(block == aSwitch.DivergentBlock)
    	        {
    	            prev = aSwitch.ApproachBlock;
    	            if (aSwitch.isStraight())
    	            {
    	            	//System.out.println("the switch was in the wrong position");
    	            }
    	        }
    	        
    	    }
    	    int next = TheLines.get(lineNum).TheBlocks.get(block).Next;
    	    if(next < 0)
    	    {
    	        next = (-next) - 1;
    	        Switch aSwitch = TheLines.get(lineNum).TheSwitches.get(next);
    	        if (block == aSwitch.ApproachBlock)
    	        {
    	            if (aSwitch.isStraight())
    	            {
    	            	next = aSwitch.StraightBlock;
    	            	switchAlternate = aSwitch.DivergentBlock;
    	            }
    	            else
    	            {
    	            	next = aSwitch.DivergentBlock;
    	            	switchAlternate = aSwitch.StraightBlock;
    	            }
    	        }
    	        else if(block == aSwitch.StraightBlock)
    	        {
    	            next = aSwitch.ApproachBlock;
    	            if (!aSwitch.isStraight())
    	            {
    	            	//System.out.println("the switch was in the wrong position");
    	            }
    	        }
    	        else if(block == aSwitch.DivergentBlock)
    	        {
    	            next = aSwitch.ApproachBlock;
    	            if (aSwitch.isStraight())
    	            {
    	            	//System.out.println("the switch was in the wrong position");
    	            }
    	        }
    	        
    	    }
    	    double length = TheLines.get(lineNum).TheBlocks.get(block).getLength();
            double deltaX = TheTrains.get(i).getDeltaX();
            //if (i == 0) {System.out.println("Asked for deltaX: " + deltaX);}
    	    TheTrainLocations.get(i).updateLocation(deltaX, length, prev, next, switchAlternate);
    	    
    	    int newCurrentBlock = TheTrainLocations.get(i).CurrentBlock;
    	    if(block == newCurrentBlock)
    	    {
    	    	TheLines.get(lineNum).TheBlocks.get(block).setOccupied(true);
                if(TheTrainLocations.get(i).DistanceSoFar > 32.2)
                {
                    TheLines.get(lineNum).TheBlocks.get(TheTrainLocations.get(i).PrevBlock).setOccupied(false);
                    TheLines.get(lineNum).TheBlocks.get(TheTrainLocations.get(i).PrevBlock).setBeaconSent(false);
                }
                // special case for block 77 to block 101
                else if(TheTrainLocations.get(i).PrevBlock == 101 && TheTrainLocations.get(i).CurrentBlock == 102)
                {
                    TheLines.get(lineNum).TheBlocks.get(77).setOccupied(false);
                    TheLines.get(lineNum).TheBlocks.get(77).setBeaconSent(false);
                }
    	    }
    	    else
    	    {
                if(TheTrainLocations.get(i).DistanceSoFar > 32.2)
                {
                    TheLines.get(lineNum).TheBlocks.get(block).setOccupied(false);
                    TheLines.get(lineNum).TheBlocks.get(block).setBeaconSent(false);
                }
    	    	TheLines.get(lineNum).TheBlocks.get(newCurrentBlock).setOccupied(true);
    	    }
    	    
    	    int a = TheLines.get(lineNum).TheBlocks.get(newCurrentBlock).getAuthority();
            int d = TheLines.get(lineNum).TheBlocks.get(newCurrentBlock).getDestination();
            String dd = TheLines.get(lineNum).TheBlocks.get(d).getStationString();
            double s = TheLines.get(lineNum).TheBlocks.get(newCurrentBlock).getVelocity();
            double g = TheLines.get(lineNum).TheBlocks.get(newCurrentBlock).getGradient();
            boolean u = TheLines.get(lineNum).TheBlocks.get(newCurrentBlock).isUnderground();
            TrackSignal t = new TrackSignal(s, a, u, dd, g);
            TheTrains.get(i).setTrackSignal(t);
            if(TheLines.get(lineNum).TheBlocks.get(newCurrentBlock).hasABeacon())
            {
                if(TheTrainLocations.get(i).PrevBlock < TheTrainLocations.get(i).CurrentBlock)
                {
                    if((TheTrainLocations.get(i).DistanceSoFar >= TheLines.get(lineNum).TheBlocks.get(newCurrentBlock).getBeaconLocation()) && !TheLines.get(lineNum).TheBlocks.get(newCurrentBlock).getBeaconSent())
                    {
                        TheTrains.get(i).setBeaconSignal(TheLines.get(lineNum).TheBlocks.get(newCurrentBlock).getBeacon());
                        TheLines.get(lineNum).TheBlocks.get(newCurrentBlock).setBeaconSent(true);
                    }
                }
                else
                {
                    if((TheTrainLocations.get(i).DistanceSoFar >= (TheLines.get(lineNum).TheBlocks.get(newCurrentBlock).getLength() - TheLines.get(lineNum).TheBlocks.get(newCurrentBlock).getBeaconLocation())) && !TheLines.get(lineNum).TheBlocks.get(newCurrentBlock).getBeaconSent())
                    {
                        TheTrains.get(i).setBeaconSignal(TheLines.get(lineNum).TheBlocks.get(newCurrentBlock).getBeacon());
                        TheLines.get(lineNum).TheBlocks.get(newCurrentBlock).setBeaconSent(true);
                    } 
                }
            }
            if(block != newCurrentBlock)
            {
                if(TheLines.get(lineNum).TheBlocks.get(block).hasABeacon() && !TheLines.get(lineNum).TheBlocks.get(block).getBeaconSent())
                {
                    TheTrains.get(i).setBeaconSignal(TheLines.get(lineNum).TheBlocks.get(block).getBeacon());
                    TheLines.get(lineNum).TheBlocks.get(block).setBeaconSent(true);
                }
            }
            /*
            if(i == 0)
            {
                System.out.println("CurrentBlock: " + TheTrainLocations.get(i).CurrentBlock + " Distance in: " +TheTrainLocations.get(i).DistanceSoFar);
            }
            */
    	}
    }
    
    // uploadTrackSpec() reads in a track specification from a file called track.csv
    public final void uploadTrackSpec()
    {
        String csvFile = "track.csv";
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
        
        int blockID, stationID, next, prev, approach, divergent, straight;
        int tswitchID = 0;
        double speedLimit, length, elevation, cumElev, gradient;
        boolean underground, light, rrxing, station, tswitch;
        String stationString;
        LightColor lColor;
        
        try {
 
		br = new BufferedReader(new FileReader(csvFile));
		while ((line = br.readLine()) != null) {
                    String[] blockSpec = line.split(cvsSplitBy);
                    
                    if (blockSpec[0].equalsIgnoreCase("green"))
                    {
                        if (blockSpec[1].equalsIgnoreCase("b"))
                        {
                            blockID = Integer.parseInt(blockSpec[2]);
                            length = Double.parseDouble(blockSpec[3]);
                            
                            next = Integer.parseInt(blockSpec[4]);
                            prev = Integer.parseInt(blockSpec[5]);
                            if (next < 0 || prev < 0)
                            {
                            	tswitch = true;
                            	if(next < 0)
                            	{
                            	    tswitchID = next - (2 * next);
                            	}
                            	else if(prev < 0)
                            	{
                            	    tswitchID = prev - (2 * prev);
                            	}
                            	light = true;
                                lColor = LightColor.GREEN;
                            }
                            else
                            {
                            	tswitch = false;
                            	tswitchID = -1;
                            	light = false;
                                lColor = LightColor.NO_LIGHT_ON_BLOCK;
                            }
                            
                            gradient = Double.parseDouble(blockSpec[6]);
                            elevation = Double.parseDouble(blockSpec[7]);
                            cumElev = Double.parseDouble(blockSpec[8]);
                            
                            speedLimit = Double.parseDouble(blockSpec[9]);
                            speedLimit = speedLimit * 1000.0;
                            speedLimit = speedLimit / 3600.0;
                            
                            station = Boolean.parseBoolean(blockSpec[10]);
                            if (station)
                            {
                                stationID = Integer.parseInt(blockSpec[11]);
                                stationString = blockSpec[12];
                            }
                            else
                            {
                                stationID = -1;
                                stationString = "";
                            }
                            
                            underground = Boolean.parseBoolean(blockSpec[13]);
                            rrxing = Boolean.parseBoolean(blockSpec[14]);
                            XingState xState;
                            if (rrxing)
                            {
                                xState = XingState.NOT_ACTIVE;
                            }
                            else
                            {
                                xState = XingState.NO_XING_ON_BLOCK;
                            }
                            
                            Block b = new Block(blockID, next, prev, length, speedLimit, elevation, cumElev, gradient, underground, light, rrxing, station, tswitch);
                            b.setStationID(stationID);
                            b.setStationString(stationString);
                            b.setTswitchID(tswitchID);
                            b.setLightColor(lColor);
                            b.setRRXingState(xState);
                            
                            boolean hasBeacon = Boolean.parseBoolean(blockSpec[15]);
                            if(hasBeacon)
                            {
                                String beaconName = blockSpec[16];
                                double distanceIn = Double.parseDouble(blockSpec[17]);
                                boolean specialBeacon = Boolean.parseBoolean(blockSpec[19]);
                                BeaconSignal theBeacon;
                                if(specialBeacon)
                                {
                                    theBeacon = new BeaconSignal(beaconName, Boolean.parseBoolean(blockSpec[18]), Double.parseDouble(blockSpec[20]));
                                }
                                else
                                {
                                    theBeacon = new BeaconSignal(beaconName, Boolean.parseBoolean(blockSpec[18]));
                                }
                                b.setBeacon(theBeacon);
                                b.setBeaconLocation(distanceIn);
                                b.setHasABeacon(true);
                            }
                            else
                            {
                                b.setHasABeacon(false);
                            }
                            
                            TheLines.get(0).TheBlocks.add(b);
                        }
                        else if(blockSpec[1].equalsIgnoreCase("s"))
                        {
                            blockID = Integer.parseInt(blockSpec[2]);
                            approach = Integer.parseInt(blockSpec[3]);
                            straight = Integer.parseInt(blockSpec[4]);
                            divergent = Integer.parseInt(blockSpec[5]);
                            Switch s = new Switch(LineColor.GREEN, blockID, approach, straight, divergent, true);
                            TheLines.get(0).TheSwitches.add(s);
                        }
                    }
                    else if(blockSpec[0].equalsIgnoreCase("red"))
                    {
                        if (blockSpec[1].equalsIgnoreCase("b"))
                        {
                            blockID = Integer.parseInt(blockSpec[2]);
                            length = Double.parseDouble(blockSpec[3]);
                            
                            next = Integer.parseInt(blockSpec[4]);
                            prev = Integer.parseInt(blockSpec[5]);
                            if (next < 0 || prev < 0)
                            {
                            	tswitch = true;
                            	if(next < 0)
                            	{
                            	    tswitchID = next - (2 * next);
                            	}
                            	else if(prev < 0)
                            	{
                            	    tswitchID = prev - (2 * prev);
                            	}
                            	light = true;
                            }
                            else
                            {
                            	tswitch = false;
                            	tswitchID = -1;
                            	light = false;
                            }
                            
                            gradient = Double.parseDouble(blockSpec[6]);
                            elevation = Double.parseDouble(blockSpec[7]);
                            cumElev = Double.parseDouble(blockSpec[8]);
                            
                            speedLimit = Double.parseDouble(blockSpec[9]);
                            speedLimit = speedLimit * 1000.0;
                            speedLimit = speedLimit / 3600.0;
                            
                            station = Boolean.parseBoolean(blockSpec[10]);
                            if (station)
                            {
                                stationID = Integer.parseInt(blockSpec[11]);
                                stationString = blockSpec[12];
                            }
                            else
                            {
                                stationID = -1;
                                stationString = "";
                            }
                            
                            underground = Boolean.parseBoolean(blockSpec[13]);
                            rrxing = Boolean.parseBoolean(blockSpec[14]);

                            Block b = new Block(blockID, next, prev, length, speedLimit, elevation, cumElev, gradient, underground, light, rrxing, station, tswitch);
                            b.setStationID(stationID);
                            b.setStationString(stationString);
                            b.setTswitchID(tswitchID);
                            
                            boolean hasBeacon = Boolean.parseBoolean(blockSpec[15]);
                            if(hasBeacon)
                            {
                                String beaconName = blockSpec[16];
                                double distanceIn = Double.parseDouble(blockSpec[17]);
                                boolean specialBeacon = Boolean.parseBoolean(blockSpec[19]);
                                BeaconSignal theBeacon;
                                if(specialBeacon)
                                {
                                    theBeacon = new BeaconSignal(beaconName, Boolean.parseBoolean(blockSpec[18]), Double.parseDouble(blockSpec[20]));
                                }
                                else
                                {
                                    theBeacon = new BeaconSignal(beaconName, Boolean.parseBoolean(blockSpec[18]));
                                }
                                b.setBeacon(theBeacon);
                                b.setBeaconLocation(distanceIn);
                                b.setHasABeacon(true);
                            }
                            else
                            {
                                b.setHasABeacon(false);
                            }
                            
                            TheLines.get(1).TheBlocks.add(b);
                        }
                        else if(blockSpec[1].equalsIgnoreCase("s"))
                        {
                            blockID = Integer.parseInt(blockSpec[2]);
                            approach = Integer.parseInt(blockSpec[3]);
                            straight = Integer.parseInt(blockSpec[4]);
                            divergent = Integer.parseInt(blockSpec[5]);
                            Switch s = new Switch(LineColor.RED, blockID, approach, straight, divergent, true);
                            TheLines.get(1).TheSwitches.add(s);
                        }
                    }
                    
		}
 
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
 
	System.out.println("Track Model is done reading spec.");
    }
}
