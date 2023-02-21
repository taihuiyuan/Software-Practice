package fudan.se.myWardrobe.service;

import fudan.se.myWardrobe.controller.dto.LocationDTO;
import fudan.se.myWardrobe.entity.Location;
import fudan.se.myWardrobe.entity.User;
import fudan.se.myWardrobe.entity.Wardrobe;
import fudan.se.myWardrobe.exception.BaseException;
import fudan.se.myWardrobe.repository.LocationRepository;
import fudan.se.myWardrobe.repository.UserRepository;
import fudan.se.myWardrobe.repository.WardrobeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService{

    private Wardrobe wardrobe;

    @Resource
    private UserRepository userRepository;

    @Resource
    private WardrobeRepository wardrobeRepository;

    @Resource
    private LocationRepository locationRepository;

    @Resource
    private MyWardrobe myWardrobe;

    private void init(String username){
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new BaseException("the user not exit");
        }
        if (wardrobeRepository.findByUser(user) == null){
            Wardrobe wardrobe = new Wardrobe(user);
            wardrobeRepository.save(wardrobe);
        }

        this.wardrobe = wardrobeRepository.findByUser(user);
    }

    @Override
    public LocationDTO getLocation(String username){
        init(username);
        List<Location> list = locationRepository.findAllByWardrobe(wardrobe);
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setLocations(list);
        return locationDTO;
    }

    @Override
    public void addLocation(String username,String locationName){
        init(username);
        Location location = new Location();
        location.setName(locationName);
        location.setWardrobe(wardrobe);
        locationRepository.save(location);
    }

    @Override
    public void modifyLocation(String username,String locationName,String newLocationName){
        init(username);
        Location location = locationRepository.findByWardrobeAndName(wardrobe,locationName);
        if(location == null){
            throw new BaseException("location not exit");
        }
        location.setName(newLocationName);
        locationRepository.save(location);
    }

    @Override
    public void deleteLocation(String username,String locationName){
        if (username == null || username.equals("")){
            throw new BaseException("the username is empty");
        }

        User user = userRepository.findByUsername(username);
        myWardrobe.init(user);

        myWardrobe.deleteLocation(locationName);
    }

}
