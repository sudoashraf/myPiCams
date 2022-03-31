package in.ashrafsiddiqui.picams;

import android.util.Log;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.util.Properties;

public class SSHCommand {

    public static Session testConn(String uname, String pass, String ip){
        try{
            JSch jsch = new JSch();
            Session session = jsch.getSession(uname, ip, 22);
            session.setPassword(pass);
            // Avoid asking for key confirmation
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.connect();
            session.disconnect();
            return session;
        } catch (Exception e){
            Log.e("testCam exception: ", e.getMessage());
            return null;
        }
    }

    public static boolean accessCam(String uname, String pass, String ip, int mode){
        try{
            JSch jsch = new JSch();
            Session session = jsch.getSession(uname, ip, 22);
            session.setPassword(pass);
            // Avoid asking for key confirmation
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.connect();
            // SSH Channel
            ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
            // Execute command
            if(mode == 1) channelssh.setCommand("python3 camerastream.py");
            else channelssh.setCommand("sudo pkill python3");
            channelssh.connect();
            channelssh.disconnect();
            session.disconnect();
            return true;
        } catch (Exception e){
            Log.e("accessCam exception: ", e.getMessage());
            return false;
        }
    }
}
