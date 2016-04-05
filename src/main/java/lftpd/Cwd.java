package lftpd;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Cwd extends Command implements ICommand{
    public Cwd(String cmd, String param, SessionState session){
        super(cmd, param, session);
        
        Path newPath = Paths.get(session.getCurrentDir() + "/" + param);
        
        if(Files.exists(newPath) && Files.isDirectory(newPath)){
            responseCode = ResponseCode.CODE_250_Requested_file_action_okay;
            session.setCurrentDir(newPath.normalize().toString());
        }else{
            responseCode = ResponseCode.CODE_550_Requested_action_not_taken;   
        }
    }
    
    @Override
    public String getResponse(){
        if(responseCode == ResponseCode.CODE_250_Requested_file_action_okay){
            return "250 Directory successfully changed";
        }else{
            return "550 failed to change directory";
        }
    }
    
    
   
}