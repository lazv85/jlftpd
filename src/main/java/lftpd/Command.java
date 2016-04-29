package lftpd;

public class Command implements ICommand{
    protected String cmd;
    protected String param;
    protected SessionState session;
    protected int responseCode = ResponseCode.CODE_202_Command_not_implemented;
    
    public Command(String cmd, String param, SessionState session){
        this.cmd = cmd;
        this.param = param;
        this.session = session;
    }
    
    @Override
    public boolean isNetwork(){
        if(this.cmd == null) return false;
        
        if(this.cmd.equals("PORT") || this.cmd.equals("PASV") || this.cmd.equals("EPRT") || this.cmd.equals("EPSV")){
            return true;
        }else{
            return false;
        }
    }
    
    @Override
    public boolean isData(){
        if(this.cmd == null) return false;
        
        if(this.cmd.equals("LIST") || this.cmd.equals("RETR") ){
            return true;
        }else{
            return false;
        }
    }
    
    @Override
    public String getCommand(){
        if(this.cmd == null) return "  ";
        
        return this.cmd;
    }
    
    @Override
    public String getParameter(){
        return this.param;
    }
    
    @Override
    public String getResponse(){
        String outCmd = " ";
        if(cmd != null) outCmd = this.cmd;
        
        String str = "202 command: " + outCmd + " is not implemented" ;
        return str;
    }
    
    @Override
    public SessionState getSessionState(){
        return session;
    }
    
    @Override
    public int getResponseCode(){
        return responseCode;
    }
}