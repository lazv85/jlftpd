package lftpd;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Pass extends Command implements ICommand{
    private Config cfg = Config.getInstance();
    
    private String userName;
    
    public Pass(String cmd, String param, SessionState session){
        super(cmd,param, session);
    }
    
    @Override
    public String getResponse(){
        String str ;
        if(responseCode == ResponseCode.CODE_230_User_logged_in){
            str = "230 Login successful";
        }else{
            str = "430 Invalid user name or password";
        }
        return str;
    }
   
    public void authorize(String userName){
        String str = cfg.getValue(userName,"users");
        Pattern ptr = Pattern.compile("([^:]+):([^:]+):([^:]+)");
        
        Matcher m = ptr.matcher(str);
        
        if(m.find()){
            String configPassword = m.group(1);
            responseCode = ResponseCode.CODE_430_Invalid_username_or_password;
            if(configPassword.equals(param)){
                responseCode = ResponseCode.CODE_230_User_logged_in;
            }
        }else{
            responseCode = ResponseCode.CODE_430_Invalid_username_or_password;
        }
    }

}