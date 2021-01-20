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
	
	private int FindRowOfUser(String username) throws Exception
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
		
		int row = FindRowOfUser(otherUser);
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
	
	private boolean PersonExistsInActivityList(String userJoining, String userBeingJoined) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		int row = FindRowOfUser(userBeingJoined);
		if(row != -1)
		{
			if(sheet.getRow(row).getCell(9) != null)
			{
				String [] ActivityList = sheet.getRow(row).getCell(9).getStringCellValue().split(";");
				for(int i = 0; i < ActivityList.length; i++)
				{
					if(ActivityList[i].equals(userJoining))
					{
						//System.out.println(userJoining + " is already in " + userBeingJoined + "'s Activitylist!");
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
	
	private List<Integer> ReturnAllRowsWithActivities() throws Exception
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
			//System.out.println(distance);
			return (float) distance;
		}
		else
		{
			System.out.println("User does not have viable coordinates. Coordinates were; User1: " 
								+ LatOfUser1 + ", " + LngOfUser1 + " User2: "
								+ LatOfUser2 + ", " + LngOfUser2
								);
			return -1f;
		}
	}
	
	//Methods called by ProcessUserInputDirectly
	
	public String login(String username, String passwort) throws Exception
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
							//WriteToDatabase(FindRowOfPerson(username), 6, "false"); //stop users activity
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
	public String registerPerson(String username, String passwort, String firstName, String lastName, String age) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		System.out.println("Registering a new User!");
		int numberOfRows = 1 + sheet.getLastRowNum();

		System.out.println(numberOfRows);
		int RowOfExistingUsername = FindRowOfUser(username);
		if(RowOfExistingUsername != -1)
		{
			return "registration_failed_username";
		}
		
		WriteToDatabase(numberOfRows, 0, username);
		WriteToDatabase(numberOfRows, 1, passwort);
		WriteToDatabase(numberOfRows, 2, firstName);
		WriteToDatabase(numberOfRows, 3, lastName);
		WriteToDatabase(numberOfRows, 4, age);
		WriteToDatabase(numberOfRows, 6, "false"); //ActivityStatus
		WriteToDatabase(numberOfRows, 10, "R.mipmap.avatar1"); //Avatar
		WriteToDatabase(numberOfRows, 13, "0"); //FitnessLevel
		
		InitialiseStopper(numberOfRows);
		
		return "registration_success";
	}
	public String getAllUsers(String userNotToInclude) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		int numberOfRows = 1 + sheet.getLastRowNum();
		String listOfUsers = "";
		
		for(int i = 1; i < numberOfRows; i++)
		{
			if(sheet.getRow(i) != null)
			{
				if(sheet.getRow(i).getCell(0) != null)
				{
					if(!sheet.getRow(i).getCell(0).getStringCellValue().equals(userNotToInclude))
					{
						listOfUsers += sheet.getRow(i).getCell(0).getStringCellValue() + ";";
					}
				}
			}
		}
		return listOfUsers;
	}
	
	//FREUNDE
	
	public String addFriend(String person1, String person2) throws Exception
	{
		//adds person1 to person2's friendlist!!
		
		if(person1.equals(person2))
		{
			return "Friend_Add_" + person1 + "_" + person2 + "_Failed_SamePerson";
		}
		
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		int person1row  = FindRowOfUser(person1);
		int person2row = FindRowOfUser(person2);
		
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
	public String removeFriend(String person1, String person2) throws Exception
	{
		String friendslist = getFriendList(person2);
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
			int rowOfPerson2 = FindRowOfUser(person2);
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
	public String getFriendList(String username) throws Exception
	{
		int rowOfUser = FindRowOfUser(username);
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
	public String getFriendAllInfo(String user) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
	
		int row = FindRowOfUser(user);
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
	public String updateLocation(String user, String location) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		int userRow = FindRowOfUser(user);
		if(userRow != -1)
		{
			WriteToDatabase(userRow, 7, location);
			return "Location updated!";
		}
		else
		{
			return "User not found";
		}
	}
	public String getInfoOfUserForFriendsList(String user) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		int rowOfUser = FindRowOfUser(user);
		String returnString = "";
		if(rowOfUser != -1)
		{
			if(sheet.getRow(rowOfUser).getCell(0) != null
			&& sheet.getRow(rowOfUser).getCell(10) != null
			&& sheet.getRow(rowOfUser).getCell(13) != null)
			{
				returnString = sheet.getRow(rowOfUser).getCell(0).getStringCellValue() + ";"
							 + sheet.getRow(rowOfUser).getCell(10).getStringCellValue() + ";"
							 + sheet.getRow(rowOfUser).getCell(13).getStringCellValue() + ";";
				return returnString;
			}
			else
			{
				return "Cant get users info!";
			}
		}
		else
		{
			return "User not found!";
		}
	}
	//LAUFEN
	
	public String StartActivity(String time, String username, String location) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		int row = FindRowOfUser(username);
		if(row != -1)
		{
			userLeaveAllActivities(username);
			WriteToDatabase(row, 6, "true");
			WriteToDatabase(row, 7, location);
			WriteToDatabase(row, 8, time);
			return "Activity Started!";
		}
		else
		{
			return "Person does not exist";
		}
	}
	public String StopActivityOfUser(String user) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
	
		int row = FindRowOfUser(user);
		if(row != -1)
		{
			WriteToDatabase(row, 6, "false");  //stops this activity from being found by ReturnAllRowsOfPeopleWithActivities()
			WriteToDatabase(row, 9, ""); //deletes the activity list and everyone in it
			return "Activity Stopped!";
		}
		else
		{
			return "Person does not exist";
		}
	}
	public String JoinActivity(String userJoining, String userBeingJoined) throws Exception
	{	
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		if(userJoining.equals(userBeingJoined))
		{
			int userJoiningRow = FindRowOfUser(userJoining);
			int userBeingJoinedRow = FindRowOfUser(userBeingJoined);
		
			if(userJoiningRow != -1 && userBeingJoinedRow != -1)
			{
				if(!PersonExistsInActivityList(userJoining, userBeingJoined))
				{	
					userLeaveAllActivities(userJoining); //remove us from all other activities
				
					String newActivityList = "";
					if(sheet.getRow(userBeingJoinedRow).getCell(9) != null)
					{
						newActivityList = sheet.getRow(userBeingJoinedRow).getCell(9).getStringCellValue();
					}
					newActivityList += userJoining + ";";
					WriteToDatabase(userBeingJoinedRow, 9, newActivityList);
					
					StopActivityOfUser(userJoining); //now leave our own run if we have one!
					return "User joining suceeded!";
				}
				else
				{
					return "User already joined!";
				}
			}
			else
			{
				return "One of these users doesnt exist!";
			}
		}
		else
		{
			return "Cant join yourself!";
		}
		
	}
	public String LeaveActivity(String userLeaving, String userBeingLeft) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");

		int userLeavingRow = FindRowOfUser(userLeaving);
		int userBeingLeftRow = FindRowOfUser(userBeingLeft);
		if(userLeavingRow != -1 && userBeingLeftRow != -1)
		{
			if(PersonExistsInActivityList(userLeaving, userBeingLeft))
			{	
				if(sheet.getRow(userBeingLeftRow).getCell(9) != null)
				{
					String newActivityList = "";
					String[] splitString = sheet.getRow(userBeingLeftRow).getCell(9).getStringCellValue().split(";");
					for(int i = 0; i < splitString.length; i++)
					{
						if(!splitString[i].equals(userLeaving))
						{
							newActivityList += splitString[i] + ";";
						}
					}
					WriteToDatabase(userBeingLeftRow, 9, newActivityList);
					return "User removed from Activity!";
				}
				else
				{
					return "User does not have any users in his activity list!";
				}
			}
			else
			{
				return "User was not part of the Activity!";
			}
			
		}
		else
		{
			return "One of the users does not exist!";
		}
	}
	
	public void userLeaveAllActivities(String username) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		int userRow = FindRowOfUser(username);
		if(userRow != -1)
		{
			String debugString = username + " has left:";
			List<Integer> allRowsWithActivities = ReturnAllRowsWithActivities();
			for(int i : allRowsWithActivities)
			{
				if(PersonExistsInActivityList(username, sheet.getRow(i).getCell(0).getStringCellValue()))
				{
					LeaveActivity(username, sheet.getRow(i).getCell(0).getStringCellValue());
					debugString += " " + sheet.getRow(i).getCell(0).getStringCellValue();
				}
			}
			if(debugString.equals(username + " has left:"))
			{
				//System.out.println("User was not in any activities");
			}
			//System.out.println(debugString);
			}
		else
		{
			//System.out.println("User does not exist");
		}
	}
	public String getPeopleWithActivitiesInArea(String user, float maxDistance) throws Exception
	{	
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		int rowOfUser = FindRowOfUser(user);
		if(rowOfUser == -1)
		{
			return "UserNotFound!";
		}
		else
		{
			String peopleInArea = "";
			List<Integer> PeopleRowList = ReturnAllRowsWithActivities();
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
						peopleInArea += sheet.getRow(i).getCell(0).getStringCellValue() //Username
						+ ";"
						+ sheet.getRow(i).getCell(7).getStringCellValue() //Latitude/Longitude
						+ ";"
						+ sheet.getRow(i).getCell(8).getStringCellValue() //Zeit der Activity
						+ ";"
						+ sheet.getRow(i).getCell(16).getStringCellValue() //Art der Activity
						+ ";"
						+ sheet.getRow(i).getCell(13).getStringCellValue() //FitnessLevel
						+ "_";
					}
				}
			}
			if(peopleInArea.equals(""))
			{
				return "No People In Area!";
			}
			return peopleInArea;
		}
		
	}

	//MEDAILLEN
	
	public String AddMedalToUser(String medalName, String username) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		String medals = "";
		int userRow = FindRowOfUser(username);
		if(userRow != -1)
		{
			medals = GetMedalsCorrectOrder(username);
			
			String newMedalList = "";
			String newlyAddedMedal = "";
			String [] medalList = medals.split("-");
			String [] parsedMedal;
			System.out.println(medalList.length);
			int amountOfNewMedal = 0;
			if(!medals.equals("noMedals"))
			{
				for(int i = 0; i < medalList.length; i++)
				{
					parsedMedal = medalList[i].split(";");
					if(parsedMedal[0].equals(medalName))
					{
						amountOfNewMedal = Integer.parseInt(parsedMedal[1]) + 1;
					}
					else
					{
						newMedalList += medalList[i] + "-";
					}
				}
			}
			if(amountOfNewMedal != 0)
			{
				newlyAddedMedal = medalName + ";" + amountOfNewMedal + "-";
			}
			else
			{
				newlyAddedMedal = medalName + ";1-";
			}
			
			newMedalList += newlyAddedMedal;
			WriteToDatabase(userRow, 11, newMedalList);
			return "Added Medal to " + username;
		}
		else
		{
			return "User does not exist";
		}
	}
	public void RemoveMedals()
	{
		
	}
	public String GetMedalsReverseOrder(String username) throws Exception
	{	
		//returns the medals in the reverse order, to show the newest added one first!
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		String medals = "";
		int userRow = FindRowOfUser(username);
		if(userRow != -1)
		{
			if(sheet.getRow(userRow).getCell(11) != null)
			{
				String oldMedals = sheet.getRow(userRow).getCell(11).getStringCellValue();
				String [] medalList = oldMedals.split("-");
				for(int i = medalList.length-1; i >= 0; i--)
				{
					medals += medalList[i] + "-";
				}
			}
			else
			{
				medals = "";
			}
		}
		else
		{
			return "User not found";
		}
		return medals;
	}
	public String GetMedalsCorrectOrder(String username) throws Exception
	{	
		//returns the medals in the reverse order, to show the newest added one first!
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		String Medals = "";
		int userRow = FindRowOfUser(username);
		if(userRow != -1)
		{
			if(sheet.getRow(userRow).getCell(11) != null)
			{
				Medals = sheet.getRow(userRow).getCell(11).getStringCellValue();
			}
			else
			{
				Medals = "noMedals";
			}
		}
		else
		{
			return "User not found";
		}
		return Medals;
	}

	//AVATAR
	
	public String SetAvatar(String username, String avatarReference) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		int userRow = FindRowOfUser(username);
		if(userRow != -1)
		{
			WriteToDatabase(userRow, 10, avatarReference);
			return "Users avatar changed!";
		}
		else
		{
			return "User does not exist";
		}
	}
	public String GetAvatar(String username) throws Exception
	{
		fileInput=new FileInputStream("./data.xlsx");
		workbook=WorkbookFactory.create(fileInput);
		sheet=workbook.getSheet("Sheet1");
		
		int userRow = FindRowOfUser(username);
		if(userRow != -1)
		{
			if(sheet.getRow(userRow).getCell(10) != null)
			{
				return sheet.getRow(userRow).getCell(10).getStringCellValue();
			}
			else
			{
				return "No avatar found!";
			}
		}
		return "User does not exist";
	}
	
}
	
	    