package tourGuide.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Data Transfer Object,  UserLocation
 * grouping together the user's identifier and their location
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLocationDTO {

    private UUID userId;

    private LocationDTO locationDTO;
}
