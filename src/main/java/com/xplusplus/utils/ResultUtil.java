package com.xplusplus.utils;

import com.xplusplus.domain.Result;
import com.xplusplus.exception.AmazonException;
import com.xplusplus.exception.EnumException;

/**
 * 统一返回工具类
 *
 * @author zhouweixin
 */
public class ResultUtil {
    /**
     * 成功
     *
     * @param data
     * @return
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>();
        result.setCode(EnumException.SUCCESS.getCode());
        result.setMessage(EnumException.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    /**
     * 成功
     *
     * @return
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 异常
     *
     * @param mesException
     * @return
     */
    public static <T> Result<T> error(AmazonException mesException) {
        Result<T> result = new Result<T>();
        result.setCode(mesException.getCode());
        result.setMessage(mesException.getMessage());
        return result;
    }

    /**
     * 异常
     *
     * @param message
     * @return
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<T>();
        result.setCode(-2);
        result.setMessage(message);
        return result;
    }
}
