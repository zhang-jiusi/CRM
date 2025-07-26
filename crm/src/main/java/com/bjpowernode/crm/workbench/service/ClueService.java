package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;

/**
 * @date: 2025/7/23 23:43
 */
public interface ClueService {

	/**
	 * 保存创建的线索
	 * @date:   2025/7/23 23:44
	 **/
	 public int saveCreateClue(Clue clue);

	 /*
	  * 通过id查询你市场活动明细
	  * @date:   2025/7/24 23:35
	  **/
	 Clue queryClueForDatailById(String id);

}
