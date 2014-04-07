package DataLayer.TrackModel;

import DataLayer.Bundles.*;
import java.util.*;
import DataLayer.EnumTypes.*;
import java.io.*;

public class TrackModel 
{
    public final ArrayList<Line> theLines;
    public ArrayList<TrainLocation> theTrains;
    private LineColor lineColor;
    
    
    public TrackModel()
    {
        
        theLines = new ArrayList<>();
        theLines.add(new Line(LineColor.GREEN));
        theLines.add(new Line(LineColor.RED));
        uploadTrackSpec();
        
    }
    
    public final void uploadTrackSpec()
    {
        String csvFile = "track.csv";
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
        
        int blockID, length, stationID;
        double speedLimit, elevation, cumElev, gradient;
        boolean underground, light, rrxing, station, tswitch;
        String stationString;
        
        try {
 
		br = new BufferedReader(new FileReader(csvFile));
		while ((line = br.readLine()) != null) {
                    String[] blockSpec = line.split(cvsSplitBy);
                    
                    if (blockSpec[0].equalsIgnoreCase("green"))
                    {
                        if (blockSpec[1].equalsIgnoreCase("block"))
                        {
                            blockID = Integer.parseInt(blockSpec[2]);
                            length = Integer.parseInt(blockSpec[3]);
                            speedLimit = Double.parseDouble(blockSpec[5]);
                            speedLimit = speedLimit * 1000.0;
                            speedLimit = speedLimit / 3600.0;
                            gradient = Double.parseDouble(blockSpec[4]);
                            station = Boolean.parseBoolean(blockSpec[6]);
                            if (station)
                            {
                                stationID = Integer.parseInt(blockSpec[7]);
                                stationString = blockSpec[8];
                            }
                            else
                            {
                                stationID = -1;
                                stationString = null;
                            }
                            underground = Boolean.parseBoolean(blockSpec[9]);
                            rrxing = Boolean.parseBoolean(blockSpec[10]);
                            light = Boolean.parseBoolean(blockSpec[11]);
                            elevation = Double.parseDouble(blockSpec[12]);
                            cumElev = Double.parseDouble(blockSpec[13]);
                            tswitch = Boolean.parseBoolean(blockSpec[14]);
                            Block b = new Block(blockID, length, speedLimit, elevation, cumElev, gradient, underground, light, rrxing, station, tswitch);
                            b.setStationID(stationID);
                            b.setStationString(stationString);
                            theLines.get(0).theBlocks.add(b);
                        }
                    }
                    else if(blockSpec[0].equalsIgnoreCase("red"))
                    {
                        if (blockSpec[1].equalsIgnoreCase("block"))
                        {
                            blockID = Integer.parseInt(blockSpec[2]);
                            length = Integer.parseInt(blockSpec[3]);
                            speedLimit = Double.parseDouble(blockSpec[5]);
                            speedLimit = speedLimit * 1000.0;
                            speedLimit = speedLimit / 3600.0;
                            gradient = Double.parseDouble(blockSpec[4]);
                            station = Boolean.parseBoolean(blockSpec[6]);
                            if (station)
                            {
                                stationID = Integer.parseInt(blockSpec[7]);
                                stationString = blockSpec[8];
                            }
                            else
                            {
                                stationID = -1;
                                stationString = null;
                            }
                            underground = Boolean.parseBoolean(blockSpec[9]);
                            rrxing = Boolean.parseBoolean(blockSpec[10]);
                            light = Boolean.parseBoolean(blockSpec[11]);
                            elevation = Double.parseDouble(blockSpec[12]);
                            cumElev = Double.parseDouble(blockSpec[13]);
                            tswitch = Boolean.parseBoolean(blockSpec[14]);
                            Block b = new Block(blockID, length, speedLimit, elevation, cumElev, gradient, underground, light, rrxing, station, tswitch);
                            b.setStationID(stationID);
                            b.setStationString(stationString);
                            theLines.get(1).theBlocks.add(b);
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
    
    public void setBlockInfo(BlockInfoBundle b)
    {
        int line = 0;
        lineColor = b.LineID;
        switch (lineColor)
        {
            case GREEN:
                line = 0;
            case RED:
                line = 1;
        }
        int block = b.BlockID;
        theLines.get(line).theBlocks.get(block).setRRXingState(b.RRXingState);
        theLines.get(line).theBlocks.get(block).setLightColor(b.LightColor);
    }
    
    public BlockInfoBundle getBlockInfoBundle(LineColor line, int block)
    {
        int lineNum = 0;
        switch (line)
        {
            case GREEN:
                lineNum = 0;
            case RED:
                lineNum = 1;
        }
        XingState s = theLines.get(lineNum).theBlocks.get(block).getRRXingState();
        LightColor l = theLines.get(lineNum).theBlocks.get(block).getLightColor();
        boolean c = theLines.get(lineNum).theBlocks.get(block).isClosed();
        BlockInfoBundle b = new BlockInfoBundle(l, s, block, line, c);
        return b;
    }
    
    public void setBlockSignal(BlockSignalBundle b)
    {
        int line = 0;
        lineColor = b.LineID;
        switch (lineColor)
        {
            case GREEN:
                line = 0;
            case RED:
                line = 1;
        }
        int block = b.BlockID;
        theLines.get(line).theBlocks.get(block).setAuthority(b.Authority);
        theLines.get(line).theBlocks.get(block).setDestination(b.Destination);
        theLines.get(line).theBlocks.get(block).setVelocity(b.Speed);
    }
    
    public BlockSignalBundle getBlockSignalBundle(LineColor line, int block) 
    {
        int lineNum = 0;
        switch (line)
        {
            case GREEN:
                lineNum = 0;
            case RED:
                lineNum = 1;
        }
        int a = theLines.get(lineNum).theBlocks.get(block).getAuthority();
        int d = theLines.get(lineNum).theBlocks.get(block).getDestination();
        double s = theLines.get(lineNum).theBlocks.get(block).getVelocity();
        boolean c = theLines.get(lineNum).theBlocks.get(block).isClosed();
        BlockSignalBundle b = new BlockSignalBundle(a, d, s, block, line, c);
        return b;
    }
    
    public TrackSignal getTrackSignal(LineColor line, int block)
    {
        int lineNum = 0;
        switch (line)
        {
            case GREEN:
                lineNum = 0;
            case RED:
                lineNum = 1;
        }
        int a = theLines.get(lineNum).theBlocks.get(block).getAuthority();
        int d = theLines.get(lineNum).theBlocks.get(block).getDestination();
        String dd = theLines.get(lineNum).theBlocks.get(d).getStationString();
        double s = theLines.get(lineNum).theBlocks.get(block).getVelocity();
        double g = theLines.get(lineNum).theBlocks.get(block).getGradient();
        boolean u = theLines.get(lineNum).theBlocks.get(block).isUnderground();
        TrackSignal t = new TrackSignal(s, a, u, dd);
        return t;
    }
    
}
