package com.xplusplus.utils;

import com.xplusplus.domain.Closing;
import com.xplusplus.domain.SimpleClosing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 23:19 2018/4/8
 * @Modified By:
 */
public class InfoParseUtil {
    public static Set<String> materials = new HashSet<String>();

    static {
        materials.add("polyester");
        materials.add("spandex");
        materials.add("nylon");
        materials.add("spandex");
        materials.add("cotton");
        materials.add("floral");
    }

    /**
     * 解析价钱
     *
     * @param priceStr
     * @param o
     */
    public static void parsePrice(String priceStr, Object o) {

        if (priceStr == null || priceStr.equals("")) {
            return;
        }

        double priceLower = 0.0, priceHigher = 0.0;
        String[] priceStrs = priceStr.replace("$", "").split("-");
        if (priceStrs.length > 1) {
            priceLower = Double.parseDouble(priceStrs[0].trim());
            priceHigher = Double.parseDouble(priceStrs[1].trim());
        } else if (priceStrs.length == 1) {
            priceLower = Double.parseDouble(priceStrs[0].trim());
            priceHigher = priceLower;
        }

        if (o instanceof Closing) {
            ((Closing) o).setPriceLower(priceLower);
            ((Closing) o).setPriceHigher(priceHigher);
        } else if (o instanceof SimpleClosing) {
            ((SimpleClosing) o).setPriceLower(priceLower);
            ((SimpleClosing) o).setPriceHigher(priceHigher);
        }
    }

    /**
     * 匹配颜色
     *
     * @param str
     * @return
     */
    public static String matchMaterial(String str) {
        Set<String> set = new HashSet<String>();
        if (str == null || str.equals("")) {
            return set.toString();
        }

        final String string = str.toLowerCase();
        materials.forEach(material -> {
            if (string.contains(material)) {
                set.add(material);
            }
        });
        return set.toString();
    }

    /**
     * 匹配: Product Dimensions; Shipping Weight; ASIN; Date first available at Amazon.com;
     *
     * @param str
     * @param closing
     */
    public static void parseOthers(String str, Closing closing) {
        if (str == null || str.equals("")) {
            return;
        }
        str = str.replace("inches", "").replace("ounces", "").replace("(View shipping rates and policies)", "").replace("pounds", "").trim();

        if (str.startsWith("Product Dimensions:")) {// 产品大小
            closing.setProductDimensions(str.replace("Product Dimensions:", "").trim());
        } else if (str.startsWith("Shipping Weight:")) {// 产品重量
            closing.setShippingWeight(str.replace("Shipping Weight:", "").trim());
        } else if (str.startsWith("ASIN:")) {// ASIN
            closing.setAsin(str.replace("ASIN:", "").trim());
        } else if (str.startsWith("Date first available at Amazon.com:")) {// 第一次支持亚马逊的时间
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
            try {
                closing.setFirstAvailAmazonDate(sdf.parse(str.replace("Date first available at Amazon.com:", "").trim()));
            } catch (ParseException e) {
            }
        } else if(str.startsWith("Item model number:")){// 商品模型编号
            closing.setItemModelNumber(str.replace("Item model number:", "").trim());
        }
    }
}
