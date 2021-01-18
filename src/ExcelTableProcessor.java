import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import java.util.*; 
import java.math.*;

public class ExcelTableProcessor 
{
	public static Workbook workbook;
	public static Sheet sheet;
	public static FileInputStream fileInput;
	public static FileOutputStream fileOuput;
	public static Row row;
	public static Cell cell;
	
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
				String [] FriendsList = sheet.getRow(row).getCell(5).getStringCellValue().split("_");
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
	
	private void InitialiseStopper(int row) throws Exception
	{
		WriteToDatabase(row, 50, "END");
	}
	private List<Integer> ReturnAllRowsOfPeopleWithActivities() throws Exception
	{	
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		List<Integer> RowsOfPeople = new ArrayList<>();
		int numberOfRows = 1 + sheet.getLastRowNum();
		
		for(int i = 0; i < numberOfRows; i++)
		{
			if(sheet.getRow(i) != null)
			{
				if(sheet.getRow(i).getCell(6) != null)
				{
					if(sheet.getRow(i).getCell(6).getStringCellValue().equals("true"))
					{
						RowsOfPeople.add(i);
					}
				}
			}
		}
		
		return RowsOfPeople;
	}
	
	private float[] ParseLatLng(String LatLng)
	{
		float[] floatLatLng = new float[2];
		String floatString = "";
		try
		{
			floatString = LatLng.substring(10, LatLng.length()-1);
		}
		
		catch (IndexOutOfBoundsException e)
		{
			floatLatLng[0] = -1000f;
			floatLatLng[1] = -1000f;
			return floatLatLng;
		}
		String[] splitString = floatString.split(",");
		floatLatLng[0] = Float.parseFloat(splitString[0]);
		floatLatLng[1] = Float.parseFloat(splitString[1]);
		return floatLatLng;
	}
	
	private float GetDistanceBetweenTwoUsersLatLng(String user1coords, String user2coords)
	{
		float[] parsedFloats = ParseLatLng(user1coords);
		float LatOfUser1 = parsedFloats[0];
		float LngOfUser1 = parsedFloats[1];
		
		parsedFloats = ParseLatLng(user2coords);
		float LatOfUser2 = parsedFloats[0];
		float LngOfUser2 = parsedFloats[1];
		
		if(LatOfUser1 != -1000f && LatOfUser2 != -1000f && LngOfUser1 != -1000f && LngOfUser2 != -1000f)
		{
			//ACOS(SIN(PI()*[Lat_start]/180.0)*SIN(PI()*[Lat_end]/180.0)+COS(PI()*[Lat_start]/180.0)*COS(PI()*[Lat_end]/180.0)*COS(PI()*[Long_start]/180.0-PI()*[Long_end]/180.0))*6378
			double distance =  Math.acos(Math.sin(Math.PI*LatOfUser1/180)
										*Math.sin(Math.PI*LatOfUser2/180)
										+Math.cos(Math.PI*LatOfUser1/180)
										*Math.cos(Math.PI*LatOfUser2/180)
										*Math.cos(Math.PI*LngOfUser1/180 - Math.PI*LngOfUser2/180)
										)
										*6378;
			System.out.println(distance);
			return (float) distance;
		}
		else
		{
			System.out.println("User does not have viable coordinates!");
			return -1f;
		}
	}
	
	//Methods called by ProcessUserInput
	
	public String Login(String username, String passwort) throws Exception
	{
		System.out.println("Attempting Login of User_ "+ username);
		
		String loginOutputMessage = "login_";
		
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
							WriteToDatabase(FindRowOfPerson(username), 6, "false"); //LaufStatus
							break;
						}
						else
						{
							loginOutputMessage += "failed_password";
						}
					}
				}
			}
		}
		if(!usernameExists)
		{
			loginOutputMessage += "failed_username";
		}
		
		
		return loginOutputMessage;
	}
	
	public String RegisterPerson(String username, String passwort, String firstName, String lastName, String age) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		System.out.println("Registering a new User!");
		int numberOfRows = 1 + sheet.getLastRowNum();

		System.out.println(numberOfRows);
		int RowOfExistingUsername = FindRowOfPerson(username);
		if(RowOfExistingUsername != -1)
		{
			return "registration_failed_username";
		}
		
		WriteToDatabase(numberOfRows, 0, username);
		WriteToDatabase(numberOfRows, 1, passwort);
		WriteToDatabase(numberOfRows, 2, firstName);
		WriteToDatabase(numberOfRows, 3, lastName);
		WriteToDatabase(numberOfRows, 4, age);
		WriteToDatabase(numberOfRows, 6, "false"); //LaufStatus
		WriteToDatabase(numberOfRows, 13, "0"); //FitnessLevel
		
		InitialiseStopper(numberOfRows);
		
		return "registration_success";
	}
	
	//FRIENDS
	
	public String AddFriend(String person1, String person2) throws Exception
	{
		//adds person1 to person2's friendlist
		System.out.println("Attempting to Add "+ person1 + " to " + person2 + "'s FriendsList");
		
		if(person1.equals(person2))
		{
			return "Friend_Add_" + person1 + "_" + person2 + "_Failed_SamePerson";
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
				return "Friend_Add_" + person1 + "_" + person2 + "_"+ "Failed_AlreadyFriend";
			}
		}
		else
		{
			if(person1row == -1)
			{
				return "Friend_Add_" + person1 + "_" + person2 + "_Failed_user " + person1 + " doesnt exist";
			}
			else if(person2row == -1)
			{
				return "Friend_Add_" + person1 + "_" + person2 + "_Failed_user " + person2 + " doesnt exist";
			}
		}
		return "Friend_Add_" + person1 + "_" + person2 + "_Success";
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
			return "remove failed_ friend isnt in the list";
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
	public String GetFriendAllInfo(String user) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
	
		int row = FindRowOfPerson(user);
		if(row != -1)
		{
			String returnString = "";
			for(int i = 0; i < sheet.getRow(row).getLastCellNum(); i++)
			{
				if(sheet.getRow(row).getCell(i) != null)
				{
					returnString += sheet.getRow(row).getCell(i).getStringCellValue() + "_";
				}
				else
				{
					returnString += "null_";
				}
			
			}
			return returnString;
		}
		else
		{
			return "Person not found!";		
		}
	}
	
	//LAUFEN
	
	public String StartRun(String time, String user, String location) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		int row = FindRowOfPerson(user);
		if(row != -1)
		{
			WriteToDatabase(row, 6, "true");
			WriteToDatabase(row, 7, location);
			WriteToDatabase(row, 8, time);
			return "Run Started!";
		}
		else
		{
			return "Person does not exist";
		}
	}
	public String StopRun(String user) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
	
		int row = FindRowOfPerson(user);
		if(row != -1)
		{
			WriteToDatabase(row, 6, "false");
			WriteToDatabase(row, 7, "");
			WriteToDatabase(row, 8, "");
			return "Run Stopped!";
		}
		else
		{
			return "Person does not exist";
		}
	}
	public String JoinRun() throws Exception
	{
		return "";
	}

	public String ReturnPeopleWithActivitiesInArea(String user, float maxDistance) throws Exception
	{	
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		int rowOfUser = FindRowOfPerson(user);
		if(rowOfUser == -1)
		{
			return "UserNotFound!";
		}
		else
		{
			String peopleInArea = "";
			List<Integer> PeopleRowList = ReturnAllRowsOfPeopleWithActivities();
			for(int i : PeopleRowList)
			{
				if(i == rowOfUser)
				{
					continue;
				}
				if(sheet.getRow(i).getCell(7) != null)
				{
					float UserDistance = GetDistanceBetweenTwoUsersLatLng(sheet.getRow(rowOfUser).getCell(7).getStringCellValue(), sheet.getRow(i).getCell(7).getStringCellValue());
					if(UserDistance != -1 && UserDistance <= maxDistance)
					{
						peopleInArea += sheet.getRow(i).getCell(0).getStringCellValue() + "_";
					}
					
				}

			}
			return peopleInArea;
		}
		
	}
}
	
	    