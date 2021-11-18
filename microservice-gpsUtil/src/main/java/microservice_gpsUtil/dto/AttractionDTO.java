package microservice_gpsUtil.dto;

import java.util.UUID;

public class AttractionDTO {
    public String attractionName;
    public String city;
    public String state;
    public UUID attractionId;
    public Location location;

    public AttractionDTO(String attractionName, String city, String state, UUID attractionId, Location location) {
        this.attractionName = attractionName;
        this.city = city;
        this.state = state;
        this.attractionId = attractionId;
        this.location = location;
    }

}
