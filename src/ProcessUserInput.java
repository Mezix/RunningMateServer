
public class ProcessUserInput {

	public ProcessUserInput()
	{
		System.out.println("New Processor");
	}
	
	public String ProcessString(String receivedString)
	{
		String returnedString = null;
		
		//Split String so we can process it
		
		String[] SplitString = receivedString.split(":");
		
		//Process String
		
		if(SplitString[0].equals("Friend"))
		{
			returnedString = SplitString[1];
		}
		
		if(receivedString.equals("Hello"))
		{
			returnedString = "Goodbye";
		}

		System.out.println("Received String: " + receivedString);
		System.out.println("ReturnedString : " + returnedString);
		
		//Return String
		return returnedString;
	}
}
