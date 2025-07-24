package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.mapper.ClueMapper;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date: 2025/7/23 23:43
 */
@Service("clueService")
public class ClueServiceImpl implements ClueService {

	@Autowired
	ClueMapper clueMapper;

	/**
	 * 保存创建的线索
	 * @date:   2025/7/23 23:44
	 **/
	@Override
	public int saveCreateClue(Clue clue) {
		return clueMapper.insertClue(clue);
	}
}