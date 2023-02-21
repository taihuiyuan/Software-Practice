package fudan.se.myWardrobe.repository;

import fudan.se.myWardrobe.entity.Clothes;
import fudan.se.myWardrobe.entity.User;
import fudan.se.myWardrobe.entity.Wardrobe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardrobeRepository extends JpaRepository<Wardrobe, Long> {
    public Wardrobe findByUser(User user);
    public List<Wardrobe> findAll();
}
