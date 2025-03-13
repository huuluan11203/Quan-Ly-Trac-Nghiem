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
import com.tracnghiem.bus.TestBUS;
import com.tracnghiem.bus.TestStructureBUS;
import com.tracnghiem.bus.TopicBUS;
import com.tracnghiem.dto.AnswerDTO;
import com.tracnghiem.dto.ExamDTO;
import com.tracnghiem.dto.QuestionDTO;
import com.tracnghiem.dto.TopicDTO;
import com.tracnghiem.view.components.addTest;
import com.tracnghiem.view.components.choose;
import com.tracnghiem.view.components.detailTest;
import com.tracnghiem.view.mainView;
import java.awt.BorderLayout;
import java.awt.Desktop;
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
import javax.swing.JFileChooser;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author X
 */
public class DeThiPanel extends javax.swing.JPanel {

    private static final ExamBUS eBus = new ExamBUS();
    private static final TestBUS tBUS = new TestBUS();
    private static final TestStructureBUS tsBUS = new TestStructureBUS();
    private static final TopicBUS tpBUS = new TopicBUS();
    private static ArrayList<TestDTO> listT = new ArrayList<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Định dạng ngày
    private static LocalDate dateSelected;
    private static int tIDSelected = -1;

    /**
     * Creates new form DeThiPanel
     */
    public DeThiPanel() {
        initComponents();

        listT = tBUS.getAll();

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

        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Kiểm tra click đúp
                    int row = jTable4.getSelectedRow();
                    if (row != -1) {
                        tIDSelected = (int) jTable4.getValueAt(row, 0);
                    }
                }
            }
        });

        jDateChooser1.getDateEditor().addPropertyChangeListener("date", evt -> {
            if (evt.getNewValue() != null) {
                filterTable();
            }
        });
        loadTopicComboBox();
        loadTable(listT);
    }

    // MODULE ĐỀ THI
    private void setColumnWidthsForTableTest() {

        TableColumnModel columnModel = jTable4.getColumnModel();
        int totalWidth = jTable4.getWidth(); // Lấy chiều rộng tổng của bảng

        double[] columnRatios = {0.05, 0.075, 0.25, 0.25, 0.075, 0.1, 0.05, 0.1};

        for (int i = 0; i < columnRatios.length; i++) {
            int columnWidth = (int) (totalWidth * columnRatios[i]);
            columnModel.getColumn(i).setPreferredWidth(columnWidth);
        }
    }

    private void loadTable(ArrayList<TestDTO> list) {

        DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        if (list.isEmpty()) {
            model.addRow(new Object[]{"", "", "Không có dữ liệu", "", "", "", ""});
            return;
        }

        for (TestDTO test : list) {

            // Định dạng ngày thành dd/MM/yyyy
            String formattedDate = test.getTestDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            System.out.println("Ngày đã format: " + formattedDate);

            model.addRow(new Object[]{
                test.getTestID(), // ID
                test.getTestCode(), // Mã đề thi từ bảng exams
                test.getTestTittle(), // Tiêu đề
                "", // Chủ đề
                formattedDate, // Thời gian
                test.getTestTime(), // Giờ làm
                test.getTestLimit(), // Giới hạn thời gian làm bài
                tsBUS.getTotalQuesByTestCode(test.getTestCode())
            });
        }
    }

    private void handleSearch() {
        String key = textFieldSearch.getText();

        if (dateSelected == null) {
            if (key.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nhập từ khoá để tìm kiếm!", "Thông báo", JOptionPane.ERROR_MESSAGE);
                return;
            }
            loadTable(tBUS.search(key));
        } else {
            loadTable(tBUS.search(key, dateSelected));
        }

    }

    private void refresh() {

        listT = tBUS.getAll();
        textFieldSearch.setText("");
        dateSelected = null;
        jDateChooser1.setDate(null);
        tIDSelected = -1;
        loadTopicComboBox();
        loadTable(listT);
    }

    private void exportToWord() {
        ExamBUS eBUS = new ExamBUS();
        AnswerBUS aBUS = new AnswerBUS();
        QuestionBUS qBUS = new QuestionBUS();

        TestDTO test = tBUS.findOne(tIDSelected);
        ArrayList<QuestionDTO> listQ = new ArrayList<>();
        ArrayList<AnswerDTO> listA = new ArrayList<>();

//        
        ArrayList<ExamDTO> listE = eBUS.getAll(test.getTestCode());
//      
        if (listE.get(0) != null) {
            String[] t = listE.get(0).getExQuesIDs().split(";");

            for (String s : t) {
                listQ.add(qBUS.findOne(Integer.parseInt(s)));
            }
        }

        for (QuestionDTO q : listQ) {
            listA.addAll(aBUS.getByQuesID(q.getQID()));
        }

        // Tạo Map để tra cứu câu hỏi nhanh hơn
        Map<Integer, QuestionDTO> questionMap = listQ.stream()
                .collect(Collectors.toMap(QuestionDTO::getQID, q -> q));

        // Tạo Map để tra cứu danh sách đáp án theo từng câu hỏi
        Map<Integer, List<AnswerDTO>> answerMap = listA.stream()
                .collect(Collectors.groupingBy(AnswerDTO::getQID));

        // Duyệt qua từng ExamDTO và lấy danh sách câu hỏi tương ứng
        // Duyệt qua từng ExamDTO và lấy danh sách câu hỏi tương ứng
//        for (ExamDTO exam : listE) {
//            List<QuestionDTO> examQuestions = Arrays.stream(exam.getExQuesIDs().split(";"))
//                    .map(Integer::parseInt)
//                    .map(questionMap::get)
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.toList());
//
////            System.out.println("Exam: " + exam.getTestCode());
//            for (QuestionDTO q : examQuestions) {
////                System.out.println(" - Câu hỏi: " + q.getQContent());
//
//                // Lấy danh sách đáp án của câu hỏi hiện tại
//                List<AnswerDTO> questionAnswers = answerMap.getOrDefault(q.getQID(), Collections.emptyList());
//                for (AnswerDTO a : questionAnswers) {
//                    System.out.println("   + " + a.getAwContent() + (a.isIsRight() ? " ✅" : ""));
//                }
//            }
//        }
//
        String schoolInfo = "Trường Đại học Sài Gòn";
        String examTitle = test.getTestTittle();
        String examDate = "\nNăm học:" + test.getTestDate().getYear();
        String examTime = "\nThời gian: " + test.getTestTime() + " phút";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file");

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        fileChooser.setSelectedFile(new File("DeThi_" + test.getTestID() + "_" + test.getTestTittle() + "_" + timestamp + ".docx"));
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File filePath = fileChooser.getSelectedFile();

            try (XWPFDocument document = new XWPFDocument()) {

                for (ExamDTO exam : listE) {
                  

                    // 📝 Thêm tiêu đề Exam
                      // 🏫 Thêm thông tin trường vào file Word
                    XWPFParagraph schoolPara = document.createParagraph();
                    schoolPara.setPageBreak(true);
                    schoolPara.setAlignment(ParagraphAlignment.LEFT);
                    XWPFRun schoolRun = schoolPara.createRun();
                    schoolRun.setBold(true);
                    schoolRun.setFontSize(14);
                    schoolRun.setText(schoolInfo);

                    // 📄 Thêm thông tin đề thi
                    XWPFParagraph examTitlePara = document.createParagraph();
                    examTitlePara.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun examTitleRun = examTitlePara.createRun();
                    examTitleRun.setFontSize(12);
                    examTitleRun.setText(examTitle);

                    XWPFParagraph examDatePara = document.createParagraph();
                    examDatePara.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun examRun = examDatePara.createRun();
                    examRun.setFontSize(12);
                    examRun.setText(examDate);

                    XWPFParagraph examTimePara = document.createParagraph();
                    examTimePara.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun examTimeRun = examTimePara.createRun();
                    examTimeRun.setFontSize(12);
                    examTimeRun.setText(examTime);

                    // Tạo khoảng trống
                    document.createParagraph();
                    
                    XWPFParagraph examCode = document.createParagraph();
                    XWPFRun runExamCode = examCode.createRun();
                    runExamCode.setBold(true);
                    runExamCode.setFontSize(14);
                    runExamCode.setText("\nExam: " + exam.getExCode());

                    // Lấy danh sách câu hỏi của đề
                    List<QuestionDTO> examQuestions = Arrays.stream(exam.getExQuesIDs().split(";"))
                            .map(Integer::parseInt)
                            .map(questionMap::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    int questionNumber = 1;
                    for (QuestionDTO q : examQuestions) {
                        // ➤ Thêm câu hỏi
                        XWPFParagraph questionPara = document.createParagraph();
                        XWPFRun questionRun = questionPara.createRun();
                        questionRun.setBold(true);
                        questionRun.setFontSize(12);
                        questionRun.setText(questionNumber + ". " + q.getQContent());

                        // ➤ Lấy danh sách đáp án và đánh số theo A, B, C, D, E
                        List<AnswerDTO> answers = answerMap.getOrDefault(q.getQID(), Collections.emptyList());
                        String[] labels = {"A", "B", "C", "D", "E"};
                        int index = 0;
                        for (AnswerDTO a : answers) {
                            XWPFParagraph answerPara = document.createParagraph();
                            XWPFRun answerRun = answerPara.createRun();
                            answerRun.setText(labels[index] + ". " + a.getAwContent());
                            index++;
                        }
                        questionNumber++;
                    }

                    // 🔹 Ngăn cách giữa các đề thi
                    document.createParagraph();
                }

                // 🖨 Xuất file Word
                try (FileOutputStream out = new FileOutputStream(filePath)) {
                    document.write(out);
                    System.out.println("File Word đã được tạo: " + filePath);
                    // ✅ Hiện hộp thoại xác nhận mở file
                    int option = JOptionPane.showConfirmDialog(
                            null,
                            "File Word đã được tạo thành công!\nBạn có muốn mở file không?",
                            "Xác nhận mở file",
                            JOptionPane.YES_NO_OPTION
                    );

                    // 🔹 Nếu người dùng chọn "Yes", mở file
                    if (option == JOptionPane.YES_OPTION) {
                        Desktop.getDesktop().open(filePath);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

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

        jPanel7 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        textFieldSearch = new javax.swing.JTextField();
        tim_btn1 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jButton16 = new javax.swing.JButton();
        nhap_excel = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        parentID = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();

        jPanel7.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #ffffff");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel22.setText("Tìm kiếm");

        textFieldSearch.putClientProperty(FlatClientProperties.STYLE, "arc: 10; ");
        textFieldSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập . . . . . . . . . . . . . . . . . . .");
        textFieldSearch.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

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

        nhap_excel.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3276c3; foreground: #ffffff;");
        nhap_excel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nhap_excel.setIcon(new FlatSVGIcon("icons/word.svg", 30, 30)
        );
        nhap_excel.setText("Xuất File");
        nhap_excel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        nhap_excel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nhap_excelActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel23.setText("Thời gian");

        parentID.setEditable(true);
        parentID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parentIDActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel24.setText("Môn học/Chủ đề");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(textFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tim_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(parentID, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nhap_excel)
                        .addContainerGap())
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tim_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nhap_excel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)))
                    .addComponent(parentID, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
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
        AutoCompleteDecorator.decorate(parentID);
        parentID.setMaximumRowCount(5);

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
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

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
            new Object [][] {},
            new String [] {
                "ID", "Mã đề thi", "Tiêu đề", "Chủ đề", "Ngày thi", "Giờ làm( phút)","Lượt", "Tổng số câu"
            }

        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false,false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.setCellSelectionEnabled(true);
        jScrollPane5.setViewportView(jTable4);
        SwingUtilities.invokeLater(() -> setColumnWidthsForTableTest());
        jTable4.setRowSelectionAllowed(true);
        jTable4.setColumnSelectionAllowed(false);
        jTable4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1040, Short.MAX_VALUE)
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
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE))
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

    private void tim_btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tim_btn1ActionPerformed
        // TODO add your handling code here:
        handleSearch();
    }//GEN-LAST:event_tim_btn1ActionPerformed
    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        choose ch = new choose();
        JDialog dialog = mainView.showCustomDialog1(null, ch, "Chọn cấu trúc");

        dialog.setVisible(true);

    }//GEN-LAST:event_jButton13ActionPerformed
    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed

        refresh();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void nhap_excelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nhap_excelActionPerformed
        // TODO add your handling code here:
        if (tIDSelected == -1) {
            JOptionPane.showMessageDialog(null, "Hãy chọn đề thi để xuất ra file word!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        exportToWord();
    }//GEN-LAST:event_nhap_excelActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        //update
        int selectedRow = jTable4.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(jTable4, "Vui lòng chọn bài kiểm tra!");
            return;
        }

        detailTest at = new detailTest((String) jTable4.getValueAt(selectedRow, 1), true);
        showCustomDialog(null, at, "Xem chi tiết đề thi");
    }//GEN-LAST:event_jButton14ActionPerformed


    private void parentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parentIDActionPerformed

        if (parentID.getSelectedItem() != null) {
            filterTable();
        }

    }//GEN-LAST:event_parentIDActionPerformed

    private void filterTable() {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        Date selectedDate = jDateChooser1.getDate(); // Lấy ngày từ jDateChooser1
//        String selectedTopic = (String) parentID.getSelectedItem(); // Lấy chủ đề từ ComboBox
//
//        DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
//        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
//        jTable4.setRowSorter(sorter);
//
//        List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();
//
//        // Lọc theo ngày
//        if (selectedDate != null) {
//            String selectedDateStr = sdf.format(selectedDate);
//            filters.add(RowFilter.regexFilter(selectedDateStr, 4)); // Cột ngày giả sử là 4
//        }
//
//        // Lọc theo chủ đề
//        if (!selectedTopic.equals("--Trống--")) {
//            filters.add(RowFilter.regexFilter("^" + selectedTopic + "$", 3)); // Cột chủ đề giả sử là 3
//        }
//
//        // Áp dụng bộ lọc
//        if (filters.isEmpty()) {
//            sorter.setRowFilter(null); // Không lọc nếu không chọn gì
//        } else {
//            sorter.setRowFilter(RowFilter.andFilter(filters)); // Kết hợp các bộ lọc
//        }
    }

    private void loadTopicComboBox() {
        parentID.removeAllItems(); // Xóa dữ liệu cũ
        parentID.addItem("--Trống--"); // Thêm lựa chọn mặc định
        parentID.setSelectedIndex(0);

        Map<String, Integer> topicMap = new LinkedHashMap<>();

        ArrayList<TopicDTO> topics = tpBUS.getAll();
        if (topics == null || topics.isEmpty()) {
            return;
        }

        Set<Integer> parentIds = new HashSet<>(); // Lưu ID của các topic cha để lọc topic con cấp 1

        // Duyệt qua danh sách topics để lấy topic cha (môn học)
        for (TopicDTO topic : topics) {
            if (topic.getTpStatus() != 0 && topic.getTpParent() == 0) { // Chỉ lấy môn học
                topicMap.put(topic.getTpTitle(), topic.getTpID());
                parentIds.add(topic.getTpID()); // Lưu ID để lọc topic con cấp 1
            }
        }

        // Duyệt qua danh sách topics để lấy topic con cấp 1 (có parent là topic cha)
        for (TopicDTO topic : topics) {
            if (topic.getTpStatus() != 0 && parentIds.contains(topic.getTpParent())) {
                topicMap.put(topic.getTpTitle(), topic.getTpID());
            }
        }

        // Đưa dữ liệu vào ComboBox
        for (String title : topicMap.keySet()) {
            parentID.addItem(title);
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable4;
    private javax.swing.JButton nhap_excel;
    private javax.swing.JComboBox<String> parentID;
    private javax.swing.JTextField textFieldSearch;
    private javax.swing.JButton tim_btn1;
    // End of variables declaration//GEN-END:variables
}
