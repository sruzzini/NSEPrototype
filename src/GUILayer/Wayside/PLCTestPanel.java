/******************************************************************************
 * 
 * PLCTestPanel class
 * 
 * Developed by AJility
 * April 2014
 * 
 * Contributers:
 *  Nathan Hachten
 * 
 * DISCLAIMER:
 *   This document contains un-editable code generated by NetBeans.  Due to this
 *  un-editable code, this document does not follow the AJility coding standard.
 *
 *****************************************************************************/

package GUILayer.Wayside;

import DataLayer.Bundles.BlockSignalBundle;
import DataLayer.TrackModel.Block;
import DataLayer.TrackModel.Switch;
import DataLayer.Wayside.Commands;
import DataLayer.Wayside.TrackController;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.JOptionPane;

public class PLCTestPanel extends javax.swing.JPanel {
    TrackController controller;
    ArrayList<TrackController> controllers;
    /**
     * Creates new form PLCTestPanel
     */
    public PLCTestPanel() {
        controllers = new ArrayList<>();
        initComponents();
    }
    
    public void addController(TrackController controller)
    {
        TrackController newController = new TrackController(controller.getId(), controller.getLine(), controller.getBlockNums());
        Block newBlock;
        Switch newSwitch;
        
        for (Block b : controller.getBlockInfo())
        {
            newBlock = new Block(b.getBlockID(), b.next, b.prev, b.getLength(), b.getSpeedLimit(), b.getElevation(), b.getCumElev(), b.getGradient(), b.isUnderground(), b.hasLight(),
            b.hasRRXing(), b.hasStation(), b.hasTswitch());
            /*
            blockArray.add(newBlock);
            blockTable.put(newBlock.getBlockID(), newBlock);*/
            newController.addBlock(newBlock);
           
        }
        
        for (Switch s : controller.getSwitchInfo())
        {
            newSwitch = new Switch(s.lineID, s.switchID, s.approachBlock, s.straightBlock, s.divergentBlock, s.straight);
           // switchArray.add(newSwitch);
            newController.addSwitch(newSwitch);
        }
        
        newController.setPLC();
        
        this.controllers.add(newController);
        
      
    }
    
    public void selectController(int n)
    {
        this.controller = this.controllers.get(n);
        refreshComponents();
    }
    
    private void refreshComponents()
    {
        int[] blockNums = this.controller.getBlockNums();
        final String[] blockNumsString = new String[blockNums.length];
        int k = 0;
        
        for (int n : blockNums)
        {
         blockNumsString[k] = Integer.toString(blockNums[k++]);   
        }
        blockSelectComboBox.setModel(new javax.swing.DefaultComboBoxModel(blockNumsString));
        blockOccList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = blockNumsString;
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        blockSelectComboBox = new javax.swing.JComboBox();
        inputAuthorityText = new javax.swing.JTextField();
        inputSpeedText = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        commandsOutput = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        sendSignalCheckBox = new javax.swing.JCheckBox();
        runButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        inputDestinationText = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        blockOccList = new javax.swing.JList();

        jLabel1.setText("PLC Test Panel");

        jLabel2.setText("Block Number:");

        jLabel3.setText("Authority:");

        jLabel4.setText("Suggested Speed (units?):");

        jLabel5.setText("Track Occupancies:");

        blockSelectComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        commandsOutput.setColumns(20);
        commandsOutput.setRows(5);
        jScrollPane2.setViewportView(commandsOutput);

        jLabel6.setText("Resultant Commands");

        sendSignalCheckBox.setText("Send Signal?");
        sendSignalCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendSignalCheckBoxActionPerformed(evt);
            }
        });

        runButton.setText("Run Program");
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });

        jLabel7.setText("Destination:");

        blockOccList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        blockOccList.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        jScrollPane3.setViewportView(blockOccList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(sendSignalCheckBox)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel7))
                                        .addGap(32, 32, 32)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(blockSelectComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(inputAuthorityText)
                                            .addComponent(inputSpeedText)
                                            .addComponent(inputDestinationText)))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(runButton))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(runButton)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(blockSelectComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(inputAuthorityText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(inputSpeedText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(inputDestinationText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendSignalCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sendSignalCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendSignalCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sendSignalCheckBoxActionPerformed

    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
        // TODO add your handling code here:
        //this will take the inputted occupancies and run the plc program based on these
        
        int[] selectedIndicies = this.blockOccList.getSelectedIndices();
        int[] selectedBlocks = new int[selectedIndicies.length];
        int k=0;
        String display;
        int blockNum, authority, destination;
        double speed;
        BlockSignalBundle signal;
        
        if (this.sendSignalCheckBox.isSelected())
        {
            try 
            {
            blockNum = this.controller.getBlockNums()[this.blockSelectComboBox.getSelectedIndex()];
            speed = Double.parseDouble(this.inputSpeedText.getText());
            authority = Integer.parseInt(this.inputSpeedText.getText());
            destination = Integer.parseInt(this.inputDestinationText.getText());
            if (speed < 0 || authority < 0 || destination < 0)
            {
                throw new NumberFormatException();
            }
            signal = new BlockSignalBundle(authority, destination, speed, blockNum, this.controller.getLine());
            this.controller.sendTravelSignal2(signal, 0);
            }
            catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog(null, "Incorrect input format. Please enter correct input.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        for (int n : selectedIndicies)
        {
            selectedBlocks[k++] = Integer.parseInt((String) this.blockOccList.getModel().getElementAt(n));
        }
        Hashtable<Integer, Block> blockTable = this.controller.getBlockTable();
        ArrayList<Block> blockArray = this.controller.getBlockInfo();
        
        for (Block b : blockArray)
        {
            b.setOccupied(false);
        }
        
        for (int occupiedBlock : selectedBlocks)
        {
            blockTable.get(occupiedBlock).setOccupied(true);
        }
        
        Commands results = this.controller.plcProgram.runPLCProgram();
        
        display = results.toString();
        
        for (BlockSignalBundle b : this.controller.getCommandSignalQueue())
        {
            display += b.toString();
        }
        
        this.controller.emptyCommandQueues();
        
        this.commandsOutput.setText(display);
        
        
    }//GEN-LAST:event_runButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList blockOccList;
    private javax.swing.JComboBox blockSelectComboBox;
    private javax.swing.JTextArea commandsOutput;
    private javax.swing.JTextField inputAuthorityText;
    private javax.swing.JTextField inputDestinationText;
    private javax.swing.JTextField inputSpeedText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton runButton;
    private javax.swing.JCheckBox sendSignalCheckBox;
    // End of variables declaration//GEN-END:variables
}
