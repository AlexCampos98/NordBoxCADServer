/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nordboxcad;

/**
 *
 * @author Alejandro Campos Maestre
 */
public class ExcepcionNordBox extends Exception
{
    String mensajeErrorUsuario, mensajeErrorAdministrador, sentenciaSQL;
    Integer codigoError;

    /**
     * Constructor vacio de la clase
     */
    public ExcepcionNordBox()
    {
    }

    /**
     * Constructor completo de la clase
     * @param mensajeErrorUsuario Mensaje a enviar al usuario
     * @param mensajeErrorAdministrador Mensaje a enviar al administrador de la aplicacion
     * @param sentenciaSQL Sentencia SQL que inicio el error
     * @param codigoError  Codigo de error que devuelve la BD.
     */
    public ExcepcionNordBox(String mensajeErrorUsuario, String mensajeErrorAdministrador, String sentenciaSQL, Integer codigoError)
    {
        this.mensajeErrorUsuario = mensajeErrorUsuario;
        this.mensajeErrorAdministrador = mensajeErrorAdministrador;
        this.sentenciaSQL = sentenciaSQL;
        this.codigoError = codigoError;
    }

    /**
     * Obtencion del mensaje de informacion del error al usuario
     * @return Devuelve un String con la informacion para el usuario
     */
    public String getMensajeErrorUsuario()
    {
        return mensajeErrorUsuario;
    }

    /**
     * Asignacion de mensaje de error para el usuario
     * @param mensajeErrorUsuario Mensaje de error para el usuario
     */
    public void setMensajeErrorUsuario(String mensajeErrorUsuario)
    {
        this.mensajeErrorUsuario = mensajeErrorUsuario;
    }

    /**
     * Obtencion del mensaje de informacion del error para el administrador del sistema
     * @return Devuelve un String con la informacion para el administrador del sistema 
     */
    public String getMensajeErrorAdministrador()
    {
        return mensajeErrorAdministrador;
    }

    /**
     * Asignacion de mensaje de error para el administrador del sistema
     * @param mensajeErrorAdministrador Mensaje de error para el administrador del sistema
     */
    public void setMensajeErrorAdministrador(String mensajeErrorAdministrador)
    {
        this.mensajeErrorAdministrador = mensajeErrorAdministrador;
    }

    /**
     * Obtencion del mensaje de informacion del error cuya sentencia SQL ha provocado el error
     * @return Devuelve un String con la sentencia SQL que ha provocado el error
     */
    public String getSentenciaSQL()
    {
        return sentenciaSQL;
    }

    /**
     * Asignacion de mensaje con la sentencia SQL que provoco la excepcion
     * @param sentenciaSQL Sentencia SQL que provoco la excepcion
     */
    public void setSentenciaSQL(String sentenciaSQL)
    {
        this.sentenciaSQL = sentenciaSQL;
    }

    /**
     * Obtencion de la sentencia SQL que provoco la excepcion
     * @return Devuelve un Integer con el codigo de error proporcionado por la BD
     */
    public Integer getCodigoError()
    {
        return codigoError;
    }

    /**
     * Asignacion del codigo de error SQL
     * @param codigoError Integer con el numero de error de SQL
     */
    public void setCodigoError(Integer codigoError)
    {
        this.codigoError = codigoError;
    }

    /**
     * Metodo usado para obtener todos los datos contenidos en la clase.
     * @return Devuelve un String con la informacion contenida de todos los atributos.
     */
    @Override
    public String toString()
    {
        return "excepcionUsuario{" + "mensajeErrorUsuario=" + mensajeErrorUsuario + ", mensajeErrorAdministrador=" + mensajeErrorAdministrador + ", sentenciaSQL=" + sentenciaSQL + ", codigoError=" + codigoError + '}';
    }
}
