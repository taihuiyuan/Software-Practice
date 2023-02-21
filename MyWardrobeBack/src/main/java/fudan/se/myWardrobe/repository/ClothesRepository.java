package fudan.se.myWardrobe.repository;

import fudan.se.myWardrobe.entity.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClothesRepository extends JpaRepository<Clothes, Integer> {
    public Clothes findByImageUrl(String imageUrl);
    public Clothes findById(int id);
    public void deleteById(int id);
    public List<Clothes> findAllByCategoryNameLikeAndAndColorLikeAndSeasonLikeAndLocationLikeAndNoteLike(String categoryName, String color, String season, String location, String note);
    public List<Clothes> findAllByLocation(String location);
    public List<Clothes> findAllBySeason(String season);
}
