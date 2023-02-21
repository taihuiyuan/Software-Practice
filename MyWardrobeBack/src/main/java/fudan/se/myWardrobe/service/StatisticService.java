package fudan.se.myWardrobe.service;

import fudan.se.myWardrobe.controller.dto.StatisticDTO;

public interface StatisticService {
    public StatisticDTO getByCategory(String username);
    public StatisticDTO getByLocation(String username);
    public StatisticDTO getBySeason(String username);
    public StatisticDTO getByPrice(String username);

}
