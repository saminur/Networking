import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ThreadHandler implements Runnable
{
    public Socket connection = null;
    private int counter = 0;
    ServerClass serverClass;
    //public int clientID;
    public final static int FILE_SIZE = 6022386;
    public ObjectInputStream ois=null;
    public ObjectOutputStream oos=null;
    public FileInputStream fis=null;
    public FileOutputStream fos=null;
    public int acknowledge;
    public String ClientID;
    public ThreadHandler(Socket connection, ServerClass server) {
        System.out.println("New thread started");
        this.connection = connection;
        serverClass = server;
        //this.clientID = clientID;
        
        try {
            ois = new ObjectInputStream(connection.getInputStream());
            oos = new ObjectOutputStream(connection.getOutputStream());
            ClientID=(String)ois.readObject();
            System.out.println(ClientID);
            oos.writeObject(String.valueOf(serverClass.jserver.examName.getText()));
            oos.flush();
            oos.writeObject(String.valueOf(serverClass.jserver.durationTime.getText()));
            oos.flush();
            sendFile(serverClass.jserver.RulesField.getText());
            System.out.println("Thread Handler started with client " + connection.getPort());
        } catch (IOException ex) {
            Logger.getLogger(ThreadHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ThreadHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    Calendar cal=Calendar.getInstance();
    Calendar cTime=Calendar.getInstance();
    public void run() {
        int sec=Integer.parseInt(serverClass.jserver.sFiled.getText());
        int h=Integer.parseInt(serverClass.jserver.startTime.getText());
        int m=Integer.parseInt(serverClass.jserver.minitTime.getText());
        cal.set(Calendar.SECOND,sec );
        cal.set(Calendar.HOUR, h);
        cal.set(Calendar.MINUTE,m);
        long miliSeconds=(cal.get(Calendar.HOUR)*3600*1000)+(cal.get(Calendar.MINUTE)*60*1000)+(cal.get(Calendar.SECOND)*1000);
        long milisecondsC=(cTime.get(Calendar.HOUR)*3600*1000)+(cTime.get(Calendar.MINUTE)*60*1000)+(cTime.get(Calendar.SECOND)*1000);
        System.out.println(miliSeconds);
        System.out.println(milisecondsC);
        if(milisecondsC >= miliSeconds)
        {
            //System.out.println(df.format(date).compareTo(df.format(Stime)));
            System.out.println(serverClass.jserver.QField.getText());
            sendFile(serverClass.jserver.QField.getText());
        }
        else {
            //long timeToStart =(cal.getTime().getTime()) - (cTime.getTime());
            long timeToStart=miliSeconds-milisecondsC;
            System.out.println(timeToStart);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                
                @Override
                public void run() {
                    
                    sendFile(serverClass.jserver.QField.getText());
                }
            }, timeToStart);
        }
        ;
        System.out.println("Inside threadhandler\n");
        /*int warning=Integer.parseInt(serverClass.jserver.warningTime.getText());
        long WarningTime=miliSeconds+warning*60*1000;
        System.out.println(WarningTime);
        Timer warn=new Timer();
        warn.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    //To change body of generated methods, choose Tools | Templates.
                    System.out.println("Inside Warning Timer Class");
                    oos.writeInt(1);
                    oos.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ThreadHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, WarningTime);*/
        while(true){
            String fileDir=this.serverClass.jserver.ROOT + File.separator
                    + ClientID + File.separator + "Answer.pdf"; 
            RecieveFile(fileDir);
            System.out.println("Inside Thread handler the dir is "+serverClass.jserver.RulesField.getText()+"\n");
        }
        
    }
    public File file;
    public void sendFile(String fileDir)
    {
        
        try {
            file=new File(fileDir);
            byte[] byte_array=new byte[(int) file.length()];
            fis=new FileInputStream(file);
            fis.read(byte_array);
            if(!checkRange(ClientID,serverClass.jserver.Fromto)){
                JOptionPane.showMessageDialog(null, "ID not allowed");
                return;
            }
            oos.writeObject(byte_array);
            oos.flush();
            System.out.println("file sending");
            //SendBackUP sendbackup=new SendBackUP(5);
        } catch (IOException ex) {
            Logger.getLogger(ThreadHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public byte[]byte_array;
    public void RecieveFile(String fileDir)
    {
        System.out.println(fileDir);
         try {
                file = new File(fileDir);
                if (file.getParentFile() != null && !file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                
                Object ob = ois.readObject();
                if (ob instanceof byte[]) {
                    byte_array = (byte[]) ob;
                    fos = new FileOutputStream(file);
                    fos.write(byte_array);
                    System.out.println("Answer File recieved");
                    fos.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientClass.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }

    public boolean checkRange(String ClientID, String[] Fromto) {
        //To change body of generated methods, choose Tools | Templates.
        if(Fromto[0]==null)return true;
            for(String retValue: Fromto)
            {
                if(retValue==null)return false;
                 System.out.println("Compairing "+retValue+" with "+ClientID);
                if(retValue.compareToIgnoreCase(ClientID)==0)return true;
            }
            return false;
    }
    
}
