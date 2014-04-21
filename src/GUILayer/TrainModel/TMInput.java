/******************************************************************************
 * 
 * TMInput class
 * 
 * Developed by AJility
 * April 2014
 * 
 * Contributers:
 *  Drew Winfield
 * 
 * DISCLAIMER:
 *   This document contains un-editable code generated by NetBeans.  Due to this
 *  un-editable code, this document does not follow the AJility coding standard.
 *
 *****************************************************************************/

package GUILayer.TrainModel;

import DataLayer.Train.TrainModel.PhysicsEngine;
import DataLayer.Train.TrainModel.TrainState;

public class TMInput extends javax.swing.JPanel {

    private PhysicsEngine physicsEngine;
    private TrainState trainState;
    
    /**
     * Creates new form PhysicsDisplay
     */
    public TMInput() {
        initComponents();
    }

    public void setPhysicsEngine(PhysicsEngine e)
    {
        physicsEngine = e;
    }
    
    public void setTrainState(TrainState s)
    {
        trainState = s;
    }
    
    public void setAutomatic()
    {
        eBrakeRequestButton.setEnabled(false);
        engineFaultInputCheckBox.setEnabled(false);
        sBrakeFaultInputCheckBox.setEnabled(false);
        signalFaultInputCheckBox.setEnabled(false);
        temperatureInputApplyButton.setEnabled(false);
        eBrakeFaultInputCheckBox.setEnabled(false);
    }
    public void setManual()
    {
        eBrakeRequestButton.setEnabled(true);
        engineFaultInputCheckBox.setEnabled(true);
        sBrakeFaultInputCheckBox.setEnabled(true);
        signalFaultInputCheckBox.setEnabled(true);
        temperatureInputApplyButton.setEnabled(true);
        eBrakeFaultInputCheckBox.setEnabled(true);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inputsLabel = new javax.swing.JLabel();
        temperatureInputLabel = new javax.swing.JLabel();
        faultInputLabel = new javax.swing.JLabel();
        temperatureInputTextField = new javax.swing.JTextField();
        temperatureInputApplyButton = new javax.swing.JButton();
        degreesFahLabel = new javax.swing.JLabel();
        engineFaultInputCheckBox = new javax.swing.JCheckBox();
        signalFaultInputCheckBox = new javax.swing.JCheckBox();
        sBrakeFaultInputCheckBox = new javax.swing.JCheckBox();
        eBrakeFaultInputCheckBox = new javax.swing.JCheckBox();
        eBrakeRequestButton = new javax.swing.JToggleButton();
        eBrakeRequestLabel = new javax.swing.JLabel();

        inputsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        inputsLabel.setText("Inputs");

        temperatureInputLabel.setText("Temperature");

        faultInputLabel.setText("Fault");

        temperatureInputTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                temperatureInputTextFieldActionPerformed(evt);
            }
        });

        temperatureInputApplyButton.setText("Apply");
        temperatureInputApplyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                temperatureInputApplyButtonActionPerformed(evt);
            }
        });

        degreesFahLabel.setText("ºF");

        engineFaultInputCheckBox.setText("Engine Failure");
        engineFaultInputCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                engineFaultInputCheckBoxActionPerformed(evt);
            }
        });

        signalFaultInputCheckBox.setText("Signal Failure");
        signalFaultInputCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signalFaultInputCheckBoxActionPerformed(evt);
            }
        });

        sBrakeFaultInputCheckBox.setText("Service Brake Failure");
        sBrakeFaultInputCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sBrakeFaultInputCheckBoxActionPerformed(evt);
            }
        });

        eBrakeFaultInputCheckBox.setText("Emergency Brake Failure");
        eBrakeFaultInputCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eBrakeFaultInputCheckBoxActionPerformed(evt);
            }
        });

        eBrakeRequestButton.setText("Request");
        eBrakeRequestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eBrakeRequestButtonActionPerformed(evt);
            }
        });

        eBrakeRequestLabel.setText("Passenger EBrake Request");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(temperatureInputLabel)
                    .addComponent(faultInputLabel)
                    .addComponent(eBrakeRequestLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(temperatureInputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(degreesFahLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(temperatureInputApplyButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(eBrakeRequestButton)
                            .addComponent(signalFaultInputCheckBox)
                            .addComponent(engineFaultInputCheckBox)
                            .addComponent(sBrakeFaultInputCheckBox)
                            .addComponent(eBrakeFaultInputCheckBox))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addComponent(inputsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(temperatureInputLabel)
                    .addComponent(temperatureInputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(temperatureInputApplyButton)
                    .addComponent(degreesFahLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(faultInputLabel)
                    .addComponent(engineFaultInputCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(signalFaultInputCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sBrakeFaultInputCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(eBrakeFaultInputCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eBrakeRequestButton)
                    .addComponent(eBrakeRequestLabel))
                .addContainerGap(12, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void temperatureInputTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_temperatureInputTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_temperatureInputTextFieldActionPerformed

    private void temperatureInputApplyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_temperatureInputApplyButtonActionPerformed
        double newTemp = Double.parseDouble(temperatureInputTextField.getText());
        newTemp = ((newTemp - 32) * 5) / 9; // convert to degrees Celcius
        trainState.setTemperature((int) newTemp);
    }//GEN-LAST:event_temperatureInputApplyButtonActionPerformed

    private void eBrakeRequestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eBrakeRequestButtonActionPerformed
        physicsEngine.setPassengerEBrakeRequest(eBrakeRequestButton.isSelected());
    }//GEN-LAST:event_eBrakeRequestButtonActionPerformed

    private void engineFaultInputCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_engineFaultInputCheckBoxActionPerformed
        physicsEngine.setEngineFault(engineFaultInputCheckBox.isSelected());
    }//GEN-LAST:event_engineFaultInputCheckBoxActionPerformed

    private void signalFaultInputCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signalFaultInputCheckBoxActionPerformed
        physicsEngine.setSignalFault(signalFaultInputCheckBox.isSelected());
    }//GEN-LAST:event_signalFaultInputCheckBoxActionPerformed

    private void sBrakeFaultInputCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sBrakeFaultInputCheckBoxActionPerformed
        physicsEngine.setSBrakeFault(sBrakeFaultInputCheckBox.isSelected());
    }//GEN-LAST:event_sBrakeFaultInputCheckBoxActionPerformed

    private void eBrakeFaultInputCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eBrakeFaultInputCheckBoxActionPerformed
        physicsEngine.setEBrakeFault(eBrakeFaultInputCheckBox.isSelected());
    }//GEN-LAST:event_eBrakeFaultInputCheckBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel degreesFahLabel;
    private javax.swing.JCheckBox eBrakeFaultInputCheckBox;
    private javax.swing.JToggleButton eBrakeRequestButton;
    private javax.swing.JLabel eBrakeRequestLabel;
    private javax.swing.JCheckBox engineFaultInputCheckBox;
    private javax.swing.JLabel faultInputLabel;
    private javax.swing.JLabel inputsLabel;
    private javax.swing.JCheckBox sBrakeFaultInputCheckBox;
    private javax.swing.JCheckBox signalFaultInputCheckBox;
    private javax.swing.JButton temperatureInputApplyButton;
    private javax.swing.JLabel temperatureInputLabel;
    private javax.swing.JTextField temperatureInputTextField;
    // End of variables declaration//GEN-END:variables
}
