package fudan.se.myWardrobe.service;

import fudan.se.myWardrobe.controller.dto.LocationDTO;

public interface LocationService {
    public LocationDTO getLocation(String username);
    public void addLocation(String username,String locationName);
    public void modifyLocation(String username,String locationName,String newLocationName);
    public void deleteLocation(String username,String locationName);
}

