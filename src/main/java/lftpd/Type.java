package lftpd;

public class Type extends Command implements ICommand{
    public Type(String cmd, String param, SessionState session){
        super(cmd,param,session);
        if(param.equals("A") ||
            param.equals("I") )
        {
            session.setTypeFile(param);
            responseCode = ResponseCode.CODE_200_Action_successfully_completed;
        }else
        {
            responseCode = ResponseCode.CODE_501_Syntax_error_in_parameters;
        }
    }
    
    @Override
    public String getResponse(){
        String str ;
        if(responseCode == ResponseCode.CODE_200_Action_successfully_completed){
            str = "200 TYPE set to " + session.getTypeFile();
        }else{
            str = "501 Unsupported parameters for Type command: " + session.getTypeFile();
        }
        return str;
    }
}