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
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class SelectTest {

    @Resource
    private UserRepository userRepository;

    @Resource
    private WardrobeRepository wardrobeRepository;

    @Resource
    private MyWardrobe myWardrobe;

    @Resource
    private CategoryRepository categoryRepository;

    String[] seasons = {"春天", "夏天", "秋天", "冬天"};
    String[] colors = {"黑色系", "白色系", "灰色系", "裸色系", "红色系", "橙色系", "黄色系", "绿色系", "蓝色系", "紫色系", "其它"};
    List<Category> categories;
    List<Location> locations;

    @Before
    public void init() {
        System.out.println("开始测试----------");
        User user = new User("username", "password", "email");
        if (userRepository.findByUsername("username") == null) {
            userRepository.save(user);
        }
        user = userRepository.findByUsername("username");

        myWardrobe.init(user);
        myWardrobe.clearClothes();
        myWardrobe.clearCategory();

        categories = myWardrobe.getCategories();//默认有一个未分类
        locations = myWardrobe.getLocations();//默认有三个：卧室衣柜/衣帽间/鞋柜
        Clothes clothes = new Clothes("http:127.0.0.1/api/file/1.png",
                categories.get(0).getName(),
                colors[0],//{"黑色系","白色系","灰色系","裸色系","红色系","橙色系","黄色系","绿色系","蓝色系","紫色系","其它"}
                seasons[0],//{"春天", "夏天","秋天","冬天"}
                50,
                locations.get(0).getName(),
                "test");

        myWardrobe.addClothes(clothes);
    }

    @After
    public void after() {
        System.out.println("测试结束----------");
    }

    @Test
    public void search_by_full_attribute() {
        List<Clothes> clothes = myWardrobe.searchByAttribute(categories.get(0).getName(), seasons[0], colors[0],
                "40,60", locations.get(0).getName());
        Assert.assertEquals(1, clothes.size());
        Assert.assertEquals(colors[0], clothes.get(0).getColor());
        Assert.assertEquals(categories.get(0).getName(), clothes.get(0).getCategoryName());
        Assert.assertEquals(seasons[0], clothes.get(0).getSeason());
        Assert.assertEquals(locations.get(0).getName(), clothes.get(0).getLocation());
        if (clothes.get(0).getPrice() < 40 || clothes.get(0).getPrice() > 60) {
            Assert.fail();
        }
    }
}
