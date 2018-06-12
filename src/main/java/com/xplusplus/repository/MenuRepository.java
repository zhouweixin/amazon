package com.xplusplus.repository;

import com.xplusplus.domain.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: zhouweixin
 * @Description: 菜单
 * @Date: Created in 11:13 2018/4/3
 * @Modified By:
 */
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    /**
     * 通过名字查找
     *
     * @param url
     * @return
     */
    public Menu findMenuByUrl(String url);

    /**
     * 通过名称和url查询
     *
     * @param name
     * @param url
     * @return
     */
    public Menu findMenuByNameAndUrl(String name, String url);

    /**
     * 通过名称模糊查询-分页
     *
     * @param name
     * @param pageable
     * @return
     */
    public Page<Menu> findByNameLike(String name, Pageable pageable);

    /**
     * 通过id批量查询-分页
     *
     * @param ids
     * @param pageable
     * @return
     */
    public Page<Menu> findMenusByIdIn(Iterable<Integer> ids, Pageable pageable);

    /**
     * 通过爷菜单id查询菜单
     *
     * @param parentMenu
     * @return
     */
    public List<Menu> findMenusByParentMenu(Menu parentMenu);
}
