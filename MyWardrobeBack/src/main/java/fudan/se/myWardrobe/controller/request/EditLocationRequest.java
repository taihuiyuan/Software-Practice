package fudan.se.myWardrobe.controller.request;

import lombok.Data;

@Data
public class EditLocationRequest {
    private String username;
    private String locationName;
    private String newLocationName;
}
