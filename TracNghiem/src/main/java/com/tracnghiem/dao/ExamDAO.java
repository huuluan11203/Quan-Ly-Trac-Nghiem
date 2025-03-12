/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.dao;

import com.tracnghiem.config.JDBCUtil;
import com.tracnghiem.dto.ExamDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author X
 */
public class ExamDAO implements InterfaceDAO<ExamDTO> {

    public static ExamDAO getInstance() {
        return new ExamDAO();
    }

    @Override
    public boolean insert(ExamDTO exam) {
        boolean rs = false;
        String sql = "INSERT INTO exams(testCode, exOrder, exCode, ex_quesIDs) VALUES(?,?,?,?)";
        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, exam.getTestCode());
            ps.setString(2, exam.getExOrder());
            ps.setString(3, exam.getExCode());
            ps.setString(4, exam.getExQuesIDs());
            rs = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    @Override
    public boolean update(ExamDTO exam) {
        boolean rs = false;
        String sql = "UPDATE exams SET testCode=?, exOrder=?, ex_quesIDs=? WHERE exCode=?";
        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, exam.getTestCode());
            ps.setString(2, exam.getExOrder());
            ps.setString(3, exam.getExQuesIDs());
            ps.setString(4, exam.getExCode());
            rs = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    @Override
    public ArrayList<ExamDTO> selectAll() {
        ArrayList<ExamDTO> rs = new ArrayList<>();
        String sql = "SELECT * FROM exams";
        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rsSet = ps.executeQuery()) {
            while (rsSet.next()) {
                rs.add(new ExamDTO(
                        rsSet.getString("testCode"),
                        rsSet.getString("exOrder"),
                        rsSet.getString("exCode"),
                        rsSet.getString("ex_quesIDs")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    @Override
    public ExamDTO selectByID(String exCode) {
        ExamDTO rs = null;
        String sql = "SELECT * FROM exams WHERE exCode=?";
        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, exCode);
            try (ResultSet rsSet = ps.executeQuery()) {
                if (rsSet.next()) {
                    rs = new ExamDTO(
                            rsSet.getString("testCode"),
                            rsSet.getString("exOrder"),
                            rsSet.getString("exCode"),
                            rsSet.getString("ex_quesIDs")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    @Override
    public boolean delete(String exCode) {
        boolean rs = false;
        String sql = "DELETE FROM exams WHERE exCode = ?";
        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, exCode);
            rs = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    public ArrayList<ExamDTO> selectAll(String testCode) {
        ArrayList<ExamDTO> rs = new ArrayList<>();
        String sql = "SELECT * FROM exams WHERE testCode=?";
        try (Connection conn = JDBCUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, testCode);
            ResultSet rsSet = ps.executeQuery();

            while (rsSet.next()) {
                rs.add(new ExamDTO(
                        rsSet.getString("testCode"),
                        rsSet.getString("exOrder"),
                        rsSet.getString("exCode"),
                        rsSet.getString("ex_quesIDs")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    public ArrayList<String> getExamCodesByTestCode(String testCode) {
        ArrayList<String> examCodes = new ArrayList<>();
        String sql = "SELECT exCode FROM exams WHERE testCode = ?";
        try (Connection conn = JDBCUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, testCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                examCodes.add(rs.getString("exCode"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return examCodes;
    }

    public ExamDTO getExamByExCode(String exCode) {
        ExamDTO exam = null;
        String sql = "SELECT * FROM exams WHERE exCode = ?"; // Truy vấn SQL

        try (Connection conn = JDBCUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, exCode); // Set giá trị tham số

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Tạo đối tượng ExamDTO từ dữ liệu trong ResultSet
                    exam = new ExamDTO();
                    exam.setTestCode(rs.getString("testCode"));
                    exam.setExCode(rs.getString("exCode"));
                    exam.setExOrder(rs.getString("exOrder"));
                    exam.setExQuesIDs(rs.getString("ex_quesIDs"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exam;
    }

    public ArrayList<ExamDTO> getUserExam(int userId) {
        ArrayList<ExamDTO> examList = new ArrayList<>();
        String sql = "SELECT e.* FROM exams e "
                + "WHERE e.testCode NOT IN ( "
                + "    SELECT DISTINCT e2.testCode "
                + "    FROM exams e2 "
                + "    JOIN result r ON e2.exCode = r.exCode "
                + "    WHERE r.userID = ? "
                + ")";

        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ExamDTO exam = new ExamDTO();
                exam.setExCode(rs.getString("exCode"));
                exam.setTestCode(rs.getString("testCode"));
                exam.setExQuesIDs(rs.getString("ex_quesIDs"));
                exam.setExOrder(rs.getString("exOrder"));
                examList.add(exam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return examList;
    }

}
