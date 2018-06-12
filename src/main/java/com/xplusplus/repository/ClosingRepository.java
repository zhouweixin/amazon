package com.xplusplus.repository;

import com.xplusplus.domain.Closing;
import com.xplusplus.domain.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.ManyToMany;
import java.util.Date;
import java.util.List;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 18:55 2018/3/31
 * @Modified By:
 */
public interface ClosingRepository extends JpaRepository<Closing, Long> {

    @Query(value = "select max(id) from Closing")
    public Long findIdMax();

    @Query(value = "select count(id) from Closing where id>?1")
    public int findCountLargerThanId(Long id);

    /**
     * 根据asin和下载日期查询
     * @param asin
     * @param date
     * @return
     */
    public List<Closing> findClosingsByAsinAndDownloadDate(String asin, Date date);

    /**
     * 通过菜单和下载日期分页查询
     *
     * @param menu
     * @param downloadDate
     * @param pageable
     * @return
     */
    public Page<Closing> findClosingsByMenuAndDownloadDate(Menu menu, Date downloadDate, Pageable pageable);

    /**
     * 通过asin和时间段查询
     *
     * @param asin
     * @param startDate
     * @param endDate
     * @return
     */
    @Query(value = "select c from Closing c where c.asin=?1 and c.downloadDate >= ?2 and c.downloadDate <= ?3 order by downloadDate asc")
    public List<Closing> findClosingByAsinAndStartDateToEndDate(String asin, Date startDate, Date endDate);

    /**
     * 通过menu和下载日期查询
     * @param menu
     * @param downloadDate
     * @return
     */
    public List<Closing> findClosingsByMenuAndDownloadDate(Menu menu, Date downloadDate);

    /**
     * 查询rankDeta ==
     *
     * @param menu
     * @param date
     * @param rankDeta
     * @param pageable
     * @return
     */
    public Page<Closing> findClosingsByMenuAndDownloadDateAndRankDeta(Menu menu, Date date, Integer rankDeta, Pageable pageable);

    /**
     * 查询rankDeta >=
     *
     * @param menu
     * @param date
     * @param rankDeta
     * @param pageable
     * @return
     */
    public Page<Closing> findClosingsByMenuAndDownloadDateAndRankDetaGreaterThanEqual(Menu menu, Date date, Integer rankDeta, Pageable pageable);

    /**
     * 查询rankDeta <=
     *
     * @param menu
     * @param date
     * @param rankDeta
     * @param pageable
     * @return
     */
    public Page<Closing> findClosingsByMenuAndDownloadDateAndRankDetaLessThanEqual(Menu menu, Date date, Integer rankDeta, Pageable pageable);
}
