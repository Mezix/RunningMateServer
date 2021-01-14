import java.io.*;
import java.net.*;

public class Server {

    public static void main (String[] args) throws IOException
    {
    	ProcessUserInput Processor = new ProcessUserInput();
        ServerSocket serverSocket = null;
        int port = 8000;

        try
        {
            serverSocket = new ServerSocket(port);
            System.out.println("Server Socket Created");
            
        }
        catch (IOException e)
        {
            System.err.println("Could not listen on port:" + port);
            System.exit(1);
        }

        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        System.out.println(serverSocket.toString());

        while(true)
        {
            try
            {
                clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("Connection Established");
            }
            catch (IOException e)
            {
                System.err.println("Accept failed");
                break;
            }
            String input;
            while ((input = in.readLine()) != null)
            {
                System.out.println("Received: " + input);
                
                if(input.equals("Bye"))
                    break;
                
                out.println(Processor.ProcessString(input));
            }
            out.println("Bye");

            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
}
