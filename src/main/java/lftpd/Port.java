package lftpd;

import java.net.Socket;
import java.net.InetAddress;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.net.UnknownHostException;
import java.io.IOException;

public class Port extends Command implements ICommand, INetwork{
    
    private String remoteIpAddr;
    private int remotePort;
    
    public Port(String cmd, String param, SessionState session){
        super(cmd,param,session);
        
        Pattern ptr = Pattern.compile("(\\d+,\\d+,\\d+,\\d+),(\\d+),(\\d+)");
        Matcher m = ptr.matcher(param);
        
        if(m.find()){
            remoteIpAddr = m.group(1).replace(',','.');
            remotePort = Integer.parseInt(m.group(2))*256 + Integer.parseInt(m.group(3));
            cmdStatus = CommandStatus.CMD_NOT_RUN;
        }else{
            cmdStatus = CommandStatus.CMD_ERR;
        }
    }
    
    @Override
    public String getResponse(){
        if(cmdStatus == CommandStatus.CMD_OK){
            return "225 Data connection open; no transfer in progress.";
        }else if (cmdStatus == CommandStatus.CMD_ERR){
            return "425 Can't open data connection.";
        }else if (cmdStatus == CommandStatus.CMD_NOT_RUN){
            return "425 Can't open data connection.";
        }else{
            return  "425 Can't open data connection.";
        }
    }
    
    @Override
    public CommandStatus getStatus(){
        return cmdStatus;
    }
    
    @Override
    public Socket getSocket() throws IOException, UnknownHostException{
        if(cmdStatus == CommandStatus.CMD_ERR){
            return null;
        }
        
        Socket sock = new Socket( InetAddress.getByName(remoteIpAddr) , remotePort);
        cmdStatus = CommandStatus.CMD_OK;
        
        return sock;
    }
}