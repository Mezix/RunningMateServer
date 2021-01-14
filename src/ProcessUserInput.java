
public class ProcessUserInput {

	ExcelTableProcessor excelTable = new ExcelTableProcessor();
	
	public String ProcessString(String receivedString)
	{
		String returnedString = null;
		
		//Split String so we can process it
		
		String[] SplitString = receivedString.split(":");
		
		//Process String
		if(SplitString[0].equals("Register"))
		{
			//String Syntax we need:
			//Register:<Username>:<Password>:<DisplayName>:<Age> (MORE TO COME HERE)
			returnedString = RegisterUser(SplitString);
		}
		if(SplitString[0].equals("Friend"))
		{
			//returnedString = SplitString[1];
			if(SplitString[1].equals("Get"))
			{
				//String syntax we are hoping for:
				//Friend:<name here>
				returnedString = ReturnFriend();
			}
			if(SplitString[1].equals("Add"))
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
	
	public String ReturnFriend()
	{
		return "Heres your friend (kinda)";	
	}
	public String AddFriend(String FriendToAdd, String PersonsListToAddTo)
	{
		return "Added Friend maybe (test)";
	}
	public String RegisterUser(String[] s)
	{
		try 
		{
			return excelTable.AddNewPersonToDatabase(s[1], s[2], s[3], s[4]);
		} 
		catch (Exception e) {
			e.printStackTrace();
			return "Adding Person caused Exception";
		}
	}
	
}
