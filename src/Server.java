import java.io.*;
import java.net.*;


public class Server {

    public static void main (String[] args) throws IOException
    {
    	ProcessUserInput InputProcessor = new ProcessUserInput();
        ServerSocket serverSocket = null;
        int port = 8000;
        
        //InputProcessor.ProcessString("login_Mezix_password");
        //InputProcessor.ProcessString("register_Hande2_password_Hande_Eyicalis_21");
        //InputProcessor.ProcessString("friend_add_Mezix2_Mezix");
        //InputProcessor.ProcessString("friend_remove_Enric_Mezix");
        //InputProcessor.ProcessString("friend_get_list_Mezix");
        //InputProcessor.ProcessString("friend_get_allInfo_Mezix");
        //InputProcessor.ProcessString("Run_start_14:23_Mezix_lat/lng: (53.6175727,9.8987057)");
        //InputProcessor.ProcessString("run_stop_Mezix");
        //ProcessString("Run_getpeopleinarea_Enric_5000");
        //InputProcessor.ProcessString("run_join_Phine_Enric");
        //InputProcessor.ProcessString("run_leave_Phine_Enric");
        
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
                System.out.println("______________________");
                System.out.println("Connection Established");
                System.out.println("______________________");
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
                
                out.println(InputProcessor.ProcessString(input));
            }
            out.println("Bye");

            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
}
