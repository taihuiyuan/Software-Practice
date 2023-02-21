package fudan.se.myWardrobe;

import fudan.se.myWardrobe.controller.request.CategoryRequest;
import fudan.se.myWardrobe.controller.request.EditCategoryRequest;
import fudan.se.myWardrobe.controller.response.SuccessResponse;
import fudan.se.myWardrobe.entity.Category;
import fudan.se.myWardrobe.entity.User;
import fudan.se.myWardrobe.entity.Wardrobe;
import fudan.se.myWardrobe.exception.BaseException;
import fudan.se.myWardrobe.repository.CategoryRepository;
import fudan.se.myWardrobe.repository.UserRepository;
import fudan.se.myWardrobe.repository.WardrobeRepository;
import fudan.se.myWardrobe.service.MyWardrobe;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CategoryTest {

    @Resource
    private CategoryRepository categoryRepository;

    @Resource
    private MyWardrobe myWardrobe;

    @Resource
    private UserRepository userRepository;

    @Resource
    private WardrobeRepository wardrobeRepository;

    Wardrobe wardrobe;


    @Before
    public void init() {
        System.out.println("开始测试----------");
        User user = new User("username", "password", "email");
        if (userRepository.findByUsername("username")==null){
            userRepository.save(user);
        }
        user = userRepository.findByUsername("username");
        wardrobe = wardrobeRepository.findByUser(user);

        myWardrobe.init(user);

        myWardrobe.clearCategory();
    }
    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }

    //成功添加
    @Test
    public void success_add(){
        try {
            myWardrobe.addCategory("category");

            Assert.assertEquals(2, myWardrobe.countCategory());//第一个是默认的未分类
            Assert.assertEquals("category", myWardrobe.getCategory(1).getName());

        }catch (BaseException e){
            System.out.println(e.getMessage());
            Assert.fail();
        }
    }

    //成功修改
    @Test
    public void success_edit(){
        try{
            myWardrobe.addCategory("oldName");

            myWardrobe.editCategory("oldName","newName");

            List<Category> categories = myWardrobe.getCategories();

            int flag = 0;
            for (Category category: categories){
                if (Objects.equals(category.getName(), "oldName")){
                    Assert.fail();
                }
                if (Objects.equals(category.getName(), "newName")){
                    flag = 1;
                }
            }
            Assert.assertEquals(1, flag);

        }catch (BaseException e){
            System.out.println(e.getMessage());
            Assert.fail();
        }
    }

    //成功删除
    @Test
    public void success_delete(){
        try{
            myWardrobe.addCategory("name");

            myWardrobe.deleteCategory("name");

            Assert.assertNull(categoryRepository.findByWardrobeAndName(wardrobe, "name"));
        }catch (BaseException e){
            System.out.println(e.getMessage());
            Assert.fail();
        }
    }



}
