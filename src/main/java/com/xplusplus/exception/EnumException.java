package com.xplusplus.exception;

/**
 * 所有异常类型
 * 
 * @author zhouweixin
 *
 */
public enum EnumException {
	UNKOWN_ERROR(-1, "未知错误"), 
	SUCCESS(0, "成功"),
	ADD_FAILED_EXIST(1, "添加失败, 已存在"),
	DELETE_FAILED_NOT_EXIST(2, "删除失败, 不存在"),
	UPDATE_FAILED_NOT_EXIST(3, "更新失败, 不存在"),
	NOT_EXISTE_FIELD(4, "字段名不存在"),
	REQUEST_METHOD_NOT_MATCH(5, "请求方面不匹配"),
	START_URL_NULL(6, "启动失败, 开始网址为空"),
	SORT_FIELD(7, "排序失败, 排序字段名不存在"),
	SPIDER_START_FAILED_RUNNING(8, "爬虫启动失败, 正在运行"),
	NOT_RUNNING_OR_STOPED(9, "没有启动或已停止"),
    INIT(10, "正在初始化"),
    RUNNING(11, "正在运行"),
    SPIDER_START_FAILED_MENUS_NULL(12, "爬虫启动失败, 没有选择菜单"),
	SCHEDULE_RUN_FAILED_MENUS_NULL(13, "定时器启动失败, 没有选择菜单"),
	SCHEDULE_RUN_FAILED_START_URL_NULL(14, "定时器启动失败, 开始网址为空"),
	LOGIN_FAILED_ID_NULL(15, "登录失败, id为空"),
	LOGIN_FAILED_PASSWORD_NULL(16, "登录失败, 密码为空"),
	LOGIN_FAILED_ID_OR_PASSWORD_ERROR(17, "登录失败, id或密码不正确"),
	USER_ID_OR_PASSWORD_NULL(18, "添加失败, id或密码为空"),
    USER_NOT_LOGIN(19, "用户没有登录"),
    COMPARE_FAILED_NO_DATA(20, "yyyy年MM月dd日没有数据无法对比，请重新选择日期"),


	;

    /** 编码 */
	private Integer code;
	/** 信息 */
	private String message;

	/**
	 * 构造函数
	 * 
	 * @param code
	 * @param message
	 */
	private EnumException(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
