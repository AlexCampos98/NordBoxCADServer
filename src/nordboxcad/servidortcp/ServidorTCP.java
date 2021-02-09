/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nordboxcad.servidortcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Alejandro Campos Maestre
 */
public class ServidorTCP extends Thread
{
    /**
     * Método que implementa el comportamiento del hilo
    */
    @Override
    public void run () {
        try {
            System.out.println("Servidor.Consola - Se abre un socket servidor en el puerto 30500 de la máquina local");
            int puertoServidor = 30501;
            ServerSocket socketServidor = new ServerSocket(puertoServidor);
            
            System.out.println("Servidor.Consola - Se crea un ArrayList para almacenar los manejadores de sockets de los clientes");
            ArrayList<SesionServidor> sesiones = new ArrayList();
            System.out.println("Servidor.Consola - El servidor queda a la espera indefinidamente de todas las conexiones de cliente que se produzcan");
            Socket clienteConectado;
            SesionServidor sesion;
            while (true) {
                clienteConectado = socketServidor.accept();
                System.out.println("Ha entrado");
                sesion = new SesionServidor(clienteConectado);
                sesiones.add(sesion);
                sesion.start();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
