package microservice_TripPricer.dto;

import java.util.UUID;

public class ProviderDTO {

    public String name;
    public double price;
    public UUID tripId;

    public ProviderDTO(String name, double price, UUID tripId) {
        this.name = name;
        this.price = price;
        this.tripId = tripId;
    }
}
