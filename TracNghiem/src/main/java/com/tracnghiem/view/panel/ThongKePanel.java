/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tracnghiem.view.panel;

import com.tracnghiem.dto.TestDTO;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.tracnghiem.bus.AnswerBUS;
import com.tracnghiem.bus.ExamBUS;
import com.tracnghiem.bus.QuestionBUS;
import com.tracnghiem.bus.ResultBUS;
import com.tracnghiem.bus.TestBUS;
import com.tracnghiem.bus.TestStructureBUS;
import com.tracnghiem.bus.TopicBUS;
import com.tracnghiem.dto.AnswerDTO;
import com.tracnghiem.dto.ExamDTO;
import com.tracnghiem.dto.QuestionDTO;
import com.tracnghiem.dto.ResultDTO;
import com.tracnghiem.dto.TopicDTO;
import com.tracnghiem.view.components.addTest;
import com.tracnghiem.view.components.choose;
import com.tracnghiem.view.components.detailTest;
import com.tracnghiem.view.mainView;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author X
 */
public class ThongKePanel extends javax.swing.JPanel {

    private static final ExamBUS eBus = new ExamBUS();
    private static final TestBUS tBUS = new TestBUS();
    private static final TestStructureBUS tsBUS = new TestStructureBUS();
    private static final TopicBUS tpBUS = new TopicBUS();
    private static final ResultBUS rBUS = new ResultBUS();
    private static ArrayList<TestDTO> listT = new ArrayList<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Định dạng ngày
    private static LocalDate dateSelected;
    Map<String, int[]> dataStatistic = new HashMap<>();
    private JPanel tkChart;
    private String testCodeSelected = "";

    /**
     * Creates new form DeThiPanel
     */
    public ThongKePanel() {

        dataStatistic.put("", new int[]{0, 0, 100});

        tkChart = createChartPanel(dataStatistic);

        initComponents();

        panelTK.removeAll();  // Xóa biểu đồ cũ nếu có
        panelTK.setLayout(new BorderLayout());
        panelTK.add(tkChart, BorderLayout.CENTER);
        panelTK.revalidate(); // Cập nhật lại layout
        panelTK.repaint();    // Vẽ lại panel

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
                        testCodeSelected = (String) jTable5.getValueAt(row, 0);
                        
                        loadExCode(testCodeSelected);
                    }
                }
            }
        });
        
        listT = tBUS.getAll();
        loadTable(listT);
    }

    private void loadExCode(String testCode) {
        ArrayList<String> exOrders = eBus.getExamCodesByTestCode(testCode);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        
        model.addElement("--");
        for (String t : exOrders) {
            
            model.addElement(t);
        }


        parentID.setModel(model);
    }
    
    private void loadTable(ArrayList<TestDTO> list) {

        DefaultTableModel model = (DefaultTableModel) jTable5.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        if (list.isEmpty()) {
            model.addRow(new Object[]{"", "Không có dữ liệu", ""});
            return;
        }

        for (TestDTO test : list) {

            // Định dạng ngày thành dd/MM/yyyy
            String formattedDate = test.getTestDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            ArrayList<String> l = eBus.getExamCodesByTestCode(test.getTestCode());

            model.addRow(new Object[]{
                test.getTestCode(), // Mã đề thi từ bảng exams
                test.getTestTittle(), // Tiêu đề
                formattedDate, // Thời gian
            });
        }
    }

    private void setColumnWidthsForTable() {
        TableColumnModel columnModel = jTable5.getColumnModel();
        int totalWidth = jTable5.getWidth(); // Lấy chiều rộng tổng của bảng

        double[] columnRatios = {0.2, 0.6, 0.2};

        for (int i = 0; i < columnRatios.length; i++) {
            int columnWidth = (int) (totalWidth * columnRatios[i]);
            columnModel.getColumn(i).setPreferredWidth(columnWidth);
        }
    }

    private JPanel createChartPanel(Map<String, int[]> data) {
         DefaultPieDataset dataset = new DefaultPieDataset();

    int totalBelow5 = 0;
    int totalAboveOrEqual5 = 0;

    // Tính tổng số thí sinh theo từng nhóm
    for (int[] scores : data.values()) {
        totalBelow5 += scores[0];  // Số thí sinh < 5 điểm
        totalAboveOrEqual5 += scores[1]; // Số thí sinh >= 5 điểm
    }

    int totalCandidates = totalBelow5 + totalAboveOrEqual5; // Tổng số thí sinh

    // Kiểm tra tránh lỗi chia cho 0
    if (totalCandidates == 0) {
        dataset.setValue("Không có dữ liệu", 1);
    } else {
        dataset.setValue(String.format("Thí sinh >= 5 điểm", totalAboveOrEqual5), totalAboveOrEqual5);
        dataset.setValue(String.format("Thí sinh < 5 điểm ", totalBelow5), totalBelow5);
    }

    // Tạo biểu đồ tròn
    JFreeChart pieChart = ChartFactory.createPieChart(
            "THỐNG KÊ", // Tiêu đề
            dataset, // Dataset
            true, // Hiển thị chú thích (legend)
            true, // Hiển thị tooltips
            false // Không cần URLs
    );

    // Đặt màu nền thành trắng
    pieChart.setBackgroundPaint(Color.WHITE);

    // Tùy chỉnh màu nền của biểu đồ
    PiePlot plot = (PiePlot) pieChart.getPlot();
    plot.setBackgroundPaint(Color.WHITE);
    plot.setSectionPaint(String.format("Thí sinh >= 5 điểm (%d)", totalAboveOrEqual5), new Color(102, 204, 102)); // Xanh lá
    plot.setSectionPaint(String.format("Thí sinh < 5 điểm (%d)", totalBelow5), new Color(255, 102, 102)); // Đỏ

    // Hiển thị số lượng và phần trăm trên từng phần của biểu đồ
    plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
            "{0}: {1} ({2})", NumberFormat.getIntegerInstance(), NumberFormat.getPercentInstance()
    ));

    // Tạo ChartPanel và đặt kích thước cứng
    ChartPanel chartPanel = new ChartPanel(pieChart);
    chartPanel.setPreferredSize(new Dimension(676, 614)); // Kích thước theo panel cha

    return chartPanel;
    }


    
    private void refresh() {

        listT = tBUS.getAll();
        dateSelected = null;
        jDateChooser1.setDate(null);
        loadTable(listT);
        parentID.removeAllItems();
        testCodeSelected = "";
        
        dataStatistic.clear();
        dataStatistic.put("", new int[]{0, 0, 100});


        tkChart = createChartPanel(dataStatistic);
        panelTK.removeAll();  // Xóa biểu đồ cũ nếu có
        panelTK.setLayout(new BorderLayout());
        panelTK.add(tkChart, BorderLayout.CENTER);
        panelTK.revalidate(); // Cập nhật lại layout
        panelTK.repaint();    // Vẽ lại panel

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel7 = new javax.swing.JPanel();
        tim_btn1 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jButton16 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        parentID = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        panelTK = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();

        jPanel7.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

        tim_btn1.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #0bae1d; foreground: #ffffff;");
        tim_btn1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tim_btn1.setIcon(new FlatSVGIcon("icons/search.svg", 25, 25)
        );
        tim_btn1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tim_btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tim_btn1ActionPerformed(evt);
            }
        });

        jDateChooser1.putClientProperty(FlatClientProperties.STYLE, "arc: 10;");

        jButton16.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #0bae1d; foreground: #ffffff;");
        jButton16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton16.setIcon(new FlatSVGIcon("icons/refresh.svg", 30, 30)
        );
        jButton16.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel23.setText("Thời gian");

        parentID.setEditable(false);
        parentID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parentIDActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel24.setText("Mã đề");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(tim_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(parentID, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 8, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tim_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(parentID, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
        //AutoCompleteDecorator.decorate(parentID);
        //parentID.setMaximumRowCount(5);

        jPanel8.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

        javax.swing.GroupLayout panelTKLayout = new javax.swing.GroupLayout(panelTK);
        panelTK.setLayout(panelTKLayout);
        panelTKLayout.setHorizontalGroup(
            panelTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 676, Short.MAX_VALUE)
        );
        panelTKLayout.setVerticalGroup(
            panelTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 614, Short.MAX_VALUE)
        );

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Test code", "Tiêu đề", "Ngày thi"
            }

        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        SwingUtilities.invokeLater(() -> setColumnWidthsForTable());
        jScrollPane6.setViewportView(jTable5);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 358, Short.MAX_VALUE)
                .addComponent(panelTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(687, Short.MAX_VALUE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed

        refresh();
    }//GEN-LAST:event_jButton16ActionPerformed

    
    
    
    private void parentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parentIDActionPerformed
        
        String exCode = (String) parentID.getSelectedItem();
        
        if (exCode == null ||exCode.equalsIgnoreCase("--")) {
            return;
        }
        ArrayList<ResultDTO> listR = rBUS.findAllByExCode(exCode);
        int dgt5 = 0;
        
        
        for(ResultDTO r : listR) {
            if (r.getRsMark() >= 5.0) {
                dgt5++;
            }
        }
        
        dataStatistic.clear();
        dataStatistic.put("Bài kiểm tra mã đề: "+exCode, new int[]{listR.size() - dgt5, dgt5, listR.size()});


        tkChart = createChartPanel(dataStatistic);
        panelTK.removeAll();  // Xóa biểu đồ cũ nếu có
        panelTK.setLayout(new BorderLayout());
        panelTK.add(tkChart, BorderLayout.CENTER);
        panelTK.revalidate(); // Cập nhật lại layout
        panelTK.repaint();    // Vẽ lại panel

        
        
        
    }//GEN-LAST:event_parentIDActionPerformed

    private void tim_btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tim_btn1ActionPerformed
        // TODO add your handling code here:
        
        
        listT = tBUS.search("", dateSelected);
        loadTable(listT);
    }//GEN-LAST:event_tim_btn1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton16;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTable5;
    private javax.swing.JPanel panelTK;
    private javax.swing.JComboBox<String> parentID;
    private javax.swing.JButton tim_btn1;
    // End of variables declaration//GEN-END:variables
}
