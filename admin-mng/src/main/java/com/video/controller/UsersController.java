package com.video.controller;

import com.video.bean.AdminUser;
import com.video.pojo.User;
import com.video.pojo.Users;
import com.video.service.UsersService;
import com.video.utils.IMoocJSONResult;
import com.video.utils.PagedResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/showList")
    public String showList() {
        return "users/usersList";
    }

    @GetMapping("/list")
    @ResponseBody
    public PagedResult list(Users user, Integer page) {
        PagedResult result = usersService.queryUsers(user, page == null ? 1 : page, 10);
        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public IMoocJSONResult list(@RequestParam(name = "id") String id) {
        System.out.println(id);
        if (usersService.setIsActive(id)){
            return IMoocJSONResult.ok();
        }else {
            return IMoocJSONResult.errorMap("操作失败");
        }
    }


    @PostMapping("/update")
    @ResponseBody
    public IMoocJSONResult updateUser(@RequestBody Users users) {
        if (usersService.updateUser(users)){
            return IMoocJSONResult.ok();
        }else {
            return IMoocJSONResult.errorMap("操作失败");
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public IMoocJSONResult userLogin(
            @RequestBody User userLogin,
            HttpServletRequest request) {

        // TODO 模拟登陆
        if (StringUtils.isEmpty(userLogin.getUserName()) || StringUtils.isEmpty(userLogin.getPassWord())) {
            return IMoocJSONResult.errorMap("用户名和密码不能为空");
        } else if (userLogin.getUserName().equals("root") && userLogin.getPassWord().equals("root")) {
            String token = UUID.randomUUID().toString();
            AdminUser user = new AdminUser(userLogin.getUserName(), userLogin.getPassWord(), token);
            request.getSession().setAttribute("sessionUser", user);
            return IMoocJSONResult.ok();
        }

        return IMoocJSONResult.errorMsg("登陆失败，请重试...");
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("sessionUser");
        return "login";
    }

}
