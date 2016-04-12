package lftpd;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

class CommandFactory{
    
    private static String[] getCommandDetails(String commandText){
        Pattern ptr = Pattern.compile("([a-zA-Z]+)(\\s+([^\\r^\\n]+))?");
        
        Matcher m = ptr.matcher(commandText);
        
        if(m.find()){
            String []cmd = new String[2] ;
            cmd[0] = m.group(1) ;
            cmd[1] = m.group(3) ;
            return cmd;
        }
        return null;
    }
    
    public static ICommand getCommand(String commandText){
        return getCommand(commandText, new SessionState());
    }
    
    public static ICommand getCommand(String commandText, SessionState session){
        String []cmd = getCommandDetails(commandText);
        
        if(cmd == null){
            return new Command(" ",null, session);
        }
        
        System.out.println("cmd = " + cmd[0]);
        System.out.println("prm = " + cmd[1]);
        System.out.println("localIp = " + session.getLocalAddress());
        System.out.println("remoteIp = " + session.getRemoteAddress());
        System.out.println();
        
        if(cmd[0].equals("USER")){
            return new User(cmd[0],cmd[1], session);
        }
        
        if(cmd[0].equals("PASS")){
            return new Pass(cmd[0],cmd[1], session);
        }
        
        if(cmd[0].equals("SYST")){
            return new Syst(cmd[0],cmd[1], session);
        }
        
        if(cmd[0].equals("PWD")){
            return new Pwd(cmd[0],cmd[1],session);
        }
        
        if(cmd[0].equals("PORT")){
            return new Port(cmd[0],cmd[1],session);
        }
        
        if(cmd[0].equals("LIST")){
            return new List(cmd[0],cmd[1],session);
        }
        
        if(cmd[0].equals("CWD")){
            return new Cwd(cmd[0],cmd[1],session);
        }
        
        if(cmd[0].equals("TYPE")){
            return new Type(cmd[0],cmd[1],session);
        }
        
        if(cmd[0].equals("SIZE")){
            return new Size(cmd[0],cmd[1],session);
        }
        
        if(cmd[0].equals("PASV")){
            return new Pasv(cmd[0],cmd[1],session);
        }
        
        if(cmd[0].equals("EPRT")){
            return new Eprt(cmd[0],cmd[1],session);
        }
        
        if(cmd[0].equals("EPSV")){
            return new Epsv(cmd[0],cmd[1],session);
        }
        
        if(cmd[0].equals("MDTM")){
            return new Mdtm(cmd[0],cmd[1],session);
        }
        return new Command(commandText,null, session);
    }
}