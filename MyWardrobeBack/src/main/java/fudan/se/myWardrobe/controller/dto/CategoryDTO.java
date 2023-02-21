package fudan.se.myWardrobe.controller.dto;

import fudan.se.myWardrobe.entity.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDTO {
    private List<Category> categories;
}
