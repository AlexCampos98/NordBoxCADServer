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
            Usuario usuario;

            InputStream inputStream = clienteConectado.getInputStream();
            DataInputStream recepcion = new DataInputStream(inputStream);

            ObjectOutputStream envio = new ObjectOutputStream(clienteConectado.getOutputStream());

            //Capturo los datos que envie el cliente y los separo, para poder interactuar con ellos.
            String capturaDatos = recepcion.readUTF();
            String[] datosSeparados = capturaDatos.split("-");

            //El primer caracter lo comparo para saber que funcion hara.
            switch (datosSeparados[0])
            {
                //Busqueda del usuario por el correo.
                case "1":
                    usuario = boxCAD.buscarUsuarioCorreo(datosSeparados[1]);
                    envio.writeObject(usuario);
                    break;

                //Busqueda del usuario por el id.
                case "2":
                    usuario = boxCAD.buscarUsuarioID(Integer.parseInt(datosSeparados[1]));
                    envio.writeObject(usuario);
                    break;

                //Verificacion del login
                case "3":
                    usuario = boxCAD.comprobarLogin(datosSeparados[1], datosSeparados[2]);
                    envio.writeObject(usuario);
                    break;
                    
                //Creacion de un ejercicio de benchmark.
                case "4":
                    boxCAD.crearEjeBench(Integer.parseInt(datosSeparados[1]), Integer.parseInt(datosSeparados[2]), Integer.parseInt(datosSeparados[3]));
                    //TODO enviar algun verificador y añadirlo al CAD.
                    break;
                    
                //Envio de datos de todos los ejercicios bench del usuario.
                case "5":
                    ArrayList<EjercicioBenchUsuario> arrayList = boxCAD.ejeBenchUsuario(Integer.parseInt(datosSeparados[1]), Integer.parseInt(datosSeparados[2]));
                    envio.writeObject(arrayList);
                    break;
            }

            envio.close();
            recepcion.close();

        } catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        } catch (ExcepcionNordBox ex)
        {
            Logger.getLogger(SesionServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
