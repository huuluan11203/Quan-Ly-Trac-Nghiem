/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.dao;

import com.tracnghiem.config.JDBCUtil;
import com.tracnghiem.dto.AnswerDTO;
import com.tracnghiem.config.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author X
 */
public class AnswerDAO implements InterfaceDAO<AnswerDTO>{

    public static AnswerDAO getInstance() {
        return new AnswerDAO();
    }
    @Override
    public boolean insert(AnswerDTO t) {
        boolean rs = false;
        String sql = "INSERT INTO answers(qID, awContent, awPictures, isRight, awStatus) VALUES(?,?,?,?,?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getQID());
            ps.setString(2, t.getAwContent());
            ps.setString(3, t.getAwPicture());
            ps.setBoolean(4, t.isIsRight());
            ps.setInt(5, t.getAwStatus());
            rs = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }
    @Override
    public boolean update(AnswerDTO t) {
        boolean rs = false;
        String sql = "UPDATE answers SET qID=?, awContent=?, awPictures=?, isRight=?, awStatus=? WHERE awID=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getQID());
            ps.setString(2, t.getAwContent());
            ps.setString(3, t.getAwPicture());
            ps.setBoolean(4, t.isIsRight());
            ps.setInt(5, t.getAwStatus());
            ps.setInt(6, t.getAwID());
            rs = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    @Override
    public ArrayList<AnswerDTO> selectAll() {
        ArrayList<AnswerDTO> rs = new ArrayList<>();
        String sql = "SELECT * FROM answers WHERE awStatus=1";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rsSet = ps.executeQuery()) {
            while (rsSet.next()) {
                rs.add(new AnswerDTO(rsSet.getInt("awID"),
                                     rsSet.getInt("qID"),
                                     rsSet.getString("awContent"),
                                     rsSet.getString("awPictures"),
                                     rsSet.getBoolean("isRight"),
                                     rsSet.getInt("awStatus")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    @Override
    public AnswerDTO selectByID(String id) {
        AnswerDTO rs = null;
        String sql = "SELECT * FROM answers WHERE awID=? AND awStatus=1";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rsSet = ps.executeQuery()) {
                if (rsSet.next()) {
                    rs = new AnswerDTO(rsSet.getInt("awID"),
                                       rsSet.getInt("qID"),
                                       rsSet.getString("awContent"),
                                       rsSet.getString("awPictures"),
                                       rsSet.getBoolean("isRight"),
                                       rsSet.getInt("awStatus"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    @Override
    public boolean delete(String id) {
        boolean rs = false;
        String sql = "UPDATE answers SET awStatus=0 WHERE awID = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            rs = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }
    
    public int getMaxID() {
        String sql = "SELECT MAX(awID) FROM answers";
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
