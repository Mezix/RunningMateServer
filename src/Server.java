import java.io.*;
import java.net.*;

public class Server {

    public static void main (String[] args) throws IOException
    {
        ServerSocket serverSocket = null;
        int port = 5000;

        try
        {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            System.err.println("Could not listen on port:" + port);
            System.exit(1);
        }

        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        while(true)
        {
            try
            {
                clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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
                if(input.equals("Test"))
                {
                	out.println("Test Received");
                }
                if(input.equals("Bye"))
                    break;
                out.println("Im a dummy");
            }
            out.println("Bye");

            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
}
