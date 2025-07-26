package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

import java.util.List;

/**
 * @date: 2025/7/25 12:17
 */
public interface ClueRemarkService {

	/**
	 * 根据clue线索的id，查询该id线索下的备注信息
	 * @date:   2025/7/25 12:18
	 **/
	List<ClueRemark> queryClueRemarkForDetaidByClueId(String clueId);
}
