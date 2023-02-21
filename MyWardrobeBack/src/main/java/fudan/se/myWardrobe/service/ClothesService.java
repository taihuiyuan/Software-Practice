package fudan.se.myWardrobe.service;

import fudan.se.myWardrobe.controller.dto.ClothesDTO;
import fudan.se.myWardrobe.controller.dto.HistoryDTO;
import fudan.se.myWardrobe.controller.dto.SingleClothesDTO;
import fudan.se.myWardrobe.controller.request.ClothesRequest;
import fudan.se.myWardrobe.controller.request.EditClothesRequest;
import fudan.se.myWardrobe.controller.request.SelectRequest;

public interface ClothesService {
    public void addClothes(ClothesRequest clothesRequest);

    public ClothesDTO getClothesByCategory(String username, int categoryID);

    void deleteClothes(String username, int clothesID);

    void modifyClothes(EditClothesRequest request);

    ClothesDTO searchByAttribute(SelectRequest request);

    ClothesDTO searchByKeyword(String username, String keyword);

    HistoryDTO getLocationHistory(String username, int clothesID);

    ClothesDTO getAllClothes(String username);

    SingleClothesDTO getClothesByID(String username, int clothesID);
}
