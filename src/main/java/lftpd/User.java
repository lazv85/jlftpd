package lftpd;

public class User extends Command implements ICommand{
    private Config cfg = Config.getInstance();
    
    public User(String cmd, String param, SessionState session){
        super(cmd, param,session);
        
        String user = cfg.getValue(param, "users");
        String anonAccess = cfg.getValue("anonymous_access","system");

        if(user == null && anonAccess.equals("yes")){
            responseCode = ResponseCode.CODE_230_User_logged_in;
        }else{
            responseCode = ResponseCode.CODE_331_User_name_ok_need_password;
        }
        
        
    }
    
    @Override
    public String getResponse(){
        String str = new String();
        
        if(responseCode == ResponseCode.CODE_230_User_logged_in){
            str = "230 Login successful";
        }else if(responseCode == ResponseCode.CODE_331_User_name_ok_need_password){
            str = "331 user name is correct, password is required for " + param;
        }
        return str;
    }

}