package lftpd;

public class Port extends Command implements ICommand{
    
    public Port(String cmd, String param, SessionState session){
        super(cmd,param,session);
    }
    
    @Override
    public String getResponse(){
        
    }
    
    @Override
    public CommandStatus getStatus(){
        
    }
}