package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int intentos = 3;

        System.out.println("Bienvenido al Cajero Automático.");

        Connection connection = null;
        try {
            connection = getConnection();
        } catch (SQLException ex) {
            System.err.println("No se pudo conectar");
            ex.printStackTrace();
            System.exit(1);
        }

        while (intentos > 0) {
            System.out.println("Ingrese su PIN de 4 dígitos: ");
            int pinIngresado = scanner.nextInt();
            if (validarPIN(connection, pinIngresado)) {
                pinActual = pinIngresado;
                mostrarMenu(connection);
                break;
            } else {
                intentos--;
                if (intentos > 0) {
                    System.out.println("PIN incorrecto. Le quedan " + intentos + " intentos");
                } else {
                    System.out.println("PIN incorrecto. Ha excedido el número de intentos");
                    System.exit(0);
                }
            }
        }
    }

    public static boolean validarPIN (Connection connection, int pin){
        String query = "SELECT id, saldo FROM usuarios WHERE pin = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, pin);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if(resultSet.next()){
                usuarioId = resultSet.getInt("id");
                saldo = resultSet.getDouble("saldo");
                return true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void mostrarMenu(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nMenú Principal:");
            System.out.println("1. Consultar saldo.");
            System.out.println("2. Realizar un depósito.");
            System.out.println("3. Realizar un retiro.");
            System.out.println("4. Registro histórico.");
            System.out.println("5. Cambiar PIN.");
            System.out.println("6. Salir.");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    consultarSaldo(connection);
                    break;
                case 2:
                    realizarDeposito(connection);
                    break;
                case 3:
                    realizarRetiro(connection);
                    break;
                case 4:
                    registroHistorico(connection);
                    break;
                case 5:
                    cambiarPIN(connection);
                    break;
                case 6:
                    System.out.println("Gracias por usar el cajero. ¡Hasta luego!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    public static void consultarSaldo(Connection connection) {
        try {
            String query = "SELECT saldo FROM usuarios WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, usuarioId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                double saldoActual = resultSet.getDouble("saldo");
                System.out.println("Su saldo actual es: $" + saldoActual);
            } else {
                System.out.println("Error al consultar saldo.");
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
                    System.out.println("Depósito realizado con éxito. Su nuevo saldo es: $" + (saldo + cantidad));
    
                    registrarOperacion(connection, "depóito", cantidad);
                } else {
                    System.out.println("Error al realizar el depósito.");
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
                    System.out.println("Retiro realizado con éxito. Su nuevo saldo es: $" + (saldo - cantidad));
                    
                    registrarOperacion(connection, "retiro", cantidad);
                } else {
                    System.out.println("Error al realizar el retiro.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
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
    
            System.out.println("\nÚltimos 5 movimientos:");
            while (resultSet.next()) {
                String tipoOperacion = resultSet.getString("tipo_operacion");
                double cantidad = resultSet.getDouble("cantidad");
                String fecha = resultSet.getString("fecha");
                System.out.println(tipoOperacion + " - $" + cantidad + " - " + fecha);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public static void cambiarPIN(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese su PIN actual: ");
        int pinIngresado = scanner.nextInt();

        if (pinIngresado == pinActual) {
            System.out.print("Ingrese su nuevo PIN: ");
            int nuevoPin = scanner.nextInt();
            System.out.print("Confirme su nuevo PIN: ");
            int confirmacionPin = scanner.nextInt();

            if (nuevoPin == confirmacionPin) {
                try {
                    String updateQuery = "UPDATE usuarios SET pin = ? WHERE id = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, nuevoPin);
                    updateStatement.setInt(2, usuarioId);
                    int rowsAffected = updateStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        pinActual = nuevoPin;
                        System.out.println("PIN actualizado con éxito.");
                    } else {
                        System.out.println("Error al cambiar el PIN.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Los PINs no coinciden.");
            }
        } else {
            System.out.println("PIN incorrecto.");
        }
    }
}
