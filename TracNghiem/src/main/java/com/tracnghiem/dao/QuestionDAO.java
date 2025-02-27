/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.dao;

import com.tracnghiem.config.JDBCUtil;
import com.tracnghiem.dto.QuestionDTO;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author X
 */
public class QuestionDAO implements InterfaceDAO<QuestionDTO> {
    public static QuestionDAO getInstance() {
        return new QuestionDAO();
    }
    
     @Override
    public boolean insert(QuestionDTO question) {
        String sql = "INSERT INTO questions(qContent, qPictures, qTopicID, qLevel, qStatus) VALUES(?,?,?,?,?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, question.getQContent());
            ps.setString(2, question.getQPictures());
            ps.setInt(3, question.getQTopic());
            ps.setString(4, question.getQLevel());
            ps.setInt(5, question.getQStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(QuestionDTO question) {
        String sql = "UPDATE questions SET qContent=?, qPictures=?, qTopicID=?, qLevel=?, qStatus=? WHERE qID=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, question.getQContent());
            ps.setString(2, question.getQPictures());
            ps.setInt(3, question.getQTopic());
            ps.setString(4, question.getQLevel());
            ps.setInt(5, question.getQStatus());
            ps.setInt(6, question.getQID());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<QuestionDTO> selectAll() {
        ArrayList<QuestionDTO> questions = new ArrayList<>();
        String sql = "SELECT * FROM questions";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                questions.add(new QuestionDTO(
                    rs.getInt("qID"),
                    rs.getString("qContent"),
                    rs.getString("qPictures"),
                    rs.getInt("qTopicID"),
                    rs.getString("qLevel"),
                    rs.getInt("qStatus")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return questions;
    }

    @Override
    public QuestionDTO selectByID(String id) {
        QuestionDTO question = null;
        String sql = "SELECT * FROM questions WHERE qID=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    question = new QuestionDTO(
                        rs.getInt("qID"),
                        rs.getString("qContent"),
                        rs.getString("qPictures"),
                        rs.getInt("qTopicID"),
                        rs.getString("qLevel"),
                        rs.getInt("qStatus")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return question;
    }

    @Override
    public boolean delete(String id) {
        String sql = "UPDATE questions SET qStatus=0 WHERE qID=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public int getMaxID() {
        String sql = "SELECT MAX(qID) FROM questions";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {  // Dùng executeQuery() thay vì executeUpdate()

            if (rs.next()) {
                return rs.getInt(1); // Lấy giá trị MAX(qID)
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1; // Trả về -1 nếu có lỗi hoặc không có dữ liệu
    }

}
