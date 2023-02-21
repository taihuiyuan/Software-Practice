package fudan.se.myWardrobe.service;

import fudan.se.myWardrobe.controller.dto.UserDTO;
import fudan.se.myWardrobe.controller.request.EditUserInfoRequest;
import fudan.se.myWardrobe.controller.request.LoginRequest;
import fudan.se.myWardrobe.controller.request.RegisterRequest;
import fudan.se.myWardrobe.entity.User;

public interface UserService {
    public int login(LoginRequest loginRequest);
    public int register(RegisterRequest registerRequest);
    public UserDTO getInfo(String username);
    public void modifyInfo(EditUserInfoRequest request);
    public void modifyPwd(String username,String oldPwd,String newPwd);
}
