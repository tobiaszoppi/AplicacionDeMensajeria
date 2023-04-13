package server;

import user.ClienteHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Representa un servidor que acepta conexiones de clientes y maneja las solicitudes en hilos separados.
 */
public class Servidor {

    private ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();


    /**
     * Crea una instancia de server.Servidor con el socket del servidor.
     *
     * @param serverSocket El socket del servidor.
     */
    public Servidor(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * Inicia el servidor y comienza a aceptar conexiones de clientes.
     */
    public void abrirServidor() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("Un nuevo cliente se conecto");

                // Manejar la conexión del cliente en un hilo separado
                ClienteHandler clienteHandler = new ClienteHandler(socket);

                executor.submit(clienteHandler);
            }
        } catch (IOException e) {
            cerrarServidor();
        }
    }

    /**
     * Cierra el servidor y libera los recursos asociados.
     */
    public void cerrarServidor() {
        try {
            if (serverSocket != null) {
                executor.shutdownNow();
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // Iniciar el servidor en el puerto 4444
        try (ServerSocket serverSocket = new ServerSocket(4444)) {
            Servidor servidor = new Servidor(serverSocket);
            servidor.abrirServidor();
        }
    }
}
