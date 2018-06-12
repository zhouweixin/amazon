package com.xplusplus.utils;

import com.xplusplus.domain.*;
import org.springframework.data.domain.Page;

import java.util.*;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 21:41 2018/4/9
 * @Modified By:
 */
public class DataStatisticsUtil {
    /**
     * 统计星
     *
     * @param closings
     * @return
     */
    public static List<StarNum> computeClosingStarNum(List<Closing> closings) {

        Map<Double, Integer> star2num = new HashMap<Double, Integer>();

        closings.forEach(closing -> {
            Double star = closing.getStar();
            if (star != null) {
                if (star2num.containsKey(star)) {
                    star2num.put(star, star2num.get(star) + 1);
                } else {
                    star2num.put(star, 1);
                }
            }
        });

        List<StarNum> starNums = new ArrayList<StarNum>();
        star2num.forEach((key, value) -> {
            starNums.add(new StarNum(key, value));
        });

        Collections.sort(starNums, (starNum1, starNum2) -> {
            return starNum1.getStar() - starNum2.getStar() >= 0 ? 1 : -1;
        });

        return starNums;
    }

    /**
     * 统计星
     *
     * @param simpleClosings
     * @return
     */
    public static List<StarNum> computeSimpleClosingStarNum(List<SimpleClosing> simpleClosings) {

        Map<Double, Integer> star2num = new HashMap<Double, Integer>();

        simpleClosings.forEach(simpleClosing -> {
            Double star = simpleClosing.getStar();
            if (star != null) {
                if (star2num.containsKey(star)) {
                    star2num.put(star, star2num.get(star) + 1);
                } else {
                    star2num.put(star, 1);
                }
            }
        });

        List<StarNum> starNums = new ArrayList<StarNum>();
        star2num.forEach((key, value) -> {
            starNums.add(new StarNum(key, value));
        });

        return starNums;
    }

    /**
     * 统计品牌
     *
     * @param closings
     * @return
     */
    public static Collection computeClosingBrandNum(List<Closing> closings) {
        Map<String, BrandNum> brand2num = new HashMap<String, BrandNum>();

        for (Closing closing : closings) {
            String brand = closing.getBrand();
            if(brand != null && !brand.equals("")){
                if(brand2num.containsKey(brand)){
                    BrandNum brandNum = brand2num.get(brand);
                    brandNum.setNum(brandNum.getNum() + 1);
                }else{
                    BrandNum brandNum = new BrandNum(brand);
                    brandNum.setNum(1);
                    brand2num.put(brand, brandNum);
                }
            }
        }

        return brand2num.values();
    }

    /**
     * 统计价钱
     *
     * @param closings
     * @return
     */
    public static Collection computeClosingPriceNum(List<Closing> closings) {
        Map<String, PriceNum> interval2priceNum = new HashMap<String, PriceNum>();

        for (Closing closing : closings) {
            priceUtil(0, closing.getPriceLower(), interval2priceNum);
            priceUtil(1, closing.getPriceHigher(), interval2priceNum);
        }

        List<PriceNum> list = new ArrayList<PriceNum>(interval2priceNum.values());
        Collections.sort(list, (priceNum1, priceNum2)->{
            return priceNum1.getBasePrice() - priceNum2.getBasePrice() >= 0 ? 1 : -1;
        });
        return list;
    }

    /**
     * 价钱统计工具函数
     *
     * @param type
     * @param price
     * @param interval2priceNum
     */
    public static void priceUtil(int type, Double price, Map<String, PriceNum> interval2priceNum) {
        if (price != null) {
            if (type == 0) {
                for (int i = 0; i < 10; i++) {
                    if (price >= i * 10 && price < (i + 1) * 10) {
                        String key = "[" + i * 10 + ", " + (i + 1) * 10 + ")";
                        if (interval2priceNum.containsKey(key)) {
                            PriceNum priceNum = interval2priceNum.get(key);
                            priceNum.setLowerNum(priceNum.getLowerNum() + 1);
                        } else {
                            PriceNum priceNum = new PriceNum(key);
                            priceNum.setLowerNum(1);
                            priceNum.setBasePrice(i * 10);
                            interval2priceNum.put(key, priceNum);
                        }

                        return;
                    }
                }

                if (price >= 100 && price < 300) {
                    String key = "[" + 100 + ", " + 300 + ")";
                    if (interval2priceNum.containsKey(key)) {
                        PriceNum priceNum = interval2priceNum.get(key);
                        priceNum.setLowerNum(priceNum.getLowerNum() + 1);
                    } else {
                        PriceNum priceNum = new PriceNum(key);
                        priceNum.setLowerNum(1);
                        priceNum.setBasePrice(100);
                        interval2priceNum.put(key, priceNum);
                    }
                } else if (price >= 300) {
                    String key = "[" + 300 + ", ∞)";
                    if (interval2priceNum.containsKey(key)) {
                        PriceNum priceNum = interval2priceNum.get(key);
                        priceNum.setLowerNum(priceNum.getLowerNum() + 1);
                        priceNum.setHigherNum(priceNum.getHigherNum() + 1);
                    } else {
                        PriceNum priceNum = new PriceNum(key);
                        priceNum.setLowerNum(1);
                        priceNum.setBasePrice(300);
                        interval2priceNum.put(key, priceNum);
                    }
                }
            } else {
                for (int i = 0; i < 10; i++) {
                    if (price >= i * 10 && price < (i + 1) * 10) {
                        String key = "[" + i * 10 + ", " + (i + 1) * 10 + ")";
                        if (interval2priceNum.containsKey(key)) {
                            PriceNum priceNum = interval2priceNum.get(key);
                            priceNum.setHigherNum(priceNum.getHigherNum() + 1);
                        } else {
                            PriceNum priceNum = new PriceNum(key);
                            priceNum.setHigherNum(1);
                            priceNum.setBasePrice(i * 10);
                            interval2priceNum.put(key, priceNum);
                        }

                        return;
                    }
                }

                if (price >= 100 && price < 300) {
                    String key = "[" + 100 + ", " + 300 + ")";
                    if (interval2priceNum.containsKey(key)) {
                        PriceNum priceNum = interval2priceNum.get(key);
                        priceNum.setHigherNum(priceNum.getHigherNum() + 1);
                    } else {
                        PriceNum priceNum = new PriceNum(key);
                        priceNum.setHigherNum(1);
                        priceNum.setBasePrice(100);
                        interval2priceNum.put(key, priceNum);
                    }
                } else if (price >= 300) {
                    String key = "[" + 300 + ", ∞)";
                    if (interval2priceNum.containsKey(key)) {
                        PriceNum priceNum = interval2priceNum.get(key);
                        priceNum.setHigherNum(priceNum.getHigherNum() + 1);
                    } else {
                        PriceNum priceNum = new PriceNum(key);
                        priceNum.setHigherNum(1);
                        priceNum.setBasePrice(300);
                        interval2priceNum.put(key, priceNum);
                    }
                }
            }
        }
    }

    /**
     * 计算上升的名次
     *
     * @param cur
     * @param last
     */
//    public static void computeClosingRankDeta(List<Closing> cur, List<Closing> last) {
//        if(cur == null || last == null){
//            return;
//        }
//
//        Map<String, Closing> map = new HashMap<String, Closing>();
//        for(Closing closing : last){
//            if(closing.getAsin() != null && !closing.getAsin().equals("")){
//                map.put(closing.getAsin(), closing);
//            }
//        }
//
//        for(Closing closing : cur){
//            if(closing.getAsin() != null && !closing.getAsin().equals("")){
//                String asin = closing.getAsin();
//                if(map.containsKey(asin)){
//                    Closing lastClosing = map.get(asin);
//                    Integer lastRank = lastClosing.getRank();
//                    Integer curRank = closing.getRank();
//
//                    if(curRank != null && lastRank != null){
//                        closing.setRankDeta(curRank - lastRank);
//                    }
//                }
//            }
//        }
//    }

//    public static void computeClosingRankDetaGreater(List<Closing> content, List<Closing> content1, Integer rankDeta) {
//        if(rankDeta > 0){
//
//        }else if(rankDeta < 0){
//
//        }else {
//
//        }
//    }
}
