package com.xplusplus.repository;

import com.xplusplus.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 14:26 2018/4/26
 * @Modified By:
 */
public interface UserRepository extends JpaRepository<User, String> {
    public User findUserByIdAndPassword(String id, String password);

    @Modifying
    @Query(value = "update User set name=?1 where id=?2")
    public int updateName(String name, String id);

    @Modifying
    @Query(value = "update User set password=?1 where id=?2 and password=?3")
    public int updatePassword(String newPassword, String id, String oldPassword);
}
