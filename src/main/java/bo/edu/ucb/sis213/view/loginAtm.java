package bo.edu.ucb.sis213.view;

import javax.swing.*;

import bo.edu.ucb.sis213.bl.App;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class loginAtm extends JFrame {
    private JTextField usuarioField;
    private JPasswordField contrasenaField;

    App app = new App();

    public loginAtm() {
        setTitle("Bienvenido a MiBanco");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel tituloLabel = new JLabel("Bienvenido a MiBanco");
        tituloLabel.setHorizontalAlignment(JLabel.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(tituloLabel, constraints);

        JLabel usuarioLabel = new JLabel("Usuario:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(usuarioLabel, constraints);

        usuarioField = new JTextField(15);
        usuarioField.setMaximumSize(usuarioField.getPreferredSize());
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(usuarioField, constraints);

        JLabel contrasenaLabel = new JLabel("Contraseña:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(contrasenaLabel, constraints);

        contrasenaField = new JPasswordField(15);
        contrasenaField.setMaximumSize(contrasenaField.getPreferredSize());
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(contrasenaField, constraints);

        JButton botonIniciarSesion = new JButton("Iniciar Sesión");
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        panel.add(botonIniciarSesion, constraints);

        botonIniciarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String usuario = usuarioField.getText();
                char[] contrasena = contrasenaField.getPassword();
                String contrasenaStr = new String(contrasena);
                int contrasenaInt = Integer.parseInt(contrasenaStr);
                // Lógica de validación de inicio de sesión
                app.validarCredenciales(usuario, contrasenaInt);
                app.datos(usuario, contrasenaInt);
                JOptionPane.showMessageDialog(loginAtm.this,
                        "¡Bienvenido, " + usuario + "!\nHas iniciado sesión correctamente.",
                        "Inicio de Sesión Exitoso", JOptionPane.INFORMATION_MESSAGE);
                //menuAtm;
            }
        });

        add(panel);
    }
}
