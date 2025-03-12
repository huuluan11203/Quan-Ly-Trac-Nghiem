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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author X
 */
public class MonHocPanel extends javax.swing.JPanel {

    private static TopicBUS tpBUS;
    private Map<String, Integer> parentTitleToIdMap = new HashMap<>(); // Ánh xạ tpTitle -> tpID cho topic cha
    private Map<String, Integer> subTopicTitleToIdMap = new HashMap<>();
    DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Mã môn học", "Tên môn học", "Chủ đề", "Bài học", "Trạng thái"}, 0
    );

    /**
     * Creates new form MonHocPanel
     */
    public MonHocPanel() {
        initComponents();

        tpBUS = new TopicBUS();
        loadParentIDComboBox();

        loadDataSubjectTable();
    }

    private void loadDataSubjectTable() {
        model.setRowCount(0); // Xóa dữ liệu cũ trong model
        ArrayList<TopicDTO> lTp = tpBUS.getAll(); // Lấy tất cả topic

        // Tạo map để tra cứu topic theo tpID
        Map<Integer, TopicDTO> topicMap = new HashMap<>();
        for (TopicDTO topic : lTp) {
            topicMap.put(topic.getTpID(), topic);
        }

        // Tạo danh sách để lưu các topic theo cấp độ
        List<TopicDTO> rootTopics = new ArrayList<>();    // Topic gốc
        Map<Integer, List<TopicDTO>> childTopics = new HashMap<>(); // Topic con theo tpParent

        // Phân loại topic theo cấp độ
        for (TopicDTO topic : lTp) {
            if (topic.getTpParent() == 0) {
                rootTopics.add(topic); // Thêm vào danh sách topic gốc
            } else {
                childTopics.computeIfAbsent(topic.getTpParent(), k -> new ArrayList<>()).add(topic); // Thêm vào danh sách topic con
            }
        }

        // Duyệt qua các topic gốc
        for (TopicDTO rootTopic : rootTopics) {
            List<TopicDTO> level1Topics = childTopics.getOrDefault(rootTopic.getTpID(), new ArrayList<>());

            // Nếu không có topic con (trường hợp như "dfdf")
            if (level1Topics.isEmpty()) {
                Object[] row = new Object[5];
                row[0] = rootTopic.getTpID();      // "Mã môn học"
                row[1] = rootTopic.getTpTitle();   // "Tên môn học"
                row[2] = "";                       // "Chuyên đề" - rỗng
                row[3] = "";                       // "Bài học" - rỗng
                row[4] = rootTopic.getTpStatus() == 1 ? "Hoạt động" : "Tạm dừng"; // "Trạng thái"
                model.addRow(row);
            } else {
                // Duyệt qua topic cấp 1 (chuyên đề)
                for (TopicDTO level1Topic : level1Topics) {
                    List<TopicDTO> level2Topics = childTopics.getOrDefault(level1Topic.getTpID(), new ArrayList<>());

                    if (level2Topics.isEmpty()) {
                        // Nếu cấp 1 không có cấp 2 (trường hợp như "Bài 10" trực tiếp dưới "Toán")
                        Object[] row = new Object[5];
                        row[0] = rootTopic.getTpID();      // "Mã môn học"
                        row[1] = rootTopic.getTpTitle();   // "Tên môn học"
                        row[2] = "";                       // "Chuyên đề" - rỗng
                        row[3] = level1Topic.getTpTitle(); // "Bài học" - từ topic cấp 1
                        row[4] = level1Topic.getTpStatus() == 1 ? "Hoạt động" : "Tạm dừng"; // "Trạng thái"
                        model.addRow(row);
                    } else {
                        // Duyệt qua topic cấp 2 (bài học)
                        for (TopicDTO level2Topic : level2Topics) {
                            Object[] row = new Object[5];
                            row[0] = rootTopic.getTpID();      // "Mã môn học"
                            row[1] = rootTopic.getTpTitle();   // "Tên môn học"
                            row[2] = level1Topic.getTpTitle(); // "Chuyên đề"
                            row[3] = level2Topic.getTpTitle(); // "Bài học"
                            row[4] = level2Topic.getTpStatus() == 1 ? "Hoạt động" : "Tạm dừng"; // "Trạng thái"
                            model.addRow(row);
                        }
                    }
                }
            }
        }

        // Gán model cho table_monhoc
        table_monhoc.setModel(model);

        // Căn giữa dữ liệu trong các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table_monhoc.getColumnCount(); i++) {
            table_monhoc.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Cập nhật giao diện bảng
        table_monhoc.revalidate();
        table_monhoc.repaint();

        // Gọi hàm xử lý sự kiện hai lần nhấp
        twoClickToShowMessageOfRestore(table_monhoc);
    }

    private void loadDataSubjectTable(ArrayList<TopicDTO> topics) {
        model.setRowCount(0); // Xóa dữ liệu cũ trong model

        // Tạo map để tra cứu topic theo tpID
        Map<Integer, TopicDTO> topicMap = new HashMap<>();
        for (TopicDTO topic : topics) {
            topicMap.put(topic.getTpID(), topic);
        }

        // Tạo danh sách để lưu các topic theo cấp độ
        List<TopicDTO> rootTopics = new ArrayList<>();    // Topic gốc
        Map<Integer, List<TopicDTO>> childTopics = new HashMap<>(); // Topic con theo tpParent

        // Phân loại topic theo cấp độ
        for (TopicDTO topic : topics) {
            if (topic.getTpParent() == 0) {
                rootTopics.add(topic); // Thêm vào danh sách topic gốc
            } else {
                childTopics.computeIfAbsent(topic.getTpParent(), k -> new ArrayList<>()).add(topic); // Thêm vào danh sách topic con
            }
        }

        // Hiển thị topic gốc trước
        for (TopicDTO rootTopic : rootTopics) {
            Object[] rootRow = new Object[5];
            rootRow[0] = rootTopic.getTpID();      // "Mã môn học"
            rootRow[1] = rootTopic.getTpTitle();   // "Tên môn học"
            rootRow[2] = "-";                      // "Chủ đề" - không có
            rootRow[3] = "-";                      // "Bài học" - không có
            rootRow[4] = rootTopic.getTpStatus() == 1 ? "Hoạt động" : "Tạm dừng"; // "Trạng thái"
            model.addRow(rootRow);

            // Hiển thị topic cấp 1 và cấp 2
            List<TopicDTO> level1Topics = childTopics.getOrDefault(rootTopic.getTpID(), new ArrayList<>());
            for (TopicDTO level1Topic : level1Topics) {
                // Thêm dòng cho topic cấp 1
                Object[] level1Row = new Object[5];
                level1Row[0] = rootTopic.getTpID();      // "Mã môn học" - từ topic gốc
                level1Row[1] = rootTopic.getTpTitle();   // "Tên môn học" - từ topic gốc
                level1Row[2] = level1Topic.getTpTitle(); // "Chủ đề" - từ topic cấp 1
                level1Row[3] = "-";                      // "Bài học" - không có
                level1Row[4] = level1Topic.getTpStatus() == 1 ? "Hoạt động" : "Tạm dừng"; // "Trạng thái"
                model.addRow(level1Row);

                // Thêm các topic cấp 2 (bài học) của topic cấp 1
                List<TopicDTO> level2Topics = childTopics.getOrDefault(level1Topic.getTpID(), new ArrayList<>());
                for (TopicDTO level2Topic : level2Topics) {
                    Object[] level2Row = new Object[5];
                    level2Row[0] = rootTopic.getTpID();      // "Mã môn học" - từ topic gốc
                    level2Row[1] = rootTopic.getTpTitle();   // "Tên môn học" - từ topic gốc
                    level2Row[2] = level1Topic.getTpTitle(); // "Chủ đề" - từ topic cấp 1
                    level2Row[3] = level2Topic.getTpTitle(); // "Bài học" - từ topic cấp 2
                    level2Row[4] = level2Topic.getTpStatus() == 1 ? "Hoạt động" : "Tạm dừng"; // "Trạng thái"
                    model.addRow(level2Row);
                }
            }
        }
    }

    private void searchSubjectTable() {
        String keyword = txt_monhoc_timkiem.getText().trim().toLowerCase(); // Lấy từ khóa tìm kiếm

        DefaultTableModel model = (DefaultTableModel) table_monhoc.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table_monhoc.setRowSorter(sorter);

        // Nếu từ khóa rỗng, hiển thị tất cả dữ liệu
        if (keyword.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }

        // Tạo bộ lọc để tìm kiếm trong cột 1 (Tên môn học), cột 2 (Chủ đề) và cột 3 (Bài học)
        RowFilter<DefaultTableModel, Object> rowFilter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                for (int i = 1; i <= 3; i++) { // Duyệt qua cột 1, 2, 3
                    String value = entry.getStringValue(i).toLowerCase();
                    if (value.contains(keyword)) {
                        return true; // Nếu có chứa từ khóa thì hiển thị dòng này
                    }
                }
                return false;
            }
        };

        sorter.setRowFilter(rowFilter);
    }

    private void restore(TopicDTO topic, JTable table) {
        topic.setTpStatus(1);
        int status = topic.getTpStatus();
        TopicBUS tpBUS = new TopicBUS();
        boolean result = tpBUS.update(topic);
        if (result) {
            JOptionPane.showMessageDialog(table, "Cập nhật thành công");
        } else {
            JOptionPane.showMessageDialog(table, "Cập nhật thất bại");
        }
    }

    private void twoClickToShowMessageOfRestore(JTable table) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) {  // Kiểm tra click đúp
                    e.consume();  // Đánh dấu sự kiện đã được xử lý
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        TopicBUS tpBUS = new TopicBUS();
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        String monHoc = model.getValueAt(selectedRow, 1).toString();
                        TopicDTO topic = tpBUS.findOneTitle(monHoc);
                        if (topic.getTpStatus() == 0) {
                            int confirm = JOptionPane.showConfirmDialog(table, "Bạn muốn khôi phục trạng thái hoạt động ?", "Xác nhận cập nhật", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                restore(topic, table);
                                loadDataSubjectTable();
                            }
                        }
                    }
                }
            }
        });
    }

    private void loadParentIDComboBox() {
        cbb_monhoc_monhoc.removeAllItems();
        cbb_monhoc_monhoc.addItem("--Trống--");
        cbb_monhoc_monhoc.setSelectedIndex(0);
        parentTitleToIdMap.clear();
        parentTitleToIdMap.put("--Trống--", 0);

        List<TopicDTO> topics = tpBUS.getAll();
        if (topics == null || topics.isEmpty()) {
            return;
        }

        for (TopicDTO topic : topics) {
            if (topic.getTpStatus() != 0 && topic.getTpParent() == 0) {
                cbb_monhoc_monhoc.addItem(topic.getTpTitle());
                parentTitleToIdMap.put(topic.getTpTitle(), topic.getTpID());
            }
        }
    }

    private void loadSubTopicComboBox() {
        cbb_monhoc_chude.removeAllItems();
        cbb_monhoc_chude.addItem("--Trống--");
        cbb_monhoc_chude.setSelectedIndex(0);
        subTopicTitleToIdMap.clear();
        subTopicTitleToIdMap.put("--Trống--", 0);

        int selectedParentId = getSelectedParentId();
        if (selectedParentId == 0) {
            return; // Không load nếu chọn "--Trống--"
        }

        List<TopicDTO> topics = tpBUS.getAll();
        if (topics == null || topics.isEmpty()) {
            return;
        }

        for (TopicDTO topic : topics) {
            if (topic.getTpStatus() != 0 && topic.getTpParent() == selectedParentId) {
                cbb_monhoc_chude.addItem(topic.getTpTitle());
                subTopicTitleToIdMap.put(topic.getTpTitle(), topic.getTpID());
            }
        }
    }

    private int getSelectedParentId() {
        String selectedTitle = (String) cbb_monhoc_monhoc.getSelectedItem();
        if (selectedTitle == null || selectedTitle.equals("--Trống--")) {
            return 0;
        }
        return parentTitleToIdMap.getOrDefault(selectedTitle, 0);
    }

    private int getSelectedSubTopicId() {
        String selectedTitle = (String) cbb_monhoc_chude.getSelectedItem();
        if (selectedTitle == null || selectedTitle.equals("--Trống--")) {
            return 0;
        }
        return subTopicTitleToIdMap.getOrDefault(selectedTitle, 0);
    }

    private void filterTable() {
        int selectedParentId = getSelectedParentId();    // Từ cbb_monhoc_monhoc
        int selectedSubTopicId = getSelectedSubTopicId(); // Từ cbb_monhoc_chude

        DefaultTableModel model = (DefaultTableModel) table_monhoc.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ trong bảng

        ArrayList<TopicDTO> topics = tpBUS.getAll();
        ArrayList<TopicDTO> tpFilter = new ArrayList<>();
        if (topics == null || topics.isEmpty()) {
            return;
        }

        // Tạo set chứa tất cả tpID liên quan
        Set<Integer> relatedIds = new HashSet<>();

        if (selectedParentId == 0 && selectedSubTopicId == 0) {
            // Hiển thị tất cả topic nếu không chọn gì
            tpFilter.addAll(topics);
        } else if (selectedSubTopicId == 0 && selectedParentId != 0) {
            // Khi chỉ chọn topic cha: hiển thị tất cả topic liên quan đến topic cha
            relatedIds.add(selectedParentId); // Thêm topic cha
            collectRelatedTopics(selectedParentId, topics, relatedIds);
            for (TopicDTO topic : topics) {
                if (topic.getTpStatus() != 0 && relatedIds.contains(topic.getTpID())) {
                    tpFilter.add(topic);
                }
            }
        } else if (selectedSubTopicId != 0) {

            tpFilter.clear();
            relatedIds.add(selectedSubTopicId); // Thêm topic con
            collectRelatedTopics(selectedSubTopicId, topics, relatedIds);
            for (TopicDTO topic : topics) {
                if (topic.getTpStatus() != 0 && relatedIds.contains(topic.getTpID())) {
                    tpFilter.add(topic);
                }
            }
        }

        loadDataSubjectTable(tpFilter); // Load dữ liệu đã lọc lên bảng
    }

    private void collectRelatedTopics(int parentId, ArrayList<TopicDTO> topics, Set<Integer> relatedIds) {
        for (TopicDTO topic : topics) {
            if (topic.getTpParent() == parentId) {
                relatedIds.add(topic.getTpID());
                // Đệ quy để lấy các topic con sâu hơn
                collectRelatedTopics(topic.getTpID(), topics, relatedIds);
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

        jPanel3 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        txt_monhoc_timkiem = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        cbb_monhoc_monhoc = new javax.swing.JComboBox<>();
        cbb_monhoc_chude = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        table_monhoc = new javax.swing.JTable();

        jPanel3.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel18.setText("Tìm kiếm");

        txt_monhoc_timkiem.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
        txt_monhoc_timkiem.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập môn học . . . . . . . . . . . . . . . . . . .");
        txt_monhoc_timkiem.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jButton3.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #0bae1d; foreground: #ffffff;");
        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton3.setIcon(new FlatSVGIcon("icons/search.svg", 25, 25)
        );
        jButton3.setText("Tìm");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        cbb_monhoc_monhoc.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Danh sách môn học");
        cbb_monhoc_monhoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbb_monhoc_monhocActionPerformed(evt);
            }
        });

        cbb_monhoc_chude.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Danh sách chủ đề");
        cbb_monhoc_chude.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbb_monhoc_chudeActionPerformed(evt);
            }
        });

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
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_monhoc_timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbb_monhoc_monhoc, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbb_monhoc_chude, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

        jButton6.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
        jButton6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton6.setIcon(new FlatSVGIcon("icons/add.svg", 30, 30)
        );
        jButton6.setText("Tạo mới");
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
        jButton7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton7.setIcon(new FlatSVGIcon("icons/detail.svg", 30, 30)
        );
        jButton7.setText("Chi tiết");
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ee2020; foreground: #ffffff;");
        jButton8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton8.setIcon(new FlatSVGIcon("icons/delete.svg", 30, 30)
        );
        jButton8.setText("Xóa");
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
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
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6)
                .addContainerGap())
            .addComponent(jScrollPane3)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton7)
                    .addComponent(jButton8))
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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        searchSubjectTable();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        addSubject panel = new addSubject(null, false);

        // Gọi showCustomDialog() và lấy JDialog
        JDialog dialog = mainView.showCustomDialog1(null, panel, "Thêm môn học");

        // Thêm WindowListener để reload dữ liệu khi dialog đóng
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                loadDataSubjectTable();
            }
        });

        // Hiển thị dialog
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:

        int selectedRow = table_monhoc.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) table_monhoc.getModel();
            String ID = model.getValueAt(selectedRow, 1).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Bán muốn xóa dữ liệu này ?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                TopicDTO topic = tpBUS.findOneTitle(ID);
                boolean result = tpBUS.delete(topic);
                if (result) {
//                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Xóa thành công");
//                    for(int i = 0;i < model.getRowCount();i++)
//                        model.setValueAt(i + 1, i, 0);
                    loadDataSubjectTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dự liệu muốn xóa");
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private TopicDTO getSelectedTopicFromTable(int selectedRow) {

        String level1 = (String) table_monhoc.getValueAt(selectedRow, 1);
        String level2 = (String) table_monhoc.getValueAt(selectedRow, 2);
        String level3 = (String) table_monhoc.getValueAt(selectedRow, 3);

        // Tìm topic tương ứng theo cấp thấp nhất có giá trị
        if (level3 != null && !level3.equals("-")) {
            return tpBUS.findTopicByTitle(level3);
        } else if (level2 != null && !level2.equals("-")) {
            return tpBUS.findTopicByTitle(level2);
        } else if (level1 != null && !level1.equals("-")) {
            return tpBUS.findTopicByTitle(level1);
        }

        return null; // Nếu không tìm thấy
    }


    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        int selectedRow = table_monhoc.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(table_monhoc, "Vui lòng chọn môn/chủ đề");
            return;
        }
        TopicDTO topic = getSelectedTopicFromTable(selectedRow);

        addSubject panel = new addSubject(topic, true);

        // Gọi showCustomDialog() và lấy JDialog
        JDialog dialog = mainView.showCustomDialog1(null, panel, "Sửa môn học");

        // Thêm WindowListener để reload dữ liệu khi dialog đóng
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                loadDataSubjectTable();
            }
        });

        // Hiển thị dialog
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void cbb_monhoc_monhocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbb_monhoc_monhocActionPerformed
        filterTable(); // Lọc bảng theo môn học cha
        loadSubTopicComboBox();
    }//GEN-LAST:event_cbb_monhoc_monhocActionPerformed

    private void cbb_monhoc_chudeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbb_monhoc_chudeActionPerformed
        filterTable();
    }//GEN-LAST:event_cbb_monhoc_chudeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbb_monhoc_chude;
    private javax.swing.JComboBox<String> cbb_monhoc_monhoc;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable table_monhoc;
    private javax.swing.JTextField txt_monhoc_timkiem;
    // End of variables declaration//GEN-END:variables
}
