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

}
