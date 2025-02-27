/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tracnghiem.view.panel;

import com.tracnghiem.dto.TestDTO;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.tracnghiem.bus.TestBUS;
import com.tracnghiem.bus.TopicBUS;
import com.tracnghiem.view.components.addTest;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

/**
 *
 * @author X
 */
public class DeThiPanel extends javax.swing.JPanel {
    private final TestBUS tBUS = new TestBUS();
    private final TopicBUS tpBUS = new TopicBUS();
    private ArrayList<TestDTO> listT = new ArrayList<>();
    private LocalDate dateSelected;
    private int tIDSelected = -1;
    
    /**
     * Creates new form DeThiPanel
     */
    public DeThiPanel() {
        initComponents();
        
        listT = tBUS.getAll();
        
        
        jDateChooser1.getDateEditor().addPropertyChangeListener("date", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    Date selectedDate = jDateChooser1.getDate();
                    if (selectedDate != null) {
                        LocalDate localDate = selectedDate.toInstant()
                                .atZone(ZoneId.systemDefault()) // Chuyển đổi theo múi giờ hệ thống
                                .toLocalDate();
                        dateSelected = localDate;    
                    }
                    
                }
            }
        });

        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Kiểm tra click đúp
                    int row = jTable4.getSelectedRow();
                    if (row != -1) {
                        tIDSelected = (int) jTable4.getValueAt(row, 0);
                    }
                }
            }
        });
        
        

        loadTable(listT);
    }
    // MODULE ĐỀ THI
    private void setColumnWidthsForTableTest() {

        TableColumnModel columnModel = jTable4.getColumnModel();
        int totalWidth = jTable4.getWidth(); // Lấy chiều rộng tổng của bảng

        double[] columnRatios = {0.05, 0.075, 0.25, 0.25,0.075,0.1,0.05,0.05,0.05,0.05 };

        for (int i = 0; i < columnRatios.length; i++) {
            int columnWidth = (int) (totalWidth * columnRatios[i]);
            columnModel.getColumn(i).setPreferredWidth(columnWidth);
        }
    }
    private void loadTable(ArrayList<TestDTO> list) {
        DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        if (list.isEmpty()) {
            model.addRow(new Object[]{"", "", "Không có dữ liệu", "", "", "","", "", ""});
            return;
        }
        for (TestDTO test : list) {
            model.addRow(new Object[]{
                test.getTestID(),      // ID
                test.getTestCode(),    // Mã đề thi
                test.getTestTitle(),   // Tiêu đề
                tpBUS.findOne(test.getTpID()).getTpTitle(),        // Chủ đề
                test.getTestDate(),    // Thời gian
                test.getTestTime(),    // Giờ làm
                test.getTestLimit(),
                test.getNumEasy(),     // Số lượng câu dễ
                test.getNumMedium(),   // Số lượng câu trung bình
                test.getNumDifficult() // Số lượng câu khó
            });
        }
    }
    
    private void handleSearch() {
        String key = textFieldSearch.getText();
        
        if (dateSelected == null) {
            if (key.trim().isEmpty()) {
                 JOptionPane.showMessageDialog(null, "Nhập từ khoá để tìm kiếm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            loadTable(tBUS.search(key));
        } else {
            loadTable(tBUS.search(key, dateSelected));
        }
    
    }
    
    private void refresh() {
        listT = tBUS.getAll();
        textFieldSearch.setText("");
        dateSelected = null;
        jDateChooser1.setDate(null);
        tIDSelected = -1;
        
        
        
        loadTable(listT);
    }
    
    // DÙNG CHUNG
    public static void showCustomDialog(JFrame parent, JPanel panel, String title) {
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

        jPanel7 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        textFieldSearch = new javax.swing.JTextField();
        tim_btn1 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jButton16 = new javax.swing.JButton();
        refreshBtn = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();

        jPanel7.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel22.setText("Tìm kiếm");

        textFieldSearch.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
        textFieldSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập . . . . . . . . . . . . . . . . . . .");
        textFieldSearch.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        tim_btn1.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #0bae1d; foreground: #ffffff;");
        tim_btn1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tim_btn1.setIcon(new FlatSVGIcon("icons/search.svg", 25, 25)
        );
        tim_btn1.setText("Tìm");
        tim_btn1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tim_btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tim_btn1ActionPerformed(evt);
            }
        });

        jDateChooser1.putClientProperty(FlatClientProperties.STYLE, "arc: 10;");

        jButton16.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
        jButton16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton16.setText("X");
        jButton16.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        refreshBtn.setIcon(new FlatSVGIcon("icons/refresh.svg", 30 ,30)) ;
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(textFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tim_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(refreshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tim_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                        .addComponent(refreshBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jDateChooser1.setDateFormatString("dd/MM/yyyy");
        jDateChooser1.getDateEditor().setEnabled(false);
        // Lấy JTextField từ JDateChooser
        JTextField dateTextField = ((JTextField) jDateChooser1.getDateEditor().getUiComponent());

        // Áp dụng bo góc cho JTextField bên trong JDateChooser
        dateTextField.putClientProperty(FlatClientProperties.STYLE, "arc: 10;");
        // Lấy nút chọn ngày (JButton)
        JButton calendarButton = (JButton) jDateChooser1.getCalendarButton();

        // Tuỳ chỉnh nút chọn ngày
        calendarButton.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
        calendarButton.setIcon(new FlatSVGIcon("icons/calendar.svg", 30, 30));  // Đổi icon thành emoji lịch (hoặc setIcon)
        calendarButton.setFocusPainted(false);
        calendarButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        refreshBtn.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #0bae1d; foreground: #ffffff;");

        jPanel8.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

        jButton13.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
        jButton13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton13.setIcon(new FlatSVGIcon("icons/add.svg", 30, 30)
        );
        jButton13.setText("Tạo mới");
        jButton13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
        jButton14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton14.setIcon(new FlatSVGIcon("icons/detail.svg", 30, 30)
        );
        jButton14.setText("Chi tiết");
        jButton14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jButton15.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ee2020; foreground: #ffffff;");
        jButton15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton15.setIcon(new FlatSVGIcon("icons/delete.svg", 30, 30)
        );
        jButton15.setText("Xóa");
        jButton15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "ID", "Mã đề thi", "Tiêu đề", "Chủ đề", "Ngày thi", "Giờ làm( phút)","Lượt", "SL dễ", "SL trung", "SL khó"
            }

        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false,false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.setCellSelectionEnabled(true);
        jScrollPane5.setViewportView(jTable4);
        SwingUtilities.invokeLater(() -> setColumnWidthsForTableTest());
        jTable4.setRowSelectionAllowed(true);
        jTable4.setColumnSelectionAllowed(false);
        jTable4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton13)
                .addContainerGap())
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1040, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton13)
                    .addComponent(jButton14)
                    .addComponent(jButton15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tim_btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tim_btn1ActionPerformed
        // TODO add your handling code here:
        handleSearch();
    }//GEN-LAST:event_tim_btn1ActionPerformed
    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        showCustomDialog(null, new addTest(null, false), "Thêm bài thi");
    }//GEN-LAST:event_jButton13ActionPerformed
    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        jDateChooser1.setDate(null);
        dateSelected = null;
    }//GEN-LAST:event_jButton16ActionPerformed

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        // TODO add your handling code here:
        refresh();
    }//GEN-LAST:event_refreshBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable4;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JTextField textFieldSearch;
    private javax.swing.JButton tim_btn1;
    // End of variables declaration//GEN-END:variables
}
