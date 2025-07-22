package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.mapper.ActivityRemarkMapper;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @date: 2025/7/18 11:35
 */
@Service("activityRemarkService")
public class ActivityRemarkServiceImpl implements ActivityRemarkService {

	@Autowired
	private ActivityRemarkMapper activityRemarkMapper;

	/**
	 * 根据市场活动id查询活动备注的信息
	 * @date:   2025/7/18 11:39
	 **/
	@Override
	public List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String activityId) {
		return activityRemarkMapper.selectActivityRemarkForDetailByActivityId(activityId);
	}

	/**
	 * 插入市场活动信息
	 * @date:   2025/7/18 22:40
	 **/
	@Override
	public int saveCreateActivityRemark(ActivityRemark activityRemark) {
		return activityRemarkMapper.insertActivityRemark(activityRemark);
	}

	/**
	 * 删除市场活动备注信息
	 * @date:   2025/7/20 0:35
	 **/
	@Override
	public int deleteActivityRemarkById(String id) {
		return activityRemarkMapper.deleteActivityRemarkById(id);
	}

	/**
	 * 保存市场活动的修改内容
	 * @date:   2025/7/21 1:13
	 **/
	public  int updateActivityRemark(ActivityRemark activityRemark){
		return activityRemarkMapper.updateActivityRemark(activityRemark);
	}
}