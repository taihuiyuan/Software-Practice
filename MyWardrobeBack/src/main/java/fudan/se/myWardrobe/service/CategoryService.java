package fudan.se.myWardrobe.service;

import fudan.se.myWardrobe.controller.dto.CategoryDTO;
import fudan.se.myWardrobe.controller.request.CategoryRequest;
import fudan.se.myWardrobe.controller.request.EditCategoryRequest;
import fudan.se.myWardrobe.entity.Category;

import java.util.List;

public interface CategoryService {
    public CategoryDTO getAllCategories(String username);
    public void addCategory(CategoryRequest request);
    public void editCategory(EditCategoryRequest request);
    public void deleteCategory(CategoryRequest request);
}
