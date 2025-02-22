package criptografiaSources;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.security.Signature;

/**
 * Clase en la que se almacena la gestión de firma digital.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class FirmaDigital {

    /**
     * Método encargado de comprobar la autenticación de un fichero, mediante
     * el uso de una firma y clave publica se comprueba la autenticidad del mismo.
     *
     * @param ficheroFirmado El fichero a comprobar.
     */
    public static void comprobarFirmaDigital(String ficheroFirmado) {
        try (ObjectInputStream oisFirma = new ObjectInputStream(new FileInputStream("./files/keys/firmaDigital/bin/firma.bin"));
             ObjectInputStream oisClave = new ObjectInputStream(new FileInputStream("./files/keys/firmaDigital/key/clavePublicaSete.key"))) {
            byte[] firma = (byte[]) oisFirma.readObject();
                PublicKey publicKey = (PublicKey) oisClave.readObject();
                    byte[] datosFirmados = Files.readAllBytes(Paths.get(ficheroFirmado));

            Signature signature = Signature.getInstance("MD5withRSA");
                signature.initVerify(publicKey);
                    signature.update(datosFirmados);
            if (signature.verify(firma)) {
                System.out.println("Firma valida");
            } else {
                System.err.println("Firma NO valida");
            }
        } catch (Exception e) {
            System.err.println("Error compruebe las rutas de los archivos y vuelva a intentarlo.");
        }
    }
}
