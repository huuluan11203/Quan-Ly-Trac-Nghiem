/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.dao;

import com.tracnghiem.config.JDBCUtil;
import com.tracnghiem.dto.TestDTO;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author X
 */
public class TestDAO implements InterfaceDAO<TestDTO> {
    public static TestDAO getInstance() {
        return new TestDAO();
    }
    @Override
    public boolean insert(TestDTO test) {
        String sql = "INSERT INTO test(testID, testCode, testTitle, testTime, testLimit, testDate, testStatus) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, test.getTestID());
            ps.setString(2, test.getTestCode());
            ps.setString(3, test.getTestTittle());
            ps.setInt(4, test.getTestTime());
            ps.setInt(5, test.getTestLimit());
            ps.setDate(6, Date.valueOf(test.getTestDate()));
            ps.setInt(7, test.getTestStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(TestDTO test) {
        String sql = "UPDATE test SET testCode=?, testTitle=?, testTime=?, testLimit=?, testDate=?, testStatus=? WHERE testID=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, test.getTestCode());
            ps.setString(2, test.getTestTittle());
            ps.setInt(3, test.getTestTime());
            ps.setInt(4, test.getTestLimit());
            ps.setDate(5, Date.valueOf(test.getTestDate()));
            ps.setInt(6, test.getTestStatus());
            ps.setInt(7, test.getTestID());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<TestDTO> selectAll() {
        ArrayList<TestDTO> tests = new ArrayList<>();
        String sql = "SELECT * FROM test WHERE testStatus=1";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tests.add(new TestDTO(
                    rs.getInt("testID"),
                    rs.getString("testCode"),
                    rs.getString("testTitle"),
                    rs.getInt("testTime"),
                    rs.getInt("testLimit"),
                    rs.getDate("testDate").toLocalDate(),
                    rs.getInt("testStatus")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tests;
    }

    @Override
    public TestDTO selectByID(String id) {
        TestDTO test = null;
        String sql = "SELECT * FROM test WHERE testID=? AND testStatus=1";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    test = new TestDTO(
                        rs.getInt("testID"),
                        rs.getString("testCode"),
                        rs.getString("testTitle"),
                        rs.getInt("testTime"),
                        rs.getInt("testLimit"),
                        rs.getDate("testDate").toLocalDate(),
                        rs.getInt("testStatus")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return test;
    }

    @Override
    public boolean delete(String id) {
        String sql = "UPDATE test SET testStatus=0 WHERE testID=?";
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
        String sql = "SELECT MAX(testID) FROM test";
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

