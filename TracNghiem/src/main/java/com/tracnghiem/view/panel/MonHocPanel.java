/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tracnghiem.view.panel;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.tracnghiem.bus.TopicBUS;
import com.tracnghiem.dto.TopicDTO;
import com.tracnghiem.view.components.addSubject;
import com.tracnghiem.view.mainView;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author X
 */
public class MonHocPanel extends javax.swing.JPanel {
    private final TopicBUS tpBUS = new TopicBUS();
    /**
     * Creates new form MonHocPanel
     */
    public MonHocPanel() {
        initComponents();
        reloadSubject();
        childrenOfSelectedSubject();
        loadDataSubjectTable();
    }
    
    
    private void loadDataSubjectTable(){
        List<TopicDTO> topics = tpBUS.getAll();
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"STT","Môn học","Chủ đề","Bài học","Trạng thái"},0)
        {
        Class[] types = new Class[]{
            Integer.class, Object.class, Object.class, Object.class, Object.class
        };

        boolean[] canEdit = new boolean[]{
            false, false, false, false, false  // Không cho phép chỉnh sửa ô nào
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
        for(TopicDTO topic : topics){
            String status = "Hoạt động";
            if(topic.getTpParent() == -1){
                if(topic.getTpStatus() == 0)
                    status = "Tạm dừng";
                model.addRow(new Object[]{
                    stt++,
                    topic.getTpTitle(),
                    "",
                    "",
                    status
                });
            } else{
                TopicDTO parent = tpBUS.findOne(topic.getTpParent());
                if(topic.getTpStatus() == 0)
                    status = "Tạm dừng";
                model.addRow(new Object[]{
                    stt++,
                    parent.getTpTitle(),
                    topic.getTpTitle(),
                    "",
                    status
                });
            }
        }
        table_monhoc.setModel(model);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table_monhoc.getColumnCount(); i++) 
            table_monhoc.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        table_monhoc.revalidate();
        table_monhoc.repaint();
        twoClickToShowMessageOfRestore(table_monhoc);
    }
    
    private void loadDataSubjectTable(ArrayList<TopicDTO> topics){
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"STT","Môn học","Chủ đề","Bài học","Trạng thái"},0)
        {
        Class[] types = new Class[]{
            Integer.class, Object.class, Object.class, Object.class, Object.class
        };

        boolean[] canEdit = new boolean[]{
            false, false, false, false, false  // Không cho phép chỉnh sửa ô nào
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
        for(TopicDTO topic : topics){
            String status = "Hoạt động";
            if(topic.getTpParent() == -1){
                if(topic.getTpStatus() == 0)
                    status = "Tạm dừng";
                model.addRow(new Object[]{
                    stt++,
                    topic.getTpTitle(),
                    "",
                    "",
                    status
                });
            } else{
                TopicDTO parent = tpBUS.findOne(topic.getTpParent());
                if(topic.getTpStatus() == 0)
                    status = "Tạm dừng";
                model.addRow(new Object[]{
                    stt++,
                    parent.getTpTitle(),
                    topic.getTpTitle(),
                    "",
                    status
                });
            }
        }
        table_monhoc.setModel(model);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table_monhoc.getColumnCount(); i++) 
            table_monhoc.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        table_monhoc.revalidate();
        table_monhoc.repaint();
        twoClickToShowMessageOfRestore(table_monhoc);
    }
    
    private void restore(TopicDTO topic, JTable table){
        topic.setTpStatus(1);      
        int status = topic.getTpStatus();
        TopicBUS tpBUS = new TopicBUS();
        boolean result = tpBUS.update(topic);
        if(result){
            JOptionPane.showMessageDialog(table, "Cập nhật thành công");
        } else {
            JOptionPane.showMessageDialog(table, "Cập nhật thất bại");
        }
    }
    
    private void twoClickToShowMessageOfRestore(JTable table){
        table.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) {  // Kiểm tra click đúp
                    e.consume();  // Đánh dấu sự kiện đã được xử lý
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        String monHoc = model.getValueAt(selectedRow, 1).toString();
                        TopicDTO topic = tpBUS.findOneTitle(monHoc);
                        if(topic.getTpStatus() == 0){
                            int confirm = JOptionPane.showConfirmDialog(table, "Bạn muốn khôi phục trạng thái hoạt động ?", "Xác nhận cập nhật",JOptionPane.YES_NO_OPTION);
                            if(confirm == JOptionPane.YES_OPTION){
                                restore(topic, table);
                                reloadSubject();
                                loadDataSubjectTable();
                            }
                        }
                    }
                }
            }
        });
    }

    private void childrenOfSelectedSubject(){
        List<TopicDTO> topics = tpBUS.getAll();
        cbb_monhoc_chude.removeAllItems();
        cbb_monhoc_chude.addItem("Chọn chủ đề");
        if(cbb_monhoc_monhoc.getSelectedIndex() > 0){
            TopicDTO selectedtopic = tpBUS.findOneTitle((String) cbb_monhoc_monhoc.getSelectedItem());
            for(TopicDTO topic : topics)
                if (topic.getTpParent() == selectedtopic.getTpID() && topic.getTpStatus() == 1)
                    cbb_monhoc_chude.addItem(""+topic.getTpTitle());
        } 
//        else {
//            for(TopicDTO topic : topics)
//                if(topic.getTpParent() != -1 && topic.getTpStatus() == 1)
//                    cbb_monhoc_chude.addItem(topic.getTpTitle());
//        }
    }
    
    private void reloadSubject(){
        cbb_monhoc_monhoc.removeAllItems();
        cbb_monhoc_monhoc.addItem("Chọn môn học");
        cbb_monhoc_chude.removeAllItems();
        cbb_monhoc_chude.addItem("Chọn chủ đề");
        ArrayList<TopicDTO> topics = tpBUS.getAll();
        for(TopicDTO topic : topics){
            if(topic.getTpStatus() != 0){
                cbb_monhoc_monhoc.addItem(topic.getTpTitle());
            }
//            if(topic.getTpParent()== -1 && topic.getTpStatus() != 0){
//                cbb_monhoc_monhoc.addItem(topic.getTpTitle());
//            }
//            else if(topic.getTpParent() != -1 && topic.getTpStatus() == 1)
//                cbb_monhoc_chude.addItem(topic.getTpTitle());
        }
    }
    
    private List<TopicDTO> findSubject(){
        List<TopicDTO> list = new ArrayList<>();
        TopicBUS tpBUS = new TopicBUS();
        List<TopicDTO> topics = tpBUS.getAll();
        if(!txt_monhoc_timkiem.getText().isEmpty()){
            String normalized = Normalizer.normalize(txt_monhoc_timkiem.getText(), Normalizer.Form.NFD);
            String keyword = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            Pattern pattern = Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE);
            for(TopicDTO topic : topics){
                Matcher matcher = pattern.matcher(topic.getTpTitle());
                if(matcher.find())
                    list.add(topic);
            }
        } else if(cbb_monhoc_monhoc.getSelectedIndex() !=0 && cbb_monhoc_chude.getSelectedIndex() != 0){
            String normalized_chude = Normalizer.normalize(cbb_monhoc_chude.getSelectedItem().toString(), Normalizer.Form.NFD);
            String keyword_chude = normalized_chude.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            Pattern pattern_chude = Pattern.compile(Pattern.quote(keyword_chude), Pattern.CASE_INSENSITIVE);
            TopicDTO selected_chude = tpBUS.findOneTitle(cbb_monhoc_chude.getSelectedItem().toString());
            System.out.println(selected_chude.toString());
            for (TopicDTO topic : topics) {
                Matcher matcher_chude = pattern_chude.matcher(topic.getTpTitle());
                if (matcher_chude.find() && topic.getTpID() == selected_chude.getTpID())
                    list.add(topic);
            }  
        } else if(cbb_monhoc_monhoc.getSelectedIndex() !=0 && cbb_monhoc_chude.getSelectedIndex() == 0){
            String normalized_monhoc = Normalizer.normalize(cbb_monhoc_monhoc.getSelectedItem().toString(), Normalizer.Form.NFD);
            String keyword_monhoc = normalized_monhoc.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            Pattern pattern_monhoc = Pattern.compile(Pattern.quote(keyword_monhoc), Pattern.CASE_INSENSITIVE);
            TopicDTO selected_monhoc = tpBUS.findOneTitle(cbb_monhoc_monhoc.getSelectedItem().toString());
            for (TopicDTO topic : topics) {
                Matcher matcher_monhoc = pattern_monhoc.matcher(topic.getTpTitle());
                if (matcher_monhoc.find() || topic.getTpParent() == selected_monhoc.getTpID()) 
                    list.add(topic);
            }
        }
        return list;
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        txt_monhoc_timkiem = new javax.swing.JTextField();
        btn_tim = new javax.swing.JButton();
        cbb_monhoc_monhoc = new javax.swing.JComboBox<>();
        cbb_monhoc_chude = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btn_taomoi = new javax.swing.JButton();
        btn_capnhat = new javax.swing.JButton();
        btn_xoa = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        table_monhoc = new javax.swing.JTable();

        jPanel3.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel18.setText("Tìm kiếm");

        txt_monhoc_timkiem.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
        txt_monhoc_timkiem.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập môn học . . . . . . . . . . . . . . . . . . .");
        txt_monhoc_timkiem.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

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

        cbb_monhoc_monhoc.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Danh sách môn học");
        cbb_monhoc_monhoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbb_monhoc_monhocActionPerformed(evt);
            }
        });

        cbb_monhoc_chude.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Danh sách chủ đề");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel19.setText("Môn học");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel20.setText("Chủ đề");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txt_monhoc_timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_tim, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbb_monhoc_monhoc, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbb_monhoc_chude, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38))))
            .addComponent(jSeparator3)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_tim, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_monhoc_timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbb_monhoc_monhoc, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbb_monhoc_chude, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

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

        btn_capnhat.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
        btn_capnhat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_capnhat.setIcon(new FlatSVGIcon("icons/reset.svg", 30, 30)
        );
        btn_capnhat.setText("Cập  nhật");
        btn_capnhat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_capnhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capnhatActionPerformed(evt);
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

        table_monhoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Mã môn học", "Tên môn học", "Chủ đề", "Bài học", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(table_monhoc);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_xoa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_capnhat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_taomoi)
                .addContainerGap())
            .addComponent(jScrollPane3)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_taomoi)
                    .addComponent(btn_capnhat)
                    .addComponent(btn_xoa))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_timActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_timActionPerformed
        // TODO add your handling code here:
        loadDataSubjectTable((ArrayList<TopicDTO>) findSubject());
        if(txt_monhoc_timkiem.getText().isEmpty())
            loadDataSubjectTable();
    }//GEN-LAST:event_btn_timActionPerformed

    private void btn_taomoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_taomoiActionPerformed
        mainView.showCustomDialog(null, new addSubject(null, false), "Thêm môn học");
    }//GEN-LAST:event_btn_taomoiActionPerformed

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        // TODO add your handling code here:
          // TODO add your handling code here:

        int selectedRow = table_monhoc.getSelectedRow();
        if(selectedRow != -1){
            DefaultTableModel model = (DefaultTableModel) table_monhoc.getModel();
            String title = model.getValueAt(selectedRow, 1).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Bán muốn xóa dữ liệu này ?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION){
                TopicDTO topic = tpBUS.findOneTitle(title);
                if (topic == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy chủ đề với ID: " + title);
                    return;
                }
                boolean result = tpBUS.delete(topic);
                if(result){
//                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Xóa thành công");
//                    for(int i = 0;i < model.getRowCount();i++)
//                        model.setValueAt(i + 1, i, 0);
                    reloadSubject();
                    loadDataSubjectTable();
                } else{
                    JOptionPane.showMessageDialog(this, "Xóa thất bại");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dự liệu muốn xóa");
        }
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_capnhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capnhatActionPerformed
        // TODO add your handling code here:
         reloadSubject();
         childrenOfSelectedSubject();
         loadDataSubjectTable();
    }//GEN-LAST:event_btn_capnhatActionPerformed

    private void cbb_monhoc_monhocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbb_monhoc_monhocActionPerformed
        // TODO add your handling code here:
        childrenOfSelectedSubject();
    }//GEN-LAST:event_cbb_monhoc_monhocActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_capnhat;
    private javax.swing.JButton btn_taomoi;
    private javax.swing.JButton btn_tim;
    private javax.swing.JButton btn_xoa;
    private javax.swing.JComboBox<String> cbb_monhoc_chude;
    private javax.swing.JComboBox<String> cbb_monhoc_monhoc;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable table_monhoc;
    private javax.swing.JTextField txt_monhoc_timkiem;
    // End of variables declaration//GEN-END:variables
}
