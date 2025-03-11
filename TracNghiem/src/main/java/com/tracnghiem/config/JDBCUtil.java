package com.tracnghiem.config;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */



import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author X
 */
public class JDBCUtil {
    
    public static Connection getConnection() {
        Connection result = null;
        try {
            // Dang ky MySQL Driver voi DriverManager
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            
            /*Cac thong so
            //Phat
            String url = "jdbc:mySQL://localhost:2434/tracnghiem";
            String password = "";
            */
            
            // Luan
//            String url = "jdbc:mysql://127.0.0.1:3303/tracnghiem";
//            String password = "1234"; 
            // Phat
            String url = "jdbc:mysql://127.0.0.1:2434/tracnghiem";
            String password = ""; 
            // Kiet
//            String url = "jdbc:mysql://localhost/tracnghiem";
//            String password = "your_password";
            
            String userName = "root";         
            //Tao ket noi 
            result = DriverManager.getConnection(url, userName, password);
            
//            JOptionPane.showMessageDialog(null, "Kết nối đến cơ sở dữ liệu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến cơ sở dữ liệu !", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    
  
}



