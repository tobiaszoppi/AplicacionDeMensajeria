import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class Cliente {
    private Socket socket;
    private BufferedReader lectorDeBuffer;
    private BufferedWriter escritorDeBuffer;
    private String nombreDeUsuario;

    private JFrame ventana;
    private JTextArea areaDeMensajes;
    private JTextField campoDeTexto;

    public Cliente(Socket socket) throws IOException {
        // Conecta al servidor y crea un nuevo cliente
        this.socket = socket;
        this.escritorDeBuffer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.lectorDeBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.nombreDeUsuario = JOptionPane.showInputDialog("Introduce tu nombre de usuario:");

        // Envía el nombre de usuario al servidor
        escribirMensaje(nombreDeUsuario);

        // Inicia el hilo para recibir mensajes del servidor
        new Thread(this::escucharMensajes).start();

        // Crea la interfaz gráfica
        SwingUtilities.invokeLater(this::crearInterfazGrafica);
    }

    /**
     * Crea la interfaz gráfica.
     */
    private void crearInterfazGrafica() {
        // Crea la ventana principal
        ventana = new JFrame(nombreDeUsuario);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(600, 400);

        // Crea el área de mensajes
        areaDeMensajes = new JTextArea();
        areaDeMensajes.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaDeMensajes);

        // Crea el campo de texto y el botón de enviar
        campoDeTexto = new JTextField();
        campoDeTexto.addActionListener(new EnviarMensajeListener());
        JButton botonEnviar = new JButton("Enviar");
        botonEnviar.addActionListener(new EnviarMensajeListener());

        // Crea el panel inferior con el campo de texto y el botón de enviar
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(campoDeTexto, BorderLayout.CENTER);
        panelInferior.add(botonEnviar, BorderLayout.EAST);

        // Agrega los componentes a la ventana
        ventana.getContentPane().add(scrollPane, BorderLayout.CENTER);
        ventana.getContentPane().add(panelInferior, BorderLayout.SOUTH);

        // Muestra la ventana
        ventana.setVisible(true);
    }


    /**
     * Envía un mensaje al servidor.
     *
     * @param mensaje el mensaje a enviar
     * @throws IOException si hay un problema al enviar el mensaje
     */
    private void escribirMensaje(String mensaje) {
        try {
            escritorDeBuffer.write(mensaje);
            escritorDeBuffer.newLine();
            escritorDeBuffer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Escucha mensajes del servidor y los muestra en el área de mensajes.
     */
    private void escucharMensajes() {
        while (socket.isConnected()) {
            try {
                String mensajeDelServidor = lectorDeBuffer.readLine();
                if (mensajeDelServidor != null && !mensajeDelServidor.isEmpty() && areaDeMensajes != null) {
                    SwingUtilities.invokeLater(() -> {
                        areaDeMensajes.append(mensajeDelServidor + "\n");
                    });
                } else {
                    cerrarConexion();
                }
            } catch (IOException e) {
                System.err.println("Error al leer el mensaje del servidor: " + e.getMessage());
                cerrarConexion();
            } catch (Exception e) {
                System.err.println("Error al procesar el mensaje del servidor: " + e.getMessage());
                cerrarConexion();
            }
        }
    }


    /**
     * Cierra el socket y los flujos de entrada/salida.
     */
    private void cerrarConexion() {
        try {
            socket.close();
            lectorDeBuffer.close();
            escritorDeBuffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clase interna para manejar el envío de mensajes desde el campo de texto.
     */
    private class EnviarMensajeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String mensaje = campoDeTexto.getText();
            escribirMensaje(mensaje);
            areaDeMensajes.append(nombreDeUsuario + ": " + mensaje + "\n"); // Agregamos el mensaje al área de mensajes
            campoDeTexto.setText("");
        }
    }

    /**
     * Método principal para ejecutar el cliente.
     */
    public static void main(String[] args) throws IOException {
        // Conecta al servidor y crea un nuevo cliente
        Socket socket = new Socket("localhost", 4444);
        // Crea un nuevo cliente con el nombre de usuario proporcionado
        Cliente cliente = new Cliente(socket);

        // Muestra un diálogo para que el usuario introduzca su contraseña de usuario

        String contraseñaDeUsuario = JOptionPane.showInputDialog("Introduce tu contraseña de usuario:");

        // Si el usuario no cancela el diálogo, utiliza el nombre de usuario proporcionado
        if (cliente.nombreDeUsuario != null) {
            UserServices us = new UserServices();
            try {
                if (us.handleRegistration(cliente.nombreDeUsuario, contraseñaDeUsuario)){
                    cliente.ventana.setTitle(cliente.nombreDeUsuario);
                    cliente.escribirMensaje(" se ha unido a la sala.");

                    // Inicia el hilo para recibir mensajes del servidor
                    new Thread(cliente::escucharMensajes).start();
                } else {
                    System.out.println("Error al registrarse/iniciar sesión");
                    System.exit(0);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}