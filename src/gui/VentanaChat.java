package gui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaChat extends JFrame {

    private JPanel panelPrincipal;
    private JPanel panelEnviar;
    private JTextArea areaMensajes;
    public JTextField campoTexto;
    private JButton botonEnviar = new JButton("Enviar");
    private JScrollPane scrollPane;

    public VentanaChat() {
        // Ejecutar en el hilo de despacho de eventos
        SwingUtilities.invokeLater(() -> {
            crearVentana();
            crearCampoTexto();
            crearPanelPrincipal();
            crearAreaMensajes();
            crearPanelEnviar();
            agregarElementosAPanelPrincipal();
            centrarVentana();
        });
    }

    private void crearVentana() {
        // Crear ventana con título y operación de cierre
        setTitle("Chat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setSize(800, 600);
        setFont(new Font("Helvetica Neue", Font.BOLD, 24));
    }

    private void crearPanelPrincipal() {
        // Crear panel principal y asignar BorderLayout
        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(240, 240, 240));
        panelPrincipal.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.lightGray, 1), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        getContentPane().add(panelPrincipal);
    }

    private void crearAreaMensajes() {
        // Crear área de texto para mensajes y agregar a panel principal
        areaMensajes = new JTextArea();
        areaMensajes.setEditable(false);
        scrollPane = new JScrollPane(areaMensajes);
        areaMensajes.setBackground(new Color(255, 255, 255));
        areaMensajes.setForeground(new Color(51, 51, 51));
        areaMensajes.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
        scrollPane.setPreferredSize(new Dimension(800, 400));
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
    }

    private void crearPanelEnviar() {
        // Crear panel para enviar mensajes y asignar BorderLayout
        panelEnviar = new JPanel(new BorderLayout());
        panelEnviar.setBackground(new Color(240, 240, 240));
        panelEnviar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.lightGray, 1), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        panelPrincipal.add(panelEnviar, BorderLayout.SOUTH);
    }

    private void crearBotonEnviar() {
        // Crear botón para enviar mensajes y asignar ActionListener
        botonEnviar.setBackground(new Color(0, 123, 255));
        botonEnviar.setForeground(new Color(255, 255, 255));
        botonEnviar.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
        botonEnviar.setPreferredSize(new Dimension(120, 40));
    }

    private void crearCampoTexto() {
        // Crear campo de texto para escribir mensajes y asignar KeyListener
        campoTexto = new JTextField();
        crearBotonEnviar();
        campoTexto.setBackground(new Color(255, 255, 255));
        campoTexto.setForeground(new Color(51, 51, 51));
        campoTexto.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
        campoTexto.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.lightGray, 1), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        campoTexto.setPreferredSize(new Dimension(600, 40));
        campoTexto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    botonEnviar.doClick();
                }
            }
        });
    }

    private void agregarElementosAPanelPrincipal() {
        // Agregar campo de texto y botón enviar al panel para enviar mensajes
        panelEnviar.add(campoTexto, BorderLayout.CENTER);
        panelEnviar.add(botonEnviar, BorderLayout.EAST);
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

            // Bajar scroll a posición más reciente
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        }
    }

    public void notificarNuevoMensaje() {
        JLabel label = new JLabel("Nuevo Mensaje!");
        label.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 123, 255));
        panel.add(label, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.lightGray, 1), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        getContentPane().add(panel, BorderLayout.NORTH);
        pack();
        setVisible(true);
        Timer timer = new Timer(3000, e -> {
            getContentPane().remove(panel);
            pack();
            setVisible(true);
        });
        timer.setRepeats(false);
        timer.start();
    }
}