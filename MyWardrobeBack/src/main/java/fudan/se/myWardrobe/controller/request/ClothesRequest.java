package fudan.se.myWardrobe.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClothesRequest {
    private String username;
    private String imageUrl;
    private String categoryName;
    private String color;
    private String season;
    private String price;
    private String location;
    private String note;
}
