package lftpd;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

class CommandFactory{
    
    private static String[] getCommandDetails(String commandText){
        Pattern ptr = Pattern.compile("([a-zA-Z]+)(\\s+([a-zA-Z0-9,]+))?");
        
        Matcher m = ptr.matcher(commandText);
        
        if(m.find()){
            String []cmd = new String[2] ;
            cmd[0] = m.group(1) ;
            cmd[1] = m.group(2) ;
            return cmd;
        }
        return null;
    }
    
    public static ICommand getCommand(String commandText){
        String []cmd = getCommandDetails(commandText);
        
        if(cmd.length == 0){
            return new Command(commandText,null);
        }
        
        if(cmd[0].equals("USER")){
            return new User(cmd[0],cmd[1]);
        }
        
        return new Command(commandText,null);
    }
}