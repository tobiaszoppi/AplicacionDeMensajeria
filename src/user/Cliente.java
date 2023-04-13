package user;

import gui.VentanaChat;
import gui.VentanaInicio;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cliente {
    private Socket socket;
    private BufferedReader lectorDeBuffer;
    private BufferedWriter escritorDeBuffer;
    private String nombreDeUsuario;
    private VentanaChat ventana;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public Cliente(Socket socket, String nombreDeUsuario) throws IOException {
        this.socket = socket;
        this.lectorDeBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.escritorDeBuffer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.nombreDeUsuario = nombreDeUsuario;
        escribirMensaje(nombreDeUsuario);
    }

    public void start() {
        // Iniciamos la ventana de chat
        ventana = new VentanaChat();
        ventana.setVisible(true);
        ventana.setTitle(this.nombreDeUsuario);

        ventana.agregarEventoEnviar(new EnviarMensajeListener());

        // Inicia el hilo para recibir mensajes del servidor
        escucharMensajes();
    }

    public void close() {
        ventana.dispose();
        cerrarConexion();
        executor.shutdownNow();
    }

    /**
     * Envía un mensaje al servidor.
     *
     * @param mensaje el mensaje a enviar
     * @throws IOException si hay un problema al enviar el mensaje
     */
    private void escribirMensaje(String mensaje) throws IOException {
        escritorDeBuffer.write(mensaje);
        escritorDeBuffer.newLine();
        escritorDeBuffer.flush();
    }

    /**
     * Escucha mensajes del servidor y los muestra en el área de mensajes.
     */
    private void escucharMensajes() {
        executor.submit(() -> {
            try {
                String mensaje;
                while ((mensaje = lectorDeBuffer.readLine()) != null) {
                    String finalMensaje = mensaje;
                    SwingUtilities.invokeLater(() -> {
                        ventana.agregarMensaje(finalMensaje);
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
     * Clase para manejar el envío de mensajes desde el campo de texto.
     */
    private class EnviarMensajeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String mensaje = ventana.campoTexto.getText();
            ventana.agregarMensajePropio(mensaje, nombreDeUsuario);
            try {
                escribirMensaje(mensaje);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ventana.campoTexto.setText("");
        }
    }


    public static void main(String[] args) {
        iniciarSesion();
    }

    public static void iniciarSesion() {
        VentanaInicio inicio = new VentanaInicio();
        inicio.setVisible(true);

        inicio.agregarEventoIniciarSesion(ActionEvent -> {
            UserServices uServices = new UserServices();
            try {
                if (uServices.handleAccess(inicio.getUsuario(), inicio.getContraseña())) {
                    iniciarVentanaChat(inicio.getUsuario());
                    inicio.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
                    System.out.println("validacion de credenciales fallida");
                }
            } catch (SQLException | IOException e) {
                System.exit(0);
                throw new RuntimeException(e);
            }
        });
    }

    public static void iniciarVentanaChat(String usuario) throws IOException {
        Socket socket = new Socket("localhost", 4444);
        Cliente cliente = new Cliente(socket, usuario);
        cliente.start();
    }
}