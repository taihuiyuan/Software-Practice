package fudan.se.myWardrobe.controller;

import fudan.se.myWardrobe.controller.dto.CategoryDTO;
import fudan.se.myWardrobe.controller.request.CategoryRequest;
import fudan.se.myWardrobe.controller.request.EditCategoryRequest;
import fudan.se.myWardrobe.controller.response.ErrorResponse;
import fudan.se.myWardrobe.controller.response.SuccessResponse;
import fudan.se.myWardrobe.entity.Category;
import fudan.se.myWardrobe.exception.BaseException;
import fudan.se.myWardrobe.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    //获取分类列表
    @GetMapping("/category/get")
    public CategoryDTO getCategories(@RequestParam("username") String username){
        return categoryService.getAllCategories(username);
    }

    @PostMapping("/category/add")
    public ResponseEntity<?> addCategory(@RequestBody CategoryRequest request){
        try{
            categoryService.addCategory(request);
            SuccessResponse successResponse = new SuccessResponse("add successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }catch(BaseException e){
            ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/category/edit")
    public ResponseEntity<?> editCategory(@RequestBody EditCategoryRequest request){
        try{
            categoryService.editCategory(request);
            SuccessResponse successResponse = new SuccessResponse("edit successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }catch(BaseException e){
            ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/category/delete")
    public ResponseEntity<?> deleteCategory(@RequestBody CategoryRequest request){
        try{
            categoryService.deleteCategory(request);
            SuccessResponse successResponse = new SuccessResponse("delete successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }catch(BaseException e){
            ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }



}
