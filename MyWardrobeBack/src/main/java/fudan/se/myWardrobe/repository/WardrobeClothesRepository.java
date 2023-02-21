package fudan.se.myWardrobe.repository;

import fudan.se.myWardrobe.entity.Clothes;
import fudan.se.myWardrobe.entity.Wardrobe;
import fudan.se.myWardrobe.entity.WardrobeClothes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardrobeClothesRepository extends JpaRepository<WardrobeClothes, Long> {
    public List<WardrobeClothes> findAllByWardrobe(Wardrobe wardrobe);
    public WardrobeClothes findByClothes(Clothes clothes);
    public void deleteByWardrobeAndClothes(Wardrobe wardrobe, Clothes clothes);
}
