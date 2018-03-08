package com.xh.prevent.resubmit.commons.utils;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

public class WebUtils extends org.springframework.web.util.WebUtils {

	/**
	 * 判断是否ajax请求 spring ajax 返回含有 ResponseBody 或者 RestController注解
	 * 
	 * @param handlerMethod
	 *            HandlerMethod
	 * @return 是否ajax请求
	 */
	public static boolean isAjax(HandlerMethod handlerMethod) {
		ResponseBody responseBody = handlerMethod.getMethodAnnotation(ResponseBody.class);
		if (responseBody != null) {
			return true;
		}
		RestController restController = handlerMethod.getBean().getClass().getAnnotation(RestController.class);
		if (restController != null) {
			return true;
		}
		return false;
	}
}
