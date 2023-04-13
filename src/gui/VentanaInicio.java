package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaInicio extends JFrame {

    private JPanel panelContenido;
    private JTextField fieldUsuario;
    private JPasswordField fieldContraseña;
    private JButton btnIniciarSesion;

    public VentanaInicio() {
        super("Iniciar Sesión - Chat Grupal");

        // Configuración de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Crear el panel de contenido de la ventana
        panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Crear los componentes de la ventana
        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        GridBagConstraints gbcTitulo = new GridBagConstraints();
        gbcTitulo.gridx = 0;
        gbcTitulo.gridy = 0;
        gbcTitulo.gridwidth = GridBagConstraints.REMAINDER;
        gbcTitulo.insets = new Insets(0, 0, 20, 0);
        panelContenido.add(lblTitulo, gbcTitulo);

        JLabel lblUsuario = new JLabel("Nombre de Usuario:");
        fieldUsuario = new JTextField(20);
        fieldUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        GridBagConstraints gbcUsuario = new GridBagConstraints();
        gbcUsuario.gridx = 0;
        gbcUsuario.gridy = 1;
        gbcUsuario.anchor = GridBagConstraints.WEST;
        gbcUsuario.insets = new Insets(0, 0, 10, 5);
        panelContenido.add(lblUsuario, gbcUsuario);
        gbcUsuario.gridx = 1;
        gbcUsuario.gridy = 1;
        gbcUsuario.anchor = GridBagConstraints.EAST;
        gbcUsuario.insets = new Insets(0, 0, 10, 0);
        panelContenido.add(fieldUsuario, gbcUsuario);

        JLabel lblContraseña = new JLabel("Contraseña:");
        fieldContraseña = new JPasswordField(20);
        fieldContraseña.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldContraseña.addKeyListener(new CustomKeyListener());
        GridBagConstraints gbcContraseña = new GridBagConstraints();
        gbcContraseña.gridx = 0;
        gbcContraseña.gridy = 2;
        gbcContraseña.anchor = GridBagConstraints.WEST;
        gbcContraseña.insets = new Insets(0, 0, 10, 5);
        panelContenido.add(lblContraseña, gbcContraseña);
        gbcContraseña.gridx = 1;
        gbcContraseña.gridy = 2;
        gbcContraseña.anchor = GridBagConstraints.EAST;
        gbcContraseña.insets = new Insets(0, 0, 10, 0);
        panelContenido.add(fieldContraseña, gbcContraseña);

        btnIniciarSesion = new JButton("Iniciar Sesión");
        btnIniciarSesion.setFont(new Font("Arial", Font.PLAIN, 14));
        GridBagConstraints gbcBoton = new GridBagConstraints();
        gbcBoton.gridx = 0;
        gbcBoton.gridy = 3;
        gbcBoton.gridwidth = GridBagConstraints.REMAINDER;
        gbcBoton.anchor = GridBagConstraints.CENTER;
        panelContenido.add(btnIniciarSesion, gbcBoton);

        setContentPane(panelContenido);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class CustomKeyListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                btnIniciarSesion.doClick();
            }
        }
    }

    public void agregarEventoIniciarSesion(ActionListener listener) {
        btnIniciarSesion.addActionListener(listener);
    }

    public String getUsuario() {
        return fieldUsuario.getText();
    }

    public String getContraseña() {
        return new String(fieldContraseña.getPassword());
    }

}