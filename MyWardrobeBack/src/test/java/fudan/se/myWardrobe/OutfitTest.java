package fudan.se.myWardrobe;

import fudan.se.myWardrobe.controller.dto.GetOutfit;
import fudan.se.myWardrobe.controller.dto.OutfitDTO;
import fudan.se.myWardrobe.entity.*;
import fudan.se.myWardrobe.repository.UserRepository;
import fudan.se.myWardrobe.repository.WardrobeRepository;
import fudan.se.myWardrobe.service.MyWardrobe;
import fudan.se.myWardrobe.service.OutfitServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OutfitTest {
    @Resource
    private MyWardrobe myWardrobe;

    @Resource
    private UserRepository userRepository;

    @Resource
    private OutfitServiceImpl outfitService;

    @Resource
    private WardrobeRepository wardrobeRepository;

    Wardrobe wardrobe;

    List<Integer> clothes_id = new ArrayList<>();
    List<Integer> clothes_id2 = new ArrayList<>();
    List<Integer> clothes_ids = new ArrayList<>();

    String[] seasons = {"春天", "夏天", "秋天", "冬天"};
    String[] colors = {"黑色系", "白色系", "灰色系", "裸色系", "红色系", "橙色系", "黄色系", "绿色系", "蓝色系", "紫色系", "其它"};
    List<Category> categories;
    List<Location> locations;

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
        myWardrobe.clearOutfit();
        myWardrobe.clearLocation();
        categories = myWardrobe.getCategories();//默认有一个未分类
        locations = myWardrobe.getLocations();//默认有三个：卧室衣柜/衣帽间/鞋柜
        Clothes clothes1 = new Clothes("http:127.0.0.1/api/file/1.png",
                categories.get(0).getName(),
                colors[0],//{"黑色系","白色系","灰色系","裸色系","红色系","橙色系","黄色系","绿色系","蓝色系","紫色系","其它"}
                seasons[0],//{"春天", "夏天","秋天","冬天"}
                50,
                locations.get(0).getName(),
                "test");


        Clothes clothes2 = new Clothes("http:127.0.0.1/api/file/2.png",
                categories.get(0).getName(),
                colors[1],//{"黑色系","白色系","灰色系","裸色系","红色系","橙色系","黄色系","绿色系","蓝色系","紫色系","其它"}
                seasons[1],//{"春天", "夏天","秋天","冬天"}
                50,
                locations.get(1).getName(),
                "test");
        myWardrobe.addClothes(clothes1);
        myWardrobe.addClothes(clothes2);

        List<Clothes> clothes = myWardrobe.getAllClothes();
        clothes_id.add(clothes.get(0).getId());
        clothes_id2.add(clothes.get(1).getId());
        for (Clothes c: clothes){
            clothes_ids.add(c.getId());
        }
    }

    @Test
    public void test_add_outfit(){
        //添加穿搭outfit1,里面包括clothes1
        myWardrobe.addOutfit("outfit1", clothes_id);

        OutfitDTO outfitDTO = outfitService.getOutfits("username");
        GetOutfit outfit = outfitDTO.getOutfits().get(0);
        Assert.assertEquals(1, outfit.getClothes().size());
        Assert.assertEquals("outfit1", outfit.getOutfitName());
        Assert.assertEquals(clothes_id.get(0), outfit.getClothes().get(0).getId());
    }

    @Test
    public void test_delete_outfit(){
        myWardrobe.addOutfit("outfit1", clothes_id);
        myWardrobe.deleteOutfit("outfit1");
        Assert.assertEquals(0, myWardrobe.getOutfits().size());
    }

    @Test
    public void test_add_clothes_into_outfit(){
        //添加穿搭outfit1,里面包括clothes1
        myWardrobe.addOutfit("outfit1", clothes_id);
        //向穿搭outfit1内添加clothes2
        myWardrobe.addClothesIntoOutfit("outfit1",clothes_id2);

        OutfitDTO outfitDTO = outfitService.getOutfits("username");
        GetOutfit outfit = outfitDTO.getOutfits().get(0);
        Assert.assertEquals(2, outfit.getClothes().size());
        Assert.assertEquals("outfit1", outfit.getOutfitName());
        Assert.assertEquals(clothes_id.get(0), outfit.getClothes().get(0).getId());
        Assert.assertEquals(clothes_id2.get(0), outfit.getClothes().get(1).getId());
    }

    @Test
    public void test_delete_clothes_from_outfit(){
        //添加穿搭outfit1,里面包括clothes1、clothes2
        myWardrobe.addOutfit("outfit1", clothes_ids);
        //删除clothes2
        myWardrobe.deleteClothesFromOutfit("outfit1", clothes_id2);

        OutfitDTO outfitDTO = outfitService.getOutfits("username");
        GetOutfit outfit = outfitDTO.getOutfits().get(0);
        Assert.assertEquals(1, outfit.getClothes().size());
        Assert.assertEquals("outfit1", outfit.getOutfitName());
        Assert.assertEquals(clothes_id.get(0), outfit.getClothes().get(0).getId());
    }
}
