package fudan.se.myWardrobe.controller.dto;

import fudan.se.myWardrobe.entity.Clothes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClothesDTO {
    private List<Clothes> clothes;
}
