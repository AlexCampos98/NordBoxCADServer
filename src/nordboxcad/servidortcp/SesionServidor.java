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
import java.util.logging.Level;
import java.util.logging.Logger;
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
                    Usuario usuario = boxCAD.buscarUsuarioCorreo(datosSeparados[1]);
                    envio.writeObject(usuario);
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
