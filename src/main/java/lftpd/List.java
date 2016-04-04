package lftpd;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.File;

public class List extends Command implements ICommand, IData{
    
    public List(String cmd, String param, SessionState session){
        super(cmd, param, session);
        responseCode = ResponseCode.CODE_150_File_status_okay;
    }
    
    @Override
    public String getResponse(){
        if(responseCode == ResponseCode.CODE_226_Closing_data_connection){
            return "226 Directory or File information successfully transmitted.";
        }else if(responseCode == ResponseCode.CODE_451_Requested_action_aborted) {
            return "451 File or Directory is not available.";
        }else if(responseCode == ResponseCode.CODE_426_Connection_closed_aborted){
            return "426 TCP connection has been broken.";
        }else if(responseCode == ResponseCode.CODE_150_File_status_okay){
             return "150 Here comes info";
        }
        return null;
    }
    

    private String listFiles(){

        String str = "";
        try{
            File f = new File(session.getCurrentDir());
            
            if(f.isDirectory()){
                String []content = f.list();
                for(String i : content){
                    str += i + "\r\n" ;
                }
            }
        }catch(Exception e){
            responseCode = ResponseCode.CODE_451_Requested_action_aborted;
        }
        return str;
    }
    
    @Override
    public void transferData(Socket sock) throws IOException{
        try{
            PrintWriter pw = new PrintWriter(sock.getOutputStream(), true);
            
            String content = listFiles();
            
            pw.printf(content);
            responseCode = ResponseCode.CODE_226_Closing_data_connection;
        }catch(Exception e){
            responseCode = ResponseCode.CODE_426_Connection_closed_aborted;
        }
    }
   
}