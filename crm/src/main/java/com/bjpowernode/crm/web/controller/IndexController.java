package com.bjpowernode.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 处理前台用户访问首页的请求
 *
 * @date:  2024/5/31 21:45
 **/
@Controller
public class IndexController {


	/**
	 * @RequestMapping("/") 这个注解用于将前台的用户请求链接 url，交由当前的方法处理
	 * http://127.0.0.1:8080/crm/ 按照 http 协议，访问 127.0.0.1 服务器，8080 端口，
	 * 							  crm 项目的 / 资源
	 *  理论上，给 Controller 方法分配 url：http://127.0.0.1:8080/crm/
	 *  但是，为了简便，协议://ip 地址:port/应用名称，springMVC 强制省去，用/代表根目录下的/
	 *
	 * @date:  2024/5/31 21:30
	 **/
	@RequestMapping("/")
	public String index(){


		/**
		 * Q: 为什么请求转发的写法：return "/WEB-INF/pages/index.jsp"; 中 "/" 从根目录开始？
		 * A: 因为将项目打包到 tomcat 的时候，就是 / 表示这个 crm 项目
		 *
		 * webapps(Tomcat 包目录，crm 项目从 web-inf 被打包到 tomcat 资源目录下)
		 *       |->stumgr
		 *       |->crm
		 *           |->.css,.js,.img
		 *           |->WEB-INF
		 *               |->web.xml
		 *               |->classes
		 *               |->lib
		 *               |->pages  test.jsp
		 *
		 * Q: 为什么 return "/WEB-INF/pages/index.jsp"; 省去路径 /WEB-INF/pages/，省去后缀 .jsp？
		 * A: 因为在 applicationContext-mvc.xml 已经配置视图解析器
		 *
		 **/

		//请求转发（return 中返回资源路径的地址）
		return "index";
	}


}