package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaChat extends JFrame {

    private JTextArea areaMensajes;
    public JTextField campoTexto;
    private JButton botonEnviar;

    public VentanaChat() {
        super("Chat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setSize(500, 400);
        centrarVentana();

        // Creamos el panel principal y le asignamos un BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        getContentPane().add(panelPrincipal);

        // Creamos el área de texto para los mensajes y lo agregamos al panel principal
        areaMensajes = new JTextArea();
        areaMensajes.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaMensajes);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Creamos el campo de texto y el botón para enviar mensajes
        JPanel panelEnviar = new JPanel(new BorderLayout());
        campoTexto = new JTextField();
        panelEnviar.add(campoTexto, BorderLayout.CENTER);
        campoTexto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    botonEnviar.doClick();
                }
            }
        });

        botonEnviar = new JButton("Enviar");
        panelEnviar.add(botonEnviar, BorderLayout.EAST);
        panelPrincipal.add(panelEnviar, BorderLayout.SOUTH);
    }

    private void centrarVentana() {
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = getSize();
        setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
    }

    public void agregarEventoEnviar(ActionListener listener) {
        botonEnviar.addActionListener(listener);
    }

    public void agregarMensaje(String mensaje) {
        if (!mensaje.isEmpty()) {
            areaMensajes.append(mensaje + "\n");
            campoTexto.setText("");
        }
    }

    public void agregarMensajePropio(String mensaje, String usuario) {
        if (!mensaje.isEmpty()) {
            areaMensajes.append(usuario + ": " + mensaje + "\n");
            campoTexto.setText("");
        }
    }

}
