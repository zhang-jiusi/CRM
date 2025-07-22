package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 市场活动的相关实现
 *
 * @date: 2025/6/15 23:26
 */
@Service("activityServiceImpl")
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityMapper activityMapper;


	/**
	 * 保存创建的市场活动
	 * @date:   2025/6/22
	 **/
	public int saveCreateActivity(Activity activity) {
		// 返回插入数据的0.1结果
		return activityMapper.insertActivity(activity);
	}

	/**
	 * 根据条件分页查询市场活动列表
	 * @date:   2025/6/23 10:51
	 **/
	@Override
	public List<Activity> queryActivityByConditionForPage(Map<String, Object> map) {
		return activityMapper.selectActivityByConditionForPage(map);
	}

	/**
	 * 根据条件查询所有条目总和
	 * @date:   2025/6/24 22:52
	 **/
	@Override
	public int queryCountOfActivityByCondition(Map<String, Object> map) {
		return activityMapper.selectCountOfActivityByCondition(map);
	}

	/**
	 * 根据ids数组批量删除市场活动
	 * @date:   2025/7/2 23:29
	 **/
	public int deleteActivityByIds(String[] ids){

		return activityMapper.deleteActivityByIds(ids);
	}

	/**
	 * 根据id查询市场活动
	 * @date:   2025/7/6 23:07
	 **/
	@Override
	public Activity queryActivityById(String id) {
		return activityMapper.selectActivityById(id);
	}

	/**
	 * 保存更新数据
	 * @date:   2025/7/12 23:17
	 **/
	@Override
	public int saveEditActivty(Activity activty) {
		return activityMapper.updateActivity(activty);
	}

	/**
	 * 查询所有的市场活动
	 * @date:   2025/7/15 11:17
	 **/
	@Override
	public List<Activity> queryAllActivitys() {
		return activityMapper.selectAllActivitys();
	}

	/**
	 * 根据id数组查询相对应的记录
	 * @date:   2025/7/15 19:42
	 **/
	@Override
	public List<Activity> queryActivityByIds(String[] ids) {
		return activityMapper.selectActivityByIds(ids);
	}

	/**
	 * 批量插入市场活动
	 * @date:   2025/7/16 16:16
	 **/
	@Override
	public int saveCreateActivityByList(List<Activity> activityList) {
		return activityMapper.insertActivityByList(activityList);
	}

	/**
	 * 根据id查询市场活动详情
	 * @date:   2025/7/18 10:10
	 **/
	@Override
	public Activity queryActivityForDetailById(String id) {
		return activityMapper.selectActivityForDetailById(id);
	}
}