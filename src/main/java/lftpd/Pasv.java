package lftpd;

import java.net.Socket;
import java.net.InetAddress;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.net.UnknownHostException;
import java.io.IOException;

public class Pasv extends Command implements ICommand, INetwork{
    
    public Pasv(String cmd, String param, SessionState session){
        super(cmd, param,session);
    }
    
    @Override
    public Socket getSocket() throws IOException, UnknownHostException{
        return null;
    }
    
    @Override
    public String getResponse(){
        return null;
    }
}