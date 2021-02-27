/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidortcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
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

            Date date = new Date();

            DataInputStream recepcionData = new DataInputStream(clienteConectado.getInputStream());
            ObjectInputStream recepcionObject = null;

            ObjectOutputStream envioObject = null;
            DataOutputStream envioData = null;

            System.out.println("Captura de dato");

            //Capturo los datos que envie el cliente y los separo, para poder interactuar con ellos.
            String capturaDatos = recepcionData.readUTF();

            switch (capturaDatos)
            {
                case "comprobarLogin":
                    recepcionObject = new ObjectInputStream(clienteConectado.getInputStream());
                    envioObject = new ObjectOutputStream(clienteConectado.getOutputStream());
                    
                    System.out.println(date.toString() + " - Verificacion del login");
                    recepcionUsuario = (Usuario) recepcionObject.readObject();
                    usuario = boxCAD.comprobarLogin(recepcionUsuario.getCorreo(), recepcionUsuario.getPassword());
                    envioObject.writeObject(usuario);
                    break;

                case "insertarEjerciciosBench":
                    recepcionObject = new ObjectInputStream(clienteConectado.getInputStream());
                    envioObject = new ObjectOutputStream(clienteConectado.getOutputStream());
                    
                    System.out.println(date.toString() + " - insertarEjerciciosBench");
                    recepcionEjerciciosBench = (EjerciciosBench) recepcionObject.readObject();
                    varInt = boxCAD.insertarEjerciciosBench(recepcionEjerciciosBench.getNombre(), recepcionEjerciciosBench.getDificultad());
                    envioObject.writeObject(varInt);
                    break;

                case "crearUsuario":
                    recepcionObject = new ObjectInputStream(clienteConectado.getInputStream());
                    envioObject = new ObjectOutputStream(clienteConectado.getOutputStream());
                    
                    System.out.println(date.toString() + " - crearUsuario");
                    recepcionUsuario = (Usuario) recepcionObject.readObject();
                    varInt = boxCAD.crearUsuario(recepcionUsuario);
                    envioObject.writeObject(varInt);
                    break;

                case "ejeBench":
                    envioObject = new ObjectOutputStream(clienteConectado.getOutputStream());
                    
                    System.out.println(date.toString() + " - ejeBench");
                    envioObject.writeObject(boxCAD.ejeBench());
                    break;

                case "modificarUsuarioNoPass":
                    recepcionObject = new ObjectInputStream(clienteConectado.getInputStream());
                    envioObject = new ObjectOutputStream(clienteConectado.getOutputStream());
                    
                    System.out.println(date.toString() + " - modificarUsuarioNoPass");
                    recepcionUsuario = (Usuario) recepcionObject.readObject();
                    varInt = boxCAD.modificarUsuarioNoPass(recepcionUsuario);

                    if (varInt > 0 && recepcionUsuario.getImg() != null)
                    {
                        guardarImgPerfil(recepcionUsuario.getImg(), new File("imgPerfil/" + recepcionUsuario.getImg().getName()));
                    }
                    envioObject.writeObject(varInt);

                    break;
            }

            System.out.println("Finalizacion del socket");

            if (envioData != null)
            {
                envioData.close();
            }
            if (recepcionObject != null)
            {
                recepcionObject.close();
            }
            if (envioObject != null)
            {
                envioObject.close();
            }
            if (recepcionData != null)
            {
                recepcionData.close();
            }

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

    public static void guardarImgPerfil(File original, File copia)
    {
        FileInputStream archivoOriginal = null;
        FileOutputStream archivoCopia = null;
        if ((original != null) && (copia != null))
        {
            try
            {
                copia.createNewFile();
                archivoOriginal = new FileInputStream(original);
                archivoCopia = new FileOutputStream(copia);
                //lectura por segmentos de 0.5MB
                byte buffer[] = new byte[512 * 1024];
                int nbLectura;
                while ((nbLectura = archivoOriginal.read(buffer)) != -1)
                {
                    archivoCopia.write(buffer, 0, nbLectura);
                }
            } catch (FileNotFoundException fnf)
            {
                System.out.println(fnf);
            } catch (IOException io)
            {
                System.out.println(io);
            } finally
            {
                try
                {
                    archivoOriginal.close();
                } catch (Exception e)
                {
                    System.out.println(e);
                }
                try
                {
                    archivoCopia.close();
                } catch (Exception e)
                {
                    System.out.println(e);
                }
            }
        }
    }
}
