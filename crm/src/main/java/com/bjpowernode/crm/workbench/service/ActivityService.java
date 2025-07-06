package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 市场活动的相关实现接口
 *
 * @date: 2025/6/15 23:23
 */
public interface ActivityService {
	/**
	 * 保存创建的市场活动
	 * @date:   2025/6/22
	 **/
	int saveCreateActivity(Activity activity);

	/*
	 * 根据条件分页查询市场活动列表
	 * @date:   2025/6/23 10:50
	 **/
	List<Activity> queryActivityByConditionForPage(Map<String,Object> map);

	/**
	 * 根据条件查询所有条目总和
	 * @date:   2025/6/24 22:51
	 **/
	int queryCountOfActivityByCondition(Map<String,Object> map);

	/**
	 * 根据ids数组批量删除市场活动
	 * @date:   2025/7/2 23:30
	 **/
	int deleteActivityByIds(String[] ids);
}
