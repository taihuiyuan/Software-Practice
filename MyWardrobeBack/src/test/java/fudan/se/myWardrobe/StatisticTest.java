package fudan.se.myWardrobe;

import fudan.se.myWardrobe.controller.dto.Statistic;
import fudan.se.myWardrobe.entity.*;
import fudan.se.myWardrobe.repository.LocationRepository;
import fudan.se.myWardrobe.repository.UserRepository;
import fudan.se.myWardrobe.repository.WardrobeRepository;
import fudan.se.myWardrobe.service.MyWardrobe;
import fudan.se.myWardrobe.service.StatisticService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StatisticTest {

    @Resource
    private LocationRepository locationRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private WardrobeRepository wardrobeRepository;

    Wardrobe wardrobe;

    @Resource
    private MyWardrobe myWardrobe;

    @Resource
    private StatisticService statisticService;

    String[] seasons = {"春天", "夏天", "秋天", "冬天"};
    String[] colors = {"黑色系", "白色系", "灰色系", "裸色系", "红色系", "橙色系", "黄色系", "绿色系", "蓝色系", "紫色系", "其它"};
    List<Category> categories = new ArrayList<>();
    List<Location> locations = new ArrayList<>();

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
        myWardrobe.clearClothes();
        myWardrobe.clearLocation();

        categories = myWardrobe.getCategories();
        locations = myWardrobe.getLocations();

        Clothes clothes1 = new Clothes("http:127.0.0.1/api/file/1.png",
                categories.get(0).getName(),
                colors[0],//{"黑色系","白色系","灰色系","裸色系","红色系","橙色系","黄色系","绿色系","蓝色系","紫色系","其它"}
                seasons[0],//{"春天", "夏天","秋天","冬天"}
                75,
                locations.get(0).getName(),
                "test");
        Clothes clothes2 = new Clothes("http:127.0.0.1/api/file/2.png",
                categories.get(0).getName(),
                colors[1],//{"黑色系","白色系","灰色系","裸色系","红色系","橙色系","黄色系","绿色系","蓝色系","紫色系","其它"}
                seasons[1],//{"春天", "夏天","秋天","冬天"}
                250,
                locations.get(1).getName(),
                "test");
        Clothes clothes3 = new Clothes("http:127.0.0.1/api/file/3.png",
                "新分类",
                colors[2],//{"黑色系","白色系","灰色系","裸色系","红色系","橙色系","黄色系","绿色系","蓝色系","紫色系","其它"}
                seasons[2],//{"春天", "夏天","秋天","冬天"}
                10000,
                locations.get(2).getName(),
                "test");
        myWardrobe.addClothes(clothes1);
        myWardrobe.addClothes(clothes2);
        myWardrobe.addClothes(clothes3);
    }
    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }

    @Test
    public void test_category_statistic(){
        List<Statistic> list = statisticService.getByCategory("username").getResult();
        Assert.assertEquals(2,list.size());
        Assert.assertEquals("未分类",list.get(0).getName());
        Assert.assertEquals(2,list.get(0).getNumber());
        Assert.assertEquals("新分类",list.get(1).getName());
        Assert.assertEquals(1,list.get(1).getNumber());
    }

    @Test
    public void test_season_statistic(){
        List<Statistic> list = statisticService.getBySeason("username").getResult();
        Assert.assertEquals(4,list.size());
        Assert.assertEquals("春天",list.get(0).getName());
        Assert.assertEquals(1,list.get(0).getNumber());
        Assert.assertEquals("夏天",list.get(1).getName());
        Assert.assertEquals(1,list.get(1).getNumber());
        Assert.assertEquals("秋天",list.get(2).getName());
        Assert.assertEquals(1,list.get(2).getNumber());
    }

    @Test
    public void test_location_statistic(){
        List<Statistic> list = statisticService.getByLocation("username").getResult();
        Assert.assertEquals(4,list.size());
        Assert.assertEquals("默认",list.get(0).getName());
        Assert.assertEquals(1,list.get(0).getNumber());
        Assert.assertEquals("卧室衣柜",list.get(1).getName());
        Assert.assertEquals(1,list.get(1).getNumber());
        Assert.assertEquals("衣帽间",list.get(2).getName());
        Assert.assertEquals(1,list.get(2).getNumber());
        Assert.assertEquals("鞋柜",list.get(3).getName());
        Assert.assertEquals(0,list.get(3).getNumber());
    }

    @Test
    public void test_price_statistic(){
        List<Statistic> list = statisticService.getByPrice("username").getResult();
        System.out.println(list.toString());
        Assert.assertEquals(6,list.size());
        Assert.assertEquals("50-100",list.get(1).getName());
        Assert.assertEquals(1,list.get(1).getNumber());
        Assert.assertEquals("200-500",list.get(3).getName());
        Assert.assertEquals(1,list.get(3).getNumber());
        Assert.assertEquals("大于1000",list.get(5).getName());
        Assert.assertEquals(1,list.get(5).getNumber());
    }
}
