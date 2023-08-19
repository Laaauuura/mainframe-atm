package bo.edu.ucb.sis213;

import bo.edu.ucb.sis213.App; 
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;  // Importar la clase Connection desde el paquete java.sql

public class atmApp {

    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    private Connection connection;
    private int usuarioId; // Agregado para evitar errores de símbolo
    private double saldo;  // Agregado para evitar errores de símbolo


    public atmApp(Connection connection) {
        this.connection = connection;
        initializeLoginScreen();
    }

    private void initializeLoginScreen() {
        frame = new JFrame("Login - ATM Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                int usuarioId = App.validarCredenciales(connection, username, password);
                
                if (usuarioId != -1) {
                    initializeMainMenu(usuarioId);
                    frame.dispose();
                } else {
                    showResultScreen("Error", "Credenciales incorrectas");
                }
            }
        });

        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(new JLabel()); // Espacio vacío
        frame.add(loginButton);

        frame.setVisible(true);
    }

    
    private void initializeMainMenu(int usuarioId) {
        JFrame menuFrame = new JFrame("ATM Menu");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(400, 300);
        menuFrame.setLayout(new GridLayout(7, 1));
    
        JButton consultarSaldoButton = new JButton("1. Consultar Saldo");
        JButton realizarDepositoButton = new JButton("2. Realizar Depósito");
        JButton realizarRetiroButton = new JButton("3. Realizar Retiro");
        JButton registroHistoricoButton = new JButton("4. Registro Histórico");
        JButton cambiarPINButton = new JButton("5. Cambiar PIN");
        JButton salirButton = new JButton("6. Salir");
    
        consultarSaldoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.consultarSaldo(connection);
            }
        });


        realizarDepositoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.realizarDeposito(connection); // Usa App.
            }
        });

    
        realizarRetiroButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.realizarRetiro(connection);
            }
        });
    
        registroHistoricoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.registroHistorico(connection);
            }
        });
    
        cambiarPINButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.cambiarPIN(connection);
            }
        });
    
        salirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Gracias por usar el cajero. ¡Hasta luego!");
                System.exit(0);
            }
        });
    

        menuFrame.add(consultarSaldoButton);
        menuFrame.add(realizarDepositoButton);
        menuFrame.add(realizarRetiroButton);
        menuFrame.add(registroHistoricoButton);
        menuFrame.add(cambiarPINButton);
        menuFrame.add(salirButton);

        menuFrame.setVisible(true);
    }

    private void showResultScreen(String title, String result) {
        JFrame resultFrame = new JFrame(title);
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.setSize(300, 150);
        resultFrame.setLayout(new BorderLayout());

        JLabel resultLabel = new JLabel(result);
        resultLabel.setHorizontalAlignment(JLabel.CENTER);

        resultFrame.add(resultLabel, BorderLayout.CENTER);

        resultFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Connection connection = App.getConnection();
                    new atmApp(connection);
                } catch (SQLException e) {
                    System.err.println("No se pudo conectar");
                    e.printStackTrace();
                }
            }
        });
    }
}
