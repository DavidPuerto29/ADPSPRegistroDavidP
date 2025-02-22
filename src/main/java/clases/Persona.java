package clases;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un usuario de nuestro programa integrado por
 * varios campos utilizados en el programa principal.
 *
 * Consta con un toString modificado para cumplir los requisitos de la aplicación.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
@NoArgsConstructor @Getter @Setter
public class Persona {

    private String username;
    private String password;
    private String name;
    private Integer age;
    private List<String> emails = new ArrayList<>();
    private Address address;

    public Persona(String username, String password, String name, Integer age, List<String> emails, Address address) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.age = age;
        this.emails = emails;
        this.address = address;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usuario: ").append(username);

        if (name != null) {
            sb.append("\nNombre: ").append(name);
        }
        if (age != null && age > 0) {
            sb.append("\nEdad: ").append(age);
        }
        if (emails != null && !emails.isEmpty()) {
            sb.append("\nEmails: ").append(emails);
        }
        if (address != null) {
            if (address.getStreet() != null) {
                sb.append("\nAddress (Calle): ").append(address.getStreet());
            }
            if (address.getNumber() != null && address.getNumber() > 0) {
                sb.append("\nAddress (Número): ").append(address.getNumber());
            }
            if (address.getPostalCode() != null) {
                sb.append("\nAddress (Código Postal): ").append(address.getPostalCode());
            }
        }

        return sb.toString();
    }
}
