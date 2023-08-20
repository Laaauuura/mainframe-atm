package bo.edu.ucb.sis213;


import java.sql.Connection;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class App {
    
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 3306;
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final String DATABASE = "atm";

    private static int  pinActual;
    private static int  usuarioId;  
    private static double saldo; 
    
    
    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new atmApp(connection);
                }
            });
        } catch (SQLException ex) {
            System.err.println("No se pudo conectar");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static Connection getConnection() throws SQLException {
        String jdbcUrl = String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DATABASE);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL Driver not found", e);
        }
        return DriverManager.getConnection(jdbcUrl, USER, PASSWORD);
    }


    public static void consultarSaldo(Connection connection) {
        try {
            String query = "SELECT saldo FROM usuarios WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, usuarioId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double saldoActual = resultSet.getDouble("saldo");
                JOptionPane.showMessageDialog(null, "Su saldo actual es: $" + saldoActual);
            } else {
                JOptionPane.showMessageDialog(null, "Error al consultar saldo.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void realizarDeposito(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a depositar: $");
        double cantidad = scanner.nextDouble();

        if (cantidad <= 0) {
            System.out.println("Cantidad no válida.");
        } else {
            try {
                String updateQuery = "UPDATE usuarios SET saldo = saldo + ? WHERE id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setDouble(1, cantidad);
                updateStatement.setInt(2, usuarioId);
                int rowsAffected = updateStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Depósito realizado con éxito. Su nuevo saldo es: $" + (saldo + cantidad));
                    registrarOperacion(connection, "depósito", cantidad);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al realizar el depósito.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void realizarRetiro(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a retirar: $");
        double cantidad = scanner.nextDouble();

        if (cantidad <= 0) {
            System.out.println("Cantidad no válida.");
        } else if (cantidad > saldo) {
            System.out.println("Saldo insuficiente.");
        } else {
            try {
                String updateQuery = "UPDATE usuarios SET saldo = saldo - ? WHERE id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setDouble(1, cantidad);
                updateStatement.setInt(2, usuarioId);
                int rowsAffected = updateStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Retiro realizado con éxito. Su nuevo saldo es: $" + (saldo - cantidad));
                    registrarOperacion(connection, "retiro", cantidad);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al realizar el retiro.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void cambiarPIN(Connection connection) {
        JTextField pinField = new JTextField();
        JPasswordField nuevoPinField = new JPasswordField();
        JPasswordField confirmacionPinField = new JPasswordField();

        Object[] message = {
                "Ingrese su PIN actual:", pinField,
                "Ingrese su nuevo PIN:", nuevoPinField,
                "Confirme su nuevo PIN:", confirmacionPinField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Cambiar PIN", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int pinIngresado = Integer.parseInt(pinField.getText());
            int nuevoPin = Integer.parseInt(new String(nuevoPinField.getPassword()));
            int confirmacionPin = Integer.parseInt(new String(confirmacionPinField.getPassword()));

            if (pinIngresado == pinActual) {
                if (nuevoPin == confirmacionPin) {
                    try {
                        String updateQuery = "UPDATE usuarios SET password = ? WHERE id = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                        updateStatement.setString(1, Integer.toString(nuevoPin));
                        updateStatement.setInt(2, usuarioId);
                        int rowsAffected = updateStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            pinActual = nuevoPin;
                            JOptionPane.showMessageDialog(null, "PIN actualizado con éxito.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al cambiar el PIN.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Los PINs no coinciden.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "PIN incorrecto.");
            }
        }
    }
    public static void registrarOperacion(Connection connection, String tipoOperacion, double cantidad) {
        try {
            String insertQuery = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, usuarioId);
            insertStatement.setString(2, tipoOperacion);
            insertStatement.setDouble(3, cantidad);
            int rowsAffected = insertStatement.executeUpdate();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void registroHistorico(Connection connection) {
        try {
            String query = "SELECT tipo_operacion, cantidad, fecha FROM historico WHERE usuario_id = ? ORDER BY fecha DESC LIMIT 5";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, usuarioId);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            StringBuilder historicoMessage = new StringBuilder();
            historicoMessage.append("\nÚltimos 5 movimientos:\n");
            while (resultSet.next()) {
                String tipoOperacion = resultSet.getString("tipo_operacion");
                double cantidad = resultSet.getDouble("cantidad");
                String fecha = resultSet.getString("fecha");
                historicoMessage.append(tipoOperacion).append(" - $").append(cantidad).append(" - ").append(fecha).append("\n");
            }
    
            JOptionPane.showMessageDialog(null, historicoMessage.toString(), "Historial de Operaciones", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int validarCredenciales(Connection connection, String username, String password) {
        try {
        String query = "SELECT id FROM usuarios WHERE username = '"+username+"' AND 'password' = '"+password+"'";
        
        
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //preparedStatement.setString(1, username);
            //preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
           
    
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return -1; // Indicador de credenciales incorrectas
    }    
    
}
 
