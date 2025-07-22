package com.bjpowernode.crm.poi;

import com.bjpowernode.crm.commons.utils.HSSFUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 使用poi解析excel文件
 * @date: 2025/7/16 11:14
 */
public class ParseExcelTest {
	public static void main(String[] args) throws IOException {
		// 根据 excel 文件生成HSSFworkbook对象，封装excel文件的所有信息
		FileInputStream is = new FileInputStream("C:\\Users\\Administrator\\Desktop\\测试用表.xls");
		HSSFWorkbook wb = new HSSFWorkbook(is);
		// 根据wb获取到的HSSFSheet对象，封装了一页的所有信息
		HSSFSheet sheet = wb.getSheetAt(0);
		// 根据sheet获取到的HSSFRow对象，封装了一行的所有信息
		HSSFRow row = null;
		HSSFCell cell = null;
		for (int i=0;i<=sheet.getLastRowNum();i++){  // sheet.getLastRowNum()最后一行的下标
			row = sheet.getRow(i);
			for (int j=0;j<row.getLastCellNum();j++){ //row.getLastCellNum(),最后一列的编号+1，是总列数
				// 根据row获取HSSFCell的对象，封装了一列的所有信息
				cell = row.getCell(j);  //列的下标，从0开始，依次增加

				// 获取列中的数据
				System.out.print(HSSFUtils.getCellValueForStr(cell));
			}
			// 每一行中所有数据打印完成后，回车换行
			System.out.println();

		}
	}

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