/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tracnghiem.view.components;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.tracnghiem.bus.TopicBUS;
import com.tracnghiem.dto.TopicDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author huulu
 */
public class addSubject extends javax.swing.JPanel {

    private final TopicBUS tpBUS = new TopicBUS();
    private static boolean update;
    private static TopicDTO topic;

    /**
     * Creates new form addSubject
     */
    public addSubject(TopicDTO topic, boolean update) {
        initComponents();
        this.update = update;
        this.topic = topic;

        if (this.update && topic != null) {
            setTopic(topic);
        } else {
            reload();
        }

    }

    private void setTopic(TopicDTO topic) {
        id.setText(String.valueOf(topic.getTpID()));
        txt_tenmonhoc.setText(topic.getTpTitle());
        setSelectedParentID(topic.getTpParent());
        cbb_parentID.setEnabled(false);
        setSelectedStatus(topic.getTpStatus());
    }

    private void addNewTopic() {
        String tenmonhoc = txt_tenmonhoc.getText().trim();
        int selectedParentId = getSelectedParentId();
        if (tpBUS.isExistWithParent(tenmonhoc, selectedParentId)) {
            JOptionPane.showMessageDialog(this, "Môn học '" + tenmonhoc + "' đã tồn tại trong cùng chủ đề");
            return;
        }

        // Nếu không trùng, thêm mới topic
        TopicDTO newTopic = new TopicDTO();
        newTopic.setTpTitle(tenmonhoc);
        newTopic.setTpParent(selectedParentId);
        newTopic.setTpStatus(1); // Giả sử 1 là active
        tpBUS.add(newTopic);

        JOptionPane.showMessageDialog(this, "Thêm môn học thành công");
        reload();
        loadParentIDComboBox(); // Cập nhật lại combobox nếu cần
    }

    private void updateTopic(String name) {

        int selectedStatus = cbb_trangthai.getSelectedItem().equals("Hoạt động") ? 1 : 0;

        if (!name.equals(topic.getTpTitle())) {
            // Kiểm tra xem tên có bị trùng trong cùng một parent không
            if (tpBUS.isExistWithParent(name, topic.getTpParent())) {
                JOptionPane.showMessageDialog(this, "Môn học '" + name + "' đã tồn tại trong cùng chủ đề");
                return;
            }
        }

        // Lấy topic cần update
        TopicDTO topicToUpdate = tpBUS.findOne(topic.getTpID());
        if (topicToUpdate == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy môn học để cập nhật");
            return;
        }

        // Cập nhật thông tin
        topicToUpdate.setTpTitle(name);
        topicToUpdate.setTpParent(getSelectedParentId());
        topicToUpdate.setTpStatus(selectedStatus);

        // Gửi yêu cầu cập nhật
        boolean isUpdated = tpBUS.update(topicToUpdate);
        if (isUpdated) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadParentIDComboBox(); // Cập nhật lại danh sách chủ đề
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }

    
    
    private int getSelectedParentId() {
        String selectedTitle = (String) cbb_parentID.getSelectedItem();
        if (selectedTitle.equals("--Trống--")) {
            return 0; // Giả sử 0 là gốc
        }

        // Tìm tpID tương ứng với tpTitle từ danh sách topic
        List<TopicDTO> topics = tpBUS.getAll();
        for (TopicDTO topic : topics) {
            if (topic.getTpTitle().equals(selectedTitle)) {
                return topic.getTpID();
            }
        }
        return 0; // Mặc định trả về 0 nếu không tìm thấy
    }

    private void setSelectedParentID(int tpParent) {
        loadParentIDComboBox(); // Load lại danh sách parent trước

        if (tpParent == 0) {
            cbb_parentID.setSelectedIndex(0); // Nếu không có parent, chọn "--Trống--"
            return;
        }

        for (int i = 0; i < cbb_parentID.getItemCount(); i++) {
            String item = (String) cbb_parentID.getItemAt(i);
            TopicDTO topic = tpBUS.findTopicByTitle(item); // Tìm topic theo title

            if (topic != null && topic.getTpID() == tpParent) {
                cbb_parentID.setSelectedIndex(i); // Chọn giá trị phù hợp
                return;
            }
        }
    }

    private void setSelectedStatus(int tpStatus) {
        cbb_trangthai.removeAllItems(); // Xóa tất cả item cũ
        cbb_trangthai.addItem("Hoạt động");
        cbb_trangthai.addItem("Tạm ngừng");

        if (tpStatus == 1) {
            cbb_trangthai.setSelectedItem("Hoạt động");
        } else if (tpStatus == 0) {
            cbb_trangthai.setSelectedItem("Tạm ngừng");
        } else {
            cbb_trangthai.setSelectedIndex(-1); // Không chọn gì nếu giá trị không hợp lệ
        }
    }

    private void reload() {
        txt_tenmonhoc.setText("");
        id.setText(Integer.toString(tpBUS.getNextID()));
        loadParentIDComboBox();
        cbb_parentID.setSelectedIndex(0);
    }

    private int getParentID() {
        if (cbb_parentID.getSelectedIndex() <= 0) {
            return 0; // Nếu chưa chọn hoặc chọn mặc định, trả về 0
        }
        Object selectedItem = cbb_parentID.getSelectedItem();
        if (selectedItem == null) {
            return 0;
        }

        String parentTitle = selectedItem.toString();
        TopicDTO parentTopic = tpBUS.findOneTitle(parentTitle);

        return (parentTopic != null) ? parentTopic.getTpID() : 0; // Nếu không tìm thấy, cũng trả về 0
    }

    private int getStatusID() {
        Object selectedItem = cbb_trangthai.getSelectedItem();
        if (selectedItem == null) {
            return 0; // Mặc định là 0 nếu không có gì được chọn
        }
        return selectedItem.toString().equalsIgnoreCase("Hoạt động") ? 1 : 0;
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        id = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_tenmonhoc = new javax.swing.JTextField();
        luu = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        cbb_trangthai = new javax.swing.JComboBox<>();
        cbb_parentID = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        jPanel2.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel1.setText("Mã môn học:");

        id.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        id.setText("NULL");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel3.setText("Chọn môn học / chủ đề chính:");

        txt_tenmonhoc.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
        txt_tenmonhoc.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên môn học hoặc chủ đề");

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

        cbb_trangthai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hoạt động", "Tạm dừng" }));
        cbb_trangthai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbb_trangthaiActionPerformed(evt);
            }
        });

        cbb_parentID.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,"Danh sách môn học" );
        cbb_parentID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbb_parentIDActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel5.setText("Tên môn học / chủ đề / bài học:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel6.setText("Trạng thái:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_tenmonhoc)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 402, Short.MAX_VALUE)
                        .addComponent(luu))
                    .addComponent(cbb_parentID, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbb_trangthai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(luu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_tenmonhoc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbb_parentID, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbb_trangthai, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbb_trangthaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbb_trangthaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbb_trangthaiActionPerformed

    private void cbb_parentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbb_parentIDActionPerformed

    }//GEN-LAST:event_cbb_parentIDActionPerformed

    private void luuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_luuActionPerformed
        String tenmonhoc = txt_tenmonhoc.getText();
        if (!update) {

            if (tenmonhoc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên môn học không được để trống!");
                return;
            }
            if (!tenmonhoc.isEmpty()) {
                int result = JOptionPane.showConfirmDialog(this, "Xác nhận thêm môn học?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    addNewTopic();

                }
            }
        } else {

            if (tenmonhoc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên môn học không được để trống!");
                return;
            }
            if (!tenmonhoc.isEmpty()) {
                int result = JOptionPane.showConfirmDialog(this, "Lưu các thay đổi?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    updateTopic(tenmonhoc);

                }
            }
        }
    }//GEN-LAST:event_luuActionPerformed

    private void loadParentIDComboBox() {
        cbb_parentID.removeAllItems();
        cbb_parentID.addItem("--Trống--");
        cbb_parentID.setSelectedIndex(0);

        List<TopicDTO> topics = tpBUS.getAll();
        if (topics == null || topics.isEmpty()) {
            return;
        }

        Map<Integer, TopicDTO> topicMap = new HashMap<>();
        for (TopicDTO topic : topics) {
            topicMap.put(topic.getTpID(), topic);
        }

        for (TopicDTO topic : topics) {
            if (topic.getTpStatus() != 0) {
                if (topic.getTpParent() == 0
                        || (topicMap.containsKey(topic.getTpParent()) && topicMap.get(topic.getTpParent()).getTpParent() == 0)) {
                    cbb_parentID.addItem(topic.getTpTitle());
                }
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbb_parentID;
    private javax.swing.JComboBox<String> cbb_trangthai;
    private javax.swing.JLabel id;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton luu;
    private javax.swing.JTextField txt_tenmonhoc;
    // End of variables declaration//GEN-END:variables
}
