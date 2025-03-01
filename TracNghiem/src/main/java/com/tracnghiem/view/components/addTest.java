package com.tracnghiem.view.components;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.tracnghiem.bus.ExamBUS;
import com.tracnghiem.bus.QuestionBUS;
import com.tracnghiem.bus.TestBUS;
import com.tracnghiem.bus.TopicBUS;
import com.tracnghiem.dto.ExamDTO;
import com.tracnghiem.dto.QuestionDTO;
import com.tracnghiem.dto.TestDTO;
import com.tracnghiem.dto.TopicDTO;
import com.tracnghiem.utils.RandomListsUtil;
import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.text.NumberFormatter;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author huulu
 */
public class addTest extends javax.swing.JPanel {

    private final Map<String, Integer> topicMapParent = new LinkedHashMap<>();
    private final Map<String, Integer> topicMapChildren = new LinkedHashMap<>();
    private final TopicBUS tpBUS = new TopicBUS();
    private final QuestionBUS qBUS = new QuestionBUS();
    private final ExamBUS eBUS = new ExamBUS();
    private final TestBUS tBUS = new TestBUS();
    private ArrayList<QuestionDTO> listQ = new ArrayList<>();
    private int idTopicParent = -1, idTopicChildren = -1, qIDSelected = -1, rowSelected = -1;
    private String qLevel = "--None--";
    private int newTestID = -1;
    private ArrayList<QuestionDTO> listQSelected = new ArrayList<>();
    private LocalDate dateSelected = null;
    private final ArrayList<String> listExOrder = new ArrayList<>(Arrays.asList(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"
    ));

    /**
     * Creates new form addTest
     */
    public addTest(TestDTO test, boolean update) {
        initComponents();

        newTestID = tBUS.getMaxID() + 1;

        jLabel1.setText(newTestID + "");

        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 1) { // Kiểm tra click
                    qIDSelected = -1;
                    rowSelected = -1;
                    int row = jTable2.getSelectedRow();
                    if (row != -1) {
                        qIDSelected = (int) jTable2.getValueAt(row, 0);
                    }
                }
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 1) { // Kiểm tra click 
                    qIDSelected = -1;
                    rowSelected = -1;
                    int row = jTable1.getSelectedRow();
                    if (row != -1) {
                        rowSelected = row;
                        qIDSelected = (int) jTable1.getValueAt(row, 0);
                    }
                }
            }
        });

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

        loadTpParent();

    }

    private void setColumnWidths() {
        TableColumnModel columnModel = jTable2.getColumnModel();
        int totalWidth = jTable2.getWidth(); // Lấy chiều rộng tổng của bảng

        TableColumnModel columnModel1 = jTable1.getColumnModel();
        int totalWidth1 = jTable1.getWidth(); // Lấy chiều rộng tổng của bảng

        double[] columnRatios = {0.1, 0.5, 0.3, 0.1};

        for (int i = 0; i < columnRatios.length; i++) {
            int columnWidth = (int) (totalWidth * columnRatios[i]);
            columnModel.getColumn(i).setPreferredWidth(columnWidth);

            int columnWidth1 = (int) (totalWidth1 * columnRatios[i]);
            columnModel1.getColumn(i).setPreferredWidth(columnWidth1);

        }
    }

    private void loadTpParent() {
        topicMapParent.clear();
        topicMapParent.put("--None--", -1);

        for (TopicDTO t : tpBUS.getAll()) {
            topicMapParent.put(t.getTpTitle(), t.getTpID());
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        for (String s : topicMapParent.keySet()) {
            model.addElement(s);
        }
        monhocCBB.setModel(model);
    }

    private void loadTpChildren(int idParent) {
        topicMapChildren.clear();
        topicMapChildren.put("--None--", -1);
        for (TopicDTO t : tpBUS.getAllChildTopics(idParent)) {
            topicMapChildren.put(t.getTpTitle(), t.getTpID());
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        for (String s : topicMapChildren.keySet()) {
            model.addElement(s);
        }
        parentID.setModel(model);
    }

    private void loadTableByTopic(int idParent, int idChild) {

        if (idParent != -1 && idChild == -1) {
            listQ = qBUS.getByTopicParent(idParent);
        } else if (idChild != -1 && idParent != -1) {
            listQ = qBUS.getByTopicParent(idChild);
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

    private void loadTableQuestion(ArrayList<QuestionDTO> list) {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        if (list.isEmpty()) {
            model.addRow(new Object[]{"", "Không có dữ liệu", "", ""});
            return;
        }

        for (QuestionDTO question : list) {
            model.addRow(new Object[]{
                question.getQID(),
                question.getQContent(),
                tpBUS.findOne(question.getQTopic()).getTpTitle(),
                question.getQLevel()});
        }
    }

    private void handleSearch(String key) {

        if (idTopicChildren != -1 || idTopicParent != -1 || !qLevel.equalsIgnoreCase("--none--")) {
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

    private void countQuantityTypeQuestion() {
        int[] rs = {0, 0, 0};

        for (QuestionDTO q : listQSelected) {
            if (q.getQLevel().equalsIgnoreCase("easy")) {
                rs[0]++;
            }
            if (q.getQLevel().equalsIgnoreCase("medium")) {
                rs[1]++;
            }
            if (q.getQLevel().equalsIgnoreCase("diff")) {
                rs[2]++;
            }
        }

        totalEasy.setText(rs[0] + "");
        totalMedium.setText(rs[1] + "");
        totalDiff.setText(rs[2] + "");
        totalQuestion.setText(listQSelected.size() + "");
    }

    private void addQuestionToTable() {
        QuestionDTO qAdd = qBUS.findOne(qIDSelected);

        for (QuestionDTO q : listQSelected) {
            if (q.getQID() == qIDSelected) {
                JOptionPane.showMessageDialog(null, "Câu hỏi có ID=" + qIDSelected + " đã bị trùng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        listQSelected.add(qAdd);
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.addRow(new Object[]{
            qAdd.getQID(),
            qAdd.getQContent(),
            tpBUS.findOne(qAdd.getQTopic()).getTpTitle(),
            qAdd.getQLevel()});

        

    }

    private boolean validDataAddNew() {
        if (testTitle.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên bài kiểm tra trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (idTopicParent == -1) {
            JOptionPane.showMessageDialog(null, "Hãy chọn chủ đề!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (dateSelected == null) {
            JOptionPane.showMessageDialog(null, "Hãy chọn ngày!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (dateSelected.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(null, "Ngày thi không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (testCode.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã bài thi trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (tBUS.isExistTestCode(testCode.getText().trim())) {
            JOptionPane.showMessageDialog(null, "Mã bài thi bị trùng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (testTime.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Thời gian làm bài trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (listQSelected.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Hãy chọn câu hỏi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void buildData() {
        
        TestDTO tNew = new TestDTO(
                testCode.getText(), 
                testTitle.getText(),
                Integer.parseInt(testTime.getText()),
                idTopicParent,
                Integer.parseInt(totalEasy.getText()),
                Integer.parseInt(totalMedium.getText()),
                Integer.parseInt(totalDiff.getText()),
                (int)testLimit.getValue(),
                dateSelected,
                1
        
        );
        
        ArrayList<Integer> listIDs = new ArrayList<>();
        for (QuestionDTO q : listQSelected) {
            listIDs.add(q.getQID());
        }
        

        int k = (int) quantityExams.getValue(); 
        ArrayList<String> listRandomIDs = RandomListsUtil.generateUniqueRandomLists(listIDs, k);

        
        
        
        
        ArrayList<ExamDTO> listE = new ArrayList<>();
        ExamDTO e;
        for (int i = 0; i < k; i++) {
            e = new ExamDTO(tNew.getTestCode(),
                    listExOrder.get(i), 
                    tNew.getTestCode().concat(listExOrder.get(i)), 
                    listRandomIDs.get(i));
            
            listE.add(e);
        }
       
        
        if (!tBUS.add(tNew)) {
            JOptionPane.showMessageDialog(null, "Thêm bài thi thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return ;   
        }
        
        for (ExamDTO exam : listE) {
                System.out.println(exam.toString());}
        for (ExamDTO exam : listE) {
                System.out.println("");
                if (!eBUS.add(exam)) {
                    JOptionPane.showMessageDialog(null, "Thêm đề thi thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return ; 
                }
        }
        
        JOptionPane.showMessageDialog(null, "Thêm bài thi thành công!", "Thông", JOptionPane.INFORMATION_MESSAGE);
        java.awt.Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JDialog) {
                ((JDialog) window).dispose();
            }
    }
    
    
    
    private void addNew() {
        buildData();
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

        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        levelCBB = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        parentID = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        tim_btn1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        tim_btn2 = new javax.swing.JButton();
        tim_btn3 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        SpinnerNumberModel model1 = new SpinnerNumberModel(1, 1, 10, 1);
        quantityExams = new javax.swing.JSpinner(model1);
        jLabel13 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        totalQuestion = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        totalDiff = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        totalMedium = new javax.swing.JLabel();
        totalEasy = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        monhocCBB = new javax.swing.JComboBox<>();
        testTitle = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        testTime = new javax.swing.JTextField();
        time = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
        testLimit = new javax.swing.JSpinner(model);
        jLabel8 = new javax.swing.JLabel();
        luu = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        testCode = new javax.swing.JTextField();
        jButton16 = new javax.swing.JButton();

        jPanel2.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel6.setText("Độ khó");

        monhocCBB.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Danh sách môn học");
        monhocCBB.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
        levelCBB.setToolTipText("");
        levelCBB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--None--", "Easy", "Medium", "Diff" }));
        levelCBB.setName(""); // NOI18N
        levelCBB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                levelCBBActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        parentID.setEditable(true);
        parentID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parentIDActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel9.setText("Chủ đề");

        jTextField8.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
        jTextField8.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập câu hỏi . . . . . . . . . . . . . . .");
        jTextField8.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel10.setText("Tìm kiếm");

        tim_btn1.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
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

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã câu hỏi", "Nội dung", "Chủ đề","Độ khó"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }

        });
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã câu hỏi", "Nội dung", "Chủ đề","Độ khó"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }

        }
    );
    jScrollPane2.setViewportView(jTable2);
    SwingUtilities.invokeLater(() -> setColumnWidths());

    tim_btn2.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
    tim_btn2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    tim_btn2.setIcon(new FlatSVGIcon("icons/add.svg", 25, 25)
    );
    tim_btn2.setText("Thêm câu hỏi");
    tim_btn2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    tim_btn2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            tim_btn2ActionPerformed(evt);
        }
    });

    tim_btn3.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
    tim_btn3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    tim_btn3.setIcon(new FlatSVGIcon("icons/detail.svg", 25, 25)
    );
    tim_btn3.setText("Xem");
    tim_btn3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    tim_btn3.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            tim_btn3ActionPerformed(evt);
        }
    });

    jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
    jLabel11.setText("Câu hỏi đã chọn:");

    jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
    jLabel12.setText("Tạo đề thi:");

    quantityExams.putClientProperty(FlatClientProperties.STYLE, "arc :10;");

    jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel13.setText("Số lượng đề thi (A, B, C, D, E,. . . . . .)");

    jButton5.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ee2020; foreground: #ffffff;");
    jButton5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jButton5.setIcon(new FlatSVGIcon("icons/delete.svg", 30, 30)
    );
    jButton5.setText("Bỏ chọn");
    jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jButton5.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton5ActionPerformed(evt);
        }
    });

    jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel16.setText("Tổng số câu");

    totalQuestion.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
    totalQuestion.setText("0");

    jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel17.setText("SL khó");

    totalDiff.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
    totalDiff.setText("0");

    jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel18.setText("SL trung");

    totalMedium.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
    totalMedium.setText("0");

    totalEasy.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
    totalEasy.setText("0");

    jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel19.setText("SL dễ");

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane2)
        .addComponent(jScrollPane1)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(levelCBB, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(parentID, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(5, 5, 5)
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel7)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(tim_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(tim_btn2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tim_btn3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton5))
                        .addComponent(jLabel12)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel13)
                                .addComponent(quantityExams, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel11)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(totalEasy, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel18)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(totalMedium, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel17)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(totalDiff, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel16)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(totalQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel6)
                .addComponent(jLabel7)
                .addComponent(jLabel9)
                .addComponent(jLabel10))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(levelCBB, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(parentID, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(tim_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tim_btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tim_btn3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(totalEasy))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(totalMedium))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(totalDiff))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel16)
                    .addComponent(totalQuestion)))
            .addGap(5, 5, 5)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel12)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel13)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(quantityExams, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(14, Short.MAX_VALUE))
    );

    AutoCompleteDecorator.decorate(parentID);
    parentID.setMaximumRowCount(5);
    // Định dạng lại phần nhập số (loại bỏ dấu phân cách)
    JSpinner.DefaultEditor editor1 = (JSpinner.DefaultEditor) quantityExams.getEditor();
    JFormattedTextField textField1 = editor1.getTextField();
    NumberFormatter formatter1 = (NumberFormatter) textField1.getFormatter();
    formatter1.setAllowsInvalid(false);  // Không cho phép nhập ký tự không hợp lệ
    formatter1.setCommitsOnValidEdit(true); // Tự động cập nhật khi nhập số hợp lệ

    jPanel3.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

    jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel2.setText("Tên bài kiểm tra");

    monhocCBB.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Danh sách môn học");
    monhocCBB.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
    monhocCBB.setEditable(true);
    monhocCBB.setToolTipText("");
    monhocCBB.setName(""); // NOI18N
    monhocCBB.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            monhocCBBActionPerformed(evt);
        }
    });

    testTitle.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
    testTitle.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tên bài kiểm tra");
    testTitle.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

    jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel3.setText("Môn học / chủ đề");

    jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel4.setText("Thời gian làm bài");

    testTime.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
    testTime.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Thời gian (Phút)");
    testTime.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

    time.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
    time.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    time.setIcon(new FlatSVGIcon("icons/time.svg", 30, 30)
    );
    time.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel5.setText("Số lần làm bài");

    testLimit.putClientProperty(FlatClientProperties.STYLE, "arc :10;");

    jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel8.setText("Ngày thi");

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

    jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
    jLabel14.setText("ID: ");

    jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
    jLabel1.setText("NULL");

    jDateChooser1.putClientProperty(FlatClientProperties.STYLE, "arc: 10;");

    jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel15.setText("Mã bài thi");

    testCode.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
    testCode.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Vd(TH01, DE01,...)");
    testCode.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

    jButton16.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
    jButton16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jButton16.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jButton16.setIcon(new FlatSVGIcon("icons/unlock.svg", 30, 30));
    jButton16.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton16ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel14)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(testTitle)))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8))
                    .addGap(27, 27, 27)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(testCode, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGap(18, 18, 18)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addComponent(testTime, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(time, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jLabel4)
                .addComponent(monhocCBB, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel3))
            .addGap(18, 18, 18)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(luu))
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel5)
                        .addComponent(testLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel14)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(testTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(monhocCBB, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addComponent(luu))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addComponent(jLabel4)
                    .addGap(7, 7, 7)
                    .addComponent(testTime, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(time, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(testLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel15)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(testCode, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(0, 0, Short.MAX_VALUE))))
    );

    // Kích hoạt AutoComplete
    AutoCompleteDecorator.decorate(monhocCBB);
    monhocCBB.setMaximumRowCount(5);
    ((AbstractDocument) testTime.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
        throws BadLocationException {
            if (string.matches("\\d+")) { // Chỉ cho phép số
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
        throws BadLocationException {
            if (text.matches("\\d+")) { // Chỉ cho phép số
                super.replace(fb, offset, length, text, attrs);
            }
        }
    });
    // Định dạng lại phần nhập số (loại bỏ dấu phân cách)
    JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) testLimit.getEditor();
    JFormattedTextField textField = editor.getTextField();
    NumberFormatter formatter = (NumberFormatter) textField.getFormatter();
    formatter.setAllowsInvalid(false);  // Không cho phép nhập ký tự không hợp lệ
    formatter.setCommitsOnValidEdit(true); // Tự động cập nhật khi nhập số hợp lệ
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

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
    }// </editor-fold>//GEN-END:initComponents
    private void parentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parentIDActionPerformed
        // TODO add your handling code here:

        String selectedTopic = (String) parentID.getSelectedItem();
        Integer selectedID = topicMapChildren.get(selectedTopic);
        if (selectedID != null) {
            System.out.println("ParentID");
            idTopicChildren = selectedID;
            levelCBB.setSelectedIndex(0);
        }

    }//GEN-LAST:event_parentIDActionPerformed
    private void tim_btn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tim_btn3ActionPerformed
        // TODO add your handling code here:
        if (qIDSelected != -1) {
            showCustomDialog(null, new detailQuestion(qIDSelected), "Chi tiết câu hỏi");
            qIDSelected = -1;
            rowSelected = -1;
        }
    }//GEN-LAST:event_tim_btn3ActionPerformed

    private void monhocCBBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monhocCBBActionPerformed
        // TODO add your handling code here:
        String selectedTopic = (String) monhocCBB.getSelectedItem();
        Integer selectedID = topicMapParent.get(selectedTopic);

        if (selectedID != null) {
            System.out.println("Monhoccbb");
            loadTpChildren(selectedID);
            idTopicParent = selectedID;
            if (idTopicParent != -1) {
                monhocCBB.setEnabled(false);
                jButton16.setIcon(new FlatSVGIcon("icons/lock.svg", 30, 30));
            }
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0); // Xóa dữ liệu cũ
        }


    }//GEN-LAST:event_monhocCBBActionPerformed

    private void levelCBBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_levelCBBActionPerformed
        // TODO add your handling code here:
        String selectedLevel = (String) levelCBB.getSelectedItem();
        qLevel = selectedLevel.toLowerCase();


    }//GEN-LAST:event_levelCBBActionPerformed

    private void tim_btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tim_btn1ActionPerformed
        // TODO add your handling code here:    
        if (jTextField8.getText().trim().isEmpty()) {
            loadTableByTopic(idTopicParent, idTopicChildren);

        } else {
            handleSearch(jTextField8.getText().trim());
        }
    }//GEN-LAST:event_tim_btn1ActionPerformed

    private void tim_btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tim_btn2ActionPerformed
        // TODO add your handling code here:
        int[] selectedRows = jTable2.getSelectedRows();
        for (int row : selectedRows) {
            qIDSelected = (int) jTable2.getValueAt(row, 0);
            addQuestionToTable();
        }
        countQuantityTypeQuestion();


    }//GEN-LAST:event_tim_btn2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        int[] selectedRows = jTable1.getSelectedRows();
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            int row = selectedRows[i];

            // Lấy ID hàng được chọn
            qIDSelected = (int) jTable1.getValueAt(row, 0);
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

            // Xóa hàng khỏi bảng và danh sách
            model.removeRow(row);
            listQSelected.remove(row);

        }
        countQuantityTypeQuestion(); // Cập nhật số lượng câu hỏi


    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        if (idTopicParent == -1) {
            return;
        }

      
        int confirm = JOptionPane.showConfirmDialog(
            null, 
            "Đổi chủ đề sẽ xoá tất cả câu hỏi đã chọn!\nBạn có muốn đổi chủ đề?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            jButton16.setIcon(new FlatSVGIcon("icons/unlock.svg", 30, 30));
            monhocCBB.setEnabled(true);
            monhocCBB.setSelectedIndex(0);
            idTopicParent = -1;
            listQSelected.clear();
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0); // Xóa dữ liệu cũ
            totalQuestion.setText(listQSelected.size() + "");
        }

    }//GEN-LAST:event_jButton16ActionPerformed

    private void luuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_luuActionPerformed
        // TODO add your handling code here:
            if (validDataAddNew()) {
                int confirm = JOptionPane.showConfirmDialog(
                null, 
                "Xác nhận thêm bài thi mới?", 
                "Xác nhận", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.INFORMATION_MESSAGE
            );

        if (confirm == JOptionPane.YES_OPTION) 
            addNew();
        }

    }//GEN-LAST:event_luuActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton5;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JComboBox<String> levelCBB;
    private javax.swing.JButton luu;
    private javax.swing.JComboBox<String> monhocCBB;
    private javax.swing.JComboBox<String> parentID;
    private javax.swing.JSpinner quantityExams;
    private javax.swing.JTextField testCode;
    private javax.swing.JSpinner testLimit;
    private javax.swing.JTextField testTime;
    private javax.swing.JTextField testTitle;
    private javax.swing.JButton tim_btn1;
    private javax.swing.JButton tim_btn2;
    private javax.swing.JButton tim_btn3;
    private javax.swing.JButton time;
    private javax.swing.JLabel totalDiff;
    private javax.swing.JLabel totalEasy;
    private javax.swing.JLabel totalMedium;
    private javax.swing.JLabel totalQuestion;
    // End of variables declaration//GEN-END:variables
}
