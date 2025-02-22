package criptografiaSources;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;

/**
 * Clase en la que se almacena la gestión de cifrado asimétrico.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class ClaveAsimetrica {
    /**
     * Método encargado de encriptar mediante el uso de la clave asimétrica un String.
     *
     * @param mensajeUsuario Mensaje introducido para su cifrado.
     */
    public static void encriptarConClaveAsimetrica(String mensajeUsuario) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./files/keys/firmaDigital/key/clavePublicaSete.key"));
             ObjectOutputStream oosCifrado = new ObjectOutputStream(new FileOutputStream("./files/mensajeDavidP.bin"))) {
            PublicKey pk = (PublicKey) ois.readObject();
                Cipher cipher = Cipher.getInstance(pk.getAlgorithm());
                    cipher.init(Cipher.ENCRYPT_MODE, pk);
                        byte[] cifrado = cipher.doFinal(mensajeUsuario.getBytes());
                            oosCifrado.writeObject(cifrado);
        } catch (Exception e) {
            System.err.println("Error compruebe las rutas de los archivos y vuelva a intentarlo.");
        }
    }
}
