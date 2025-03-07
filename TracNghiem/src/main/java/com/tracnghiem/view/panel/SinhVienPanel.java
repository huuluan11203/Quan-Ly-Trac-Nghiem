/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tracnghiem.view.panel;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.tracnghiem.bus.UserBUS;
import com.tracnghiem.dto.UserDTO;
import com.tracnghiem.view.components.addUser;
import com.tracnghiem.view.components.detailUser;
import com.tracnghiem.view.mainView;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author X
 */
public class SinhVienPanel extends javax.swing.JPanel {
    private final UserBUS userBUS = new UserBUS();
    /**
     * Creates new form SinhVienPanel
     */
    public SinhVienPanel() {
        initComponents();
        loadUserTable();
    }
    
    public void loadUserTable(){
        List<UserDTO> users = userBUS.getAll();
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"STT","Tên đăng nhập","Họ và tên","Email","ID","Quyền","Trạng thái"},0)
        {
        Class[] types = new Class[]{
            Integer.class, Object.class, Object.class, Object.class,Integer.class, Object.class, Object.class
        };

        boolean[] canEdit = new boolean[]{
            false, false, false, false, false, false, false  // Không cho phép chỉnh sửa ô nào
        };

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return types[columnIndex];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
        };
        int stt = 1;
        for(UserDTO user : users){
            String quyen = "USER";
            if(user.getIsAdmin() == 1)
                quyen = "ADMIN";
            String status = "Hoạt động";
            if(user.getUserStatus() == 0)
                status = "Khóa";
            model.addRow(new Object[]{
                stt++,
                user.getUserName(),
                user.getUserFullName(),
                user.getUserEmail(),
                user.getUserID(),
                quyen,
                status
            });
        }
        table_sinhvien.setModel(model);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table_sinhvien.getColumnCount(); i++) 
            table_sinhvien.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        table_sinhvien.revalidate();
        table_sinhvien.repaint();
    }
    
    public void loadUserTable(ArrayList<UserDTO> users){
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"STT","Tên đăng nhập","Họ và tên","Email","ID","Quyền","Trạng thái"},0)
        {
        Class[] types = new Class[]{
            Integer.class, Object.class, Object.class, Object.class,Integer.class, Object.class, Object.class
        };

        boolean[] canEdit = new boolean[]{
            false, false, false, false, false, false, false  // Không cho phép chỉnh sửa ô nào
        };

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return types[columnIndex];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
        };
        int stt = 1;
        for(UserDTO user : users){
            String quyen = "USER";
            if(user.getIsAdmin() == 1)
                quyen = "ADMIN";
            String status = "Hoạt động";
            if(user.getUserStatus() == 0)
                status = "Khóa";
            model.addRow(new Object[]{
                stt++,
                user.getUserName(),
                user.getUserFullName(),
                user.getUserEmail(),
                user.getUserID(),
                quyen,
                status
            });
        }
        table_sinhvien.setModel(model);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table_sinhvien.getColumnCount(); i++) 
            table_sinhvien.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        table_sinhvien.revalidate();
        table_sinhvien.repaint();
    }

    private UserDTO getOneSelectedInTable() {
    int selectedRow = table_sinhvien.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng!");
        return null;
    }
    DefaultTableModel table = (DefaultTableModel) table_sinhvien.getModel();
    int id = Integer.parseInt(table.getValueAt(selectedRow, 4).toString()); // Cột chứa ID
    UserDTO user = userBUS.findOne(id);
    if (user != null && user.getUserStatus() == 0) {
        btn_xoa.setText("Kích hoạt");
        btn_xoa.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #20c020; foreground: #ffffff;");
        btn_xoa.setIcon(new FlatSVGIcon("icons/reset.svg", 30, 30));
    } else {
        btn_xoa.setText("Xóa");
        btn_xoa.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ee2020; foreground: #ffffff;");
    }
    return user;
}

    
    private void searchUser(){
        String search = txt_tim.getText();
        String kieu = cbb_kieu.getSelectedItem().toString();
        if(!search.isEmpty() && kieu != null)
            loadUserTable(userBUS.search(search, kieu));
    }
    
    private void deleteActivateUser(UserDTO user){
        if(user.getUserStatus() == 1){
            user.setUserStatus(0);
            if(userBUS.update(user)){
                JOptionPane.showMessageDialog(this, "Khóa tài khoản thành công");
                loadUserTable();
            }
        } else{
            user.setUserStatus(1);
            if(userBUS.update(user)){
                JOptionPane.showMessageDialog(this, "Kích hoạt tài khoản thành công");
                loadUserTable();
            }
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

        jPanel5 = new javax.swing.JPanel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel21 = new javax.swing.JLabel();
        txt_tim = new javax.swing.JTextField();
        btn_tim = new javax.swing.JButton();
        cbb_kieu = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        btn_taomoi = new javax.swing.JButton();
        btn_chitiet = new javax.swing.JButton();
        btn_xoa = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        table_sinhvien = new javax.swing.JTable();
        btn_capnhat = new javax.swing.JButton();

        jPanel5.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel21.setText("Tìm kiếm");

        txt_tim.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
        txt_tim.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập . . . . . . . . . . . . . . . . . . .");
        txt_tim.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txt_tim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_timActionPerformed(evt);
            }
        });

        btn_tim.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #0bae1d; foreground: #ffffff;");
        btn_tim.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_tim.setIcon(new FlatSVGIcon("icons/search.svg", 25, 25)
        );
        btn_tim.setText("Tìm");
        btn_tim.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_tim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_timActionPerformed(evt);
            }
        });

        cbb_kieu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chọn kiểu", "Tất cả", "Họ và tên", "Email", "Tên đăng nhập" }));
        cbb_kieu.setSelectedItem("Chọn kiểu");
        cbb_kieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbb_kieuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txt_tim, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_tim, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbb_kieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
            .addComponent(jSeparator4)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbb_kieu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_tim, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_tim, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

        btn_taomoi.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
        btn_taomoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_taomoi.setIcon(new FlatSVGIcon("icons/add.svg", 30, 30)
        );
        btn_taomoi.setText("Tạo mới");
        btn_taomoi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_taomoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_taomoiActionPerformed(evt);
            }
        });

        btn_chitiet.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
        btn_chitiet.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_chitiet.setIcon(new FlatSVGIcon("icons/detail.svg", 30, 30)
        );
        btn_chitiet.setText("Chi tiết");
        btn_chitiet.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_chitiet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_chitietActionPerformed(evt);
            }
        });

        btn_xoa.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ee2020; foreground: #ffffff;");
        btn_xoa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_xoa.setIcon(new FlatSVGIcon("icons/delete.svg", 30, 30)
        );
        btn_xoa.setText("Xóa");
        btn_xoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaActionPerformed(evt);
            }
        });

        table_sinhvien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Họ và tên", "Email", "Quyền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_sinhvien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_sinhvienMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(table_sinhvien);

        btn_capnhat.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
        btn_capnhat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_capnhat.setIcon(new FlatSVGIcon("icons/reset.svg", 30, 30)
        );
        btn_capnhat.setText("Cập nhật");
        btn_capnhat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_capnhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capnhatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_xoa)
                .addGap(27, 27, 27)
                .addComponent(btn_capnhat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_chitiet)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_taomoi)
                .addContainerGap())
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_taomoi)
                    .addComponent(btn_chitiet)
                    .addComponent(btn_xoa)
                    .addComponent(btn_capnhat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_timActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_timActionPerformed
        // TODO add your handling code here:
        searchUser();
        if(txt_tim.getText().isEmpty())
            loadUserTable();
    }//GEN-LAST:event_btn_timActionPerformed

    private void btn_taomoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_taomoiActionPerformed
        mainView.showCustomDialog(null, new addUser(null, false), "Thêm người dùng");
    }//GEN-LAST:event_btn_taomoiActionPerformed

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        // TODO add your handling code here:
        if(btn_xoa.getText().equals("Xóa")){
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận khóa tài khoản ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION)
                deleteActivateUser(getOneSelectedInTable());
        } else {
            int confirm = JOptionPane.showConfirmDialog(this, "Kích hoạt tài khoản ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION)
                deleteActivateUser(getOneSelectedInTable());
        }
        
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_chitietActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_chitietActionPerformed
        // TODO add your handling code here:
        mainView.showCustomDialog(null, new detailUser(getOneSelectedInTable(),true), "Chi tiết người dùng");
    }//GEN-LAST:event_btn_chitietActionPerformed

    private void cbb_kieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbb_kieuActionPerformed
        // TODO add your handling code here:
        if(cbb_kieu.getSelectedIndex() == 0){
            loadUserTable();
            txt_tim.setText("");
        }
    }//GEN-LAST:event_cbb_kieuActionPerformed

    private void txt_timActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_timActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_timActionPerformed

    private void table_sinhvienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_sinhvienMouseClicked
        // TODO add your handling code here:
        UserDTO user = getOneSelectedInTable();
    }//GEN-LAST:event_table_sinhvienMouseClicked

    private void btn_capnhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capnhatActionPerformed
        // TODO add your handling code here:
        for(UserDTO user : userBUS.getAll())
            System.out.println(user.toString());
        loadUserTable();
    }//GEN-LAST:event_btn_capnhatActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_capnhat;
    private javax.swing.JButton btn_chitiet;
    private javax.swing.JButton btn_taomoi;
    private javax.swing.JButton btn_tim;
    private javax.swing.JButton btn_xoa;
    private javax.swing.JComboBox<String> cbb_kieu;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable table_sinhvien;
    private javax.swing.JTextField txt_tim;
    // End of variables declaration//GEN-END:variables
}
