package menus;

import clases.Address;
import clases.Persona;
import java.util.*;

import static criptografiaSources.CifradoMD5.*;
import static dao.PersonaDAO.addUser;
import static dao.PersonaDAO.getAllUsers;
import static menus.CriptografiaMenu.menuCriptografia;
import static menus.MenuBaseDeDatos.menuBaseDeDatos;

/**
 * Clase encargada de contener el menu principal y sus opciones, además de diversos métodos
 * posteriormente llamados para la gestión del programa.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class MenuPrincipal {
    public static void main(String[] args) {
        boolean loop = true;

        while (loop) {
            System.out.println("""
                         ╔══════════════════|Menú Principal|═══════════════════╗\r
                         ║	                                        		   ║\r
                    	 ║	           1 Registrar a una persona               ║\r
                    	 ║	           2 Iniciar sesión          	           ║\r
                    	 ║	           3 Ver datos	                           ║\r
                    	 ║	                                        	       ║\r
                    	 ║	                   0 Salir 		       	           ║\r
                         ╚═════════════════════════════════════════════════════╝\r
                    """);
            Integer op = writeInteger();

            switch (op) {
                case 0:
                    loop = false;
                    break;
                case 1:
                    Persona p = registarUsuario();
                        boolean usuarioRegistrado = usuarioExiste(p);

                    if(p != null && !usuarioRegistrado) {
                        addUser(p);
                    }else if (usuarioRegistrado){
                        System.err.println("Este usuario ya se encuentra registrado.");
                    }
                    break;
                case 2:
                    if(inicioDeSesion()) {
                        menuCriptografia();
                    }
                    break;
                case 3:
                    menuBaseDeDatos();
                    break;
                    

                //En el propio método de writeInteger se informa al usuario que introduzca un dato correcto.
                case null: break;
                default :
                    System.out.println("Seleccione una opción valida.");
            }
        }
        System.out.println("Programa finalizado.");
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
            System.err.println("Dato incorrecto, definido por defecto -1");
            return null;
        }
    }

    /**
     * Este método es el encargado de permitir al usuario registrarse,
     * es importante saber que el username y contraseña son obligatorios pero
     * los demás campos son opcionales y si se dejan vacíos no se guardaran en la
     * base de datos, ademas de los Integer si su valor es -1 o null
     * tampoco se guardaran.
     *
     * @return Devuelve un objeto de la clase persona con los datos rellenados por el usuario,
     * posteriormente se comprobará si no existe un usuario igual en el main.
     */
    private static Persona registarUsuario(){
        List<String> emails = new ArrayList<>();
        boolean otroEmail = true;

        System.out.println("Introduzca usuario: ");
            String username = writeString();
                System.out.println("Introduzca contraseña:");
                    String password = writeString();
        if(!username.isEmpty() && !password.isEmpty()) {
            System.out.println("Introduzca nombre: ");
                String name = writeString();
                    System.out.println("Introduzca edad: (-1 = Vació)");
                        Integer age = writeInteger();
                            while (otroEmail) {
                                System.out.println("Introduzca un email:");
                                    String email = writeString();
                                        if(!email.isEmpty()) {
                                            emails.add(email);
                                        }
                                System.out.println("¿Quiere introducir otro email?(Y-N)");
                                    String opcion = writeString().toLowerCase();
                                        if(opcion.equals("n")){
                                            otroEmail= false;
                                        }
                            }
            System.out.println("Introduzca calle:");
                String street = writeString();
                    System.out.println("Introduzca numero: (-1 = Vació)");
                        Integer number = writeInteger();
                            System.out.println("Introduzca código postal:");
                                String codigoPostal = writeString();

            return new Persona(username,password,name,age,emails,new Address(street,number,codigoPostal));
        }else{
            System.err.println("Los campos de username y password son obligatorios.");
                return null;
        }
    }

    /**
     * Método encargado de comprobar si un usuario ya esta registrado en la base de datos.
     *
     * @param p Persona con los datos, se comprueba su existencia por username.1
     * @return True en caso de que ya exista en la base de datos y false si no se encuentra registrada.
     */
    private static boolean usuarioExiste(Persona p){
        List<Persona> personas = getAllUsers();
        if(p != null) {
            for (Persona personasComparar : personas) {
                if (p.getUsername().equals(personasComparar.getUsername())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Este método se encarga del inicio de sesion del usuario, el usuario introduce
     * un usuario y contraseña y se comprueba en la base de datos su existencia.
     *
     * @return En caso de que el inicio de sesión sea correcto devuelve true.
     */
    private static boolean inicioDeSesion(){
        byte[] contraseñaIntroducida;
        System.out.println("Introduzca usuario: ");
            String user = writeString();
                System.out.println("Introduzca contraseña: ");
                    String password = writeString();
                        contraseñaIntroducida = cifrarContraseña(password);

        List<Persona> personas = getAllUsers();
        for (Persona personasComparar : personas) {
            byte[] storedHash = convertirContraseña(personasComparar.getPassword());
            if(user.equals(personasComparar.getUsername()) &&Arrays.equals(storedHash, contraseñaIntroducida)){
                return true;
            }
        }
        System.err.println("Usuario o contraseña incorrecta.");
            return false;
    }

}
