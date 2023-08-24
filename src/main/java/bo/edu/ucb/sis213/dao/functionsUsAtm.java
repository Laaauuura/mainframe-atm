package bo.edu.ucb.sis213.dao;
    
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class functionsUsAtm {

    private static Connection connection = null;

    public functionsUsAtm() {
        try {
            connection = conectionAtm.getConnection();
        } catch (Exception ex) {
            System.err.println("No se puede conectar a Base de Datos");
            ex.printStackTrace();
            System.exit(1);
        }
    }
    public  double consultarSaldo(int usuarioId) {
        double saldoActual = 0;

        try {
            String query = "SELECT saldo FROM usuarios WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, usuarioId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                saldoActual = resultSet.getDouble("saldo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return saldoActual;
    }

    public  void realizarOp(int usuarioId, double cantidad) {
        try {
            String updateQuery = "UPDATE usuarios SET saldo = ? WHERE id = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setDouble(1, cantidad);
            updateStatement.setInt(2, usuarioId);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  void cambiarPIN(int usuarioId, int nuevoPin) {
        try {
            String updateQuery = "UPDATE usuarios SET password = ? WHERE id = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setInt(1, nuevoPin);
            updateStatement.setInt(2, usuarioId);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int validarCredenciales(String username, int password) {
        try {
            String query = "SELECT id FROM usuarios WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Indicador de credenciales incorrectas
    }

    public  int pinAct(String username) {
        try {
            String query = "SELECT pasword FROM usuarios WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Indicador de credenciales incorrectas
    }


    
    }