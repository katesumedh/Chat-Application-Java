import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.*;
import java.net.*;
import java.awt.Font;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.BorderLayout;

public class Client extends JFrame {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Declare components 
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);


    public Client()
    {
        try{
            System.out.println("Sending request to host");
            socket=new Socket("127.0.0.1",7778);
            System.out.println("Connection done");
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
           createGUI();
            handleEvents();
            startReading();
            startWriting();
            


        }catch (Exception e){

        }
    }

    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                // TODO Auto-generated method stub
                
            }
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                // TODO Auto-generated method stub
                
            }
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                // TODO Auto-generated method stub
                // System.out.println("Key Released " + e.getKeyCode());
                if(e.getKeyCode()==10){ // 10 for enter
                    // System.out.println("You have pressed enter button");
                String contentToSend = messageInput.getText();
                messageArea.append("Me :" + contentToSend + "\n");
                out.println(contentToSend);
                out.flush();
               messageInput.setText((""));
               messageInput.requestFocus();
                }
            }
        });
    }


    private void createGUI()
    {
        this.setTitle("Client Messager");
        this.setSize(500,500);
        this.setLocationRelativeTo(null); //window to null
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);


       // heading.setIcon(new ImageIcon("download.png")); //image size issue
        
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
       messageArea.setEditable(false);

 
        this.setLayout(new BorderLayout()); //divide into 5----awt use hua h

        //adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);



        this.setVisible(true);
    }

    public void startReading(){
        Runnable r1=()->{  //lamda expression
            System.out.println("Reader started");

            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Host terminated the chat");
                        JOptionPane.showMessageDialog(this, "Host terminated the chat");
                        messageInput.setEnabled(false);

                        socket.close();
                        break;
                    }
                   // System.out.println("Server :" + msg);
                    messageArea.append("Host :" + msg+"\n");
                }

            }catch (Exception e){
                //e.printStackTrace();
                System.out.println("Connection is closed");
            }
        };
        new Thread(r1).start();
    }

    //writing
    public void startWriting(){
        Runnable r2=()->{
            System.out.println("Writer started..");


            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }
                   // System.out.println("connection is closed");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        };
        new Thread(r2).start();

    }
    public static void main(String[] args) {
        System.out.println("This is client..");
        new Client();
    }
}