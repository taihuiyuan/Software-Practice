package fudan.se.myWardrobe.service;

import fudan.se.myWardrobe.controller.dto.GetOutfit;
import fudan.se.myWardrobe.controller.dto.OutfitDTO;
import fudan.se.myWardrobe.entity.Clothes;
import fudan.se.myWardrobe.entity.Outfit;
import fudan.se.myWardrobe.entity.User;
import fudan.se.myWardrobe.exception.BaseException;
import fudan.se.myWardrobe.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OutfitServiceImpl implements OutfitService{
    @Resource
    private UserRepository userRepository;

    @Resource
    private MyWardrobe myWardrobe;

    @Override
    public void addOutfit(String username, String outfitName, List<Integer> clothes) {
        if (username == null || username.equals("")){
            throw new BaseException("the username is empty");
        }

        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        myWardrobe.addOutfit(outfitName, clothes);
    }

    @Override
    public OutfitDTO getOutfits(String username) {
        if (username == null || username.equals("")){
            throw new BaseException("the username is empty");
        }

        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        List<Outfit> outfits = myWardrobe.getOutfits();

        List<GetOutfit> getOutfits = new ArrayList<>();
        for (Outfit outfit: outfits){
            int flag = 0;
            for (GetOutfit o: getOutfits){
                if (Objects.equals(o.getOutfitName(), outfit.getOutfitName())){
                    flag = 1;
                    if (outfit.getClothes() != null){
                        o.getClothes().add(outfit.getClothes());
                    }
                }
            }

            if (flag == 0){
                List<Clothes> clothes = new ArrayList<>();
                if (outfit.getClothes() != null){
                    clothes.add(outfit.getClothes());
                }
                GetOutfit o = new GetOutfit(outfit.getOutfitName(), clothes);
                getOutfits.add(o);
            }
        }

        return new OutfitDTO(getOutfits);
    }

    @Override
    public void deleteOutfit(String username, String outfitName) {
        if (username == null || username.equals("")){
            throw new BaseException("the username is empty");
        }

        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        myWardrobe.deleteOutfit(outfitName);
    }

    @Override
    public void addClothesIntoOutfit(String username, String outfitName, List<Integer> clothes) {
        if (username == null || username.equals("")){
            throw new BaseException("the username is empty");
        }

        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        myWardrobe.addClothesIntoOutfit(outfitName, clothes);
    }

    @Override
    public void deleteClothesFromOutfit(String username, String outfitName, List<Integer> clothes) {
        if (username == null || username.equals("")){
            throw new BaseException("the username is empty");
        }

        if (outfitName == null || outfitName.equals("")){
            throw new BaseException("the outfitName is empty");
        }

        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        myWardrobe.deleteClothesFromOutfit(outfitName, clothes);
    }
}
