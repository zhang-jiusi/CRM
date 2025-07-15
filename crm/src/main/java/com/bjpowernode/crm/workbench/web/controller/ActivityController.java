package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.setting.domain.User;
import com.bjpowernode.crm.setting.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.sun.deploy.net.HttpResponse;
import com.sun.tools.javac.jvm.ByteCodes;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

import static com.sun.tools.javac.jvm.ByteCodes.bool_not;
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

	/**
	 *  使用 filedownloadtest.jsp 演示文件下载,并非主体功能
	 *  文件下载
	 *
	 * 注意：返回网页，		用String类型的返回值，
	 * 		返回JSON的时候， 用Object类型的返回值
	 * 		返回文件的时候， 不借助返回类型返回数据，使用流输出文件信息
	 * @date:   2025/7/14 18:47
	 **/
	@RequestMapping("/workbench/activity/fileDownload.do")
	public void fileDownload(HttpServletResponse response) throws IOException {

		/* 读服务器中的excel文件 */
		// 1.设置响应信息
		response.setContentType("application/octet-stream;charset=UTF-8");

		// 2.获取输出流

		/**
		 * 浏览器在接收到响应信息时候，
		 * 默认情况下，直接显示在窗口中的打开响应信息，
		 * 即使打不开，也会调用应用程序打开，只有实在打不开，才会激活文件下载窗口
		 *
		 * 可以设置响应头信息，使得浏览器在收到响应信息后，直接激活文件下载床口，即使能也不打开
		 **/
		response.addHeader("Content-Disposition","attachement;filename=myStudenList.xls");

		ServletOutputStream out = response.getOutputStream();
		// 读取excel文件（InputStream），把输出到浏览器（OutputStream）
		FileInputStream is = new FileInputStream("C:\\Users\\Administrator\\Desktop\\1010 Studio.xlsx");
		byte[] buff = new byte[256];
		int len = 0;
		while ((len=is.read(buff))!=-1){
			out.write(buff,0,len);
		}


		is.close();
		out.flush();

	}

	/**
	 * 导出全部市场活动
	 * @date:   2025/7/15 11:40
	 **/
	@RequestMapping("/workbench/activity/exportAllActivity.do")
	public void exportAllActivity(HttpServletResponse response) throws Exception {
		// 查询所有的市场活动
		List<Activity> activityList = activityService.queryAllActivitys();

		// 将查询到的数据写入到 excel 文件中
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("市场活动");
		HSSFRow row = sheet.createRow(0);// 行下标从0开始，用作表头

		HSSFCell cell = row.createCell(0);// 单元格？列下标，从0开始
		cell.setCellValue("id");

		cell = row.createCell(1);
		cell.setCellValue("owner");

		cell = row.createCell(2);
		cell.setCellValue("name");

		cell = row.createCell(3);
		cell.setCellValue("start_date");

		cell = row.createCell(4);
		cell.setCellValue("end_date");

		cell = row.createCell(5);
		cell.setCellValue("cost");

		cell = row.createCell(6);
		cell.setCellValue("description");

		cell = row.createCell(7);
		cell.setCellValue("create_time");

		cell = row.createCell(8);
		cell.setCellValue("create_by");

		cell = row.createCell(9);
		cell.setCellValue("edit_time");

		cell = row.createCell(10);
		cell.setCellValue("edit_by");

		Activity activity = null;
		// 遍历activityList数组，生成所有数据行
		if(activityList!=null && activityList.size()>0){
			for(int i=0;i<activityList.size();i++){
				activity = activityList.get(i);
				row = sheet.createRow(i+1);

				cell = row.createCell(0);// 单元格？列下标，从0开始
				cell.setCellValue(activity.getId());

				cell = row.createCell(1);
				cell.setCellValue(activity.getOwner());

				cell = row.createCell(2);
				cell.setCellValue(activity.getName());

				cell = row.createCell(3);
				cell.setCellValue(activity.getStartDate());

				cell = 	row.createCell(4);
				cell.setCellValue(activity.getEndDate());

				cell = cell = row.createCell(5);
				cell.setCellValue(activity.getCost());

				cell = row.createCell(6);
				cell.setCellValue(activity.getDescription());

				cell = row.createCell(7);
				cell.setCellValue(activity.getCreateTime());

				cell = row.createCell(8);
				cell.setCellValue(activity.getCreateBy());

				cell = row.createCell(9);
				cell.setCellValue(activity.getEditTime());

				cell = row.createCell(10);
				cell.setCellValue(activity.getEditBy());
			}
		}

		// 根据wb对象，生成excel对象
		/* 优化：不在将从数据库中查询到的数据写入到磁盘中，再通过输入流is，输出流os输出到前台页面中
		FileOutputStream os = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\ActivityList.xls");
		wb.write(os);
		// 关闭资源
		os.close();
		wb.close();
		*/

		// 把生成的excel文件下载到用户客户端
		// 1.设置响应信息
		response.setContentType("application/octet-stream;charset=UTF-8");
		response.addHeader("Content-Disposition","attachement;filename=myStudenList.xls");

		// 2.获取输出流
		OutputStream out = response.getOutputStream();
		// 3.创建输入流读取文件
		/*
		FileInputStream is = new FileInputStream("C:\\Users\\Administrator\\Desktop\\ActivityList.xls");
		byte[] buff = new byte[256];
		int len = 0;
		while ((len=is.read(buff))!=-1){
			out.write(buff,0,len);
		}
		is.close();
		*/
		wb.write(out);
		wb.close();
		out.flush();


	}

	/**
	 * 选择导出市场活动
	 * @date:   2025/7/15 17:27
	 **/
	@RequestMapping("/workbench/activity/exportXZActivity.do")
	public void exportXZActivity(HttpServletResponse response,@RequestParam("id")String[] ids) throws Exception {
		// 查询指定ids数组的市场活动信息
		System.out.println("========================进入 exportXZActivity =========================");
		if(ids==null){
			System.out.println("ids没有接收到参数");
		}
		System.out.println(ids.toString());
		System.out.println(ids.length);
		List<Activity> activityList = activityService.queryActivityByIds(ids);

		System.out.println("========================遍历 activityList 数组=========================");
		Activity activity1 = activityList.get(0);
		System.out.println(activity1.getId());
		System.out.println("========================遍历 activityList 数组=========================");
		// 将查询到的数据写入到 excel 文件中
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("市场活动");
		HSSFRow row = sheet.createRow(0);// 行下标从0开始，用作表头

		HSSFCell cell = row.createCell(0);// 列下标，从0开始
		cell.setCellValue("id");

		cell = row.createCell(1);
		cell.setCellValue("所有者");

		cell = row.createCell(2);
		cell.setCellValue("名称");

		cell = row.createCell(3);
		cell.setCellValue("开始事件");

		cell = row.createCell(4);
		cell.setCellValue("结束事件");

		cell = row.createCell(5);
		cell.setCellValue("成本");

		cell = row.createCell(6);
		cell.setCellValue("描述");

		cell = row.createCell(7);
		cell.setCellValue("创建时间");

		cell = row.createCell(8);
		cell.setCellValue("创建者");

		cell = row.createCell(9);
		cell.setCellValue("编辑时间");

		cell = row.createCell(10);
		cell.setCellValue("编辑者");

		Activity activity = null;
		// 遍历activityList数组，生成所有数据行
		if(activityList!=null && activityList.size()>0){
			for(int i=0;i<activityList.size();i++){
				activity = activityList.get(i);
				row = sheet.createRow(i+1);

				cell = row.createCell(0);
				cell.setCellValue(activity.getId());

				cell = row.createCell(1);
				cell.setCellValue(activity.getOwner());

				cell = row.createCell(2);
				cell.setCellValue(activity.getName());

				cell = row.createCell(3);
				cell.setCellValue(activity.getStartDate());

				cell = 	row.createCell(4);
				cell.setCellValue(activity.getEndDate());

				cell = cell = row.createCell(5);
				cell.setCellValue(activity.getCost());

				cell = row.createCell(6);
				cell.setCellValue(activity.getDescription());

				cell = row.createCell(7);
				cell.setCellValue(activity.getCreateTime());

				cell = row.createCell(8);
				cell.setCellValue(activity.getCreateBy());

				cell = row.createCell(9);
				cell.setCellValue(activity.getEditTime());

				cell = row.createCell(10);
				cell.setCellValue(activity.getEditBy());
			}
		}

		response.setContentType("application/octet-stream;charset=UTF-8");
		response.addHeader("Content-Disposition","attachement;filename=myStudenList.xls");

		// 2.获取输出流
		OutputStream out = response.getOutputStream();

		wb.write(out);
		wb.close();
		out.flush();


	}


	}