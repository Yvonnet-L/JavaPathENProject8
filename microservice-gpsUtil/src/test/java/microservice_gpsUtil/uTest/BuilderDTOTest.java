package microservice_gpsUtil.uTest;


import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import microservice_gpsUtil.dto.AttractionDTO;
import microservice_gpsUtil.dto.Location;
import microservice_gpsUtil.dto.VisitedLocationDTO;
import microservice_gpsUtil.tool.BuilderDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@ExtendWith(MockitoExtension.class)
public class BuilderDTOTest {


    @InjectMocks
    BuilderDTO builderDTO;

    //-------Test ---------buildAttractionDto()-----------------------------------------------------------------------
    @Test
    @DisplayName("buildAttractionDto() test with Attraction valid")
    public void buildAttractionDtoTestWithParamValid(){
        // GIVEN
        Attraction attraction = new Attraction("Disneyland", "Anaheim", "CA",
                                        33.817595D, -117.922008D);
        // WHEN
        AttractionDTO attractionDTOResult = builderDTO.buildAttractionDto(attraction);
        // THEN
        assertThat(attractionDTOResult.attractionName).isEqualTo("Disneyland");
        assertThat(attractionDTOResult.city).isEqualTo("Anaheim");
        assertThat(attractionDTOResult.state).isEqualTo("CA");
        assertThat(attractionDTOResult.location.latitude).isEqualTo(33.817595D);
        assertThat(attractionDTOResult.location.longitude).isEqualTo(-117.922008D);

    }

    @Test
    @DisplayName("buildAttractionDto() test with Attraction not valid")
    public void buildAttractionDtoTestWithBadParam(){
        // THEN
        assertThatNullPointerException().isThrownBy(() -> builderDTO.buildAttractionDto(null));
    }
    //-------Test ---------buildAttractionDto()-----------------------------------------------------------------------
    @Test
    @DisplayName("buildVisitedLocationDto() test with VisitedLocation valid")
    public void buildVisitedLocationDtoTestWithParamValid(){
        // GIVEN
        UUID userId = UUID.randomUUID();
        gpsUtil.location.Location locationUtil = new gpsUtil.location.Location(144.109457,-60.620536);
        VisitedLocation userLocation = new VisitedLocation(userId, locationUtil,  new Date());
        // WHEN
        VisitedLocationDTO userLocationDTOResult = builderDTO.buildVisitedLocationDto(userLocation);
        // THEN
        assertThat(userLocationDTOResult.userId).isEqualTo(userId);
        assertThat(userLocationDTOResult.location.longitude).isEqualTo(-60.620536);
        assertThat(userLocationDTOResult.location.latitude).isEqualTo(144.109457);
    }
}
