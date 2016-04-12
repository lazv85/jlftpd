package lftpd;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.File;
import java.text.SimpleDateFormat;

public class Mdtm extends Command implements ICommand{
    
    String modificationTime;
    
    public Mdtm(String cmd, String param, SessionState session){
        super(cmd,param, session);
        
        Path filePath = param != null ? 
            Paths.get(session.getCurrentDir() + "/" + param).normalize() :
            Paths.get(session.getCurrentDir() ).normalize();
            
        if(Files.exists(filePath)){
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHMM");
            File f = new File(filePath.toString());
            
            modificationTime = sdf.format(f.lastModified());
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
            str = "213 " + modificationTime;
        }else{
            str = "550 file: " + param + " not found";
        }
        return str;
    }
}