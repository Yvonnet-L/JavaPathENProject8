package tourGuide.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


/**
 * Model for collecting data from a Provider.
 */
@Getter
@Setter
@AllArgsConstructor
public class Provider {

    public final String name;
    public final double price;
    public final UUID tripId;
}
