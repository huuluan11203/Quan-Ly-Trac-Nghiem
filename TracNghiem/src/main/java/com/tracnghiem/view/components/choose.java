/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tracnghiem.view.components;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.tracnghiem.view.mainView;
import javax.swing.JDialog;

/**
 *
 * @author huulu
 */
public class choose extends javax.swing.JPanel {

    /**
     * Creates new form choose
     */
    public choose() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        luu = new javax.swing.JButton();
        luu1 = new javax.swing.JButton();

        jPanel1.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

        luu.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
        luu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        luu.setIcon(new FlatSVGIcon("icons/clipboard.svg", 30, 30)
        );
        luu.setText("Thủ công");
        luu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        luu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                luuActionPerformed(evt);
            }
        });

        luu1.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
        luu1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        luu1.setIcon(new FlatSVGIcon("icons/auto.svg", 30, 30)
        );
        luu1.setText("Tự động");
        luu1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        luu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                luu1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(luu1)
                .addGap(18, 18, 18)
                .addComponent(luu)
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(luu)
                    .addComponent(luu1))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void luuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_luuActionPerformed
        
        mainView.closeCurrentDialog(this);
        addTest addtest= new addTest(null, false);
        
        JDialog dialog = mainView.showCustomDialog1(null, addtest, "Thêm thủ công");
        
        dialog.setVisible(true);
    }//GEN-LAST:event_luuActionPerformed

    private void luu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_luu1ActionPerformed
        mainView.closeCurrentDialog(this);
        addTestAuto addtestAuto= new addTestAuto(null, false);
        
        JDialog dialog = mainView.showCustomDialog1(null, addtestAuto, "Thêm tự động");
        
        dialog.setVisible(true);
    }//GEN-LAST:event_luu1ActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton luu;
    private javax.swing.JButton luu1;
    // End of variables declaration//GEN-END:variables
}
