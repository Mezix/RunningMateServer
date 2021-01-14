import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import org.apache.poi.ss.usermodel.*;

public class ExcelTableProcessor 
{
	public static Workbook workbook;
	public static Sheet sheet;
	public static FileInputStream fileInput;
	public static FileOutputStream fileOuput;
	public static Row row;
	public static Cell cell;

	public static void main(String[] args) throws Exception
	{
		System.out.println("Starting ExcelTableProcessor");
		
		fileInput=new FileInputStream("./data.xlsx");
		
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		int numberOfRows = sheet.getLastRowNum();
		System.out.println(numberOfRows);
		
		row = sheet.createRow(5);
		cell = row.createCell(0);
		cell.setCellValue("TEST");
		
		fileOuput=new FileOutputStream("./data.xlsx");
		workbook.write(fileOuput);
		fileOuput.flush();
		fileOuput.close();
		System.out.println("Data entered in a Data excel file");
	}
}
	
	    