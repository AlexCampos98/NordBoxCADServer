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
public class Usuario implements Serializable
{
    Integer id;
    String correo, password, nombre, pApellido, sApellido, telefono, telefonoEmergencia;
    Integer codigoPostal;
    String localidad, provincia;

    public Usuario()
    {
    }

    public Usuario(Integer id, String correo, String password)
    {
        this.id = id;
        this.correo = correo;
        this.password = password;
    }

    public Usuario(Integer id, String correo, String password, String nombre, String pApellido, String sApellido, String telefono, String telefonoEmergencia, Integer codigoPostal, String localidad, String provincia)
    {
        this.id = id;
        this.correo = correo;
        this.password = password;
        this.nombre = nombre;
        this.pApellido = pApellido;
        this.sApellido = sApellido;
        this.telefono = telefono;
        this.telefonoEmergencia = telefonoEmergencia;
        this.codigoPostal = codigoPostal;
        this.localidad = localidad;
        this.provincia = provincia;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCorreo()
    {
        return correo;
    }

    public void setCorreo(String correo)
    {
        this.correo = correo;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getpApellido()
    {
        return pApellido;
    }

    public void setpApellido(String pApellido)
    {
        this.pApellido = pApellido;
    }

    public String getsApellido()
    {
        return sApellido;
    }

    public void setsApellido(String sApellido)
    {
        this.sApellido = sApellido;
    }

    public String getTelefono()
    {
        return telefono;
    }

    public void setTelefono(String telefono)
    {
        this.telefono = telefono;
    }

    public String getTelefonoEmergencia()
    {
        return telefonoEmergencia;
    }

    public void setTelefonoEmergencia(String telefonoEmergencia)
    {
        this.telefonoEmergencia = telefonoEmergencia;
    }

    public Integer getCodigoPostal()
    {
        return codigoPostal;
    }

    public void setCodigoPostal(Integer codigoPostal)
    {
        this.codigoPostal = codigoPostal;
    }

    public String getLocalidad()
    {
        return localidad;
    }

    public void setLocalidad(String localidad)
    {
        this.localidad = localidad;
    }

    public String getProvincia()
    {
        return provincia;
    }

    public void setProvincia(String provincia)
    {
        this.provincia = provincia;
    }

    @Override
    public String toString()
    {
        return "Usuario{" + "id=" + id + ", correo=" + correo + ", password=" + password + ", nombre=" + nombre + ", pApellido=" + pApellido + ", sApellido=" + sApellido + ", telefono=" + telefono + ", telefonoEmergencia=" + telefonoEmergencia + ", codigoPostal=" + codigoPostal + ", localidad=" + localidad + ", provincia=" + provincia + '}';
    }
    
}
