/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tracnghiem.view.panel;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.tracnghiem.bus.ExamBUS;
import com.tracnghiem.bus.ResultBUS;
import com.tracnghiem.bus.TestBUS;
import com.tracnghiem.bus.TestStructureBUS;
import com.tracnghiem.bus.TopicBUS;
import com.tracnghiem.bus.UserBUS;
import com.tracnghiem.dto.ExamDTO;
import com.tracnghiem.dto.ResultDTO;
import com.tracnghiem.dto.TestDTO;
import com.tracnghiem.dto.UserDTO;
import com.tracnghiem.view.components.detailExam;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author X
 */
public class DiemPanel extends javax.swing.JPanel {

    private ArrayList<TestDTO> listT = new ArrayList<>();
    private ArrayList<ExamDTO> listE = new ArrayList<>();
    private ArrayList<UserDTO> listU = new ArrayList<>();
    private ArrayList<ResultDTO> listR = new ArrayList<>();

    private TopicBUS tpBUS = new TopicBUS();
    private TestBUS tBUS = new TestBUS();
    private TestStructureBUS tsBUS = new TestStructureBUS();
    private ExamBUS eBUS = new ExamBUS();
    private UserBUS uBUS = new UserBUS();
    private ResultBUS rBUS = new ResultBUS();

//    private int rowSelected = -1;
    private LocalDate dateSelected;
    private String exCodeSelected = "";

    /**
     * Creates new form DiemPanel
     */
    public DiemPanel() {
        initComponents();

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
        jTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Kiểm tra click đúp
                    int row = jTable5.getSelectedRow();
                    if (row != -1) {
                        exCodeSelected = (String) jTable5.getValueAt(row, 0);
                    }
                }
            }
        });

        listT = tBUS.getAll();
        listR = rBUS.getAll();

        loadTable(listT);
    }

    private void setColumnWidthsForTable() {
        TableColumnModel columnModel = jTable5.getColumnModel();
        int totalWidth = jTable5.getWidth(); // Lấy chiều rộng tổng của bảng

        System.out.println("check: " + totalWidth);

        double[] columnRatios = {0.1, 0.35, 0.1, 0.15, 0.1, 0.1, 0.05, 0.05};

        for (int i = 0; i < columnRatios.length; i++) {
            int columnWidth = (int) (totalWidth * columnRatios[i]);
            columnModel.getColumn(i).setPreferredWidth(columnWidth);
        }
    }

    private void refresh() {
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) jTable5.getRowSorter();
        if (sorter != null) {
            sorter.setRowFilter(null);
            jTable5.setRowSorter(null); // Đặt lại TableRowSorter
        }

        listT = tBUS.getAll();
        jTextField6.setText("");
        dateSelected = null;
        jDateChooser1.setDate(null);
        exCodeSelected = "";
        loadTable(listT);
    }

    private void loadTable(ArrayList<TestDTO> list) {
        DefaultTableModel model = (DefaultTableModel) jTable5.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        if (list.isEmpty()) {
            model.addRow(new Object[]{"", "", "Không có dữ liệu", "", ""});
            return;
        }

        for (TestDTO test : list) {

            ArrayList<String> l = eBUS.getExamCodesByTestCode(test.getTestCode());
            // Định dạng ngày thành dd/MM/yyyy
            String formattedDate = test.getTestDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            for (String s : l) {
                model.addRow(new Object[]{
                    s,
                    test.getTestTittle(), // Tiêu đề
                    rBUS.getQuantityByExCode(s), // SL tham gia
                    formattedDate, // Thời gian
                    tsBUS.getTotalQuesByTestCode(test.getTestCode()), // tổng số câu hỏi
                    rBUS.getMaxMarkByExCode(s), // Điểm cao nhất
                    getPercentByExCode(s)[0] + "%",
                    getPercentByExCode(s)[1] + "%"
                });
            }
        }
    }

    private double[] getPercentByExCode(String exCode) {
        double[] rs = {0.0, 0.0};

        int d = 0;
        int dL = 0;

        for (ResultDTO r : listR) {
            if (r.getExCode().equals(exCode)) {
                d++;
                if (r.getRsMark() > 5.0) {
                    dL++;
                }
            }
        }

        if (d != 0) {
            rs[0] = ((1.0) * dL / d) * 100;
            rs[1] = 100 - rs[0];
        }
        return rs;
    }

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

    private void search(String keyword) {
        DefaultTableModel model = (DefaultTableModel) jTable5.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        jTable5.setRowSorter(sorter);

        // Chuyển keyword thành regex an toàn (tránh lỗi khi có ký tự đặc biệt)
        String safeKeyword = Pattern.quote(keyword.trim().toLowerCase());

        // Áp dụng bộ lọc không phân biệt hoa thường
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + safeKeyword));
    }

    private void search(String keyword, LocalDate date) {
        DefaultTableModel model = (DefaultTableModel) jTable5.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        jTable5.setRowSorter(sorter);

        // Chuyển LocalDate thành chuỗi định dạng dd-MM-yyyy
        String dateString = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Chuyển keyword thành regex an toàn
        String safeKeyword = Pattern.quote(keyword.trim().toLowerCase());
        String safeDate = Pattern.quote(dateString);

        // Áp dụng bộ lọc: keyword + ngày (không phân biệt hoa thường)
        List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();
        filters.add(RowFilter.regexFilter("(?i)" + safeKeyword));
        filters.add(RowFilter.regexFilter("(?i)" + safeDate));

        sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        tim_btn2 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jButton19 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jButton17 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();

        jPanel9.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel23.setText("Tìm kiếm");

        jTextField6.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
        jTextField6.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập . . . . . . . . . . . . . . . . . . .");
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

        jDateChooser1.putClientProperty(FlatClientProperties.STYLE, "arc: 10;");

        jButton19.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #0bae1d; foreground: #ffffff;");
        jButton19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton19.setIcon(new FlatSVGIcon("icons/refresh.svg", 25,25));
        jButton19.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
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
                        .addComponent(tim_btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(273, 273, 273)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tim_btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

        jPanel10.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

        jButton17.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
        jButton17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton17.setIcon(new FlatSVGIcon("icons/detail.svg", 30, 30)
        );
        jButton17.setText("Chi tiết");
        jButton17.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Mã đề", "Tiêu đề", "SL tham gia", "Ngày thi","Tổng câu", "Điểm cao nhất","Đạt", "Trượt"
            }

        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        SwingUtilities.invokeLater(() -> setColumnWidthsForTable());
        jScrollPane6.setViewportView(jTable5);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1044, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton17))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    
    
    private void tim_btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tim_btn2ActionPerformed
        // TODO add your handling code here:
        String key = jTextField6.getText();

        if (dateSelected == null) {
            if (key.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nhập từ khoá để tìm kiếm!", "Thông báo", JOptionPane.ERROR_MESSAGE);
                return;
            }
            search(key);
        } else {
            search(key, dateSelected);
        }
    }//GEN-LAST:event_tim_btn2ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        refresh();
    }//GEN-LAST:event_jButton19ActionPerformed

   
    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        if (exCodeSelected.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn 1 Exam!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        showCustomDialog(null, new detailExam(exCodeSelected, false), "Thông tin chi tiết");

    }//GEN-LAST:event_jButton17ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton19;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTable5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JButton tim_btn2;
    // End of variables declaration//GEN-END:variables
}
