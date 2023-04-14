package user;

import java.sql.SQLException;
import java.util.HashMap;

public class Comandos extends UserServices {
    // HashMap que almacena los comandos y su correspondiente acción
    private HashMap<String, Comando> comandos;

    // Constructor que inicializa el HashMap y agrega los comandos y su correspondiente acción
    public Comandos(boolean isAdmin) {
        comandos = new HashMap<String, Comando>();

        // Si isAdmin es true, se agregan más comandos al HashMap
        if (isAdmin) {
            comandos.put("/kick", new KickComando());
            comandos.put("/mute", new MuteComando());
        }

        comandos.put("/ayuda", new AyudaComando());
        comandos.put("/salir", new SalirComando());
        comandos.put("/setadmin", new SetAdminComando());
    }

    // Método que verifica si un mensaje contiene un comando y lo ejecuta
    public boolean ejecutarComando(String mensaje, Cliente cliente) {
        // Verificar si el mensaje comienza con una barra
        System.out.println("Mensaje: " + mensaje);
        if (mensaje.charAt(0) != '/') {
            return false;
        }

        // Separar el mensaje en el comando y sus argumentos
        String[] partes = mensaje.split(" ");
        String comando = partes[0];
        String[] argumentos = new String[partes.length - 1];
        for (int i = 1; i < partes.length; i++) {
            argumentos[i - 1] = partes[i];
        }
        /*
        String[] partes = mensaje.split(" ");: esta línea divide el mensaje en dos partes:
        el comando y sus argumentos, separados por espacios en blanco.
        La división se realiza utilizando el método split() que recibe como parámetro un espacio en blanco.

        String comando = partes[0];: esta línea almacena el comando en una variable llamada comando.
        El comando es la primera parte del mensaje dividido anteriormente.

        String[] argumentos = new String[partes.length - 1];:
        esta línea almacena los argumentos en un arreglo de cadenas llamado argumentos.
        Se utiliza la longitud del arreglo partes para determinar cuántos argumentos hay
        (la longitud menos uno, ya que el primer elemento es el comando).

        for (int i = 1; i < partes.length; i++) { argumentos[i - 1] = partes[i]; }:
        esta línea recorre las partes restantes del mensaje (los argumentos) y las agrega a argumentos.
         */

        // Buscar el comando en el HashMap y ejecutarlo si se encuentra
        Comando accion = comandos.get(comando);
        if (accion != null) {
            accion.ejecutar(argumentos, cliente);
            return true;
        }

        return false;
    }

    // Interfaz para definir la acción de un comando
    private interface Comando {
        public void ejecutar(String[] argumentos, Cliente cliente);
    }

    // Clase que define la acción del comando /ayuda
    private class AyudaComando implements Comando {
        public void ejecutar(String[] argumentos, Cliente cliente) {

            for (String comando : comandos.keySet()) {

            }
        }
    }

    // Clase que define la acción del comando /salir
    private class SalirComando implements Comando {
        public void ejecutar(String[] argumentos, Cliente cliente) {
            cliente.close();
        }
    }

    // Clase que define la acción del comando /setadmin
    private class SetAdminComando implements Comando {
        public void ejecutar(String[] argumentos, Cliente cliente) {

        }
    }


    // Clase que define la acción del comando /kick
    private class KickComando implements Comando {

        public void ejecutar(String[] argumentos, Cliente cliente) {

        }
    }

    // Clase que define la acción del comando /mute
    private class MuteComando implements Comando {
        public void ejecutar(String[] argumentos, Cliente cliente) {
            System.out.println("Comando /mute");
        }
    }
}
