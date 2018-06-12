package com.xplusplus.service;

import com.xplusplus.domain.Menu;
import com.xplusplus.exception.AmazonException;
import com.xplusplus.exception.EnumException;
import com.xplusplus.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 11:14 2018/4/3
 * @Modified By:
 */
@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    /**
     * 新增
     *
     * @param menu
     * @return
     */
    public Menu save(Menu menu) {
        return menuRepository.save(menu);
    }

    /**
     * 更新
     *
     * @param menu
     * @return
     */
    public Menu update(Menu menu) {

        if (menu.getUrl() == null || menu.getUrl().equals("")) {
            return null;
        }

        Menu oldMenu = menuRepository.findMenuByUrl(menu.getUrl());

        // 如果不存在就新增
        if (oldMenu == null) {
            return menuRepository.save(menu);
        }

        // 如果存在就取出ID，然后更新
        menu.setId(oldMenu.getId());
        return menuRepository.save(menu);
    }

    /**
     * 批量更新
     *
     * @param menus
     */
    public void updateBatch(List<Menu> menus) {
        if(menus == null){
            return;
        }

        menus.forEach((menu) -> {
            // 设置父菜单
            menu.setParentMenu(findByUrl(menu.getParentUrl()));
            update(menu);
        });
    }

    /**
     * 批量添加
     *
     * @param menus
     */
    public void addBatch(List<Menu> menus) {

        if(menus == null){
            return;
        }

        menus.forEach((menu) -> {
            // 设置父菜单
            menu.setParentMenu(findByUrl(menu.getParentUrl()));

            if (menu.getUrl() == null || menu.getUrl().equals("")) {
                return;
            }

            if (menuRepository.findMenuByUrl(menu.getUrl()) == null) {

                // 判断是否有url变化的情况
                Menu oldMenu = menuRepository.findMenuByNameAndUrl(menu.getName(), menu.getUrl());
                if (oldMenu == null) {
                    menuRepository.save(menu);
                } else {
                    oldMenu.setUrl(menu.getUrl());
                    menuRepository.save(oldMenu);
                }
            }
        });
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Integer id) {
        menuRepository.delete(id);
    }

    /**
     * 通过id查找
     *
     * @param id
     * @return
     */
    public Menu findOne(Integer id) {
        return menuRepository.findOne(id);
    }

    /**
     * 通过url查找
     *
     * @param url
     * @return
     */
    public Menu findByUrl(String url) {
        return menuRepository.findMenuByUrl(url);
    }

    /**
     * 查找所有
     *
     * @return
     */
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    /**
     * 通过id批量查询
     *
     * @return
     */
    public List<Menu> findAllByBatchId(List<Integer> ids) {
        return menuRepository.findAll(ids);
    }

    /**
     * 通过id批量查询-分页
     *
     * @param page          当前页
     * @param size          每页的记录数
     * @param sortFieldName 排序的字段名
     * @param asc           增序还是减序
     * @return
     * @throws Exception
     */
    public Page<Menu> findAllByBatchIdByPage(Set<Integer> ids, Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断字段名是否存在
        try {
            Menu.class.getDeclaredField(sortFieldName);
        } catch (Exception e) {
            throw new AmazonException(EnumException.SORT_FIELD);
        }

        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortFieldName);
        }
        Pageable pageable = new PageRequest(page, size, sort);
        return menuRepository.findMenusByIdIn(ids, pageable);
    }


    /**
     * 通过分页查询所有
     *
     * @param page          当前页
     * @param size          每页的记录数
     * @param sortFieldName 排序的字段名
     * @param asc           增序还是减序
     * @return
     * @throws Exception
     */
    public Page<Menu> findAllByPage(Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断字段名是否存在
        try {
            Menu.class.getDeclaredField(sortFieldName);
        } catch (Exception e) {
            throw new AmazonException(EnumException.SORT_FIELD);
        }

        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortFieldName);
        }
        Pageable pageable = new PageRequest(page, size, sort);
        return menuRepository.findAll(pageable);
    }

    /**
     * 通过名称模糊查询
     *
     * @param name          名称
     * @param page          当前页
     * @param size          每页的记录数
     * @param sortFieldName 排序的字段名
     * @param asc           增序或减序
     * @return
     */
    public Page<Menu> findAllByLikeNameByPage(String name, Integer page, Integer size, String sortFieldName,
                                              Integer asc) {

        try {
            Menu.class.getDeclaredField(sortFieldName);
        } catch (Exception e) {
            // 排序的字段名不存在
            throw new AmazonException(EnumException.SORT_FIELD);
        }

        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortFieldName);
        }

        // 分页
        Pageable pageable = new PageRequest(page, size, sort);

        return menuRepository.findByNameLike("%" + name + "%", pageable);
    }

    public List<Menu> findByParentId(Menu parentMenu) {
        return menuRepository.findMenusByParentMenu(parentMenu);
    }
}
