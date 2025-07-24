package com.bjpowernode.crm.setting.service.impl;

import com.bjpowernode.crm.setting.domain.DicValue;
import com.bjpowernode.crm.setting.mapper.DicValueMapper;
import com.bjpowernode.crm.setting.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @date: 2025/7/23 1:33
 */
@Service("dicValueService")
public class DicValueServiceImpl implements DicValueService {

	@Autowired
	private DicValueMapper dicValueMapper;

	/**
	 * 通过 TypeCodeId 查询 DictValue
	 * @date:   2025/7/23 1:38
	 **/
	@Override
	public List<DicValue> queryDictValueByTypeCode(String typeCode) {
		return dicValueMapper.selectDicValueByTypeCode(typeCode);
	}
}