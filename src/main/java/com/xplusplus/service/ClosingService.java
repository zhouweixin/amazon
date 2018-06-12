package com.xplusplus.service;

import com.xplusplus.domain.Closing;
import com.xplusplus.domain.Menu;
import com.xplusplus.exception.AmazonException;
import com.xplusplus.exception.EnumException;
import com.xplusplus.repository.ClosingRepository;
import com.xplusplus.utils.DataStatisticsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort.Direction;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 18:56 2018/3/31
 * @Modified By:
 */
@Service
public class ClosingService {
    @Autowired
    private ClosingRepository closingRepository;

    /**
     * 新增
     *
     * @param closing
     * @return
     */
    public Closing save(Closing closing) {
        return closingRepository.save(closing);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        closingRepository.delete(id);
    }

    /**
     * 通过ASIN查询
     *
     * @param id
     * @return
     */
    public Closing findOne(Long id) {
        return closingRepository.findOne(id);
    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<Closing> findAll() {
        return closingRepository.findAll();
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
    public Page<Closing> findAllByPage(Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断字段名是否存在
        try {
            Closing.class.getDeclaredField(sortFieldName);
        } catch (Exception e) {
            throw new AmazonException(EnumException.NOT_EXISTE_FIELD);
        }

        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Direction.ASC, sortFieldName);
        }
        Pageable pageable = new PageRequest(page, size, sort);
        return closingRepository.findAll(pageable);
    }

    /**
     * 通过下载日期查询
     *
     * @param downloadDate  下载日期
     * @param page          当前页
     * @param size          每页的记录数
     * @param sortFieldName 排序的字段名
     * @param asc           增序还是减序
     * @return
     * @throws Exception
     */
    public Page<Closing> findClosingsByMenuAndDownloadDate(Menu menu, long downloadDate, Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断字段名是否存在
        try {
            Closing.class.getDeclaredField(sortFieldName);
        } catch (Exception e) {
            throw new AmazonException(EnumException.NOT_EXISTE_FIELD);
        }

        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Direction.ASC, sortFieldName);
        }
        Pageable pageable = new PageRequest(page, size, sort);

        Calendar curDate = Calendar.getInstance();
        curDate.setTimeInMillis(downloadDate);
        curDate.set(Calendar.HOUR_OF_DAY, 0);

        return closingRepository.findClosingsByMenuAndDownloadDate(menu, new Date(downloadDate), pageable);
    }

    /**
     * 通过菜单和下载日期查询
     *
     * @param menu
     * @param startDate
     * @return
     */
    public List<Closing> findClosingsByMenuAndDownloadDate(Menu menu, long startDate) {
        return closingRepository.findClosingsByMenuAndDownloadDate(menu, new Date(startDate));
    }

    /**
     * 通过asin和时间段查询
     *
     * @param id
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Closing> findClosingByAsinAndStartDateToEndDate(long id, long startDate, long endDate) {
        List<Closing> list = new ArrayList<Closing>();

        Closing one = closingRepository.findOne(id);
        if (one == null || one.getAsin() == null) {
            return list;
        }
        String asin = one.getAsin();

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTimeInMillis(startDate);
        end.setTimeInMillis(endDate);
        start.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.HOUR_OF_DAY, 0);

        return closingRepository.findClosingByAsinAndStartDateToEndDate(asin, start.getTime(), end.getTime());
    }

    /**
     * 通过 menu, downloadDate, rankDeta查询
     *
     * @param menu
     * @param startDate
     * @param endDate
     * @param rankDeta
     * @return
     */
    public List<Closing> findClosingsByMenuAndDownloadDateAndRankDetaEqual(Menu menu, long startDate, long endDate, Integer rankDeta) {

        List<Closing> closings = updateRankDetaByMenuAndStartEndDate(menu, startDate, endDate);

        List<Closing> closingList = new ArrayList<>();
        for(Closing closing : closings){
            if(closing.getRankDeta() == rankDeta){
                closingList.add(closing);
            }
        }

        Collections.sort(closingList, (closing1, closing2) -> {
            return closing1.getRank() - closing2.getRank();
        });

        return closingList;
    }

    /**
     * 通过 menu, downloadDate, >=rankDeta查询
     *
     * @param menu
     * @param startDate
     * @param endDate
     * @param rankDeta
     * @return
     */
    public List<Closing> findClosingsByMenuAndDownloadDateAndRankDetaGreater(Menu menu, long startDate, long endDate, Integer rankDeta) {
        List<Closing> closings = updateRankDetaByMenuAndStartEndDate(menu, startDate, endDate);

        List<Closing> closingList = new ArrayList<>();
        for(Closing closing : closings){
            if(closing.getRankDeta() >= rankDeta){
                closingList.add(closing);
            }
        }

        Collections.sort(closingList, (closing1, closing2) -> {
            return closing2.getRankDeta() - closing1.getRankDeta();
        });

        return closingList;
    }

    /**
     * 通过 menu, downloadDate, >=rankDeta查询
     *
     * @param menu
     * @param startDate
     * @param endDate
     * @param rankDeta
     * @return
     */
    public List<Closing> findClosingsByMenuAndDownloadDateAndRankDetaLess(Menu menu, long startDate, long endDate, Integer rankDeta) {
        List<Closing> closings = updateRankDetaByMenuAndStartEndDate(menu, startDate, endDate);

        List<Closing> closingList = new ArrayList<>();
        for(Closing closing : closings){
            if(closing.getRankDeta() <= rankDeta){
                closingList.add(closing);
            }
        }

        Collections.sort(closingList, (closing1, closing2) -> {
            return closing1.getRankDeta() - closing2.getRankDeta();
        });

        return closingList;
    }

    public Long findIdMax() {
        return closingRepository.findIdMax();
    }

    /**
     * 更新rankDeta
     *
     * @param menu
     * @param startDate
     * @param endDate
     */
    public List<Closing> updateRankDetaByMenuAndStartEndDate(Menu menu, long startDate, long endDate) {
        List<Closing> startClosings = closingRepository.findClosingsByMenuAndDownloadDate(menu, new Date(startDate));
        List<Closing> endClosings = closingRepository.findClosingsByMenuAndDownloadDate(menu, new Date(endDate));

        EnumException exception = EnumException.COMPARE_FAILED_NO_DATA;
        SimpleDateFormat sdf = new SimpleDateFormat(exception.getMessage());

        if(startClosings == null || startClosings.size() == 0){
            exception.setMessage(sdf.format(new Date(startDate)));
            throw new AmazonException(exception);
        }

        if(endClosings == null || endClosings.size() == 0){
            exception.setMessage(sdf.format(new Date(endDate)));
            throw new AmazonException(exception);
        }

        List<Closing> closings = computeRankDeta(startClosings, endClosings);
        return closings;
    }

    /**
     * 计算排名变化
     *
     * @param startClosings
     * @param endClosings
     * @return
     */
    public List<Closing> computeRankDeta(List<Closing> startClosings, List<Closing> endClosings){
        List<Closing> closings = new ArrayList<Closing>();

        Map<String, Closing> startClosingMap = new HashMap<String, Closing>();
        Map<String, Closing> endClosingMap = new HashMap<String, Closing>();

        startClosings.forEach(closing -> {
            startClosingMap.put(closing.getAsin(), closing);
        });

        endClosings.forEach(closing -> {
            endClosingMap.put(closing.getAsin(), closing);
        });

        endClosingMap.forEach((key, endClosing) -> {
            if(startClosingMap.containsKey(key)){
                Closing startClosing = startClosingMap.get(key);
                endClosing.setRankDeta(startClosing.getRank() - endClosing.getRank());
            }else{
                endClosing.setRankDeta(101 - endClosing.getRank());
            }
        });

        startClosingMap.forEach((key, startClosing) -> {
            if(!endClosingMap.containsKey(key)){
                startClosing.setRankDeta(startClosing.getRank() - 101);
                endClosingMap.put(startClosing.getAsin(), startClosing);
            }
        });

        closings.addAll(endClosingMap.values());

        return closings;
    }
}
