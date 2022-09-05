package com.example.mywardrobe.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClothesDetailResponse {
    private Integer id;
    private String categoryName;
    private String color;
    private String season;
    private String price;
    private String location;
    private String note;
    private String imageUrl;
}
