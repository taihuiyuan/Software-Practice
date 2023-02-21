package fudan.se.myWardrobe;

import fudan.se.myWardrobe.entity.*;
import fudan.se.myWardrobe.repository.UserRepository;
import fudan.se.myWardrobe.repository.WardrobeRepository;
import fudan.se.myWardrobe.service.MyWardrobe;
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
public class LocationTrackingTest {
    @Resource
    private MyWardrobe myWardrobe;

    @Resource
    private UserRepository userRepository;

    @Resource
    private WardrobeRepository wardrobeRepository;

    Wardrobe wardrobe;

    String[] seasons = {"春天", "夏天","秋天","冬天"};
    String[] colors = {"黑色系","白色系","灰色系","裸色系","红色系","橙色系","黄色系","绿色系","蓝色系","紫色系","其它"};

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
    }

    @Test
    public void test_track_location(){
        List<Category> categories = myWardrobe.getCategories();//默认有一个未分类
        List<Location> locations = myWardrobe.getLocations();//默认有四个：默认/卧室衣柜/衣帽间/鞋柜
        Clothes clothes1 = new Clothes("http:127.0.0.1/api/file/1.png",
                categories.get(0).getName(),
                colors[0],//{"黑色系","白色系","灰色系","裸色系","红色系","橙色系","黄色系","绿色系","蓝色系","紫色系","其它"}
                seasons[0],//{"春天", "夏天","秋天","冬天"}
                100,
                locations.get(0).getName(),
                "test");

        myWardrobe.addClothes(clothes1);
        List<LocationHistory> histories = myWardrobe.getLocationHistory(myWardrobe.getClothes(0).getId());
        Assert.assertEquals(1, histories.size());
        Assert.assertEquals(locations.get(0).getName(),histories.get(0).getLocation());

        myWardrobe.modifyClothes(myWardrobe.getClothes(0).getId(), clothes1.getCategoryName(), clothes1.getColor(), clothes1.getSeason(), clothes1.getPrice(), locations.get(1).getName(), clothes1.getNote());
        histories = myWardrobe.getLocationHistory(myWardrobe.getClothes(0).getId());
        Assert.assertEquals(2, histories.size());
        Assert.assertEquals(locations.get(0).getName(),histories.get(0).getLocation());
        Assert.assertEquals(locations.get(1).getName(),histories.get(1).getLocation());
    }
}
