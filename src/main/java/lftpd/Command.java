package lftpd;

public class Command implements ICommand{
    protected String cmd;
    protected String param;
    protected CommandStatus cmdStatus = CommandStatus.CMD_NOT_RUN;
    protected SessionState session;
    
    public Command(String cmd, String param, SessionState session){
        this.cmd = cmd;
        this.param = param;
        this.session = session;
    }
    
    @Override
    public boolean isNetwork(){
        if(this.cmd.equals("PORT") || this.cmd.equals("PASV") || this.cmd.equals("EPRT") || this.cmd.equals("EPSV")){
            return true;
        }else{
            return false;
        }
    }
    
    @Override
    public boolean isData(){
        if(this.cmd.equals("LIST")){
            return true;
        }else{
            return false;
        }
    }
    
    @Override
    public String getCommand(){
        return this.cmd;
    }
    
    @Override
    public String getParameter(){
        return this.param;
    }
    
    @Override
    public CommandStatus getStatus(){
        return CommandStatus.CMD_WRONG_CMD;
    }
    
    @Override
    public String getResponse(){
        String str = "202 command: " + this.cmd + " is not supported" ;
        return str;
    }
    
    @Override
    public SessionState getSessionState(){
        return session;
    }
}