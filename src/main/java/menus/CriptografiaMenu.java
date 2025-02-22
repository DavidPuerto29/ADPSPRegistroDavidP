package menus;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

import static criptografiaSources.ClaveAsimetrica.encriptarConClaveAsimetrica;
import static criptografiaSources.ClaveSimetrica.*;
import static criptografiaSources.FirmaDigital.comprobarFirmaDigital;

/**
 * Clase encargada de contener el menu y los métodos del apartado de cartografiá,
 * posteriormente es llamado desde el menu principal una vez el usuario ha iniciado
 * sesión correctamente.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class CriptografiaMenu {
    private static final File rutaSimetrica = new File("./files/keys/simetrica/claveSimetrica.key");
    private static final File rutaSimetricaBin = new File("./files/keys/simetrica/bin/mensajeCifrado.bin");
    private static final File rutaClaveSete = new File("./files/keys/firmaDigital/key/clavePublicaSete.key");

    public static void menuCriptografia(){
        boolean loop = true;

        while (loop) {
            System.out.println("""
                         ╔═══════════════════|Menú Criptografía|═════════════════╗  \r
                         ║	                                        			 ║\r
                    	 ║	       1 Crear clave simétrica                       ║\r
                    	 ║	       2 Cifrar mensaje con clave simétrica	         ║\r
                    	 ║	       3 Descifrar mensaje con clave simétrica	     ║\r
                    	 ║	       4 Verificar firma digital			  	     ║\r
                    	 ║	       5 Cifrar mensaje con clave asimétrica         ║\r	
                    	 ║	                                        			 ║\r
                    	 ║	                     0 Salir 			      	   	 ║\r
                         ╚═══════════════════════════════════════════════════════╝\r
                    """);
            Integer op = writeInteger();

            switch (op) {
                case 0:
                    loop = false;
                    break;
                case 1:
                    if(rutaSimetrica.exists()) {
                        System.out.println("Ya existe una clave, ¿Desea reemplazarla?");
                        String confirmation = writeString();
                        if(confirmation.equals("si")){
                            generarClaveSimetrica();
                            System.out.println("Clave creada correctamente.");
                        }else{
                            System.out.println("Clave no sobreescrita, volviendo al menu...");
                        }
                        generarClaveSimetrica();
                    }else{
                        generarClaveSimetrica();
                        System.out.println("Clave creada correctamente.");
                    }
                    break;
                case 2:
                    System.out.println("Introduzca el mensaje a cifrar:");
                    String mensajeUsuario = writeString();
                    if(rutaSimetrica.exists()) {
                        encriptarConClaveSimetrica(mensajeUsuario);
                        System.out.println("Mensaje encriptado correctamente.");
                    }else{
                        System.err.println("Error, la clave simétrica no ha podido ser encontrada primero genere una clave.");
                    }
                    break;
                case 3:
                    if(rutaSimetrica.exists() && rutaSimetricaBin.exists()) {
                        desencriptarConClaveSimetrica();
                    }else{
                        System.err.println("La clave o el mensaje no han sido encontrados, se requieren los dos archivos para este proceso.");
                    }
                    break;
                case 4:
                    System.out.println("Introduzca el documento a verificar: (Ej Doc1.pdf)");
                    String pathDocumento = "./files/keys/firmaDigital/documents/"+writeString();
                    File documento = new File(pathDocumento);
                    if (documento.exists()) {
                        comprobarFirmaDigital(pathDocumento);
                    }else{
                        System.out.println("El fichero introducido no ha sido encontrado, deben estar en el path: (/files/keys/firmaDigital/documents/Doc1.pdf)");
                    }
                    break;
                case 5 :
                    System.out.println("Introduzca el mensaje a cifrar:");
                    String mensajeUsuarioAsimetrico = writeString();
                    if(rutaClaveSete.exists()) {
                        encriptarConClaveAsimetrica(mensajeUsuarioAsimetrico);
                        System.out.println("Mensaje encriptado correctamente.");
                    }else{
                        System.err.println("Error, la clave asimétrica no ha podido ser encontrada.");
                    }
                    break;

                //En el propio método de writeInteger se informa al usuario que introduzca un dato correcto.
                case null: break;
                default :
                    System.out.println("Seleccione una opción valida.");
            }
        }
    }

    /**
     * Este método se utiliza cuando al usuario se le pide una entrada de un dato entero
     * debido a que se puede producir una excepción, si el usuario introduce otro dato
     * se gestiona la excepción, se informa al usuario de que introduzca un dato correcto.
     *
     * @return En caso de que el dato no sea numérico se devolverá un null.
     */
    private static Integer writeInteger(){
        Scanner num = new Scanner(System.in);
        try{
            return num.nextInt();
        }catch (InputMismatchException e){
            System.out.println("Introduzca un dato correcto.");
            return null;
        }
    }

    /**
     * Para evitar la repetición de código cada vez que el usuario tenga
     * que introducir una cadena de caracteres se llama a este método.
     * @return devuelve la cadena de caracteres introducida por el usuario.
     */
    private static String writeString(){
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

}
