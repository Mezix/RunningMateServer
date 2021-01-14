import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;

public class ExcelTableProcessor 
{
	public static Workbook workbook;
	public static Sheet sheet;
	public static FileInputStream fileInput;
	public static FileOutputStream fileOuput;
	public static Row row;
	public static Cell cell;
	
	/*
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
	}*/
	
	public String AddNewPersonToDatabase(String UsernameToAdd, String Password, String DisplayName, String Age) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		System.out.println("Adding New Person");
		int numberOfRows = 1 + sheet.getLastRowNum();
		System.out.println( numberOfRows + " Rows");
		for(int i = 0; i < numberOfRows; i++)
		{
			if(sheet.getRow(i) != null)
			{
				if(sheet.getRow(i).getCell(0) != null)
				{
					if(sheet.getRow(i).getCell(0).getStringCellValue().equals(UsernameToAdd))
					{
						return "Already Added a person with this Name!";
					}
				}
			}
		}
		
		//Add Username
		WriteToDatabase(numberOfRows, 0, UsernameToAdd);
		//Add Password
		WriteToDatabase(numberOfRows, 1, Password);
		//Add Displayname
		WriteToDatabase(numberOfRows, 2, DisplayName);
		//Add Age
		WriteToDatabase(numberOfRows, 3, Age);
		
		
		System.out.println("Data entered in a Data excel file");
		
		return "Added Person: '" + UsernameToAdd + "' to the Database (or at least the excel spreadsheet)!";
	}
	
	private void WriteToDatabase(int row, int cell, String text) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		if(sheet.getRow(row) == null)
		{
			sheet.createRow(row).createCell(cell).setCellValue(text);
		}
		else
		{
			sheet.getRow(row).createCell(cell).setCellValue(text);
		}
		
		
		fileOuput=new FileOutputStream("./data.xlsx");
		workbook.write(fileOuput);
		fileOuput.flush();
		fileOuput.close();
	}
}
	
	    