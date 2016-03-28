package lftpd;

public class Command implements ICommand{
    private String cmd;
    private String param;
    
    public Command(String cmd, String param){
        this.cmd = cmd;
        this.param = param;
    }
    
    @Override
    public String getCommand(){
        return this.cmd;
    }
    
    @Override
    public String getParameter(){
        return this.param;
    }
}