package com.bjpowernode.crm.commons.utils;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;

import java.util.UUID;

/**
 * @date: 2025/6/16 7:16
 */
public class UUIDUtils {

	public static String getUUID(){

		// 使用 JDK 生成一个 32 字符串作为 id
		return UUID.randomUUID().toString().replaceAll("-","");
	}

}