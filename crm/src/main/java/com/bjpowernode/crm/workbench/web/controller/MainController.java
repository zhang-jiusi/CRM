package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 处理当用户执行登陆后，访问工作区 /workbench/main/index 页面
 *
 * @date: 2024/7/11 18:13
 */
@Controller
public class MainController {

	/**
	 * 访问 /workbench/main/index 页面
	 *
	 * @date:   2024/7/11 18:15
	 **/
	@RequestMapping("/workbench/main/index.do")
	public String index(){

		// 跳转到 /workbench/main/index.jsp
		return "/workbench/main/index";
	}

}