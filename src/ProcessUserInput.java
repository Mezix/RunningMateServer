
public class ProcessUserInput {

	ExcelTableProcessor excelTable = new ExcelTableProcessor();
	
	public String ProcessString(String receivedString)
	{
		String serverMessage = null;
		
		//Split String so we can process it
		
		String[] SplitString = receivedString.split("_");
		
		SplitString[0] = SplitString[0].toLowerCase(); //Lowercase the command part of the string so we dont have frustrating case issues!
		
		//Process String
		
		if(SplitString[0].equals("login"))
		{
			serverMessage = login(SplitString);
		}
		else if(SplitString[0].equals("register"))
		{
			serverMessage = registerUser(SplitString);
		}
		else if(SplitString[0].equals("getallusernames"))
		{
			serverMessage = getAllUserNames(SplitString[1]);
		}
		else if(SplitString[0].equals("friend"))
		{
			SplitString[1] = SplitString[1].toLowerCase();
			
			if(SplitString[1].equals("get"))
			{
				if(SplitString[2].equals("friendlist"))
				{
					serverMessage = getFriendsList(SplitString[3]);
				}
				else if(SplitString[2].equals("allInfo"))
				{
					serverMessage = getFriendAllInfo(SplitString[3]);
				}
				else if(SplitString[2].equals("infoForFriendlist"))
				{
					serverMessage = getInfoOfUserForFriendsList(SplitString[3]);
				}
			}
			else if(SplitString[1].equals("add"))
			{
				serverMessage = addFriend(SplitString[2], SplitString[3]);
			}
			else if(SplitString[1].equals("remove"))
			{
				serverMessage = removeFriend(SplitString[2], SplitString[3]);
			}
			else if(SplitString[1].equals("updatelocation"))
			{
				serverMessage = updateLocation(SplitString[2], SplitString[3]);
			}
		}
		else if(SplitString[0].equals("activity"))
		{
			SplitString[1] = SplitString[1].toLowerCase();
			SplitString[2] = SplitString[2].toLowerCase();
			
			if(SplitString[2].equals("start"))
			{
				serverMessage = startActivity(SplitString[3], SplitString[4], SplitString[5], SplitString[6]);	
			}
			else if(SplitString[2].equals("stop"))
			{
				serverMessage = stopActivity(SplitString[3]);	
			}
			else if(SplitString[2].equals("join")) 
			{
				serverMessage = joinActivity(SplitString[3], SplitString[4]);
			}
			else if(SplitString[2].equals("leave")) 
			{
				serverMessage = leaveActivity(SplitString[3], SplitString[4]);
			}
			else if(SplitString[2].equals("getpeopleinarea")) 
			{
				serverMessage = getPeopleInArea(SplitString[3], SplitString[4]);
			}
		}
		else if(SplitString[0].equals("medal")) 
		{
			SplitString[1] = SplitString[1].toLowerCase();
			if(SplitString[1].equals("add"))
			{
				SplitString[2] = SplitString[2].toLowerCase();
				serverMessage = addMedalToUser(SplitString[2], SplitString[3]);
			}
			else if(SplitString[1].equals("remove"))
			{
				
			}
			else if(SplitString[1].equals("get"))
			{
				serverMessage = getMedalsOfUser(SplitString[2]);
			}
		}
		else if(SplitString[0].equals("avatar")) 
		{
			SplitString[1] = SplitString[1].toLowerCase();
			if(SplitString[1].equals("set"))
			{
				serverMessage = setAvatar(SplitString[2], SplitString[3]);
			}
			else if(SplitString[1].equals("get"))
			{
				serverMessage = getAvatar(SplitString[2]);
			}
		}
		else
		{
			serverMessage = "Not a valid command!";
		}
		
		System.out.println("Received String : " + receivedString);
		System.out.println("Returned String : " + serverMessage);
		
		return serverMessage;
	}
	
	public String login(String[] s)
	{
	
		try 
		{
			return excelTable.login(s[1], s[2]);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return "Login caused Exception";
		}
	}
	public String registerUser(String[] s)
	{
		try 
		{
			return excelTable.registerPerson(s[1], s[2], s[3], s[4], s[5]);
		} 
		catch (Exception e) {
			e.printStackTrace();
			return "Registering Person caused Exception";
		}
	}
	
	public String getAllUserNames(String userNotToInclude)
	{
		try
		{
			return excelTable.getAllUsers(userNotToInclude);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Leaving Activity caused Exception";
		}
	}
	
	//FRIEND
	
	public String getFriendsList(String username)
	{
		try 
		{
			return excelTable.getFriendList(username);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "Adding Friend caused Exception";
		}
	}
	public String getFriendAllInfo(String username)
	{
		try
		{
			return excelTable.getFriendAllInfo(username);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Stopping Run caused Exception";
		}
	}
	public String addFriend(String person1, String person2) //Adds person1, to person 2's friendlist
	{
		try
		{
			return excelTable.addFriend(person1, person2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "Adding Friend caused Exception";
		}
	}
	public String removeFriend(String person1, String person2)
	{
		try
		{
			return excelTable.removeFriend(person1, person2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "Removing Friend caused Exception";
		}
	}
	public String updateLocation(String person1, String location)
	{
		try
		{
			return excelTable.updateLocation(person1, location);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "Updating Location caused Exception";
		}
	}
	public String getInfoOfUserForFriendsList(String user)
	{
		try
		{
			return excelTable.getInfoOfUserForFriendsList(user);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "Getting info for friendslist caused Exception";
		}
	}
	//RUN
	
	public String startActivity(String date, String user, String location, String activityType)
	{
		try
		{
			return excelTable.startActivity(date, user, location, activityType);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Starting Activity caused Exception";
		}
	}
	public String stopActivity(String user)
	{
		try
		{
			return excelTable.stopActivityOfUser(user);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Stopping Activity caused Exception";
		}
	}
	public String joinActivity(String userJoining, String userBeingJoined)
		{
		try
		{
			return excelTable.joinActivity(userJoining, userBeingJoined);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Joining Activity caused Exception";
		}
	}
	public String leaveActivity(String userLeaving, String userBeingLeft)
	{
		try
		{
			return excelTable.leaveActivity(userLeaving, userBeingLeft);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Leaving Activity caused Exception";
		}
	}
	public String getPeopleInArea(String Username, String distance)
	{
		try
		{
			return excelTable.getPeopleWithActivitiesInArea(Username, Float.parseFloat(distance));
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Getting people in area caused exception";
		}
	}
	
	//MEDALS
	
	public String addMedalToUser(String medalname, String username)
	{
		try
		{
			return excelTable.addMedalToUser(medalname, username);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Adding Medal caused Exception";
		}
	}
	
	public String getMedalsOfUser(String username)
	{
		try
		{
			return excelTable.getMedalsReverseOrder(username);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Getting Medal caused Exception";
		}
	}
	
	//AVATAR 
	
	public String getAvatar(String username)
	{
		try
		{
			return excelTable.getAvatar(username);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Getting Avatar caused Exception";
		}
	}
	public String setAvatar(String username, String avatarRef)
	{
		try
		{
			return excelTable.setAvatar(username, avatarRef);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Setting Avatar caused Exception";
		}
	}
	
	//CHAT
	
	
	public void GetChatLog(String Person1, String Person2)
	{
		//Person 1 sent the messages to person2
		//return all messages with A very long decoder between each message
		//, so we dont accidentally trigger it and save two messages in one
		//like:  <&%msg%&>
	}
	
	public void GetAllActiveChats(String person)
	{
		//search person in cell 1 and cell 2 of each row!
		//return List of People to display, using the other cell we didnt find our person in
	}
	//Add a new excel spreadsheet for Chat messages!!
	
	//Name1|Name2|sender;Message1|sender;Message2|...|sender;MessageN
	//			a new cell for each message! always add the sender to the message as well with a code so we can split it later
	//			see GetChatLogs string splitter for inspiration
}
