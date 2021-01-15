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
		String loginOutputMessage = "login:";
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		System.out.println("Attempting Login of User: "+ username);
		int numberOfRows = 1 + sheet.getLastRowNum();

		boolean usernameExists = false;
		for(int i = 0; i < numberOfRows; i++)
		{
			if(sheet.getRow(i) != null)
			{
				if(sheet.getRow(i).getCell(0) != null)
				{
					if(sheet.getRow(i).getCell(0).getStringCellValue().equals(username))
					{
						usernameExists = true;
						if(sheet.getRow(i).getCell(1).getStringCellValue().equals(passwort))
						{
							loginOutputMessage += "success";
							break;
						}
						else
						{
							loginOutputMessage += "failed:password";
						}
					}
				}
			}
		}
		if(!usernameExists)
		{
			loginOutputMessage += "failed:username";
		}
		
		return loginOutputMessage;
	}
	
	public String AddNewPersonToDatabase(String username, String passwort, String firstName, String lastName, String age) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		System.out.println("Registering a new User!");
		int numberOfRows = 1 + sheet.getLastRowNum();
		for(int i = 0; i < numberOfRows; i++)
		{
			if(sheet.getRow(i) != null)
			{
				if(sheet.getRow(i).getCell(0) != null)
				{
					if(sheet.getRow(i).getCell(0).getStringCellValue().equals(username))
					{
						return "registration:failed:username";
					}
				}
			}
		}
		
		WriteToDatabase(numberOfRows, 0, username);
		WriteToDatabase(numberOfRows, 1, passwort);
		WriteToDatabase(numberOfRows, 2, firstName);
		WriteToDatabase(numberOfRows, 3, lastName);
		WriteToDatabase(numberOfRows, 4, age);
		
		return "registration:success";
	}
	
	private void WriteToDatabase(int row, int cell, String text) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		if(sheet.getRow(row) == null) //check to make sure the row we are trying to access actually exists
		{
			sheet.createRow(row).createCell(cell).setCellValue(text); //and if not, we need to create it first instead of accesing it
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
	
	    