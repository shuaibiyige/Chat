import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class client
{
    private JFrame frame;
    private JTextField text;
    private JTextArea area;
    private JButton button;
    private JPanel panel;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private Scanner read;
    
    public static void main()
    {
        client c = new client();
        c.go();
    }
    
    public void go()
    {
        frame = new JFrame();
        text = new JTextField(20);
        button = new JButton("send");
        panel = new JPanel();
        area = new JTextArea(15,40);
        area.setLineWrap(true);
        JScrollPane sc = new JScrollPane(area);
        sc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        sc.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        button.addActionListener(new send());
        panel.add(button);
        panel.add(text);
        panel.add(sc);
        frame.getContentPane().add(BorderLayout.CENTER,panel);
        frame.setSize(500,400);
        frame.setVisible(true);
        set();
        Thread t = new Thread(new read());
        t.start();
    }
    
    public void set()
    {
        try
        {
            socket = new Socket("localhost",5000);
            InputStreamReader r = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(r);
            writer = new PrintWriter(socket.getOutputStream());            
            String m = reader.readLine();
            area.append(m + "\n");
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public class send implements ActionListener
    {
        public void actionPerformed(ActionEvent x)
        {
            try
            {
                String m = text.getText();
                writer.println(m);  //write
                writer.flush();
                area.append("client: " + m + "\n");
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            finally
            {
                text.setText("");
                text.requestFocus();
            }
        }
    }
    
    public class read implements Runnable
    {
        public void run()
        {
            try
            {
                String m2;
                while((m2 = reader.readLine()) != null)
                {
                    area.append("server: " + m2 + "\n");
                }
                socket.shutdownInput();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}