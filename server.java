import java.io.*;
import java.net.*;
import java.util.*;

public class server
{
    private BufferedReader reader;
    private Socket sock;
    private String input;
    private PrintWriter writer;
    
    public static void main()
    {
        server s =  new server();
        s.go();
    }
    
    public void go()
    {
        try 
        {
            ServerSocket serverSock = new ServerSocket(5000);
            Scanner console = new Scanner(System.in);
            while(true)
            {
                sock = serverSock.accept();
                writer = new PrintWriter(sock.getOutputStream());
                writer.println("已成功连接到远程服务器！"+"\t"+"请您先发言。");
                writer.flush();
                Thread t = new Thread(new write(sock));
                t.start();
                input = console.nextLine();
                while(input != null)
                {                    
                    System.out.println("server: " + input);
                    writer.println(input);
                    writer.flush();
                    input = console.nextLine();
                }
            }
        }
         
        catch (Exception ex) 
        { 
            ex.printStackTrace(); 
        }
    }
    
    public class write implements Runnable
    {
        public write(Socket s)
        {
            try
            {
                sock = s;
                InputStreamReader r = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(r);
            }
            catch (Exception ex) 
            { 
                ex.printStackTrace(); 
            }
        }
        
        public void run() 
        {
            String message;
            try 
            {
                while((message = reader.readLine()) != null)
                {
                    System.out.println("client: " + message);
                }
                sock.shutdownInput();
            } 
            catch (Exception ex) 
            { 
                ex.printStackTrace(); 
            }
        }
    }
}