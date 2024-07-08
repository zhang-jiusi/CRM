package com.bjpowernode.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @date: 2024/7/7 17:06
 */
public class DateUtils {

	/**
	 * 将日期转换为指定的字符串格式
	 **/
	public static String formateDatTIme(Date date){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(date);
		return dateStr;

	}


}