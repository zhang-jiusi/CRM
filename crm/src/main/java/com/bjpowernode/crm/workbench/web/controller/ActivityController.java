package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.setting.domain.User;
import com.bjpowernode.crm.setting.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.sun.tools.javac.jvm.ByteCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

import static com.sun.tools.javac.jvm.ByteCodes.ret;

/**
 * 市场活动的 controller
 *
 * @date: 2024/7/16 21:19
 */
@Controller
public class ActivityController {

	@Autowired
	private UserService userService;

	@Autowired
	private ActivityService activityService;

	/**
	 * 返回市场活动的主页面
	 *
	 * @date:   2024/7/16 21:21
	 **/
	@RequestMapping("/workbench/activity/index")
	public String index(HttpServletRequest request){

		// 调用 service 层的方法，查询所有的用户
		List<User> users = userService.queryAllUsers();

		// 将数据保存的 requset 的作用域中
		request.setAttribute("userList",users);

		// 请求转发，到市场活动的主界面上
		return "/workbench/activity/index";
	}

	/**
	 * 保存市场活动
	 *
	 * @RequestMapping("/workbench/activity/saveCreateActivity.do")
	 * 		访问路径：处理资源路径+方法名
	 *
	 * @date:   2025/6/16 6:46
	 **/
	@RequestMapping("/workbench/activity/saveCreateActivity.do")
	public @ResponseBody Object saveCreateActivity(Activity activity,HttpSession session){

		User user =(User) session.getAttribute(Contants.SESSION_USER);

		/**
		 *  前端模态窗口中获取到的参数，缺少id,create_time, create_by 继续封装参数
		 *  id：UUIDUtils.getUUID() 从工具类方法中，使用 JDK 生成一个 32 字符串作为 id
		 *  create_time：DateUtils.formateDatTIme(new Date()) 当前的 date 类型的系统时间转化为字符串保存在数据库中
		 *  create_by：使用当前登录到系统中的用户 id
		 **/
		activity.setId(UUIDUtils.getUUID());
		activity.setCreateTime(DateUtils.formateDatTIme(new Date()));
		activity.setCreateBy(user.getId());
		System.out.println("--------------- user.toString() ---------------");
		System.out.println(user.toString());
		/*
		* User{id='06f5fc056eac41558a964f96daa7f27c', loginAct='ls', name='李四',
		*      loginPwd='yf123', email='ls@163.com', expireTime='2025-11-27 21:50:05',
		*      lockState='1', deptno='A001',
		*      allowIps='192.168.1.1,0:0:0:0:0:0:0:1,192.168.1.126,192.168.183.1,192.168.183.1192.168.1.1,192.168.1.2,127.0.0.1,0:0:0:0:0:0:0:1',
		*      createtime='2018-11-22 12:11:40',
		*      createBy='李四',
		*      editTime='null', editBy='null'}
		**/
		System.out.println("--------------- user.toString() ---------------");
		/**
		 * 向数据库中写入数据的时候 ，使用 try{}catch{} 包裹住写数据语句
		 * 成功：返回受影响的记录条数 失败：返回 0
		 **/
		ReturnObject returnObject = new ReturnObject();
		try{
			// 调用 service 层方法，保存市场活动
			int ret = activityService.saveCreateActivity(activity);

			// 受影响的记录条数 > 0
			if (ret>0){
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			}else {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统忙，请稍后再试......");
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return returnObject;
	}

	/*
	 * 分页查询数据
	 * PageNo：第几页，pageSize：每页显示的条数
	 * @date:   2025/6/24 22:57
	 **/
	@RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
	public @ResponseBody Object queryActivityByConditionForPage(String name,String owner,
																String startDate,String endDate,
																int pageNo,int pageSize){

		System.out.println("===================奇怪，打印不出来这个参数======================");
		System.out.println("name:"+name+",owner："+owner+",startDate："+startDate+
				",endDate："+endDate+",pageNo："+pageNo+",pageSize："+pageSize);
		System.out.println("========================================================");
		//封装参数
		Map<String,Object> map=new HashMap<>();
		map.put("name",name);
		map.put("owner",owner);
		map.put("startDate",startDate);
		map.put("endDate",endDate);
		map.put("beginNo",(pageNo-1)*pageSize);
		map.put("pageSize",pageSize);

		//调用service层方法，查询数据
		List<Activity> activityList=activityService.queryActivityByConditionForPage(map);
		int totalRows=activityService.queryCountOfActivityByCondition(map);
		//根据查询结果结果，生成响应信息
		Map<String,Object> retMap=new HashMap<>();
		retMap.put("activityList",activityList);
		retMap.put("totalRows",totalRows);
		return retMap;
	}

	/**
	 * 删除数据
	 * @date:   2025/7/3 22:17
	 **/
	@RequestMapping("/workbench/activity/deleteActivityIds.do")
	@ResponseBody
	public Object deleteActivityIds(String[] id){

		ReturnObject returnObject = new ReturnObject();

		try{
			// 调用service层方法，删除市场活动请求
			int ret = activityService.deleteActivityByIds(id);
			if(ret>0){
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			}else {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统忙碌，请稍后再试！");
			}

		}catch (Exception e){
			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统忙碌，请稍后再试！");

		}

		return returnObject;

	};

	/**
	 * 根据id查询市场活动
	 * @date:   2025/7/7 23:01
	 **/
	@RequestMapping("/workbench/activity/queryActivityById.do")
	@ResponseBody
	public Object queryActivityById(String id){
		// 调用service层查询市场活动
		Activity activity = activityService.queryActivityById(id);
		// 根据查询结果，返回查询信息
		return activity;
	}



	/**
	 * 保存修改数据信息
	 * @date:   2025/7/13 18:02
	 **/
	@ResponseBody
	@RequestMapping("/workbench/activity/saveEditActivity.do")
	public Object saveEditActivity(Activity activity,HttpSession session){
		User user = (User)session.getAttribute(Contants.SESSION_USER);
		// 进一步封装参数
		activity.setEditTime(DateUtils.formateDatTIme(new Date()));
		activity.setEditBy(user.getId());
		ReturnObject returnObject = new ReturnObject();
		// 调用service层方法，保存市场活动
		try{
			int ret = activityService.saveEditActivty(activity);
			if(ret>0){
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			}else{
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统忙，请稍后！");
			}
		}catch (Exception e){
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统忙，请稍后！");
		}

		return returnObject;
	}

}