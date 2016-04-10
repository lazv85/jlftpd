package lftpd;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Cwd extends Command implements ICommand{
    public Cwd(String cmd, String param, SessionState session){
        super(cmd, param, session);
        
        Path newPath ;

        if(!param.substring(0,1).equals("/")){
            newPath = Paths.get(session.getCurrentDir() + "/" + param);
        }else{
            newPath = Paths.get(session.getRootDir() + "/" + param);
        }
        
        if(Files.exists(newPath) && Files.isDirectory(newPath)){
            String p = newPath.normalize().toString();
            if(p.length() >= session.getRootDir().length()){
                session.setCurrentDir(p);
                responseCode = ResponseCode.CODE_250_Requested_file_action_okay;
            }else{
                responseCode = ResponseCode.CODE_550_Requested_action_not_taken;  
            }
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