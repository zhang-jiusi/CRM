package com.bjpowernode.crm.setting.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.setting.domain.User;
import com.bjpowernode.crm.setting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.SimpleFormatter;

/**
 * 处理用户的登录请求
 *
 * @date:   2024/7/2 22:15
 **/
@Controller
public class UserController {

	@Autowired
	private UserService userService;
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


	/**
	 * 处理前端的登录请求，并且返回给前端 json 字符串
	 *
	 * RequestMapping 中的参数:
	 *   1.url 路径，要与 controller 方法处理完请求之后，响应信息返回的资源目录保持一致
	 * 	 2.要访问的资源名称要与方法名一致
	 *
	 * ResponseBody: 将返回的数据转译为 json 格式
	 *
	 * @return: java.lang.Object
	 * 		返回 object 对象，在实际将 object 对象转为 json 对象的时候，
	 * 		是转译为子类类（多态）
	 *
	 * @date:   2024/7/6 20:52
	 **/
	@RequestMapping("/settings/qx/user/login.do")
	@ResponseBody
	public Object login(String loginAct, String loginPwd, String isRemPwd,
						HttpServletRequest request, HttpServletResponse response, HttpSession session){

		// 封装参数，这个参数是交由数据库查询的，isRemPwd 不被数据库查询，故不被 传入 map
		HashMap<String, Object> map = new HashMap<>();
		map.put("loginAct",loginAct);
		map.put("loginPwd",loginPwd);


		// 调用 service 层方法，查询用户
		User user = userService.queryUserByLoginActAndPwd(map);
		// returnObject 返回给前端的相关信息
		ReturnObject returnObject = new ReturnObject();
		// 根据查询结果，确定响应信息
		// 判断 1：数据库中是否能够查询到指定的用户，
		if(user==null){
			// 登录失败 1：账户，密码错误
			returnObject.setCode("0");
			returnObject.setMessage("用户名或密码信息错误");
		}else {
			// 若已经从数据库中查询到用户，则进一步判断账号是否合法
			/**
			 * 判断 2：当前账号用户是否已经过期
			 *
			 * 方法 1：获取字符串时间格式，二者进行比较
			 * 			2020-01-01
			 * 		    2024-01-01
			 * 		   当由字符串的数字大小不一样的时候，判断二者大小，即可知道哪个时间在后
			 *
			 * 方法 2：获取两个日期距离 1970 的时间戳，相减
			 *
			 * user.getExpireTime() 返回用户的到期时间
			 * user.getLockState()  当前账户是否被锁定
			 **/

			String oddStr = user.getExpireTime();
			/**
			 * SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
			 * String nowStr = simpleFormatter.format(new Date());
			 **/
			// 2024-07-07 17:16:21 转义后的时间格式
			String nowStr = DateUtils.formateDatTIme(new Date());
			// 判断 2：账号时间是否过期
			if(nowStr.compareTo(oddStr)>0){
				// 登录失败 2，账号时间过期
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("账号时间过期");

			// 判断 3：状态是否被锁定
			}else if("0".equals(user.getLockState())){
				// 登录失败 3，状态被锁定
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("状态被锁定");

			// 判断 4：IP 地址是否在允许范围内（数据库中已经内置被允许的 ip 地址）
			}else if(!user.getAllowIps().contains(request.getRemoteAddr())){

				// 登录失败 4，IP 地址不在被允许范围内 当前 request.getRemoteAddr() 获取到的 ip 地址为 127.0.0.1
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("IP 地址不在被允许范围内");
			}else {

				// 登录成功
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

				// 把查询到的 user 对象，保存到 session 中
				session.setAttribute(Contants.SESSION_USER,user);

				/**
				 * 实现浏览器记住用户密码的功能
				 **/
				// 用户需要记住密码，向浏览器写入，cookie
				if("true".equals(isRemPwd)){

					Cookie c1 = new Cookie("loginAct", user.getLoginAct());
					Cookie c2 = new Cookie("loginPwd", user.getLoginPwd());
					c1.setMaxAge(10*24*60*60);
					c2.setMaxAge(10*24*60*60);
					response.addCookie(c1);
					response.addCookie(c2);

				// 用户没有点击记住密码，删除 cookie
				}else {

					// 向浏览器同 name 的 cookie 并且设定 cookie 的生命周期为 0，根据 cookie 设定，后面的 cookie 覆盖原来的 cookie
					Cookie c1 = new Cookie("loginAct", "1");
					Cookie c2 = new Cookie("loginPwd", "1");
					c1.setMaxAge(0);
					c2.setMaxAge(0);
					response.addCookie(c1);
					response.addCookie(c2);

				}
			}

		}
		return returnObject;

	}

}