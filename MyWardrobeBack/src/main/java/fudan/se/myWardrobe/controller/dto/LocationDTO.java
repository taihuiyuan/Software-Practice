package fudan.se.myWardrobe.controller.dto;

import fudan.se.myWardrobe.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
    private List<Location> locations;
}
