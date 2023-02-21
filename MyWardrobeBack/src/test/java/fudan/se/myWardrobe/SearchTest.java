package fudan.se.myWardrobe;

import fudan.se.myWardrobe.entity.*;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SearchTest {
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

    private Clothes newClothes;

    String[] seasons = {"春天", "夏天", "秋天", "冬天"};
    String[] colors = {"黑色系", "白色系", "灰色系", "裸色系", "红色系", "橙色系", "黄色系", "绿色系", "蓝色系", "紫色系", "其它"};
    List<Category> categories;
    List<Location> locations;

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

        myWardrobe.clearClothes();
        myWardrobe.clearCategory();

        categories = myWardrobe.getCategories();//默认有一个未分类
        locations = myWardrobe.getLocations();//默认有三个：卧室衣柜/衣帽间/鞋柜
        newClothes = new Clothes("http:127.0.0.1/api/file/1.png",
                categories.get(0).getName(),
                colors[0],//{"黑色系","白色系","灰色系","裸色系","红色系","橙色系","黄色系","绿色系","蓝色系","紫色系","其它"}
                seasons[0],//{"春天", "夏天","秋天","冬天"}
                50,
                locations.get(0).getName(),
                "test");

        myWardrobe.addClothes(newClothes);
    }

    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }

    @Test
    public void search_by_keyword_category(){
        List<Clothes> clothes = myWardrobe.searchByKeyword(newClothes.getCategoryName());
        Assert.assertEquals(1, clothes.size());
        Assert.assertEquals(newClothes.getImageUrl(), clothes.get(0).getImageUrl());
    }

    @Test
    public void search_by_keyword_color(){
        List<Clothes> clothes = myWardrobe.searchByKeyword(newClothes.getColor());
        Assert.assertEquals(1, clothes.size());
        Assert.assertEquals(newClothes.getImageUrl(), clothes.get(0).getImageUrl());
    }

    @Test
    public void search_by_keyword_location(){
        List<Clothes> clothes = myWardrobe.searchByKeyword(newClothes.getLocation());
        Assert.assertEquals(1, clothes.size());
        Assert.assertEquals(newClothes.getImageUrl(), clothes.get(0).getImageUrl());
    }
}
