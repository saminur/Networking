/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class ClientClass implements Runnable {
    
    //private static long start = System.currentTimeMillis();
    public ServerClass serverclass;
    public JFrameClient jClient;
    public int portNumber;
    public String IPAdress;
    public int ClientID;
    public Socket ClientSocket;
    //File SendFile;
    public String fb;
    public String DirFile;
    
    public final static int FILE_SIZE = 6022386;
    public ObjectInputStream ois=null;
    public ObjectOutputStream oos=null;
    public FileOutputStream fos=null;
    public FileInputStream fis=null;
    public File file;
    public ClientClass(JFrameClient jClient, int portnumber, int ClientID, String IP) {
       
            this.jClient = jClient;
            this.portNumber = portnumber;
            this.ClientID = ClientID;
            this.IPAdress = IP;
            connectToServer();
             
            //Thread sendBackUpThread=new Thread(sb);
            //sendBackUpThread.start();
            
    }

    public void run() {
        
        while (true) {
            try {
                System.out.println("inside client thread" + jClient.RfileDir.getText() + "\n");
                file = new File(jClient.RfileDir.getText());
                Object ob = ois.readObject();
                if (ob instanceof byte[]) {
                    byte_array = (byte[]) ob;
                    fos = new FileOutputStream(file);
                    fos.write(byte_array);
                    System.out.println("File recieved");
                    fos.close();
                    SendBackUP sb=new SendBackUP(this,DirFile);
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientClass.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    public void sendFile(String fileDir)
    {
        System.out.println(fileDir);
        try {
            file=new File(fileDir);
            byte[] byte_array=new byte[(int) file.length()];
            fis=new FileInputStream(file);
            fis.read(byte_array);
            
            oos.writeObject(byte_array);
            oos.flush();
            System.out.println("Question File sending");
        } catch (IOException ex) {
            Logger.getLogger(ThreadHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public void DisplayMessage(final String MessageToDisplay) {
        this.jClient.setClientMessage(MessageToDisplay);
         System.out.println(MessageToDisplay);
    }
     public byte[]byte_array;
     public void connectToServer()
     {
        try {
            this.ClientSocket=new Socket(IPAdress,portNumber);
            DirFile=jClient.QuestionFile.getText();
            DisplayMessage("Connected");
            getStream();
            System.out.println("Stream Received");
            oos.writeObject(String.valueOf(ClientID));
            oos.flush();
            String EN=(String)ois.readObject();
            DisplayMessage("\n"+EN+"\n");
            String Duration=(String)ois.readObject();
            DisplayMessage("\n"+Duration+"\n");
            
            System.out.println("\nExam Name is " + EN + "\n");
            System.out.println("Client ID written");
           
            
        } catch (IOException ex) {
            Logger.getLogger(ClientClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientClass.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     public void getStream()
     {
        try {
            oos=new ObjectOutputStream(ClientSocket.getOutputStream());
            ois=new ObjectInputStream(ClientSocket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientClass.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
}
