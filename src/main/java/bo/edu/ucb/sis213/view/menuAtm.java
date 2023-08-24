package bo.edu.ucb.sis213.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class menuAtm extends JFrame {

    public menuAtm() {
        setTitle("Bienvenido a MiBanco");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(220, 240, 255)); // Color de fondo

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("SELECCIONE UNA OPCIÓN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Estilo del título
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2; // Ocupa dos columnas
        panel.add(titleLabel, constraints);

        JButton consultarSaldoButton = new JButton("Consultar Saldo");
        JButton realizarDepositoButton = new JButton("Realizar Depósito");
        JButton realizarRetiroButton = new JButton("Realizar Retiro");
        JButton registroHistoricoButton = new JButton("Registro Histórico");
        JButton cambiarPINButton = new JButton("Cambiar PIN");
        JButton salirButton = new JButton("Salir");

        consultarSaldoButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
    
        }
    });

        // Colores para los botones
        consultarSaldoButton.setBackground(new Color(60, 179, 113));
        realizarDepositoButton.setBackground(new Color(70, 130, 180));
        realizarRetiroButton.setBackground(new Color(255, 165, 0));
        registroHistoricoButton.setBackground(new Color(147, 112, 219));
        cambiarPINButton.setBackground(new Color(255, 99, 71));
        salirButton.setBackground(new Color(192, 192, 192));

        constraints.gridwidth = 1;
        constraints.gridy = 1;
        panel.add(consultarSaldoButton, constraints);

        constraints.gridy = 2;
        panel.add(realizarDepositoButton, constraints);

        constraints.gridy = 3;
        panel.add(realizarRetiroButton, constraints);

        constraints.gridy = 4;
        panel.add(registroHistoricoButton, constraints);

        constraints.gridy = 5;
        panel.add(cambiarPINButton, constraints);

        constraints.gridy = 6;
        panel.add(salirButton, constraints);

        add(panel);
    }

}
