/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerClass implements Runnable
{
    
    public static ServerSocket serverSocket = null;
    public static Socket connection = null;    
    public  int Counter = 1;
    public int serverPortNO;
    public JFrameServer jserver;
    public boolean ClientConnection;
    ServerClass(JFrameServer jfs, int pNo) {
        this.serverPortNO = pNo;
        this.jserver = jfs;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(serverPortNO);
            displayMessage("Server Started\n");
            while (true) {
                try {
                    //ClientConnection=false;
                    waitForConnection();    
                } catch (EOFException eofException) {
                    displayMessage("\nServer terminated connection");
                } finally {
                    // closeConnection();
                    Counter++;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public ThreadHandler handler;

    private void waitForConnection() throws IOException {
        displayMessage("Waiting for connection\n");
        
            connection = serverSocket.accept();
            System.out.println("Connection received from " + connection.getInetAddress());
            //ClientConnection=true;
            handler = new ThreadHandler(connection, this);            
            Thread threadHandler = new Thread(handler);
            threadHandler.start();
            displayMessage("Connection " + (Counter++) + " received from: "
                    + connection.getInetAddress().getHostAddress() + "\n");

       // }
    }

    public void displayMessage(final String messageToDisplay) {
        this.jserver.setServerMessage(messageToDisplay);
    }
    
}
