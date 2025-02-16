/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.tracnghiem.view;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
  
        
        
        setLocationRelativeTo(null);
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
                    cardLayout.show(main_panel, menu.getName());
                    
                }
            }
        });
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
        monhoc = new javax.swing.JPanel();
        sinhvien = new javax.swing.JPanel();
        dethi = new javax.swing.JPanel();
        diem = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        main.setPreferredSize(new java.awt.Dimension(1200, 750));

        left_panel.setMaximumSize(new java.awt.Dimension(180, 32767));
        left_panel.setPreferredSize(new java.awt.Dimension(180, 750));

        head.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        head.setName("Info");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new FlatSVGIcon("icons/user.svg", 35, 35
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

    cauhoi.putClientProperty(FlatClientProperties.STYLE, "arc: 20; background: #eaeaea");

    javax.swing.GroupLayout cauhoiLayout = new javax.swing.GroupLayout(cauhoi);
    cauhoi.setLayout(cauhoiLayout);
    cauhoiLayout.setHorizontalGroup(
        cauhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 1014, Short.MAX_VALUE)
    );
    cauhoiLayout.setVerticalGroup(
        cauhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 744, Short.MAX_VALUE)
    );

    main_panel.add(cauhoi, "cauhoi");

    monhoc.putClientProperty(FlatClientProperties.STYLE, "arc: 20; background: #eaeaea");

    javax.swing.GroupLayout monhocLayout = new javax.swing.GroupLayout(monhoc);
    monhoc.setLayout(monhocLayout);
    monhocLayout.setHorizontalGroup(
        monhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 1014, Short.MAX_VALUE)
    );
    monhocLayout.setVerticalGroup(
        monhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 744, Short.MAX_VALUE)
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
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
