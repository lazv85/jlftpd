package lftpd;

public class User extends Command implements ICommand{
    private Config cfg = Config.getInstance();
    
    public User(String cmd, String param, SessionState session){
        super(cmd, param,session);
        
        String user = cfg.getValue(param, "users");
        String anonAccess = cfg.getValue("anonymous_access","system");

        if(user == null && anonAccess.equals("yes")){
            cmdStatus = CommandStatus.CMD_OK;
        }else{
            cmdStatus = CommandStatus.CMD_ERR;
        }
        
        
    }
    
    @Override
    public String getResponse(){
        String str = new String();
        
        if(cmdStatus == CommandStatus.CMD_OK){
            str = "230 Login successful";
        }else if(cmdStatus == CommandStatus.CMD_ERR){
            str = "331 user name is correct, password is required for " + param;
        }
        return str;
    }
    
    @Override
    public CommandStatus getStatus(){
        return cmdStatus;
    }
}