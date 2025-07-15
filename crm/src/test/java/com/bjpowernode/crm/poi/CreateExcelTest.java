package com.bjpowernode.crm.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileOutputStream;

/**
 * 使用 apache-poi
 * @date: 2025/7/14 16:25
 */
public class CreateExcelTest {

	public static void main(String[] args) throws Exception {

		// 创建HSSFWordbook对象，wb对应一个excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 使用wb创建HSSFSheet对象，sheet对应wb文件中的一页
		HSSFSheet sheet = wb.createSheet("学生列表页");
		// 使用sheet创建HSSRow对象，对应sheet中的一行
		HSSFRow row = sheet.createRow(0); // 行号，从0开始
		// 使用row创建HSSFCell对象，对应row中的列（我自己更想把他理解成，在一行中画几个单元格）
		HSSFCell cell = row.createCell(0);// 列的编号，从0开始
		cell.setCellValue("学号");

		cell = row.createCell(1);//
		cell.setCellValue("姓名");

		cell = row.createCell(2);//
		cell.setCellValue("年龄");

		// 生成HSSFCellStyle 对象，对xls表格进行修饰
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);  // 对齐方式，居中


		// 使用sheet创建10个HSSRow对象，对应sheet中10行
		for(int i=1;i<=10;i++){
			row = sheet.createRow(i);

			cell = row.createCell(0);
			cell = row.createCell(100+i);

			cell = row.createCell(1);
			cell.setCellValue("NAME"+i);

			cell = row.createCell(2);
			cell.setCellValue(20+i);
			// 将创建好的style指定给某一列
			cell.setCellStyle(style);
		}

		// 调用工具函数生成excel文件
		FileOutputStream os = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\Student.xls");
		wb.write(os);
		// 关闭资源
		os.close();
		wb.close();

		System.out.println("----------------------------------------------------");
	}
}