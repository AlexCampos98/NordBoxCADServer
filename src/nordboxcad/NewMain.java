/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nordboxcad;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alejandro Campos Maestre
 */
public class NewMain
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {

        try
        {
            NordBoxCAD aD = new NordBoxCAD();

            aD.insertarEjerciciosBench("algo", 1);
            Usuario usuario = aD.comprobarLogin("alex_-campos@hotmail.com", "algo");
            System.out.println(usuario.toString());
            ArrayList<EjercicioBenchUsuario> al = aD.ejeBenchUsuario(1, 4);
            Iterator<EjercicioBenchUsuario> iterable = al.iterator();
            while (iterable.hasNext())
            {
                System.out.println(iterable.next().toString());
            }

        } catch (ExcepcionNordBox ex)
        {
            Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
