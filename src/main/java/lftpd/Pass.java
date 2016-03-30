package lftpd;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Pass extends Command implements ICommand{
    private Config cfg = Config.getInstance();
    
    private String userName;
    
    public Pass(String cmd, String param, SessionState session){
        super(cmd,param, session);
    }
    
    @Override
    public String getResponse(){
        String str ;
        if(cmdStatus == CommandStatus.CMD_OK){
            str = "230 Login successful";
        }else{
            str = "430 Invalid user name or password";
        }
        return str;
    }
    
    @Override
    public CommandStatus getStatus(){
        return cmdStatus;
    }
    
    public void authorize(String userName){
        String str = cfg.getValue(userName,"users");
        Pattern ptr = Pattern.compile("([^:]+):([^:]+):([^:]+)");
        
        Matcher m = ptr.matcher(str);
        
        if(m.find()){
            String configPassword = m.group(1);
            cmdStatus = CommandStatus.CMD_ERR;
            if(configPassword.equals(param)){
                cmdStatus = CommandStatus.CMD_OK;
            }
        }else{
            cmdStatus = CommandStatus.CMD_ERR;
        }
    }

}