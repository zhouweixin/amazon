package com.xplusplus.controller;

import com.xplusplus.adapter.WebSecurityConfig;
import com.xplusplus.domain.Result;
import com.xplusplus.domain.User;
import com.xplusplus.exception.AmazonException;
import com.xplusplus.exception.EnumException;
import com.xplusplus.service.UserService;
import com.xplusplus.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 14:42 2018/4/26
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 新增
     * @param user
     * @return
     */
    @RequestMapping(value = "/add")
    public Result<User> add(User user){
        if(user.getId() == null || user.getPassword() == null || user.getPassword().equals("")){
            return ResultUtil.error(new AmazonException(EnumException.USER_ID_OR_PASSWORD_NULL));
        }

        if(userService.findOne(user.getId()) != null){
            return ResultUtil.error(new AmazonException(EnumException.ADD_FAILED_EXIST));
        }

        return ResultUtil.success(userService.save(user));
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteById")
    public Result<Object> delete(@RequestParam(name = "id", defaultValue = "") String id){
        userService.delete(id);
        return ResultUtil.success();
    }

    /**
     * 更新姓名
     *
     * @param id
     * @param name
     * @return
     */
    @RequestMapping(value = "/updateNameById")
    public Result<Object> updateName(@RequestParam(name = "id", defaultValue = "") String id, @RequestParam(name = "name", defaultValue = "") String name){
        int n = userService.updateName(id, name);
        if(n > 0){
            return ResultUtil.success();
        }
        return ResultUtil.error(new AmazonException(EnumException.UNKOWN_ERROR));
    }

    /**
     * 更新密码
     *
     * @param id
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @RequestMapping(value = "/updatePassword")
    public Result<Object> updatePassword(@RequestParam(name = "id", defaultValue = "") String id,
                                         @RequestParam(name = "oldPassword", defaultValue = "") String oldPassword,
                                         @RequestParam(name = "newPassword", defaultValue = "") String newPassword){
        int n = userService.updatePassword(id, oldPassword, newPassword);
        if(n > 0){
            return ResultUtil.success();
        }
        return ResultUtil.error(new AmazonException(EnumException.UNKOWN_ERROR));
    }

    /**
     * 登录
     *
     * @param id
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "/login")
    public Result<User> login(@RequestParam(name = "id", defaultValue = "") String id,
                              @RequestParam(name = "password", defaultValue = "") String password,
                              HttpSession session){

        if(id.equals("")){
            return ResultUtil.error(new AmazonException(EnumException.LOGIN_FAILED_ID_NULL));
        }

        if(password.equals("")){
            return ResultUtil.error(new AmazonException(EnumException.LOGIN_FAILED_PASSWORD_NULL));
        }

        User user = userService.login(id, password);
        if(user == null){
            return ResultUtil.error(new AmazonException(EnumException.LOGIN_FAILED_ID_OR_PASSWORD_ERROR));
        }

        session.setAttribute(WebSecurityConfig.SESSION_USER, user);

        return ResultUtil.success(user);
    }

    /**
     * 注销
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout")
    public Result<Object> logout(HttpSession session){
        session.removeAttribute(WebSecurityConfig.SESSION_USER);
        return ResultUtil.success();
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getById")
    public Result<User> getById(@RequestParam(name = "id", defaultValue = "") String id){
        return ResultUtil.success(userService.findOne(id));
    }

    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping(value = "/getAll")
    public Result<List<User>> getAll(){
        return ResultUtil.success(userService.findAll());
    }
}
