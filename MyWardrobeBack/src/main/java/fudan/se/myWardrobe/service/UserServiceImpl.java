package fudan.se.myWardrobe.service;

import com.fasterxml.jackson.databind.ser.Serializers;
import fudan.se.myWardrobe.controller.dto.UserDTO;
import fudan.se.myWardrobe.controller.request.EditUserInfoRequest;
import fudan.se.myWardrobe.controller.request.LoginRequest;
import fudan.se.myWardrobe.controller.request.RegisterRequest;
import fudan.se.myWardrobe.entity.*;
import fudan.se.myWardrobe.exception.BaseException;
import fudan.se.myWardrobe.repository.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService{
    @Resource
    private UserRepository userRepository;

    @Resource
    private WardrobeRepository wardrobeRepository;

    @Resource
    private CategoryRepository categoryRepository;

    @Resource
    private LocationRepository locationRepository;

    @Override
    public int login(LoginRequest loginRequest){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userRepository.findByUsername(username);
        if(user == null){
            return -1;
        }else{
            if(password.equals(user.getPassword())){
                return 0;
            }else {
                return 1;
            }
        }
    }

    @Override
    public int register(RegisterRequest registerRequest){
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        String email = registerRequest.getEmail();

        User user = userRepository.findByUsername(username);
        if(user!=null){
            return -1;
        }else{
            //验证password的合法性
            if(Objects.equals(password, "") || password == null)
                return 1;

            user = new User(username,password,email);
            userRepository.save(user);

            Wardrobe wardrobe = new Wardrobe(user);
            wardrobeRepository.save(wardrobe);

            categoryRepository.save(new Category(wardrobe, "未分类"));

            locationRepository.save(new Location(wardrobe, "默认"));
            locationRepository.save(new Location(wardrobe, "卧室衣柜"));
            locationRepository.save(new Location(wardrobe, "衣帽间"));
            locationRepository.save(new Location(wardrobe, "鞋柜"));
            return 0;
        }
    }

    @Override
    public UserDTO getInfo(String username){
        if(username == null || username.equals("")){
            throw new BaseException("username is empty");
        }

        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new BaseException("user not exist");
        }

        return new UserDTO(user.getUsername(), user.getEmail());
    }

    @Override
    public void modifyInfo(EditUserInfoRequest request){
        String username = request.getUsername();
        String email = request.getEmail();

        if(username == null||username.equals("")){
            throw new BaseException("username is empty");
        }
        if(email == null||email.equals("")){
            throw new BaseException("email is empty");
        }
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new BaseException("user not exist");
        }

        user.setEmail(email);
        userRepository.save(user);
    }

    @Override
    public void modifyPwd(String username,String oldPwd,String newPwd) {
        if (username == null || username.equals("")) {
            throw new BaseException("username is empty");
        }
        if (oldPwd == null || oldPwd.equals("")) {
            throw new BaseException("oldPassword is empty");
        }
        if (newPwd == null || newPwd.equals("")) {
            throw new BaseException("newPassword is empty");
        }
        if (oldPwd.equals(newPwd)) {
            throw new BaseException("new password equals old password");
        }
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new BaseException("user not exist");
        }
        if (user.getPassword().equals(oldPwd)) {
            user.setPassword(newPwd);
            userRepository.save(user);
        } else {
            throw new BaseException("password is incorrect");
        }
    }
}
