
public class ProcessUserInput {

	ExcelTableProcessor excelTable = new ExcelTableProcessor();
	
	public String ProcessString(String receivedString)
	{
		String returnedString = null;
		
		//Split String so we can process it
		
		String[] SplitString = receivedString.split(":");
		
		SplitString[0] = SplitString[0].toLowerCase(); //Lowercase the command part of the string so we dont have frustrating case issues!
		
		//Process String
		
		if(SplitString[0].equals("login"))
		{
			returnedString = Login(SplitString);
		}
		else if(SplitString[0].equals("register"))
		{
			//String Syntax we need:
			//Register:<Username>:<Password>:<DisplayName>:<Age> (MORE TO COME HERE)
			returnedString = RegisterUser(SplitString);
		}
		else if(SplitString[0].equals("friend"))
		{
			SplitString[1] = SplitString[1].toLowerCase();
			
			if(SplitString[1].equals("get"))
			{
				returnedString = ReturnFriend();
			}
			else if(SplitString[1].equals("add"))
			{
				//Add a friend to another persons list:
				//Friend:Add:<Felix>:<Enric> (adds felix to enrics friend list)
				returnedString = AddFriend(SplitString[3], SplitString[4]);
			}
		}
		
		System.out.println("Received String: " + receivedString);
		System.out.println("ReturnedString : " + returnedString);
		
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
			return "Adding Person caused Exception";
		}
	}
	public String ReturnFriend()
	{
		return "Heres your friend (kinda)";	
	}
	public String AddFriend(String FriendToAdd, String PersonsListToAddTo)
	{
		return "Added Friend maybe (test)";
	}
	
}
