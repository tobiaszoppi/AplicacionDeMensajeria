# AplicacionDeMensajeria
Aplicacion de mensajeria en grupo.

Este proyecto de chat en grupo implementado en Java consta de un servidor que acepta conexiones de clientes y maneja las solicitudes en hilos separados, 
una clase que describe a un cliente que se conecta al servidor y envía y recibe mensajes, 
y una clase que representa un manejador de conexiones de cliente que maneja las solicitudes de un cliente en un hilo separado.

La clase Servidor representa un servidor que acepta conexiones de clientes y maneja las solicitudes en hilos separados. 
Tiene un método abrirServidor() que se encarga de aceptar nuevas conexiones de clientes y manejar cada una de ellas en un hilo separado. 
El método cerrarServidor() cierra el servidor y libera los recursos asociados. La clase también tiene un método main() que inicia el servidor en el puerto 4444.

La clase Cliente describe a un cliente que se conecta a un servidor de chat. 
Tiene un constructor que toma un socket para conectarse al servidor y el nombre de usuario del cliente. 
También tiene métodos para enviar mensajes al servidor, escuchar mensajes del servidor y escribir mensajes en el buffer de salida para enviarlos al servidor. 
La clase tiene un método main() que pide al usuario que introduzca su nombre de usuario, conecta al servidor y crea un nuevo cliente, 
y luego inicia dos hilos para enviar y recibir mensajes.

La clase ClienteHandler es una clase interna de la clase Servidor y representa un manejador de conexiones de cliente. 
Su función es manejar las solicitudes de un cliente en un hilo separado. 
Tiene un constructor que toma el socket de un cliente y tiene un método run() que se encarga de leer los mensajes del cliente y 
enviarlos a todos los clientes conectados al servidor.
