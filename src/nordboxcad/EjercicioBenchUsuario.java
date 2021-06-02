/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nordboxcad;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Alejandro Campos Maestre
 */
public class EjercicioBenchUsuario implements Serializable
{
    Integer id, id_ejeBench, id_usu;
    Date fecha;
    Integer peso, rondas;

    public EjercicioBenchUsuario()
    {
    }

    public EjercicioBenchUsuario(Integer id, Integer id_ejeBench, Integer id_usu, Date fecha, Integer peso, Integer rondas)
    {
        this.id = id;
        this.id_ejeBench = id_ejeBench;
        this.id_usu = id_usu;
        this.fecha = fecha;
        this.peso = peso;
        this.rondas = rondas;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId_ejeBench()
    {
        return id_ejeBench;
    }

    public void setId_ejeBench(Integer id_ejeBench)
    {
        this.id_ejeBench = id_ejeBench;
    }

    public Integer getId_usu()
    {
        return id_usu;
    }

    public void setId_usu(Integer id_usu)
    {
        this.id_usu = id_usu;
    }

    public Date getFecha()
    {
        return fecha;
    }

    public void setFecha(Date fecha)
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

    public Integer getRondas()
    {
        return rondas;
    }

    public void setRondas(Integer rondas)
    {
        this.rondas = rondas;
    }

    @Override
    public String toString()
    {
        return "EjercicioBenchUsuario{" + "id=" + id + ", id_ejeBench=" + id_ejeBench + ", id_usu=" + id_usu + ", fecha=" + fecha + ", peso=" + peso + ", rondas=" + rondas + '}';
    }
}
