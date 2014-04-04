/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataLayer.CTC;

import DataLayer.EnumTypes.LineColor;
import java.util.ArrayList;



/**
 *
 * @author micha_000
 */
public class CTCGUI extends javax.swing.JFrame
{
    private ArrayList<String[]> green;
    private ArrayList<String[]> red;
    
    private ArrayList<String[]> blockClosings;
    private int closings;
    
    private String[] greenSections;
    private String[] redSections;
    
    private TrainsClass[] trains; 
    
    public CTC CTCOffice;
    
    public CTCGUI()
    {
         CTCOffice = new CTC(10);
         disableManualFunctions();       
         initComponents();
    }
    
    
    private void disableManualFunctions()
    {
        //Disable all the buttons and drop down menus in the Block Closing section
        blockClosingSubmitButton.setEnabled(false);
        blockClosingLineDrop.setEnabled(false);
        blockClosingSectionDrop.setEnabled(false);
        blockClosingBlockDrop.setEnabled(false);
        blockClosingOpenRadio.setEnabled(false);
        blockClosingCloseRadio.setEnabled(false);
        
        //Disable all the buttons and drop down menus in the Route Train section
        routeTrainSubmitButton.setEnabled(false);
        routeTrainSpeedText.setEnabled(false);
        routeTrainBlockDrop.setEnabled(false);
        routeTrainSectionDrop.setEnabled(false);
        routeTrainTrainDrop.setEnabled(false);
        routeTrainLineDrop.setEnabled(false);
        
        //Disable all the buttons and drop down menus in the Change Switch Position section
        changeSwitchPositionSubmitButton.setEnabled(false);
        changeSwitchPositionStraightRadio.setEnabled(false);
        changeSwitchPositionDivergentRadio.setEnabled(false);
        changeSwitchPositionSwitchIDDrop.setEnabled(false);
        changeSwitchPositionLineDrop.setEnabled(false);

        
    }
    
    private void enableManualFunctions()
    {
        
        //Disable all the buttons and drop down menus in the Block Closing section
        blockClosingSubmitButton.setEnabled(true);
        blockClosingLineDrop.setEnabled(true);
        blockClosingSectionDrop.setEnabled(true);
        blockClosingBlockDrop.setEnabled(true);
        blockClosingOpenRadio.setEnabled(true);
        blockClosingCloseRadio.setEnabled(true);
        
        //Disable all the buttons and drop down menus in the Route Train section
        routeTrainSubmitButton.setEnabled(true);
        routeTrainSpeedText.setEnabled(true);
        routeTrainBlockDrop.setEnabled(true);
        routeTrainSectionDrop.setEnabled(true);
        routeTrainTrainDrop.setEnabled(true);
        routeTrainLineDrop.setEnabled(true);
        
        //Disable all the buttons and drop down menus in the Change Switch Position section
        changeSwitchPositionSubmitButton.setEnabled(true);
        changeSwitchPositionStraightRadio.setEnabled(true);
        changeSwitchPositionDivergentRadio.setEnabled(true);
        changeSwitchPositionSwitchIDDrop.setEnabled(true);
        changeSwitchPositionLineDrop.setEnabled(true);
    }

    private void systemManualRadioActionPerformed(java.awt.event.ActionEvent evt) 
    {//GEN-FIRST:event_systemManualRadioActionPerformed
        // TODO add your handling code here:
        enableManualFunctions();
        
    }//GEN-LAST:event_systemManualRadioActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {//GEN-FIRST:event_stopButtonActionPerformed
        // TODO add your handling code here:
        systemManualRadio.setEnabled(true);
        systemAutomaticRadio.setEnabled(true);
        
    }//GEN-LAST:event_stopButtonActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {
        // TODO add your handling code here:
        systemManualRadio.setEnabled(false);
        systemAutomaticRadio.setEnabled(false);    
    }        
    
    private void blockClosingLineDropActionPerformed(java.awt.event.ActionEvent evt) 
    {//GEN-FIRST:event_blockClosingLineDropActionPerformed
        // TODO add your handling code here:
        String x = String.valueOf(blockClosingLineDrop.getSelectedItem());
        if(x.equals("RED"))
        {
          blockClosingSectionDrop.setModel(new javax.swing.DefaultComboBoxModel(redSections));  
        }
        else
        {
          blockClosingSectionDrop.setModel(new javax.swing.DefaultComboBoxModel(greenSections));    
        }
        
        //blockClosingSectionDrop.setModel(new DefaultComboBoxModel(greenSections));
    }//GEN-LAST:event_blockClosingLineDropActionPerformed
    
    private void blockClosingSectionDropActionPerformed(java.awt.event.ActionEvent evt) 
    {                                                     
        ArrayList<String> blocks = new ArrayList<String>();
        blocks.add("Block #");
        
        String line = String.valueOf(blockClosingLineDrop.getSelectedItem());
        String section = String.valueOf(blockClosingSectionDrop.getSelectedItem()).toUpperCase();
        if(line.toUpperCase().equals("RED"))
        {
            for(String[] s: red)
            {
                if(s[0].toUpperCase().equals(section))
                {
                    blocks.add(s[1]);
                }
            }            
            blockClosingBlockDrop.setModel(new javax.swing.DefaultComboBoxModel(blocks.toArray(new String[0])));  
        }
        else
        {
            for(String[] s: green)
            {
                if(s[0].toUpperCase().equals(section))
                {
                    blocks.add(s[1]);
                }
            }            
            blockClosingBlockDrop.setModel(new javax.swing.DefaultComboBoxModel(blocks.toArray(new String[0])));
        }
    }
    
    private void blockClosingBlockDropActionPerformed(java.awt.event.ActionEvent evt) 
    {//GEN-FIRST:event_blockClosingBlockDropActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_blockClosingBlockDropActionPerformed
    
    public void routeTrainSubmitButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        String train = String.valueOf(routeTrainTrainDrop.getSelectedItem()).toUpperCase();
        String line = String.valueOf(routeTrainLineDrop.getSelectedItem()).toUpperCase();
        String section = String.valueOf(routeTrainSectionDrop.getSelectedItem()).toUpperCase();
        String block = String.valueOf(routeTrainBlockDrop.getSelectedItem()).toUpperCase();
        
        LineColor temp;
        //trains[Integer.parseInt(train)] = new TrainsClass(line, trains[Integer.parseInt(train)].section_current,  trains[Integer.parseInt(train)].block, calculateAuthority(trains[Integer.parseInt(train)]), Double.parseDouble(routeTrainSpeedText.getText()), block, section);
        if(line.equals("RED"))
        {
            temp = LineColor.RED;
        }
        else
        {
            temp = LineColor.GREEN;
        }
        //CTCOffice.routeTrainSubmit()
        //CTCOfffice.trains[Integer.parseInt(train)] = new TrainsClass(temp, trains[Integer.parseInt(train)].section_current,  trains[Integer.parseInt(train)].block, CTC.calculateAuthority(trains[Integer.parseInt(train)]), Double.parseDouble(routeTrainSpeedText.getText()), block, section);
        
     }

    private void blockClosingSubmitButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {//GEN-FIRST:event_blockClosingSubmitButtonActionPerformed
        // TODO add your handling code here:
        
        int index;
        String line = String.valueOf(blockClosingLineDrop.getSelectedItem()).toUpperCase();
        String section = String.valueOf(blockClosingSectionDrop.getSelectedItem()).toUpperCase();
        String block = String.valueOf(blockClosingBlockDrop.getSelectedItem()).toUpperCase();
        
        
        System.out.println(line + ", " + section + ", " + block + " | Open: " + blockClosingOpenRadio.isSelected());
        if(blockClosingOpenRadio.isSelected() || blockClosingCloseRadio.isSelected() && (!section.equals("SECTION") && !line.equals("LINE") && !block.equals("BLOCK #")))
        {
            index = CTCOffice.isBlockAlreadyClosed(new String[] {line,block});
            if(!blockClosingOpenRadio.isSelected())
            {
                if(index == -1)
                {
                    String[][] blocks = new String[blockClosings.size()+1][2];
                    //System.out.println(blocks.length);
                    if(blockClosings.size() > 0 && closings > 0)
                    {
                        for(int i = 0; i < blocks.length-1; i++)
                        {
                            blocks[i] = new String[] {blockClosings.get(i)[0],blockClosings.get(i)[1]};
                        }
                    }
                    
                    blocks[closings] = new String[] {line,block};
                    blockClosingTable.setModel(new javax.swing.table.DefaultTableModel(blocks,new String [] {"Line", "Block"}));
                    blockClosings.add(new String[] {line,block});
                    closings++;
                    System.out.println("Added, new blockClosing size:" + blockClosings.size() + " | Number of Closings: " + closings);
                }
            }
            else
            {
                if(index != -1)
                {
                    closings--;
                    //blockClosings.remove(new String[] {line,block});
                    //System.out.println(blockClosings.removeAll(new String[] {line,block}));
                    blockClosings.remove(index);
                    String[][] blocks = new String[closings][2];
                    //System.out.println(blocks.length);
                    if(blockClosings.size() > 0 && closings > 0)
                    {
                        for(int i = 0; i < blocks.length; i++)
                        {
                            blocks[i] = new String[] {blockClosings.get(i)[0],blockClosings.get(i)[1]};
                        }
                    }
       
                    blockClosingTable.setModel(new javax.swing.table.DefaultTableModel(blocks,new String [] {"Line", "Block"}));            
                    //System.out.println("Removed, new blockClosing size:" + blockClosings.size() + " | Number of Closings: " + closings);
                }
            }
        }
        //for(String[] s: blockClosings)
        //{
        //    System.out.println(s[0] + " " + s[1]);
        //}
    }//GEN-LAST:event_blockClosingSubmitButtonActionPerformed

    private void changeSwitchPositionLineDropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeSwitchPositionLineDropActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_changeSwitchPositionLineDropActionPerformed

    private void blockClosingCloseRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blockClosingCloseRadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_blockClosingCloseRadioActionPerformed

    private void routeTrainBlockDropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_routeTrainBlockDropActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_routeTrainBlockDropActionPerformed

    private void routeTrainSectionDropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_routeTrainSectionDropActionPerformed
        // TODO add your handling code here:
        ArrayList<String> blocks = new ArrayList<String>();
        blocks.add("Block #");
        
        String line = String.valueOf(routeTrainLineDrop.getSelectedItem());
        String section = String.valueOf(routeTrainSectionDrop.getSelectedItem()).toUpperCase();
        if(line.toUpperCase().equals("RED"))
        {
            for(String[] s: red)
            {
                if(s[0].toUpperCase().equals(section))
                {
                    blocks.add(s[1]);
                }
            }            
            routeTrainBlockDrop.setModel(new javax.swing.DefaultComboBoxModel(blocks.toArray(new String[0])));  
        }
        else
        {
            for(String[] s: green)
            {
                if(s[0].toUpperCase().equals(section))
                {
                    blocks.add(s[1]);
                }
            }            
            routeTrainBlockDrop.setModel(new javax.swing.DefaultComboBoxModel(blocks.toArray(new String[0])));
        }
    }//GEN-LAST:event_routeTrainSectionDropActionPerformed

    private void systemAutomaticRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_systemAutomaticRadioActionPerformed
        // TODO add your handling code here:
        disableManualFunctions();
    }//GEN-LAST:event_systemAutomaticRadioActionPerformed

    private void routeTrainSpeedTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_routeTrainSpeedTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_routeTrainSpeedTextActionPerformed

    private void routeTrainLineDropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_routeTrainLineDropActionPerformed
        // TODO add your handling code here:
        String x = String.valueOf(routeTrainLineDrop.getSelectedItem());
        if(x.equals("RED"))
        {
          routeTrainSectionDrop.setModel(new javax.swing.DefaultComboBoxModel(redSections));  
        }
        else
        {
          routeTrainSectionDrop.setModel(new javax.swing.DefaultComboBoxModel(greenSections));    
        }
    }//GEN-LAST:event_routeTrainLineDropActionPerformed

    private void routeTrainTrainDropActionPerformed( java.awt.event.ActionEvent evt)
    {
        String x = String.valueOf(routeTrainTrainDrop.getSelectedItem());
        if(trains[Integer.parseInt(x)].block.equals("Yard"))
        {
          routeTrainLineDrop.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Line", "RED","GREEN"}));  
        }
        else
        {
          routeTrainLineDrop.setModel(new javax.swing.DefaultComboBoxModel(new String[] {trains[Integer.parseInt(x)].lineColor()}));    
        }        
    }

    
    private void initComponents() 
    {

        blockClosingGroup = new javax.swing.ButtonGroup();
        changeSwitchPositionGroup = new javax.swing.ButtonGroup();
        systemGroup = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        trainLocationTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        blockClosingTable = new javax.swing.JTable();
        blockClosingLabel = new javax.swing.JLabel();
        trainLocationLabel = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        trainDestinationTable = new javax.swing.JTable();
        trainDestinationLabel = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        switchTabel = new javax.swing.JTable();
        switchLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        blockClosingSectionDrop = new javax.swing.JComboBox();
        blockClosingBlockDrop = new javax.swing.JComboBox();
        blockClosingLineDrop = new javax.swing.JComboBox();
        blockClosingManualLabel = new javax.swing.JLabel();
        blockClosingSubmitButton = new javax.swing.JButton();
        blockClosingOpenRadio = new javax.swing.JRadioButton();
        blockClosingCloseRadio = new javax.swing.JRadioButton();
        routeTrainSpeedPanel = new javax.swing.JPanel();
        routeTrainLabel = new javax.swing.JLabel();
        routeTrainTrainDrop = new javax.swing.JComboBox();
        routeTrainSectionDrop = new javax.swing.JComboBox();
        routeTrainBlockDrop = new javax.swing.JComboBox();
        routeTrainAuthorityLabel = new javax.swing.JLabel();
        routeTrainSubmitButton = new javax.swing.JButton();
        routeTrainSpeedText = new javax.swing.JTextField();
        routeTrainSpeedLabel = new javax.swing.JLabel();
        routeTrainAuthorityValueLabel = new javax.swing.JLabel();
        routeTrainLineDrop = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        changeSwitchPositionStraightRadio = new javax.swing.JRadioButton();
        changeSwitchPositionDivergentRadio = new javax.swing.JRadioButton();
        changeSwitchPositionLineDrop = new javax.swing.JComboBox();
        changeSwitchPositionSwitchIDDrop = new javax.swing.JComboBox();
        changeSwitchPositionSubmitButton = new javax.swing.JButton();
        changeSwitchPositionLabel = new javax.swing.JLabel();
        systemAutomaticRadio = new javax.swing.JRadioButton();
        systemManualRadio = new javax.swing.JRadioButton();
        stopButton = new javax.swing.JButton();
        timeLabel = new javax.swing.JLabel();
        trackLayout = new javax.swing.JLabel();
        startButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        //trainLocationTable.setModel(new javax.swing.table.DefaultTableModel(CTCOffice.returnTrainInfoArray(),new String [] {"Train", "Line","Section","Block"}));
        
        /*trainLocationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Train", "Line", "Section", "Block"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });*/
        jScrollPane1.setViewportView(trainLocationTable);

        blockClosingTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Line", "Block"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(blockClosingTable);

        blockClosingLabel.setText("Block Closing");

        trainLocationLabel.setText("Train Locations");

        trainDestinationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Train", "Authority", "Line", "Section", "Block"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(trainDestinationTable);

        trainDestinationLabel.setText("Train Destination");

        switchTabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Line", "Block:Block", "Position"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane6.setViewportView(switchTabel);

        switchLabel.setText("Switches");

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        blockClosingSectionDrop.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Section" }));
        blockClosingSectionDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blockClosingSectionDropActionPerformed(evt);
            }
        });

        blockClosingBlockDrop.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Block #" }));
        blockClosingBlockDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blockClosingBlockDropActionPerformed(evt);
            }
        });

        blockClosingLineDrop.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Line", "GREEN", "RED", " " }));
        blockClosingLineDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blockClosingLineDropActionPerformed(evt);
            }
        });

        blockClosingManualLabel.setText("Block Closing");

        blockClosingSubmitButton.setText("Submit");
        blockClosingSubmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blockClosingSubmitButtonActionPerformed(evt);
            }
        });

        blockClosingGroup.add(blockClosingOpenRadio);
        blockClosingOpenRadio.setText("Open");

        blockClosingGroup.add(blockClosingCloseRadio);
        blockClosingCloseRadio.setText("Close");
        blockClosingCloseRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blockClosingCloseRadioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(blockClosingManualLabel))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(blockClosingBlockDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(blockClosingOpenRadio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(blockClosingCloseRadio))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(blockClosingLineDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(blockClosingSectionDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(blockClosingSubmitButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(blockClosingManualLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(blockClosingLineDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(blockClosingSectionDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(blockClosingBlockDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(blockClosingOpenRadio)
                    .addComponent(blockClosingCloseRadio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blockClosingSubmitButton)
                .addContainerGap())
        );

        routeTrainSpeedPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        routeTrainLabel.setText("Route Trains");

        routeTrainTrainDrop.setModel(new javax.swing.DefaultComboBoxModel(CTCOffice.returnTrainNumberArray()));
        routeTrainTrainDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                routeTrainTrainDropActionPerformed(evt);
            }
        });

        routeTrainSectionDrop.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Section" }));
        routeTrainSectionDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                routeTrainSectionDropActionPerformed(evt);
            }
        });

        routeTrainBlockDrop.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Block #" }));
        routeTrainBlockDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                routeTrainBlockDropActionPerformed(evt);
            }
        });

        routeTrainAuthorityLabel.setText("Authority:");

        routeTrainSubmitButton.setText("Submit");
        routeTrainSubmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                routeTrainSubmitButtonActionPerformed(evt);
            }
        });

        routeTrainSpeedText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                routeTrainSpeedTextActionPerformed(evt);
            }
        });

        routeTrainSpeedLabel.setText("Speed:");

        routeTrainAuthorityValueLabel.setText("0");

        routeTrainLineDrop.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Line" }));
        routeTrainLineDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                routeTrainLineDropActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout routeTrainSpeedPanelLayout = new javax.swing.GroupLayout(routeTrainSpeedPanel);
        routeTrainSpeedPanel.setLayout(routeTrainSpeedPanelLayout);
        routeTrainSpeedPanelLayout.setHorizontalGroup(
            routeTrainSpeedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(routeTrainSpeedPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(routeTrainSpeedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, routeTrainSpeedPanelLayout.createSequentialGroup()
                        .addComponent(routeTrainLineDrop, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, routeTrainSpeedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(routeTrainSpeedPanelLayout.createSequentialGroup()
                            .addComponent(routeTrainTrainDrop, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, routeTrainSpeedPanelLayout.createSequentialGroup()
                            .addGroup(routeTrainSpeedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(routeTrainSubmitButton)
                                .addGroup(routeTrainSpeedPanelLayout.createSequentialGroup()
                                    .addGroup(routeTrainSpeedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(routeTrainAuthorityLabel)
                                        .addComponent(routeTrainSpeedLabel))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(routeTrainSpeedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(routeTrainSpeedText, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(routeTrainAuthorityValueLabel))
                                    .addGap(8, 8, 8)))
                            .addGap(19, 19, 19))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, routeTrainSpeedPanelLayout.createSequentialGroup()
                            .addComponent(routeTrainLabel)
                            .addGap(42, 42, 42))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, routeTrainSpeedPanelLayout.createSequentialGroup()
                            .addComponent(routeTrainSectionDrop, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(routeTrainBlockDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(11, 11, 11)))))
        );
        routeTrainSpeedPanelLayout.setVerticalGroup(
            routeTrainSpeedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(routeTrainSpeedPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(routeTrainLabel)
                .addGap(7, 7, 7)
                .addGroup(routeTrainSpeedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(routeTrainTrainDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(routeTrainLineDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(routeTrainSpeedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(routeTrainBlockDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(routeTrainSectionDrop, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(routeTrainSpeedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(routeTrainAuthorityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(routeTrainAuthorityValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(routeTrainSpeedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(routeTrainSpeedText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(routeTrainSpeedLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(routeTrainSubmitButton)
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        changeSwitchPositionGroup.add(changeSwitchPositionStraightRadio);
        changeSwitchPositionStraightRadio.setText("Straight");

        changeSwitchPositionGroup.add(changeSwitchPositionDivergentRadio);
        changeSwitchPositionDivergentRadio.setText("Divergent");

        changeSwitchPositionLineDrop.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Line" }));
        changeSwitchPositionLineDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeSwitchPositionLineDropActionPerformed(evt);
            }
        });

        changeSwitchPositionSwitchIDDrop.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Switch ID" }));

        changeSwitchPositionSubmitButton.setText("Submit");

        changeSwitchPositionLabel.setText("Change Switch Position");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(changeSwitchPositionStraightRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(changeSwitchPositionDivergentRadio))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(changeSwitchPositionSubmitButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(changeSwitchPositionLabel))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(changeSwitchPositionLineDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(changeSwitchPositionSwitchIDDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(changeSwitchPositionLabel)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(changeSwitchPositionSwitchIDDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(changeSwitchPositionLineDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changeSwitchPositionStraightRadio)
                    .addComponent(changeSwitchPositionDivergentRadio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(changeSwitchPositionSubmitButton)
                .addContainerGap())
        );

        systemGroup.add(systemAutomaticRadio);
        systemAutomaticRadio.setText("Automatic");
        systemAutomaticRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                systemAutomaticRadioActionPerformed(evt);
            }
        });

        systemGroup.add(systemManualRadio);
        systemManualRadio.setText("Manual");
        systemManualRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                systemManualRadioActionPerformed(evt);
            }
        });

        stopButton.setText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        timeLabel.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        timeLabel.setText("11:00am");

        trackLayout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ctcoffice/Untitled.png"))); // NOI18N

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(trackLayout)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(systemManualRadio)
                        .addGap(18, 18, 18)
                        .addComponent(systemAutomaticRadio)
                        .addGap(14, 14, 14)
                        .addComponent(startButton)
                        .addGap(18, 18, 18)
                        .addComponent(stopButton)
                        .addGap(52, 52, 52)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(129, 129, 129)
                                .addComponent(trainDestinationLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(123, 123, 123)
                                        .addComponent(trainLocationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(4, 4, 4))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(switchLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(blockClosingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(routeTrainSpeedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(trainDestinationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(trainLocationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(switchLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(blockClosingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(routeTrainSpeedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(25, 25, 25)
                            .addComponent(trackLayout)
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(systemManualRadio)
                                .addComponent(systemAutomaticRadio)
                                .addComponent(stopButton)
                                .addComponent(startButton)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents    

        public static void main(String args[]) 
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CTC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CTC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CTC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CTC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CTCGUI().setVisible(true);
                
            }
        });
    }
    
    private javax.swing.JComboBox blockClosingBlockDrop;
    private javax.swing.JRadioButton blockClosingCloseRadio;
    private javax.swing.ButtonGroup blockClosingGroup;
    private javax.swing.JLabel blockClosingLabel;
    private javax.swing.JComboBox blockClosingLineDrop;
    private javax.swing.JLabel blockClosingManualLabel;
    private javax.swing.JRadioButton blockClosingOpenRadio;
    private javax.swing.JComboBox blockClosingSectionDrop;
    private javax.swing.JButton blockClosingSubmitButton;
    private javax.swing.JTable blockClosingTable;
    private javax.swing.JRadioButton changeSwitchPositionDivergentRadio;
    private javax.swing.ButtonGroup changeSwitchPositionGroup;
    private javax.swing.JLabel changeSwitchPositionLabel;
    private javax.swing.JComboBox changeSwitchPositionLineDrop;
    private javax.swing.JRadioButton changeSwitchPositionStraightRadio;
    private javax.swing.JButton changeSwitchPositionSubmitButton;
    private javax.swing.JComboBox changeSwitchPositionSwitchIDDrop;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel routeTrainAuthorityLabel;
    private javax.swing.JLabel routeTrainAuthorityValueLabel;
    private javax.swing.JComboBox routeTrainBlockDrop;
    private javax.swing.JLabel routeTrainLabel;
    private javax.swing.JComboBox routeTrainLineDrop;
    private javax.swing.JComboBox routeTrainSectionDrop;
    private javax.swing.JLabel routeTrainSpeedLabel;
    private javax.swing.JPanel routeTrainSpeedPanel;
    private javax.swing.JTextField routeTrainSpeedText;
    private javax.swing.JButton routeTrainSubmitButton;
    private javax.swing.JComboBox routeTrainTrainDrop;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JLabel switchLabel;
    private javax.swing.JTable switchTabel;
    private javax.swing.JRadioButton systemAutomaticRadio;
    private javax.swing.ButtonGroup systemGroup;
    private javax.swing.JRadioButton systemManualRadio;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JLabel trackLayout;
    private javax.swing.JLabel trainDestinationLabel;
    private javax.swing.JTable trainDestinationTable;
    private javax.swing.JLabel trainLocationLabel;
    private javax.swing.JTable trainLocationTable;
    
    
}
