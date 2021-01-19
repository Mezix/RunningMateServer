
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
			serverMessage = Login(SplitString);
		}
		else if(SplitString[0].equals("register"))
		{
			serverMessage = RegisterUser(SplitString);
		}
		else if(SplitString[0].equals("friend"))
		{
			SplitString[1] = SplitString[1].toLowerCase();
			
			if(SplitString[1].equals("get"))
			{
				if(SplitString[2].equals("list"))
				{
					serverMessage = GetFriendsList(SplitString[3]);
				}
				if(SplitString[2].equals("allInfo"))
				{
					serverMessage = GetFriendAllInfo(SplitString[3]);
				}
			}
			else if(SplitString[1].equals("add"))
			{
				serverMessage = AddFriend(SplitString[2], SplitString[3]);
			}
			else if(SplitString[1].equals("remove"))
			{
				serverMessage = RemoveFriend(SplitString[2], SplitString[3]);
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
				serverMessage = StartActivity(SplitString[3], SplitString[4], SplitString[5]);	
			}
			else if(SplitString[2].equals("stop"))
			{
				serverMessage = StopActivity(SplitString[3]);	
			}
			else if(SplitString[2].equals("join")) 
			{
				serverMessage = JoinActivity(SplitString[3], SplitString[4]);
			}
			else if(SplitString[2].equals("leave")) 
			{
				serverMessage = LeaveActivity(SplitString[3], SplitString[4]);
			}
			else if(SplitString[2].equals("getpeopleinarea")) 
			{
				serverMessage = GetPeopleInArea(SplitString[3], SplitString[4]);
			}
		}
		else if(SplitString[0].equals("medal")) 
		{
			SplitString[1] = SplitString[1].toLowerCase();
			if(SplitString[1].equals("add"))
			{
				SplitString[2] = SplitString[2].toLowerCase();
				serverMessage = AddMedalToUser(SplitString[2], SplitString[3]);
			}
			else if(SplitString[1].equals("remove"))
			{
				
			}
			else if(SplitString[1].equals("get"))
			{
				serverMessage = GetMedalsOfUser(SplitString[2]);
			}
		}
		else if(SplitString[0].equals("avatar")) 
		{
			SplitString[1] = SplitString[1].toLowerCase();
			if(SplitString[1].equals("set"))
			{
				serverMessage = SetAvatar(SplitString[2], SplitString[3]);
			}
			else if(SplitString[1].equals("get"))
			{
				serverMessage = GetAvatar(SplitString[2]);
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
	
	public String Login(String[] s)
	{
	
		try 
		{
			return excelTable.Login(s[1], s[2]);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return "Login caused Exception";
		}
	}
	public String RegisterUser(String[] s)
	{
		try 
		{
			return excelTable.RegisterPerson(s[1], s[2], s[3], s[4], s[5]);
		} 
		catch (Exception e) {
			e.printStackTrace();
			return "Registering Person caused Exception";
		}
	}
	
	//FRIEND
	
	public String GetFriendsList(String username)
	{
		try 
		{
			return excelTable.GetFriendList(username);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "Adding Friend caused Exception";
		}
	}
	public String GetFriendAllInfo(String username)
	{
		try
		{
			return excelTable.GetFriendAllInfo(username);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Stopping Run caused Exception";
		}
	}
	public String AddFriend(String person1, String person2) //Adds person1, to person 2's friendlist
	{
		try
		{
			return excelTable.AddFriend(person1, person2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "Adding Friend caused Exception";
		}
	}
	public String RemoveFriend(String person1, String person2)
	{
		try
		{
			return excelTable.RemoveFriend(person1, person2);
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
			return "Removing Friend caused Exception";
		}
	}
	//RUN
	
	public String StartActivity(String date, String user, String location)
	{
		try
		{
			return excelTable.StartActivity(date, user, location);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Starting Activity caused Exception";
		}
	}
	public String StopActivity(String user)
	{
		try
		{
			return excelTable.StopActivityOfUser(user);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Stopping Activity caused Exception";
		}
	}
	public String JoinActivity(String userJoining, String userBeingJoined)
		{
		try
		{
			return excelTable.JoinActivity(userJoining, userBeingJoined);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Joining Activity caused Exception";
		}
	}
	public String LeaveActivity(String userLeaving, String userBeingLeft)
	{
		try
		{
			return excelTable.LeaveActivity(userLeaving, userBeingLeft);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Leaving Activity caused Exception";
		}
	}
	public String GetPeopleInArea(String Username, String distance)
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
	
	public String AddMedalToUser(String medalname, String username)
	{
		try
		{
			return excelTable.AddMedalToUser(medalname, username);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Adding Medal caused Exception";
		}
	}
	
	public String GetMedalsOfUser(String username)
	{
		try
		{
			return excelTable.GetMedalsReverseOrder(username);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Getting Medal caused Exception";
		}
	}
	
	//AVATAR 
	
	public String GetAvatar(String username)
	{
		try
		{
			return excelTable.GetAvatar(username);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
			return "Getting Avatar caused Exception";
		}
	}
	public String SetAvatar(String username, String avatarRef)
	{
		try
		{
			return excelTable.SetAvatar(username, avatarRef);
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
	
	//Name1|Name2|(MessageList): Message1|Message2|...|MessageN
	//or alternatively add a new cell for each message!
}
