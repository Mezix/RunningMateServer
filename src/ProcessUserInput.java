
public class ProcessUserInput {

	public ProcessUserInput()
	{
		System.out.println("New Processor");
	}
	
	public String ProcessString(String receivedString)
	{
		String returnedString = null;
		System.out.println(receivedString);
		//Parse String
		
		//Process String
		
		if(receivedString == "Hello")
		{
			returnedString = "Goodbye";
		}

		System.out.println(returnedString);
		
		//Return String
		return returnedString;
	}
}
