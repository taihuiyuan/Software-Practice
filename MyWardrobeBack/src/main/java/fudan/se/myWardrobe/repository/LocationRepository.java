package fudan.se.myWardrobe.repository;

import fudan.se.myWardrobe.entity.Location;
import fudan.se.myWardrobe.entity.Wardrobe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    public List<Location> findAllByWardrobe(Wardrobe wardrobe);
    public Location findByWardrobeAndName(Wardrobe wardrobe, String name);
}
