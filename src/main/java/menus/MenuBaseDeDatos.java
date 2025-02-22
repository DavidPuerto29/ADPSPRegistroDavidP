package menus;

import clases.Persona;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static dao.PersonaDAO.*;

/**
 * Clase encargada de contener el menu y los métodos del apartado de base de datos,
 * posteriormente es llamado desde el menu principal.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class MenuBaseDeDatos {
    static void menuBaseDeDatos(){
            boolean loop = true;

            while (loop) {
                System.out.println("""
                         ╔════════════════|Menú Base De Datos|═════════════════╗\r
                         ║	                                        		   ║\r
                    	 ║	         1 Mostrar todas las personas              ║\r
                    	 ║	         2 Mostrar personas entre edades  	       ║\r
                    	 ║	         3 Mostrar personas con email	           ║\r
                    	 ║	         4 Mostrar personas por código postal	   ║\r
                    	 ║	         5 Eliminar persona                        ║\r	
                    	 ║	         6. Actualizar contraseña                  ║
                    	 ║	                                        	       ║\r
                    	 ║	                    0 Salir 		       	       ║\r
                         ╚═════════════════════════════════════════════════════╝\r
                    """);
                Integer op = writeInteger();

                switch (op) {
                    case 0:
                        loop = false;
                        break;
                    case 1:
                        mostradorUsuarios(getAllUsers());
                        break;
                    case 2:
                        System.out.println("Introduzca edad minima:");
                            Integer edadMinima = writeInteger();
                        System.out.println("Introduzca edad maxima:");
                            Integer edadMaxima = writeInteger();
                        mostradorUsuarios(getUsersBetweenAges(edadMinima,edadMaxima));
                        break;
                    case 3:
                        mostradorUsuarios(getUsersWithEmail());
                        break;
                    case 4:
                        System.out.println("Introduzca el codigo postal:");
                            String postalCode = writeString();
                                mostradorUsuarios(getUsersByCP(postalCode));
                        break;
                    case 5 :
                        mostradorUsuarios(getAllUsers());
                            System.out.println("Introduza el nombre de usuario a eliminar: ");
                                String username = writeString();
                                    if(deleteUser(username)){
                                        System.out.println("Usuario eliminado correctamente.");
                                    }else{
                                        System.err.println("El usuario no ha sido encontrado.");
                                    }
                        break;
                    case 6:
                        mostradorUsuarios(getAllUsers());
                        System.out.println("Introduzca el usuario a actualizar: ");
                            String usuario = writeString();
                                System.out.println("Introduzca la nueva contraseña: ");
                                    String password = writeString();
                        if(updateUserPassword(usuario,password)){
                            System.out.println("Usuario actualizado correctamente.");
                        }else{
                            System.err.println("El usuario no ha sido encontrado.");
                        }
                        break;
                    case null:
                        break;
                    default :
                        System.out.println("Seleccione una opción valida.");
                }
            }
    }

    /**
     * Este método se encarga de mostrar una lista de usuarios por pantalla
     * es reutilizado en varios apartados del programa.
     *
     * @param personas La lista de Personas a mostrar por pantalla.
     */
    private static void mostradorUsuarios(List<Persona> personas){
        if(!personas.isEmpty()){
            for (Persona personasComparar : personas) {
                System.out.println("""
                        
                             ╔═════════════════╗\r
                             ║	   Usuario     ║\r
                             ╚═════════════════╝\r
                        """);
                System.out.println(personasComparar);
            }
        }else{
            System.err.println("No se han encontrado usuarios.");
        }
    }


    /**
     * Para evitar la repetición de código cada vez que el usuario tenga
     * que introducir una cadena de caracteres se llama a este método.
     * @return devuelve la cadena de caracteres introducida por el usuario.
     */
    private static String writeString(){
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().toLowerCase();
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
            Integer numero = num.nextInt();
            if(numero < 0) {
                return null;
            }
            return numero;
        }catch (InputMismatchException e){
            System.err.println("Dato incorrecto, definido por defecto 0");
            return null;
        }
    }

}
