package com.bjpowernode.crm.setting.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 处理用户的登录请求
 *
 * @date:   2024/7/2 22:15
 **/
@Controller
public class UserController {

	/**
	 * RequestMapping 中的参数：
	 * 	1.url 路径，要与 controller 方法处理完请求之后，响应信息返回的资源目录保持一致
	 * 	  url 完整的访问路径：/WEB-INF/pages/settings/qx/user/toLogin.do
	 * 	  但是因为所有的资源都是保存在 WEB-INF/pages/，故省去了WEB-INF/pages/
	 * 	2.要访问的资源名称要与方法名一致
	 *
	 * 	.do 的资源访问统一交给 controller 核心控制器来处理
	 **/
	@RequestMapping("/settings/qx/user/toLogin.do")
	public String toLogin(){

		// 请求转发到登录页面
		return "/settings/qx/user/login";
	}



}