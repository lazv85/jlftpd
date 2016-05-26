package lftpd;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

public class Rmd extends Command implements ICommand{
    
    private String dirName;
    
    public Rmd(String cmd, String param, SessionState session){
        super(cmd, param, session);
        
        dirName = param;
        
        if(param == null){
            responseCode = ResponseCode.CODE_501_Syntax_error_in_parameters;
            return;
        }
        
        Path p =  Paths.get(session.getCurrentDir() + "/" + param).normalize() ;
        
        if(! Files.exists(p)){
            responseCode = ResponseCode.CODE_452_Requested_action_not_taken;
            return;
        }
        
        if(! Files.isDirectory(p)){
            responseCode = ResponseCode.CODE_452_Requested_action_not_taken;
            return;
        }
        
        try{
            Files.delete(p);
            responseCode = ResponseCode.CODE_257_PATHNAME_created;
        } catch(IOException e){
            responseCode = ResponseCode.CODE_452_Requested_action_not_taken ;
        }
        
    }
    
    @Override
    public String getResponse(){
        if(responseCode == ResponseCode.CODE_501_Syntax_error_in_parameters){
            return "501 directory name cannot be null";
        }
        
        if(responseCode == ResponseCode.CODE_452_Requested_action_not_taken){
            return "452 directory does not exist";
        }
        
        return "257 " + dirName + " successfully created";
    }
    
}