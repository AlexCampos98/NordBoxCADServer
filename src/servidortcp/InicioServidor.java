/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidortcp;

/**
 *
 * @author Alejandro Campos Maestre
 */
public class InicioServidor
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Thread servidor = new ServidorTCP();
        servidor.start();
    }
    
}
