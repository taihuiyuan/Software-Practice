package fudan.se.myWardrobe.service;

import fudan.se.myWardrobe.controller.dto.OutfitDTO;

import java.util.List;

public interface OutfitService {
    public void addOutfit(String username, String outfitName, List<Integer> clothes);
    public OutfitDTO getOutfits(String username);
    public void deleteOutfit(String username, String outfitName);
    public void addClothesIntoOutfit(String username, String outfitName, List<Integer> clothes);
    public void deleteClothesFromOutfit(String username, String outfitName, List<Integer> clothes);
}
