package com.bjpowernode.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * 保存操作excel的工具
 * @date: 2025/7/16 15:29
 */
public class HSSFUtils {

	/**
	 * 从指定的cell中获取列的值，所有的值以String的形式返回
	 * @date:   2025/7/16 15:13
	 **/
	public static String getCellValueForStr(HSSFCell cell){

		String ret = "";
		// 获取列中的数据
		if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
			ret=cell.getStringCellValue();
		}else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
			// 强转，将不同类型的数据都转换为String类型的数据进行返回
			ret=Double.toString(cell.getNumericCellValue());
		}else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN){
			ret=cell.getBooleanCellValue()+"";
		}else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA){
			ret=cell.getCellFormula()+"";
		}else {
			ret="";
		}
		return ret;
	}

}