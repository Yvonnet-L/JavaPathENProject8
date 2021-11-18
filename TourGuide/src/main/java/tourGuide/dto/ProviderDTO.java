package tourGuide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Data Transfer Object, Provider data receptacle
 * @See Provider
 */

@Getter
@Setter
@AllArgsConstructor
public class ProviderDTO {

    public final String name;
    public final double price;
    public final UUID tripId;
}
