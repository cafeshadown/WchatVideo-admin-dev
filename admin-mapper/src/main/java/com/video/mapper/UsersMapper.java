package com.video.mapper;

import com.video.pojo.Users;
import com.video.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UsersMapper extends MyMapper<Users> {
    int setIsActive(@Param("id") String id);
}