package com.xplusplus.service;

import com.xplusplus.domain.Result;
import com.xplusplus.domain.User;
import com.xplusplus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 14:30 2018/4/26
 * @Modified By:
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        if (user != null && user.getPassword() != null) {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        return userRepository.save(user);
    }

    public void delete(String id) {
        userRepository.delete(id);
    }

    public User findOne(String id) {
        return userRepository.findOne(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User login(String id, String password) {
        if (password != null) {
            return userRepository.findUserByIdAndPassword(id, DigestUtils.md5DigestAsHex(password.getBytes()));
        }
        return null;
    }

    @Transactional
    public int updateName(String id, String name){
        return userRepository.updateName(name, id);
    }

    @Transactional
    public int updatePassword(String id, String oldPassword, String newPassword){
        return userRepository.updatePassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()), id, DigestUtils.md5DigestAsHex(oldPassword.getBytes()));
    }
}
