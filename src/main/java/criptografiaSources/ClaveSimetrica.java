package criptografiaSources;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.*;

/**
 * Clase en la que se almacena la gestión del cifrado simétrico.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class ClaveSimetrica {

    /**
     * Este método es el encargado de generar una clave simétrica mediante el algoritmo
     *  AES, con la que posteriormente se realizaran acciones.
     */
    public static void generarClaveSimetrica() {
        String algoritmo = "AES";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./files/keys/simetrica/claveSimetrica.key"))) {
            KeyGenerator keygen = KeyGenerator.getInstance(algoritmo);
                SecretKey key = keygen.generateKey();
                    oos.writeObject(key);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algoritmo invalido.");
        } catch (FileNotFoundException e) {
            System.err.println("Error, archivo no encontrado.");
        } catch (IOException e) {
            System.err.println("Error de entrada/salida.");
        }
    }

    /**
     * Este método es el encargado de encriptar un mensaje mediante el uso de una clave
     * simétrica con algoritmo AES.
     *
     * @param mensajeUsuario Mensaje introducido por el usuario para su posterior cifrado.
     */
    public static void encriptarConClaveSimetrica(String mensajeUsuario){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./files/keys/simetrica/claveSimetrica.key"));
             ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./files/keys/simetrica/bin/mensajeCifrado.bin"))){
            SecretKey secretKey = (SecretKey) ois.readObject();
                Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
                    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                        byte[] cifrado = cipher.doFinal(mensajeUsuario.getBytes());
                            oos.writeObject(cifrado);
        } catch (Exception e) {
            System.err.println("Error compruebe las rutas de los archivos y vuelva a intentarlo.");
        }
    }

    /**
     * Este método es el encargado de desencriptar un mensaje mediante el uso de una clave
     * simétrica con algoritmo AES una vez desencriptado lo muestra.
     */
    public static void desencriptarConClaveSimetrica(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./files/keys/simetrica/bin/mensajeCifrado.bin"));
             ObjectInputStream oisKey = new ObjectInputStream(new FileInputStream("./files/keys/simetrica/claveSimetrica.key"))) {
            byte[] cifrado = (byte[]) ois.readObject();
                SecretKey key = (SecretKey) oisKey.readObject();
                    Cipher cipher = Cipher.getInstance(key.getAlgorithm());
                        cipher.init(Cipher.DECRYPT_MODE, key);
                            byte[] descifrado = cipher.doFinal(cifrado);
                                System.out.println("Mensaje: "+new String(descifrado)+".");
        } catch (Exception e) {
            System.err.println("Algo ha fallado, la clave puede ser incorrecta.");
        }
    }

}
