package criptografiaSources;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Esta clase es la encargada de contener los métodos de seguridad de
 * la aplicación, recibe llamadas desde varios puntos de la aplicación
 * asegurando la seguridad de la contraseña del usuario.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class CifradoMD5 {

    /**
     * Método encargado de cifrar un String en este caso la contraseña
     * del usuario que es cifrada mediante el algoritmo MD5.
     *
     * @param password Contraseña sin encriptar del usuario.
     * @return Devuelve la contraseña encriptada en bytes para su manipulación.
     */
    public static byte[] cifrarContraseña(String password){
        byte[] contraseñaIntroducida;
        try {
            contraseñaIntroducida = MessageDigest.getInstance("MD5").digest(password.getBytes());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al cifrar la contraseña",e);
        }
        return contraseñaIntroducida;
    }

    /**
     * Encargada de convertir el String recibido de la base de datos
     * en Base64.
     *
     * @param password Algoritmo en String.
     * @return Algoritmo convertido a Byte64.
     */
    public static byte[] convertirContraseña(String password){
        return Base64.getDecoder().decode(password);
    }
}
