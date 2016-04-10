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
        
    }
    
    @Override
    public Socket getSocket(){
        return null;
    }
    
    @Override
    public String getResponse(){
        return null;
    }
}