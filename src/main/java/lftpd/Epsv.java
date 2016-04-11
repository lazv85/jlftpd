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

public class Epsv extends Command implements ICommand, INetwork{
    
    private ServerSocket serverSocket;
    private int port;
    
    public Epsv(String cmd, String param, SessionState session){
        super(cmd, param, session);
        serverSocket = null;
        do{
            port = 1024 + (int)(Math.random() * 65534);
            try{
                serverSocket = new ServerSocket(port);
                responseCode = ResponseCode.CODE_229_Entering_Extended_Passive_Mode;
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
        
        if(responseCode == ResponseCode.CODE_229_Entering_Extended_Passive_Mode){
            String strPort = String.valueOf(port) ;
            str = "229 Entering passive mode (|||" + strPort + "|)" ;

        }else{
            str = "421 Service not available";
        }
        
        return str;
    }
    
}