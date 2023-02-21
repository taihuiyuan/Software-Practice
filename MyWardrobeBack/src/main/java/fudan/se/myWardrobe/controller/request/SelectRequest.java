package fudan.se.myWardrobe.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SelectRequest {
    private String username;
    private String categoryName;
    private String season;
    private String priceRange;
    private String color;
    private String location;
}
