package tourGuide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tourGuide.beans.Location;


/**
 * Data Transfer Object,  AttractionNear
 * Object grouping together the information of an attraction near to the user
 * with the distance separating them with rewardpoints
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttractionNearDTO {

    String attractionName;
    Location locationAttraction;
    Location locationUser;
    double distance;
    double rewardPoints;

}
