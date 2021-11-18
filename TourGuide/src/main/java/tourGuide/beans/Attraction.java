package tourGuide.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Model for collecting data from an attraction.
 */
@Getter
@Setter
@AllArgsConstructor
public class Attraction {

    public String attractionName;
    public String city;
    public String state;
    public UUID attractionId;
    public Location location;

}
