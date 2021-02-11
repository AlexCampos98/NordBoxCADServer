/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nordboxcad.servidortcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nordboxcad.EjercicioBenchUsuario;
import nordboxcad.EjerciciosBench;
import nordboxcad.ExcepcionNordBox;
import nordboxcad.NordBoxCAD;
import nordboxcad.Usuario;

/**
 *
 * @author Alejandro Campos Maestre
 */
public class SesionServidor extends Thread
{

    Socket clienteConectado;

    public SesionServidor(Socket clienteConectado)
    {
        this.clienteConectado = clienteConectado;
    }

    /**
     * Método que implementa el comportamiento del hilo
     */
    @Override
    public void run()
    {
        try
        {
            NordBoxCAD boxCAD = new NordBoxCAD();
            Usuario usuario, recepcionUsuario;
            EjercicioBenchUsuario recepcionEjercicioBenchUsuario;
            EjerciciosBench recepcionEjerciciosBench;
            int varInt;

            DataInputStream recepcionData = new DataInputStream(clienteConectado.getInputStream());
            ObjectInputStream recepcionObject = new ObjectInputStream(clienteConectado.getInputStream());

            ObjectOutputStream envioObject = new ObjectOutputStream(clienteConectado.getOutputStream());
            DataOutputStream envioData = new DataOutputStream(clienteConectado.getOutputStream());

            //Capturo los datos que envie el cliente y los separo, para poder interactuar con ellos.
            String capturaDatos = recepcionData.readUTF();

            switch (capturaDatos)
            {
                case "comprobarLogin":
                    System.out.println("Verificacion del login");
                    recepcionUsuario = (Usuario) recepcionObject.readObject();
                    usuario = boxCAD.comprobarLogin(recepcionUsuario.getCorreo(), recepcionUsuario.getPassword());
                    envioObject.writeObject(usuario);
                    break;

                case "insertarEjerciciosBench":
                    System.out.println("insertarEjerciciosBench");
                    recepcionEjerciciosBench = (EjerciciosBench) recepcionObject.readObject();
                    varInt = boxCAD.insertarEjerciciosBench(recepcionEjerciciosBench.getNombre(), recepcionEjerciciosBench.getDificultad());
                    envioObject.writeObject(varInt);
                    break;

                case "crearUsuario":
                    System.out.println("crearUsuario");
                    recepcionUsuario = (Usuario) recepcionObject.readObject();
                    varInt = boxCAD.crearUsuario(recepcionUsuario);
                    envioObject.writeObject(varInt);
                    break;
            }

            System.out.println("Finalizacion del socket");

            envioObject.close();
            recepcionData.close();

        } catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        } catch (ExcepcionNordBox ex)
        {
            Logger.getLogger(SesionServidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(SesionServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
