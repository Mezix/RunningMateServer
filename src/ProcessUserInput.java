
public class ProcessUserInput {

	ExcelTableProcessor excelTable = new ExcelTableProcessor();
	
	public String ProcessString(String receivedString)
	{
		String returnedString = null;
		
		//Split String so we can process it
		
		String[] SplitString = receivedString.split("_");
		
		SplitString[0] = SplitString[0].toLowerCase(); //Lowercase the command part of the string so we dont have frustrating case issues!
		
		//Process String
		
		if(SplitString[0].equals("login"))
		{
			returnedString = Login(SplitString);
		}
		else if(SplitString[0].equals("register"))
		{
			returnedString = RegisterUser(SplitString);
		}
		else if(SplitString[0].equals("friend"))
		{
			SplitString[1] = SplitString[1].toLowerCase();
			
			if(SplitString[1].equals("get"))
			{
				if(SplitString[2].equals("list"))
				{
					returnedString = GetFriendsList(SplitString[3]);
				}
				if(SplitString[2].equals("allInfo"))
				{
					returnedString = GetFriendAllInfo(SplitString[3]);
				}
			}
			else if(SplitString[1].equals("add"))
			{
				returnedString = AddFriend(SplitString[2], SplitString[3]);
			}
			else if(SplitString[1].equals("remove"))
			{
				returnedString = RemoveFriend(SplitString[2], SplitString[3]);
			}
		}
		
		System.out.println("Received String : " + receivedString);
		System.out.println( "ReturnedString : " + returnedString);
		
		return returnedString;
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
			return excelTable.AddNewPersonToDatabase(s[1], s[2], s[3], s[4], s[5]);
		} 
		catch (Exception e) {
			e.printStackTrace();
			return "Registering Person caused Exception";
		}
	}
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
		return "Heres your friend (kinda)";	
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
}
