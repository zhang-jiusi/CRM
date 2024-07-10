package com.bjpowernode.crm.setting.web.interceptor;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.setting.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录验证的拦截器
 *
 * @date: 2024/7/9 19:32
 */
public class LoginInterceptor implements HandlerInterceptor {


	/**
	 * 请求到达目标资源之前执行
	 *
	 * @date:   2024/7/10 21:05
	 **/
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		/**
		 * 设计用户登录的时候，当用户登录成功后会向浏览器返回一个session，
		 * 以此作为判断条件，如果用户没有登录成功，则跳转到登录页面
		 *
		 * return "redirect:/" 重定向仅仅可以在 controller 层进行，拦截器不可以这样写，使用原始的 servlet 实现重定向
		 *
		 * 注意：在原始的 servlet 中实现请求转发可以直接写请求转发的资源路径
		 * 		                      重定向要以 “/+本项目的名称+其他具体的资源路径”
		 *
		 * 		"本项目的名称" 动态获取的方式: request.getContextPath()
		 *
		 * @date:   2024/7/10 21:13
		 **/
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute(Contants.SESSION_USER);
		if (user==null){

			response.sendRedirect(request.getContextPath());

			// 终止目标资源访问
			return  false;
		}

		// 实现目标资源访问
		return true;
	}

	/**
	 * 请求获取目标资源之后执行
	 *
	 * @date:   2024/7/10 21:05
	 **/
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 请求响应到浏览器后执行
	 *
	 * @date:   2024/7/10 21:06
	 **/
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}
}