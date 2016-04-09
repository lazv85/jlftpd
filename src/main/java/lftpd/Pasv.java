package lftpd;

import java.net.Socket;
import java.net.InetAddress;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.net.UnknownHostException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Pasv extends Command implements ICommand, INetwork{
    private int p1;
    private int p2;
    private ServerSocket serverSocket;
    
    public Pasv(String cmd, String param, SessionState session){
        super(cmd, param,session);
        serverSocket = null;
        
        do{
            p1 = 4 + (int)(Math.random() * 100);
            p2 = 4 + (int)(Math.random() * 100);
            try{
                serverSocket = new ServerSocket(p1*256+p2);
                responseCode = ResponseCode.CODE_227_Entering_Passive_Mode;
            }catch(Exception e){
                serverSocket = null;
                responseCode = ResponseCode.CODE_421_Service_not_available;
            }
        }while(serverSocket == null);
        
    }
    
    @Override
    public Socket getSocket() throws IOException, UnknownHostException{
        serverSocket.setSoTimeout(5000);
        Socket clientSocket = null;
        
        try{
            clientSocket = serverSocket.accept();
        }catch(Exception e){
            clientSocket = null;
        }
        
        serverSocket.close();
        
        return clientSocket;
    }
    
    @Override
    public String getResponse(){
        String str = null;
        
        if(responseCode == ResponseCode.CODE_227_Entering_Passive_Mode){
            String host = session.getLocalAddress().replace('.',',') + "," + String.valueOf(p1) + "," + String.valueOf(p2) ;
            str = "227 Entering passive mode (" + host + ")" ;

        }else{
            str = "421 Service not available";
        }
        
        return str;
    }
}