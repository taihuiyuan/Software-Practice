package fudan.se.myWardrobe.controller;

import fudan.se.myWardrobe.controller.dto.UserDTO;
import fudan.se.myWardrobe.controller.request.EditUserInfoRequest;
import fudan.se.myWardrobe.controller.request.LoginRequest;
import fudan.se.myWardrobe.controller.request.ModifyPwdRequest;
import fudan.se.myWardrobe.controller.request.RegisterRequest;
import fudan.se.myWardrobe.controller.response.ErrorResponse;
import fudan.se.myWardrobe.controller.response.SuccessResponse;
import fudan.se.myWardrobe.entity.User;
import fudan.se.myWardrobe.exception.BaseException;
import fudan.se.myWardrobe.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        int result = userService.login(loginRequest);
        ErrorResponse errorResponse = new ErrorResponse("400", "密码错误");
        switch (result){
            //密码错误
            case 1:
                errorResponse = new ErrorResponse("400", "密码错误");
                break;
            //用户不存在
            case -1:
                errorResponse = new ErrorResponse("400", "用户不存在");
                break;
            //成功登录
            case 0:
                SuccessResponse successResponse = new SuccessResponse("登录成功");
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        int result = userService.register(registerRequest);
        ErrorResponse errorResponse = new ErrorResponse("400", "用户已存在");
        switch (result){
            //密码不合法
            case 1:
                errorResponse = new ErrorResponse("400", "密码不合法");
                break;
            //用户已存在
            case -1:
                errorResponse = new ErrorResponse("400", "用户已存在");
                break;
            //注册登录
            case 0:
                SuccessResponse successResponse = new SuccessResponse("注册成功");
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/user/getInformation")
    public UserDTO getInfo(@RequestParam("username") String username){
        return userService.getInfo(username);
    }

    @PostMapping("/user/modifyInformation")
    public ResponseEntity<?> modifyInfo(@RequestBody EditUserInfoRequest request){
        try{
            userService.modifyInfo(request);
            SuccessResponse successResponse = new SuccessResponse("modify successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }catch (BaseException e){
            ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/user/modifyPassword")
    public ResponseEntity<?> modifyPwd(@RequestBody ModifyPwdRequest request){
        try{
            userService.modifyPwd(request.getUsername(),request.getOldPassword(), request.getNewPassword());
            SuccessResponse successResponse = new SuccessResponse("modify password successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }catch (BaseException e){
            ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }


}
