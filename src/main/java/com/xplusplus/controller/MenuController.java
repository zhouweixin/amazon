package com.xplusplus.controller;

import com.xplusplus.domain.Menu;
import com.xplusplus.domain.Result;
import com.xplusplus.domain.SpiderConfig;
import com.xplusplus.exception.AmazonException;
import com.xplusplus.exception.EnumException;
import com.xplusplus.processor.MenuProcessor;
import com.xplusplus.service.MenuService;
import com.xplusplus.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import us.codecraft.webmagic.Spider;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 11:19 2018/4/3
 * @Modified By:
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * 新增
     *
     * @param menu
     * @param bindingResult
     * @return
     */
    @RequestMapping("/add")
    public Result<Menu> add(@Valid Menu menu, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().toString());
        }

        if (menuService.findByUrl(menu.getUrl()) != null) {
            return ResultUtil.error(new AmazonException(EnumException.ADD_FAILED_EXIST));
        }

        return ResultUtil.success(menuService.save(menu));
    }

    /**
     * 更新
     *
     * @param menu
     * @param bindingResult
     * @return
     */
    @RequestMapping("/update")
    public Result<Menu> update(@Valid Menu menu, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().toString());
        }

        if (menuService.findOne(menu.getId()) != null) {
            return ResultUtil.error(new AmazonException(EnumException.ADD_FAILED_EXIST));
        }

        return ResultUtil.success(menuService.save(menu));
    }

    /**
     * 通过id删除
     *
     * @param id
     */
    @RequestMapping("/deleteById")
    public Result<Object> deleteById(@RequestParam(name = "id") Integer id) {
        menuService.delete(id);
        return ResultUtil.success();
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @RequestMapping("/getById")
    public Result<Menu> getById(@RequestParam(name = "id") Integer id) {
        return ResultUtil.success(menuService.findOne(id));
    }

    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping("/getAll")
    public Result<List<Menu>> getAll() {
        return ResultUtil.success(menuService.findAll());
    }

    /**
     * 通过分页查询所有
     *
     * @param page 当前页,从0开始,默认是0
     * @param size 每页的记录数,默认是10
     * @param sort 排序的字段名,默认是id
     * @param asc  排序的方式,0是减序,1是增序,默认是增序
     * @return
     */
    @RequestMapping(value = "/getAllByPage")
    public Result<Page<Menu>> getAllByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size,
                                           @RequestParam(value = "sort", defaultValue = "id") String sort,
                                           @RequestParam(value = "asc", defaultValue = "1") Integer asc) {
        return ResultUtil.success(menuService.findAllByPage(page, size, sort, asc));
    }

    /**
     * 通过分页查询所有
     *
     * @param name 名称
     * @param page 当前页,从0开始,默认是0
     * @param size 每页的记录数,默认是10
     * @param sort 排序的字段名,默认是code
     * @param asc  排序的方式,0是减序,1是增序,默认是增序
     * @return
     */
    @RequestMapping(value = "/getAllByLikeNameByPage")
    public Result<Page<Menu>> findAllByLikeNameByPage(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "asc", defaultValue = "1") Integer asc) {
        return ResultUtil.success(menuService.findAllByLikeNameByPage(name, page, size, sort, asc));
    }

    /**
     * 通过id批量查询
     *
     * @param ids 名称
     * @return
     */
    @RequestMapping(value = "/getAllByBatchId")
    public Result<List<Menu>> getAllByBatchId(@RequestParam(name = "id") List<Integer> ids) {
        return ResultUtil.success(menuService.findAllByBatchId(ids));
    }


    /**
     * 通过id批量查询-分页
     *
     * @param ids
     * @param page
     * @param size
     * @param sort
     * @param asc
     * @return
     */
    @RequestMapping(value = "/getAllByBatchIdByPage")
    public Result<Page<Menu>> getAllByBatchIdByPage(
            @RequestParam(value = "id") Set<Integer> ids,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "asc", defaultValue = "1") Integer asc) {
        return ResultUtil.success(menuService.findAllByBatchIdByPage(ids, page,
                size, sort, asc));
    }

    /**
     * 爬取菜单
     *
     * @param spiderConfig
     * @return
     */
    @RequestMapping(value = "/startCrawl")
    public Result<Object> startCrawl(SpiderConfig spiderConfig) {

        Spider.Status status = MenuProcessor.getSpiderStatus();
        if (status != null && (status == Spider.Status.Init || status == Spider.Status.Running)) {
            return ResultUtil.error(new AmazonException(EnumException.SPIDER_START_FAILED_RUNNING));
        }

        if (spiderConfig.getStartUrl() == null || spiderConfig.getStartUrl().equals("")) {
            return ResultUtil.error(new AmazonException(EnumException.START_URL_NULL));
        }

        MenuProcessor.runSpider(spiderConfig);

        return ResultUtil.success();
    }

    /**
     * 爬取菜单
     *
     * @param spiderConfig
     * @return
     */
    @RequestMapping(value = "/enableSchedule")
    public Result<Object> enableSchedule(SpiderConfig spiderConfig) {

        if (spiderConfig.getStartUrl() == null || spiderConfig.getStartUrl().equals("")) {
            return ResultUtil.error(new AmazonException(EnumException.SCHEDULE_RUN_FAILED_START_URL_NULL));
        }

        MenuProcessor.enableSchedule(spiderConfig);

        return ResultUtil.success();
    }

    /**
     * 结束定时器
     *
     * @return
     */
    @RequestMapping(value = "/stopSchedule")
    public Result<Object> stopSchedule() {
        MenuProcessor.stopSchedule();
        return ResultUtil.success();
    }

    /**
     * 结束爬取
     *
     * @return
     */
    @RequestMapping(value = "/stopCrawl")
    public Result<Object> stopCrawlMenu() {
        MenuProcessor.stopSpider();
        return ResultUtil.success();
    }

    /**
     * 查询爬虫状态
     *
     * @return
     */
    @RequestMapping(value = "/getSpiderStatus")
    public Result<Object> getSpiderStatus() {
        Spider.Status status = MenuProcessor.getSpiderStatus();
        if (status == null || status == Spider.Status.Stopped) {
            return ResultUtil.success(new AmazonException(EnumException.NOT_RUNNING_OR_STOPED));
        } else if (status == Spider.Status.Init) {
            return ResultUtil.success(new AmazonException(EnumException.INIT));
        } else if (status == Spider.Status.Running) {
            return ResultUtil.success(new AmazonException(EnumException.RUNNING));
        }

        return ResultUtil.error(new AmazonException(EnumException.UNKOWN_ERROR));
    }

    /**
     * 通过id查询所有儿子节点
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getSonMenuById")
    public Result<List<Menu>> getSonMenuByParentId(@RequestParam(name = "id") Integer id){
        Menu parentMenu = new Menu();
        parentMenu.setId(id);
        return ResultUtil.success(menuService.findByParentId(parentMenu));
    }
}
