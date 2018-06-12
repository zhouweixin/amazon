package com.xplusplus.service;

import com.xplusplus.domain.Menu;
import com.xplusplus.domain.SimpleClosing;
import com.xplusplus.exception.AmazonException;
import com.xplusplus.exception.EnumException;
import com.xplusplus.repository.SimpleClosingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 20:06 2018/4/4
 * @Modified By:
 */
@Service
public class SimpleClosingService {

    @Autowired
    private SimpleClosingRepository simpleClosingRepository;

    /**
     * 新增
     *
     * @param simpleClosing
     * @return
     */
    public SimpleClosing save(SimpleClosing simpleClosing){
        return simpleClosingRepository.save(simpleClosing);
    }

    /**
     * 批量添加
     *
     * @param simpleClosings
     * @return
     */
    public List<SimpleClosing> save(List<SimpleClosing> simpleClosings){
        return simpleClosingRepository.save(simpleClosings);
    }

    /**
     * 通过id删除
     *
     * @param id
     */
    public void delete(Long id){
        simpleClosingRepository.delete(id);
    }

    /**
     * 通过编码查询
     *
     * @param id
     * @return
     */
    public SimpleClosing findOnd(Long id){
        return simpleClosingRepository.findOne(id);
    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<SimpleClosing> findAll(){
        return simpleClosingRepository.findAll();
    }

    /**
     * 通过id批量查询
     *
     * @return
     */
    public List<SimpleClosing> findAll(List<Long> ids){
        return simpleClosingRepository.findAll(ids);
    }

    /**
     * 查询所有-分页
     *
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<SimpleClosing> findAllByPage(Integer page, Integer size, String sortFieldName, Integer asc) {
        // 判断字段名是否存在
        try {
            SimpleClosing.class.getDeclaredField(sortFieldName);
        } catch (Exception e) {
            throw new AmazonException(EnumException.NOT_EXISTE_FIELD);
        }

        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortFieldName);
        }
        Pageable pageable = new PageRequest(page, size, sort);
        return simpleClosingRepository.findAll(pageable);
    }

    /**
     * 通过menu和下载日期分页查询
     *
     * @param downloadDate
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<SimpleClosing> findSimpleClosingsByMenuAndDownloadDate(Menu menu, long downloadDate, Integer page, Integer size, String sortFieldName, Integer asc) {
        // 判断字段名是否存在
        try {
            SimpleClosing.class.getDeclaredField(sortFieldName);
        } catch (Exception e) {
            throw new AmazonException(EnumException.NOT_EXISTE_FIELD);
        }

        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortFieldName);
        }
        Pageable pageable = new PageRequest(page, size, sort);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(downloadDate);

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calendar.getTime();

        return simpleClosingRepository.findSimpleClosingsByMenuAndDownloadDate(menu, new Date(downloadDate), endDate, pageable);
    }

    /**
     * 通过menu和下载日期查询
     * @param menu
     * @param startDate
     * @param endDate
     * @return
     */
    public List<SimpleClosing> findSimpleClosingByMenu(Menu menu, long startDate, long endDate) {
        return simpleClosingRepository.findSimpleClosingsByMenuAndDownloadDate(menu, new Date(startDate), new Date(endDate));
    }
}
