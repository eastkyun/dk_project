package com.eastvirus.boardserver.controller;

import com.eastvirus.boardserver.dto.UserDTO;
import com.eastvirus.boardserver.dto.request.UserDeleteId;
import com.eastvirus.boardserver.dto.request.UserLoginRequest;
import com.eastvirus.boardserver.dto.request.UserUpdatePasswordRequest;
import com.eastvirus.boardserver.dto.response.LoginResponse;
import com.eastvirus.boardserver.dto.response.UserInfoResponse;
import com.eastvirus.boardserver.service.UserService;
import com.eastvirus.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@Log4j2
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody UserDTO userDTO) {
        if(UserDTO.hasNullDataBeforeSignup(userDTO)){
            throw new NullPointerException("회원가입 정보를 확인해주세요.");
        }
        userService.register(userDTO);
    }

    @PostMapping("sign-in")
    public HttpStatus login(@RequestBody UserLoginRequest userLoginRequest, HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        LoginResponse loginResponse = null;

        String id =  userLoginRequest.getUserId();
        String password = userLoginRequest.getPassword();

        UserDTO userInfo = userService.login(id, password);

        if (userInfo == null) {
            return HttpStatus.NOT_FOUND;
        }
        else if(userInfo != null) {
            loginResponse = LoginResponse.success(userInfo);
            if(userInfo.getStatus()==(UserDTO.Status.ADMIN)){
                SessionUtil.setLoginAdminId(session, userInfo.getUserId());
            }else{
                SessionUtil.setLoginMemberId(session, userInfo.getUserId());
            }
            responseEntity = new ResponseEntity<>(loginResponse, HttpStatus.OK);
            System.out.println(responseEntity.getBody());
        } else {
            throw new RuntimeException("login fail.");
        }
        return HttpStatus.OK;
    }

    @GetMapping("my-info")
    public UserInfoResponse memberInfo(HttpSession session) {
        String memberId = SessionUtil.getLoginMemberId(session);
        if (memberId == null) {
            memberId = SessionUtil.getLoginAdminId(session);
        }
        UserDTO memberInfo = userService.getUserInfo(memberId);
        return new UserInfoResponse(memberInfo);
    }

    @PostMapping("logout")
    public void logout(HttpSession session) {
        SessionUtil.clear(session);
    }

    @PostMapping("change-password")
    public ResponseEntity<LoginResponse> changePassword(@RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest,
                                                        HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String memberId = SessionUtil.getLoginMemberId(session);
        String beforePassword = userUpdatePasswordRequest.getBeforePassword();
        String afterPassword = userUpdatePasswordRequest.getAfterPassword();

        try {
            userService.updatePassword(memberId, beforePassword, afterPassword);
            responseEntity = ResponseEntity.ok(new ResponseEntity<LoginResponse>(HttpStatus.OK).getBody());
        }catch (Exception e) {
            log.error("update password 실패",e);
            responseEntity = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
        }
        return  responseEntity;
    }

    @PostMapping("delete-user")
    public ResponseEntity<LoginResponse> deleteId(@RequestBody UserDeleteId userDeleteId,
                                                  HttpSession session){
        ResponseEntity<LoginResponse> responseEntity = null;
        String memberId = SessionUtil.getLoginMemberId(session);

        try {
            userService.deleteId(memberId, userDeleteId.getPassword());
            responseEntity = ResponseEntity.ok(new ResponseEntity<LoginResponse>(HttpStatus.OK).getBody());
        }catch (Exception e) {
            log.error("delete id 실패");
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return  responseEntity;
    }
}
