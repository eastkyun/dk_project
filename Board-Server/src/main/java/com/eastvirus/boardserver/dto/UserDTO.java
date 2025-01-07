package com.eastvirus.boardserver.dto;

import lombok.*;

import java.util.Date;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class UserDTO {

    public static boolean hasNullDataBeforeSignup(UserDTO userDTO) {
        return userDTO.getUserId() == null || userDTO.getPassword() == null
                || userDTO.getNickName() == null;
    }

    public enum Status {
        DEFAULT, ADMIN, DELETED
    }
    private int id;
    private String userId;
    private String password;
    private String nickName;
    private boolean isAdmin;
    private Date createTime;
    private boolean isWithDraw;
    private Status status;
    private Date updateTime;

}
