package lftpd;

import java.net.Socket;
import java.net.InetAddress;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.net.UnknownHostException;
import java.io.IOException;

public class Eprt extends Command implements ICommand, INetwork{
    private String remoteIpAddr;
    private int remotePort;
    private Socket sock;
    
    public Eprt(String cmd, String param, SessionState session){
        super(cmd, param, session);
        
        if(param != null){
            Pattern ptr = Pattern.compile("\\|([12])\\|([\\:0-9]+)\\|(\\d+)\\|");
            Matcher m = ptr.matcher(param);
            if(m.find()){
                System.out.println("m group " + m.group(2));
                remoteIpAddr = m.group(2);
                remotePort = Integer.parseInt(m.group(3));
                try {
                 sock = new Socket( InetAddress.getByName(remoteIpAddr) , remotePort);
                 responseCode = ResponseCode.CODE_200_Action_successfully_completed;    
                } catch(Exception e) {
                
                    
                 responseCode = ResponseCode.CODE_425_Cant_open_data_connection;    
                }
                
            }else{
                responseCode =  ResponseCode.CODE_501_Syntax_error_in_parameters;
            }
        }else{
            responseCode =  ResponseCode.CODE_501_Syntax_error_in_parameters;
        }
    }
    
    @Override
    public String getResponse(){
        if(responseCode == ResponseCode.CODE_200_Action_successfully_completed){
            return "200 Eprt command successful.";
        }else if (responseCode == ResponseCode.CODE_425_Cant_open_data_connection){
            return "425 Can't open data connection.";
        }else if (responseCode == ResponseCode.CODE_501_Syntax_error_in_parameters){
            return "501 Syntax error in command parameters";
        }else{
            return  "425 Can't open data connection.";
        }
    }
    
    @Override
    public Socket getSocket(){
        if(responseCode == ResponseCode.CODE_501_Syntax_error_in_parameters){
            return null;
        }
        
        return sock;
    }
}