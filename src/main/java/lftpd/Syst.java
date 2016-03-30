package lftpd;

public class Syst extends Command implements ICommand{
    public Syst(String cmd, String param, SessionState session){
        super(cmd, param, session);
    }
    
    @Override
    public String getResponse(){
        String str = "215 " + System.getProperty("os.name");
        return str;
    }
    
    @Override
    public CommandStatus getStatus(){
        return CommandStatus.CMD_OK;
    }
}