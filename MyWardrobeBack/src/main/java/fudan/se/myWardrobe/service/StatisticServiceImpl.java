package fudan.se.myWardrobe.service;

import fudan.se.myWardrobe.controller.dto.Statistic;
import fudan.se.myWardrobe.controller.dto.StatisticDTO;
import fudan.se.myWardrobe.entity.*;
import fudan.se.myWardrobe.exception.BaseException;
import fudan.se.myWardrobe.repository.ClothesRepository;
import fudan.se.myWardrobe.repository.UserRepository;
import fudan.se.myWardrobe.repository.WardrobeClothesRepository;
import fudan.se.myWardrobe.repository.WardrobeRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService{

    @Resource
    private UserRepository userRepository;

    @Resource
    private WardrobeRepository wardrobeRepository;

    @Resource
    private MyWardrobe myWardrobe;

    Wardrobe wardrobe;

    User user;

    @Resource
    private CategoryService categoryService;

    @Resource
    private ClothesService clothesService;

    @Resource
    private LocationService locationService;

    @Resource
    private ClothesRepository clothesRepository;

    @Resource
    private WardrobeClothesRepository wardrobeClothesRepository;

    private void init(String username){
        User user  = userRepository.findByUsername(username);
        if(user == null){
            throw new BaseException("the user not exit");
        }
        if (wardrobeRepository.findByUser(user) == null) {
            Wardrobe wardrobe = new Wardrobe(user);
            wardrobeRepository.save(wardrobe);
        }
        this.wardrobe = wardrobeRepository.findByUser(user);
        this.user = user;
    }

    public StatisticDTO getByCategory(String username){
        init(username);
        List<Category> categories = categoryService.getAllCategories(username).getCategories();
        List<Statistic> statisticList= new ArrayList<>();
        for (Category category : categories) {
            Statistic statistic = new Statistic();
            statistic.setName(category.getName());
            statistic.setNumber(clothesService.getClothesByCategory(username, category.getId()).getClothes().size());
            statisticList.add(statistic);
        }
        StatisticDTO statisticDTO = new StatisticDTO();
        statisticDTO.setResult(statisticList);
        return statisticDTO;
    }


    public StatisticDTO getByLocation(String username){
        init(username);
        List<Location> locations = locationService.getLocation(username).getLocations();
        List<Statistic> statisticList= new ArrayList<>();
        for (Location location : locations) {
            Statistic statistic = new Statistic();
            statistic.setName(location.getName());
            List<Clothes> clothesList = myWardrobe.getAllByLocation(this.user,location.getName());
            statistic.setNumber(clothesList.size());
            statisticList.add(statistic);
        }
        StatisticDTO statisticDTO = new StatisticDTO();
        statisticDTO.setResult(statisticList);
        return statisticDTO;
    }

    public StatisticDTO getBySeason(String username){
        init(username);
        List<String> seasons = new ArrayList<>();
        seasons.add("春天");
        seasons.add("夏天");
        seasons.add("秋天");
        seasons.add("冬天");
        List<Statistic> statisticList= new ArrayList<>();
        for (String season : seasons) {
            Statistic statistic = new Statistic();
            statistic.setName(season);
            List<Clothes> clothesList = myWardrobe.getAllBySeason(this.user,season);
            statistic.setNumber(clothesList.size());
            statisticList.add(statistic);
        }
        StatisticDTO statisticDTO = new StatisticDTO();
        statisticDTO.setResult(statisticList);
        return statisticDTO;
    }

    public StatisticDTO getByPrice(String username){
        init(username);
        List<Statistic> statisticList= new ArrayList<>();
        statisticList.add(new Statistic("低于50",0));
        statisticList.add(new Statistic("50-100",0));
        statisticList.add(new Statistic("100-200",0));
        statisticList.add(new Statistic("200-500",0));
        statisticList.add(new Statistic("500-1000",0));
        statisticList.add(new Statistic("大于1000",0));

        List<Clothes> clothesList = myWardrobe.getAllClothes();
        for (Clothes clothes:clothesList) {
            Double price = clothes.getPrice();
            int number = 0;
            if(price < 50){
                number = statisticList.get(0).getNumber();
                statisticList.get(0).setNumber(number+1);
            }else if(price<100){
                number = statisticList.get(1).getNumber();
                statisticList.get(1).setNumber(number+1);
            }else if(price<200){
                number = statisticList.get(2).getNumber();
                statisticList.get(2).setNumber(number+1);
            }else if(price<500){
                number = statisticList.get(3).getNumber();
                statisticList.get(3).setNumber(number+1);
            }else if(price<1000){
                number = statisticList.get(4).getNumber();
                statisticList.get(4).setNumber(number+1);
            }else{
                number = statisticList.get(5).getNumber();
                statisticList.get(5).setNumber(number+1);
            }
        }
        StatisticDTO statisticDTO = new StatisticDTO();
        statisticDTO.setResult(statisticList);
        return statisticDTO;
    }

}
