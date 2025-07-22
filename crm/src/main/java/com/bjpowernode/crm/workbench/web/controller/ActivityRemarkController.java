package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.setting.domain.User;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @date: 2025/7/18 21:26
 */
@Controller
public class ActivityRemarkController {

	@Autowired
	private ActivityRemarkService activityRemarkService;

	/**
	 * 保存备注评论内容
	 * @date:   2025/7/19 10:43
	 **/
	@RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
	@ResponseBody
	public Object saveCreateActivityRemark(ActivityRemark activityRemark, HttpSession session){
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		// 对 ActivityRemark 参数进行二次封装
		activityRemark.setId(UUIDUtils.getUUID());
		activityRemark.setCreateTime(DateUtils.formateDatTIme(new Date()));
		activityRemark.setCreateBy(user.getId());
		activityRemark.setEditFlag(Contants.REMARK_EDIT_FlAG_NO_EDITED);

		System.out.println("======================activityRemark======================");
		System.out.println(activityRemark.getActivityId());
		System.out.println(activityRemark.getNoteContent());
		System.out.println("======================activityRemark======================");
		ReturnObject returnObject = new ReturnObject();

		try{
			// 调用 service 层方法，保存创建的市场活动备注
			int i = activityRemarkService.saveCreateActivityRemark(activityRemark);
			if(i>0){
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
				returnObject.setRetData(activityRemark);
			}else {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("保存创建市场活动备注失败！");
			}
		}catch (Exception e){
			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("保存创建市场活动备注失败！");

		}
		return returnObject;
	}

	/**
	 * 根据id删除市场活动
	 * @date:   2025/7/21 1:17
	 **/
	@ResponseBody
	@RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
	public Object deleteActivityRemarkById(String id){

		ReturnObject returnObject = new ReturnObject();

		try {
			// 调用 service 方法，删除数据
			int i = activityRemarkService.deleteActivityRemarkById(id);
			if (i>0){
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
				returnObject.setMessage("成功删除数据");
			}else {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("失败删除数据");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统忙碌，请稍后再试！");
		}

		return returnObject;

	}

	/**
	 * 保存市场活动修改后的数据内容
	 * @date:   2025/7/21 1:18
	 **/
	@ResponseBody
	@RequestMapping("/workbench/activity/saveEditActivityRemark.do")
	public Object saveEditActivityRemark(ActivityRemark activityRemark,HttpSession session){
		User user = (User)session.getAttribute(Contants.SESSION_USER);
		// 封装参数
		activityRemark.setEditFlag(Contants.REMARK_EDIT_FlAG_YES_EDITED);
		activityRemark.setEditBy(user.getId());
		activityRemark.setEditTime(DateUtils.formateDatTIme(new Date()));
		System.out.println("=====================user.getId()");
		System.out.println("activityRemark.getEditBy()"+activityRemark.getEditFlag());
		System.out.println("activityRemark.getEditBy()"+activityRemark.getEditBy());
		System.out.println("activityRemark.getEditBy()"+activityRemark.getEditTime());
		int i = 0;
		ReturnObject returnObject = new ReturnObject();
		try {
			// 调用 service
			i = activityRemarkService.updateActivityRemark(activityRemark);

			// 根据处理结果，返回响应信息
			if(i>0){
				System.out.println("受影响的行数："+i);
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
				returnObject.setRetData(activityRemark);
			}else {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("修改市场活动信息的备注，处理结果失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("修改市场活动信息的备注，处理结果失败！");
		}

		return returnObject;
	}


}