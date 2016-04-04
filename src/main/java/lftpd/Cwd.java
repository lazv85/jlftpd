package lftpd;

public class Cwd extends Command implements ICommand{
    public Cwd(String cmd, String param, SessionState session){
        super(cmd, param, session);
        
    }
    
    @Override
    public String getResponse(){
        return null;
    }
   
}