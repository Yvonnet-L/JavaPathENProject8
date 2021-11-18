package tourGuide.beans;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for collecting data from a Location.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    public double longitude;
    public double latitude;
}
