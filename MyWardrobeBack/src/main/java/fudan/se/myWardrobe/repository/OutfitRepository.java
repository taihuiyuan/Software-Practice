package fudan.se.myWardrobe.repository;

import fudan.se.myWardrobe.entity.Clothes;
import fudan.se.myWardrobe.entity.Outfit;
import fudan.se.myWardrobe.entity.Wardrobe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutfitRepository extends JpaRepository<Outfit, Integer> {
    List<Outfit> findAllByWardrobeAndOutfitName(Wardrobe wardrobe, String outfitName);
    List<Outfit> findAllByWardrobe(Wardrobe wardrobe);
    List<Outfit> findAllByWardrobeAndClothes(Wardrobe wardrobe, Clothes clothes);
    Outfit findByWardrobeAndOutfitNameAndClothes(Wardrobe wardrobe, String outfitName, Clothes clothes);
}
