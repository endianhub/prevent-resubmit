package com.xh.prevent.resubmit.commons.same;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;

/**
 * <p>Title: 一个用户 相同url 同时提交 相同数据 验证  主要通过 session中保存到的url 和 请求参数。如果和上次相同，则是重复提交表单 </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月7日
 */
public class SameUrlDataInterceptor extends HandlerInterceptorAdapter {

	private final Logger LOGGER = LogManager.getLogger(getClass());
	private static HttpSession session = null;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			session = request.getSession();
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			SameUrlData annotation = method.getAnnotation(SameUrlData.class);

//			LOGGER.info("annotation=" + annotation);

			if (annotation != null) {
				// 如果重复相同数据
				if (repeatDataValidator(request)) {
					// 重定向到登录页面
//					LOGGER.info("请求地址：" + session.getAttribute("oldUrl"));
					response.sendRedirect(request.getContextPath() +session.getAttribute("oldUrl"));
					session.removeAttribute("oldUrl");
					session.removeAttribute("repeatData");
					return false;
				} else {
					return true;
				}
			}
//			LOGGER.info("保存请求地址：" + request.getServletPath());
			session.setAttribute("oldUrl", request.getServletPath());
			return true;
		} else {
//			LOGGER.info("handler=" + handler);
			return super.preHandle(request, response, handler);
		}
	}

	/** 
	 * 验证同一个url数据是否相同提交  ,相同返回true 
	 * @param request 
	 * @return 
	 */
	public boolean repeatDataValidator(HttpServletRequest request) {
		String params = JSON.toJSONString(request.getParameterMap());
//		LOGGER.info("params=" + params);
		String url = request.getRequestURI();
//		LOGGER.info("url=" + url);
		Map<String, String> map = new HashMap<String, String>();
		map.put(url, params);
		String nowUrlParams = map.toString();//

		Object preUrlParams = session.getAttribute("repeatData");
		// 如果上一个数据为null,表示还没有访问页面
		if (preUrlParams == null) {
			LOGGER.info("未提交数据");
			session.setAttribute("repeatData", nowUrlParams);
			return false;
		} else {
			// 否则，已经访问过页面
			// 如果上次url+数据和本次url+数据相同，则表示城府添加数据
			if (preUrlParams.toString().equals(nowUrlParams)) {
				LOGGER.info("已经提交数据 ");
				return true;
			} else {
				LOGGER.info("两次提交的数据不一至，可以提交");
				// 如果上次 url+数据 和本次url加数据不同，则不是重复提交
				session.setAttribute("repeatData", nowUrlParams);
				return false;
			}

		}
	}
}
