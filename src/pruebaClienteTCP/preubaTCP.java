/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebaClienteTCP;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import nordboxcad.Usuario;

/**
 *
 * @author Alejandro Campos Maestre
 */
public class preubaTCP extends Thread
{   
    String opcion, parametro1;

    public preubaTCP(String opcion, String parametro1)
    {
        this.opcion = opcion;
        this.parametro1 = parametro1;
    }
    
    @Override
    public void run() {
        try {
            
            Socket socketCliente = new Socket("127.0.0.1", 30501);
            
            System.out.println("USU - conectado a socket");
            
            DataOutputStream envio = new DataOutputStream(socketCliente.getOutputStream());
            envio.writeUTF(opcion + "-" + parametro1);
//            envio.writeUTF("alex@hot.ca");
            
            System.out.println("USU - enviado dos strings, a la espera de recepcion");
            
            ObjectInputStream recepcion = new ObjectInputStream(socketCliente.getInputStream());
            Usuario u = (Usuario) recepcion.readObject();
            
            System.out.println("USU - Recepcion de objeto usuario");
            
            System.out.println(u.toString());
            
            envio.close();
            recepcion.close();

        } catch (IOException ex) {
            System.err.println("IOException");
        } 
        catch (ClassNotFoundException ex)
        {
            System.err.println("ClassNotFoundException");
        }
    }

    public String getOpcion()
    {
        return opcion;
    }

    public void setOpcion(String opcion)
    {
        this.opcion = opcion;
    }

    public String getParametro1()
    {
        return parametro1;
    }

    public void setParametro1(String parametro1)
    {
        this.parametro1 = parametro1;
    }
    
}
