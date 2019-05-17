package com.video.service;

import com. video.pojo.Users;
import com. video.utils.PagedResult;

public interface UsersService {

	PagedResult queryUsers(Users user, Integer page, Integer pageSize);

	Boolean setIsActive(String id);

	Boolean updateUser(Users users);
}
