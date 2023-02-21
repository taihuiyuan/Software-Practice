package fudan.se.myWardrobe.service;

import fudan.se.myWardrobe.controller.dto.CategoryDTO;
import fudan.se.myWardrobe.controller.request.CategoryRequest;
import fudan.se.myWardrobe.controller.request.EditCategoryRequest;
import fudan.se.myWardrobe.entity.Category;
import fudan.se.myWardrobe.entity.User;
import fudan.se.myWardrobe.exception.BaseException;
import fudan.se.myWardrobe.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Resource
    private UserRepository userRepository;

    @Resource
    private MyWardrobe myWardrobe;

    @Override
    public CategoryDTO getAllCategories(String username) {
        if (username == null || username.equals("")){
            throw new BaseException("the username is empty");
        }

        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        List<Category> categories = myWardrobe.getCategories();
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategories(categories);
        return categoryDTO;
    }

    @Override
    public void addCategory(CategoryRequest request){
        String username = request.getUsername();
        String categoryName = request.getCategoryName();

        if(username == null || username.equals("") || categoryName == null || categoryName.equals("")){
            throw new BaseException("the username or categoryName is empty");
        }

        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        myWardrobe.addCategory(categoryName);
    }

    @Override
    public void editCategory(EditCategoryRequest request){
        String username = request.getUsername();
        String categoryName = request.getCategoryName();
        String newName = request.getNewCategoryName();

        if(username == null || username.equals("") || categoryName == null || categoryName.equals("") || newName == null || newName.equals("")){
            throw new BaseException("the username or categoryName is empty");
        }

        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        myWardrobe.editCategory(categoryName,newName);
    }

    @Override
    public void deleteCategory(CategoryRequest request){
        String username = request.getUsername();
        String categoryName = request.getCategoryName();

        if(username == null || username.equals("") || categoryName == null || categoryName.equals("")){
            throw new BaseException("the username or categoryName is empty");
        }

        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        myWardrobe.deleteCategory(categoryName);
    }

}
