/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracnghiem.config.JDBCUtil;
import com.tracnghiem.dto.ResultDTO;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author X
 */
public class ResultDAO implements InterfaceDAO<ResultDTO> {

    public static ResultDAO getInstance() {
        return new ResultDAO();
    }

    @Override
    public boolean insert(ResultDTO result) {
        String sql = "INSERT INTO result(rs_num,userID, exCode, rsAnswer, rsMark, rsDate) VALUES(?,?,?,?,?,?)";
        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, result.getRsNum());
            ps.setInt(2, result.getUserID());
            ps.setString(3, result.getExCode());
            ps.setString(4, result.getRsAnswer());
            ps.setDouble(5, result.getRsMark());
            ps.setTimestamp(6, Timestamp.valueOf(result.getRsDate()));
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(ResultDTO result) {
        String sql = "UPDATE result SET rsAnswer=?, rsMark=?, rsDate=? WHERE rs_num=? AND userID=? AND exCode=?";
        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, result.getRsAnswer());
            ps.setDouble(2, result.getRsMark());
            ps.setTimestamp(3, Timestamp.valueOf(result.getRsDate()));
            ps.setInt(4, result.getRsNum());
            ps.setInt(5, result.getUserID());
            ps.setString(6, result.getExCode());

            
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<ResultDTO> selectAll() {
        ArrayList<ResultDTO> results = new ArrayList<>();
        String sql = "SELECT * FROM result";
        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                results.add(new ResultDTO(
                        rs.getInt("rs_num"),
                        rs.getInt("userID"),
                        rs.getString("exCode"),
                        rs.getString("rs_anwsers"),
                        rs.getDouble("rs_Mark"),
                        rs.getTimestamp("rs_date").toLocalDateTime()
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return results;
    }
    
    @Override
    public ResultDTO selectByID(String id) {
        ResultDTO result = null;
        String sql = "SELECT * FROM result WHERE rs_num=? AND userID=? AND exCode=?";
        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(3, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = new ResultDTO(
                            rs.getInt("rsID"),
                            rs.getInt("userID"),
                            rs.getString("exCode"),
                            rs.getString("rsAnswer"),
                            rs.getDouble("rsMark"),
                            rs.getTimestamp("rsDate").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ResultDTO findOneByPK(int rsNum, int userID, String exCode) {
        ResultDTO result = null;
        String sql = "SELECT * FROM result WHERE rs_num=? AND userID=? AND exCode=?";
        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rsNum);
            ps.setInt(2, userID);
            ps.setString(3, exCode);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = new ResultDTO(
                            rs.getInt("rsID"),
                            rs.getInt("userID"),
                            rs.getString("exCode"),
                            rs.getString("rsAnswer"),
                            rs.getDouble("rsMark"),
                            rs.getTimestamp("rsDate").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM result WHERE rsID=?";
        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean saveResult(int userID, String exCode, ArrayList<Integer> answers, float rsMark) {
        String getCountSQL = "SELECT COUNT(*) FROM result WHERE userID = ?";
        String insertSQL = "INSERT INTO result (rs_num, userID, exCode, rs_anwsers, rs_mark, rs_date) VALUES (?, ?, ?, ?, ?, NOW())";

        String rsNumSQL = "SELECT * FROM result WHERE userID=? AND exCode=?";
        
        int rsNum = 0;
        
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(rsNumSQL)) {

            pstmt.setInt(1, userID);  // userID là kiểu số nguyên
            pstmt.setString(2, exCode); // exCode là kiểu chuỗi

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    rsNum++;
                }
                rsNum++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        
        
        
        
        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement countStmt = conn.prepareStatement(getCountSQL)) {

            countStmt.setInt(1, userID);
            ResultSet rs = countStmt.executeQuery();
//            int rsNum = 1;
//            if (rs.next()) {
//                rsNum = rs.getInt(1) + 1;
//            }

//            // Chuyển danh sách câu trả lời thành JSON
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonAnswers = objectMapper.writeValueAsString(answers);

           
            
            String rsAns = answers.stream()
                         .map(String::valueOf) // Chuyển Integer thành String
                         .collect(Collectors.joining(";"));
            
            
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                insertStmt.setInt(1, rsNum);
                insertStmt.setInt(2, userID);
                insertStmt.setString(3, exCode);
                insertStmt.setString(4, rsAns);
                insertStmt.setFloat(5, rsMark);
                return insertStmt.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<Integer, Integer> getCorrectAnswers(String exQuesIDs) {
        Map<Integer, Integer> correctAnswers = new HashMap<>();

        // Chuyển đổi chuỗi ID câu hỏi thành danh sách
        String[] idsArray = exQuesIDs.split(";");
        ArrayList<Integer> questionIDs = new ArrayList<>();
        for (String id : idsArray) {
            questionIDs.add(Integer.parseInt(id));
        }

        // Tạo chuỗi tham số (?, ?, ?, ?) cho truy vấn SQL
        String placeholders = String.join(",", Collections.nCopies(questionIDs.size(), "?"));
        String sql = "SELECT q.qID, a.awID FROM questions q "
                + "JOIN answers a ON q.qID = a.qID "
                + "WHERE a.isRight = 1 AND q.qID IN (" + placeholders + ")";

        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gán giá trị vào các tham số trong câu lệnh SQL
            for (int i = 0; i < questionIDs.size(); i++) {
                stmt.setInt(i + 1, questionIDs.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                correctAnswers.put(rs.getInt("qID"), rs.getInt("awID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return correctAnswers;
    }

    public ResultDTO getResultByExCode(String exCode) {
        String querySQL = "SELECT rs_num, userID, exCode, rs_anwsers, rs_mark, rs_date FROM result WHERE exCode = ? LIMIT 1";
        ResultDTO result = null; // Không phải danh sách, chỉ lấy một kết quả

        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(querySQL)) {
            stmt.setString(1, exCode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) { // Chỉ lấy kết quả đầu tiên
                int rsID = rs.getInt("rs_num");
                int userID = rs.getInt("userID");
                String rsAnswer = rs.getString("rs_anwsers");
                double rsMark = rs.getDouble("rs_mark");
                LocalDateTime rsDate = rs.getTimestamp("rs_date").toLocalDateTime();

                result = new ResultDTO(rsID, userID, exCode, rsAnswer, rsMark, rsDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result; // Trả về kết quả hoặc null nếu không tìm thấy
    }

}
