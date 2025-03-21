/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.tracnghiem.view;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.tracnghiem.bus.LogBUS;
import com.tracnghiem.bus.QuestionBUS;
import com.tracnghiem.bus.TopicBUS;
import com.tracnghiem.dto.QuestionDTO;
import com.tracnghiem.dto.TestDTO;
import com.tracnghiem.dto.TopicDTO;
import com.tracnghiem.dto.UserDTO;
import com.tracnghiem.view.components.addQuestion;
import com.tracnghiem.view.components.addSubject;
import com.tracnghiem.view.components.addUser;
import com.tracnghiem.view.components.detailQuestion;
import com.tracnghiem.view.components.updateQuestion;
import com.tracnghiem.view.panel.CauHoiPanel;
import com.tracnghiem.view.panel.DeThiPanel;
import com.tracnghiem.view.panel.DiemPanel;
import com.tracnghiem.view.panel.MonHocPanel;
import com.tracnghiem.view.panel.SinhVienPanel;
import com.tracnghiem.view.panel.ThongKePanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
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

    /*
        boolean update
            -> true if update
            -> false if add new
     */
    /**
     * Creates new form main
     */
    /*
        boolean action from dialog form 
            -> true if add news
            -> false if update
     */
    private static CardLayout cardLayout;
    private ArrayList<QuestionDTO> listQ = new ArrayList<>();
    private final CauHoiPanel cauhoi = new CauHoiPanel();
    private final MonHocPanel monhoc = new MonHocPanel();
    private final SinhVienPanel sinhvien = new SinhVienPanel();
    private final DeThiPanel dethi = new DeThiPanel();
    private final DiemPanel diem = new DiemPanel();
    private final ThongKePanel thongke = new ThongKePanel();
    private final UserDTO user;

    public mainView(UserDTO user) {
        this.user = user;
        initComponents();
        String[] tName =  user.getUserFullName().split(" "); 
        name. setText(tName[tName.length - 1]);
        cardLayout = (CardLayout) main_panel.getLayout();

        //Menu
        List<JPanel> menuList = List.of(menu1, menu2, menu3, menu4, menu5,  menu8, menu6);
        menu_panel.putClientProperty(FlatClientProperties.STYLE, "arc: 40; background: #eaeaea");

        for (JPanel menu : menuList) {
            menu.putClientProperty("selected", false);
            addHoverEffect(menu, menuList);
        }

        main_panel.add(cauhoi, "cauhoi");
        main_panel.add(dethi, "dethi");
        main_panel.add(monhoc, "monhoc");
        main_panel.add(diem, "diem");
        main_panel.add(sinhvien, "sinhvien");
        main_panel.add(thongke, "thongke");

        //Hien thi mac dinh
        menu1.putClientProperty(FlatClientProperties.STYLE, "arc: 20; background: #033d67");
        menu1.putClientProperty("selected", true);
        cardLayout.show(main_panel, menu1.getName());

        setLocationRelativeTo(null);

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

    public static JDialog showCustomDialog1(JFrame parent, JPanel panel, String title) {
        JDialog dialog = new JDialog(parent, title, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Thêm panel vào dialog
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);

        // Định kích thước dialog
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        return dialog; // Trả về JDialog để có thể thêm listener
    }

    public static void closeCurrentDialog(JPanel panel) {
        Window window = SwingUtilities.getWindowAncestor(panel); // Lấy JDialog chứa JPanel
        if (window instanceof JDialog) {
            ((JDialog) window).dispose(); // Đóng JDialog
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

    //
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
        name = new javax.swing.JLabel();
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
        menu8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        menu6 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        main_panel = new javax.swing.JPanel();

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

    name.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
    name.setText("ADMIN");

    javax.swing.GroupLayout headLayout = new javax.swing.GroupLayout(head);
    head.setLayout(headLayout);
    headLayout.setHorizontalGroup(
        headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(headLayout.createSequentialGroup()
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );
    headLayout.setVerticalGroup(
        headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(headLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE))
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

    menu8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    menu8.setMaximumSize(new java.awt.Dimension(168, 47));
    menu8.setPreferredSize(new java.awt.Dimension(168, 47));
    menu_panel.add(Box.createVerticalStrut(10));
    menu8.setName("thongke");

    jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
    jLabel13.setForeground(new java.awt.Color(255, 255, 255));
    jLabel13.setText("Thống kê");
    jLabel13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    jLabel18.setForeground(new java.awt.Color(255, 255, 255));
    jLabel18.setIcon(new FlatSVGIcon("icons/table-of-contents.svg", 30, 30
    )
    );
    jLabel18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jLabel18.setPreferredSize(new java.awt.Dimension(35, 35));

    javax.swing.GroupLayout menu8Layout = new javax.swing.GroupLayout(menu8);
    menu8.setLayout(menu8Layout);
    menu8Layout.setHorizontalGroup(
        menu8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(menu8Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    menu8Layout.setVerticalGroup(
        menu8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(menu8Layout.createSequentialGroup()
            .addGroup(menu8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(menu8Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(menu8Layout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    menu_panel.add(menu8);

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

    javax.swing.GroupLayout mainLayout = new javax.swing.GroupLayout(main);
    main.setLayout(mainLayout);
    mainLayout.setHorizontalGroup(
        mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(mainLayout.createSequentialGroup()
            .addComponent(left_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(main_panel, javax.swing.GroupLayout.PREFERRED_SIZE, 1046, javax.swing.GroupLayout.PREFERRED_SIZE)
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



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel head;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JPanel menu8;
    private javax.swing.JPanel menu_panel;
    private javax.swing.JLabel name;
    // End of variables declaration//GEN-END:variables
}
