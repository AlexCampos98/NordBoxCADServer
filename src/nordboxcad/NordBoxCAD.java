/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nordboxcad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Alejandro Campos Maestre
 */
public class NordBoxCAD
{

    Connection conexion;
    static String IP = null, usuarioBD = null, contraseñaBD = null;

    public NordBoxCAD() throws ExcepcionNordBox
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException ex)
        {
            ExcepcionNordBox e = new ExcepcionNordBox();
            e.setMensajeErrorAdministrador(ex.getMessage());
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
            throw e;
        } catch (InstantiationException | IllegalAccessException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public NordBoxCAD(String IP, String nombreBD, String contraseñaBD) throws ExcepcionNordBox
    {
        try
        {
            NordBoxCAD.IP = IP;
            NordBoxCAD.usuarioBD = nombreBD;
            NordBoxCAD.contraseñaBD = contraseñaBD;

            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex)
        {
            ExcepcionNordBox e = new ExcepcionNordBox();
            e.setMensajeErrorAdministrador(ex.getMessage());
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
            throw e;
        }
    }

    private void conectar() throws ExcepcionNordBox
    {
        try
        {
            if (IP == null)
            {
                //jdbc:mysql://localhost/incidencias?user=root&password=root
                conexion = DriverManager.getConnection("jdbc:mysql://localhost/nordboxbd?user=root&password=");
            } else
            {
                conexion = DriverManager.getConnection("jdbc:mysql://" + IP + "/nordboxbd?user=" + usuarioBD + "&password=" + contraseñaBD);
            }
        } catch (SQLException ex)
        {
            ExcepcionNordBox e = new ExcepcionNordBox();
            e.setMensajeErrorAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
            throw e;
        }
    }

    public int crearUsuario(Usuario usuario) throws ExcepcionNordBox
    {
        conectar();
        String dml = "INSERT INTO usuario (correo, password, nombre, pApellido, sApellido, telefono, telefonoEmergencia, codigoPostal, localidad, provincia) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?)";
        int resultado = 0;
        try
        {
            PreparedStatement preparedStatement = conexion.prepareStatement(dml);
            preparedStatement.setString(1, usuario.getCorreo());

            String passwordHash = generateStorngPasswordHash(usuario.getPassword());

            preparedStatement.setString(2, passwordHash);
            preparedStatement.setString(3, usuario.getNombre());
            preparedStatement.setString(4, usuario.getpApellido());
            preparedStatement.setString(5, usuario.getsApellido());
            preparedStatement.setString(6, usuario.getTelefono());
            preparedStatement.setString(7, usuario.getTelefonoEmergencia());
            preparedStatement.setObject(8, usuario.getCodigoPostal(), Types.INTEGER);
            preparedStatement.setString(9, usuario.getLocalidad());
            preparedStatement.setString(10, usuario.getProvincia());

            resultado = preparedStatement.executeUpdate();

            preparedStatement.close();
            conexion.close();
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;
    }

    public int insertarEjerciciosBench(String nombre, Integer dificultad) throws ExcepcionNordBox
    {
        conectar();
        String dml = "INSERT INTO ejercicio_bench (nombre, dificultad) VALUES (?,?)";
        int resultado = 0;

        try
        {
            PreparedStatement preparedStatement = conexion.prepareStatement(dml);
            preparedStatement.setString(1, nombre);
            preparedStatement.setInt(2, dificultad);

            resultado = preparedStatement.executeUpdate();

            preparedStatement.close();
            conexion.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;
    }

    public ArrayList<EjerciciosBench> ejeBench() throws ExcepcionNordBox
    {
        conectar();
        ArrayList<EjerciciosBench> arrayList = new ArrayList<>();
        String dql = "SELECT * FROM ejercicio_bench";

        try
        {
            Statement statement = conexion.createStatement();

            ResultSet resultSet = statement.executeQuery(dql);

            while (resultSet.next())
            {
                EjerciciosBench bench = new EjerciciosBench();
                bench.setId(resultSet.getInt("id_ejercicio_bench"));
                bench.setNombre(resultSet.getString("nombre"));
                bench.setDificultad(resultSet.getInt("dificultad"));
                bench.setParteCuerpo(resultSet.getInt("parte_cuerpo"));
                arrayList.add(bench);
            }

        } catch (SQLException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return arrayList;
    }

    /**
     * Busca en la base de datos el correo y comprueba que la contraseña
     * cifrada, concuerde con la que el usuario proporciona.
     *
     * @param correo
     * @param password
     * @return Devuelve un objeto Usuario, en caso de no encontrar ningun
     * usuario la clase sera null
     * @throws ExcepcionNordBox
     */
    public Usuario comprobarLogin(String correo, String password) throws ExcepcionNordBox
    {
        conectar();
        String dql = "SELECT * FROM usuario WHERE correo=?";
        Usuario usuario = new Usuario();

        try
        {
            PreparedStatement preparedStatement = conexion.prepareStatement(dql);
            preparedStatement.setString(1, correo);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                if (validatePassword(password, resultSet.getString("password")))
                {
                    usuario.setPassword(resultSet.getString("password"));
                    usuario.setId(resultSet.getInt("id_usuario"));

                    if (resultSet.getString("img_perfil") == null)
                    {
                        usuario.setImg(null);
                    } else
                    {
                        usuario.setImg(resultSet.getString("img_perfil"));
                    }

                    usuario.setCorreo(resultSet.getString("correo"));
                    usuario.setNombre(resultSet.getString("nombre"));
                    usuario.setpApellido(resultSet.getString("pApellido"));
                    usuario.setsApellido(resultSet.getString("sApellido"));
                    usuario.setTelefono(resultSet.getString("telefono"));
                    usuario.setTelefonoEmergencia(resultSet.getString("telefonoEmergencia"));
                    usuario.setCodigoPostal(resultSet.getInt("codigoPostal"));
                    usuario.setLocalidad(resultSet.getString("localidad"));
                    usuario.setProvincia(resultSet.getString("provincia"));
                } else
                {
                    System.out.println("Contraseña o correo incorrecto");
                }

            }
        } catch (SQLException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return usuario;
    }

    public Usuario buscarUsuarioID(Integer id) throws ExcepcionNordBox
    {
        conectar();
        String dql = "SELECT * FROM usuario WHERE id_usuario=?";
        Usuario usuario = new Usuario();

        try
        {
            PreparedStatement preparedStatement = conexion.prepareStatement(dql);
            preparedStatement.setObject(1, id, Types.INTEGER);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                usuario.setId(resultSet.getInt("id_usuario"));

                if (resultSet.getString("img_perfil") == null)
                {
                    usuario.setImg(null);
                } else
                {
                    usuario.setImg(resultSet.getString("img_perfil"));
                }

                usuario.setCorreo(resultSet.getString("correo"));
                usuario.setPassword(resultSet.getString("password"));
                usuario.setNombre(resultSet.getString("nombre"));
                usuario.setpApellido(resultSet.getString("pApellido"));
                usuario.setsApellido(resultSet.getString("sApellido"));
                usuario.setTelefono(resultSet.getString("telefono"));
                usuario.setTelefonoEmergencia(resultSet.getString("telefonoEmergencia"));
                usuario.setCodigoPostal(resultSet.getInt("codigoPostal"));
                usuario.setLocalidad(resultSet.getString("localidad"));
                usuario.setProvincia(resultSet.getString("provincia"));

                String a = generateStorngPasswordHash(usuario.getPassword());
                System.out.println(a);
            }

            preparedStatement.close();
            conexion.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuario;
    }

    public Usuario buscarUsuarioCorreo(String correo) throws ExcepcionNordBox
    {
        conectar();
        String dql = "SELECT * FROM usuario WHERE correo=?";
        Usuario usuario = new Usuario();

        try
        {
            PreparedStatement preparedStatement = conexion.prepareStatement(dql);
            preparedStatement.setString(1, correo);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                usuario.setId(resultSet.getInt("id_usuario"));

                if (resultSet.getString("img_perfil") == null)
                {
                    usuario.setImg(null);
                } else
                {
                    usuario.setImg(resultSet.getString("img_perfil"));
                }

                usuario.setCorreo(resultSet.getString("correo"));
                usuario.setPassword(resultSet.getString("password"));
                usuario.setNombre(resultSet.getString("nombre"));
                usuario.setpApellido(resultSet.getString("pApellido"));
                usuario.setsApellido(resultSet.getString("sApellido"));
                usuario.setTelefono(resultSet.getString("telefono"));
                usuario.setTelefonoEmergencia(resultSet.getString("telefonoEmergencia"));
                usuario.setCodigoPostal(resultSet.getInt("codigoPostal"));
                usuario.setLocalidad(resultSet.getString("localidad"));
                usuario.setProvincia(resultSet.getString("provincia"));

                String a = generateStorngPasswordHash(usuario.getPassword());
                System.out.println(a);
            }

            preparedStatement.close();
            conexion.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuario;
    }

    public int modificarUsuarioNoPass(Usuario usuario) throws ExcepcionNordBox
    {
        conectar();
        String dml = "UPDATE usuario SET img_perfil=?, correo=?, nombre=?, pApellido=?, sApellido=?, telefono=?, telefonoEmergencia=?, "
                + "codigoPostal=?, localidad=?, provincia=? WHERE id_usuario=?";
        int resultado = 0;

        try
        {
            PreparedStatement preparedStatement = conexion.prepareStatement(dml);

            preparedStatement.setString(1, usuario.getId() + ".jpg");

            preparedStatement.setString(2, usuario.getCorreo());
            preparedStatement.setString(3, usuario.getNombre());
            preparedStatement.setString(4, usuario.getpApellido());
            preparedStatement.setString(5, usuario.getsApellido());
            preparedStatement.setString(6, usuario.getTelefono());
            preparedStatement.setString(7, usuario.getTelefonoEmergencia());
            preparedStatement.setObject(8, usuario.getCodigoPostal(), Types.INTEGER);
            preparedStatement.setString(9, usuario.getLocalidad());
            preparedStatement.setString(10, usuario.getProvincia());
            preparedStatement.setObject(11, usuario.getId(), Types.INTEGER);

            resultado = preparedStatement.executeUpdate();

            preparedStatement.close();
            conexion.close();
        } catch (SQLException ex)
        {
            System.err.println(ex);
        }
        return resultado;
    }

    public int modificarUsuarioPass(Usuario usuario) throws ExcepcionNordBox
    {
        conectar();
        String dml = "UPDATE usuario SET img_perfil=?, correo=?, nombre=?, pApellido=?, sApellido=?, telefono=?, telefonoEmergencia=?, "
                + "codigoPostal=?, localidad=?, provincia=?, password=? WHERE id_usuario=?";
        int resultado = 0;

        try
        {
            PreparedStatement preparedStatement = conexion.prepareStatement(dml);

            preparedStatement.setString(1, usuario.getId() + ".jpg");

            String passwordHash = generateStorngPasswordHash(usuario.getPassword());

            preparedStatement.setString(2, usuario.getCorreo());
            preparedStatement.setString(3, usuario.getNombre());
            preparedStatement.setString(4, usuario.getpApellido());
            preparedStatement.setString(5, usuario.getsApellido());
            preparedStatement.setString(6, usuario.getTelefono());
            preparedStatement.setString(7, usuario.getTelefonoEmergencia());
            preparedStatement.setObject(8, usuario.getCodigoPostal(), Types.INTEGER);
            preparedStatement.setString(9, usuario.getLocalidad());
            preparedStatement.setString(10, usuario.getProvincia());
            preparedStatement.setString(11, passwordHash);
            preparedStatement.setObject(12, usuario.getId(), Types.INTEGER);

            resultado = preparedStatement.executeUpdate();

            preparedStatement.close();
            conexion.close();
        } catch (SQLException ex)
        {
            System.err.println(ex);
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado;
    }

    /**
     * Creacion de un registro en la tabla ejercicioBenchUsuario
     *
     * @param idUsuario
     * @param idEjercicio
     * @param peso
     * @throws nordboxcad.ExcepcionNordBox
     */
    public void crearEjeBench(Integer idUsuario, Integer idEjercicio, Integer peso) throws ExcepcionNordBox
    {
        conectar();
        String dml = "INSERT INTO apunto_ejercicio (id_ejercicio_bench, id_usuario, fecha, peso) VALUES (?,?,SYSDATE(),?)";

        try
        {
            PreparedStatement preparedStatement = conexion.prepareStatement(dml);
            preparedStatement.setObject(1, idEjercicio, Types.INTEGER);
            preparedStatement.setObject(2, idUsuario, Types.INTEGER);
            preparedStatement.setObject(3, peso, Types.INTEGER);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            conexion.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<EjercicioBenchUsuario> ejeBenchUsuario(Integer idUsuario, Integer idEjercicio) throws ExcepcionNordBox
    {
        conectar();
        String dql = "SELECT * FROM apunto_ejercicio WHERE id_ejercicio_bench=? AND id_usuario=? ORDER BY fecha DESC";
        ArrayList<EjercicioBenchUsuario> arrayList = new ArrayList<>();

        try
        {
            PreparedStatement preparedStatement = conexion.prepareStatement(dql);
            preparedStatement.setObject(1, idEjercicio, Types.INTEGER);
            preparedStatement.setObject(2, idUsuario, Types.INTEGER);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                EjercicioBenchUsuario benchUsuario = new EjercicioBenchUsuario();
                benchUsuario.setId(resultSet.getInt("id_apunte"));
                benchUsuario.setId_ejeBench(resultSet.getInt("id_ejercicio_bench"));
                benchUsuario.setId_usu(resultSet.getInt("id_usuario"));
                benchUsuario.setFecha(resultSet.getDate("fecha"));
                benchUsuario.setPeso(resultSet.getInt("peso"));

                arrayList.add(benchUsuario);
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrayList;
    }

    private static String generateStorngPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0)
        {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else
        {
            return hex;
        }
    }

    private static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++)
        {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
