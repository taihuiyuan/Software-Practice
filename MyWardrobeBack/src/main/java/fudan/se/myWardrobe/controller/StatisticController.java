package fudan.se.myWardrobe.controller;

import fudan.se.myWardrobe.controller.dto.StatisticDTO;
import fudan.se.myWardrobe.service.StatisticService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class StatisticController {

    @Resource
    private StatisticService statisticService;

    @GetMapping("/statistic/category")
    public StatisticDTO getByCategory(@RequestParam("username") String username){
        return statisticService.getByCategory(username);
    }

    @GetMapping("/statistic/location")
    public StatisticDTO getByLocation(@RequestParam("username") String username){
        return statisticService.getByLocation(username);
    }

    @GetMapping("/statistic/season")
    public StatisticDTO getBySeason(@RequestParam("username") String username){
        return statisticService.getBySeason(username);
    }

    @GetMapping("/statistic/priceRange")
    public StatisticDTO getByPrice(@RequestParam("username") String username){
        return statisticService.getByPrice(username);
    }

}
