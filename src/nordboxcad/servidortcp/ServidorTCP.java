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
            //Creo un socket por un puerto
            int puertoServidor = 30501;
            ServerSocket socketServidor = new ServerSocket(puertoServidor);
            
            //Creo array de sesiones para guardar las sesiones
            ArrayList<SesionServidor> sesiones = new ArrayList();
            Socket clienteConectado;
            SesionServidor sesion;
            
            while (true) {
                clienteConectado = socketServidor.accept();
                sesion = new SesionServidor(clienteConectado);
                sesiones.add(sesion);
                sesion.start();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
