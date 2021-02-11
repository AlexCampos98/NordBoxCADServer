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
public class EjerciciosBench implements Serializable
{
    Integer id;
    String nombre;
    Integer dificultad, parteCuerpo;

    public EjerciciosBench()
    {
    }

    public EjerciciosBench(Integer id, String nombre, Integer dificultad, Integer parteCuerpo)
    {
        this.id = id;
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.parteCuerpo = parteCuerpo;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public Integer getDificultad()
    {
        return dificultad;
    }

    public void setDificultad(Integer dificultad)
    {
        this.dificultad = dificultad;
    }

    public Integer getParteCuerpo()
    {
        return parteCuerpo;
    }

    public void setParteCuerpo(Integer parteCuerpo)
    {
        this.parteCuerpo = parteCuerpo;
    }

    @Override
    public String toString()
    {
        return "EjerciciosBench{" + "id=" + id + ", nombre=" + nombre + ", dificultad=" + dificultad + ", parteCuerpo=" + parteCuerpo + '}';
    }
}
