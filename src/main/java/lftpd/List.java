package lftpd;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import java.text.SimpleDateFormat;


public class List extends Command implements ICommand, IData{
    
    public List(String cmd, String param, SessionState session){
        super(cmd, param, session);
        responseCode = ResponseCode.CODE_150_File_status_okay;
    }
    
    @Override
    public String getResponse(){
        if(responseCode == ResponseCode.CODE_226_Closing_data_connection){
            return "226 Directory send OK";
        }else if(responseCode == ResponseCode.CODE_451_Requested_action_aborted) {
            return "451 File or Directory is not available.";
        }else if(responseCode == ResponseCode.CODE_426_Connection_closed_aborted){
            return "426 TCP connection has been broken.";
        }else if(responseCode == ResponseCode.CODE_150_File_status_okay){
             return "150 Here comes the directory listing";
        }
        return null;
    }
    

    private String listFiles(){

        String str = "";
        try{
            File f = new File(session.getCurrentDir());
            
            if(f.isDirectory()){
                String []content = f.list();
                for(String i : content){
                    Path p = Paths.get(session.getCurrentDir() + "/" + i);
                    PosixFileAttributes attrs = Files.readAttributes(p,PosixFileAttributes.class);
                    Set<PosixFilePermission> posixPermissions = Files.getPosixFilePermissions(p);

                    String owner = attrs.owner().getName();
                    String group = attrs.group().getName();
                    String perms = PosixFilePermissions.toString(posixPermissions);
                    
                    File cf = new File(p.toString());
                    
                    if(cf.isDirectory()){
                        perms = "d" + perms;
                    }else{
                        perms = "-" + perms;
                    }
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd HH:mm");
                    String linkCount = Files.getAttribute(p, "unix:nlink").toString();
                    
                    String item ;
                    
                    item = perms + " ";
                    item += String.format("%4d", Integer.parseInt(linkCount)) + " ";
                    item += owner + " ";
                    item += group + " ";
                    item += String.format("%12d", cf.length())  + " ";
                    item += sdf.format(cf.lastModified()) + " ";
                    item += i + " ";

                    str += item + "\r\n" ;
                }
            }
        }catch(Exception e){
            responseCode = ResponseCode.CODE_451_Requested_action_aborted;
        }
        return str;
    }
    
    @Override
    public void transferData(Socket sock) throws IOException{
        try{
            PrintWriter pw = new PrintWriter(sock.getOutputStream(), true);
            
            String content = listFiles();
            
            pw.printf(content);
            responseCode = ResponseCode.CODE_226_Closing_data_connection;
        }catch(Exception e){
            responseCode = ResponseCode.CODE_426_Connection_closed_aborted;
        }
    }
   
}