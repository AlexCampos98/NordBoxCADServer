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
public class ApuntoEjercicio implements Serializable
{
    Integer idUsuario, idEjercicio;
    String fecha;
    Integer peso, nRondas;

    public ApuntoEjercicio()
    {
    }

    public ApuntoEjercicio(Integer idEjercicio, Integer idUsuario, String fecha, Integer peso, Integer nRondas)
    {
        this.idUsuario = idUsuario;
        this.idEjercicio = idEjercicio;
        this.fecha = fecha;
        this.peso = peso;
        this.nRondas = nRondas;
    }

    public Integer getIdUsuario()
    {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario)
    {
        this.idUsuario = idUsuario;
    }

    public Integer getIdEjercicio()
    {
        return idEjercicio;
    }

    public void setIdEjercicio(Integer idEjercicio)
    {
        this.idEjercicio = idEjercicio;
    }

    public String getFecha()
    {
        return fecha;
    }

    public void setFecha(String fecha)
    {
        this.fecha = fecha;
    }

    public Integer getPeso()
    {
        return peso;
    }

    public void setPeso(Integer peso)
    {
        this.peso = peso;
    }

    public Integer getnRondas()
    {
        return nRondas;
    }

    public void setnRondas(Integer nRondas)
    {
        this.nRondas = nRondas;
    }

    @Override
    public String toString()
    {
        return "ApuntoEjercicio{" + "idUsuario=" + idUsuario + ", idEjercicio=" + idEjercicio + ", fecha=" + fecha + ", peso=" + peso + ", nRondas=" + nRondas + '}';
    }
}
