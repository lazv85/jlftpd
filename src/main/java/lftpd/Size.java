package lftpd;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.File;

public class Size extends Command implements ICommand{
    
    private long fileSize ;
    
    public Size(String cmd, String param, SessionState session){
        super(cmd, param, session);
        
        Path filePath = Paths.get(session.getCurrentDir() + "/" + param).normalize();
        
        if(Files.exists(filePath)){
            File f = new File(filePath.toString());
            fileSize = f.length();
            f = null;
            responseCode = ResponseCode.CODE_213_File_status;
        }else{
            responseCode = ResponseCode.CODE_550_Requested_action_not_taken;
        }
    }
    
    @Override
    public String getResponse(){
        String str = null;
        
        if(responseCode == ResponseCode.CODE_213_File_status){
            str = "213 " + String.valueOf(fileSize) ;
        }else{
            str = "550 file: " + param + " not found";
        }
        
        return str;
    }
}