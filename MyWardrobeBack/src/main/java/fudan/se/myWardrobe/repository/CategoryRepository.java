package fudan.se.myWardrobe.repository;

import fudan.se.myWardrobe.entity.Category;
import fudan.se.myWardrobe.entity.Wardrobe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    public List<Category> findAll();
    public Category findById(int id);
    public Category findByWardrobeAndName(Wardrobe wardrobe, String name);
    public List<Category> findAllByWardrobe(Wardrobe wardrobe);
    public void deleteById(int id);
}
