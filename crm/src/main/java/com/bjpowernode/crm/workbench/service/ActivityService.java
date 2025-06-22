package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;
import org.springframework.stereotype.Service;

/**
 * 市场活动的相关实现接口
 *
 * @date: 2025/6/15 23:23
 */
public interface ActivityService {
	// 保存创建的市场活动
	int saveCreateActivity(Activity activity);
}
