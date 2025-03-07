/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.dao;

import com.tracnghiem.config.JDBCUtil;
import com.tracnghiem.dto.ResultDTO;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author X
 */
public class ResultDAO implements InterfaceDAO<ResultDTO>{
    public static QuestionDAO getInstance() {
        return new QuestionDAO();
    }
     @Override
    public boolean insert(ResultDTO result) {
        String sql = "INSERT INTO results(userID, exCode, rsAnswer, rsMark, rsDate) VALUES(?,?,?,?,?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, result.getUserID());
            ps.setString(2, result.getExCode());
            ps.setString(3, result.getRsAnswer());
            ps.setDouble(4, result.getRsMark());
            ps.setTimestamp(5, Timestamp.valueOf(result.getRsDate()));
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(ResultDTO result) {
        String sql = "UPDATE results SET userID=?, exCode=?, rsAnswer=?, rsMark=?, rsDate=? WHERE rsID=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, result.getUserID());
            ps.setString(2, result.getExCode());
            ps.setString(3, result.getRsAnswer());
            ps.setDouble(4, result.getRsMark());
            ps.setTimestamp(5, Timestamp.valueOf(result.getRsDate()));
            ps.setInt(6, result.getRsID());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<ResultDTO> selectAll() {
        ArrayList<ResultDTO> results = new ArrayList<>();
        String sql = "SELECT * FROM results";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                results.add(new ResultDTO(
                    rs.getInt("rsID"),
                    rs.getInt("userID"),
                    rs.getString("exCode"),
                    rs.getString("rsAnswer"),
                    rs.getDouble("rsMark"),
                    rs.getTimestamp("rsDate").toLocalDateTime()
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
        String sql = "SELECT * FROM results WHERE rsID=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
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
        String sql = "DELETE FROM results WHERE rsID=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
