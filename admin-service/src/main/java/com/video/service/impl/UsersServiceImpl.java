package com.video.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.video.mapper.UsersMapper;
import com.video.pojo.Users;
import com.video.service.UsersService;
import com.video.utils.PagedResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

import tk.mybatis.mapper.entity.Condition;

@Service
public class UsersServiceImpl implements UsersService {


    @Resource
    private UsersMapper userMapper;

    @Override
    public PagedResult queryUsers(Users user, Integer page, Integer pageSize) {

        String username = "";
        String nickname = "";
        if (user != null) {
            username = user.getUsername();
            nickname = user.getNickname();
        }

        PageHelper.startPage(page, pageSize);

        Condition condition = new Condition(Users.class);
        Condition.Criteria criteria = condition.createCriteria();


        if (!StringUtils.isEmpty(username)) {

            criteria.andLike("username", "%" + username + "%");
        }
        if (!StringUtils.isEmpty(nickname)) {
            criteria.andLike("nickname", "%" + nickname + "%");
        }
        List<Users> userList = userMapper.selectByCondition(condition);

        PageInfo<Users> pageList = new PageInfo<Users>(userList);

        PagedResult grid = new PagedResult();
        grid.setTotal(pageList.getPages());
        grid.setRows(userList);
        grid.setPage(page);
        grid.setRecords(pageList.getTotal());

        return grid;
    }

    @Override
    public Boolean setIsActive(String id) {
        return userMapper.setIsActive(id) == 1;
    }

    @Override
    public Boolean updateUser(Users users) {
        return userMapper.updateByPrimaryKey(users)==1;
    }
}
