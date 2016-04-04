package lftpd;

public class Pwd extends Command implements ICommand{
    public Pwd(String cmd, String param, SessionState session){
        super(cmd,param,session);
    }
    
    @Override
    public String getResponse(){
        return "257 " +"\"" + session.getCurrentDir() + "\"";
    }
    
}