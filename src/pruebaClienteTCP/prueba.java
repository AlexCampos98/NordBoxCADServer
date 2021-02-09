/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebaClienteTCP;

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
        Thread cliente = new preubaTCP();
        cliente.start();
//        String a = "algo-azucar";
//        String[] b = a.split("-");
//        
//        System.out.println(b[0]);
//        System.out.println(b[1]);
    }
    
}
