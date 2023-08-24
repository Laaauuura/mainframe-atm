package bo.edu.ucb.sis213.dao;
    
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class functionsHisAtm {

    private static Connection connection = null;

    public functionsHisAtm() {
        try {
            connection = conectionAtm.getConnection();
        } catch (Exception ex) {
            System.err.println("No se puede conectar a Base de Datos");
            ex.printStackTrace();
            System.exit(1);
        }
    }
    public void operacionesHis(int usuarioId){
        try{
            String query = "SELECT tipo_operacion FROM hisorico WHERE usuario_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, usuarioId);
            ResultSet resultSet = preparedStatement.executeQuery();
        }  catch (SQLException e) {
                e.printStackTrace();
            }

    }

    public  void registrarOperacion(int usuarioId, String tipoOperacion, double cantidad) {
            try {
                String insertQuery = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, usuarioId);
                insertStatement.setString(2, tipoOperacion);
                insertStatement.setDouble(3, cantidad);
                insertStatement.executeUpdate();
        
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        public  ResultSet obtenerHistorial(int usuarioId) {
            ResultSet resultSet = null;

            try {
                String query = "SELECT tipo_operacion, cantidad, fecha FROM historico WHERE usuario_id = ? ORDER BY fecha DESC LIMIT 5";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, usuarioId);
                resultSet = preparedStatement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return resultSet;
        }
    }