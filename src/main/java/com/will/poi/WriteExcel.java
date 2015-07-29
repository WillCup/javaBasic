package com.will.poi;

import java.io.FileOutputStream;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
 

/**
 * Excel文件数据的写入
 *
 * @author yaohucaizi
 */
public class WriteExcel {

	public void write(String filePath) throws Exception {
		List<List<String>> dateList = new ListSource().listSource();
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("sheet");// 添加sheet
		// 表格样式
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 指定单元格居中对齐
		// // 边框
		// style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		// style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		// style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		// style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		// //设置字体
		// HSSFFont f = wb.createFont();
		// f.setFontHeightInPoints((short)10);
		// f.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// style.setFont(f);
		// //设置列宽
		// sheet.setColumnWidth((short)0, (short)9600);
		// sheet.setColumnWidth((short)1, (short)4000);
		// sheet.setColumnWidth((short)2, (short)8000);
		// sheet.setColumnWidth((short)3, (short)8000);

		// 在索引0的位置创建第一行

		for (int i = 0; i < dateList.size(); i++) {
			HSSFRow row = sheet.createRow(i);
			List<String> list = dateList.get(i);
			for (int j = 0; j < list.size(); j++) {
				HSSFCell cell = row.createCell(j);
				cell.setCellValue(list.get(j));
				cell.setCellStyle(style);
			}
		}
		// 导出文件
		FileOutputStream fout = new FileOutputStream(filePath);
		wb.write(fout);
		fout.close();
	}

	public static void main(String[] args) throws Exception {
		WriteExcel we = new WriteExcel();
		we.write("F:/群组.xls");
	}
}