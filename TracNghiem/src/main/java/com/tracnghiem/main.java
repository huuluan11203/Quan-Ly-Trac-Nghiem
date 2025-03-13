package com.tracnghiem;


import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.tracnghiem.dto.UserDTO;
import javax.swing.SwingUtilities;
import com.tracnghiem.view.loginView;

public class main {
    public static void main(String[] args) {
        
        FlatLaf.registerCustomDefaultsSource("themes");       
        FlatLightLaf.setup();
           
        SwingUtilities.invokeLater(() -> {
//            new com.tracnghiem.view.mainView(new UserDTO()).setVisible(true);   // Khởi động giao diện chính
            new com.tracnghiem.view.loginView().setVisible(true);   // Khởi động giao diện chính
        });
    }
}
