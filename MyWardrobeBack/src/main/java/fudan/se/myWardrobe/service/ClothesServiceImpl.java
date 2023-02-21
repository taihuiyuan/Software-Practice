package fudan.se.myWardrobe.service;

import fudan.se.myWardrobe.controller.dto.ClothesDTO;
import fudan.se.myWardrobe.controller.dto.History;
import fudan.se.myWardrobe.controller.dto.HistoryDTO;
import fudan.se.myWardrobe.controller.dto.SingleClothesDTO;
import fudan.se.myWardrobe.controller.request.ClothesRequest;
import fudan.se.myWardrobe.controller.request.EditClothesRequest;
import fudan.se.myWardrobe.controller.request.SelectRequest;
import fudan.se.myWardrobe.entity.Category;
import fudan.se.myWardrobe.entity.Clothes;
import fudan.se.myWardrobe.entity.LocationHistory;
import fudan.se.myWardrobe.entity.User;
import fudan.se.myWardrobe.exception.BaseException;
import fudan.se.myWardrobe.repository.CategoryRepository;
import fudan.se.myWardrobe.repository.ClothesRepository;
import fudan.se.myWardrobe.repository.UserRepository;
import fudan.se.myWardrobe.repository.WardrobeRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClothesServiceImpl implements ClothesService{

    @Resource
    private UserRepository userRepository;

    @Resource
    private MyWardrobe myWardrobe;

    @Override
    public void addClothes(ClothesRequest clothesRequest) {
        String username = clothesRequest.getUsername();
        String imageUrl = clothesRequest.getImageUrl();
        String categoryName = clothesRequest.getCategoryName();
        String color = clothesRequest.getColor();
        String season = clothesRequest.getSeason();
        double price = Double.parseDouble(clothesRequest.getPrice());
        String location = clothesRequest.getLocation();
        String note = clothesRequest.getNote();

        if (username == null || username.equals("")){
            throw new BaseException("the username is empty");
        }
        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        Clothes clothes = new Clothes(imageUrl, categoryName, color, season,price, location, note);
        myWardrobe.addClothes(clothes);
    }

    @Override
    public ClothesDTO getClothesByCategory(String username, int categoryID) {
        if (username == null || username.equals("")){
            throw new BaseException("the username is empty");
        }
        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        List<Clothes> clothes = myWardrobe.getClothesByCategory(categoryID);

        ClothesDTO clothesDTO = new ClothesDTO();
        clothesDTO.setClothes(clothes);

        return clothesDTO;
    }

    @Override
    public void deleteClothes(String username, int clothesID) {
        if (username == null || username.equals("")){
            throw new BaseException("the username is empty");
        }
        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        myWardrobe.deleteClothes(clothesID);
    }

    @Override
    public void modifyClothes(EditClothesRequest request) {
        String username = request.getUsername();
        int clothesID = request.getClothesID();
        String categoryName = request.getCategoryName();
        String color = request.getColor();
        String season = request.getSeason();
        double price = request.getPrice();
        String location = request.getLocation();
        String note = request.getNote();

        if (username == null || username.equals("")){
            throw new BaseException("the username is empty");
        }
        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        myWardrobe.modifyClothes(clothesID, categoryName, color, season,price, location, note);
    }

    @Override
    public ClothesDTO searchByAttribute(SelectRequest request) {
        String username = request.getUsername();
        String categoryName = request.getCategoryName();
        String color = request.getColor();
        String season = request.getSeason();
        String location = request.getLocation();
        String priceRange = request.getPriceRange();

        if (username == null || username.equals("")){
            throw new BaseException("the username is empty");
        }
        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        List<Clothes> clothes = myWardrobe.searchByAttribute(categoryName, season, color, priceRange, location);

        ClothesDTO clothesDTO = new ClothesDTO();
        clothesDTO.setClothes(clothes);

        return clothesDTO;
    }

    @Override
    public ClothesDTO searchByKeyword(String username, String keyword) {
        if (username == null || username.equals("")){
            throw new BaseException("the username is empty");
        }
        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        List<Clothes> clothes = myWardrobe.searchByKeyword(keyword);

        ClothesDTO clothesDTO = new ClothesDTO();
        clothesDTO.setClothes(clothes);

        return clothesDTO;
    }

    @Override
    public HistoryDTO getLocationHistory(String username, int clothesID) {
        if (username == null || username.equals("")){
            throw new BaseException("the username is empty");
        }
        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        List<LocationHistory> locationHistories = myWardrobe.getLocationHistory(clothesID);

        List<History> histories = new ArrayList<>();
        for (LocationHistory h: locationHistories){
            History history = new History();
            history.setLocation(h.getLocation());
            history.setTime(h.getTime());
            histories.add(history);
        }
        return new HistoryDTO(histories);
    }

    @Override
    public ClothesDTO getAllClothes(String username) {
        if (username == null || username.equals("")){
            throw new BaseException("the username is empty");
        }
        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        List<Clothes> clothes = myWardrobe.getAllClothes();
        return new ClothesDTO(clothes);
    }

    @Override
    public SingleClothesDTO getClothesByID(String username, int clothesID) {
        if (username == null || username.equals("")){
            throw new BaseException("the username is empty");
        }
        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        Clothes c = myWardrobe.getClothesByID(clothesID);

        if (c == null){
            throw new BaseException("clothes不存在");
        }

        SingleClothesDTO singleClothesDTO = new SingleClothesDTO();
        singleClothesDTO.setClothes(c);

        return singleClothesDTO;
    }
}
