package lftpd;

public class Pwd extends Command implements ICommand{
    public Pwd(String cmd, String param, SessionState session){
        super(cmd,param,session);
    }

    
    @Override
    public String getResponse(){
        String rootDir = session.getRootDir();
        String currentDir = session.getCurrentDir();
        String path;
        
        if(currentDir.substring(rootDir.length() ).length() == 0){
            path = "/";
        }else{
            path = currentDir.substring(rootDir.length() );
        }
        
        return "257 " +"\"" + path + "\"";
    }
    
}