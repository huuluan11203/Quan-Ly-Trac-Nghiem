/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.dao;

import com.tracnghiem.config.JDBCUtil;
import com.tracnghiem.dto.LogDTO;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author X
 */
public class LogDAO implements InterfaceDAO<LogDTO> {

    public static LogDAO getInstance() {
        return new LogDAO();
    }

    @Override
    public boolean insert(LogDTO log) {
        boolean rs = false;
        String sql = "INSERT INTO logs(logContent, logUserID, logExCode, logDate) VALUES(?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, log.getLogContent());
            ps.setInt(2, log.getLogUserID());
            ps.setString(3, log.getLogExCode());
            ps.setTimestamp(4, Timestamp.valueOf(log.getLogDate()));
            rs = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    @Override
    public boolean update(LogDTO log) {
        boolean rs = false;
        String sql = "UPDATE logs SET logContent=?, logUserID=?, logExCode=?, logDate=? WHERE logID=?";
        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, log.getLogContent());
            ps.setInt(2, log.getLogUserID());
            ps.setString(3, log.getLogExCode());
            ps.setTimestamp(4, Timestamp.valueOf(log.getLogDate()));
            ps.setInt(5, log.getLogID());
            rs = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    @Override
    public ArrayList<LogDTO> selectAll() {
        ArrayList<LogDTO> rs = new ArrayList<>();
        String sql = "SELECT * FROM logs";
        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rsSet = ps.executeQuery()) {
            while (rsSet.next()) {
                rs.add(new LogDTO(
                        rsSet.getInt("logID"),
                        rsSet.getString("logContent"),
                        rsSet.getInt("logUserID"),
                        rsSet.getString("logExCode"),
                        rsSet.getTimestamp("logDate").toLocalDateTime()
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    public ArrayList<LogDTO> selectByUserID(int userID) {
        ArrayList<LogDTO> rs = new ArrayList<>();
        String sql = "SELECT * FROM logs WHERE logUserID = ?";

        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userID); // Gán giá trị userID vào câu lệnh SQL

            try (ResultSet rsSet = ps.executeQuery()) {
                while (rsSet.next()) {
                    rs.add(new LogDTO(
                            rsSet.getInt("logID"),
                            rsSet.getString("logContent"),
                            rsSet.getInt("logUserID"),
                            rsSet.getString("logExCode"),
                            rsSet.getTimestamp("logDate").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    @Override
    public LogDTO selectByID(String logID) {
        LogDTO rs = null;
        String sql = "SELECT * FROM logs WHERE logID=?";
        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(logID));
            try (ResultSet rsSet = ps.executeQuery()) {
                if (rsSet.next()) {
                    rs = new LogDTO(
                            rsSet.getInt("logID"),
                            rsSet.getString("logContent"),
                            rsSet.getInt("logUserID"),
                            rsSet.getString("logExCode"),
                            rsSet.getTimestamp("logDate").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    @Override
    public boolean delete(String logID) {
        boolean rs = false;
        String sql = "DELETE FROM logs WHERE logID = ?";
        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(logID));
            rs = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }
}
