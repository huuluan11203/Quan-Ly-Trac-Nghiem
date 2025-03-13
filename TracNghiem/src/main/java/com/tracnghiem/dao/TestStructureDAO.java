/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.dao;

import com.tracnghiem.config.JDBCUtil;
import com.tracnghiem.dto.TestDTO;
import com.tracnghiem.dto.TestStructureDTO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author X
 */
public class TestStructureDAO implements InterfaceDAO<TestStructureDTO>{
     public static TestStructureDAO getInstance() {
        return new TestStructureDAO();
    }
    @Override
    public boolean insert(TestStructureDTO ts) {
        String sql = "INSERT INTO test_structure( testCode, tpID, num_easy, num_medium, num_diff) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ts.getTestCode());
            ps.setInt(2, ts.getTpID());
            ps.setInt(3, ts.getNumEasy());
            ps.setInt(4, ts.getNumMedium());
            ps.setInt(5, ts.getNumDifficult());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    
    }

    @Override
    public boolean update(TestStructureDTO t) {
        String sql = "UPDATE test_structure SET tpID=?, num_easy=?, num_medium=?, num_diff=? WHERE testCode=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getTpID());
            ps.setInt(2, t.getNumEasy());
            ps.setInt(3, t.getNumMedium());
            ps.setInt(4, t.getNumDifficult());
            ps.setString(5, t.getTestCode());
           
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    
    
    }

    @Override
    public ArrayList<TestStructureDTO> selectAll() {
       ArrayList<TestStructureDTO> tests = new ArrayList<>();
        String sql = "SELECT * FROM test_structure";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tests.add(new TestStructureDTO(
                    rs.getString("testCode"),
                    rs.getInt("tpID"),
                    rs.getInt("num_easy"),
                    rs.getInt("num_medium"),
                    rs.getInt("num_diff")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tests;
        
    
    }

    @Override
    public TestStructureDTO selectByID(String testCode) {
        
        return null;
    
    }
    public ArrayList<TestStructureDTO> selectByTestCode(String testCode) {
        ArrayList<TestStructureDTO> tests = new ArrayList<>();
        String sql = "SELECT * FROM test_structure WHERE testCode=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, testCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tests.add(new TestStructureDTO(
                    rs.getString("testCode"),
                    rs.getInt("tpID"),
                    rs.getInt("num_easy"),
                    rs.getInt("num_medium"),
                    rs.getInt("num_diff")
                    ));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tests;
    
    }

    @Override
    public boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
