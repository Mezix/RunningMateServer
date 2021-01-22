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
        //InputProcessor.ProcessString("friend_add_Phine_Mezix");
        //InputProcessor.ProcessString("friend_remove_Enric_Mezix");
        //InputProcessor.ProcessString("friend_get_friendlist_Mezix");
        //InputProcessor.ProcessString("friend_get_allInfo_Mezix");
        //%InputProcessor.ProcessString("activity_run_start_14:23_Mezix_lat/lng: (53.6175727,9.8987057)_jogging");
        //InputProcessor.ProcessString("activity_run_stop_Mezix");
        //InputProcessor.ProcessString("activity_run_getpeopleinarea_Enric_5000");
        //InputProcessor.ProcessString("activity_run_join_Phine_Enric");
        //InputProcessor.ProcessString("activity_run_leave_Phine_Enric");
        //InputProcessor.ProcessString("medal_add_10streak_Mezix");
        //InputProcessor.ProcessString("medal_get_Mezix");
        //InputProcessor.ProcessString("avatar_get_Mezix");
        //InputProcessor.ProcessString("avatar_set_Mezix_avatar2");
        //InputProcessor.ProcessString("friend_updatelocation_Mezix_lat/lng: (53.6175727,9.8987057)");
        //InputProcessor.ProcessString("getallusernames_Mezix");
        //InputProcessor.ProcessString("friend_get_infoForFriendlist_Phine");
        //InputProcessor.ProcessString("getallusernamesforfriendlist_Mezix");
        //InputProcessor.ProcessString("friend_get_fullFriendListInfo_Phine");
        
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
