package fudan.se.myWardrobe;

import fudan.se.myWardrobe.controller.request.ClothesRequest;
import fudan.se.myWardrobe.controller.response.SuccessResponse;
import fudan.se.myWardrobe.entity.*;
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
public class AddClothesTest {

    @Resource
    private TestRestTemplate testRestTemplate;

    @Resource
    private CategoryRepository categoryRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private WardrobeRepository wardrobeRepository;

    @Resource
    private MyWardrobe myWardrobe;

    private Category category0;
    private Category category_not_exist;
    String[] seasons = {"春天", "夏天","秋天","冬天"};
    String[] colors = {"黑色系","白色系","灰色系","裸色系","红色系","橙色系","黄色系","绿色系","蓝色系","紫色系","其它"};

    @Before
    public void init() {
        System.out.println("开始测试-----------------");
        User user = new User("username", "password", "email");
        if (userRepository.findByUsername("username")==null){
            userRepository.save(user);
        }
        user = userRepository.findByUsername("username");
        Wardrobe wardrobe = wardrobeRepository.findByUser(user);
        myWardrobe.init(user);

        category_not_exist = new Category(wardrobe,"test");
    }

    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }

    //成功添加
    @Test
    public void success_with_valid_information(){
        try {
            myWardrobe.clearClothes();
            myWardrobe.clearCategory();
            List<Category> categories = myWardrobe.getCategories();//默认有一个未分类
            List<Location> locations = myWardrobe.getLocations();//默认有三个：卧室衣柜/衣帽间/鞋柜
            Clothes clothes = new Clothes("http:127.0.0.1/api/file/1.png",
                    categories.get(0).getName(),
                    colors[0],//{"黑色系","白色系","灰色系","裸色系","红色系","橙色系","黄色系","绿色系","蓝色系","紫色系","其它"}
                    seasons[0],//{"春天", "夏天","秋天","冬天"}
                    100,
                    locations.get(0).getName(),
                    "test");

            myWardrobe.addClothes(clothes);

            Assert.assertEquals(1, myWardrobe.countClothes());
            Assert.assertEquals("http:127.0.0.1/api/file/1.png", myWardrobe.getClothes(0).getImageUrl());
            Assert.assertEquals(categories.get(0).getName(), myWardrobe.getClothes(0).getCategoryName());
            Assert.assertEquals(colors[0], myWardrobe.getClothes(0).getColor());
            Assert.assertEquals(seasons[0], myWardrobe.getClothes(0).getSeason());
            Assert.assertEquals(100, myWardrobe.getClothes(0).getPrice(), 0.1);
            Assert.assertEquals(locations.get(0).getName(), myWardrobe.getClothes(0).getLocation());
            Assert.assertEquals("test", myWardrobe.getClothes(0).getNote());
        }catch (BaseException e){
            System.out.println(e.getMessage());
            Assert.fail();
        }
    }

    /**
     * imageUrl合法性验证
     */
    @Test
    public void imageUrl_should_not_empty(){
        try {
            myWardrobe.clearClothes();
            myWardrobe.clearCategory();
            List<Category> categories = myWardrobe.getCategories();//默认有一个未分类
            List<Location> locations = myWardrobe.getLocations();//默认有四个：默认/卧室衣柜/衣帽间/鞋柜
            Clothes clothes = new Clothes("",
                    categories.get(0).getName(),
                    colors[0],//{"黑色系","白色系","灰色系","裸色系","红色系","橙色系","黄色系","绿色系","蓝色系","紫色系","其它"}
                    seasons[0],//{"春天", "夏天","秋天","冬天"}
                    100,
                    locations.get(0).getName(),
                    "test");

            myWardrobe.addClothes(clothes);
            Assert.fail();
        }catch (BaseException e){
            System.out.println(e.getMessage());
            Assert.assertEquals("the image is empty", e.getMessage());
        }
    }

    /**
     * category合法性验证
     */
    @Test
    public void category_not_exist_should_create(){
        try {
            myWardrobe.clearClothes();
            myWardrobe.clearCategory();
            Clothes clothes = new Clothes("http:127.0.0.1/api/file/1.png",
                    category_not_exist.getName(),
                    "red",
                    "summer",
                    100,
                    "衣柜",
                    "test");

            myWardrobe.addClothes(clothes);

            List<Category> categories = myWardrobe.getCategories();
            int flag = 0;
            for (Category category: categories){
                if (Objects.equals(category.getName(), category_not_exist.getName())) {
                    flag = 1;
                    break;
                }
            }

            Assert.assertEquals(1, flag);
        }catch (BaseException e){
            System.out.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void category_empty_should_become_category0(){
        try {
            myWardrobe.clearClothes();
            myWardrobe.clearCategory();
            List<Location> locations = myWardrobe.getLocations();//默认有三个：卧室衣柜/衣帽间/鞋柜
            Clothes clothes = new Clothes("http:127.0.0.1/api/file/1.png",
                    null,
                    colors[0],//{"黑色系","白色系","灰色系","裸色系","红色系","橙色系","黄色系","绿色系","蓝色系","紫色系","其它"}
                    seasons[0],//{"春天", "夏天","秋天","冬天"}
                    100,
                    locations.get(0).getName(),
                    "test");

            myWardrobe.addClothes(clothes);

            Assert.assertEquals("未分类", myWardrobe.getClothes(0).getCategoryName());
        }catch (BaseException e){
            System.out.println(e.getMessage());
            Assert.fail();
        }
    }
}
