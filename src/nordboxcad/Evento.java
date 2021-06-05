/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nordboxcad;

import java.io.Serializable;

/**
 *
 * @author Alejandro Campos Maestre
 */
public class Evento implements Serializable
{
    Integer idEvento;
    String fecha, hora, nombre;
    Integer nPlazas;
    String color;
    Integer idEntrenador;
    ListEventoApuntados apuntados;

    public Evento()
    {
    }

    public Evento(Integer idEvento, String fecha, String hora, String nombre, Integer nPlazas, String color, Integer idEntrenador)
    {
        this.idEvento = idEvento;
        this.fecha = fecha;
        this.hora = hora;
        this.nombre = nombre;
        this.nPlazas = nPlazas;
        this.color = color;
        this.idEntrenador = idEntrenador;
    }

    public Integer getIdEvento()
    {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento)
    {
        this.idEvento = idEvento;
    }

    public String getFecha()
    {
        return fecha;
    }

    public void setFecha(String fecha)
    {
        this.fecha = fecha;
    }

    public String getHora()
    {
        return hora;
    }

    public void setHora(String hora)
    {
        this.hora = hora;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public Integer getnPlazas()
    {
        return nPlazas;
    }

    public void setnPlazas(Integer nPlazas)
    {
        this.nPlazas = nPlazas;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public Integer getIdEntrenador()
    {
        return idEntrenador;
    }

    public void setIdEntrenador(Integer idEntrenador)
    {
        this.idEntrenador = idEntrenador;
    }

    public ListEventoApuntados getApuntados()
    {
        return apuntados;
    }

    public void setApuntados(ListEventoApuntados apuntados)
    {
        this.apuntados = apuntados;
    }

    @Override
    public String toString()
    {
        return "Evento{" + "idEvento=" + idEvento + ", fecha=" + fecha + ", hora=" + hora + ", nombre=" + nombre + ", nPlazas=" + nPlazas + ", color=" + color + ", idEntrenador=" + idEntrenador + ", apuntados=" + apuntados.toString() + '}';
    }
    
}
