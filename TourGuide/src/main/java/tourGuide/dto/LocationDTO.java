package tourGuide.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Data Transfer Object, Location data receptacle
 * @See Location
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {

    /**
     * The location latitude.
     */
    private double latitude;

    /**
     * The location longitude.
     */
    private double longitude;
}
