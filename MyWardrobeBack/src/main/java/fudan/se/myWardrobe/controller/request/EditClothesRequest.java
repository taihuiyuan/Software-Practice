package fudan.se.myWardrobe.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditClothesRequest {
    private String username;
    private int clothesID;
    private String categoryName;
    private String color;
    private String season;
    private double price;
    private String location;
    private String note;
}
