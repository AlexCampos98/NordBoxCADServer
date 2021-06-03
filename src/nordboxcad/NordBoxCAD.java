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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
        String dml = "INSERT INTO crear_usuario (email, password, fecha, id_administrador, isregistrado) "
                + "VALUES (?,?,?,3,0)";
        int resultado = 0;
        try
        {
            PreparedStatement preparedStatement = conexion.prepareStatement(dml);
            preparedStatement.setString(1, usuario.getCorreo());
            
            // Los caracteres de interés en un array de char.
            char [] chars = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

            // Longitud del array de char.
            int charsLength = chars.length;

            // Instanciamos la clase Random
            Random random = new Random();

            // Un StringBuffer para componer la cadena aleatoria de forma eficiente
            StringBuffer buffer = new StringBuffer();

            // Bucle para elegir una cadena de 10 caracteres al azar
            for (int i=0;i<10;i++){

               // Añadimos al buffer un caracter al azar del array
               buffer.append(chars[random.nextInt(charsLength)]);
            }
            
            enviarCorreo(usuario.getCorreo(), buffer.toString());

            String passwordHash = generateStorngPasswordHash(buffer.toString());

            preparedStatement.setString(2, passwordHash);
            
            Calendar fecha = new GregorianCalendar();
            String fechaActual = fecha.get(Calendar.YEAR) + "-" + fecha.get(Calendar.MONTH) + "-" + fecha.get(Calendar.DATE);
            preparedStatement.setString(3, fechaActual);

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
        String dqlNuevoUser = "SELECT * FROM crear_usuario WHERE email=?";
        String dql = "SELECT * FROM usuario WHERE correo=?";
        Usuario usuario = new Usuario();

        try
        {
            PreparedStatement preparedStatementNuevoUser = conexion.prepareStatement(dqlNuevoUser);
            preparedStatementNuevoUser.setString(1, correo);
            ResultSet resultSetNuevoUser = preparedStatementNuevoUser.executeQuery();

            PreparedStatement preparedStatement = conexion.prepareStatement(dql);
            preparedStatement.setString(1, correo);

            if (resultSetNuevoUser.next())
            {
                if (validatePassword(password, resultSetNuevoUser.getString("password")))
                    {
                        usuario.setCorreo(resultSetNuevoUser.getString("correo"));
                        usuario.setId(0);
                    } else
                    {
                        System.out.println("Contraseña o correo incorrecto");
                    }
            } else
            {
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
     * @param apuntoEjercicio
     * @throws nordboxcad.ExcepcionNordBox
     */
    public void crearEjeBench(ApuntoEjercicio apuntoEjercicio) throws ExcepcionNordBox
    {
        conectar();
        String dml = "INSERT INTO apunto_ejercicio (id_ejercicio_bench, id_usuario, fecha, peso, n_rondas) VALUES (?,?,?,?,?)";

        try
        {
            PreparedStatement preparedStatement = conexion.prepareStatement(dml);
            preparedStatement.setObject(1, apuntoEjercicio.getIdEjercicio(), Types.INTEGER);
            preparedStatement.setObject(2, apuntoEjercicio.getIdUsuario(), Types.INTEGER);
            preparedStatement.setString(3, apuntoEjercicio.getFecha());
            preparedStatement.setObject(4, apuntoEjercicio.getPeso(), Types.INTEGER);
            preparedStatement.setObject(5, apuntoEjercicio.getnRondas(), Types.INTEGER);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            conexion.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<EjercicioBenchUsuario> ejeBenchUsuario(EjercicioBenchUsuario benchUsuario) throws ExcepcionNordBox
    {
        conectar();
        String dql = "SELECT * FROM apunto_ejercicio WHERE id_ejercicio_bench=? AND id_usuario=? ORDER BY fecha DESC";
        ArrayList<EjercicioBenchUsuario> arrayList = new ArrayList<>();

        try
        {
            PreparedStatement preparedStatement = conexion.prepareStatement(dql);
            preparedStatement.setObject(1, benchUsuario.getId_ejeBench(), Types.INTEGER);
            preparedStatement.setObject(2, benchUsuario.getId_usu(), Types.INTEGER);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                EjercicioBenchUsuario ejercicioBenchUsuario = new EjercicioBenchUsuario();
                ejercicioBenchUsuario.setId(resultSet.getInt("id_apunte"));
                ejercicioBenchUsuario.setId_ejeBench(resultSet.getInt("id_ejercicio_bench"));
                ejercicioBenchUsuario.setId_usu(resultSet.getInt("id_usuario"));
                ejercicioBenchUsuario.setFecha(resultSet.getDate("fecha"));
                ejercicioBenchUsuario.setPeso(resultSet.getInt("peso"));
                ejercicioBenchUsuario.setRondas(resultSet.getInt("n_rondas"));

                arrayList.add(ejercicioBenchUsuario);
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrayList;
    }

    public void crearEvento(Evento evento) throws ExcepcionNordBox
    {
        conectar();
        String dml = "INSERT INTO evento (fecha, hora, nombre, n_plazas, color, id_entrenador) VALUES (?,?,?,?,?,?)";

        try
        {
            PreparedStatement preparedStatement = conexion.prepareStatement(dml);
            preparedStatement.setString(1, evento.getFecha());
            preparedStatement.setString(2, evento.getHora());
            preparedStatement.setString(3, evento.getNombre());
            preparedStatement.setObject(4, evento.getnPlazas(), Types.INTEGER);
            preparedStatement.setString(5, evento.getColor());
            preparedStatement.setObject(6, evento.getIdEntrenador(), Types.INTEGER);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            conexion.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(NordBoxCAD.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private void enviarCorreo(String correo, String password)
    {
        String asunto="Bienvenido a NordBox Fitness Santoña";
        String cuerpo="Datos de acceso\n" +
            "Usuario: " + correo + "\n" +
            "\n" +
            "Contraseña: " + password;
        
        System.out.println("Estableciendo las propiedades ...");
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");

        System.out.println("Configurando el autenticador ...");
        Authenticator autenticador = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("NordBoxApp@gmail.com", "Abc123.kk");
            }
        };

        System.out.println("Estableciendo una conexión con el servidor SMTP ...");
        Session sesion = Session.getInstance(propiedades, autenticador);

        try {
            System.out.println("Creando el mensaje ...");
            Message mensaje = new MimeMessage(sesion);
            InternetAddress iaDe = new InternetAddress("NordBoxApp@gmail.com");
            mensaje.setFrom(iaDe);
            InternetAddress[] iaA = InternetAddress.parse(correo);
            mensaje.setRecipients(Message.RecipientType.TO, iaA);
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);

            System.out.println("Enviando el mensaje ...");
            Transport.send(mensaje);
            System.out.println("Correo electrónico enviado");

        } catch (MessagingException e) {
            System.out.println("Fallo en el envío del correo electrónico. Fallo que se ha producido: " + e.getMessage());
        }
    }
}
