package fudan.se.myWardrobe.repository;

import fudan.se.myWardrobe.entity.Clothes;
import fudan.se.myWardrobe.entity.LocationHistory;
import fudan.se.myWardrobe.entity.Wardrobe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationHistoryRepository extends JpaRepository<LocationHistory, Integer> {
    List<LocationHistory> findAllByWardrobeAndClothes(Wardrobe wardrobe, Clothes clothes);
    void deleteAllByWardrobeAndClothes(Wardrobe wardrobe, Clothes clothes);
}
