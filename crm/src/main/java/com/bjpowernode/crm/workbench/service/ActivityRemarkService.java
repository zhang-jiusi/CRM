package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;

/**
 * @date: 2025/7/18 11:34
 */
public interface ActivityRemarkService {

	/**
	 * 根据市场活动id查询活动备注的信息
	 * @date:   2025/7/18 11:37
	 **/
	List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String activityId);

	/**
	 * 插入市场活动信息
	 * @date:   2025/7/18 22:41
	 **/
	int saveCreateActivityRemark(ActivityRemark activityRemark);

	/**
	 * 删除市场活动备注信息
	 * @date:   2025/7/20 0:34
	 **/
	int deleteActivityRemarkById(String id);

	/**
	 * 保存市场活动的修改内容
	 * @date:   2025/7/21 1:13
	 **/
	public  int updateActivityRemark(ActivityRemark activityRemark);

}
