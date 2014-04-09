/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.Wayside;

import DataLayer.Bundles.BlockInfoBundle;
import DataLayer.Bundles.BlockSignalBundle;
import DataLayer.Bundles.DispatchBundle;
//import DataLayer.Bundles.Switch;
import DataLayer.TrackModel.Switch;
import DataLayer.EnumTypes.LineColor;
import DataLayer.TrackModel.Block;
import DataLayer.TrackModel.TrackModel;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nwhachten
 * @version 1.0
 * @since 2014-04-02
 */
public final class Wayside {
    private  TrackController[] controllers;
    private  int controllerCount;
    private TrackModel track;
    //private final int[][] blockNums;
    //private final LineColor[] lines;
    
    
    public Wayside(TrackModel track)
    {
       /* int[][] blockNums = new int[][]{{0,1},{2,3},{4,5},{5,6},{6,7},{8,9}};
        LineColor[] lines = new LineColor[] {LineColor.GREEN, LineColor.GREEN, LineColor.GREEN,
            LineColor.RED, LineColor.RED, LineColor.RED};*/
        //int[][] blockNums = new int[][];
        
        BufferedReader br = null;
        this.track = track;
        
        try 
        {
            String fileName = "wayside_layout.txt";
            String currentLine;
            br = new BufferedReader(new FileReader(fileName));
            
            controllerCount = Integer.parseInt(br.readLine());
            
        
            this.controllers = new TrackController[controllerCount];
            //int[][] blockNums = new int[controllerCount][];
            //LineColor[] lines = new LineColor[controllerCount];
            
            
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
           
        
       // this.setBlockInfoArray(blockInfoArray);
        //this.setBlockSignalArray(blockSignalArray);
        
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
    
    public void StartSimulation()
    {
        //put each tc into a thread and start those bad boys
        this.startControllers();
        
    }
    
    public void StopSimulation()
    {
        
    }
    
    private void startControllers()
    {
       /* for(TrackController tc : this.controllers)
        {
            new Thread(tc).start();
        }*/
        
        TrackController tc0 = this.controllers[0];
        TrackController tc1 = this.controllers[1];
        
        new Thread(tc0).start();
        new Thread(tc1).start();
    }
    
    public void sendTravelSignal(BlockSignalBundle packet)
    {
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
                        this.controllers[i].sendTravelSignal(packet);
                    }
                }
            }
        }
    }
    
    public void sendSwitchStateSignal(Switch packet)
    {
        for (TrackController tc : this.controllers)
        {
            if (tc.getLine() == packet.lineID && (tc.containsBlock(packet.straightBlock) || 
                    tc.containsBlock(packet.approachBlock) || tc.containsBlock(packet.divergentBlock) ))
            {
                tc.sendSwitchStateSignal(packet);
                break;
            }
        }
    }
    
    public void sendDispatchSignal(DispatchBundle packet)
    {
        System.out.println("Go you damn train! To line: " + packet.toLine + " train ID: " + packet.trainID);
        this.track.setDispatchSignal(packet);
    }
    
    private void setBlockInfoArray(ArrayList<Block> blockArray, LineColor line)
    {
        /*ArrayList<ArrayList<Block>> list;
        list = new ArrayList<>(controllerCount);*/
        
        for (Block b : blockArray)
        {
            for (TrackController tc : this.controllers)
            {
                if (tc.getLine() == line && tc.containsBlock(b.getBlockID()))
                {
                    //list.get(tc.getId()).add(b);
                   // System.out.println("Adding block with blockID: " + b.getBlockID());
                    tc.addBlock(b);
                }
                      
            }
        }
        /*
        for (TrackController tc : this.controllers)
        {
            if (tc.getLine() == line)
            {
                tc.setTrackBlockInfo(list.get(tc.getId()));
            }
        }*/
        
    }
    
    public ArrayList<Block> getBlockInfoArray(LineColor line)
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
    }
    
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
    
    private void setSwitchArray(List<Switch> switchArray)
    {
        /*List<List<Switch>> list;
        list = new ArrayList<>(controllerCount);*/
        //LineColor line = switchArray.get(0).lineID;
        LineColor line;
        
        for (Switch s : switchArray)
        {
            line = s.lineID;
            for (TrackController tc : this.controllers)
            {
                if (tc.getLine() == s.lineID && ( tc.containsBlock(s.approachBlock) || tc.containsBlock(s.divergentBlock) || tc.containsBlock(s.straightBlock) ))
                {
                    tc.addSwitch(s);
                   // System.out.println("Adding switch with ID: " + s.switchID + " to TC: " + tc.getId());
                }
                      
            }
        }
        
      /*  for (TrackController tc : this.controllers)
        {
            if (tc.getLine() == line)
            {
                tc.setSwitchInfo((ArrayList<Switch>) list.get(tc.getId()));
            }
        }*/
    }
    
    public ArrayList<BlockSignalBundle> getBlockSignalCommands()
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
    }
    
    public ArrayList<BlockInfoBundle> getBlockInfoCommands()
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
    }
    
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
    }
    
    private void configurePLCs()
    {
        for (TrackController tc : this.controllers)
        {
            tc.setPLC();
        }
    }
    
   
    
    
    
}
