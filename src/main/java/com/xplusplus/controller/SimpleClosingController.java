package com.xplusplus.controller;

import com.xplusplus.domain.*;
import com.xplusplus.exception.AmazonException;
import com.xplusplus.exception.EnumException;
import com.xplusplus.processor.SimpleClosingProcessor;
import com.xplusplus.service.MenuService;
import com.xplusplus.service.SimpleClosingService;
import com.xplusplus.utils.DataStatisticsUtil;
import com.xplusplus.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import us.codecraft.webmagic.Spider;

import javax.validation.Valid;
import java.util.*;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 20:13 2018/4/4
 * @Modified By:
 */
@RestController
@RequestMapping("/simpleClosing")
public class SimpleClosingController {
    @Autowired
    private SimpleClosingService simpleClosingService;

    @Autowired
    private MenuService menuService;

    /**
     * 新增
     *
     * @param simpleClosing
     * @param bindingResult
     * @return
     */
    @RequestMapping("/add")
    public Result<SimpleClosing> add(@Valid SimpleClosing simpleClosing, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultUtil.error(bindingResult.getFieldError().toString());
        }

        if(simpleClosingService.findOnd(simpleClosing.getId()) != null){
            return ResultUtil.error(new AmazonException(EnumException.ADD_FAILED_EXIST));
        }

        return ResultUtil.success(simpleClosingService.save(simpleClosing));
    }

    /**
     * 新增
     *
     * @param simpleClosing
     * @param bindingResult
     * @return
     */
    @RequestMapping("/update")
    public Result<SimpleClosing> update(@Valid SimpleClosing simpleClosing, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultUtil.error(bindingResult.getFieldError().toString());
        }

        if(simpleClosingService.findOnd(simpleClosing.getId()) == null){
            return ResultUtil.error(new AmazonException(EnumException.UPDATE_FAILED_NOT_EXIST));
        }

        return ResultUtil.success(simpleClosingService.save(simpleClosing));
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteById")
    public Result<Object> delete(@RequestParam(name = "id") Long id){
        simpleClosingService.delete(id);
        return ResultUtil.success();
    }

    /**
     * 通过编码查询
     *
     * @param id
     * @return
     */
    @RequestMapping("/getById")
    public Result<SimpleClosing> getById(@RequestParam(name = "id") Long id){
        return ResultUtil.success(simpleClosingService.findOnd(id));
    }

    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping("/getAll")
    public Result<List<SimpleClosing>> getAll(){
        return ResultUtil.success(simpleClosingService.findAll());
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
    public Result<Page<SimpleClosing>> getAllByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                              @RequestParam(value = "size", defaultValue = "10") Integer size,
                                              @RequestParam(value = "sort", defaultValue = "id") String sort,
                                              @RequestParam(value = "asc", defaultValue = "1") Integer asc) {
        return ResultUtil.success(simpleClosingService.findAllByPage(page, size, sort, asc));
    }

    /**
     * 通过菜单Id和下载日期分页查询
     *
     * @param downloadDate 名称
     * @param page         当前页,从0开始,默认是0
     * @param size         每页的记录数,默认是10
     * @param sort         排序的字段名,默认是id
     * @param asc          排序的方式,0是减序,1是增序,默认是增序
     * @return
     */
    @RequestMapping(value = "/getByMenuIdAndDownloadDateByPage")
    public Result<Page<SimpleClosing>> getByDownloadDateByPage(
            @RequestParam(value = "menuId") Integer menuId,
            @RequestParam(value = "downloadDate", defaultValue = "") long downloadDate,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "asc", defaultValue = "1") Integer asc) {
        Menu menu = new Menu();
        menu.setId(menuId);
        return ResultUtil.success(simpleClosingService.findSimpleClosingsByMenuAndDownloadDate(menu, downloadDate, page, size, sort, asc));
    }

    /**
     * 通过menu和下载日期查询star2Num
     * @param menuId
     * @return
     */
    @RequestMapping(value = "/getStarNumByMenuIdAndDate1ToDate2")
    public Result<List<StarNum>> getStarNumByMenuId(@RequestParam(name = "menuId") Integer menuId,
                                                    @RequestParam(name = "startDate") long startDate,
                                                    @RequestParam(name = "endDate") long endDate){
        Menu menu = new Menu();
        menu.setId(menuId);
        List<SimpleClosing> simpleClosings = simpleClosingService.findSimpleClosingByMenu(menu, startDate, endDate);
        return ResultUtil.success(DataStatisticsUtil.computeSimpleClosingStarNum(simpleClosings));
    }

    /**
     * 爬取简单信息
     *
     * @param spiderConfig
     * @return
     */
    @RequestMapping(value = "/startCrawl")
    public Result<Object> startRunSpider(SpiderConfig spiderConfig) {

        if(spiderConfig.getMenus() == null){
            return ResultUtil.error(new AmazonException(EnumException.SPIDER_START_FAILED_MENUS_NULL));
        }

        Spider.Status status = SimpleClosingProcessor.getSpiderStatus();
        if (status != null && (status == Spider.Status.Init || status == Spider.Status.Running)) {
            return ResultUtil.error(new AmazonException(EnumException.SPIDER_START_FAILED_RUNNING));
        }

        // 取出菜单的id
        List<Integer> menuIds = new ArrayList<Integer>();
        spiderConfig.getMenus().forEach((menu) -> {
            if(menu.getId() != null) {
                menuIds.add(menu.getId());
            }
        });

        // 查询菜单信息
        spiderConfig.setMenus(menuService.findAllByBatchId(menuIds));

        // 判断选择的菜单是否为空
        if(spiderConfig.getMenus() == null || spiderConfig.getMenus().size() == 0){
            return ResultUtil.error(new AmazonException(EnumException.SPIDER_START_FAILED_MENUS_NULL));
        }

        // 开始爬取
        SimpleClosingProcessor.startCrawl(spiderConfig);

        return ResultUtil.success();
    }

    /**
     * 启动定时器
     *
     * @param spiderConfig
     * @return
     */
    @RequestMapping(value = "/enableSchedule")
    public Result<Object> enableSchedule(SpiderConfig spiderConfig) {

        if(spiderConfig.getMenus() == null){
            return ResultUtil.error(new AmazonException(EnumException.SCHEDULE_RUN_FAILED_MENUS_NULL));
        }

        // 取出菜单的id
        List<Integer> menuIds = new ArrayList<Integer>();
        spiderConfig.getMenus().forEach((menu) -> {
            if(menu.getId() != null) {
                menuIds.add(menu.getId());
            }
        });

        // 查询菜单信息
        spiderConfig.setMenus(menuService.findAllByBatchId(menuIds));

        // 判断选择的菜单是否为空
        if(spiderConfig.getMenus() == null || spiderConfig.getMenus().size() == 0){
            return ResultUtil.error(new AmazonException(EnumException.SCHEDULE_RUN_FAILED_MENUS_NULL));
        }

        // 启动定时器
        SimpleClosingProcessor.enableSchedule(spiderConfig);

        return ResultUtil.success();
    }

    /**
     * 结束定时器
     *
     * @return
     */
    @RequestMapping(value = "/stopSchedule")
    public Result<Object> stopSchedule() {
        SimpleClosingProcessor.stopSchedule();
        return ResultUtil.success();
    }

    /**
     * 结束爬取
     *
     * @return
     */
    @RequestMapping(value = "/stopCrawl")
    public Result<Object> stopCrawlMenu() {
        SimpleClosingProcessor.stopSpider();
        return ResultUtil.success();
    }

    /**
     * 查询爬虫状态
     *
     * @return
     */
    @RequestMapping(value = "/getSpiderStatus")
    public Result<Object> getSpiderStatus() {
        Spider.Status status = SimpleClosingProcessor.getSpiderStatus();
        if (status == null || status == Spider.Status.Stopped) {
            return ResultUtil.success(new AmazonException(EnumException.NOT_RUNNING_OR_STOPED));
        } else if (status == Spider.Status.Init) {
            return ResultUtil.success(new AmazonException(EnumException.INIT));
        } else if (status == Spider.Status.Running) {
            return ResultUtil.success(new AmazonException(EnumException.RUNNING));
        }

        return ResultUtil.error(new AmazonException(EnumException.UNKOWN_ERROR));
    }
}
