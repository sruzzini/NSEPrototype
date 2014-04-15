/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUILayer;
import DataLayer.*;
import DataLayer.TrackModel.*;
import DataLayer.Train.*;
import DataLayer.Wayside.Wayside;
import java.util.Vector;

/**
 *
 * @author domino54
 */
public class NSEFrame extends javax.swing.JFrame implements Runnable {

    public NSE NSEObject;
    public boolean StartClicked;
    /**
     * Creates new form NSEFrame
     */
    public NSEFrame() {
        initComponents();
        this.NSEObject = null; //create new NSE object with 10 trains
        this.wallClock_Radio.setSelected(true);
        this.StartClicked = false;
    }
    
    public void SetNSE(NSE n)
    {
        this.NSEObject = n;
        this.trainPanel1.setTrain(this.NSEObject.Trains.get(0));
        //this.cTCGUI1.setCTCOffice(this.NSEObject.CTCOffice);
    }
    
    public void SetTrackModel(TrackModel t)
    {
        this.trackModelPanel1.setTrack(t);
    }
    
    public void SetWayside(Wayside w)
    {
       this.waysidePanel1.setWayside(w);
    }
    
    public void UpdateTrainSelectList()
    {
        Vector<String> list = new Vector<String>();
        for (Train t : NSEObject.Trains)
        {
            list.add(t.ToString());
        }
        trainSelectList.setListData(list);
        trainSelectList.setSelectedIndex(0);
    }
    
    public void run()
    {
        while (true)
        {
            this.trainPanel1.update(); 
            this.trackModelPanel1.updateDisplay();
            //this.cTCGUI1.update();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        speed_group = new javax.swing.ButtonGroup();
        wallClock_Radio = new javax.swing.JRadioButton();
        wallClock10_Radio = new javax.swing.JRadioButton();
        pause_button = new javax.swing.JButton();
        reset_button = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        CTC_panel = new javax.swing.JPanel();
        cTCGUI1 = new GUILayer.CTC.CTCGUI();
        Waysides_panel = new javax.swing.JPanel();
        waysidePanel1 = new GUILayer.Wayside.WaysidePanel();
        TrackModel_panel = new javax.swing.JPanel();
        trackModelPanel1 = new GUILayer.TrackModelPanel();
        Trains_panel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        trainPanel1 = new GUILayer.TrainPanel();
        trainSelectList = new javax.swing.JList();
        start_button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        speed_group.add(wallClock_Radio);
        wallClock_Radio.setText("Wall Clock");

        speed_group.add(wallClock10_Radio);
        wallClock10_Radio.setText("10x Wall Clock");

        pause_button.setText("Pause");

        reset_button.setText("Reset");

        javax.swing.GroupLayout CTC_panelLayout = new javax.swing.GroupLayout(CTC_panel);
        CTC_panel.setLayout(CTC_panelLayout);
        CTC_panelLayout.setHorizontalGroup(
            CTC_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CTC_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cTCGUI1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CTC_panelLayout.setVerticalGroup(
            CTC_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CTC_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cTCGUI1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("CTC", CTC_panel);

        javax.swing.GroupLayout Waysides_panelLayout = new javax.swing.GroupLayout(Waysides_panel);
        Waysides_panel.setLayout(Waysides_panelLayout);
        Waysides_panelLayout.setHorizontalGroup(
            Waysides_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Waysides_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(waysidePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                .addContainerGap())
        );
        Waysides_panelLayout.setVerticalGroup(
            Waysides_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Waysides_panelLayout.createSequentialGroup()
                .addComponent(waysidePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Waysides", Waysides_panel);

        javax.swing.GroupLayout TrackModel_panelLayout = new javax.swing.GroupLayout(TrackModel_panel);
        TrackModel_panel.setLayout(TrackModel_panelLayout);
        TrackModel_panelLayout.setHorizontalGroup(
            TrackModel_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TrackModel_panelLayout.createSequentialGroup()
                .addContainerGap(198, Short.MAX_VALUE)
                .addComponent(trackModelPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(144, 144, 144))
        );
        TrackModel_panelLayout.setVerticalGroup(
            TrackModel_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TrackModel_panelLayout.createSequentialGroup()
                .addComponent(trackModelPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Track Model", TrackModel_panel);

        jLabel1.setText("Trains");

        trainSelectList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        trainSelectList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                trainSelectListValueChanged(evt);
            }
        });

        javax.swing.GroupLayout Trains_panelLayout = new javax.swing.GroupLayout(Trains_panel);
        Trains_panel.setLayout(Trains_panelLayout);
        Trains_panelLayout.setHorizontalGroup(
            Trains_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Trains_panelLayout.createSequentialGroup()
                .addComponent(trainPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Trains_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(trainSelectList, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Trains_panelLayout.setVerticalGroup(
            Trains_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Trains_panelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(trainSelectList, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(Trains_panelLayout.createSequentialGroup()
                .addComponent(trainPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Trains", Trains_panel);

        start_button.setText("Start");
        start_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                start_Clicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 913, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(wallClock_Radio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(wallClock10_Radio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(start_button)
                        .addGap(2, 2, 2)
                        .addComponent(pause_button)
                        .addGap(12, 12, 12)
                        .addComponent(reset_button)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(wallClock_Radio)
                    .addComponent(wallClock10_Radio)
                    .addComponent(pause_button)
                    .addComponent(reset_button)
                    .addComponent(start_button))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void start_Clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_start_Clicked
        // TODO add your handling code here:
        this.StartClicked = true;
        new Thread(this.NSEObject).start();
    }//GEN-LAST:event_start_Clicked

    private void trainSelectListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_trainSelectListValueChanged
        this.trainPanel1.setTrain(this.NSEObject.Trains.get(trainSelectList.getSelectedIndices()[0]));
    }//GEN-LAST:event_trainSelectListValueChanged

    /**
     * @param args the command line arguments
     */
    /*public static void main(String args[]) {
        /* Set the Nimbus look and feel 
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        /*
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NSEFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NSEFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NSEFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NSEFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        */

        /* Create and display the form 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NSEFrame().setVisible(true);
            }
        });*/
        
        /*NSE nse = new NSE(1, 10);
        nse.RunAutomatic();
        
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CTC_panel;
    private javax.swing.JPanel TrackModel_panel;
    private javax.swing.JPanel Trains_panel;
    private javax.swing.JPanel Waysides_panel;
    private GUILayer.CTC.CTCGUI cTCGUI1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton pause_button;
    private javax.swing.JButton reset_button;
    private javax.swing.ButtonGroup speed_group;
    private javax.swing.JButton start_button;
    private GUILayer.TrackModelPanel trackModelPanel1;
    private GUILayer.TrainPanel trainPanel1;
    private javax.swing.JList trainSelectList;
    private javax.swing.JRadioButton wallClock10_Radio;
    private javax.swing.JRadioButton wallClock_Radio;
    private GUILayer.Wayside.WaysidePanel waysidePanel1;
    // End of variables declaration//GEN-END:variables
}
