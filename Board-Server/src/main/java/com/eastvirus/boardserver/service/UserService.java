package com.eastvirus.boardserver.service;

import com.eastvirus.boardserver.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void register(UserDTO userDTO);
    UserDTO login(String id,String password);
    UserDTO getUserInfo(String id);

    boolean isDuplicatedId(String id);

    void updatePassword(String id, String beforePassword, String newPassword);
    void deleteId(String id, String password);
}
