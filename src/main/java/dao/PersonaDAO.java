package dao;

import clases.Address;
import clases.Persona;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;

import java.util.*;

import static criptografiaSources.CifradoMD5.cifrarContraseña;

/**
 * Clase encargada de contener todos los métodos y consultas sql
 * necesarias en este programa de la clase Persona y, por lo tanto,
 * su clase embebida Address.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class PersonaDAO {

    /**
     * Método encargado de conectarse con la base de datos MongoDb y su correspondiente
     * url, tambien se define la base de datos a conectarse.
     *
     * @return Devuelve la conexión con la base de datos deseada.
     */
    public static MongoDatabase getDatabase() {
        String url = "mongodb://root:Sandia4you@localhost:27017";
            MongoClient cliente = MongoClients.create(url);
                return cliente.getDatabase("appregistro");
    }

    /**
     * Método encargado de añadir un usuario a la base de datos, en caso de que los campos
     * String se encuentren vacíos no se introducen también los Integer en caso de tener un valor
     * de -1 tampoco son introducidos en la base de datos.
     *
     * @param user Objeto de la clase persona de la cual se obtienen los datos para registrarlos a la base
     * de datos.
     */
    public static void addUser(Persona user) {
        boolean addressEmpty = true;
        MongoDatabase database = getDatabase();
        MongoCollection<Document> userCollection = database.getCollection("users");

        Document addressDocument = new Document();
            if (!user.getAddress().getStreet().isEmpty()) {
                addressDocument.append("street", user.getAddress().getStreet());
                addressEmpty = false;
            }
            if (user.getAddress().getNumber() != null) {
                addressDocument.append("number", user.getAddress().getNumber());
                addressEmpty = false;
            }
            if (!user.getAddress().getPostalCode().isEmpty()) {
                addressDocument.append("codigoPostal", user.getAddress().getPostalCode());
                addressEmpty = false;
            }

        Document userDocument = new Document()
                .append("username", user.getUsername());

        byte[] hashPassword = cifrarContraseña(user.getPassword());
        userDocument.append("password", new Binary(hashPassword));

        if (!user.getName().isEmpty()) {
            userDocument.append("name", user.getName());
        }
        if (user.getAge() != null) {
            userDocument.append("age", user.getAge());
        }
        if (!user.getEmails().isEmpty()) {
            userDocument.append("emails", user.getEmails());
        }
        if (!addressEmpty) {
            userDocument.append("address", addressDocument);
        }
        userCollection.insertOne(userDocument);
    }

    /**
     * Método encargado de eliminar un usuario de la base de datos,
     * recibe el usuario a eliminar lo busca y si lo encuentra lo elimina.
     *
     * @param username Usuario a eliminar.
     * @return Devuelve true si la operación ha sido correcta.
     */
    public static boolean deleteUser(String username){
        MongoDatabase database = getDatabase();
        MongoCollection<Document> userCollection = database.getCollection("users");
        Bson filter = Filters.eq("username", username);

        DeleteResult result = userCollection.deleteOne(filter);
        return result.getDeletedCount() > 0;
    }

    /**
     * Método encargado de actualizar la contraseña de un usuario,
     * recibe el usuario lo busca y si lo encuentra actualiza la contraseña.
     *
     * @param username Usuario a modificar.
     * @param password Nueva contraseña posteriormente encriptada y insertada en la base datos.
     * @return Devuelve true si la operación ha sido correcta.
     */
    public static boolean updateUserPassword(String username, String password) {
        MongoDatabase database = getDatabase();
        MongoCollection<Document> userCollection = database.getCollection("users");
        Bson filter = Filters.eq("username", username);

        byte[] hashPassword = cifrarContraseña(password);
        Bson update = Updates.set("password", new Binary(hashPassword));
            UpdateResult result = userCollection.updateOne(filter, update);
                return result.getModifiedCount() > 0;
    }

    /**
     * Método encargado de obtener todos los usuarios de la base de datos
     * y guardarlos en una Lista.
     *
     * @return Devuelve una lista con todos los usuarios de la sql almacenados.
     */
    public static List<Persona> getAllUsers() {
        List<Persona> personas = new ArrayList<>();
        MongoDatabase database = getDatabase();
        MongoCollection<Document> usersCollection = database.getCollection("users");

        Iterable<Document> userDocuments = usersCollection.find();
        for (Document users : userDocuments) {
            byte[] storedHash = ((Binary) users.get("password")).getData();
            String password = Base64.getEncoder().encodeToString(storedHash);

            Document addressDoc = users.get("address", Document.class);
            Address address = null;
            if (addressDoc != null) {
                address = new Address(
                        addressDoc.getString("street"),
                        addressDoc.getInteger("number"),
                        addressDoc.getString("codigoPostal")
                );
            }

            Persona persona = new Persona(
                    users.getString("username"),
                    password,
                    users.getString("name"),
                    users.getInteger("age", -1),
                    users.getList("emails", String.class),
                    address
            );
            personas.add(persona);
        }
        return personas;
    }

    /**
     * Método encargado de buscar los usuarios entre dos edades, guardando los
     * usuarios entre esas edades en una lista.
     *
     * @param min Edad minima para obtener el usuario.
     * @param max Edad maxima para obtener el usuario.
     * @return Devuelve una lista con todos los usuarios almacenados entre esa edad.
     */
    public static List<Persona> getUsersBetweenAges(Integer min, Integer max) {
        MongoDatabase database = getDatabase();
        MongoCollection<Document> userCollection = database.getCollection("users");

        Bson minAge = Filters.gte("age", min);
        Bson maxAge = Filters.lte("age", max);
        Bson filter = Filters.and(minAge, maxAge);

        Iterable<Document> filterResult = userCollection.find(filter);
        List<Persona> users = new ArrayList<>();

        for (Document usersLoop : filterResult) {
            byte[] storedHash = ((Binary) usersLoop.get("password")).getData();
            String password = Base64.getEncoder().encodeToString(storedHash);

            Document addressDoc = usersLoop.get("address", Document.class);
            Address address = null;
            if (addressDoc != null) {
                address = new Address(
                        addressDoc.getString("street"),
                        addressDoc.getInteger("number"),
                        addressDoc.getString("codigoPostal")
                );
            }

            Persona persona = new Persona(
                    usersLoop.getString("username"),
                    password,
                    usersLoop.getString("name"),
                    usersLoop.getInteger("age", -1),
                    usersLoop.getList("emails", String.class),
                    address
            );
            users.add(persona);
        }
        return users;
    }

    /**
     * Método encargado de buscar los usuarios por su código postal, guardando
     * los usuarios encontrados en una lista.
     *
     * @param codigoPostal Código postal por el que buscar a los usuarios.
     * @return Devuelve una lista de Personas con los usuarios encontrados con ese código postal.
     */
    public static List<Persona> getUsersByCP(String codigoPostal){
        MongoDatabase database = getDatabase();
        MongoCollection<Document> userCollection = database.getCollection("users");

        Bson filters = Filters.eq("address.codigoPostal", codigoPostal);

        Iterable<Document> filterResult = userCollection.find(filters);
        List<Persona> users = new ArrayList<>();


        for (Document usersLoop : filterResult) {
            byte[] storedHash = ((Binary) usersLoop.get("password")).getData();
            String password = Base64.getEncoder().encodeToString(storedHash);

            Document addressDoc = usersLoop.get("address", Document.class);
            Address address = null;
            if (addressDoc != null) {
                address = new Address(
                        addressDoc.getString("street"),
                        addressDoc.getInteger("number"),
                        addressDoc.getString("codigoPostal")
                );
            }

            Persona persona = new Persona(
                    usersLoop.getString("username"),
                    password,
                    usersLoop.getString("name"),
                    usersLoop.getInteger("age", -1),
                    usersLoop.getList("emails", String.class),
                    address
            );
            users.add(persona);
        }
        return users;
    }

    /**
     * Método encargado de buscar los usuarios que tengan registrado un correo, guardando
     * los usuarios encontrados en una lista.
     *
     * @return Devuelve una lista de Personas con los usuarios encontrados con uno o más correos electrónicos.
     */
    public static List<Persona> getUsersWithEmail(){
        MongoDatabase database = getDatabase();
        MongoCollection<Document> userCollection = database.getCollection("users");

        Bson filters = Filters.exists("emails");

        Iterable<Document> filterResult = userCollection.find(filters);
        List<Persona> users = new ArrayList<>();


        for (Document usersLoop : filterResult) {
            byte[] storedHash = ((Binary) usersLoop.get("password")).getData();
            String password = Base64.getEncoder().encodeToString(storedHash);

            Document addressDoc = usersLoop.get("address", Document.class);
            Address address = null;
            if (addressDoc != null) {
                address = new Address(
                        addressDoc.getString("street"),
                        addressDoc.getInteger("number"),
                        addressDoc.getString("codigoPostal")
                );
            }

            Persona persona = new Persona(
                    usersLoop.getString("username"),
                    password,
                    usersLoop.getString("name"),
                    usersLoop.getInteger("age", -1),
                    usersLoop.getList("emails", String.class),
                    address
            );
            users.add(persona);
        }
        return users;
    }
}
