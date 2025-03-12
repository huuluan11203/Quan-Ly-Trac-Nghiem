/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.dao;

import com.tracnghiem.config.JDBCUtil;
import com.tracnghiem.dto.UserDTO;
import com.tracnghiem.utils.PasswordUtil;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author X
 */
public class UserDAO implements InterfaceDAO<UserDTO> {
    
    
    public static UserDAO getInstance() {
        return new UserDAO();
    }
    @Override
    public boolean insert(UserDTO user) {
        
        user.setUserPassword(PasswordUtil.hashPassword(user.getUserPassword()));
        String sql = "INSERT INTO users (userName, userEmail, userPassword, userFullName, isAdmin) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getUserEmail());
            stmt.setString(3, user.getUserPassword());
            stmt.setString(4, user.getUserFullName());
            stmt.setInt(5, user.getIsAdmin());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(UserDTO user) {
        user.setUserPassword(PasswordUtil.hashPassword(user.getUserPassword()));
        String sql = "UPDATE users SET userName=?, userEmail=?, userPassword=?, userFullName=?, isAdmin=?, userStatus=? WHERE userID=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getUserEmail());
            stmt.setString(3, user.getUserPassword());
            stmt.setString(4, user.getUserFullName());
            stmt.setInt(5, user.getIsAdmin());
            stmt.setInt(6, user.getUserStatus());
            stmt.setInt(7, user.getUserID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM users WHERE userID=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public UserDTO selectByID(String id) {
        String sql = "SELECT * FROM users WHERE userID=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserDTO(
                    rs.getInt("userID"),
                    rs.getString("userName"),
                    rs.getString("userEmail"),
                    rs.getString("userPassword"),
                    rs.getString("userFullName"),
                    rs.getInt("isAdmin"),
                    rs.getInt("userStatus")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public UserDTO selectByUserName(String user) {
        String sql = "SELECT * FROM users WHERE userName=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(user));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserDTO(
                    rs.getInt("userID"),
                    rs.getString("userName"),
                    rs.getString("userEmail"),
                    rs.getString("userPassword"),
                    rs.getString("userFullName"),
                    rs.getInt("isAdmin"),
                    rs.getInt("userStatus")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<UserDTO> selectAll() {
        ArrayList<UserDTO> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new UserDTO(
                    rs.getInt("userID"),
                    rs.getString("userName"),
                    rs.getString("userEmail"),
                    rs.getString("userPassword"),
                    rs.getString("userFullName"),
                    rs.getInt("isAdmin"),
                    rs.getInt("userStatus")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
}
