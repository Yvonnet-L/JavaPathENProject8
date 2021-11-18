package microservice_gpsUtil.uTest;


import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

import microservice_gpsUtil.dto.AttractionDTO;
import microservice_gpsUtil.dto.Location;
import microservice_gpsUtil.dto.VisitedLocationDTO;
import microservice_gpsUtil.service.GpsUtilService;
import microservice_gpsUtil.tool.BuilderDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class GpsUtilServiceTest {

    @InjectMocks
    GpsUtilService gpsUtilService;

    @Mock
    BuilderDTO builderDTO;

    @Mock
    private GpsUtil gpsUtil;

    private  gpsUtil.location.Location locationUtil;

    //-------Test-------getAttractions()------------------------------------------------------------------------
    @Test
    @DisplayName("Test on getAttractions()")
    public void getAttractionsTestOk() {
        // GIVEN
        List<Attraction> attractions = new ArrayList<>();
        attractions.add(new Attraction("Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D));
        attractions.add(new Attraction("Jackson Hole", "Jackson Hole", "WY", 43.582767D, -110.821999D));
        attractions.add(new Attraction("Mojave National Preserve", "Kelso", "CA", 35.141689D, -115.510399D));

        AttractionDTO attractionDTO1 = new AttractionDTO("Disneyland", "Anaheim", "CA", UUID.randomUUID(),  new Location(-29.211451, 88.888888));
        AttractionDTO attractionDTO2 = new AttractionDTO ("Jackson Hole", "Jackson Hole", "WY", UUID.randomUUID(), new Location(43.582767D, -110.821999D));
        AttractionDTO attractionDTO3 = new AttractionDTO("Mojave National Preserve", "Kelso", "CA", UUID.randomUUID(),  new Location(35.141689D, -115.510399D));
        // WHEN
        Mockito.when(gpsUtil.getAttractions()).thenReturn(attractions);
        // THEN
        assertThat(gpsUtilService.getAttractions().size()).isEqualTo(3);
    }

    //---------Test-------getUserLocation(UUID userId)------------------------------------------------------------------------
    @Test
    @DisplayName("Test on getUserLocation(UUID userId)")
    public void getAttractionTestWithParamOk(){
        // GIVEN
        //VisitedLocationDTO userLocationDTO = builderDTO.buildVisitedLocationDto(gpsUtil.getUserLocation(userId));
        UUID userId = UUID.randomUUID();
        locationUtil = new gpsUtil.location.Location(144.109457,-60.620536);
        VisitedLocation userLocation = new VisitedLocation(userId, locationUtil,  new Date());
        VisitedLocationDTO userLocationDTO = new VisitedLocationDTO(userId,
                                    new Location(144.109457,-60.620536),  new Date());
        // WHEN
        Mockito.when(gpsUtil.getUserLocation(userId)).thenReturn(userLocation);
        Mockito.when(builderDTO.buildVisitedLocationDto(any(VisitedLocation.class))).thenReturn(userLocationDTO);
        // THEN
        assertThat(gpsUtilService.getUserLocation(userId)).isEqualTo(userLocationDTO);
    }


}
