package clases;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase embebida introducida dentro de la clase Persona,
 * consta de tres campos enfocados en la dirección del usuario.
 *
 * Consta con un toString modificado para cumplir los requisitos de la aplicación.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
@NoArgsConstructor @Getter @Setter
public class Address {
    private String street;
    private Integer number;
    private String postalCode;

    public Address(String street, Integer number, String postalCode) {
        this.street = street;
        this.number = number;
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (street != null) {
            sb.append("Street: ").append(street);
        }
        if (number != null && number > 0) {
            sb.append(" Number: ").append(number);
        }
        if (postalCode != null) {
            sb.append(" Código Postal: ").append(postalCode);
        }
        return sb.toString();
    }
}
