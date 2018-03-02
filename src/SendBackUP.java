
import java.util.Timer;
import java.util.TimerTask;




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class SendBackUP {
    Timer timer;
    public int miliSecond=5;
    public RemindTask remindTask;
    public ClientClass cc;
    public String Dir;
    public SendBackUP(ClientClass c,String dir)
    {
        this.cc=c;
        this.Dir=dir;
        timer = new Timer();
        timer.scheduleAtFixedRate(new RemindTask(), miliSecond*1000, miliSecond*3000);
        System.out.println("schedule time to send file");
    }

    class RemindTask extends TimerTask {

        public void run() {
            System.out.println(Dir);
            //System.out.format("Time's up! %n");
            cc.sendFile(Dir);
            //timer.cancel();
        }
    }
    
}
