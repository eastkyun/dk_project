package com.eastvirus.boardserver.mapper;

import com.eastvirus.boardserver.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserProfileMapper {
    public UserDTO getUserProfile(@Param("id") String id);
    int insertUserProfile(@Param("id") int userId, @Param("password") String password) ;
    int updateUserProfile(@Param("id") int userId, @Param("password") String password) ;
    int deleteUserProfile(@Param("id") String id);
    public int register(UserDTO userDTO);
    public UserDTO findByIdAndPassword(@Param("id") String id, @Param("password") String password);
    int idCheck(@Param("id") String id);
    public int updatePassword(UserDTO userDTO);

}
