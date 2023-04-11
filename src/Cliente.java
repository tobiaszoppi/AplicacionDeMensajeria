import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Esta clase describe a un cliente que se conecta a un servidor de chat.
 */
public class Cliente {
    private final Socket socket;
    private final BufferedReader lectorDeBuffer;
    private final BufferedWriter escritorDeBuffer;
    private final String nombreDeUsuario;

    /**
     * Crea un nuevo cliente.
     *
     * @param socket              el socket utilizado para conectarse al servidor
     * @param nombreDeUsuario     el nombre de usuario del cliente
     * @throws IOException si hay un problema al crear los flujos de entrada/salida
     */
    public Cliente(Socket socket, String nombreDeUsuario) throws IOException {
        this.socket = socket;
        this.escritorDeBuffer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.lectorDeBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.nombreDeUsuario = nombreDeUsuario;
    }

    /**
     * Envía mensajes al servidor.
     *
     * @throws IOException si hay un problema al enviar mensajes al servidor
     */
    public void enviarMensajes() {
        try {
            // Envía el nombre de usuario al servidor
            escribirMensaje(nombreDeUsuario);

            // Lee la entrada del usuario y envía mensajes al servidor
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String mensajeDelUsuario = scanner.nextLine();
                escribirMensaje(mensajeDelUsuario);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Escucha mensajes del servidor y los imprime en la consola.
     */
    public void escucharMensajes() {
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    String mensajeDelServidor = lectorDeBuffer.readLine();
                    System.out.println(mensajeDelServidor);
                } catch (IOException e) {
                    cerrarConexion();
                }
            }
        }).start();
    }

    /**
     * Escribe un mensaje en el buffer de salida y lo envía al servidor.
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

    public static void main(String[] args) throws IOException {
        // Lee el nombre de usuario del cliente
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce tu nombre de usuario: ");
        String nombreDeUsuario = scanner.nextLine();
        System.out.println("Introduce tu contraseña de usuario: ");
        String contraseñaDeUsuario = scanner.nextLine();

        UserServices us = new UserServices();
        try {
            if (us.handleRegistration(nombreDeUsuario, contraseñaDeUsuario)){
                // Conecta al servidor y crea un nuevo cliente
                Socket socket = new Socket("localhost", 4444);
                Cliente cliente = new Cliente(socket, nombreDeUsuario);

                // Inicia los hilos para enviar y recibir mensajes
                new Thread(cliente::enviarMensajes).start();
                cliente.escucharMensajes();
            } else {
                System.out.println("Error al registrarse/iniciar sesión");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
