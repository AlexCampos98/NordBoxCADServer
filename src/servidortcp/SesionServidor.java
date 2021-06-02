/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidortcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import nordboxcad.EjercicioBenchUsuario;
import nordboxcad.EjerciciosBench;
import nordboxcad.Evento;
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
            Evento recepcionEvento, envioEvento, evento;
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
                    System.out.println("Antes de capturar");
                    if(recepcionUsuario.getImg() != null)
                        capturarArchivo(recepcionUsuario);
                    System.out.println("Recibida la imagen");

                    envioObject.writeObject(varInt);

                    break;
                    
                case "modificarUsuarioPass":
                    recepcionObject = new ObjectInputStream(clienteConectado.getInputStream());
                    envioObject = new ObjectOutputStream(clienteConectado.getOutputStream());

                    System.out.println(date.toString() + " - modificarUsuarioPass");
                    recepcionUsuario = (Usuario) recepcionObject.readObject();
                    varInt = boxCAD.modificarUsuarioPass(recepcionUsuario);
                    System.out.println("Antes de capturar");
                    if(recepcionUsuario.getImg() != null)
                        capturarArchivo(recepcionUsuario);
                    System.out.println("Recibida la imagen");

                    envioObject.writeObject(varInt);

                    break;
                
                case "buscarUsuarioID":
                    recepcionObject = new ObjectInputStream(clienteConectado.getInputStream());
                    envioObject = new ObjectOutputStream(clienteConectado.getOutputStream());

                    System.out.println(date.toString() + " - buscarUsuarioID");
                    recepcionUsuario = (Usuario) recepcionObject.readObject();
                    usuario = boxCAD.buscarUsuarioID(recepcionUsuario.getId());
                    envioObject.writeObject(usuario);
                    
                    break;
                    
                case "crearEvento":
                    recepcionObject = new ObjectInputStream(clienteConectado.getInputStream());

                    System.out.println(date.toString() + " - crearEvento");
                    recepcionEvento = (Evento) recepcionObject.readObject();
                    boxCAD.crearEvento(recepcionEvento);
                    
                    break;
                    
                case "ejeBenchUsuario":
                    recepcionObject = new ObjectInputStream(clienteConectado.getInputStream());
                    envioObject = new ObjectOutputStream(clienteConectado.getOutputStream());

                    System.out.println(date.toString() + " - ejeBenchUsuario");
                    envioObject.writeObject(boxCAD.ejeBenchUsuario((EjercicioBenchUsuario) recepcionObject.readObject()));
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

    private int capturarArchivo(Usuario usuario)
    {
        System.out.println("Dentro de capturar");
        BufferedInputStream bis;
        BufferedOutputStream bos;

        byte[] receivedData;
        int in;

        try
        {
            //Buffer de 1024 bytes
            receivedData = new byte[1024];
            bis = new BufferedInputStream(clienteConectado.getInputStream());

            //Para guardar fichero recibido
            bos = new BufferedOutputStream(new FileOutputStream("C:/xampp/htdocs/imgPerfil/" + usuario.getId() + ".jpg"));
            while ((in = bis.read(receivedData)) > 1023)
            {
                bos.write(receivedData, 0, in);
                bos.flush();
            }
            System.out.println("Termina recpcion imagen");
            return 1;

        } catch (Exception e)
        {
            System.err.println(e);
            return 0;
        }
    }
    
//    private void enviarArchivo(Usuario usuario)
//    {        
//        BufferedInputStream bis;
//        BufferedOutputStream bos;
//        int in;
//        byte[] byteArray;
//        //Fichero a transferir
//        final String filename = usuario.getImg().getAbsolutePath(); 
//        
//        try
//        {
//            final File localFile = new File(filename);
//            
//            bis = new BufferedInputStream(new FileInputStream(localFile));
//            bos = new BufferedOutputStream(clienteConectado.getOutputStream());
//            
//            //Enviamos el fichero
//            byteArray = new byte[8192];
//            while ((in = bis.read(byteArray)) > 0)
//            {
//                bos.write(byteArray, 0, in);
//                bos.flush();
//            }
//            System.out.println("Salida de enviar foto");
//
//        } catch (Exception e)
//        {
//            System.out.println(e);
//        }
//    }
}
