package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 市场活动的相关实现
 *
 * @date: 2025/6/15 23:26
 */
@Service("activityServiceImpl")
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityMapper activityMapper;

	// 保存创建的市场活动
	public int saveCreateActivity(Activity activity) {
		// 返回插入数据的0.1结果
		return activityMapper.insertActivity(activity);
	}
}