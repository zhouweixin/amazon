package com.xplusplus.handle;

import com.xplusplus.domain.Result;
import com.xplusplus.exception.AmazonException;
import com.xplusplus.exception.EnumException;
import com.xplusplus.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常处理类
 *
 * @author zhouweixin
 *
 */
@ControllerAdvice
public class ExceptionHandle {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<Object> handle(Exception e) {
        if (e instanceof AmazonException) {
            // 自定义异常
            AmazonException mesException = (AmazonException) e;
            return ResultUtil.error(mesException);
        }  else {
            logger.error("【系统异常】 {}", e.getMessage());

            if (e.getMessage().contains("Request method")) {
                // 请求方法不匹配
                return ResultUtil.error(new AmazonException(EnumException.REQUEST_METHOD_NOT_MATCH));
            } else {
                return ResultUtil.error(new AmazonException(EnumException.UNKOWN_ERROR));
            }
        }
    }
}
