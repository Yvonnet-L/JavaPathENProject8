package tourGuide.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

/**
 * Model for collecting data from a VisitedLocation.
 */
@Getter
@Setter
@AllArgsConstructor
public class VisitedLocation {

    public  UUID userId;
    public Location location;
    public  Date timeVisited;

    public VisitedLocation() {

    }
}
