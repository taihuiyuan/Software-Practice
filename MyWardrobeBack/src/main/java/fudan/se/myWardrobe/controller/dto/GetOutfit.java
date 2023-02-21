package fudan.se.myWardrobe.controller.dto;

import fudan.se.myWardrobe.entity.Clothes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetOutfit {
    private String outfitName;
    private List<Clothes> clothes;
}
