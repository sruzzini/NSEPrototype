/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.Bundles.*;
import DataLayer.EnumTypes.*;
import DataLayer.TrackModel.*;
import java.io.*;
import java.util.*;

/**
 * <h1>Wayside</h1>
 * <p>
 * The Wayside class acts as a communication helper between the CTC office and the
 * Track Controllers. It forwards signals from the CTC to the appropriate Track Controller. </p>
 * <p>
 * It also provides the means for the NSE simulation to setup and start and pause the 
 * Track Controllers. </p>
 *
 * @author nwhachten
 * @version 1.1
 * @since 2014-04-02
 */
public final class Wayside 
{
    public static double STOP_SPEED = 15.28;
    private int controllerCount;
    private TrackController[] controllers;
    private final TrackModel track;
    
    /**
     * This method is the constructor for the Wayside class. It handles instantiation 
     * of all Track Controllers 
     * @param track This is a reference to the TrackModel class that is used in the sim
     */
    public Wayside(TrackModel track)
    { 
        BufferedReader br;
        this.track = track;
        
        try 
        {
            String fileName = "wayside_layout.txt";
            String currentLine;
            br = new BufferedReader(new FileReader(fileName));
            
            controllerCount = Integer.parseInt(br.readLine());
            this.controllers = new TrackController[controllerCount];
            
            
            String[] controllerInfo;
            String[] blocks;
            int[] blockNums;
            LineColor line;
            for (int i = 0; i < controllerCount; i++)
            {
                currentLine = br.readLine();
                controllerInfo = currentLine.split(":");
                if (controllerInfo[1].equals("Green"))
                {
                    line = LineColor.GREEN;
                }
                else
                {
                    line = LineColor.RED;
                }
                blocks = controllerInfo[2].split(",");
                blockNums = new int[blocks.length];
                for (int j =0; j < blockNums.length; j++)
                {
                    blockNums[j] = Integer.parseInt(blocks[j]);
                }
                
                controllers[i] = new TrackController(Integer.parseInt(controllerInfo[0]), line, blockNums);
               
                
            }
        
        }
        catch (FileNotFoundException e)
        {
            //catch error
            e.printStackTrace();
        }
        catch (IOException e1)
        {
            //catch error
            e1.printStackTrace();
        }
        this.setBlockInfoArray(track.theLines.get(0).theBlocks, LineColor.GREEN);
        this.setBlockInfoArray(track.theLines.get(1).theBlocks, LineColor.RED);
        this.setSwitchArray(track.theLines.get(0).theSwitches);
        this.setSwitchArray(track.theLines.get(1).theSwitches);
        this.configurePLCs();
  
    }
    
    public TrackController getController(int n)
    {
        return this.controllers[n];
    }
    
    public String[] getControllerNames()
    {
        String[] names = new String[controllerCount];
        int i = 0;
        
        for (TrackController tc : this.controllers)
        {
            names[i++] = tc.toString();
        }
        
        
        return names;
    }
    
    public TrackController[] getControllers()
    {
        return this.controllers;
    }
    
   /* public ArrayList<Block> getBlockInfoArray(LineColor line)
    {
        ArrayList<Block> blockList;
        blockList = new ArrayList<>();
        
        for (TrackController tc : this.controllers)
        {
            if (tc.getLine() == line)
            {
                blockList.addAll(tc.getBlockInfo());
            }
        }
        
        return blockList;
    }*/
    
   /* public ArrayList<BlockInfoBundle> getBlockInfoCommands()
    {
        ArrayList<BlockInfoBundle> commands;
        commands = new ArrayList<>();
        
        for (TrackController tc : this.controllers)
        {
            for (BlockInfoBundle c : tc.getCommandBlockQueue())
            {
                commands.add(c);
            }
        }
        
        return commands;
    }*/
    
   /* public ArrayList<BlockSignalBundle> getBlockSignalCommands()
    {
        ArrayList<BlockSignalBundle> commands;
        commands = new ArrayList<>();
        
        for (TrackController tc : this.controllers)
        {
            for (BlockSignalBundle c : tc.getCommandSignalQueue())
            {
                commands.add(c);
            }
        }
        
        return commands;
    }*/
    
    /**
     * This method returns an array of signals indicating which blocks are currently
     * being observed as occupied.
     * @return ArrayList<BlockSignalBundle> This returns an array containing info for all occupied
     * blocks in the track model.
     */
    public ArrayList<BlockSignalBundle> getOccupancyInfo()
    {
        ArrayList<BlockSignalBundle> occupiedBlocks;
        occupiedBlocks = new ArrayList<>();
        BlockSignalBundle currentSignal;
        
        for (TrackController tc : this.controllers)
        {
            for (Block b : tc.getOccupiedBlocks())
            {
                currentSignal = new BlockSignalBundle(b.getAuthority(),b.getDestination(),b.getSpeedLimit(),b.getBlockID(),tc.getLine());
                occupiedBlocks.add(currentSignal);
            }
        }

        return occupiedBlocks;
    }
    
    /*
    public ArrayList<Switch> getSwitchCommands()
    {
        ArrayList<Switch> commands;
        commands = new ArrayList<>();
        
        for (TrackController tc : this.controllers)
        {
            for (Switch c : tc.getCommandSwitchQueue())
            {
                commands.add(c);
            }
        }
        
        return commands;
    }*/
    
    public ArrayList<Switch> getSwitchInfo()
    {
        ArrayList<Switch> switchInfo;
        switchInfo = new ArrayList<>();
        
        for (TrackController tc : this.controllers)
        {
            for (Switch s : tc.getSwitchInfo())
            {
                switchInfo.add(s);
            }
        }
        
        return switchInfo;
    }
    
    public void sendDispatchSignal(DispatchBundle packet)
    {
        //System.out.println("Go you damn train! To line: " + packet.toLine + " train ID: " + packet.trainID + " also packet blockid " + packet.BlockID);
        if (packet == null)
        {
            System.out.println("Wayside - sendDispatchBundle - packet is null");
            return;
        }
        this.track.setDispatchSignal(packet.copy());
        ArrayList<BlockSignalBundle> array = new ArrayList<>();
        if (packet.toLine == LineColor.GREEN)
        {
            
            //array.add(new BlockSignalBundle(packet, 152, LineColor.GREEN));
            //this.sendTravelSignal(array);
            this.sendTravelSignal(new BlockSignalBundle(packet.Authority, packet.Destination, packet.Speed, 152, LineColor.GREEN));
        }
        else
        {
            //array.add(new BlockSignalBundle(packet, 77, LineColor.RED));
            //this.sendTravelSignal(array);
            this.sendTravelSignal(new BlockSignalBundle(packet.Authority, packet.Destination, packet.Speed, 77, LineColor.RED));
        }
    }
    
    public void sendSwitchStateSignal(Switch packet)
    {
        if (packet == null)
        {
            System.out.println("Wayside - sendSwitchStateSignal - packet is null");
        }
        else
        {
            for (TrackController tc : this.controllers)
            {
                if (tc.getLine() == packet.lineID && tc.containsBlock(packet.approachBlock) )
                {
                    tc.sendSwitchStateSignal(new Switch(packet.lineID, packet.switchID, packet.approachBlock, packet.straightBlock, packet.divergentBlock, packet.straight));
                    break;
                }
            }
        }
    }
    
   /* public void sendTravelSignal(BlockSignalBundle sentPacket)
    {
        if (sentPacket == null) 
        {
            System.out.println("Wayside - sendTravelSignal - packet is null");
            return;
        
        }
        BlockSignalBundle packet = sentPacket.copy();
        LineColor line = packet.LineID;
        int blockNum = packet.BlockID;
        
        for (int i=0; i < controllerCount; i++)
        {
            if (controllers[i].getLine() == line)
            {
                for (int b : controllers[i].getBlockNums() )
                {
                    if (b == blockNum)
                    {
                       // System.out.println("Sending signal: to block " + packet.BlockID + " to line " + packet.LineID + " with auth " + packet.Authority + " with speed " + packet.Speed + " with dest " + packet.Destination);
                       // System.out.println("To controller " + i);
                        this.controllers[i].sendTravelSignal(packet.copy());
                    }
                }
            }
        }
    }*/
    
    private void sendTravelSignal(BlockSignalBundle packet)
    {
        if (packet == null)
        {
            System.out.println("Wayside - sendTravelSignal array version - packet is null");
        }
        
        LineColor line = packet.LineID;
        int blockNum = packet.BlockID;
        
        for (TrackController tc : this.controllers)
        {
            if (tc.getLine() == line)
            {
                if (tc.containsBlock(blockNum))
                {
                    tc.sendTravelSignal(packet.copy());
                  // System.out.println("Wayside - sendTravelSignal(BlockSignalBundle) - signal for block: " + packet.BlockID +
                    //      " Authority: " + packet.Authority + " Destination: " + packet.Destination);
                    break;
                }
            }
        }
    }
    
    public void sendTravelSignal(ArrayList<BlockSignalBundle> signal)
    {
        if (signal == null)
        {
            System.out.println("Wayside - sendTravelSignal(ArrayList) - list is null");
        }
        else
        {
            if (signal.size() > 0)
            {
                
                BlockSignalBundle packet = signal.get(0);
                this.sendTravelSignal(packet);
            }
        }
        
        
        
    }
    
    /*public void sendTravelSignal(ArrayList<BlockSignalBundle> signal)
    {
        int k = 0;
        for (BlockSignalBundle packet : signal)
        {
            if (packet == null)
            {
                System.out.println("Wayside - sendTravelSignal array version - packet is null");
            }
            
            LineColor line = packet.LineID;
            int blockNum = packet.BlockID;
            
           //System.out.println("Wayside - sendTravelSignal array version - received travel signal for block " + blockNum + " and authority " + packet.Authority);
            
           
            
            for (TrackController tc : this.controllers)
            {
                if (tc.getLine() == line)
                {
                    if (tc.containsBlock(blockNum))
                    {
                        //tc.sendTravelSignal2(packet.copy(), k++);
                    }
                }
            }
            if ( k > 0)
                break;
        }
    }*/
    
   /* public void sendTravelSignal(BlockSignalBundle packet)
    {
        if (packet == null)
        {
            System.out.println("Wayside - sendTravelSignal - packet is null");
        }
        else
        {
            
            LineColor line = packet.LineID;
            int blockNum = packet.BlockID;
            BlockSignalBundle returnedBundle;
            
            System.out.println("Wayside - sendTravelSignal - received travel signal for block " + blockNum + " and authority " + packet.Authority);
            
            
            for (TrackController tc : this.controllers)
            {
                if (tc.getLine() == line)
                {
                    if (tc.containsBlock(blockNum))
                    {
                        returnedBundle = tc.sendTravelSignal(packet.copy());
                        if (returnedBundle == null)
                        {
                            break;
                        }
                        else
                        {
                            this.sendTravelSignal(returnedBundle);
                        }
                        
                    }
                }
            }
        }
        
    }*/
    
    public void setBlockClosing(BlockSignalBundle packet)
    {
        
    }
    
    public void StartSimulation()
    {
        //put each tc into a thread and start those bad boys
        this.startControllers();
        
    }
    
    public void StopSimulation()
    {
        
    }
    
    private void configurePLCs()
    {
        for (TrackController tc : this.controllers)
        {
            tc.setPLC();
        }
    }
    
    private void setBlockInfoArray(ArrayList<Block> blockArray, LineColor line)
    {
        
        for (Block b : blockArray)
        {
            for (TrackController tc : this.controllers)
            {
                if (tc.getLine() == line && tc.containsBlock(b.getBlockID()))
                {
                    tc.addBlock(b);
                }
                      
            }
        }    
    }
    
    private void setSwitchArray(List<Switch> switchArray)
    {
        LineColor line;
        
        for (Switch s : switchArray)
        {
            line = s.lineID;
            for (TrackController tc : this.controllers)
            {
                if (tc.getLine() == s.lineID && ( tc.containsBlock(s.approachBlock)))// || tc.containsBlock(s.divergentBlock) || tc.containsBlock(s.straightBlock) ))
                {
                    tc.addSwitch(s);
                }
                      
            }
        }
    }
    
    private void startControllers()
    {
        
        TrackController tc0 = this.controllers[0];
        TrackController tc1 = this.controllers[1];
        TrackController tc2 = this.controllers[2];
        
        new Thread(tc0).start();
        new Thread(tc1).start();
        new Thread(tc2).start();
    }
    
}
