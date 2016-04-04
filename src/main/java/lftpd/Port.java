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
            responseCode = ResponseCode.CODE_225_Data_connection_open;
        }else{
            responseCode = ResponseCode.CODE_501_Syntax_error_in_parameters;
        }
    }
    
    @Override
    public String getResponse(){
        if(responseCode == ResponseCode.CODE_225_Data_connection_open){
            return "225 Data connection open; no transfer in progress.";
        }else if (responseCode == ResponseCode.CODE_425_Cant_open_data_connection){
            return "425 Can't open data connection.";
        }else if (responseCode == ResponseCode.CODE_501_Syntax_error_in_parameters){
            return "501 Syntax error in command parameters";
        }else{
            return  "425 Can't open data connection.";
        }
    }

    
    @Override
    public Socket getSocket() throws IOException, UnknownHostException{
        if(responseCode == ResponseCode.CODE_501_Syntax_error_in_parameters){
            return null;
        }
        Socket sock = null;
        
        try {
         sock = new Socket( InetAddress.getByName(remoteIpAddr) , remotePort);
         responseCode = ResponseCode.CODE_225_Data_connection_open;    
        } catch(Exception e) {
         responseCode = ResponseCode.CODE_425_Cant_open_data_connection;    
        }
        
        return sock;
    }
}