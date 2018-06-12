package com.xplusplus.repository;

import com.xplusplus.domain.Menu;
import com.xplusplus.domain.SimpleClosing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 20:05 2018/4/4
 * @Modified By:
 */
public interface SimpleClosingRepository extends JpaRepository<SimpleClosing, Long> {
    /**
     * 通过menu和下载日期分页查询
     * @param menu
     * @param startDate
     * @param endDate
     * @param pageable
     * @return
     */
    @Query(value = "select s from SimpleClosing s where s.menu=?1 and s.downloadDate >= ?2 and s.downloadDate < ?3")
    public Page<SimpleClosing> findSimpleClosingsByMenuAndDownloadDate(Menu menu, Date startDate, Date endDate, Pageable pageable);

    /**
     * 通过menu和下载日期分页查询
     * @param menu
     * @param startDate
     * @param endDate
     * @return
     */
    @Query(value = "select s from SimpleClosing s where s.menu=?1 and s.downloadDate >= ?2 and s.downloadDate < ?3")
    public List<SimpleClosing> findSimpleClosingsByMenuAndDownloadDate(Menu menu, Date startDate, Date endDate);
}
