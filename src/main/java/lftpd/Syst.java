package lftpd;

public class Syst extends Command implements ICommand{
    public Syst(String cmd, String param){
        super(cmd, param);
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