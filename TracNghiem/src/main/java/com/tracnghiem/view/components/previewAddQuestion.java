/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tracnghiem.view.components;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.tracnghiem.bus.AnswerBUS;
import com.tracnghiem.bus.QuestionBUS;
import com.tracnghiem.bus.TopicBUS;
import com.tracnghiem.dto.AnswerDTO;
import com.tracnghiem.dto.QuestionDTO;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author X
 */
public class previewAddQuestion extends javax.swing.JPanel {

    private ArrayList<QuestionDTO> listQ = new ArrayList<>();

    HashMap<String, ArrayList<AnswerDTO>> answerMap = new HashMap<>();
    private final AnswerBUS aBUS = new AnswerBUS();
    private final QuestionBUS qBUS = new QuestionBUS();
    private final TopicBUS tBUS = new TopicBUS();

    /**
     * Creates new form previewAddQuestion
     *
     * @param listQ
     * @param answerMap
     */
    public previewAddQuestion(ArrayList<QuestionDTO> listQ, HashMap<String, ArrayList<AnswerDTO>> answerMap) {
        initComponents();
        this.listQ = listQ;
        this.answerMap = answerMap;

        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Kiểm tra click đúp
                    int row = jTable1.getSelectedRow();
                    if (row != -1) {

                        ArrayList<AnswerDTO> answers = answerMap.get(listQ.get(row).getQContent());

                        previewDetailQuestion(answers);

                    }
                }
            }
        });

        loadTableQuestion(listQ);
    }

    

    private void addNew() {
        // Xử lý dữ liệu
        boolean check = true;
        for (QuestionDTO question : listQ) {
            ArrayList<AnswerDTO> answers = answerMap.get(question.getQContent());

            if (answers.size() < 2 || answers.size() > 5) {
                JOptionPane.showMessageDialog(null, "Câu hỏi: '" + question.getQContent() + "' có " + answers.size() + " đáp án. Yêu cầu từ 2-5!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (qBUS.isExist(question.getQContent(), question.getQTopic())) {
                JOptionPane.showMessageDialog(null, "Câu hỏi: '" + question.getQContent() + "' đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (qBUS.add(question)) {
                int newID = qBUS.getMaxID();

                for (AnswerDTO a : answers) {
                    a.setQID(newID);
                    if (!aBUS.add(a)) {
                        check = false;
                        break;
                    }
                }
            } else {
                check = false;
            }

        }

        if (check) {

            JOptionPane.showMessageDialog(null, "Import dữ liệu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            java.awt.Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JDialog) {
                ((JDialog) window).dispose();
            }
        } else {

            JOptionPane.showMessageDialog(null, "Import dữ liệu thất bại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTableQuestion(ArrayList<QuestionDTO> list) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        if (list.isEmpty()) {
            model.addRow(new Object[]{"", "", "Không có dữ liệu", "", "", ""});
            return;
        }

        for (QuestionDTO question : list) {
            model.addRow(new Object[]{
                question.getQID(),
                tBUS.findOne(question.getQTopic()).getTpTitle(),
                question.getQContent(),
                question.getQLevel(),
                question.getQPictures(),
                question.getQStatus() == 1 ? "Active" : "Hidden"
            });
        }
    }

    private void previewDetailQuestion(ArrayList<AnswerDTO> listA) {
        showCustomDialog(null, new previewDetailQuestion(listA), "Đáp án");
    }

    private static void showCustomDialog(JFrame parent, JPanel panel, String title) {
        JDialog dialog = new JDialog(parent, title, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Thêm panel vào dialog
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);

        // Định kích thước dialog
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        luu = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Mã câu", "Chủ đề", "Câu hỏi", "Độ khó", "Hình", "Trạng thái"
            }

        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable1);

        luu.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #0bae1d; foreground: #ffffff;");
        luu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        luu.setIcon(new FlatSVGIcon("icons/save.svg", 30, 30)
        );
        luu.setText("Lưu");
        luu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        luu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                luuActionPerformed(evt);
            }
        });

        jButton5.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ee2020; foreground: #ffffff;");
        jButton5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton5.setIcon(new FlatSVGIcon("icons/delete.svg", 30, 30)
        );
        jButton5.setText("Huỷ");
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(411, Short.MAX_VALUE)
                .addComponent(luu)
                .addGap(73, 73, 73)
                .addComponent(jButton5)
                .addGap(412, 412, 412))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1028, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(615, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(luu)
                    .addComponent(jButton5))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(47, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void luuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_luuActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(
                null, "Bạn có muốn lưu dữ liệu không?", "Xác nhận lưu",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            addNew();
        }
    }//GEN-LAST:event_luuActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        java.awt.Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JDialog) {
            ((JDialog) window).dispose();
        }
    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton luu;
    // End of variables declaration//GEN-END:variables
}
