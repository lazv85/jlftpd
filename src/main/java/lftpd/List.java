package lftpd;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class List extends Command implements ICommand, IData{
    
    public List(String cmd, String param, SessionState session){
        super(cmd, param, session);
    }
    
    /*
    errors:
        "425 No TCP connection to transfer data."
        "426 TCP connection has been broken."
        "451 File or Directory is not available."
    */
    
    @Override
    public String getResponse(){
        if(cmdStatus == CommandStatus.CMD_OK){
            return "226 Directory or File information successfully transmitted.";
        }else if(cmdStatus == CommandStatus.CMD_ERR) {
            return "451 File or Directory is not available.";
        }
        return "426 TCP connection has been broken.";
    }
    
    @Override
    public CommandStatus getStatus(){
        return cmdStatus;
    }
    
    @Override
    public void transferData(Socket sock) throws IOException{
        PrintWriter pw = new PrintWriter(sock.getOutputStream(), true);
        pw.println("new file");
    }
}