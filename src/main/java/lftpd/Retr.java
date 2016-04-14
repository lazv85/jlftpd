package lftpd;

import java.net.Socket;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;


public class Retr extends Command implements ICommand, IData{
    public Retr(String cmd, String param, SessionState session){
        super(cmd, param, session);
        responseCode = ResponseCode.CODE_150_File_status_okay;
        if(param == null){
            responseCode = ResponseCode.CODE_551_Requested_action_aborted;
        }
    }
    
    @Override
    public String getResponse(){
        if(responseCode == ResponseCode.CODE_226_Closing_data_connection){
            return "226 File sent OK";
        }else if(responseCode == ResponseCode.CODE_425_Cant_open_data_connection){
            return "425 No data connection";
        }else if(responseCode == ResponseCode.CODE_426_Connection_closed_aborted){
            return "426 Data connection failure during file transmission";
        }else if(responseCode == ResponseCode.CODE_451_Requested_action_aborted){
            return "451 Can not read file";
        } else if (responseCode == ResponseCode.CODE_551_Requested_action_aborted){
            return "551 file name cannot be empty";
        }else if(responseCode == ResponseCode.CODE_150_File_status_okay){
            return "150 Here comes the file";
        }
        return null;
    }
    
    private void sendFile(OutputStream os) {
        Path p = Paths.get(session.getCurrentDir() + "/" + param).normalize();
        File fd = new File(p.toString());
        if(Files.exists(p) && !fd.isDirectory()){
            try{
                FileInputStream f = new FileInputStream(p.toString());
                int c;
    
                while ((c = f.read()) != -1) {
                    os.write(c);
                }
                responseCode = ResponseCode.CODE_226_Closing_data_connection;
            }catch(IOException e){
                responseCode = ResponseCode.CODE_451_Requested_action_aborted;
            }
        }else{
            responseCode = ResponseCode.CODE_451_Requested_action_aborted;
        }
    }
    
    @Override
    public void transferData(Socket sock) throws IOException{

        if(responseCode != ResponseCode.CODE_551_Requested_action_aborted){

            try{
                OutputStream pw = sock.getOutputStream();
                sendFile(pw);
                
            }catch(Exception e){
                responseCode = ResponseCode.CODE_426_Connection_closed_aborted;
            }
        }
    }
}