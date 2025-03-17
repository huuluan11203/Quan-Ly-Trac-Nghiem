/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tracnghiem.view.panel;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.tracnghiem.bus.QuestionBUS;
import com.tracnghiem.bus.TopicBUS;
import com.tracnghiem.dto.QuestionDTO;
import com.tracnghiem.dto.TopicDTO;
import com.tracnghiem.view.components.addQuestion;
import com.tracnghiem.view.components.detailQuestion;
import com.tracnghiem.view.components.updateQuestion;
import com.tracnghiem.view.loginView;
import com.tracnghiem.view.mainView;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
/**
 *
 * @author X
 */
public class CauHoiPanel extends javax.swing.JPanel {
    private final QuestionBUS qBUS = new QuestionBUS();
    private final TopicBUS tBUS = new TopicBUS();
    private int idTopicParent = -1, idTopicChild = -1, idTopicChild1 = -1, qIDSelected = -1;
    private String qLevel = "--none--";
    private final Map<String, Integer> topicMapParent = new LinkedHashMap<>();
    private final Map<String, Integer> topicMapChildren = new LinkedHashMap<>();
    private final Map<String, Integer> topicMapChildren1 = new LinkedHashMap<>();
    private ArrayList<QuestionDTO> listQ = new ArrayList<>();

    private boolean isUpdatingComboBox = false;
    /**
     * Creates new form cauhoiPanel
     */
    public CauHoiPanel() {
        initComponents();
        
        
        
        
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Kiểm tra click đúp
                    int row = jTable1.getSelectedRow();
                    if (row != -1) {
                        qIDSelected = (int) jTable1.getValueAt(row, 0);
                    }
                }
            }
        });

        listQ = qBUS.getAll();
        loadTableQuestion(listQ);
        loadTpParent(tBUS.getAllParent());

    }

    private void loadTpParent(ArrayList<TopicDTO> list) {
        topicMapParent.clear();
        topicMapParent.put("--None--", -1);

        for (TopicDTO t : list) {
            topicMapParent.put(t.getTpTitle(), t.getTpID());
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        for (String s : topicMapParent.keySet()) {
            model.addElement(s);
        }
        cbbF.setModel(model);
    }

    private void loadTpChild1(int idParent) {
        topicMapChildren1.clear();
        topicMapChildren1.put("--None--", -1);

        ArrayList<TopicDTO> list = tBUS.getChildTopics(idParent);

        for (TopicDTO t : list) {
            topicMapChildren1.put(t.getTpTitle(), t.getTpID());
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String s : topicMapChildren1.keySet()) {
            model.addElement(s);
        }
        cbbT.setModel(model);
        cbbT.setSelectedIndex(0);
        
       
    }

    private void loadTpChild(int idParent) {
        topicMapChildren.clear();
        topicMapChildren.put("--None--", -1);
        
        ArrayList<TopicDTO> list = tBUS.getChildTopics(idParent);

        for (TopicDTO t : list) {
            topicMapChildren.put(t.getTpTitle(), t.getTpID());
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String s : topicMapChildren.keySet()) {
            model.addElement(s);
        }
        cbbS.setModel(model);
        cbbS.setSelectedIndex(0);
         loadTpChild1(-1);
    }

    private void loadTableQuestion(ArrayList<QuestionDTO> list) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        if (list.isEmpty()) {
            model.addRow(new Object[]{"", "", "Không có dữ liệu", "", "", ""});
            return;
        }

        for (QuestionDTO question : list) {
            TopicDTO t = tBUS.findOne(question.getQTopic());
            model.addRow(new Object[]{
                question.getQID(),
                t == null ? "" : t.getTpTitle(),
                question.getQContent(),
                question.getQLevel(),
                question.getQPictures(),
                question.getQStatus() == 1 ? "Active" : "Hidden"
            });
        }
    }

    private void loadTableByTopic(int idParent, int idChild, int idChild1) {

        if (idParent != -1 && idChild == -1 && idChild1 == -1) {
            listQ = qBUS.getByTopicParent(idParent);
        } else if (idChild != -1 && idParent != -1 && idChild1 == -1) {
            listQ = qBUS.getByTopicParent(idChild);
        } else if (idChild != -1 && idParent != -1 && idChild1 != -1) {
            listQ = qBUS.getByTopic(idChild1);
        } else {
            listQ = qBUS.getAll();
        }

        if (!qLevel.equalsIgnoreCase("--none--")) {
            ArrayList<QuestionDTO> rs = new ArrayList<>();
            for (int i = 0; i < listQ.size(); i++) {
                if (listQ.get(i).getQLevel().toLowerCase().equalsIgnoreCase(qLevel)) {
                    rs.add(listQ.get(i));
                }
            }
            listQ = rs;
        }
//
        loadTableQuestion(listQ);
    }

    private void setColumnWidthsForTableQuestion() {
        TableColumnModel columnModel = jTable1.getColumnModel();
        int totalWidth = jTable1.getWidth(); // Lấy chiều rộng tổng của bảng

        
        System.out.println("check: " + totalWidth);
        
        double[] columnRatios = {0.05, 0.15, 0.55, 0.1, 0.15, 0.1};

        for (int i = 0; i < columnRatios.length; i++) {
            int columnWidth = (int) (totalWidth * columnRatios[i]);
            columnModel.getColumn(i).setPreferredWidth(columnWidth);
        }
    }

    private void handleSearchQuestion() {
        String key = jtfSearch.getText().toLowerCase().trim();
        if (idTopicChild != -1 || idTopicParent != -1 || !qLevel.equalsIgnoreCase("--none--")) {
            ArrayList<QuestionDTO> rs = new ArrayList<>();
            for (QuestionDTO q : listQ) {
                if (q.getQContent().toLowerCase().contains(key.toLowerCase()) || String.valueOf(q.getQID()).equals(key)) {
                    rs.add(q);
                }
            }
            loadTableQuestion(rs);
        } else {
            listQ = qBUS.search(key, "Tất cả");
            loadTableQuestion(listQ);
        }
    }

    
    
    private void refresh() {
        listQ = qBUS.getAll();
        loadTableQuestion(listQ);

        jtfSearch.setText("");

        qIDSelected = -1;
        idTopicParent = -1;
        idTopicChild = -1;
        idTopicChild1 = -1;
        qLevel = "--none--";

        loadTpParent(tBUS.getAllParent());

        topicMapChildren.clear();
        topicMapChildren.put("--None--", -1);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String s : topicMapChildren.keySet()) {
            model.addElement(s);
        }
        cbbS.setModel(model);

        topicMapChildren1.clear();
        topicMapChildren1.put("--None--", -1);
        DefaultComboBoxModel<String> model1 = new DefaultComboBoxModel<>();
        for (String s : topicMapChildren.keySet()) {
            model1.addElement(s);
        }
        cbbT.setModel(model1);

        cbbLevel.setSelectedIndex(0);

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
        jLabel13 = new javax.swing.JLabel();
        jtfSearch = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        cbbF = new javax.swing.JComboBox<>();
        cbbS = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        cbbT = new javax.swing.JComboBox<>();
        refreshBtn = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        cbbLevel = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();

        jPanel1.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel13.setText("Tìm kiếm");

        jtfSearch.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
        jtfSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập câu hỏi . . . . . . . . . . . . . . . . . . .");
        jtfSearch.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jtfSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfSearchActionPerformed(evt);
            }
        });
        jtfSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfSearchKeyPressed(evt);
            }
        });

        searchBtn.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #0bae1d; foreground: #ffffff;");
        searchBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        searchBtn.setIcon(new FlatSVGIcon("icons/search.svg", 25, 25)
        );
        searchBtn.setText("Tìm");
        searchBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        cbbF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbFActionPerformed(evt);
            }
        });

        cbbS.addItem("--None--");
        cbbS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbSActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel16.setText("Môn học");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel17.setText("Chủ đề");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel24.setText("Bài học");

        cbbT.addItem("--None--");
        cbbT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbTActionPerformed(evt);
            }
        });

        refreshBtn.setIcon(new FlatSVGIcon("icons/refresh.svg", 30 ,30)) ;
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel25.setText("Mức độ");

        cbbLevel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--None--", "Easy", "Medium", "Diff" }));
        cbbLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbLevelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(114, 114, 114))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jtfSearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbbF, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbbS, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbbT, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(cbbLevel, 0, 0, Short.MAX_VALUE)))
                .addGap(6, 6, 6)
                .addComponent(refreshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbbLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtfSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbF, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbS, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbT, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(refreshBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        refreshBtn.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #0bae1d; foreground: #ffffff;");

        jPanel2.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

        jButton2.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton2.setIcon(new FlatSVGIcon("icons/add.svg", 30, 30)
        );
        jButton2.setText("Tạo mới");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

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
        SwingUtilities.invokeLater(() -> setColumnWidthsForTableQuestion());

        jButton4.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton4.setIcon(new FlatSVGIcon("icons/detail.svg", 30, 30)
        );
        jButton4.setText("Chi tiết");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ee2020; foreground: #ffffff;");
        jButton5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton5.setIcon(new FlatSVGIcon("icons/delete.svg", 30, 30)
        );
        jButton5.setText("Xóa");
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton9.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
        jButton9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton9.setIcon(new FlatSVGIcon("icons/edit.svg", 30, 30)
        );
        jButton9.setText("Sửa");
        jButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(702, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap())
            .addComponent(jScrollPane2)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtfSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfSearchActionPerformed

    private void jtfSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfSearchKeyPressed
        // TODO add your handling code here:
        // Kiểm tra nếu phím Enter được nhấn
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            handleSearchQuestion();
        }
    }//GEN-LAST:event_jtfSearchKeyPressed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        // TODO add your handling code here:
        handleSearchQuestion();
    }//GEN-LAST:event_searchBtnActionPerformed

    private void cbbFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbFActionPerformed
        // TODO add your handling code here:
//        if (isUpdatingComboBox) return;  // Nếu đang cập nhật thì bỏ qua
//        isUpdatingComboBox = true; // Đánh dấu đang cập nhật combobox khác
        String selectedTopic = (String) cbbF.getSelectedItem();
        Integer selectedID = topicMapParent.get(selectedTopic);
        // Kiểm tra nếu selectedID là null thì không làm gì cả
        if (selectedID == null) {            
//            isUpdatingComboBox = false; // Xong việc, bỏ cờ

            return;
        }
//        loadTableByTopic(idTopicParent, idTopicChild, idTopicChild1);
        loadTpChild(selectedID);
        idTopicParent = selectedID;
        idTopicChild = -1;
        idTopicChild1 = -1;
        cbbLevel.setSelectedIndex(0);
        
//        isUpdatingComboBox = false; // Xong việc, bỏ cờ

    }//GEN-LAST:event_cbbFActionPerformed

    private void cbbSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbSActionPerformed
        // TODO add your handling code here:
        if (isUpdatingComboBox) return;  // Nếu đang cập nhật thì bỏ qua
        isUpdatingComboBox = true; // Đánh dấu đang cập nhật combobox khác

        String selectedTopic = (String) cbbS.getSelectedItem();
        Integer selectedID = topicMapChildren.get(selectedTopic);
        // Kiểm tra nếu selectedID là null thì không làm gì cả
        if (selectedID == null) {
            isUpdatingComboBox = false; // Xong việc, bỏ cờ
            return;
        }
        idTopicChild = selectedID;
        loadTableByTopic(idTopicParent, idTopicChild, idTopicChild1);
        loadTpChild1(selectedID);
        idTopicChild = selectedID;
        idTopicChild1 = -1;
        cbbT.setSelectedIndex(0);
        cbbLevel.setSelectedIndex(0);

        isUpdatingComboBox = false; // Xong việc, bỏ cờ
    }//GEN-LAST:event_cbbSActionPerformed

    private void cbbTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbTActionPerformed
        // TODO add your handling code here:
        if (isUpdatingComboBox) return;  // Nếu đang cập nhật thì bỏ qua

        isUpdatingComboBox = true; // Đánh dấu đang cập nhật combobox khác

        String selectedTopic = (String) cbbT.getSelectedItem();
        Integer selectedID = topicMapChildren1.get(selectedTopic);
        // Kiểm tra nếu selectedID là null thì không làm gì cả
        if (selectedID == null) {
            isUpdatingComboBox = false; // Xong việc, bỏ cờ
            return;
        }
        idTopicChild1 = selectedID;
        loadTableByTopic(idTopicParent, idTopicChild, idTopicChild1);
        cbbLevel.setSelectedIndex(0);
        isUpdatingComboBox = false; // Xong việc, bỏ cờ
    }//GEN-LAST:event_cbbTActionPerformed

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        // TODO add your handling code here:
        refresh();
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void cbbLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbLevelActionPerformed
        // TODO add your handling code here:
        if (isUpdatingComboBox) return;  // Nếu đang cập nhật thì bỏ qua

        isUpdatingComboBox = true; // Đánh dấu đang cập nhật combobox khác

        String selectedLevel = (String) cbbLevel.getSelectedItem();
        qLevel = selectedLevel.toLowerCase();
        loadTableByTopic(idTopicParent, idTopicChild, idTopicChild1);
        isUpdatingComboBox = false; // Xong việc, bỏ cờ
    }//GEN-LAST:event_cbbLevelActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //Open

        mainView.showCustomDialog(null, new addQuestion(null, false), "Thêm câu hỏi");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if (qIDSelected == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhấp đôi để chọn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int qID = qIDSelected;

        mainView.showCustomDialog(null, new detailQuestion(qID), "Chi tiết");
        qIDSelected = -1;
    }//GEN-LAST:event_jButton4ActionPerformed

    private void handleDeleteQues() {
        if (qIDSelected == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhấp đôi để chọn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int qID = qIDSelected;

        // Hiển thị hộp thoại xác nhận trước khi xóa
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xoá câu hỏi ID = " + qID + "?",
            "Xác nhận xoá",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean check = qBUS.delete(qBUS.findOne(qID));

            if (check) {
                JOptionPane.showMessageDialog(
                    this,
                    "Xoá thành công câu hỏi ID = " + qID,
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Xoá thất bại! Vui lòng thử lại.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        handleDeleteQues();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        if (qIDSelected == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhấp đôi để chọn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int qID = qIDSelected;

        mainView.showCustomDialog(null, new updateQuestion(qBUS.findOne(qID), false), "Chỉnh sửa");
        qIDSelected = -1;
    }//GEN-LAST:event_jButton9ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbbF;
    private javax.swing.JComboBox<String> cbbLevel;
    private javax.swing.JComboBox<String> cbbS;
    private javax.swing.JComboBox<String> cbbT;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jtfSearch;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JButton searchBtn;
    // End of variables declaration//GEN-END:variables
}
