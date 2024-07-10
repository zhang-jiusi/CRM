package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @date: 2024/7/7 15:48
 */
@Controller
public class WorkbenchIndexController {

	/**
	 * 跳转到业务主界面
	 *
	 * @date: 2024/7/7 16:22
	 **/
	@RequestMapping("/workbench/index.do")
	public String index(){
		return "workbench/index";
	}

}