/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.tracnghiem.view;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.tracnghiem.bus.QuestionBUS;
import com.tracnghiem.bus.TopicBUS;
import com.tracnghiem.dto.QuestionDTO;
import com.tracnghiem.dto.TopicDTO;
import com.tracnghiem.view.components.addQuestion;
import com.tracnghiem.view.components.addSubject;
import com.tracnghiem.view.components.addUser;
import com.tracnghiem.view.components.detailQuestion;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.KeyEvent;

/**
 *
 * @author huulu
 */
public class mainView extends javax.swing.JFrame {

    /**
     * Creates new form main
     */
    /*
        boolean action from dialog form 
            -> true if add news
            -> false if update
     */
    private static String panelName;
    private static CardLayout cardLayout;
    private final QuestionBUS qBUS = new QuestionBUS();
    private final TopicBUS tBUS = new TopicBUS();
    private int idTopicParent = -1, idTopicChild = -1, idTopicChild1 = -1, qIDSelected = -1;
    private String qLevel = "--none--";
    private final Map<String, Integer> topicMapParent = new LinkedHashMap<>();
    private final Map<String, Integer> topicMapChildren = new LinkedHashMap<>();
    private final Map<String, Integer> topicMapChildren1 = new LinkedHashMap<>();

    private ArrayList<QuestionDTO> listQ = new ArrayList<>();

    public mainView() {

        initComponents();
        cardLayout = (CardLayout) main_panel.getLayout();

        //Menu
        List<JPanel> menuList = List.of(menu1, menu2, menu3, menu4, menu5, menu6);
        menu_panel.putClientProperty(FlatClientProperties.STYLE, "arc: 40; background: #eaeaea");

        for (JPanel menu : menuList) {
            menu.putClientProperty("selected", false);
            addHoverEffect(menu, menuList);
        }

        //Hien thi mac dinh
        menu1.putClientProperty(FlatClientProperties.STYLE, "arc: 20; background: #033d67");
        menu1.putClientProperty("selected", true);
        cardLayout.show(main_panel, menu1.getName());

        setColumnWidths();

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
        loadTable(listQ);
        loadTpParent(tBUS.getAllParent());

        setLocationRelativeTo(null);
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
    }

    private void loadTable(ArrayList<QuestionDTO> list) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

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
        loadTable(listQ);
    }

    private void setColumnWidths() {
        TableColumnModel columnModel = jTable1.getColumnModel();
        int totalWidth = jTable1.getWidth(); // Lấy chiều rộng tổng của bảng

        double[] columnRatios = {0.05, 0.15, 0.55, 0.1, 0.15, 0.1};

        for (int i = 0; i < columnRatios.length; i++) {
            int columnWidth = (int) (totalWidth * columnRatios[i]);
            columnModel.getColumn(i).setPreferredWidth(columnWidth);
        }
    }

    private void handleSearchQuestion() {
        String key = jtfSearch.getText().toLowerCase().trim();
        if (idTopicChild != -1 || idTopicParent != -1 || !qLevel.equalsIgnoreCase("--none--")) {
            System.out.println("listQ.size(): " + listQ.size() + " ____ " + key);
            ArrayList<QuestionDTO> rs = new ArrayList<>();
            for (QuestionDTO q : listQ) {
                if (q.getQContent().toLowerCase().contains(key.toLowerCase())) {
                    rs.add(q);
                }
            }
            loadTable(rs);
        } else {
            listQ = qBUS.search(key, "Tất cả");
            loadTable(listQ);
        }
    }

    private void refresh() {
        listQ = qBUS.getAll();
        loadTable(listQ);

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

    private void addHoverEffect(JPanel menu, List<JPanel> allMenus) {
        String defaultStyle = "arc: 20; background: #005b91";
        String hoverStyle = "arc: 20; background: #033d67";
        String selectedStyle = "arc: 20; background: #033d67";

        menu.putClientProperty(FlatClientProperties.STYLE, defaultStyle);

        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!menu.getClientProperty("selected").equals(true)) {
                    menu.putClientProperty(FlatClientProperties.STYLE, hoverStyle);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!menu.getClientProperty("selected").equals(true)) {
                    menu.putClientProperty(FlatClientProperties.STYLE, defaultStyle);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    for (JPanel otherMenu : allMenus) {
                        otherMenu.putClientProperty(FlatClientProperties.STYLE, defaultStyle);
                        otherMenu.putClientProperty("selected", false);
                    }
                    // Giữ màu menu được chọn
                    menu.putClientProperty(FlatClientProperties.STYLE, selectedStyle);
                    menu.putClientProperty("selected", true);
                    if (!menu.getName().equals("dangxuat")) {
                        cardLayout.show(main_panel, menu.getName());
                    } else {
                        if ((int) showLogoutDialog() == JOptionPane.YES_OPTION) {
                            //tro ve dang nhap
                            new loginView().setVisible(true);
                            dispose();
                            //
                        }
                    }

                }
            }
        });
    }

    private static int showLogoutDialog() {
        JPanel panel = new JPanel();
        panel.setSize(300, 200);
        panel.add(new JLabel("Bạn có chắc chắn muốn đăng xuất không?"));
        Object[] options = {"Đăng xuất", "Hủy"};
        UIManager.put("OptionPane.yesButtonText", "Xác nhận");
        UIManager.put("OptionPane.noButtonText", "Hủy");
        int result = JOptionPane.showOptionDialog(
                null,
                "Bạn có chắc chắn muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[1]
        );
        return result;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        main = new javax.swing.JPanel();
        left_panel = new javax.swing.JPanel();
        head = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        menu_panel = new javax.swing.JPanel();
        menu1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        menu2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        menu3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        menu4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        menu5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        menu6 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        main_panel = new javax.swing.JPanel();
        cauhoi = new javax.swing.JPanel();
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
        monhoc = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        sinhvien = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel21 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        tim_btn = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        dethi = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel22 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        tim_btn1 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        diem = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel23 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        tim_btn2 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        main.setBackground(new Color(0xeaeaea));
        main.setPreferredSize(new java.awt.Dimension(1200, 750));

        left_panel.setMaximumSize(new java.awt.Dimension(180, 32767));
        left_panel.setPreferredSize(new java.awt.Dimension(180, 750));

        head.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        head.setName("Info");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new FlatSVGIcon("icons/user.svg", 40, 40
        )
    );

    jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
    jLabel2.setText("ADMIN");

    javax.swing.GroupLayout headLayout = new javax.swing.GroupLayout(head);
    head.setLayout(headLayout);
    headLayout.setHorizontalGroup(
        headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(headLayout.createSequentialGroup()
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );
    headLayout.setVerticalGroup(
        headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(headLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    menu_panel.setPreferredSize(new java.awt.Dimension(168, 637));
    menu_panel.setLayout(new javax.swing.BoxLayout(menu_panel, javax.swing.BoxLayout.Y_AXIS));

    menu1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    menu1.setMaximumSize(new java.awt.Dimension(168, 47));
    menu1.setPreferredSize(new java.awt.Dimension(168, 47));
    menu_panel.add(Box.createVerticalStrut(0));
    menu1.setName("cauhoi");

    jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel4.setForeground(new java.awt.Color(255, 255, 255));
    jLabel4.setText("Câu Hỏi");
    jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    jLabel3.setForeground(new java.awt.Color(255, 255, 255));
    jLabel3.setIcon(new FlatSVGIcon("icons/table-of-contents.svg", 30, 30
    )
    );
    jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jLabel3.setPreferredSize(new java.awt.Dimension(35, 35));

    javax.swing.GroupLayout menu1Layout = new javax.swing.GroupLayout(menu1);
    menu1.setLayout(menu1Layout);
    menu1Layout.setHorizontalGroup(
        menu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(menu1Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    menu1Layout.setVerticalGroup(
        menu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(menu1Layout.createSequentialGroup()
            .addGroup(menu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(menu1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(menu1Layout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    menu_panel.add(menu1);

    menu2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    menu2.setMaximumSize(new java.awt.Dimension(168, 47));
    menu2.setPreferredSize(new java.awt.Dimension(168, 47));
    menu_panel.add(Box.createVerticalStrut(10));
    menu2.setName("monhoc");

    jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel5.setForeground(new java.awt.Color(255, 255, 255));
    jLabel5.setText("Môn Học");
    jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    jLabel6.setForeground(new java.awt.Color(255, 255, 255));
    jLabel6.setIcon(new FlatSVGIcon("icons/table-of-contents.svg", 30, 30
    )
    );
    jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jLabel6.setPreferredSize(new java.awt.Dimension(35, 35));

    javax.swing.GroupLayout menu2Layout = new javax.swing.GroupLayout(menu2);
    menu2.setLayout(menu2Layout);
    menu2Layout.setHorizontalGroup(
        menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(menu2Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    menu2Layout.setVerticalGroup(
        menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(menu2Layout.createSequentialGroup()
            .addGroup(menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(menu2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(menu2Layout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    menu_panel.add(menu2);

    menu3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    menu3.setMaximumSize(new java.awt.Dimension(168, 47));
    menu3.setPreferredSize(new java.awt.Dimension(168, 47));
    menu_panel.add(Box.createVerticalStrut(10));
    menu3.setName("sinhvien");

    jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel7.setForeground(new java.awt.Color(255, 255, 255));
    jLabel7.setText("Người Dùng");
    jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    jLabel8.setForeground(new java.awt.Color(255, 255, 255));
    jLabel8.setIcon(new FlatSVGIcon("icons/table-of-contents.svg", 30, 30
    )
    );
    jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jLabel8.setPreferredSize(new java.awt.Dimension(35, 35));

    javax.swing.GroupLayout menu3Layout = new javax.swing.GroupLayout(menu3);
    menu3.setLayout(menu3Layout);
    menu3Layout.setHorizontalGroup(
        menu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(menu3Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
            .addContainerGap())
    );
    menu3Layout.setVerticalGroup(
        menu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(menu3Layout.createSequentialGroup()
            .addGroup(menu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(menu3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(menu3Layout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    menu_panel.add(menu3);

    menu4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    menu4.setMaximumSize(new java.awt.Dimension(168, 47));
    menu4.setPreferredSize(new java.awt.Dimension(168, 47));
    menu_panel.add(Box.createVerticalStrut(10));
    menu4.setName("dethi");

    jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel9.setForeground(new java.awt.Color(255, 255, 255));
    jLabel9.setText("Đề Thi");
    jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    jLabel10.setForeground(new java.awt.Color(255, 255, 255));
    jLabel10.setIcon(new FlatSVGIcon("icons/table-of-contents.svg", 30, 30
    )
    );
    jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jLabel10.setPreferredSize(new java.awt.Dimension(35, 35));

    javax.swing.GroupLayout menu4Layout = new javax.swing.GroupLayout(menu4);
    menu4.setLayout(menu4Layout);
    menu4Layout.setHorizontalGroup(
        menu4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(menu4Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    menu4Layout.setVerticalGroup(
        menu4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(menu4Layout.createSequentialGroup()
            .addGroup(menu4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(menu4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(menu4Layout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    menu_panel.add(menu4);

    menu5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    menu5.setMaximumSize(new java.awt.Dimension(168, 47));
    menu5.setPreferredSize(new java.awt.Dimension(168, 47));
    menu_panel.add(Box.createVerticalStrut(10));
    menu5.setName("diem");

    jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel11.setForeground(new java.awt.Color(255, 255, 255));
    jLabel11.setText("Điểm");
    jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    jLabel12.setForeground(new java.awt.Color(255, 255, 255));
    jLabel12.setIcon(new FlatSVGIcon("icons/table-of-contents.svg", 30, 30
    )
    );
    jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jLabel12.setPreferredSize(new java.awt.Dimension(35, 35));

    javax.swing.GroupLayout menu5Layout = new javax.swing.GroupLayout(menu5);
    menu5.setLayout(menu5Layout);
    menu5Layout.setHorizontalGroup(
        menu5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(menu5Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    menu5Layout.setVerticalGroup(
        menu5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(menu5Layout.createSequentialGroup()
            .addGroup(menu5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(menu5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(menu5Layout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    menu_panel.add(menu5);

    menu6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    menu6.setMaximumSize(new java.awt.Dimension(168, 47));
    menu6.setPreferredSize(new java.awt.Dimension(168, 47));
    menu_panel.add(Box.createVerticalGlue());
    menu_panel.add(Box.createRigidArea(new Dimension(0, 10)));
    menu6.setName("dangxuat");

    jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel14.setForeground(new java.awt.Color(255, 255, 255));
    jLabel14.setText("Đăng Xuất");
    jLabel14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    jLabel15.setForeground(new java.awt.Color(255, 255, 255));
    jLabel15.setIcon(new FlatSVGIcon("icons/logout.svg", 30, 30
    )
    );
    jLabel15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jLabel15.setPreferredSize(new java.awt.Dimension(35, 35));

    javax.swing.GroupLayout menu6Layout = new javax.swing.GroupLayout(menu6);
    menu6.setLayout(menu6Layout);
    menu6Layout.setHorizontalGroup(
        menu6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(menu6Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    menu6Layout.setVerticalGroup(
        menu6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(menu6Layout.createSequentialGroup()
            .addGroup(menu6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(menu6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(menu6Layout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    menu_panel.add(menu6);

    javax.swing.GroupLayout left_panelLayout = new javax.swing.GroupLayout(left_panel);
    left_panel.setLayout(left_panelLayout);
    left_panelLayout.setHorizontalGroup(
        left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, left_panelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(menu_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(head, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING))
            .addContainerGap())
    );
    left_panelLayout.setVerticalGroup(
        left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(left_panelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(head, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(menu_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );

    main_panel.setLayout(new java.awt.CardLayout());

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
                    .addComponent(jtfSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(cbbF, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cbbS, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(cbbT, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(cbbLevel, 0, 109, Short.MAX_VALUE)
                    .addGap(18, 18, 18)
                    .addComponent(refreshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(55, 55, 55)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbF, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbS, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbT, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(refreshBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbbLevel))
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
    if (jTable1.getColumnModel().getColumnCount() > 0) {
        jTable1.getColumnModel().getColumn(0).setHeaderValue("ID");
        jTable1.getColumnModel().getColumn(1).setHeaderValue("Tên sinh viên");
        jTable1.getColumnModel().getColumn(2).setHeaderValue("Email");
        jTable1.getColumnModel().getColumn(3).setHeaderValue("Họ và tên");
    }

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

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton5)
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
                .addComponent(jButton5))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE))
    );

    cauhoi.putClientProperty(FlatClientProperties.STYLE, "arc: 20; background: #eaeaea");

    javax.swing.GroupLayout cauhoiLayout = new javax.swing.GroupLayout(cauhoi);
    cauhoi.setLayout(cauhoiLayout);
    cauhoiLayout.setHorizontalGroup(
        cauhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cauhoiLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(cauhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    cauhoiLayout.setVerticalGroup(
        cauhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(cauhoiLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );

    main_panel.add(cauhoi, "cauhoi");

    monhoc.putClientProperty(FlatClientProperties.STYLE, "arc: 20; background: #eaeaea");
    monhoc.setPreferredSize(new java.awt.Dimension(1014, 744));

    jPanel3.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

    jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
    jLabel18.setText("Tìm kiếm");

    jTextField3.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
    jTextField3.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập môn học . . . . . . . . . . . . . . . . . . .");
    jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

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

    jComboBox3.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Danh sách môn học");

    jComboBox4.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Danh sách chủ đề");

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
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    jTable2.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null}
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
    jScrollPane3.setViewportView(jTable2);
    if (jTable2.getColumnModel().getColumnCount() > 0) {
        jTable2.getColumnModel().getColumn(0).setHeaderValue("ID");
        jTable2.getColumnModel().getColumn(1).setHeaderValue("Tên sinh viên");
        jTable2.getColumnModel().getColumn(2).setHeaderValue("Email");
        jTable2.getColumnModel().getColumn(3).setHeaderValue("Họ và tên");
        jTable2.getColumnModel().getColumn(4).setHeaderValue("Quyền");
    }

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

    javax.swing.GroupLayout monhocLayout = new javax.swing.GroupLayout(monhoc);
    monhoc.setLayout(monhocLayout);
    monhocLayout.setHorizontalGroup(
        monhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, monhocLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(monhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    monhocLayout.setVerticalGroup(
        monhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(monhocLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );

    main_panel.add(monhoc, "monhoc");

    sinhvien.putClientProperty(FlatClientProperties.STYLE, "arc: 20; background: #eaeaea");

    jPanel5.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

    jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
    jLabel21.setText("Tìm kiếm");

    jTextField4.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
    jTextField4.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập . . . . . . . . . . . . . . . . . . .");
    jTextField4.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

    tim_btn.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #0bae1d; foreground: #ffffff;");
    tim_btn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    tim_btn.setIcon(new FlatSVGIcon("icons/search.svg", 25, 25)
    );
    tim_btn.setText("Tìm");
    tim_btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    tim_btn.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            tim_btnActionPerformed(evt);
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
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(tim_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(503, Short.MAX_VALUE))
        .addComponent(jSeparator4)
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, 0)
            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(5, 5, 5)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(tim_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel6.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

    jButton10.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
    jButton10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jButton10.setIcon(new FlatSVGIcon("icons/add.svg", 30, 30)
    );
    jButton10.setText("Tạo mới");
    jButton10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jButton10.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton10ActionPerformed(evt);
        }
    });

    jButton11.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
    jButton11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jButton11.setIcon(new FlatSVGIcon("icons/detail.svg", 30, 30)
    );
    jButton11.setText("Chi tiết");
    jButton11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    jButton12.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ee2020; foreground: #ffffff;");
    jButton12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jButton12.setIcon(new FlatSVGIcon("icons/delete.svg", 30, 30)
    );
    jButton12.setText("Xóa");
    jButton12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jButton12.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton12ActionPerformed(evt);
        }
    });

    jTable3.setModel(new javax.swing.table.DefaultTableModel(
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
    jScrollPane4.setViewportView(jTable3);
    if (jTable3.getColumnModel().getColumnCount() > 0) {
        jTable3.getColumnModel().getColumn(0).setHeaderValue("ID");
        jTable3.getColumnModel().getColumn(1).setHeaderValue("Họ và tên");
        jTable3.getColumnModel().getColumn(2).setHeaderValue("Email");
        jTable3.getColumnModel().getColumn(3).setHeaderValue("Quyền");
    }

    javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
    jPanel6.setLayout(jPanel6Layout);
    jPanel6Layout.setHorizontalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton12)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jButton11)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jButton10)
            .addContainerGap())
        .addComponent(jScrollPane4)
    );
    jPanel6Layout.setVerticalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel6Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton10)
                .addComponent(jButton11)
                .addComponent(jButton12))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout sinhvienLayout = new javax.swing.GroupLayout(sinhvien);
    sinhvien.setLayout(sinhvienLayout);
    sinhvienLayout.setHorizontalGroup(
        sinhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(sinhvienLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(sinhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    sinhvienLayout.setVerticalGroup(
        sinhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(sinhvienLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );

    main_panel.add(sinhvien, "sinhvien");

    dethi.putClientProperty(FlatClientProperties.STYLE, "arc: 20; background: #eaeaea");

    jPanel7.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

    jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
    jLabel22.setText("Tìm kiếm");

    jTextField5.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
    jTextField5.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập . . . . . . . . . . . . . . . . . . .");
    jTextField5.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

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

    javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
    jPanel7.setLayout(jPanel7Layout);
    jPanel7Layout.setHorizontalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel7Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(tim_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(503, Short.MAX_VALUE))
        .addComponent(jSeparator5)
    );
    jPanel7Layout.setVerticalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, 0)
            .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(5, 5, 5)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(tim_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

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
        new Object [][] {
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    jScrollPane5.setViewportView(jTable4);

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
        .addComponent(jScrollPane5)
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
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout dethiLayout = new javax.swing.GroupLayout(dethi);
    dethi.setLayout(dethiLayout);
    dethiLayout.setHorizontalGroup(
        dethiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(dethiLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(dethiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    dethiLayout.setVerticalGroup(
        dethiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(dethiLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );

    main_panel.add(dethi, "dethi");

    diem.putClientProperty(FlatClientProperties.STYLE, "arc: 20; background: #eaeaea");

    jPanel9.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

    jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
    jLabel23.setText("Tìm kiếm");

    jTextField4.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
    jTextField4.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập . . . . . . . . . . . . . . . . . . .");
    jTextField6.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

    tim_btn2.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #0bae1d; foreground: #ffffff;");
    tim_btn2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    tim_btn2.setIcon(new FlatSVGIcon("icons/search.svg", 25, 25)
    );
    tim_btn2.setText("Tìm");
    tim_btn2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    tim_btn2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            tim_btn2ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
    jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel9Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(tim_btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(503, Short.MAX_VALUE))
        .addComponent(jSeparator6)
    );
    jPanel9Layout.setVerticalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, 0)
            .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(5, 5, 5)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(tim_btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel10.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

    jButton16.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
    jButton16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jButton16.setIcon(new FlatSVGIcon("icons/add.svg", 30, 30)
    );
    jButton16.setText("Tạo mới");
    jButton16.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jButton16.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton16ActionPerformed(evt);
        }
    });

    jButton17.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
    jButton17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jButton17.setIcon(new FlatSVGIcon("icons/detail.svg", 30, 30)
    );
    jButton17.setText("Chi tiết");
    jButton17.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    jButton18.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ee2020; foreground: #ffffff;");
    jButton18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jButton18.setIcon(new FlatSVGIcon("icons/delete.svg", 30, 30)
    );
    jButton18.setText("Xóa");
    jButton18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jButton18.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton18ActionPerformed(evt);
        }
    });

    jTable5.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    jScrollPane6.setViewportView(jTable5);

    javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
    jPanel10.setLayout(jPanel10Layout);
    jPanel10Layout.setHorizontalGroup(
        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton18)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jButton17)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jButton16)
            .addContainerGap())
        .addComponent(jScrollPane6)
    );
    jPanel10Layout.setVerticalGroup(
        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel10Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton16)
                .addComponent(jButton17)
                .addComponent(jButton18))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout diemLayout = new javax.swing.GroupLayout(diem);
    diem.setLayout(diemLayout);
    diemLayout.setHorizontalGroup(
        diemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(diemLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(diemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    diemLayout.setVerticalGroup(
        diemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(diemLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );

    main_panel.add(diem, "diem");

    javax.swing.GroupLayout mainLayout = new javax.swing.GroupLayout(main);
    main.setLayout(mainLayout);
    mainLayout.setHorizontalGroup(
        mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(mainLayout.createSequentialGroup()
            .addComponent(left_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, 0)
            .addComponent(main_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );
    mainLayout.setVerticalGroup(
        mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(left_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(mainLayout.createSequentialGroup()
            .addComponent(main_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(main, javax.swing.GroupLayout.DEFAULT_SIZE, 1238, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //Open
        showCustomDialog(this, new addQuestion(null, true), "Thêm câu hỏi");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
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
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        showCustomDialog(this, new addSubject(null, true), "Thêm môn học");
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void tim_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tim_btnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tim_btnActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        showCustomDialog(this, new addUser(null, true), "Thêm người dùng");
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void tim_btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tim_btn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tim_btn1ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void tim_btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tim_btn2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tim_btn2ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton18ActionPerformed

    private void cbbTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbTActionPerformed
        // TODO add your handling code here:
        String selectedTopic = (String) cbbT.getSelectedItem();
        Integer selectedID = topicMapChildren1.get(selectedTopic);
         // Kiểm tra nếu selectedID là null thì không làm gì cả
        if (selectedID == null) {
            return;
        }
        idTopicChild1 = selectedID;
        loadTableByTopic(idTopicParent, idTopicChild, idTopicChild1);
        cbbLevel.setSelectedIndex(0);

    }//GEN-LAST:event_cbbTActionPerformed

    private void cbbFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbFActionPerformed
        // TODO add your handling code here:
        String selectedTopic = (String) cbbF.getSelectedItem();
        Integer selectedID = topicMapParent.get(selectedTopic);
         // Kiểm tra nếu selectedID là null thì không làm gì cả
        if (selectedID == null) {
            return;
        }
        loadTableByTopic(idTopicParent, idTopicChild, idTopicChild1);
        loadTpChild(selectedID);
        idTopicParent = selectedID;
        idTopicChild = -1;
        cbbLevel.setSelectedIndex(0);
    }//GEN-LAST:event_cbbFActionPerformed

    private void cbbSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbSActionPerformed
        // TODO add your handling code here:
        String selectedTopic = (String) cbbS.getSelectedItem();
        Integer selectedID = topicMapChildren.get(selectedTopic);
         // Kiểm tra nếu selectedID là null thì không làm gì cả
        if (selectedID == null) {
            return;
        }
        idTopicChild = selectedID;
        loadTableByTopic(idTopicParent, idTopicChild, idTopicChild1);
        loadTpChild1(selectedID);
        idTopicChild = selectedID;
        idTopicChild1 = -1;
        cbbT.setSelectedIndex(0);
        cbbLevel.setSelectedIndex(0);
    }//GEN-LAST:event_cbbSActionPerformed

    private void cbbLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbLevelActionPerformed
        // TODO add your handling code here:
        String selectedLevel = (String) cbbLevel.getSelectedItem();
        qLevel = selectedLevel.toLowerCase();
        loadTableByTopic(idTopicParent, idTopicChild, idTopicChild1);
    }//GEN-LAST:event_cbbLevelActionPerformed

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        // TODO add your handling code here:
        refresh();
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if (qIDSelected == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhấp đôi để chọn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int qID = qIDSelected;

        showCustomDialog(this, new detailQuestion(qID), "Chi tiết");
        qIDSelected = -1;
    }//GEN-LAST:event_jButton4ActionPerformed

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
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cauhoi;
    private javax.swing.JComboBox<String> cbbF;
    private javax.swing.JComboBox<String> cbbLevel;
    private javax.swing.JComboBox<String> cbbS;
    private javax.swing.JComboBox<String> cbbT;
    private javax.swing.JPanel dethi;
    private javax.swing.JPanel diem;
    private javax.swing.JPanel head;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
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
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jtfSearch;
    private javax.swing.JPanel left_panel;
    private javax.swing.JPanel main;
    private javax.swing.JPanel main_panel;
    private javax.swing.JPanel menu1;
    private javax.swing.JPanel menu2;
    private javax.swing.JPanel menu3;
    private javax.swing.JPanel menu4;
    private javax.swing.JPanel menu5;
    private javax.swing.JPanel menu6;
    private javax.swing.JPanel menu_panel;
    private javax.swing.JPanel monhoc;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JButton searchBtn;
    private javax.swing.JPanel sinhvien;
    private javax.swing.JButton tim_btn;
    private javax.swing.JButton tim_btn1;
    private javax.swing.JButton tim_btn2;
    // End of variables declaration//GEN-END:variables
}
