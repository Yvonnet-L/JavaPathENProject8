package tourGuide.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;


/**
 * Data Transfer Object, user preferential data receptacle
 * @See UserPreferences
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferencesDTO {

    /**
     *  maximum distance where the attrations must be
     */
    @Min(value =0,  message ="must be greater than or equal to 0")
    private int attractionProximity ;

    /**
     *  lowest acceptable price
     */
    @Min(value =0,  message ="must be greater than or equal to 0")
    private int lowerPricePoint;

    /**
     *  maximum acceptable price
     */
    @Min(value =1,  message ="must be greater than or equal to 0")
    private int highPricePoint;

    /**
     *  length of stay
     */
    @Min(value =1,  message ="must be greater than or equal to 1")
    private int tripDuration;

    @Min(value =1,  message ="must be greater than or equal to 1")
    private int ticketQuantity;

    @Min(value =1,  message ="must be greater than or equal to 1")
    private int numberOfAdults;

    @Min(value =0,  message ="must be greater than or equal to 0")
    private int numberOfChildren;

}
