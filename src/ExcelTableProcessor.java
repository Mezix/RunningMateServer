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
	public String Login(String username, String passwort) throws Exception
	{
		boolean loginSucceeded = false;
		
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		System.out.println("Adding New Person");
		int numberOfRows = 1 + sheet.getLastRowNum();
		for(int i = 0; i < numberOfRows; i++)
		{
			if(sheet.getRow(i) != null)
			{
				if(sheet.getRow(i).getCell(3) != null)
				{
					if(sheet.getRow(i).getCell(3).getStringCellValue().equals(username))
					{
						if(sheet.getRow(i).getCell(4).getStringCellValue().equals(passwort))
						{
							loginSucceeded = true;
							break;
						}
					}
				}
			}
		}
		
		if(loginSucceeded)
		{
			return"Login:Success";
		}
		else
		{
			return "Login:Failed";
		}
	}
	
	public String AddNewPersonToDatabase(String firstName, String lastName, String age, String username, String passwort) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		System.out.println("Adding New Person");
		int numberOfRows = 1 + sheet.getLastRowNum();
		for(int i = 0; i < numberOfRows; i++)
		{
			if(sheet.getRow(i) != null)
			{
				if(sheet.getRow(i).getCell(3) != null)
				{
					if(sheet.getRow(i).getCell(3).getStringCellValue().equals(username))
					{
						return "Already Added a person with this Name!";
					}
				}
			}
		}
		
		WriteToDatabase(numberOfRows, 0, firstName);
		WriteToDatabase(numberOfRows, 1, lastName);
		WriteToDatabase(numberOfRows, 2, age);
		WriteToDatabase(numberOfRows, 3, username);
		WriteToDatabase(numberOfRows, 4, passwort);
		
		
		System.out.println("Data entered in a Data excel file");
		
		return "Added Person: '" + username + "' to the Database (or at least the excel spreadsheet)!";
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
	
	    