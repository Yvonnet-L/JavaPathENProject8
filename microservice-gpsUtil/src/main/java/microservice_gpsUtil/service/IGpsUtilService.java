package microservice_gpsUtil.service;

import microservice_gpsUtil.dto.AttractionDTO;
import microservice_gpsUtil.dto.VisitedLocationDTO;

import java.util.List;
import java.util.UUID;

public interface IGpsUtilService {

    public List<AttractionDTO> getAttractions();

    public VisitedLocationDTO getUserLocation(UUID userId);
}
