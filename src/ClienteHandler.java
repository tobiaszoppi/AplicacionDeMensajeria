import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Representa un manejador de cliente que se encarga de procesar mensajes
 * entrantes y salientes para un cliente específico.
 */
public class ClienteHandler implements Runnable {

    /**
     * Una lista compartida de todos los manejadores de clientes conectados al servidor.
     */
    public static ArrayList<ClienteHandler> clienteHandlers = new ArrayList<>();

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clienteUsername;

    /**
     * Crea un nuevo manejador de cliente para el socket especificado.
     * Este método inicializa los flujos de entrada y salida, lee el nombre
     * del cliente y agrega el manejador a clienteHandlers.
     * @param socket el socket del cliente
     */
    public ClienteHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clienteUsername = bufferedReader.readLine();
        } catch (IOException e) {
            cerrarTodo(socket, bufferedReader, bufferedWriter);
            return;
        }
        clienteHandlers.add(this);
        mostrarMensaje("SERVIDOR: Bienvenido " + clienteUsername);
    }

    @Override
    public void run() {
        String mensajeDelCliente;
        while (socket.isConnected()) {
            try {
                mensajeDelCliente = bufferedReader.readLine();
                mostrarMensaje(clienteUsername + ": " + mensajeDelCliente);
            } catch (IOException e) {
                cerrarTodo(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    /**
     * Envia un mensaje a todos los demás clientes (excluyendo al cliente actual)
     * que están conectados al servidor.
     * @param message el mensaje a enviar
     */
    public void mostrarMensaje(String message) {
        for (ClienteHandler clienteHandler : clienteHandlers) {
            if (clienteHandler.socket.isConnected() && clienteHandler != this) {
                try {
                    clienteHandler.bufferedWriter.write(message);
                    clienteHandler.bufferedWriter.newLine();
                    clienteHandler.bufferedWriter.flush();
                } catch (IOException e) {
                    cerrarTodo(socket, bufferedReader, bufferedWriter);
                }
            }
        }
    }

    /**
     * Remueve este manejador de cliente de clienteHandlers y notifica a
     * los demás clientes que el cliente actual se ha desconectado.
     */
    public void removerClienteHandler() {
        clienteHandlers.remove(this);
        if (socket.isConnected()) {
            mostrarMensaje("SERVIDOR: " + clienteUsername + " se ha desconectado");
        }
    }

    /**
     * Cierra el socket, los flujos de entrada y salida asociados y remueve este manejador de cliente
     * de clienteHandlers.
     * @param socket el socket del cliente
     * @param bufferedReader el BufferedReader del cliente
     * @param bufferedWriter el BufferedWriter del cliente
     */
    public void cerrarTodo(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removerClienteHandler();
        try {
            if (socket != null) {
                socket.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
