package com.xplusplus.controller;

import com.xplusplus.domain.*;
import com.xplusplus.exception.AmazonException;
import com.xplusplus.exception.EnumException;
import com.xplusplus.processor.ClosingProcessor;
import com.xplusplus.service.ClosingService;
import com.xplusplus.service.MenuService;
import com.xplusplus.utils.DataStatisticsUtil;
import com.xplusplus.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sun.security.provider.ConfigFile;
import us.codecraft.webmagic.Spider;

import javax.validation.Valid;
import java.util.*;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 19:01 2018/3/31
 * @Modified By:
 */
@RestController
@RequestMapping("/closing")
public class ClosingController {
    @Autowired
    private ClosingService closingService;

    @Autowired
    private MenuService menuService;

    /**
     * 新增
     *
     * @param closing
     * @param bindingResult
     * @return
     */
    @RequestMapping("/add")
    public Result<Closing> add(@Valid Closing closing, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getAllErrors().toString());
        }

        // 设置下载日期
        closing.setDownloadDate(new Date());

        return ResultUtil.success(closingService.save(closing));
    }

    /**
     * 更新
     *
     * @param closing
     * @param bindingResult
     * @return
     */
    @RequestMapping("/update")
    public Result<Closing> update(@Valid Closing closing, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getAllErrors().toString());
        }

        if (closingService.findOne(closing.getId()) == null) {
            return ResultUtil.error(new AmazonException(EnumException.UPDATE_FAILED_NOT_EXIST));
        }

        return ResultUtil.success(closingService.save(closing));
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteById")
    public Result<Object> deleteByAsin(@RequestParam(name = "id") Long id) {
        if (closingService.findOne(id) == null) {
            return ResultUtil.error(new AmazonException(EnumException.DELETE_FAILED_NOT_EXIST));
        }
        closingService.delete(id);
        return ResultUtil.success();
    }

    /**
     * 通过Asin查询
     *
     * @param id
     * @return
     */
    @RequestMapping("/getById")
    public Result<Closing> getByAsin(@RequestParam(name = "id") Long id) {
        return ResultUtil.success(closingService.findOne(id));
    }

    /**
     * 通过menuId查询star2num
     *
     * @param menuId
     * @return
     */
    @RequestMapping(value = "/getStarNumByMenuIdAndDownloadDate")
    public Result<List<StarNum>> getStarNumByMenuIdAndDownloadDate(@RequestParam(name = "menuId") Integer menuId,
                                                                   @RequestParam(name = "downloadDate") long downloadDate) {
        Menu menu = new Menu();
        menu.setId(menuId);
        List<Closing> closings = closingService.findClosingsByMenuAndDownloadDate(menu, downloadDate);
        return ResultUtil.success(DataStatisticsUtil.computeClosingStarNum(closings));
    }

    /**
     * 通过menuId查询price2num
     *
     * @param menuId
     * @return
     */
    @RequestMapping(value = "/getPriceNumByMenuIdAndDownloadDate")
    public Result<Collection> getPriceNumByMenuIdAndDownloadDate(@RequestParam(name = "menuId") Integer menuId,
                                                                 @RequestParam(name = "downloadDate") long downloadDate) {
        Menu menu = new Menu();
        menu.setId(menuId);
        List<Closing> closings = closingService.findClosingsByMenuAndDownloadDate(menu, downloadDate);
        return ResultUtil.success(DataStatisticsUtil.computeClosingPriceNum(closings));
    }

    /**
     * 通过menuId查询brand2num
     *
     * @param menuId
     * @return
     */
    @RequestMapping(value = "/getBrandNumByMenuIdAndDownloadDate")
    public Result<Collection> getBrandNumByMenuIdAndDownloadDate(@RequestParam(name = "menuId") Integer menuId,
                                                                 @RequestParam(name = "downloadDate") long downloadDate) {
        Menu menu = new Menu();
        menu.setId(menuId);
        List<Closing> closings = closingService.findClosingsByMenuAndDownloadDate(menu, downloadDate);
        return ResultUtil.success(DataStatisticsUtil.computeClosingBrandNum(closings));
    }

    /**
     * 通过id, 开始日期和结束日期查询
     *
     * @param id
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/getClosingsByIdAndStartDateToEndDate")
    public Result<List<Closing>> getClosingsByIdAndStartDateToEndDate(@RequestParam(name = "id") long id,
                                                                      @RequestParam(name = "startDate") long startDate,
                                                                      @RequestParam(name = "endDate") long endDate) {
        return ResultUtil.success(closingService.findClosingByAsinAndStartDateToEndDate(id, startDate, endDate));
    }

    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping("/getAll")
    public Result<List<Closing>> getAll() {
        return ResultUtil.success(closingService.findAll());
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
    public Result<Page<Closing>> getAllByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                              @RequestParam(value = "size", defaultValue = "10") Integer size,
                                              @RequestParam(value = "sort", defaultValue = "id") String sort,
                                              @RequestParam(value = "asc", defaultValue = "1") Integer asc) {
        return ResultUtil.success(closingService.findAllByPage(page, size, sort, asc));
    }

    /**
     * 通过下载日期分页查询, 按rank升序排
     *
     * @param downloadDate 名称
     * @param page         当前页,从0开始,默认是0
     * @param size         每页的记录数,默认是10
     * @param sort         排序的字段名,默认是id
     * @param asc          排序的方式,0是减序,1是增序,默认是增序
     * @return
     */
    @RequestMapping(value = "/getByMenuIdAndDownloadDateByPage")
    public Result<Page<Closing>> getByDownloadDateByPage(
            @RequestParam(value = "menuId") Integer menuId,
            @RequestParam(value = "downloadDate", defaultValue = "") long downloadDate,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sort", defaultValue = "rank") String sort,
            @RequestParam(value = "asc", defaultValue = "1") Integer asc) {
        Menu menu = new Menu();
        menu.setId(menuId);
        return ResultUtil.success(closingService.findClosingsByMenuAndDownloadDate(menu, downloadDate, page, size, sort, asc));
    }

    /**
     * 通过下载日期分页查询，按排名上升名次降序排
     *
     * @param menuId
     * @param startDate
     * @param endDate
     * @param rankDeta
     * @return
     */
    @RequestMapping(value = "/getByMenuIdAndStartEndDateAndRankDetaByPage")
    public Result<List<Closing>> getByMenuIdAndStartEndDateAndRankDetaByPage(
            @RequestParam(value = "menuId") Integer menuId,
            @RequestParam(value = "startDate", defaultValue = "") long startDate,
            @RequestParam(value = "endDate", defaultValue = "") long endDate,
            @RequestParam(value = "rankDeta", defaultValue = "0") Integer rankDeta) {
        Menu menu = new Menu();
        menu.setId(menuId);

        if (rankDeta == 0) {
            return ResultUtil.success(closingService.findClosingsByMenuAndDownloadDateAndRankDetaEqual(menu, startDate, endDate, rankDeta));
        } else if (rankDeta > 0) {
            return ResultUtil.success(closingService.findClosingsByMenuAndDownloadDateAndRankDetaGreater(menu, startDate, endDate, rankDeta));
        } else if (rankDeta < 0) {
            return ResultUtil.success(closingService.findClosingsByMenuAndDownloadDateAndRankDetaLess(menu, startDate, endDate, rankDeta));
        }
        return ResultUtil.error(new AmazonException(EnumException.UNKOWN_ERROR));
    }

    /**
     * 定时爬取所有
     *
     * @return
     */
    @RequestMapping(value = "/enableScheduleAllMenu")
    public Result<Object> enableScheduleAllMenu() {
        SpiderConfig spiderConfig = new SpiderConfig();
        spiderConfig.setMenus(menuService.findAll());

        // 开始爬取
        ClosingProcessor.enableSchedule(spiderConfig);
        return ResultUtil.success();
    }

    /**
     * 开始爬取所有
     *
     * @return
     */
    @RequestMapping(value = "/startCrawlAllMenu")
    public Result<Object> startCrawlAllMenu() {
        Spider.Status status = ClosingProcessor.getSpiderStatus();
        if (status != null && (status == Spider.Status.Init || status == Spider.Status.Running)) {
            return ResultUtil.error(new AmazonException(EnumException.SPIDER_START_FAILED_RUNNING));
        }

        SpiderConfig spiderConfig = new SpiderConfig();
        spiderConfig.setMenus(menuService.findAll());
        // 开始爬取
        ClosingProcessor.startCrawl(spiderConfig);

        return ResultUtil.success();
    }

    /**
     * 爬取信息
     *
     * @param spiderConfig
     * @return
     */
    @RequestMapping(value = "/startCrawl")
    public Result<Object> startCrawl(SpiderConfig spiderConfig) {

        if (spiderConfig.getMenus() == null) {
            return ResultUtil.error(new AmazonException(EnumException.SPIDER_START_FAILED_MENUS_NULL));
        }

        Spider.Status status = ClosingProcessor.getSpiderStatus();
        if (status != null && (status == Spider.Status.Init || status == Spider.Status.Running)) {
            return ResultUtil.error(new AmazonException(EnumException.SPIDER_START_FAILED_RUNNING));
        }

        // 取出菜单的id
        List<Integer> menuIds = new ArrayList<Integer>();
        spiderConfig.getMenus().forEach((menu) -> {
            if (menu.getId() != null) {
                menuIds.add(menu.getId());
            }
        });

        // 查询菜单信息
        spiderConfig.setMenus(menuService.findAllByBatchId(menuIds));

        // 判断选择的菜单是否为空
        if (spiderConfig.getMenus() == null || spiderConfig.getMenus().size() == 0) {
            return ResultUtil.error(new AmazonException(EnumException.SPIDER_START_FAILED_MENUS_NULL));
        }

        // 开始爬取
        ClosingProcessor.startCrawl(spiderConfig);

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

        if (spiderConfig.getMenus() == null) {
            return ResultUtil.error(new AmazonException(EnumException.SCHEDULE_RUN_FAILED_MENUS_NULL));
        }

        // 取出菜单的id
        List<Integer> menuIds = new ArrayList<Integer>();
        spiderConfig.getMenus().forEach((menu) -> {
            if (menu.getId() != null) {
                menuIds.add(menu.getId());
            }
        });

        // 查询菜单信息
        spiderConfig.setMenus(menuService.findAllByBatchId(menuIds));

        // 判断选择的菜单是否为空
        if (spiderConfig.getMenus() == null || spiderConfig.getMenus().size() == 0) {
            return ResultUtil.error(new AmazonException(EnumException.SCHEDULE_RUN_FAILED_MENUS_NULL));
        }

        // 启动定时器
        ClosingProcessor.enableSchedule(spiderConfig);

        return ResultUtil.success();
    }

    /**
     * 结束定时器
     *
     * @return
     */
    @RequestMapping(value = "/stopSchedule")
    public Result<Object> stopSchedule() {
        ClosingProcessor.stopSchedule();
        return ResultUtil.success();
    }

    /**
     * 结束爬取
     *
     * @return
     */
    @RequestMapping(value = "/stopCrawl")
    public Result<Object> stopCrawlMenu() {
        ClosingProcessor.stopSpider();
        return ResultUtil.success();
    }

    /**
     * 查询爬虫状态
     *
     * @return
     */
    @RequestMapping(value = "/getSpiderStatus")
    public Result<Object> getSpiderStatus() {
        SpiderConfig spiderConfig = null;
        if (ClosingProcessor.spiderConfig != null) {
            spiderConfig = ClosingProcessor.spiderConfig.clone();
        }

        if (spiderConfig == null) {
            spiderConfig = new SpiderConfig();
        }
        spiderConfig.setMenus(null);
        spiderConfig.setEndId(closingService.findIdMax());

        spiderConfig.setEndTime(System.currentTimeMillis());

        Spider.Status status = ClosingProcessor.getSpiderStatus();
        if (status == null || status == Spider.Status.Stopped) {

            spiderConfig.setCode(EnumException.NOT_RUNNING_OR_STOPED.getCode());
            spiderConfig.setMessage(EnumException.NOT_RUNNING_OR_STOPED.getMessage());
            return ResultUtil.success(spiderConfig);
        } else if (status == Spider.Status.Init) {
            spiderConfig.setCode(EnumException.INIT.getCode());
            spiderConfig.setMessage(EnumException.INIT.getMessage());
            return ResultUtil.success(spiderConfig);
        } else if (status == Spider.Status.Running) {
            spiderConfig.setCode(EnumException.RUNNING.getCode());
            spiderConfig.setMessage(EnumException.RUNNING.getMessage());
            return ResultUtil.success(spiderConfig);
        }

        return ResultUtil.error(new AmazonException(EnumException.UNKOWN_ERROR));
    }
}
