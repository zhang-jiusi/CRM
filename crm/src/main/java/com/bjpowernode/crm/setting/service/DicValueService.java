package com.bjpowernode.crm.setting.service;

import com.bjpowernode.crm.setting.domain.DicValue;

import java.util.List;

/**
 * @date: 2025/7/23 1:38
 */
public interface DicValueService {

	/**
	 * 通过 TypeCodeId 查询 DictValue
	 * @date:   2025/7/23 1:38
	 **/
	List<DicValue> queryDictValueByTypeCode(String typeCode);

}

