package bo.edu.ucb.sis213;
import javax.swing.*;

import bo.edu.ucb.sis213.view.loginAtm;


public class atmApp {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            loginAtm login = new loginAtm();
            login.setVisible(true);
    
        });
    }
}