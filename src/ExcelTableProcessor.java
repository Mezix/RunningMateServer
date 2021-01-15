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
	
	//Helper Methods
	
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
	
	private int FindRowOfPerson(String username) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		int numberOfRows = 1 + sheet.getLastRowNum();
		for(int i = 0; i < numberOfRows; i++)
		{
			if(sheet.getRow(i) != null)
			{
				if(sheet.getRow(i).getCell(0) != null)
				{
					if(sheet.getRow(i).getCell(0).getStringCellValue().equals(username))
					{
						return i;
					}
				}
			}
		}
		return -1;
	}
	
	private boolean PersonExistsInFriendsList(String username, String otherUser) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		int row = FindRowOfPerson(otherUser);
		if(row != -1)
		{
			if(sheet.getRow(row).getCell(5) != null)
			{
				String [] FriendsList = sheet.getRow(row).getCell(5).getStringCellValue().split(";");
				for(int i = 0; i < FriendsList.length; i++)
				{
					if(FriendsList[i].equals(username))
					{
						System.out.println(username + " is already in " + otherUser + "'s Friendslist!");
						return true;
					}
				}
			}
		}
		return false;
	}
	//Methods called by ProcessUserInput
	
	public String Login(String username, String passwort) throws Exception
	{
		System.out.println("Attempting Login of User: "+ username);
		
		String loginOutputMessage = "login:";
		
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
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
		
		int RowOfExistingUsername = FindRowOfPerson(username);
		if(RowOfExistingUsername != -1)
		{
			return "registration:failed:username";
		}
		
		WriteToDatabase(numberOfRows, 0, username);
		WriteToDatabase(numberOfRows, 1, passwort);
		WriteToDatabase(numberOfRows, 2, firstName);
		WriteToDatabase(numberOfRows, 3, lastName);
		WriteToDatabase(numberOfRows, 4, age);
		
		return "registration:success";
	}
	
	public String AddFriend(String person1, String person2) throws Exception
	{
		//adds person1 to person2's friendlist
		System.out.println("Attempting to Add "+ person1 + " to " + person2 + "'s FriendsList");
		
		if(person1.equals(person2))
		{
			return "Friend:Add:" + person1 + ":" + person2 + ":Failed:SamePerson";
		}
		
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		int person1row  = FindRowOfPerson(person1);
		int person2row = FindRowOfPerson(person2);
		
		if(person1row != -1 && person2row != -1)
		{	
			if(!PersonExistsInFriendsList(person1, person2))
			{
				String FriendsList = "";
				if(sheet.getRow(person2row).getCell(5) != null)
				{
					FriendsList = sheet.getRow(person2row).getCell(5).getStringCellValue();
				}
				WriteToDatabase(person2row, 5, FriendsList + person1 + ";");
			}
			else
			{
				return "Friend:Add:" + person1 + ":" + person2 + ":"+ "Failed:AlreadyFriend";
			}
		}
		else
		{
			if(person1row == -1)
			{
				return "Friend:Add:" + person1 + ":" + person2 + ":Failed:user " + person1 + " doesnt exist";
			}
			else if(person2row == -1)
			{
				return "Friend:Add:" + person1 + ":" + person2 + ":Failed:user " + person2 + " doesnt exist";
			}
		}
		return "Friend:Add:" + person1 + ":" + person2 + ":Success";
	}
	
	public String RemoveFriend(String person1, String person2) throws Exception
	{
		String friendslist = GetFriendList(person2);
		if(friendslist.equals(""))
		{
			return "friends list empty";
		}
		String[] splitString = friendslist.split(";");
		if(PersonExistsInFriendsList(person1, person2))
		{
			String newFriendslist = "";
			for(int i = 0; i < splitString.length; i++)
			{
				if(!splitString[i].equals(person1))
				{
					newFriendslist += splitString[i] + ";";
				}
			}
			int rowOfPerson2 = FindRowOfPerson(person2);
			if(rowOfPerson2 != -1)
			{
				WriteToDatabase(rowOfPerson2, 5, newFriendslist);
				return "Person 1 removed from person 2's list!";
			}
			else
			{
				return "Person 2 doesnt exist";
			}
		}
		else
		{
			return "remove failed: friend isnt in the list";
		}
		
	}
	public String GetFriendList(String username) throws Exception
	{
		int rowOfUser = FindRowOfPerson(username);
		if(rowOfUser != -1)
		{
			if(sheet.getRow(rowOfUser).getCell(5) != null)
			{
				return sheet.getRow(rowOfUser).getCell(5).getStringCellValue();
			}
			else
			{
				return "";
			}
		}
		else
		{
			return username + " does not exist!";
		}
	}
}
	
	    