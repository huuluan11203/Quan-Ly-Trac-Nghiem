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
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author huulu
 */
public class mainView extends javax.swing.JFrame {

    /**
     * Creates new form main
     */
    private static String panelName;
    private static CardLayout cardLayout;
    private final QuestionBUS qBUS = new QuestionBUS();
    private final TopicBUS tBUS = new TopicBUS();
    private int idTopicParent = -1, idTopicChild = -1;
    private final Map<String, Integer> topicMapParent = new LinkedHashMap<>();
    private final Map<String, Integer> topicMapChildren = new LinkedHashMap<>();
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

        loadTable(qBUS.getAll());
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
        jComboBox2.setModel(model);

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
        jComboBox1.setModel(model);
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

    private void loadTableByTopic(int idParent, int idChild) {
        ArrayList<QuestionDTO> list = new  ArrayList<>();
        
        if (idParent != -1 && idChild == -1) {
            list = qBUS.getByTopic(idChild);
          
        } else if (idParent == -1 && idChild != -1) {
            list = qBUS.getByTopic(idChild);
        } 
        else if (idChild != -1 && idParent != -1) {
            list = qBUS.getByTopic(idParent);
        } else {
            list = qBUS.getAll();
        }

        loadTable(list);
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
        jSeparator2 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        monhoc = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        sinhvien = new javax.swing.JPanel();
        dethi = new javax.swing.JPanel();
        diem = new javax.swing.JPanel();

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
    jLabel4.setText("Quản Lý Câu Hỏi");
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
    jLabel5.setText("Quản Lý Môn Học");
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
    jLabel7.setText("Quản Lý Sinh Viên");
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
            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
    jLabel9.setText("Quản Lý Đề Thi");
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
    jLabel11.setText("Quản Lý Điểm");
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

    jTextField2.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
    jTextField2.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập câu hỏi. . . . . . . . . . . . . . . . . . .");
    jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

    jButton1.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #0bae1d; foreground: #ffffff;");
    jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jButton1.setIcon(new FlatSVGIcon("icons/search.svg", 25, 25)
    );
    jButton1.setText("Tìm");
    jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });

    jComboBox2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jComboBox2ActionPerformed(evt);
        }
    });
    jComboBox2.setMaximumRowCount(10);

    jComboBox1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jComboBox1ActionPerformed(evt);
        }
    });
    jComboBox1.setMaximumRowCount(10);

    jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
    jLabel16.setText("Môn học");

    jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
    jLabel17.setText("Chủ đề");

    jComboBox5.addItem("--None--");
    jComboBox5.addItem("Easy");
    jComboBox5.addItem("Medium");
    jComboBox5.addItem("Diff");

    jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
    jLabel21.setText("Mức độ");

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(65, 65, 65)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(37, 37, 37)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(107, 107, 107))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        .addComponent(jSeparator2)
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(16, Short.MAX_VALUE))
    );

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
            "Mã câu", "Chủ đề", "Câu hỏi", "Độ khó", "Hình", "Trạng thái"}
    ));
    jScrollPane2.setViewportView(jTable1);

    jButton4.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
    jButton4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jButton4.setIcon(new FlatSVGIcon("icons/detail.svg", 30, 30)
    );
    jButton4.setText("Chi tiết");
    jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

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
            .addContainerGap(744, Short.MAX_VALUE)
            .addComponent(jButton5)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jButton4)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jButton2)
            .addContainerGap())
        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1002, Short.MAX_VALUE)
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
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE))
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

    jPanel1.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

    jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
    jLabel18.setText("Tìm kiếm");

    jTextField2.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
    jTextField2.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập câu hỏi. . . . . . . . . . . . . . . . . . .");
    jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

    jButton1.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #0bae1d; foreground: #ffffff;");
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

    jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
    jLabel19.setText("Môn học");

    jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
    jLabel20.setText("Chủ đề");

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(48, 48, 48)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(38, 38, 38))))
        .addComponent(jSeparator3)
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jTable2.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null}
        },
        new String [] {
            "Mã câu", "Môn học", "Chủ đề", "Câu hỏi", "Độ khó", "Đáp án đúng", "Đáp án A", "Đáp án B", "Đáp án C", "Đáp án D", "Đáp án E"
        }
    ));
    jScrollPane3.setViewportView(jTable2);

    javax.swing.GroupLayout monhocLayout = new javax.swing.GroupLayout(monhoc);
    monhoc.setLayout(monhocLayout);
    monhocLayout.setHorizontalGroup(
        monhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(monhocLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1002, Short.MAX_VALUE)
            .addContainerGap())
    );
    monhocLayout.setVerticalGroup(
        monhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(monhocLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(36, 36, 36)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
            .addGap(15, 15, 15))
    );

    main_panel.add(monhoc, "monhoc");

    sinhvien.putClientProperty(FlatClientProperties.STYLE, "arc: 20; background: #eaeaea");

    javax.swing.GroupLayout sinhvienLayout = new javax.swing.GroupLayout(sinhvien);
    sinhvien.setLayout(sinhvienLayout);
    sinhvienLayout.setHorizontalGroup(
        sinhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 1014, Short.MAX_VALUE)
    );
    sinhvienLayout.setVerticalGroup(
        sinhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 744, Short.MAX_VALUE)
    );

    main_panel.add(sinhvien, "sinhvien");

    dethi.putClientProperty(FlatClientProperties.STYLE, "arc: 20; background: #eaeaea");

    javax.swing.GroupLayout dethiLayout = new javax.swing.GroupLayout(dethi);
    dethi.setLayout(dethiLayout);
    dethiLayout.setHorizontalGroup(
        dethiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 1014, Short.MAX_VALUE)
    );
    dethiLayout.setVerticalGroup(
        dethiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 744, Short.MAX_VALUE)
    );

    main_panel.add(dethi, "dethi");

    diem.putClientProperty(FlatClientProperties.STYLE, "arc: 20; background: #eaeaea");

    javax.swing.GroupLayout diemLayout = new javax.swing.GroupLayout(diem);
    diem.setLayout(diemLayout);
    diemLayout.setHorizontalGroup(
        diemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 1014, Short.MAX_VALUE)
    );
    diemLayout.setVerticalGroup(
        diemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 744, Short.MAX_VALUE)
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
        .addComponent(main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //Open
        showCustomDialog(this, new addQuestion(), "");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
        String selectedTopic = (String) jComboBox2.getSelectedItem();
        Integer selectedID = topicMapParent.get(selectedTopic);
        loadTpChild(selectedID);
        idTopicParent = selectedID;
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        String selectedTopic = (String) jComboBox1.getSelectedItem();
        Integer selectedID = topicMapChildren.get(selectedTopic);
        idTopicChild = selectedID;
        
    }//GEN-LAST:event_jComboBox1ActionPerformed

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
    private javax.swing.JPanel dethi;
    private javax.swing.JPanel diem;
    private javax.swing.JPanel head;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
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
    private javax.swing.JPanel sinhvien;
    // End of variables declaration//GEN-END:variables
}
