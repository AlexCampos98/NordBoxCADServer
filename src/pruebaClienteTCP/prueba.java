/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebaClienteTCP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nordboxcad.ExcepcionNordBox;
import nordboxcad.NordBoxCAD;
import nordboxcad.Usuario;

/**
 *
 * @author Alejandro Campos Maestre
 */
public class prueba
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        File origen = new File("C:\\Users\\alex_\\Desktop\\Avatares\\prueba1.png");
        File destino = new File("imgPerfil/" + origen.getName());
        System.out.println(destino.getPath());
        
        guardarImgPerfil(origen, destino);

//        Usuario usuario = new Usuario(3, "aa@aa.aa", null, "nombre", "pApellido", "sApellido", "telefono", "telefEm", 134, "localidad", "provincia", null);
//        try
//        {
//            NordBoxCAD boxCAD = new NordBoxCAD();
//            boxCAD.modificarUsuarioNoPass(usuario);
//        } catch (ExcepcionNordBox ex)
//        {
//            System.out.println(ex);
//        }
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
